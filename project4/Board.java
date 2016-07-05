package project4;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Board {
	private final int[][] blocks; // array to represent the game board
	private final int N; // size of the game board
	private final int hammingDistance; // cached copy of Hamming distance
	private final int manhattanDistance; // cached copy of Manhattan distance
	private final int zeroLocation; // cached copy of the blank space's location


	public Board(int[][] tiles) {
		this.N = tiles.length;
		this.blocks = new int[N][N];
		int blocksOutOfPlace = 0;
		int distance = 0;
		int blankSpace = -1;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				// set the value of the blocks array equal
				this.blocks[i][j] = tiles[i][j];

				// increment counter if block is out of place
				// (for Hamming distance)
				if (blocks[i][j] != (N*i)+j+1) { blocksOutOfPlace++; }

				// calculate the Manhattan distance of the current block
				if (blocks[i][j] != 0) {
					int correctXPosition = (blocks[i][j] - 1) % N;
					int correctYPosition = ((blocks[i][j] - 1) / N);
					distance += Math.abs(i-correctYPosition);
					distance += Math.abs(j-correctXPosition);
				}

				// if the current square is blank, store its location
				if (blocks[i][j] == 0) { 
					blankSpace = (N*i)+j;
				}
			}
		}

		// reduce blocksOutOfPlace by 1 to account for the blank space
		hammingDistance = blocksOutOfPlace - 1;

		// define remaining instance variables
		manhattanDistance = distance;
		zeroLocation = blankSpace;
	}

	public int tileAt(int i, int j)   {
		return blocks[i][j];
	}
	// board size N
	public int size()   {
		return N;
	}
	// number of tiles out of place
	public int hamming()  {
		return hammingDistance;
	}

	// sum of Manhattan distances between tiles and goal
	public int manhattan()  {
		return manhattanDistance;
	}

	// is this board the goal board?
	public boolean isGoal()  {
		if (zeroLocation == N - 1 && hamming() == 0)
			return true;
		return
				false;
	}

	// is this board solvable?
	public boolean isSolvable()   {
		int inversions = 0;

		// count the number of inversions in the game board
		for (int i = 0; i < (N*N); i++){
			for (int j = i; j < (N*N); j++){        
				if ((blocks[convertToRow(i)][convertToCol(i)] 
						> blocks[convertToRow(j)][convertToCol(j)])
						&& blocks[convertToRow(j)][convertToCol(j)] != 0) { 
					inversions++; 
				}
			}
		}

		// if the board size is even
		if (this.size() % 2 == 0){
			return ((convertToRow(zeroLocation) + inversions) % 2 != 0);
		}

		// if the board size is odd
		else {
			return ((inversions % 2) == 0);
		}

	}


	// does this board equal y?
	public boolean equals(Object y) {
		if (y == this)                       return true;
        if (y == null)                       return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        if (that.hammingDistance != this.hammingDistance)     return false;
        if (that.manhattanDistance != this.manhattanDistance) return false;
        if (that.zeroLocation != this.zeroLocation)           return false;
        if (that.size() != this.size())                       return false;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (this.blocks[i][j] != that.blocks[i][j]) { return false; }
            }
        }
        
        return true;
	}
	// all neighboring boards
	public Iterable<Board> neighbors() {
		Stack<Board> stack = new Stack<Board>();
        
        int row = this.convertToRow(this.zeroLocation);
        int col = this.convertToCol(this.zeroLocation);
        
        // corner cases (2 neighbors)
        if (row == 0 && col == 0){
            stack.push(this.createNeighbor(0, 1, 0, 0));
            stack.push(this.createNeighbor(1, 0, 0, 0));
        }
        else if (row == N - 1 && col == 0){
            stack.push(this.createNeighbor(N - 1, 1, N - 1, 0));
            stack.push(this.createNeighbor(N - 2, 0, N - 1, 0));
        }
        else if (row == 0 && col == N - 1){
            stack.push(this.createNeighbor(0, N - 2, 0, N - 1));
            stack.push(this.createNeighbor(1, N - 1, 0, N - 1));
        }
        else if (row == N - 1 && col == N - 1){
            stack.push(this.createNeighbor(N - 2, N - 1, N - 1, N - 1));
            stack.push(this.createNeighbor(N - 1, N - 2, N - 1, N - 1));
        }
        
        // all four edges, but not corners (3 neighbors)
        else if (row == 0){
            stack.push(this.createNeighbor(row, col - 1, row, col));
            stack.push(this.createNeighbor(row, col + 1, row, col));
            stack.push(this.createNeighbor(row + 1, col, row, col));
        }
        
        else if (col == 0){
            stack.push(this.createNeighbor(row - 1, col, row, col));
            stack.push(this.createNeighbor(row + 1, col, row, col));
            stack.push(this.createNeighbor(row, col + 1, row, col)); 
        }
        else if (row == N - 1){
            stack.push(this.createNeighbor(row, col - 1, row, col));
            stack.push(this.createNeighbor(row, col + 1, row, col));
            stack.push(this.createNeighbor(row - 1, col, row, col));
        }
        else if (col == N - 1){
            stack.push(this.createNeighbor(row - 1, col, row, col));
            stack.push(this.createNeighbor(row + 1, col, row, col));
            stack.push(this.createNeighbor(row, col - 1, row, col)); 
        }
        
        // middle (4 neighbors)
        else{
            stack.push(this.createNeighbor(row - 1, col, row, col));
            stack.push(this.createNeighbor(row + 1, col, row, col));
            stack.push(this.createNeighbor(row, col - 1, row, col));
            stack.push(this.createNeighbor(row, col + 1, row, col));
        }
        
        return stack;
	}

	private int convertToCol(int input){
		if (input == -1) { return -1; }
		return ((input) % N);
	}

	private int convertToRow(int input){
		if (input == -1) { return -1; }
		return ((input) / N);
	}

	private Board createNeighbor(int i, int j, int a, int b){
        int[][] newBoard = new int[N][N];
        for (int k = 0; k < N; k++){
            for (int l = 0; l < N; l++){
                newBoard[k][l] = this.blocks[k][l];
            }
        }
        newBoard[a][b] = newBoard[i][j];
        newBoard[i][j] = 0;
        Board toReturn = new Board(newBoard);
        return toReturn;
        
    }
	
	// string representation of this board (in the output format specified below)
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tileAt(i, j)));
			}
			s.append("\n");
		}
		return s.toString();
	}
	// unit testing (required)
	public static void main(String[] args) {
		int[][] blocks = new int[3][3];
        blocks[0][0] = 6;
        blocks[0][1] = 3;
        blocks[0][2] = 8;
        blocks[1][0] = 2;
        blocks[1][1] = 4;
        blocks[1][2] = 0;
        blocks[2][0] = 1;
        blocks[2][1] = 7;
        blocks[2][2] = 5;
        
        int[][] blocks2 = new int[3][3];
        blocks2[0][0] = 6;
        blocks2[0][1] = 3;
        blocks2[0][2] = 8;
        blocks2[1][0] = 4;
        blocks2[1][1] = 2;
        blocks2[1][2] = 0;
        blocks2[2][0] = 1;
        blocks2[2][1] = 7;
        blocks2[2][2] = 5;
        
        // test all the methods of the Board class
        Board test = new Board(blocks);
        Board test2 = new Board(blocks2);
        
        StdOut.println(test);
        StdOut.println("-------------------------");
        StdOut.println("Board under consideration:");
        StdOut.println("Size = " + test.size());
        StdOut.println("Hamming distance = " + test.hamming());
        StdOut.println("Manhattan distance = " + test.manhattan());
        StdOut.println("Is it solved? " + test.isGoal());
        StdOut.println("Is it solvable? " + test.isSolvable());
        StdOut.println("Is \n" + test + " the same as \n" + test2 + " ? "
                               + test.equals(test2));
        StdOut.println("Neighbors of this board are: ");
        
        for (Board b : test.neighbors())
        {
            StdOut.println(b);
        }
    }

}


