/*
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*/
package hep.aida.bin;

/** 
Function objects computing dynamic bin aggregations; to be passed to generic methods.
@see cern.colt.matrix.doublealgo.Formatter
@see cern.colt.matrix.doublealgo.Statistic
@author wolfgang.hoschek@cern.ch
@version 1.0, 09/24/99
*/
public class BinFunctions1D extends Object {
	/**
	Little trick to allow for "aliasing", that is, renaming this class.
	Using the aliasing you can instead write
	<p>
	<tt>BinFunctions F = BinFunctions.functions; <br>
	someAlgo(F.max);</tt>
	*/
	public static final BinFunctions1D functions = new BinFunctions1D();

	/**
	 * Function that returns <tt>bin.max()</tt>.
	 */
	public static final BinFunction1D max = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.max(); }
		public final String name() { return "Max"; }
	};		

	/**
	 * Function that returns <tt>bin.mean()</tt>.
	 */
	public static final BinFunction1D mean = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.mean(); }
		public final String name() { return "Mean"; }
	};		

	/**
	 * Function that returns <tt>bin.median()</tt>.
	 */
	public static final BinFunction1D median = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.median(); }
		public final String name() { return "Median"; }
	};		

	/**
	 * Function that returns <tt>bin.min()</tt>.
	 */
	public static final BinFunction1D min = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.min(); }
		public final String name() { return "Min"; }
	};		

	/**
	 * Function that returns <tt>bin.rms()</tt>.
	 */
	public static final BinFunction1D rms = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.rms(); }
		public final String name() { return "RMS"; }
	};		

	/**
	 * Function that returns <tt>bin.size()</tt>.
	 */
	public static final BinFunction1D size = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.size(); }
		public final String name() { return "Size"; }
	};		

	/**
	 * Function that returns <tt>bin.standardDeviation()</tt>.
	 */
	public static final BinFunction1D stdDev = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.standardDeviation(); }
		public final String name() { return "StdDev"; }
	};		

	/**
	 * Function that returns <tt>bin.sum()</tt>.
	 */
	public static final BinFunction1D sum = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.sum(); }
		public final String name() { return "Sum"; }
	};
	/**
	 * Function that returns <tt>bin.sumOfLogarithms()</tt>.
	 */
	public static final BinFunction1D sumLog = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.sumOfLogarithms(); }
		public final String name() { return "SumLog"; }
	};
	/**
	 * Function that returns <tt>bin.geometricMean()</tt>.
	 */
	public static final BinFunction1D geometricMean = new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.geometricMean(); }
		public final String name() { return "GeomMean"; }
	};		


/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected BinFunctions1D() {}
/**
 * Function that returns <tt>bin.quantile(percentage)</tt>.
 * @param the percentage of the quantile (<tt>0 <= percentage <= 1</tt>).
 */
public static BinFunction1D quantile(final double percentage) {
	return new BinFunction1D() {
		public final double apply(DynamicBin1D bin) { return bin.quantile(percentage); }
		public final String name() { return new corejava.Format("%1.2G").form(percentage*100)+"% Q."; }
	};
}	

}
