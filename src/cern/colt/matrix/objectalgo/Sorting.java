package cern.colt.matrix.objectalgo;

/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.function.IntComparator;
/**
 * Matrix Quicksorts.
 * Come in two variants: a) Sorting according to the order induces by a user supplied comparator, b) sorting according to natural ordering.
 * Natural ordering means cells must implement {@java.lang.Comparable}.
 * In both variants, cells must not be <tt>null</tt>.
 *
 * All sorting algorithms are tuned quicksorts, adapted from Jon
 * L. Bentley and M. Douglas McIlroy's "Engineering a Sort Function",
 * Software-Practice and Experience, Vol. 23(11) P. 1249-1265 (November
 * 1993).  This algorithm offers n*log(n) performance on many data sets
 * that cause other quicksorts to degrade to quadratic performance.
 *
 * @see cern.colt.Sorting
 * @see cern.colt.GenericSorting
 * @see java.util.Arrays
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public class Sorting extends Object {
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected Sorting() {}
/**
Sorts the vector into ascending order, according to the <i>natural ordering</i>.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
To sort ranges use sub-ranging views. To sort descending, use flip views ...
<p>
<b>Example:</b> 
<table border="1" cellspacing="0">
  <tr nowrap> 
	<td valign="top"><tt> 7, 1, 3, 1<br>
	  </tt></td>
	<td valign="top"> 
	  <p><tt> ==&gt; 1, 1, 3, 7<br>
		The vector IS NOT SORTED.<br>
		The new VIEW IS SORTED.</tt></p>
	</td>
  </tr>
</table>

@param vector the vector to be sorted.
@return a new sorted vector (matrix) view. 
		<b>Note that the original matrix is left unaffected.</b>
*/
public static ObjectMatrix1D quickSort(final ObjectMatrix1D vector) {
	int[] indexes = new int[vector.size()]; // row indexes to reorder instead of matrix itself
	for (int i=indexes.length; --i >= 0; ) indexes[i] = i;

	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			Comparable av = (Comparable) (vector.getQuick(a));
			Comparable bv = (Comparable) (vector.getQuick(b));
			int r = av.compareTo(bv);
			return r<0 ? -1 : (r>0 ? 1 : 0);
		}
	};

	cern.colt.Sorting.quickSort(indexes,0,indexes.length,comp);

	return vector.viewSelection(indexes);
}
/**
Sorts the vector into ascending order, according to the order induced by the specified comparator.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
The algorithm compares two cells at a time, determinining whether one is smaller, equal or larger than the other.
To sort ranges use sub-ranging views. To sort descending, use flip views ...
<p>
<b>Example:</b>
<pre>
// sort by sinus of cells
ObjectComparator comp = new ObjectComparator() {
&nbsp;&nbsp;&nbsp;public int compare(Object a, Object b) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Object as = Math.sin(a); Object bs = Math.sin(b);
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return as < bs ? -1 : as == bs ? 0 : 1;
&nbsp;&nbsp;&nbsp;}
};
sorted = quickSort(vector,comp);
</pre>

@param vector the vector to be sorted.
@param c the comparator to determine the order.
@return a new matrix view sorted as specified.
		<b>Note that the original vector (matrix) is left unaffected.</b>
*/
public static ObjectMatrix1D quickSort(final ObjectMatrix1D vector, final java.util.Comparator c) {
	int[] indexes = new int[vector.size()]; // row indexes to reorder instead of matrix itself
	for (int i=indexes.length; --i >= 0; ) indexes[i] = i;

	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			return c.compare(vector.getQuick(a), vector.getQuick(b));
		}
	};

	cern.colt.Sorting.quickSort(indexes,0,indexes.length,comp);

	return vector.viewSelection(indexes);
}
/**
Sorts the matrix rows into ascending order, according to the <i>natural ordering</i> of the matrix values in the given column.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
To sort ranges use sub-ranging views. To sort columns by rows, use dice views. To sort descending, use flip views ...
<p>
<b>Example:</b> 
<table border="1" cellspacing="0">
  <tr nowrap> 
	<td valign="top"><tt>4 x 2 matrix: <br>
	  7, 6<br>
	  5, 4<br>
	  3, 2<br>
	  1, 0 <br>
	  </tt></td>
	<td align="left" valign="top"> 
	  <p><tt>column = 0;<br>
		view = quickSort(matrix,column);<br>
		System.out.println(view); </tt><tt><br>
		==> </tt></p>
	  </td>
	<td valign="top"> 
	  <p><tt>4 x 2 matrix:<br>
		1, 0<br>
		3, 2<br>
		5, 4<br>
		7, 6</tt><br>
		The matrix IS NOT SORTED.<br>
		The new VIEW IS SORTED.</p>
	  </td>
  </tr>
</table>

@param matrix the matrix to be sorted.
@param column the index of the column inducing the order.
@return a new matrix view having rows sorted by the given column.
		<b>Note that the original matrix is left unaffected.</b>
@throws IndexOutOfBoundsException if <tt>column < 0 || column >= matrix.columns()</tt>.
*/
public static ObjectMatrix2D quickSort(ObjectMatrix2D matrix, int column) {
	if (column < 0 || column >= matrix.columns()) throw new IndexOutOfBoundsException("column="+column+", matrix="+Formatter.shape(matrix));

	int[] rowIndexes = new int[matrix.rows()]; // row indexes to reorder instead of matrix itself
	for (int i=rowIndexes.length; --i >= 0; ) rowIndexes[i] = i;

	final ObjectMatrix1D col = matrix.viewColumn(column);
	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			Comparable av = (Comparable) (col.getQuick(a));
			Comparable bv = (Comparable) (col.getQuick(b));
			int r = av.compareTo(bv);
			return r<0 ? -1 : (r>0 ? 1 : 0);
		}
	};

	cern.colt.Sorting.quickSort(rowIndexes,0,rowIndexes.length,comp);

	// view the matrix according to the reordered row indexes
	// take all columns in the original order
	return matrix.viewSelection(rowIndexes,null);
}
/**
Sorts the matrix rows according to the order induced by the specified comparator.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
The algorithm compares two rows (1-d matrices) at a time, determinining whether one is smaller, equal or larger than the other.
To sort ranges use sub-ranging views. To sort columns by rows, use dice views. To sort descending, use flip views ...
<p>
<b>Example:</b>
<pre>
// sort by sum of values in a row
ObjectMatrix1DComparator comp = new ObjectMatrix1DComparator() {
&nbsp;&nbsp;&nbsp;public int compare(ObjectMatrix1D a, ObjectMatrix1D b) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Object as = a.zSum(); Object bs = b.zSum();
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return as < bs ? -1 : as == bs ? 0 : 1;
&nbsp;&nbsp;&nbsp;}
};
sorted = quickSort(matrix,comp);
</pre>

@param matrix the matrix to be sorted.
@param c the comparator to determine the order.
@return a new matrix view having rows sorted as specified.
		<b>Note that the original matrix is left unaffected.</b>
*/
public static ObjectMatrix2D quickSort(final ObjectMatrix2D matrix, final ObjectMatrix1DComparator c) {
	int[] rowIndexes = new int[matrix.rows()]; // row indexes to reorder instead of matrix itself
	for (int i=rowIndexes.length; --i >= 0; ) rowIndexes[i] = i;

	final ObjectMatrix1D[] views = new ObjectMatrix1D[matrix.rows()]; // precompute views for speed
	for (int i=views.length; --i >= 0; ) views[i] = matrix.viewRow(i);

	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			//return c.compare(matrix.viewRow(a), matrix.viewRow(b));
			return c.compare(views[a], views[b]);
		}
	};

	cern.colt.Sorting.quickSort(rowIndexes,0,rowIndexes.length,comp);

	// view the matrix according to the reordered row indexes
	// take all columns in the original order
	return matrix.viewSelection(rowIndexes,null);
}
/**
Sorts the matrix slices into ascending order, according to the <i>natural ordering</i> of the matrix values in the given <tt>[row,column]</tt> position.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
To sort ranges use sub-ranging views. To sort by other dimensions, use dice views. To sort descending, use flip views ...
<p>
The algorithm compares two 2-d slices at a time, determinining whether one is smaller, equal or larger than the other.
Comparison is based on the cell <tt>[row,column]</tt> within a slice.
Let <tt>A</tt> and <tt>B</tt> be two 2-d slices. Then we have the following rules
<ul>
<li><tt>A &lt;  B  iff A.get(row,column) &lt;  B.get(row,column)</tt>
<li><tt>A == B iff A.get(row,column) == B.get(row,column)</tt>
<li><tt>A &gt;  B  iff A.get(row,column) &gt;  B.get(row,column)</tt>
</ul>

@param matrix the matrix to be sorted.
@param row the index of the row inducing the order.
@param column the index of the column inducing the order.
@return a new matrix view having slices sorted by the values of the slice view <tt>matrix.viewRow(row).viewColumn(column)</tt>.
		<b>Note that the original matrix is left unaffected.</b>
@throws IndexOutOfBoundsException if <tt>row < 0 || row >= matrix.rows() || column < 0 || column >= matrix.columns()</tt>.
*/
public static ObjectMatrix3D quickSort(ObjectMatrix3D matrix, int row, int column) {
	if (row < 0 || row >= matrix.rows()) throw new IndexOutOfBoundsException("row="+row+", matrix="+Formatter.shape(matrix));
	if (column < 0 || column >= matrix.columns()) throw new IndexOutOfBoundsException("column="+column+", matrix="+Formatter.shape(matrix));

	int[] sliceIndexes = new int[matrix.slices()]; // indexes to reorder instead of matrix itself
	for (int i=sliceIndexes.length; --i >= 0; ) sliceIndexes[i] = i;

	final ObjectMatrix1D sliceView = matrix.viewRow(row).viewColumn(column);
	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			Comparable av = (Comparable) (sliceView.getQuick(a));
			Comparable bv = (Comparable) (sliceView.getQuick(b));
			int r = av.compareTo(bv);
			return r<0 ? -1 : (r>0 ? 1 : 0);
		}
	};

	cern.colt.Sorting.quickSort(sliceIndexes,0,sliceIndexes.length,comp);

	// view the matrix according to the reordered slice indexes
	// take all rows and columns in the original order
	return matrix.viewSelection(sliceIndexes,null,null);
}
/**
Sorts the matrix slices according to the order induced by the specified comparator.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
The algorithm compares two slices (2-d matrices) at a time, determinining whether one is smaller, equal or larger than the other.
To sort ranges use sub-ranging views. To sort by other dimensions, use dice views. To sort descending, use flip views ...
<p>
<b>Example:</b>
<pre>
// sort by sum of values in a slice
ObjectMatrix2DComparator comp = new ObjectMatrix2DComparator() {
&nbsp;&nbsp;&nbsp;public int compare(ObjectMatrix2D a, ObjectMatrix2D b) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Object as = a.zSum(); Object bs = b.zSum();
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return as < bs ? -1 : as == bs ? 0 : 1;
&nbsp;&nbsp;&nbsp;}
};
sorted = quickSort(matrix,comp);
</pre>

@param matrix the matrix to be sorted.
@param c the comparator to determine the order.
@return a new matrix view having slices sorted as specified.
		<b>Note that the original matrix is left unaffected.</b>
*/
public static ObjectMatrix3D quickSort(final ObjectMatrix3D matrix, final ObjectMatrix2DComparator c) {
	int[] sliceIndexes = new int[matrix.slices()]; // indexes to reorder instead of matrix itself
	for (int i=sliceIndexes.length; --i >= 0; ) sliceIndexes[i] = i;

	final ObjectMatrix2D[] views = new ObjectMatrix2D[matrix.slices()]; // precompute views for speed
	for (int i=views.length; --i >= 0; ) views[i] = matrix.viewSlice(i);

	IntComparator comp = new IntComparator() {  
		public int compare(int a, int b) {
			//return c.compare(matrix.viewSlice(a), matrix.viewSlice(b));
			return c.compare(views[a], views[b]);
		}
	};

	cern.colt.Sorting.quickSort(sliceIndexes,0,sliceIndexes.length,comp);

	// view the matrix according to the reordered slice indexes
	// take all rows and columns in the original order
	return matrix.viewSelection(sliceIndexes,null,null);
}
}
