/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
package cern.colt;

import cern.colt.list.IntArrayList;
import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.*;
/**
 * Tests and benchmarks methods of {@link cern.colt.Partitioning}.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 03-Jul-99
 */
class PartitioningTest extends Object {
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected PartitioningTest() {}
/**
 * Tests various methods of this class.
 */
public static void main(String args[]) {
	
	int runs = Integer.parseInt(args[0]);
	int size = Integer.parseInt(args[1]);
	int splittersSize = Integer.parseInt(args[2]);
	boolean isOutlier = new Boolean(args[3]).booleanValue();
	String kind = args[4];
	boolean newSort = args[5].equals("new");

	//if (kind.equals("int")) benchmarkPartition(runs, size, splittersSize, isOutlier, newSort);
	//if (kind.equals("double")) benchmarkPartitionDouble(runs, size, splittersSize, isOutlier);
}
public static void testPartition() {
	System.out.println("\n\n");
	/*
	//int[] values = {78,7,3000,7,6,3};
	//int[] values = {78,7,3000,7,6,3,12,4,11,150,90,1,50,1500,2000,80};
	int[] values = {7,4,5,50,6,4,3,6};
	//int[] splitterValues = {5,10,30};
	int[] splitterValues = {5};
	//int[] splitterValues = {5,10,50,100,1000};

	// single
	IntArrayList list = new IntArrayList(values).copy();
	
	int splitter = 5;
	System.out.println(list);

	int splitIndex = Partitioning.partition(list.elements(),0,list.size()-1, splitter);
	
	System.out.println(list);
	System.out.println("splitIndex="+splitIndex);
	*/

	{
		//matrix
		DoubleMatrix2D matrix = cern.colt.matrix.DoubleFactory2D.dense.descending(4,2);
		//matrix.zMult(-1);
		//double[] splitterValues = {5.0,10.0,30.0};
		double[] splitterValues = {5.0,10.0,12.0};
		//double[] splitterValues = {5.0,10.0,50.0,100.0,1000.0};
		DoubleMatrix1D column = matrix.viewColumn(0);

		int[] splitIndexes = new int[splitterValues.length];
		System.out.println(matrix);
		System.out.println("col1="+column);
		double[] col = column.toArray();
		System.out.println("col2="+new DoubleArrayList(col));
		
		//JDKArrays.sort(col,0,matrix.rows());
		System.out.println("col3="+new DoubleArrayList(col));
		System.out.println("sorted1="+cern.colt.matrix.doublealgo.Sorting.quickSort.sort(matrix,0));
		//System.out.println("sorted2="+Sorting.quickSort2(matrix,0));
		System.out.println(cern.colt.matrix.doublealgo.Partitioning.partition(matrix,0,splitterValues,splitIndexes));

		System.out.println("splitters="+new DoubleArrayList(splitterValues));
		System.out.println("splitIndexes="+new IntArrayList(splitIndexes));
		
		System.out.println(matrix);
		//cern.colt.matrix.algo.Partitioning.xPartitionOld(matrix,column,0,column.size()-1,splitterValues,0,splitterValues.length-1,splitIndexes);

		//System.out.println(matrix);
		//System.out.println("splitters="+new DoubleArrayList(splitterValues));
		//System.out.println("splitIndexes="+new IntArrayList(splitIndexes));
	}

	
	{
		//matrix
		DoubleMatrix3D matrix = cern.colt.matrix.DoubleFactory3D.dense.descending(4,2,3);
		System.out.println(matrix);
		System.out.println("sorted1="+cern.colt.matrix.doublealgo.Sorting.quickSort.sort(matrix,0,0));
		System.out.println(matrix);
	}
/*

	// multiple
	list = new IntArrayList(values).copy();
	IntArrayList splitters = new IntArrayList(splitterValues);
	IntArrayList splitIndexes = new IntArrayList(new int[splitters.size()]);
	
	System.out.println();
	System.out.println(list);
	System.out.println(splitters);

	System.out.println("Partitioning...");
	xPartitioningOld.partition(list,0,list.size()-1, splitters, splitIndexes);
	System.out.println(list);
	System.out.println("splitIndexes="+splitIndexes);

	System.out.println("Partitioning2...");
	list = new IntArrayList(values).copy();
	Partitioning.partition(list,0,list.size()-1, splitters, splitIndexes);
	System.out.println(list);
	System.out.println("splitIndexes="+splitIndexes);
	*/
	/*
	//dual multiple
	list = new IntArrayList(values).copy();
	IntArrayList secondary = new IntArrayList(values).copy();
	System.out.println();
	System.out.println(list);
	System.out.println(secondary);
	System.out.println(splitters);

	dualpartition(list,secondary,0,list.size()-1, splitters, splitIndexes);

	System.out.println(list);
	System.out.println(secondary);
	System.out.println("splitIndexes="+splitIndexes);
	*/
}
/**
 * Checks the correctness of the partition method.
 */
public static void testPartition(IntArrayList list, int from, int to, IntArrayList splitters) {

	IntArrayList splitIndexes = new IntArrayList(splitters.size());
	splitIndexes.setSize(splitters.size());
	
	IntArrayList partiallySorted = list.copy();
	Partitioning.partition(partiallySorted.elements(),from,to,splitters.elements(),0,splitters.size()-1,splitIndexes.elements());

	// check the normal values
	int lastSplitter = Integer.MIN_VALUE;
	int lastSplitIndex = from-1;
	for (int i=0; i<splitters.size(); i++) {
		int splitter = splitters.get(i);
		int splitIndex = splitIndexes.get(i);
		for (int j=lastSplitIndex+1; j<=splitIndex; j++) {
			if (! (lastSplitter <= partiallySorted.get(j) && partiallySorted.get(j) < splitter)) {
				throw new RuntimeException("bug detected");
			}
		}
		lastSplitter = splitter;
		lastSplitIndex = splitIndex;
	}
	// now check the trailing values
	//for (int j=1+splitIndexes.get(splitters.size()-1); j<=to; j++) {
	for (int j=1+lastSplitIndex; j<=to; j++) {
		if (! (lastSplitter <= partiallySorted.get(j) && partiallySorted.get(j) <= Integer.MAX_VALUE)) {
			System.out.println("list   ="+list.partFromTo(from,to));
			System.out.println("partial="+partiallySorted.partFromTo(from,to));
			System.out.println("splitters="+splitters);
			System.out.println("splitIndexes="+splitIndexes);
			System.out.println("j="+j);
			System.out.println("element[j]="+partiallySorted.get(j));
			System.out.println("lastSplitter="+lastSplitter);
			throw new RuntimeException("bug detected");
		}
	}
	

	// check multiset equality.
	partiallySorted.sortFromTo(from,to);
	IntArrayList sortedList = list.copy();
	sortedList.sortFromTo(from,to);

	int size = list.size();
	if (!( size==partiallySorted.size() &&
			jal.INT.Sorting.includes(partiallySorted.elements(),sortedList.elements(),from,to+1,from,to+1) &&
			jal.INT.Sorting.includes(sortedList.elements(),partiallySorted.elements(),from,to+1,from,to+1))) {
			System.out.println("sortedList="+sortedList.partFromTo(from,to));
			System.out.println("partiallySorted="+partiallySorted.partFromTo(from,to));
			throw new RuntimeException("bug detected");
	}
}
/**
 * Checks the correctness of the partition method by generating random input parameters and checking whether results are correct.
 */
public static void testPartitionRandomly(int runs) {
	cern.jet.random.engine.RandomEngine engine = new cern.jet.random.engine.MersenneTwister();
	cern.jet.random.Uniform gen = new cern.jet.random.Uniform(engine);
	
	for (int run=0; run<runs; run++) {
		int maxSize = 50;
		int maxSplittersSize = 2*maxSize;
		
		
		int size = gen.nextIntFromTo(0,maxSize);
		int from, to;
		if (size==0) { 
			from=0; to=-1;
		}
		else {
			from = gen.nextIntFromTo(0,size-1);
			to = gen.nextIntFromTo(Math.min(from,size-1),size-1);
		}

		int intervalFrom = gen.nextIntFromTo(size/2,2*size);
		int intervalTo = gen.nextIntFromTo(intervalFrom,2*size);
		IntArrayList list = new IntArrayList(size);
		for (int i=0; i<size; i++) list.add(gen.nextIntFromTo(intervalFrom,intervalTo));

		int splittersSize = gen.nextIntFromTo(0,maxSplittersSize);
		IntArrayList splitters = new IntArrayList(splittersSize);
		for (int i=0; i<splittersSize; i++) splitters.add(gen.nextIntFromTo(intervalFrom/2,2*intervalTo));
		splitters.sort();
		//System.out.println("\nbefore="+splitters);
		splittersSize = jal.INT.Modification.unique(splitters.elements(),0,splittersSize);
		splitters.setSize(splittersSize);
		//System.out.println(" after="+splitters);

		testPartition(list, from, to, splitters);
	}

	System.out.println("All tests passed. No bug detected.");
}
}
