package project5;
/*************************************************************************
 *  Author: 	  Sam Caldwell
 *  Compilation:  javac PointST.java.java
 *  Execution:    java PointST 
 *  Dependencies: Point2D.java, RectHV.java
 *
 *  PointST.java outlines a brute force implementation of a redblackBST 
 *
 *************************************************************************/
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;



public class PointST<Value> {
	
	
	private RedBlackBST<Point2D,Value> bst;
	
	// construct an empty symbol table of points  
	public PointST(){
		
		bst = new RedBlackBST<Point2D,Value>();
		
	}
	
	// is the symbol table empty? 
	public boolean isEmpty(){
		return bst.isEmpty();
		
	}
	// number of points    
	public int size(){
		return bst.size();
	}
	
	// associate the value val with point p   
	public void put(Point2D p, Value val){
		if(p == null || val == null) throw new java.lang.NullPointerException("Argument is null");
		bst.put(p, val);
	}
	
	// value associated with point p    
	public Value get(Point2D p){
		if(p == null) throw new java.lang.NullPointerException("Argument is null");
		return bst.get(p);
		
	}
	
	// does the symbol table contain point p? 
	public boolean contains(Point2D p){
		if(p == null) throw new java.lang.NullPointerException("Argument is null");
		return bst.contains(p);
		
	}
	
	// all points in the symbol table   
	public Iterable<Point2D> points(){
		return bst.keys();
		
	}
	   
	// all points that are inside the rectangle  
	public Iterable<Point2D> range(RectHV rect){
		if(rect == null) throw new java.lang.NullPointerException("Argument is null");
		
		// Create an instance of Iterable<Point2D>.
		Queue<Point2D> q = new Queue<>();
		
		// For each point in bst, we check to see if it is in the rect.
		for(Point2D p : bst.keys()){	
			if(rect.contains(p)) q.enqueue(p);
		}
		
		return q;
	}
	 
	// a nearest neighbor to point p; null if the symbol table is empty 
	public Point2D nearest(Point2D p){
		if(p == null) throw new java.lang.NullPointerException("Argument is null");
		Point2D champion = null;
		double distanceToBeat = 0;
		
		for(Point2D point : bst.keys()){
			if(champion == null) {
				champion = point;
				distanceToBeat = point.distanceSquaredTo(p);
				
			} else if(point.distanceSquaredTo(p) < distanceToBeat){
				champion = point;
				distanceToBeat = point.distanceSquaredTo(p);
			}
		}
		return champion;
		
	}
	   
