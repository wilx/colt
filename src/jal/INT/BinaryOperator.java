package jal.INT;

/**
 * Interface that represents a function object: a function that takes two
 *  arguments and returns a value of the same type.
 *
 * <P>Copyright &copy; 1996
 * Silicon Graphics, Inc.
 *
 * <BR>Permission to use, copy, modify, distribute and sell this software
 * and its documentation for any purpose is hereby granted without fee,
 * provided that the above copyright notice appear in all copies and
 * that both that copyright notice and this permission notice appear
 * in supporting documentation.  Silicon Graphics makes no
 * representations about the suitability of this software for any
 * purpose.  It is provided &quot;as is&quot; without express or 
 * implied warranty.
 *
 *
 * @see Generator
 * @see UnaryOperator
 * @author Matthew Austern (austern@mti.sgi.com)
 * @author Alexander Stepanov (stepanov@mti.sgi.com)
 */

public interface BinaryOperator {
  /**
   * Performs a function f on a supplied argument
   * @param x   First argument
   * @param y   Second argument    
   * @return    f(x,y).
   */
  abstract public int apply(int x, int y);
}
