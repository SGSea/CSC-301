package project6;
import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private double[] energy; 
	private int[] pathTo; 
	private Picture pic;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		pic = new Picture(picture);
	}
	// current picture
	public Picture picture(){
		return new Picture(pic);
	}
	// width of current picture
	public int width(){
		return pic.width();
	}
	// height of current picture
	public int height(){
		return pic.height();
	}

	private double gradient(Color x, Color y) { 
		double r = x.getRed() - y.getRed(); 
		double g = x.getGreen() - y.getGreen(); 
		double b = x.getBlue() - y.getBlue(); 
		return r*r + g*g + b*b; 
	} 

	// energy of pixel at column x and row y
	public double energy(int x, int y){
		if (x < 0 || x >= width() || y < 0 || y >= height()) 
			throw new IndexOutOfBoundsException(); 
		if (x == 0 || y == 0 || x == width()-1 || y == height()-1) 
			return 195075; 
		return gradient(pic.get(x-1, y), pic.get(x+1, y)) + gradient(pic.get(x, y-1), pic.get(x, y+1)); 
	}

	private double energy(int x, int y, int flag) { 
		if (flag == 1) 
			return energy(y, x); 
		else 
			return energy(x, y); 
	} 

	private void computeEnergy(int w, int h, int flag) { 
		//double maxE = 0; 
		energy = new double[w*h]; 
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) { 
				energy[r*w + c] = energy(c, r, flag);  
			} 
		}
	} 
	
	private int[] computePath(int w, int h) { 
        pathTo = new int[w*h]; 
        for (int i = 0; i < w; i++) 
            pathTo[i] = -1; 
        for (int r = 1, i = w; r < h; r++) { 
            if (energy[i-w] <= energy[i-w+1]) pathTo[i] = i-w; 
            else pathTo[i] = i-w+1; 
            energy[i] += energy[pathTo[i]]; i++; 
            for (int c = 1; c < w-1; c++, i++) { 
                if (energy[i-w-1] <= energy[i-w]) { 
                    if (energy[i-w-1] <= energy[i-w+1]) pathTo[i] = i-w-1; 
                    else pathTo[i] = i-w+1; 
                } else { 
                    if (energy[i-w] <= energy[i-w+1]) pathTo[i] = i-w; 
                    else pathTo[i] = i-w+1; 
                } 
                energy[i] += energy[pathTo[i]]; 
            } 
            if (energy[i-w-1] <= energy[i-w]) pathTo[i] = i-w-1; 
            else pathTo[i] = i-w; 
            energy[i] += energy[pathTo[i]]; i++; 
        } 
 
        int pathEnd = w*(h-1); 
        double minE = energy[w*(h-1)]; 
        for (int i = w*(h-1); i < w*h; i++) { 
            if (minE > energy[i]) { 
                minE = energy[i]; 
                pathEnd = i; 
            } 
        } 
 
        int[] path = new int[h]; 
        for (int p = pathEnd; p >= 0; p = pathTo[p]) 
            path[p/w] = p % w; 
        return path; 
    } 

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam(){
		int w = height(), h = width(); 
		computeEnergy(w, h, 1); 
		return computePath(w, h); 
	}
	// sequence of indices for vertical seam
	public int[] findVerticalSeam(){
		int w = width(), h = height(); 
        computeEnergy(w, h, 0); 
        return computePath(w, h); 
	}
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam){
		if (height() <= 1) 
            throw new IllegalArgumentException(); 
        if (seam.length != width()) 
            throw new IllegalArgumentException("Wrong Length"); 
 
        Picture p = new Picture(width(), height()-1); 
        //Picture seamPic = new Picture(pic); 
        int prerow = seam[0]; 
        for (int c = 0; c < width(); c++) { 
            if (Math.abs(seam[c] - prerow) > 1) 
                throw new IllegalArgumentException("Non-valid seam"); 
            if (seam[c] < 0 || seam[c] >= height()) 
                throw new IndexOutOfBoundsException(); 
            //seamPic.set(c, a[c], Color.red); 
            prerow = seam[c]; 
            for (int r = 0; r < height()-1; r++) 
                if (r < seam[c]) 
                    p.set(c, r, pic.get(c, r)); 
                else 
                    p.set(c, r, pic.get(c, r+1)); 
        } 
        pic = p; 
        energy = null; 
        pathTo = null; 
        //seamPic.show(); 
    } 
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam){
		if (width() <= 1) 
            throw new IllegalArgumentException(); 
        if (seam.length != height()) 
            throw new IllegalArgumentException("Wrong Length"); 
 
        Picture p = new Picture(width()-1, height()); 
        //Picture seamPic = new Picture(pic); 
        int precol = seam[0]; 
        for (int r = 0; r < height(); r++) { 
            if (Math.abs(seam[r] - precol) > 1) 
                throw new IllegalArgumentException("Non-valid seam"); 
            if (seam[r] < 0 || seam[r] >= width()) 
                throw new IndexOutOfBoundsException(); 
            //seamPic.set(a[r], r, Color.red); 
            precol = seam[r]; 
            for (int c = 0; c < width()-1; c++) 
                if (c < seam[r]) 
                    p.set(c, r, pic.get(c, r)); 
                else 
                    p.set(c, r, pic.get(c+1, r)); 
        } 
        pic = p; 
        energy = null; 
        pathTo = null; 
        //seamPic.show(); 
    }
	
	// do unit testing of this class
	public static void main(String[] args){
		Picture picture = new Picture(args[0]); 
        picture.show(); 
        SeamCarver seamCarver = new SeamCarver(picture); 
        long start, end;  
        start = System.currentTimeMillis(); 
        for (int i = 0; i < 10; i++) 
            seamCarver.findVerticalSeam(); 
        end = System.currentTimeMillis(); 
        System.out.println("Time: "+(end-start)+"ms"); 
 
        start = System.currentTimeMillis(); 
        for (int i = 0; i < 10; i++) 
            seamCarver.findHorizontalSeam(); 
        end = System.currentTimeMillis(); 
        System.out.println("Time: "+(end-start)+"ms"); 
         
        start = System.currentTimeMillis(); 
        for (int i = 0; i < 1; i++) 
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam()); 
        end = System.currentTimeMillis(); 
        System.out.println("Time: "+(end-start)+"ms"); 
         
        start = System.currentTimeMillis(); 
        for (int i = 0; i < 1; i++) 
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam()); 
        end = System.currentTimeMillis(); 
        System.out.println("Time: "+(end-start)+"ms"); 
    } 
}