	// unit testing (required)
	public static void main(String[] args){
		
		PointST<String> testBST= new PointST<>();
		
		//An array of points, including the origin and one point in each quadrant at various distances.
		int[][] testArray= {{0,0},{5,5},{-5,5},{-5,-5},{5,-5}};
		String[] valueArray = {"origin","first quadrant","second quadrant","third quadrant","fourth quadrant"};
		
		for(int i = 0 ; i < testArray.length ; i++){
			Point2D p = new Point2D(testArray[i][0],testArray[i][1]);
			testBST.put(p, "Point (" + testArray[i][0] + "," +testArray[i][1] + ") " + valueArray[i]);
		}
		
		//Begin testing nearest method:
		
		StdOut.println("\nTesting nearest: \n");
		
		
		//testing point close to origin - should give the origin as closest neighbor.
		Point2D queryPoint = new Point2D(1,1);
		StdOut.println("\nTesting " + queryPoint);
		StdOut.println("Result: ");
		assert(testBST.nearest(queryPoint).x() == 0);
		assert(testBST.nearest(queryPoint).y() == 0);
		StdOut.println(testBST.get(testBST.nearest(queryPoint)));
		
		// testing point very far from origin in fourth quadrant - 
		//should give the last point "Fourth Closest" as the closest neighbor
		queryPoint = new Point2D(5000,-5000);
		StdOut.println("\nTesting " + queryPoint);
		assert(testBST.nearest(queryPoint).x() == 5);
		assert(testBST.nearest(queryPoint).y() == -5);
		StdOut.println("Result: ");
		StdOut.println(testBST.get(testBST.nearest(queryPoint)));
		
		// testing point very far from origin in third quadrant - 
		//should give the "third Closest" point as the closest neighbor
		queryPoint = new Point2D(-5000,-5000);
		StdOut.println("\nTesting " + queryPoint);
		assert(testBST.nearest(queryPoint).x() == -5);
		assert(testBST.nearest(queryPoint).y() == -5);
		StdOut.println("Result: ");
		StdOut.println(testBST.get(testBST.nearest(queryPoint)));
		
		// testing point very far from origin in second quadrant - 
		//should give the "Second Closest" point as the closest neighbor
		queryPoint = new Point2D(-5000,5000);
		StdOut.println("\nTesting " + queryPoint);
		assert(testBST.nearest(queryPoint).x() == -5);
		assert(testBST.nearest(queryPoint).y() == 5);
		StdOut.println("Result: ");
		StdOut.println(testBST.get(testBST.nearest(queryPoint)));
		
		// testing point very far from origin in first quadrant - 
		//should give the "Closest to Origin" point as the closest neighbor
		queryPoint = new Point2D(5000,5000);
		StdOut.println("\nTesting " + queryPoint);
		assert(testBST.nearest(queryPoint).x() == 5);
		assert(testBST.nearest(queryPoint).y() == 5);
		StdOut.println("Result: ");
		StdOut.println(testBST.get(testBST.nearest(queryPoint)));
		
		
		//adding more points
		
		//First quadrant:
		testBST.put(new Point2D(6,7),"New Point in First quad");
		testBST.put(new Point2D(9,9),"New Point far out in First quad");
		
		//Second quadrant:
		testBST.put(new Point2D(-6,7),"New Point in Second quad");
		testBST.put(new Point2D(-9,9),"New Point far out in Second quad");
		
		//Third quadrant:
		testBST.put(new Point2D(-6,-7),"New Point in Third quad");
		testBST.put(new Point2D(-9,-9),"New Point far out in third quad");
		
		//Fourth quadrant:
		testBST.put(new Point2D(6,-7),"New Point in Fourth quad");
		testBST.put(new Point2D(9,-9),"New Point far out in Fourth quad");
		
		//Begin testing range
		StdOut.println("\nTesting range method: \n");
		
		RectHV queryRect;
		
		//Testing rectangle in first quadrant;
		queryRect = new RectHV(1,1,8,8);
		StdOut.println("\nTesting " + queryRect);
		StdOut.println("Result: ");
		for(Point2D p : testBST.range(queryRect)){
			StdOut.println(testBST.get(p));
			assert(p.x() == 5 || p.x() == 6);
			assert(p.y() == 5 || p.y() == 7);
		}
		
		//Testing rectangle in second quadrant;
		queryRect = new RectHV(-8,1,-1,8);
		StdOut.println("\nTesting " + queryRect);
		StdOut.println("Result: ");
		for(Point2D p : testBST.range(queryRect)){
			StdOut.println(testBST.get(p));
			assert(p.x() == -5 || p.x() == -6);
			assert(p.y() == 5 || p.y() == 7);
		}
		
		//Testing rectangle in third quadrant;
		queryRect = new RectHV(-8,-8,-1,-1);
		StdOut.println("\nTesting " + queryRect);
		StdOut.println("Result: ");
		for(Point2D p : testBST.range(queryRect)){
			StdOut.println(testBST.get(p));
			assert(p.x() == -5 || p.x() == -6);
			assert(p.y() == -5 || p.y() == -7);
		}
		
		//Testing rectangle in fourth quadrant;
		queryRect = new RectHV(1,-8,8,-1);
		StdOut.println("\nTesting " + queryRect);
		StdOut.println("Result: ");
		for(Point2D p : testBST.range(queryRect)){
			StdOut.println(testBST.get(p));
			assert(p.x() == 5 || p.x() == 6);
			assert(p.y() == -5 || p.y() == -7);
		}
	}
	
}