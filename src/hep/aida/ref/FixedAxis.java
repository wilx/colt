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
package hep.aida.ref;

import hep.aida.*;
/**
Fixed-width axis; A reference implementation of hep.aida.IAxis.

@author Wolfgang Hoschek, Tony Johnson, and others.
@version 1.0, 23/03/2000
*/
public class FixedAxis implements IAxis
{
	private int bins;
	private double min;
	private double binWidth;
	// Package private for ease of use in Histogram1D and Histogram2D
	private int xunder, xover;
	/**
	 * Create an Axis
	 * @param bins Number of bins
	 * @param min Minimum for axis
	 * @param max Maximum for axis
	 */
	public FixedAxis(int bins, double min, double max)
	{
		if (bins < 1) throw new IllegalArgumentException("bins="+bins);
		if (max <= min) throw new IllegalArgumentException("max <= min");
		
		// Note, for internal consistency we save only min and binWidth
		// and always use these quantities for all calculations. Due to 
		// rounding errors the return value from upperEdge is not necessarily
		// exactly equal to max
		
		this.bins = bins;
		this.min = min;
		this.binWidth =  (max - min)/bins;
		
		// our internal definition of overflow/underflow differs from
		// that of the outside world
		//this.under = 0;
		//this.over = bins+1;

	}
	public double binCentre(int index)
	{
		return min + binWidth*index + binWidth/2;
	}
	public double binLowerEdge(int index)
	{
		if (index == IHistogram.UNDERFLOW) return Double.NEGATIVE_INFINITY;
		if (index == IHistogram.OVERFLOW) return upperEdge();
		return min + binWidth*index;
	}
	public int bins()
	{
		return bins;
	}
	public double binUpperEdge(int index)
	{
		if (index == IHistogram.UNDERFLOW) return min;
		if (index == IHistogram.OVERFLOW) return Double.POSITIVE_INFINITY;
		return min + binWidth*(index+1);
	}
	public double binWidth(int index)
	{
		return binWidth;
	}
	public int coordToIndex(double coord)
	{
		if (coord < min) return IHistogram.UNDERFLOW;
		int index = (int) Math.floor((coord - min)/binWidth);
		if (index >= bins) return IHistogram.OVERFLOW;
		
		return index;
	}
	public double lowerEdge()
	{
		return min;
	}
	public double upperEdge()
	{
		return min + binWidth*bins;
	}
	/**
	 * This package private method is similar to coordToIndex except
	 * that it returns our internal definition for overflow/underflow
	 */
	int xgetBin(double coord)
	{
		if (coord < min) return xunder; 
		int index = (int) Math.floor((coord - min)/binWidth);
		if (index > bins) return xover; 
		return index+1;
	}
	/**
	 * Package private method to map from the external representation of bin
	 * number to our internal representation of bin number
	 */
	int xmap(int index)
	{
		if (index >= bins) throw new IllegalArgumentException("bin="+index);
		if (index >= 0) return index+1;
		if (index == IHistogram.UNDERFLOW) return xunder;
		if (index == IHistogram.OVERFLOW) return xover;
		throw new IllegalArgumentException("bin="+index);
	}
}
