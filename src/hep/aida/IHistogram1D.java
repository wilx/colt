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
package hep.aida;

/**
A Java interface corresponding to the AIDA 1D Histogram.
<p> 
<b>Note</b> All methods that accept a bin number as an argument will
also accept the constants OVERFLOW or UNDERFLOW as the argument, and 
as a result give the contents of the resulting OVERFLOW or UNDERFLOW
bin.
@see <a href="http://wwwinfo.cern.ch/asd/lhc++/AIDA/">AIDA</a>
@author Pavel Binko, Dino Ferrero Merlino, Wolfgang Hoschek, Tony Johnson, Andreas Pfeiffer, and others.
@version 1.0, 23/03/2000
*/
public interface IHistogram1D extends IHistogram
{
	/**
	 * Number of entries in the corresponding bin (ie the number of times fill was called for this bin).
	 * @param index the bin number (0...N-1) or OVERFLOW or UNDERFLOW.
	 */
	public int binEntries(int index);
	/**
	 * The error on this bin.
	 * @param index the bin number (0...N-1) or OVERFLOW or UNDERFLOW.
	 */ 
	public double binError(int index );
	/**
	 * Total height of the corresponding bin (ie the sum of the weights in this bin).
	 * @param index the bin number (0...N-1) or OVERFLOW or UNDERFLOW.
	 */ 
	public double binHeight(int index);
	/**
	 * Fill histogram with weight 1.
	 */ 
	public void fill(double x);
	/**
	 * Fill histogram with specified weight.
	 */
	public void fill(double x, double weight);
	/**
	 * Returns the mean of the whole histogram as calculated on filling-time.
	 */ 
	public double mean();
	/** 
	 * Indexes of the in-range bins containing the smallest and largest binHeight(), respectively.
	 * @return <tt>{minBin,maxBin}</tt>.
	 */ 
	public int[] minMaxBins();
	/**
	 * Returns the rms of the whole histogram as calculated on filling-time.
	 */ 
	public double rms();
	/**
	 * Returns the X Axis.
	 */
	public IAxis xAxis();
}
