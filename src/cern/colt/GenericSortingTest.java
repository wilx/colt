/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
package cern.colt;

import cern.colt.function.IntComparator;
/**
Demonstrates how to use {@link GenericSorting}.

@author wolfgang.hoschek@cern.ch
@version 1.0, 03-Jul-99
*/
class GenericSortingTest extends Object {
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected GenericSortingTest() {}
/**
 * Just a demo.
 */
public static void demo1() {
	final int[] x;
	final double[] y;
	final double[] z;

	x = new int[]    {3,   2,   1  };
	y = new double[] {3.0, 2.0, 1.0};
	z = new double[] {6.0, 7.0, 8.0};

	Swapper swapper = new Swapper() {
		public void swap(int a, int b) {
			int t1;	double t2, t3;
			t1 = x[a]; x[a] = x[b];	x[b] = t1;
			t2 = y[a]; y[a] = y[b]; y[b] = t2;
			t3 = z[a]; z[a] = z[b];	z[b] = t3;
		}
	}; 

	IntComparator comp = new IntComparator() {
		public int compare(int a, int b) {
			return x[a]==x[b] ? 0 : (x[a]<x[b] ? -1 : 1);
		}
	};

	System.out.println("before:");
	System.out.println("X="+Arrays.toString(x));
	System.out.println("Y="+Arrays.toString(y));
	System.out.println("Z="+Arrays.toString(z));

			
	int from = 0;
	int to = x.length;
	GenericSorting.quickSort(from, to, comp, swapper);

	System.out.println("after:");
	System.out.println("X="+Arrays.toString(x));
	System.out.println("Y="+Arrays.toString(y));
	System.out.println("Z="+Arrays.toString(z));
	System.out.println("\n\n");
}
/**
 * Just a demo.
 */
public static void demo2() {
	final int[] x;
	final double[] y;
	final double[] z;

	x = new int[]    {6,   7,   8,   9  };
	y = new double[] {3.0, 2.0, 1.0, 3.0};
	z = new double[] {5.0, 4.0, 4.0, 1.0};

	Swapper swapper = new Swapper() {
		public void swap(int a, int b) {
			int t1;	double t2, t3;
			t1 = x[a]; x[a] = x[b];	x[b] = t1;
			t2 = y[a]; y[a] = y[b]; y[b] = t2;
			t3 = z[a]; z[a] = z[b];	z[b] = t3;
		}
	}; 
	
	IntComparator comp = new IntComparator() {
		public int compare(int a, int b) {
			if (y[a]==y[b]) return z[a]==z[b] ? 0 : (z[a]<z[b] ? -1 : 1);
			return y[a]<y[b] ? -1 : 1;
		}
	};
	

	System.out.println("before:");
	System.out.println("X="+Arrays.toString(x));
	System.out.println("Y="+Arrays.toString(y));
	System.out.println("Z="+Arrays.toString(z));

			
	int from = 0;
	int to = x.length;
	GenericSorting.quickSort(from, to, comp, swapper);

	System.out.println("after:");
	System.out.println("X="+Arrays.toString(x));
	System.out.println("Y="+Arrays.toString(y));
	System.out.println("Z="+Arrays.toString(z));
	System.out.println("\n\n");
}
}
