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
 * Convenient histogram utilities. 
 */
class Util
{
	/**
	 * Creates a new utility object.
	 */
	public Util() {}
	/** 
	 * Returns the index of the in-range bin containing the maxBinHeight().
	 */ 
	public int maxBin(IHistogram1D h)
	{
		int maxBin = -1;
		double maxValue = Double.MIN_VALUE;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			double value = h.binHeight(i);
			if (value > maxValue) {
				maxValue = value;
				maxBin = i;
			}
		}
		return maxBin;
	}
	/** 
	 * Returns the indexX of the in-range bin containing the maxBinHeight().
	 */ 
	public int maxBinX(IHistogram2D h)
	{
		double maxValue = Double.MIN_VALUE;
		int maxBinX = -1;
		int maxBinY = -1;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			for (int j=h.yAxis().bins(); --j >= 0; ) {
				double value = h.binHeight(i,j);
				if (value > maxValue) {
					maxValue = value;
					maxBinX = i;
					maxBinY = j;
				}
			}
		}
		return maxBinX;
	}
	/** 
	 * Returns the indexY of the in-range bin containing the maxBinHeight().
	 */ 
	public int maxBinY(IHistogram2D h)
	{
		double maxValue = Double.MIN_VALUE;
		int maxBinX = -1;
		int maxBinY = -1;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			for (int j=h.yAxis().bins(); --j >= 0; ) {
				double value = h.binHeight(i,j);
				if (value > maxValue) {
					maxValue = value;
					maxBinX = i;
					maxBinY = j;
				}
			}
		}
		return maxBinY;
	}
	/** 
	 * Returns the index of the in-range bin containing the minBinHeight().
	 */ 
	public int minBin(IHistogram1D h)
	{
		int minBin = -1;
		double minValue = Double.MAX_VALUE;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			double value = h.binHeight(i);
			if (value < minValue) {
				minValue = value;
				minBin = i;
			}
		}
		return minBin;
	}
	/** 
	 * Returns the indexX of the in-range bin containing the minBinHeight().
	 */ 
	public int minBinX(IHistogram2D h)
	{
		double minValue = Double.MAX_VALUE;
		int minBinX = -1;
		int minBinY = -1;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			for (int j=h.yAxis().bins(); --j >= 0; ) {
				double value = h.binHeight(i,j);
				if (value < minValue) {
					minValue = value;
					minBinX = i;
					minBinY = j;
				}
			}
		}
		return minBinX;
	}
	/** 
	 * Returns the indexY of the in-range bin containing the minBinHeight().
	 */ 
	public int minBinY(IHistogram2D h)
	{
		double minValue = Double.MAX_VALUE;
		int minBinX = -1;
		int minBinY = -1;
		for (int i=h.xAxis().bins(); --i >= 0; ) {
			for (int j=h.yAxis().bins(); --j >= 0; ) {
				double value = h.binHeight(i,j);
				if (value < minValue) {
					minValue = value;
					minBinX = i;
					minBinY = j;
				}
			}
		}
		return minBinY;
	}
}
