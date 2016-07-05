package project5;
/*************************************************************************
 *  Author: Sam Caldwell
 *  Compilation:  javac KdTreeST.java
 *  Execution:    java KdTreeST
 *  Dependencies: Point2D.java, RectHV.java, PointST.java
 *
 *  KdTreeST implements a redblack BST in a 2 dimensional key tree
 *
 *************************************************************************/
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;



public class KdTreeST<Value> {
	
	//Root and size fields.
	private Node root;
	private int size = 0;
	
	//Private inner class for nodes
	private class Node {
        private Point2D key;       // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private boolean horizontal;// orientation of node
        

        public Node(Point2D key, Value val, boolean horizontal) {
            this.key = key;
            this.val = val;
            this.horizontal = horizontal;
        }
    }
	
	// construct an empty symbol table of points  
	public KdTreeST(){
		
	}
	
	// is the symbol table empty? 
	public boolean isEmpty(){
		return size() == 0;
	}
	// number of points    
	public int size(){
		return size;
	}
	
	// associate the value val with point p   
	public void put(Point2D p, Value val){
        if (val == null) return; //do something here???
        root = put(root, p, val, false);
	}
	
	// private recursive put method for inner use
	private Node put(Node x, Point2D key, Value val, boolean hor){
        if (x == null) {
        	size++;
        	return new Node(key, val, hor);
        }
        int cmp = comparePoints(key, x.key, x.horizontal);
        if      (cmp < 0) x.left  = put(x.left,  key, val, !x.horizontal);
        else if (cmp > 0) x.right = put(x.right, key, val, !x.horizontal);
        else              x.val   = val;
        return x;
	}
	
	// private helper method for Point2D comparison. This method compares the x and 
	//y coords of two points, 
	// but usually returns a value(-1,0, or 1) based on only one of the 
	//comparisons (according to the boolean parameter).
	// If the initial compared values are equal, it returns as if the first point is greater UNLESS 
	// both x and y are equal. In that case only does it return 0.
	private int comparePoints(Point2D p1, Point2D p2, boolean orientation){
		
		double result = 0;
		double subResult = 0;
		
		if(orientation) {
			result = p1.y() - p2.y();
			subResult = p1.x() - p2.x();
		} else {
			result = p1.x() - p2.x();
			subResult = p1.y() - p2.y();
		}
		if(result > 0 ) return 1;
		if(result < 0) return -1;
		if(subResult !=0) return 1;
		return 0;
	}
	
	// value associated with point p    
	public Value get(Point2D p){
		return get(root, p);
	}
	
	// private recursive method for get.
	private Value get(Node x,Point2D key){
        if (x == null) return null;
        int cmp = comparePoints(key,x.key,x.horizontal);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
	}
	
	// does the symbol table contain point p? 
	public boolean contains(Point2D p){
		return get(p) != null;
	}
	
	// all points in the symbol table in level-order as specified by the assignment. 
    public Iterable<Point2D> points() {
        Queue<Point2D> pointsQueue = new Queue<>();
        Queue<Node> nodeQueue = new Queue<>();
        nodeQueue.enqueue(root);
        return points(pointsQueue,nodeQueue);
    }

