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
 * Interface that represents a function object: a function that takes 
 * two bins as arguments and returns a single value.
 */
public interface BinFunction1D {
/**
 * Applies a function to one bin argument.
 *
 * @param x   the argument passed to the function.
 * @return the result of the function.
 */
abstract public double apply(DynamicBin1D x);
/**
 * Returns the name of this function.
 *
 * @return the name of this function.
 */
abstract public String name();
}
