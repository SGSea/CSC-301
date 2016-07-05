package project4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	
    private final MinPQ<SearchNode> boardPQ; // priority queue for A* algorithm
    private final Stack<Board> solution; // stack of solution boards
    private final int moves; // number of moves required to solve the board
    private SearchNode currentNode; // current search node of the Solver object
    
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board; // game board of the search node
        private final SearchNode previous; // pointer to the parent search node
        private final int moves; // number of moves used to reach this search node
        
        // constructor for the search node object
        private SearchNode(Board initial, SearchNode previous, int moves)  {
            this.board = initial;
            this.previous = previous;
            this.moves = moves;
        } 
        
        // compareTo method for the Comparable interface
        public int compareTo(SearchNode that) {
            int thisPriority = this.board.manhattan() + this.moves;
            int thatPriority = that.board.manhattan() + that.moves;
            if (thisPriority < thatPriority) return -1;
            else if (thisPriority > thatPriority) return 1;
            else return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // throw an IllegalArgumentException if the board is unsolvable
        if (!initial.isSolvable()) throw new java.lang.IllegalArgumentException();
        
        // initialize instance variables
        boardPQ = new MinPQ<SearchNode>();
        solution = new Stack<Board>();
        currentNode = new SearchNode(initial, null, 0);
        
        // insert initial search node into the priority queue
        boardPQ.insert(currentNode);
        
        // while the current search node isn't the goal board, delete the
        // smallest priority board from the priority queue and insert
        // its neighbors into the priority queue 
        while (!currentNode.board.isGoal()) {
            currentNode = boardPQ.delMin();
            for (Board b : currentNode.board.neighbors()) {
                
                // only insert a new search node if it's different from the
                // previous search node's board
                if (currentNode.previous == null
                        || !b.equals(currentNode.previous.board)) { 
                    SearchNode nodeToEnqueue
                        = new SearchNode(b, currentNode, currentNode.moves + 1);
                    boardPQ.insert(nodeToEnqueue); 
                }
            }
        } 
        
        moves = currentNode.moves;
    }
    
    // min number of moves to solve initial board
    public int moves() {
        return this.moves;
    }
    
    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        while (currentNode != null) {
            solution.push(currentNode.board);
            currentNode = currentNode.previous;
        }
        
        return solution;
    }
    
    // test client
    public static void main(String[] args)  {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // check if puzzle is solvable; if so, solve it and output solution
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        
        // if not, report unsolvable
        else {
            StdOut.println("Unsolvable puzzle");
        }
        
    }
    
}