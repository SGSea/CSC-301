package project3;
/********* 
 * Author: Sam Caldwell 
 * Date: 4-21-2016
 * Comments: Term is part of the greater scheme of things. Term parses data from the input text file and arranges the objects
 * in a term array that has the weights, and important information about the prefixes of the objects for the purpose of
 * a search later. 
 * 
 * 
 */
import java.util.Comparator;
import edu.princeton.cs.algs4.*;

public class Term implements Comparable<Term> {
	private String query; 
	private long weight; 
	
    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
    	
    	if(query == null) { 
            throw new NullPointerException("Query is null");
        }
        if(weight < 0) { 
            throw new IllegalArgumentException("Weight is negative");
        }
        
    	this.query = query; 
    	this.weight = weight; 
    }
    
    public static Comparator<Term> byReverseWeightOrder() {
    	return new ReverseWeightOrder();
    }
    // Compares the two terms in descending order by weight.
    private static class ReverseWeightOrder implements Comparator<Term> {
    	public int compare(Term t, Term v) {
            if (t.weight > v.weight) {
                return -1;
            } else if (t.weight == v.weight) {
                return 0;
            } else {
                return 1;
            }
        } 
    }
    
    public static Comparator<Term> byPrefixOrder(int r) {
    	return new PrefixOrder(r);
    }
    
    // Compares the two terms in lexicographic order but using only the first r characters of each query.
    private static class PrefixOrder implements Comparator<Term> {
        private final int r;

        public PrefixOrder(final int r) { 
            if(r < 0) { 
                throw new IllegalArgumentException();
            }

            this.r = r;
        }

        public int compare(Term t, Term v) {
            String s1 = t.query;
            String s2 = v.query;
            int minlength = s1.length() < s2.length() ? s1.length() : s2.length();
            if (minlength >= r) {
                return s1.substring(0, r).compareTo(s2.substring(0, r));
            } else if (s1.substring(0, minlength).compareTo(s2.substring(0, minlength)) == 0) {
                if (s1.length() == minlength) return -1;
                else return 1;
            } else return s1.substring(0, minlength).compareTo(s2.substring(0, minlength));
        } 
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term other) {
    	 return this.query.compareTo(other.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
    	return this.query + "\t" + this.weight;
    }

    // unit testing (required)
   /** public static void main(String[] args)   {
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
        MergeX.sort(terms, new byReverseWeightOrder());
        for (int i = 0; i < N; i++) {
        	StdOut.println(terms[i]);
        	
        }
    } */
}