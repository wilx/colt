package hep.aida.bin;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
/**
 * Abstract base class for all 1-dimensional bins consumes <tt>double</tt> elements.
 * First see the <a href="package-summary.html">package summary</a> and javadoc <a href="package-tree.html">tree view</a> to get the broad picture.
 * <p>
 * This class is fully thread safe (all public methods are synchronized).
 * Thus, you can have one or more threads adding to the bin as well as one or more threads reading and viewing the statistics of the bin <i>while it is filled</i>.
 * For high performance, add data in large chunks (buffers) via method <tt>addAllOf</tt> rather than piecewise via method <tt>add</tt>.
 * 
 * @author wolfgang.hoschek@cern.ch
 * @version 0.9, 03-Jul-99
 */
public abstract class AbstractBin1D extends AbstractBin implements cern.colt.buffer.DoubleBufferConsumer {
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected AbstractBin1D() {
}
/**
 * Adds the specified element to the receiver.
 *
 * @param element element to be appended.
 */
public abstract void add(double element);
/**
 * Adds all values of the specified list to the receiver.
 * @param list the list of which all values shall be added.
 */
public final synchronized void addAllOf(DoubleArrayList list) {
	addAllOfFromTo(list,0,list.size()-1);
}
/**
 * Adds the part of the specified list between indexes <tt>from</tt> (inclusive) and <tt>to</tt> (inclusive) to the receiver.
 * You may want to override this method for performance reasons.
 *
 * @param list the list of which elements shall be added.
 * @param from the index of the first element to be added (inclusive).
 * @param to the index of the last element to be added (inclusive).
 * @throws IndexOutOfBoundsException if <tt>list.size()&gt;0 && (from&lt;0 || from&gt;to || to&gt;=list.size())</tt>.
 */
public synchronized void addAllOfFromTo(DoubleArrayList list, int from, int to) {
	for (int i=from; i<=to; i++) add(list.getQuick(i));
}
/**
 * Constructs and returns a streaming buffer connected to the receiver.
 * Whenever the buffer is full it's contents are automatically flushed to <tt>this</tt>. 
 * (Addding elements via a buffer to a bin is significantly faster than adding them directly.)
 * @param capacity the number of elements the buffer shall be capable of holding before overflowing and flushing to the receiver.
 * @return a streaming buffer having the receiver as target.
 */
public synchronized cern.colt.buffer.DoubleBuffer buffered(int capacity) {
	return new cern.colt.buffer.DoubleBuffer(this,capacity);
}
/**
 * Computes the deviations from the receiver's measures to another bin's measures.
 * @param other the other bin to compare with
 * @return a summary of the deviations.
 */
public String compareWith(AbstractBin1D other) {
	StringBuilder buf = new StringBuilder();
	buf.append("\nDifferences [percent]");
	buf.append("\nSize: ").append(relError(size(), other.size())).append(" %");
	buf.append("\nSum: ").append(relError(sum(), other.sum())).append(" %");
	buf.append("\nSumOfSquares: ").append(relError(sumOfSquares(), other.sumOfSquares())).append(" %");
	buf.append("\nMin: ").append(relError(min(), other.min())).append(" %");
	buf.append("\nMax: ").append(relError(max(), other.max())).append(" %");
	buf.append("\nMean: ").append(relError(mean(), other.mean())).append(" %");
	buf.append("\nRMS: ").append(relError(rms(), other.rms())).append(" %");
	buf.append("\nVariance: ").append(relError(variance(), other.variance())).append(" %");
	buf.append("\nStandard deviation: ").append(relError(standardDeviation(), other.standardDeviation())).append(" %");
	buf.append("\nStandard error: ").append(relError(standardError(), other.standardError())).append(" %");
	buf.append("\n");
	return buf.toString();
}
/**
 * Returns whether two bins are equal; 
 * They are equal if the other object is of the same class or a subclass of this class and both have the same size, minimum, maximum, sum and sumOfSquares.
 */
public boolean equals(Object object) {
	if (!(object instanceof AbstractBin1D)) return false;
	AbstractBin1D other = (AbstractBin1D) object;
	return size()==other.size() && min()==other.min() && max()==other.max()
		&& sum()==other.sum() && sumOfSquares()==other.sumOfSquares();
}
/**
 * Returns the maximum.
 */
public abstract double max();
/**
 * Returns the arithmetic mean, which is <tt>Sum( x[i] ) / size()</tt>.
 */
public synchronized double mean() {
	return sum() / size();
}
/**
 * Returns the minimum.
 */
public abstract double min();
/**
 * Computes the relative error (in percent) from one measure to another.
 */
protected double relError(double measure1, double measure2) {
	return 100 * (1 - measure1 / measure2);
}
/**
 * Returns the rms (Root Mean Square), which is <tt>Math.sqrt( Sum( x[i]*x[i] ) / size() )</tt>.
 */
public synchronized double rms() {
	return Descriptive.rms(size(), sumOfSquares());
}
/**
 * Returns the sample standard deviation, which is <tt>Math.sqrt(variance())</tt>.
 */
public synchronized double standardDeviation() {
	return Math.sqrt(variance());
}
/**
 * Returns the sample standard error, which is <tt>Math.sqrt(variance() / size())</tt>
 */
public synchronized double standardError() {
	return Descriptive.standardError(size(),variance());
}
/**
 * Returns the sum of all elements, which is <tt>Sum( x[i] )</tt>.
 */
public abstract double sum();
/**
 * Returns the sum of squares, which is <tt>Sum( x[i] * x[i] )</tt>.
 */
public abstract double sumOfSquares();
/**
 * Returns a String representation of the receiver.
 */
public synchronized String toString() {
	StringBuilder buf = new StringBuilder();
	buf.append(getClass().getName());
	buf.append("\n-------------");
	buf.append("\nSize: ").append(size());
	buf.append("\nSum: ").append(sum());
	buf.append("\nSumOfSquares: ").append(sumOfSquares());
	buf.append("\nMin: ").append(min());
	buf.append("\nMax: ").append(max());
	buf.append("\nMean: ").append(mean());
	buf.append("\nRMS: ").append(rms());
	buf.append("\nVariance: ").append(variance());
	buf.append("\nStandard deviation: ").append(standardDeviation());
	buf.append("\nStandard error: ").append(standardError());
	/*
	buf.append("\nValue: "+value());
	buf.append("\nError(0): "+error(0));
	*/
	buf.append("\n");
	return buf.toString();
}
/**
 * Trims the capacity of the receiver to be the receiver's current size.
 * Releases any superfluos internal memory.
 * An application can use this operation to minimize the storage of the receiver.
 * This default implementation does nothing.
 */
public synchronized void trimToSize() {}
/**
 * Returns the sample variance, which is <tt>Sum( (x[i]-mean())<sup>2</sup> )  /  (size()-1)</tt>.
 */
public synchronized double variance() {
	return Descriptive.sampleVariance(size(), sum(), sumOfSquares());
}
}
