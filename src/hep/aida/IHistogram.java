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
A common base class for IHistogram1D and IHistogram2D.

@author Pavel Binko, Dino Ferrero Merlino, Wolfgang Hoschek, Tony Johnson, Andreas Pfeiffer, and others.
@version 1.0, 23/03/2000
*/
public interface IHistogram extends java.io.Serializable
{
	/**
	 * Constant specifying the overflow bin (can be passed to any method expecting a bin number).
	 */
	public final static int OVERFLOW = -1;
	/**
	 * Constant specifying the underflow bin (can be passed to any method expecting a bin number).
	 */
	public final static int UNDERFLOW = -2;
	/**
	 * Number of all entries in all (both in-range and under/overflow) bins in the histogram.
	 */ 
	public int allEntries();
	/**
	 * Returns 1 for one-dimensional histograms, 2 for two-dimensional histograms, and so on.
	 */ 
	public int dimensions();
	/**
	 * Number of in-range entries in the histogram.
	 */ 
	public int entries();
	/** 
	 * Number of equivalent entries.
	 * @return <tt>SUM[ weight ] ^ 2 / SUM[ weight^2 ]</tt>.
	 */ 
	public double equivalentBinEntries();
	/**
	 * Number of under and overflow entries in the histogram.
	 */ 
	public int extraEntries();
	/**
	 * Reset contents; as if just constructed.
	 */
	public void reset();
	/**
	 * Sum of all (both in-range and under/overflow) bin heights in the histogram.
	 */ 
	public double sumAllBinHeights();
	/**
	 * Sum of in-range bin heights in the histogram.
	 */ 
	public double sumBinHeights();
	/**
	 * Sum of under/overflow bin heights in the histogram.
	 */ 
	public double sumExtraBinHeights();
	/**
	 *  Title of the histogram (will be set only in the constructor).
	 */ 
	public String title();
}
