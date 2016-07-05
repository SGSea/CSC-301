package project2;
import edu.princeton.cs.algs4.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] queue;
	private int numberOfItems;
	
	 // construct an empty randomized queue
	public RandomizedQueue()     { 
		int defaultnumberOfItems = 1;
		
		this.queue = (Item[]) new Object[defaultnumberOfItems];
		this.numberOfItems = 0;
	}
	
	// is the queue empty?
	public boolean isEmpty()   {
			return (this.numberOfItems == 0);
	}
	// return the number of items on the queue
	public int numberOfItems()    {
		return this.numberOfItems;
	}
	// add the item
	public void enqueue(Item item)  {
		if (item == null) {
			throw new NullPointerException("Cannot enqueue null objects.");
		}
		
		if (this.numberOfItems == queue.length) {
			Item[] renumberOfItemsdQueue = (Item[]) new Object[queue.length * 2];
			
			for(int i = 0; i < queue.length; i++) {
				renumberOfItemsdQueue[i] = queue[i];
			}
			
			this.queue = renumberOfItemsdQueue;
		}
		
		queue[numberOfItems] = item;
		
		this.numberOfItems++;
	}
	// remove and return a random item
	public Item dequeue()  {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is currently empty.");
		}
		
		int rand = getRandomOccupiedIndex();
		Item dequeued = queue[rand];
		
		this.numberOfItems--;
		
		queue[rand] = queue[this.numberOfItems];
		queue[this.numberOfItems] = null;
		
		if (this.queue.length > 4 && this.numberOfItems <= queue.length / 4) {
			Item [] renumberOfItemsdQueue = (Item[]) new Object[queue.length / 2];
			
			for(int i = 0; i < this.numberOfItems; i++) {
				renumberOfItemsdQueue[i] = queue[i];
			}
			
			this.queue = renumberOfItemsdQueue;
		}
		
		return dequeued;
	}
	// return a random item (but do not remove it)
	public Item sample()    {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is currently empty.");
		}
		
		return queue[getRandomOccupiedIndex()];
	}
	
	// return an integer of a random index which is not null
	private int getRandomOccupiedIndex() {
		while(true) {
			int rand = StdRandom.uniform(this.numberOfItems);
			if(queue[rand] != null) {
				return rand;
			}
		}
	}
	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new ListIterator(queue, numberOfItems);
	}
	
private class ListIterator implements Iterator<Item> {
		
		private Item[] iteratorQueue;
		private int iteratorIndex = 0;
		
		public ListIterator(Item[] queue, int numberOfItems) {
			
			iteratorQueue = (Item[]) new Object[numberOfItems];
			
			//Copy items into iterator queue
			for(int i = 0; i < iteratorQueue.length; i++) {
				iteratorQueue[i] = queue[i];
			}
			
			//shuffle the iterator queue
			for(int j = 1; j < iteratorQueue.length; j++) {
				int swapIndex = StdRandom.uniform(j + 1);
				
				Item temp = iteratorQueue[j];
				iteratorQueue[j] = iteratorQueue[swapIndex];
				iteratorQueue[swapIndex] = temp;
			}
		}
		
		@Override
		public boolean hasNext() {
			return (iteratorIndex < iteratorQueue.length);
		}

		@Override
		public Item next() {
			if(!hasNext()) {
				throw new NoSuchElementException("No more objects to iterate through");
			}
			
			Item item = iteratorQueue[iteratorIndex];
			iteratorIndex++;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove method not supported");
		}
	}
	// unit testing (required)
	public static void main(String[] args)  {
		RandomizedQueue<String> newRQ = new RandomizedQueue<String>();
		
		newRQ.enqueue("0");
		newRQ.enqueue("1");
        newRQ.enqueue("2");
        newRQ.enqueue("3");
        newRQ.enqueue("4");
        newRQ.enqueue("5");
        newRQ.enqueue("6");
        newRQ.enqueue("7");
        newRQ.enqueue("8");
        newRQ.enqueue("9");
        
		
		StdOut.println("Test 1: Expected output is 10");
		StdOut.println("items: " + newRQ.numberOfItems);
		StdOut.println();
		
		
		StdOut.println("Test 2: Expected output is a random deqeue of 3 objects in the stack, and then an iterator function of the remaining integers randomly assorted, twice.");
        StdOut.println(newRQ.dequeue());
        StdOut.println(newRQ.dequeue());
        StdOut.println(newRQ.dequeue());

        StdOut.println("items: " + newRQ.numberOfItems);

        Iterator it1 = newRQ.iterator();
        Iterator it2 = newRQ.iterator();

        while (it1.hasNext()) {
            System.out.print(it1.next());
        }
        StdOut.println("\n");
        while (it2.hasNext()) {
            System.out.print(it2.next());
        }
        
    }
}

