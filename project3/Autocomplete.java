package project3;

/******
 * Author: Sam Caldwell
 * Date: 4-21-2016	
 * About: Autocomplete is the beast doing all the heavy lifting here. Autocomplete takes in a commant line argument that 
 * is a data file (i.e. wikitionary.txt), a numeric input that determines how many responses to show of the document, and 
 * makes calls to both Term and BinarySearchDeluxe to bring them all together nicely. yum. 
 * 
 */
import edu.princeton.cs.algs4.*;
public class Autocomplete {
	
	private final Term[] terms;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
    	if (terms == null) throw new NullPointerException("Terms can't be null");
    	
    	this.terms = new Term[terms.length];
    	for (int i = 0; i < terms.length; i++)
    		this.terms[i] = terms[i];
    	MergeX.sort(this.terms);
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix) {
    	if (prefix == null) throw new NullPointerException("Prefix can't be null");
    	
    	Term temp = new Term(prefix, 0);
    	
    	int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
    	// StdOut.println("first " + firstIndex);
    	if (firstIndex == -1) return new Term[0];
    	int lastIndex  = BinarySearchDeluxe.lastIndexOf (terms, temp, Term.byPrefixOrder(prefix.length()));
    	// StdOut.println("last " + lastIndex);
    	Term[] termMatch = new Term[1 + lastIndex - firstIndex]; 
    	
    	for (int i = 0; i < termMatch.length; i++) {
    		termMatch[i] = terms[firstIndex++];
    	}
    	
    	MergeX.sort(termMatch, Term.byReverseWeightOrder());
    	
    		return termMatch;
    	
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
    	Term temp = new Term(prefix, 0);
    	return 1 + 
    			BinarySearchDeluxe.lastIndexOf(terms, temp, Term.byPrefixOrder(prefix.length())) -
    			BinarySearchDeluxe.firstIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
    }
    
    public static void main(String[] args) {

        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print out the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            // StdOut.println("Searching for prefix " + prefix);
            Term[] results = autocomplete.allMatches(prefix);
           //  StdOut.println(results.length);
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }
}