    // private inner method for points.
    private Iterable<Point2D> points(Queue<Point2D> queue, Queue<Node> nodes) {
        while(!nodes.isEmpty()){
        	Node n = nodes.dequeue();
        	queue.enqueue(n.key);
        	if(n.left != null) nodes.enqueue(n.left);
        	if(n.right != null) nodes.enqueue(n.right);
        }
        return queue;
    }
	   
	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect){
		Queue<Point2D> queue = new Queue<>();
		if(rect == null) return queue; 
		range(root, rect, queue, null, null, null, null);
		return queue;
	}	
	
	//private recursive method for range
	private void range(Node node, RectHV rect, Queue<Point2D> q, Double oldXMax, 
			Double oldXMin, Double oldYMax, Double oldYMin){
		
		// if the node is null, return.
		if(node == null) return;
		
		// does the corresponding rectangle of this node intersect the query rectangle?
		boolean intersects = true;
		
		//using double values with null representing infinity, we can represent the corresponding rectangle of
		//this node. Each double represents one of the four boundaries of the axis-aligned rectangle. 
		
		// To detect whether this Node's rectangle intersects the query, we compare the 
		// maximums of one with the minimums of the other on each axis.
		//if it intersects, then the minimums of one must be less than the maximums of the other, etc.
		
		if(oldXMin != null && rect.xmax() < oldXMin) intersects = false;
		if(oldXMax != null && rect.xmin() > oldXMax) intersects = false;
		if(oldYMin != null && rect.ymax() < oldYMin) intersects = false;
		if(oldYMax != null && rect.ymin() > oldYMax) intersects = false;
		
		// If the rectangles intercepts, proceed with search. If not, prune it (do nothing).
		if(intersects) {
			
			//declare and initialize new boundary values.
			Double newXMax = oldXMax;
			Double newXMin = oldXMin;
			Double newYMax = oldYMax;
			Double newYMin = oldYMin;
			
			//if this node is horizontal, change the y values. If not, change x values.
			if(node.horizontal){
				newYMax = node.key.y();
				newYMin = node.key.y();
			} else {
				newXMax = node.key.x();
				newXMin = node.key.x();	
			}
			
			//if this node's point is in the rect, add it to the queue.
			if(rect.contains(node.key)) q.enqueue(node.key);
			
			//submit the children of this node to be evaluated, along with the boundaries for their
			//corresponding rectangles. For left, we submit updated maximums 
			//(but only one is different from the old values) and for the right, 
			// we submit updated minimums.
			range(node.left,rect,q,newXMax,oldXMin,newYMax,oldYMin);
			range(node.right,rect,q,oldXMax,newXMin,oldYMax,newYMin);
		} 
	}
	 
	// a nearest neighbor to point p; null if the symbol table is empty 
	public Point2D nearest(Point2D p){
		if(p == null) throw new NullPointerException("Query point is null");
		if(root == null) return null;
		double champDistance = p.distanceSquaredTo(root.key);
		Node champNode = root;
		champNode = nearest(champDistance,champNode,root,p,null,null,null,null);
		return champNode.key;
	}
	
	//private recursive method for nearest
	private Node nearest(double distance,Node champ, Node node, Point2D query, 
			Double oldXMax, Double oldXMin, Double oldYMax, Double oldYMin){
		
		if(node == null) return null;
		
		// step 1: Is this node closer than the distance to beat?
		double champDistance = distance;
		double thisNodeDistance = query.distanceSquaredTo(node.key);
		Node champNode = champ;
		
		if(thisNodeDistance < distance) {
			champDistance = thisNodeDistance;
			champNode = node;
		}

		// step 2: can anything in this node's rect be closer than the champDistance?
			//what is the closest point to the query that is inside the rect?
				//is that point closer to the query than the champ?
				// If so , then there may be a closer point inside the rect
		
		double testX; //closest possible x value.
		double testY; // closest possible y value
		Point2D testPoint;
		double testDistance;
		
		// if oldXMax isn't infinity (null), and less than the query point's x, 
		// then the max is as close as you can get on the x axis
		if(oldXMax != null && query.x() > oldXMax) testX = oldXMax;
		
		// if oldXMin isn't infinity (null), and it is greater than the query'x x,
		// then this min is as close as you can get on the x axis.
		else if (oldXMin != null && query.x() < oldXMin) testX = oldXMin;
		// if neither of the two cases above are true, then the closest you 
		// can get is the actual x value of the query.
		else testX = query.x();
		
		// Same logic as above but for y values.
		if(oldYMax != null && query.y() > oldYMax) testY = oldYMax;
		else if (oldYMin != null && query.y() < oldYMin) testY = oldYMin;
		else testY = query.y();
		
		//test point with the closest possible values.
		testPoint = new Point2D(testX,testY);  
		
		// distance between closest point possible and the query
		testDistance = query.distanceSquaredTo(testPoint);
		
		// if the testDistance is closer than the champ distance, then proceed. 
		// if not, then prune the search.
		if(testDistance < champDistance){
			
			// the results of searching the left and right children of this node. 
			// We will return the best one later.
			Node result1;
			Node result2;
			
			// step 3: find new values for the next nodes rect:
				//find new boundaries for new method calls. First, declare them and assign them to the old values.
			Double newXMax = oldXMax;
			Double newXMin = oldXMin;
			Double newYMax = oldYMax;
			Double newYMin = oldYMin;
		
			// Next, if this node is horizontal, change the Y value, if not, change the X values. 
			// However, the corresponding rectangle for each node is only different 
			// by one value from the parent node, so we will account for changing both of them 
			// by using the old values in the method calls, depending on whether the node we are submitting 
			//in the method call is greater or less than this node. 
			//(if less, the max values change, if greater, the min values change).
			if(node.horizontal){
				newYMax = node.key.y();
				newYMin = node.key.y();
			} else {
				newXMax = node.key.x();
				newXMin = node.key.x();	
			}
			
			double bestDistance;
			
			//step 4: which way to go first, left or right?
			int cmp = comparePoints(query,node.key,node.horizontal);
			
			//if the query is greater than this node, we go right first, then left, but we must 
			// update the champDistance and champNode values in between.
			// This is to help the second method call to be more efficient by having updated values that
			// will might prune the search faster.
	        if      (cmp < 0) {
	        	//step 5: compare the points found from both sub-trees and return the closest one.
	        		// Get the result form the first subtree, then compare the distances. If it found a closer point, 
	        		// then that is our new champion.
	        	result1 = nearest(champDistance,champNode,node.left,query,newXMax,oldXMin,newYMax,oldYMin); //go left
	        	if(result1 != null) {
	        		bestDistance = query.distanceSquaredTo(result1.key);
	        		if(bestDistance < champDistance) {
		        		champDistance = bestDistance;
		        		champNode = result1;
		        	}
	        	}
	        	//Now go left with updated champion values. If it finds a better one, update champion, etc.
	        	result2 = nearest(champDistance,champNode,node.right,query,oldXMax,newXMin,oldYMax,newYMin); //then right
	        	if(result2 !=null){
	        		bestDistance = query.distanceSquaredTo(result2.key);
		        	if(bestDistance < champDistance) {
		        		champDistance = bestDistance;
		        		champNode = result2;
		        	}
	        	}
	        }
	        
	        else if (cmp > 0) {
	        	//step 5: compare the points found from both sub-trees and return the closest one.
	        		//Same process as above except we go in the opposite order.
	        	result1 = nearest(champDistance,champNode,node.right,query,oldXMax,newXMin,oldYMax,newYMin); //go right
	        	if(result1 != null) {
	        		bestDistance = query.distanceSquaredTo(result1.key);
	        		if(bestDistance < champDistance) {
		        		champDistance = bestDistance;
		        		champNode = result1;
		        	}
	        	}
	        	result2 = nearest(champDistance,champNode,node.left,query,newXMax,oldXMin,newYMax,oldYMin); //then left
	        	if(result2 !=null){
	        		bestDistance = query.distanceSquaredTo(result2.key);
		        	if(bestDistance < champDistance) {
		        		champDistance = bestDistance;
		        		champNode = result2;
		        	}
	        	}
	        }
		}
		// return the closest node we have found.
		return champNode;
	}
	
	// unit testing of the methods 
	public static void main(String[] args){
		
		KdTreeST<String> tree = new KdTreeST<>();
		tree.put(new Point2D(5,5), "middle");
		tree.put(new Point2D(6,6), "right1");
		tree.put(new Point2D(7,7), "right2");
		tree.put(new Point2D(8,8), "right3");
		tree.put(new Point2D(9,9), "right4");
		tree.put(new Point2D(10,10), "right5");
		tree.put(new Point2D(4,4), "left1");
		tree.put(new Point2D(3,3), "left2");
		tree.put(new Point2D(2,2), "left3");
		tree.put(new Point2D(1,1), "left4");
		tree.put(new Point2D(0,0), "left5");
		
		Iterable<Point2D> q = tree.points();
		
		for(Point2D p : q) StdOut.println(p + " " + tree.get(p));
		
		StdOut.println("\n Size: " + tree.size);
		assert(tree.size == 11);
		
		RectHV rect = new RectHV(0,5,5,10);
		
		StdOut.println("\nRange: ");
		for(Point2D p : tree.range(rect)) {
			StdOut.println(p + " " + tree.get(p));
			assert(tree.nearest(p).x() == 5);
			assert(tree.nearest(p).y() == 5);
		}
		
		Point2D query = new Point2D(2,8);
		
		StdOut.println();
		assert(tree.nearest(query).x() == 5);
		assert(tree.nearest(query).y() == 5);
		StdOut.println("\nNearest: " + tree.nearest(query));
		
	}

}