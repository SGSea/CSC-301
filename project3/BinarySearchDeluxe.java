package project3;
/****
 * Author: Sam Caldwell
 * Date: 4-21-2016
 * About: BinarySearchDeluxe goes through the Term array and ordered items based on keys and weights - if you search for 
 * 'car' you can specify the first or last instance of that item and BinarySearchDeluxe is the important thing.
 * 
 */
import java.util.Collections;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;

public class BinarySearchDeluxe {

    // Returns the index of the first key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
    	if (a == null || key == null || comparator == null) {
    		throw new java.lang.NullPointerException();
    	}
    	if (a.length == 0) {
    		return -1;
    	}
    	int l = 0;
    	int r = a.length - 1;
    	
    	// StdOut.println(r); (for testing)
    	while (l + 1 < r) {
    		int mid = l + (r - l)/2;
    		if (comparator.compare(key, a[mid]) <= 0) {
    			r = mid;
    		} else {
    			l = mid;
    		}
    	}
    	if (comparator.compare(key, a[l]) == 0) {
    		return l;
    	}
    	if (comparator.compare(key, a[r]) == 0) {
    		return r;
    	}
    	return -1;

    }

    // Returns the index of the last key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
    	if (a == null || key == null || comparator == null) {
    		throw new java.lang.NullPointerException();
    	}
    	if (a == null || a.length == 0) {
    		return -1;
    	}
    	int l = 0;
    	int r = a.length - 1;
    	while (l + 1 < r) {
    		int mid = l + (r - l)/2;
    		if (comparator.compare(key, a[mid]) < 0) {
    			r = mid;
    		} else {
    			l = mid;
    		}
    	}
    	
    	if (comparator.compare(key, a[r]) == 0) {
    		return r;
    	}
    	if (comparator.compare(key, a[l]) == 0) {
    		return l;
    	}
    	return -1;
    }
    
    // unit testing (required)
    public static void main(String[] args) {
    	
    /**	Integer[] numbers = {10, 10, 10, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1, 1};
    	StdOut.print(BinarySearchDeluxe.firstIndexOf (numbers, 10, Collections.reverseOrder()) + "\t");
    	StdOut.println(BinarySearchDeluxe.lastIndexOf(numbers, 10, Collections.reverseOrder()));
    	StdOut.print(BinarySearchDeluxe.firstIndexOf (numbers, 9, Collections.reverseOrder()) + "\t");
    	StdOut.println(BinarySearchDeluxe.lastIndexOf(numbers, 9, Collections.reverseOrder()));
    	StdOut.print(BinarySearchDeluxe.firstIndexOf (numbers, 4, Collections.reverseOrder()) + "\t");
    	StdOut.println(BinarySearchDeluxe.lastIndexOf(numbers, 4, Collections.reverseOrder()));
    	StdOut.print(BinarySearchDeluxe.firstIndexOf (numbers, 0, Collections.reverseOrder()) + "\t");
    	StdOut.println(BinarySearchDeluxe.lastIndexOf(numbers, 0, Collections.reverseOrder()));
    	StdOut.print(BinarySearchDeluxe.firstIndexOf (numbers, 11, Collections.reverseOrder()) + "\t");
    	StdOut.println(BinarySearchDeluxe.lastIndexOf(numbers, 11, Collections.reverseOrder())); **/
    }
}

