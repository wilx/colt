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
/**
 * To be deleted; deprecated. TODO. Contains various special purpose sorting algorithms.
 *
 * The algorithms come in two templated versions, one partially sorting <tt>int[]</tt> arrays and another one for <tt>double[]</tt> arrays.
 * They further distinguish between versions <i>synchronously</i> sorting either one, two or three arrays.
 * <p>
 * You may want to start out reading about the simplest case: Partially sorting one <tt>int[]</tt> array into two intervals.
 * To do so, read {@link #partition(int[],int,int,int)}.
 *
 * Next, building upon that foundation comes a method partially sorting <tt>int[]</tt> arrays into multiple intervals.
 * See {@link #partition(int[],int,int,int[],int,int,int[])} for related documentation.
 * <p>
 * All other methods are conceptually no different than the one's you now already understand.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 03-Jul-99
 */
class xPartitioningOld extends Object {
	public static int steps = 0;
	public static int swappedElements = 0;
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected xPartitioningOld() {}
/**
 * Same as {@link #dualPartition(int[],int[],int,int,int[],int,int,int[])}
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static void dualPartition(double[] primary, double[] secondary, int from, int to, double[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	if (splitFrom>splitTo) return; // nothing to do

	int middle = (splitFrom + splitTo) / 2;

	int splitIndex;
	if (from>to) {
		splitIndex = from-1;
	}
	else {
		splitIndex = dualPartition(primary,secondary,from,to,splitters[middle]);
	}
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	if (splitFrom <= middle-1) {
		//System.out.println("1.recursive: from="+from+", to="+splitIndex+", splitFrom="+splitFrom+", splitTo="+(middle-1));
		dualPartition(primary, secondary, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	}
	
	if (middle+1 <= splitTo) {
		//System.out.println("2.recursive: from="+(splitIndex+1)+", to="+to+", splitFrom="+(middle+1)+", splitTo="+splitTo);
		dualPartition(primary, secondary, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
		//partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo, splitIndexes);
	}
}
/**
 * Same as {@link #dualPartition(int[],int[],int,int,int)} 
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static int dualPartition(double[] primary, double[] secondary, int from, int to, double splitter) {
	// returns index of last element < splitter
	double element;
	for (int i=from-1; ++i<=to; ) {
		element = primary[i];
		if (element < splitter) {
			primary[i] = primary[from];
			primary[from] = element;

			element = secondary[i];
			secondary[i] = secondary[from];
			secondary[from++] = element;
		}
	}

	return from-1;
}
/**
 * Same as {@link #partition(int[],int,int,int[],int,int,int[])} except that this method <i>synchronously</i> sorts two arrays at the same time;
 * both arrays are partially sorted according to the elements of the primary array.
 * In other words, each time an element in the primary array is moved from index A to B, the correspoding element within the secondary array is also moved from index A to B.
 * <p>
 * <b>Use cases:</b>
 * <p>
 * Image having a large list of 2-dimensional points. 
 * If memory consumption and performance matter, it is a good idea to physically lay them out as two 1-dimensional arrays
 * (using something like <tt>Point2D</tt> objects would be prohibitively expensive, both in terms of time and space).
 * Now imagine wanting to histogram the points.
 * We may want to partially sort the points by x-coordinate into intervals.
 * This method efficiently does the job.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Same as for single-partition methods, i.e. <tt>O( N * log(k+1) )</tt>.
 * No temporary memory is allocated; the sort is in-place.
 */
public static void dualPartition(int[] primary, int[] secondary, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	if (splitFrom>splitTo) return; // nothing to do
	
	int middle = (splitFrom + splitTo) / 2;
	int splitIndex = dualPartition(primary,secondary,from,to,splitters[middle]);
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	dualPartition(primary, secondary, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	dualPartition(primary, secondary, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
}
/**
 * Same as {@link #partition(int[],int,int,int)} except that this method <i>synchronously</i> sorts two arrays at the same time;
 * both arrays are partially sorted according to the elements of the primary array.
 * In other words, each time an element in the primary array is moved from index A to B, the correspoding element within the secondary array is also moved from index A to B.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Same as for single-partition methods, i.e. <tt>O( N )</tt>.
 * No temporary memory is allocated; the sort is in-place.
 */
public static int dualPartition(int[] primary, int[] secondary, int from, int to, int splitter) {
	// returns index of last element < splitter
	int element;
	for (int i=from-1; ++i<=to; ) {
		element = primary[i];
		if (element < splitter) {
			primary[i] = primary[from];
			primary[from] = element;

			element = secondary[i];
			secondary[i] = secondary[from];
			secondary[from++] = element;
		}
	}

	return from-1;
}
/**
 * Equivalent to {@link #dualPartition(int[],int[],int,int,int[],int,int,int[]) dualPartition(primary.elements(),secondary.elements(),from,to,splitters.elements(),0,splitters.size()-1,splitIndexes.elements())}.
 */
public static void dualPartition(IntArrayList primary, IntArrayList secondary, int from, int to, IntArrayList splitters, IntArrayList splitIndexes) {
	dualPartition(primary.elements(),secondary.elements(),from,to,splitters.elements(),0,splitters.size()-1,splitIndexes.elements());
}
/**
 * Same as {@link #partition(int[],int,int,int[],int,int,int[])}
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static void partition(double[] list, int from, int to, double[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	if (splitFrom>splitTo) return; // nothing to do
	
	int middle = (splitFrom + splitTo) / 2;
	int splitIndex = partition(list,from,to,splitters[middle]);
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	partition(list, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
}
/**
 * Same as {@link #partition(int[],int,int,int)}
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static int partition(double[] list, int from, int to, double splitter) {
	double element;
	for (int i=from-1; ++i<=to; ) {
		element = list[i];
		if (element < splitter) {
			// swap list[i] with list[from]
			list[i] = list[from];
			list[from++] = element;
		}
	}
	return from-1;
}
/**
 * Partially sorts the given list such that all elements falling into some intervals are placed next to each other.
 * Returns the indexes of elements delimiting intervals.
 * <p>
 * <b>Example:</b>
 * <p>
 * <tt>list = (7, 4, 5, 50, 6, 4, 3, 6), splitters = (5, 10, 30)</tt>
 * defines the three intervals <tt>[-infinity,5), [5,10), [10,30)</tt>.
 * Lets define to sort the entire list (<tt>from=0, to=7</tt>) using all splitters (<tt>splitFrom==0, splitTo=2</tt>).
 * <p>
 * The method modifies the list to be <tt>list = (4, 4, 3, 6, 7, 5, 6, 50)</tt>
 * and returns the <tt>splitIndexes = (2, 6, 6)</tt>.
 * In other words,
 * <ul>
 * <li>All values <tt>list[0..2]</tt> fall into <tt>[-infinity,5)</tt>.
 * <li>All values <tt>list[3..6]</tt> fall into <tt>[5,10)</tt>.
 * <li>All values <tt>list[7..6]</tt> fall into interval <tt>[10,30)</tt>, i.e. no elements, since <tt>7>6</tt>.
 * <li>All values <tt>list[7 .. 7=list.length-1]</tt> fall into <tt>[30,infinity]</tt>.
 * <li>In general, all values <tt>list[splitIndexes[j-1]+1 .. splitIndexes[j]]</tt> fall into interval <tt>j</tt>.
 * </ul>
 * As can be seen, the list is partially sorted such that values falling into a certain interval are placed next to each other.
 * Note that <i>within</i> an interval, elements are entirelly unsorted.
 * They are only sorted across interval boundaries.
 * <p>
 * More formally, this method guarantees that upon return <tt>for all j = splitFrom .. splitTo</tt> there holds:
 * <br><tt>for all i = splitIndexes[j-1]+1 .. splitIndexes[j]: splitters[j-1] <= list[i] < splitters[j]</tt>.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Let <tt>N=to-from+1</tt> be the number of elements to be partially sorted.
 * Let <tt>k=splitTo-splitFrom+1</tt> be the number of splitter elements.
 * Then the time complexity is <tt>O( N * log(k+1) )</tt> to logarithmic base 2.
 * No temporary memory is allocated; the sort is in-place.
 *
 * <p>
 * @param list the list to be partially sorted.
 
 * @param from the index of the first element within <tt>list</tt> to be considered.
 * @param to the index of the last element within <tt>list</tt> to be considered.
 * The method considers the elements <tt>list[from] .. list[to]</tt>.
 
 * @param splitters the values at which the list shall be split into intervals.
 * Must be sorted ascending and must not contain multiple identical values.
 * These preconditions are not checked; be sure that they are met.
 
 * @param splitFrom the index of the first splitter element to be considered.
 * @param splitTo the index of the last splitter element to be considered.
 * The method considers the splitter elements <tt>splitters[splitFrom] .. splitters[splitTo]</tt>.
 
 * @param splitIndexes a list into which this method fills the indexes of elements delimiting intervals.
 * Upon return <tt>splitIndexes[splitFrom..splitTo]</tt> will be set accordingly.
 * Therefore, must satisfy <tt>splitIndexes.length > splitTo</tt>.
 * <p>
 * Tip: Normally you will have <tt>splitIndexes.length == splitters.length</tt> as well as <tt>from==0, to==list.length-1</tt> and <tt>splitFrom==0, splitTo==splitters.length-1</tt>.
 */
public static void partition(int[] list, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	int f,t,min,element,splitter; // int, double --> template type dependent
	
	while (splitFrom<=splitTo && from<=to) {

		/*
		System.out.println();
		if (from<=to) {
			System.out.println("SORT WORKING: from="+from+", to="+to+", splitter="+splitter+", splitFrom="+splitFrom+", splitTo="+splitTo);
		}
		else {
			System.out.println("SORT WORKING: NOTHING TO DO.");
		}
		*/
		
		// the next block can be deleted without changing semantics - optimization only
		if (to-from==1) { // two elements to process only
			f = list[from];
			t = list[to];
			min = f;
			if (t < min) min = t;
			
			//System.out.println("list="+ new IntArrayList(list));
			//System.out.println("splitters="+ new IntArrayList(splitters));
			//System.out.println("list[from]="+list[from]);
			//System.out.println("list[to]="+list[to]);
			
			int i = splitFrom;
			int s = from-1;
			while (i<=splitTo && (!(min < splitters[i]))) splitIndexes[i++] = s;
			if (i>splitTo) return; // both elements have been processed
			//System.out.println("i="+i);
			//System.out.println("splitters[i]="+splitters[i]);
			
			boolean fromLess = f < splitters[i];
			boolean toLess =   t < splitters[i];
			if (fromLess) s++;
			if (toLess) s++;
			if (fromLess && toLess) {
				while (i<=splitTo) splitIndexes[i++] = s;
				return; // both elements have been processed
			}
			if (toLess) { // swap
				list[to] = f; 
				list[from] = t;
			}
			splitIndexes[i++] = s;
			from = to;
			splitFrom = i;
			// one element left to process
			// will jump into "if (from==to)" and do the rest there
		}
		
		// the next block can be deleted without changing semantics - optimization only
		if (from==to) { // one element to process only
			element = list[from];
			int i = splitFrom;
			from--;
			while (i<=splitTo && (!(element < splitters[i]))) splitIndexes[i++] = from;
			from++;
			while (i<=splitTo) splitIndexes[i++] = from;
			return;
		}
		
		// now the normal binary (logarithmic) partitioning
			
		// choose splitter (pivot) element
		int middle = (splitFrom + splitTo) / 2;
		splitter = splitters[middle];
		
		// Establish invariant: list[i] < splitter <= list[j] for i=from..splitIndex and j=splitIndex+1 .. to
		// inlined: 
		// int	splitIndex = partition(list,from,to,splitters[middle]);
		steps += to-from+1;
		int head = from;
		for (int i=from-1; ++i<=to; ) { // swap all elements < splitter to front
			element = list[i];
			if (element < splitter) {		
				list[i] = list[head];
				list[head++] = element;
			}
		}
		int splitIndex = head-1;
		//if (from<=to) System.out.println("Swapped "+(head-from)+" elements");
		swappedElements += (head-from);
		
		
		splitIndexes[middle] = splitIndex;
		//System.out.println("middle="+middle);
		//System.out.println("splitIndex="+splitIndex);
		if (splitFrom == splitTo) return; // done

		// sort right half 
		// System.out.println("1.recursive: from="+(splitIndex+1)+", to="+to+", splitFrom="+(middle+1)+", splitTo="+splitTo);
		if (middle+1 <= splitTo) partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);

		// sort left half
		// tail recursion transformed to iteration
		// System.out.println("2.recursive: from="+from+", to="+splitIndex+", splitFrom="+splitFrom+", splitTo="+(middle-1));
		// if (middle+1 <= splitTo) partition(list, from, splitIndex,         splitters, splitFrom,  middle-1,   splitIndexes);
		to = splitIndex;
		splitTo = middle-1;
	}

	// chill out
	if (splitFrom<=splitTo && from>to) { // set splitIndexes
		from--;
		for (int i=splitFrom; i<=splitTo; ) splitIndexes[i++] = from;
	}
}
/**
 * Partially sorts the given list such that all elements falling into the given interval are placed next to each other.
 * Returns the index of the element delimiting the interval.
 * <p>
 * <b>Example:</b>
 * <p>
 * <tt>list = (7, 4, 5, 50, 6, 4, 3, 6), splitter = 5</tt>
 * defines the two intervals <tt>[-infinity,5), [5,+infinity]</tt>.
 * <p>
 * The method modifies the list to be <tt>list = (4, 4, 3, 50, 6, 7, 5, 6)</tt>
 * and returns the split index <tt>2</tt>.
 * In other words,
 * <ul>
 * <li>All values <tt>list[0..2]</tt> fall into <tt>[-infinity,5)</tt>.
 * <li>All values <tt>list[3=2+1 .. 7=list.length-1]</tt> fall into <tt>[5,+infinity]</tt>.
 * </ul>
 * As can be seen, the list is partially sorted such that values falling into a certain interval are placed next to each other.
 * Note that <i>within</i> an interval, elements are entirelly unsorted.
 * They are only sorted across interval boundaries.
 * <p>
 * More formally, this method guarantees that upon return there holds:
 * <ul>
 * <li>for all <tt>i = from .. returnValue: list[i] < splitter</tt> and
 * <li>for all <tt>i = returnValue+1 .. list.length-1: !(list[i] < splitter)</tt>.
 * </ul>
 * <p>
 * <b>Performance:</b>
 * <p>
 * Let <tt>N=to-from+1</tt> be the number of elements to be partially sorted.
 * Then the time complexity is <tt>O( N )</tt>.
 * No temporary memory is allocated; the sort is in-place.
 *
 * <p>
 * @param list the list to be partially sorted.
 
 * @param from the index of the first element within <tt>list</tt> to be considered.
 * @param to the index of the last element within <tt>list</tt> to be considered.
 * The method considers the elements <tt>list[from] .. list[to]</tt>.
 
 * @param splitter the value at which the list shall be split.
 
 * @return the index of the largest element falling into the interval <tt>[-infinity,splitter)</tt>, as seen after partial sorting.
 */
public static int partition(int[] list, int from, int to, int splitter) {
	/*
	System.out.println();
	if (from<=to) {
		System.out.println("SORT WORKING: from="+from+", to="+to+", splitter="+splitter);
	}
	else {
		System.out.println("SORT WORKING: NOTHING TO DO.");
	}
	*/
	
	
		
	
	// returns index of last element < splitter

	
	/*
	for (int i=from-1; ++i<=to; ) {
		if (list[i] < splitter) {
			int element = list[i];
			list[i] = list[from];
			list[from++] = element;
		}
	}
	*/
	
	
	
	
	int element;
	for (int i=from-1; ++i<=to; ) {
		element = list[i];
		if (element < splitter) {
			// swap list[i] with list[from]
			list[i] = list[from];
			list[from++] = element;
		}
	}
	

	/*	
	//JAL:
	int first = from;
	int last = to+1;
	--first;
	while (true) {
		while (++first < last && list[first] < splitter);
		while (first < --last && !(list[last] < splitter)); 
		if (first >= last) return first-1;
		int tmp = list[first];
		list[first] = list[last];
		list[last] = tmp;
	}
	*/
	
	

	/*
	System.out.println("splitter="+splitter);
	System.out.println("before="+new IntArrayList(list));
	int head = from;
	int trail = to;
	int element;
	while (head<=trail) {
		head--;
		while (++head < trail && list[head] < splitter);
		
		trail++;
		while (--trail > head && list[trail] >= splitter);

		if (head != trail) {		
			element = list[head];
			list[head] = list[trail];
			list[trail] = element;
		}
		head++;
		trail--;
		System.out.println("after ="+new IntArrayList(list)+", head="+head);
	}
	*/
		

	/*
	//System.out.println("splitter="+splitter);
	//System.out.println("before="+new IntArrayList(list));
	to++;
	//int head = from;
	int element;
	//int oldHead;
	while (--to >= from) {
		element = list[to];
		if (element < splitter) {
			from--;
			while (++from < to && list[from] < splitter);
			//if (head != to) {
				list[to] = list[from];
				list[from++] = element;
				//oldHead = list[head];
				//list[head] = element;
				//list[i] = oldHead;
				
				//head++;
			//}
			//head++;
		}
		//System.out.println("after ="+new IntArrayList(list)+", head="+head);
	}
	*/
	
	/*
	int i=from-1;
	int head = from;
	int trail = to;
	while (++i <= trail) {
		int element = list[i];
		if (element < splitter) {
			if (head == i) head++;
			else {
				// swap list[i] with list[from]
				int oldHead = list[head];
				int oldTrail = list[trail];
				list[head++] = element;
				list[i--] = oldTrail;
				list[trail--] = oldHead;
			}
		}
		//System.out.println(new IntArrayList(list));
	
	}
	*/
	
	
	return from-1;
	//return head-1;
}
/**
 * Equivalent to {@link #partition(double[],int,int,double[],int,int,int[]) partition(list.elements(), from, to, splitters.elements(), 0, splitters.size()-1, splitIndexes.elements())}.
 */
public static void partition(DoubleArrayList list, int from, int to, DoubleArrayList splitters, IntArrayList splitIndexes) {
	partition(list.elements(),from,to,splitters.elements(),0,splitters.size()-1,splitIndexes.elements());
}
/**
 * Equivalent to {@link #partition(int[],int,int,int[],int,int,int[]) partition(list.elements(), from, to, splitters.elements(), 0, splitters.size()-1, splitIndexes.elements())}.
 */
public static void partition(IntArrayList list, int from, int to, IntArrayList splitters, IntArrayList splitIndexes) {
	partition(list.elements(),from,to,splitters.elements(),0,splitters.size()-1,splitIndexes.elements());
}
/**
 * Partially sorts the given list such that all elements falling into some intervals are placed next to each other.
 * Returns the indexes of elements delimiting intervals.
 * <p>
 * <b>Example:</b>
 * <p>
 * <tt>list = (7, 4, 5, 50, 6, 4, 3, 6), splitters = (5, 10, 30)</tt>
 * defines the three intervals <tt>[-infinity,5), [5,10), [10,30)</tt>.
 * Lets define to sort the entire list (<tt>from=0, to=7</tt>) using all splitters (<tt>splitFrom==0, splitTo=2</tt>).
 * <p>
 * The method modifies the list to be <tt>list = (4, 4, 3, 6, 7, 5, 6, 50)</tt>
 * and returns the <tt>splitIndexes = (2, 6, 6)</tt>.
 * In other words,
 * <ul>
 * <li>All values <tt>list[0..2]</tt> fall into <tt>[-infinity,5)</tt>.
 * <li>All values <tt>list[3..6]</tt> fall into <tt>[5,10)</tt>.
 * <li>All values <tt>list[7..6]</tt> fall into interval <tt>[10,30)</tt>, i.e. no elements, since <tt>7>6</tt>.
 * <li>All values <tt>list[7 .. 7=list.length-1]</tt> fall into <tt>[30,infinity]</tt>.
 * <li>In general, all values <tt>list[splitIndexes[j-1]+1 .. splitIndexes[j]]</tt> fall into interval <tt>j</tt>.
 * </ul>
 * As can be seen, the list is partially sorted such that values falling into a certain interval are placed next to each other.
 * Note that <i>within</i> an interval, elements are entirelly unsorted.
 * They are only sorted across interval boundaries.
 * <p>
 * More formally, this method guarantees that upon return <tt>for all j = splitFrom .. splitTo</tt> there holds:
 * <br><tt>for all i = splitIndexes[j-1]+1 .. splitIndexes[j]: splitters[j-1] <= list[i] < splitters[j]</tt>.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Let <tt>N=to-from+1</tt> be the number of elements to be partially sorted.
 * Let <tt>k=splitTo-splitFrom+1</tt> be the number of splitter elements.
 * Then the time complexity is <tt>O( N * log(k+1) )</tt> to logarithmic base 2.
 * No temporary memory is allocated; the sort is in-place.
 *
 * <p>
 * @param list the list to be partially sorted.
 
 * @param from the index of the first element within <tt>list</tt> to be considered.
 * @param to the index of the last element within <tt>list</tt> to be considered.
 * The method considers the elements <tt>list[from] .. list[to]</tt>.
 
 * @param splitters the values at which the list shall be split into intervals.
 * Must be sorted ascending and must not contain multiple identical values.
 * These preconditions are not checked; be sure that they are met.
 
 * @param splitFrom the index of the first splitter element to be considered.
 * @param splitTo the index of the last splitter element to be considered.
 * The method considers the splitter elements <tt>splitters[splitFrom] .. splitters[splitTo]</tt>.
 
 * @param splitIndexes a list into which this method fills the indexes of elements delimiting intervals.
 * Upon return <tt>splitIndexes[splitFrom..splitTo]</tt> will be set accordingly.
 * Therefore, must satisfy <tt>splitIndexes.length > splitTo</tt>.
 * <p>
 * Tip: Normally you will have <tt>splitIndexes.length == splitters.length</tt> as well as <tt>from==0, to==list.length-1</tt> and <tt>splitFrom==0, splitTo==splitters.length-1</tt>.
 */
public static void partition1(int[] list, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	while (splitFrom<=splitTo && from<=to) {
		// choose splitter (pivot) element
		int middle = (splitFrom + splitTo) / 2;
		int splitter = splitters[middle];
		
		System.out.println();
		if (from<=to) {
			System.out.println("SORT WORKING: from="+from+", to="+to+", splitter="+splitter+", splitFrom="+splitFrom+", splitTo="+splitTo);
		}
		else {
			System.out.println("SORT WORKING: NOTHING TO DO.");
		}
		
		// Establish invariant list[i] < splitter <= list[j] for i=from..splitIndex and j=splitIndex+1 .. to
		// inlined: 
		// int	splitIndex = partition(list,from,to,splitters[middle]);		
		int head = from;
		for (int i=from-1; ++i<=to; ) { // swap all elements < splitter to front
			int element = list[i];
			if (element < splitter) {		
				list[i] = list[head];
				list[head++] = element;
			}
		}
		int splitIndex = head-1;
		if (from<=to) System.out.println("Swapped "+(head-from)+" elements");
		swappedElements += (head-from);
		
		
		splitIndexes[middle] = splitIndex;
		//System.out.println("middle="+middle);
		System.out.println("splitIndex="+splitIndex);
		if (splitFrom == splitTo) return; // done

		// sort right half 
		// System.out.println("1.recursive: from="+(splitIndex+1)+", to="+to+", splitFrom="+(middle+1)+", splitTo="+splitTo);
		if (middle+1 <= splitTo) partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);

		// sort left half
		// tail recursion transformed to iteration
		// System.out.println("2.recursive: from="+from+", to="+splitIndex+", splitFrom="+splitFrom+", splitTo="+(middle-1));
		// if (middle+1 <= splitTo) partition(list, from, splitIndex,         splitters, splitFrom,  middle-1,   splitIndexes);
		to = splitIndex;
		splitTo = middle-1;
	}

	// chill out
	if (splitFrom<=splitTo && from>to) { // set splitIndexes
		from--;
		for (int i=splitFrom-1; ++i<=splitTo; ) splitIndexes[i] = from;
	}
}
/**
 * Partially sorts the given list such that all elements falling into some intervals are placed next to each other.
 * Returns the indexes of elements delimiting intervals.
 * <p>
 * <b>Example:</b>
 * <p>
 * <tt>list = (7, 4, 5, 50, 6, 4, 3, 6), splitters = (5, 10, 30)</tt>
 * defines the three intervals <tt>[-infinity,5), [5,10), [10,30)</tt>.
 * Lets define to sort the entire list (<tt>from=0, to=7</tt>) using all splitters (<tt>splitFrom==0, splitTo=2</tt>).
 * <p>
 * The method modifies the list to be <tt>list = (4, 4, 3, 6, 7, 5, 6, 50)</tt>
 * and returns the <tt>splitIndexes = (2, 6, 6)</tt>.
 * In other words,
 * <ul>
 * <li>All values <tt>list[0..2]</tt> fall into <tt>[-infinity,5)</tt>.
 * <li>All values <tt>list[3..6]</tt> fall into <tt>[5,10)</tt>.
 * <li>All values <tt>list[7..6]</tt> fall into interval <tt>[10,30)</tt>, i.e. no elements, since <tt>7>6</tt>.
 * <li>All values <tt>list[7 .. 7=list.length-1]</tt> fall into <tt>[30,infinity]</tt>.
 * <li>In general, all values <tt>list[splitIndexes[j-1]+1 .. splitIndexes[j]]</tt> fall into interval <tt>j</tt>.
 * </ul>
 * As can be seen, the list is partially sorted such that values falling into a certain interval are placed next to each other.
 * Note that <i>within</i> an interval, elements are entirelly unsorted.
 * They are only sorted across interval boundaries.
 * <p>
 * More formally, this method guarantees that upon return <tt>for all j = splitFrom .. splitTo</tt> there holds:
 * <br><tt>for all i = splitIndexes[j-1]+1 .. splitIndexes[j]: splitters[j-1] <= list[i] < splitters[j]</tt>.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Let <tt>N=to-from+1</tt> be the number of elements to be partially sorted.
 * Let <tt>k=splitTo-splitFrom+1</tt> be the number of splitter elements.
 * Then the time complexity is <tt>O( N * log(k+1) )</tt> to logarithmic base 2.
 * No temporary memory is allocated; the sort is in-place.
 *
 * <p>
 * @param list the list to be partially sorted.
 
 * @param from the index of the first element within <tt>list</tt> to be considered.
 * @param to the index of the last element within <tt>list</tt> to be considered.
 * The method considers the elements <tt>list[from] .. list[to]</tt>.
 
 * @param splitters the values at which the list shall be split into intervals.
 * Must be sorted ascending and must not contain multiple identical values.
 * These preconditions are not checked; be sure that they are met.
 
 * @param splitFrom the index of the first splitter element to be considered.
 * @param splitTo the index of the last splitter element to be considered.
 * The method considers the splitter elements <tt>splitters[splitFrom] .. splitters[splitTo]</tt>.
 
 * @param splitIndexes a list into which this method fills the indexes of elements delimiting intervals.
 * Upon return <tt>splitIndexes[splitFrom..splitTo]</tt> will be set accordingly.
 * Therefore, must satisfy <tt>splitIndexes.length > splitTo</tt>.
 * <p>
 * Tip: Normally you will have <tt>splitIndexes.length == splitters.length</tt> as well as <tt>from==0, to==list.length-1</tt> and <tt>splitFrom==0, splitTo==splitters.length-1</tt>.
 */
public static void partition2(int[] list, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	/*
	// check for sortedness and no multiple identical elements
	for (int i=splitFrom; i<=splitTo-1; i++) {
		if (splitters[i+1] <= splitters[i]) throw new IllegalArgumentException("must not contain multiple identical values");
	}
	*/
	while (splitFrom<=splitTo && from<=to) {
		int middle = (splitFrom + splitTo) / 2;
		//System.out.println("middle="+middle);
		int	splitIndex = partition(list,from,to,splitters[middle]);
		splitIndexes[middle] = splitIndex;
		//System.out.println("splitIndex="+splitIndex);

		if (splitFrom == splitTo) return; // done

		// recursive 
		if (splitFrom <= middle-1) {
			//System.out.println("1.recursive: from="+from+", to="+splitIndex+", splitFrom="+splitFrom+", splitTo="+(middle-1));
			partition(list, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
		}

		from = splitIndex+1;
		splitFrom = middle+1;
		/*
		if (middle+1 <= splitTo) {
			//System.out.println("2.recursive: from="+(splitIndex+1)+", to="+to+", splitFrom="+(middle+1)+", splitTo="+splitTo);
			partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
			//partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo, splitIndexes);
		}
		*/
	}
	
	if (splitFrom<=splitTo && from>to) { // set splitIndexes
		from--;
		for (int i=splitFrom-1; ++i<=splitTo; ) splitIndexes[i] = from;
	}
}
/**
 * Partially sorts the given list such that all elements falling into some intervals are placed next to each other.
 * Returns the indexes of elements delimiting intervals.
 * <p>
 * <b>Example:</b>
 * <p>
 * <tt>list = (7, 4, 5, 50, 6, 4, 3, 6), splitters = (5, 10, 30)</tt>
 * defines the three intervals <tt>[-infinity,5), [5,10), [10,30)</tt>.
 * Lets define to sort the entire list (<tt>from=0, to=7</tt>) using all splitters (<tt>splitFrom==0, splitTo=2</tt>).
 * <p>
 * The method modifies the list to be <tt>list = (4, 4, 3, 6, 7, 5, 6, 50)</tt>
 * and returns the <tt>splitIndexes = (2, 6, 6)</tt>.
 * In other words,
 * <ul>
 * <li>All values <tt>list[0..2]</tt> fall into <tt>[-infinity,5)</tt>.
 * <li>All values <tt>list[3..6]</tt> fall into <tt>[5,10)</tt>.
 * <li>All values <tt>list[7..6]</tt> fall into interval <tt>[10,30)</tt>, i.e. no elements, since <tt>7>6</tt>.
 * <li>All values <tt>list[7 .. 7=list.length-1]</tt> fall into <tt>[30,infinity]</tt>.
 * <li>In general, all values <tt>list[splitIndexes[j-1]+1 .. splitIndexes[j]]</tt> fall into interval <tt>j</tt>.
 * </ul>
 * As can be seen, the list is partially sorted such that values falling into a certain interval are placed next to each other.
 * Note that <i>within</i> an interval, elements are entirelly unsorted.
 * They are only sorted across interval boundaries.
 * <p>
 * More formally, this method guarantees that upon return <tt>for all j = splitFrom .. splitTo</tt> there holds:
 * <br><tt>for all i = splitIndexes[j-1]+1 .. splitIndexes[j]: splitters[j-1] <= list[i] < splitters[j]</tt>.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Let <tt>N=to-from+1</tt> be the number of elements to be partially sorted.
 * Let <tt>k=splitTo-splitFrom+1</tt> be the number of splitter elements.
 * Then the time complexity is <tt>O( N * log(k+1) )</tt> to logarithmic base 2.
 * No temporary memory is allocated; the sort is in-place.
 *
 * <p>
 * @param list the list to be partially sorted.
 
 * @param from the index of the first element within <tt>list</tt> to be considered.
 * @param to the index of the last element within <tt>list</tt> to be considered.
 * The method considers the elements <tt>list[from] .. list[to]</tt>.
 
 * @param splitters the values at which the list shall be split into intervals.
 * Must be sorted ascending and must not contain multiple identical values.
 * These preconditions are not checked; be sure that they are met.
 
 * @param splitFrom the index of the first splitter element to be considered.
 * @param splitTo the index of the last splitter element to be considered.
 * The method considers the splitter elements <tt>splitters[splitFrom] .. splitters[splitTo]</tt>.
 
 * @param splitIndexes a list into which this method fills the indexes of elements delimiting intervals.
 * Upon return <tt>splitIndexes[splitFrom..splitTo]</tt> will be set accordingly.
 * Therefore, must satisfy <tt>splitIndexes.length > splitTo</tt>.
 * <p>
 * Tip: Normally you will have <tt>splitIndexes.length == splitters.length</tt> as well as <tt>from==0, to==list.length-1</tt> and <tt>splitFrom==0, splitTo==splitters.length-1</tt>.
 */
public static void partitionOld(int[] list, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	/*
	// check for sortedness and no multiple identical elements
	for (int i=splitFrom; i<=splitTo-1; i++) {
		if (splitters[i+1] <= splitters[i]) throw new IllegalArgumentException("must not contain multiple identical values");
	}
	*/
	
	if (splitFrom>splitTo) return; // nothing to do
	int middle = (splitFrom + splitTo) / 2;
	//System.out.println("middle="+middle);
	int splitIndex = partition(list,from,to,splitters[middle]);

	
	//System.out.println("splitIndex="+splitIndex);
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	if (splitFrom <= middle-1) {
		//System.out.println("1.recursive: from="+from+", to="+splitIndex+", splitFrom="+splitFrom+", splitTo="+(middle-1));
		partition(list, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	}
	
	if (middle+1 <= splitTo) {
		//System.out.println("2.recursive: from="+(splitIndex+1)+", to="+to+", splitFrom="+(middle+1)+", splitTo="+splitTo);
		partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
		//partition(list, splitIndex+1, to,         splitters, middle+1,  splitTo, splitIndexes);
	}

}
/**
 * Same as {@link #triplePartition(int[],int[],int[],int,int,int[],int,int,int[])}
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static void triplePartition(double[] primary, double[] secondary, double[] tertiary, int from, int to, double[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	if (splitFrom>splitTo) return; // nothing to do
	
	int middle = (splitFrom + splitTo) / 2;
	int splitIndex = triplePartition(primary,secondary,tertiary,from,to,splitters[middle]);
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	triplePartition(primary, secondary, tertiary, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	triplePartition(primary, secondary, tertiary, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
}
/**
 * Same as {@link #triplePartition(int[],int[],int[],int,int,int)} 
 * except that it partitions <tt>double[]</tt> rather than <tt>int[]</tt> arrays.
 */
public static int triplePartition(double[] primary, double[] secondary, double[] tertiary, int from, int to, double splitter) {
	// returns index of last element < splitter
	double element;
	for (int i=from-1; ++i<=to; ) {
		element = primary[i];
		if (element < splitter) {
			primary[i] = primary[from];
			primary[from] = element;

			element = secondary[i];
			secondary[i] = secondary[from];
			secondary[from] = element;
			
			element = tertiary[i];
			tertiary[i] = tertiary[from];
			tertiary[from++] = element;
		}
	}

	return from-1;
}
/**
 * Same as {@link #partition(int[],int,int,int[],int,int,int[])} except that this method <i>synchronously</i> sorts three arrays at the same time;
 * all three arrays are partially sorted according to the elements of the primary array.
 * In other words, each time an element in the primary array is moved from index A to B, the correspoding element within the secondary array as well as the corresponding element within the tertiary array are also moved from index A to B.
 * <p>
 * <b>Use cases:</b>
 * <p>
 * Image having a large list of 3-dimensional points. 
 * If memory consumption and performance matter, it is a good idea to physically lay them out as three 1-dimensional arrays
 * (using something like <tt>Point3D</tt> objects would be prohibitively expensive, both in terms of time and space).
 * Now imagine wanting to histogram the points.
 * We may want to partially sort the points by x-coordinate into intervals.
 * This method efficiently does the job.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Same as for single-partition methods, i.e. <tt>O( N * log(k+1) )</tt>.
 * No temporary memory is allocated; the sort is in-place.
 */
public static void triplePartition(int[] primary, int[] secondary, int[] tertiary, int from, int to, int[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
	//if (splitIndexes.length <= splitTo) throw new IllegalArgumentException();
	if (splitFrom>splitTo) return; // nothing to do
	
	int middle = (splitFrom + splitTo) / 2;
	int splitIndex = triplePartition(primary,secondary,tertiary,from,to,splitters[middle]);
	splitIndexes[middle] = splitIndex;

	if (splitFrom == splitTo) return; // done

	// recursive 
	triplePartition(primary, secondary, tertiary, from,         splitIndex, splitters, splitFrom, middle-1,  splitIndexes);
	triplePartition(primary, secondary, tertiary, splitIndex+1, to,         splitters, middle+1,  splitTo,   splitIndexes);
}
/**
 * Same as {@link #partition(int[],int,int,int)} except that this method <i>synchronously</i> sorts three arrays at the same time;
 * all three arrays are partially sorted according to the elements of the primary array.
 * In other words, each time an element in the primary array is moved from index A to B, the correspoding element within the secondary array as well as the corresponding element within the tertiary array are also moved from index A to B.
 * <p>
 * <b>Performance:</b>
 * <p>
 * Same as for single-partition methods, i.e. <tt>O( N )</tt>.
 * No temporary memory is allocated; the sort is in-place.
 */
public static int triplePartition(int[] primary, int[] secondary, int[] tertiary, int from, int to, int splitter) {
	// returns index of last element < splitter
	int element;
	for (int i=from-1; ++i<=to; ) {
		element = primary[i];
		if (element < splitter) {
			primary[i] = primary[from];
			primary[from] = element;

			element = secondary[i];
			secondary[i] = secondary[from];
			secondary[from] = element;
			
			element = tertiary[i];
			tertiary[i] = tertiary[from];
			tertiary[from++] = element;
		}
	}

	return from-1;
}
}
