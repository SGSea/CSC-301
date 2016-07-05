package project2;
import edu.princeton.cs.algs4.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node first = null;
	private Node last = null;
	private int numberOfItems = 0;

	private class Node {
		private Item item;
		private Node next;
		private Node previous;
	}

	// construct an empty deque
	public Deque() { }

	// is the deque empty?
	public boolean isEmpty(){
		return ( this.numberOfItems == 0 );
	}
	// return the number of items on the deque
	public int numberOfItems(){
		return ( this.numberOfItems );
	}
	// add the item to the front
	public void addFirst(Item item){
		if (item == null) {
			throw new java.lang.NullPointerException("Cannot add an item whose value is null");
		}

		//Create a new node
		Node newNode = new Node();
		newNode.item = item;

		this.numberOfItems++;

		//It there is only 1 node, first and last point to it
		if (numberOfItems == 1) {
			this.last = newNode;
			this.first = newNode; 

		} else {
			//rearrange the pointers using a temporary pointer
			Node tempFirst = this.first;
			this.first = newNode;
			newNode.next = tempFirst;
			tempFirst.previous = newNode;
		}
	}
	// add the item to the end
	public void addLast(Item item){
		if ( item == null) {
			throw new java.lang.NullPointerException("Cannot add an item whose value is null");
		}

		// Create a new node
		Node newNode = new Node();
		newNode.item = item;

		this.numberOfItems++;

		// If there is only 1 node, first and last point to it
		if (numberOfItems == 1) {
			this.first = newNode;
			this.last = newNode;

		} else {
			// Insert new node at rear
			Node tempLast = this.last;
			this.last = newNode;
			newNode.previous = tempLast;
			tempLast.next = newNode;
		}
	}
	// remove and return the item from the front
	public Item removeFirst(){
		if (numberOfItems() == 0) {
			throw new java.util.NoSuchElementException("The stack is empty");
		}

		// Fetch item to return
		Item item = this.first.item;

		this.numberOfItems--;

		// Reorder pointers
		this.first = this.first.next;

		if (numberOfItems() == 0) {
			this.last = null;
		} else {
			this.first.previous = null;
		}

		return item;
	}
	// remove and return the item from the end
	public Item removeLast(){
		if (numberOfItems() == 0) {
			throw new java.util.NoSuchElementException("The stack is empty");
		}

		// Fetch item to return
		Item item = this.last.item;

		this.numberOfItems--;

		// Rearrange pointers
		this.last = this.last.previous;

		if (numberOfItems() == 0) {
			this.first = null;
		} else {
			this.last.next = null;
		}

		return item;
	}
	
	// return an iterator over items in order from front to end
	public Iterator<Item> iterator(){
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item> {
		private Node current = first;
		
		// returns true if there is an item next in the deque 
		@Override
		public boolean hasNext() {
			return (current != null);
		}

		// returns the current item and increments the pointer 
		@Override
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more objects to return");
			}
			
			Item item = current.item;
			current = current.next;
			
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("You cannot call the remove method in the Iterator");
		}
	}
	// unit testing (required)
	public static void main(String[] args){
		Deque<String> newdeque = new Deque<String>();
		
		StdOut.println("Test 1, Expected Output is " + "0");
		StdOut.println("items: " + newdeque.numberOfItems);
		StdOut.println();
		
		StdOut.println("Test 2, adding desserts to the queue plus an unwelcome guest. Expected output is 5");
        newdeque.addFirst("Cake");
        newdeque.addFirst("Cookies");
        newdeque.addFirst("IceCream");
        newdeque.addFirst("Cupcake");
        newdeque.addLast("Ugly Duckling"); 
        StdOut.println("items: " + newdeque.numberOfItems);
        StdOut.println();
        
        StdOut.println("Test 3, removing an element from the front. Expected output is 4");
        newdeque.removeFirst();
        StdOut.println("items: " + newdeque.numberOfItems); 
        StdOut.println();
        
        StdOut.println("Test 4, Testing the iterator. Adding in several new desserts.");
        newdeque.addFirst("Chocolate");
        newdeque.addFirst("Dark Chocolate");
        newdeque.addFirst("Darker Chocolate");
        newdeque.addFirst("Even Darker Chocolate");
        StdOut.println("After adding 4 items to the deque, there should be 8 total items with an expected output of:");
        StdOut.println("Even Darker Chocolate");
        StdOut.println("Darker Chocolate");
        StdOut.println("Dark Chocolate");
        StdOut.println("Chocolate");
        StdOut.println("IceCream");
        StdOut.println("Cookies");
        StdOut.println("Cake");
        StdOut.println("Ugly Duckling");
       
        
        
        
        StdOut.println();
        StdOut.println("Iterator Results:");
        Iterator Itr = newdeque.iterator();
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        StdOut.println(Itr.next());
        
    }
}


		
	
