package project2;
import edu.princeton.cs.algs4.*;

public class Subset {

    public static void main(String[] args) {
        int k = Integer.valueOf(args[0]);

        RandomizedQueue<String> newRQ = new RandomizedQueue<String>();
        String str = StdIn.readString();

        newRQ.enqueue(str);

        while (!StdIn.isEmpty()) {
            str = StdIn.readString();
            newRQ.enqueue(str);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(newRQ.dequeue());
        }

    }
}