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
An IAxis represents a binned histogram axis. A 1D Histogram would have
one Axis representing the X axis, while a 2D Histogram would have two
axes representing the X and Y Axis. 

@author Pavel Binko, Dino Ferrero Merlino, Wolfgang Hoschek, Tony Johnson, Andreas Pfeiffer, and others.
@version 1.0, 23/03/2000
*/
public interface IAxis extends java.io.Serializable
{
	/**
	 * Centre of the bin specified.
	 * @param index Bin number (0...bins()-1) or OVERFLOW or UNDERFLOW.
	 */ 
	public double binCentre(int index );
	/**
	 * Lower edge of the specified bin.
	 * @param index Bin number (0...bins()-1) or OVERFLOW or UNDERFLOW.
	 * @return the lower edge of the bin; for the underflow bin this is <tt>Double.NEGATIVE_INFINITY</tt>.
	 */
	public double binLowerEdge(int index );
	/** 
	 * The number of bins (excluding underflow and overflow) on the axis.
	 */
	public int bins();
	/**
	 * Upper edge of the specified bin.
	 * @param index Bin number (0...bins()-1) or OVERFLOW or UNDERFLOW.
	 * @return the upper edge of the bin; for the overflow bin this is <tt>Double.POSITIVE_INFINITY</tt>.
	 */ 
	public double binUpperEdge(int index );
	/**
	 * Width of the bin specified.
	 * @param index Bin number (0...bins()-1) or OVERFLOW or UNDERFLOW.
	 */ 
	public double binWidth(int index);
	/**
	 * Converts a coordinate on the axis to a bin number. If the coordinate
	 * is < lowerEdge returns UNDERFLOW, and if the coordinate is >= 
	 * upperEdge returns OVERFLOW.
	 */
	public int coordToIndex(double coord);
	/**
	 *  Lower axis edge.
	 */
	public double lowerEdge();
	/**
	 *  Upper axis edge.
	 */
	public double upperEdge();
}
