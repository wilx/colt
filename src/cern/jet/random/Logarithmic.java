/*
Copyright � 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
package cern.jet.random;

import edu.cornell.lassp.houle.RngPack.RandomElement;
/**
 * Logarithmic distribution.
 * <p>
 * Valid parameter ranges: <tt>0 &lt; p &lt; 1</tt>.
 * <p>
 * Instance methods operate on a user supplied uniform random number generator; they are unsynchronized.
 * <dt>
 * Static methods operate on a default uniform random number generator; they are synchronized.
 * <p>
 * <b>Implementation:</b> 
 * <dt>
 * Method: Inversion/Transformation.
 * <dt>
 * This is a port of <tt>lsk.c</tt> from the <A HREF="http://www.cis.tu-graz.ac.at/stat/stadl/random.html">C-RAND / WIN-RAND</A> library.
 * C-RAND's implementation, in turn, is based upon
 * <p>
 * A.W. Kemp (1981): Efficient generation of logarithmically distributed pseudo-random variables, Appl. Statist. 30, 249-253.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public class Logarithmic extends AbstractContinousDistribution {
	protected double my_p;

	// cached vars for method nextDouble(a) (for performance only)
 	private double t,h,a_prev = -1.0;

 	// The uniform random number generated shared by all <b>static</b> methods. 
	protected static Logarithmic shared = new Logarithmic(0.5,makeDefaultGenerator());
/**
 * Constructs a Logarithmic distribution.
 */
public Logarithmic(double p, RandomElement randomGenerator) {
	setRandomGenerator(randomGenerator);
	setState(p);
}
/**
 * Returns a random number from the distribution.
 */
public double nextDouble() {
	return nextDouble(this.my_p);
}
/**
 * Returns a random number from the distribution; bypasses the internal state.
 */
public double nextDouble(double a) {
/******************************************************************
 *                                                                *
 *      Logarithmic Distribution - Inversion/Transformation       *
 *                                                                *
 ******************************************************************
 *                                                                *
 * The algorithm combines Inversion and Transformation.           *
 * It is based on the following fact: A random variable X from    *
 * the Logarithmic distribution has the property that X for fixed *
 * Y=y is Geometric distributed with P(X=x|Y=y)=(1-y)*y^(x-1) (*) *
 * where Y has distribution function F(y)=ln(1-y)/ln(1-p).        *
 * So first random numbers y are generated by simple Inversion,   *
 * then k=(long int) (1+ln(u)/ln(y)) is a Geometric random number *
 * and because of (*) a Logarithmic one.                          *
 * To speed up the algorithm squeezes are used as well as the     *
 * fact, that many of the random numbers are 1 or 2 (depending on *
 * special circumstances).                                        *
 * On an IBM/PC 486 optimal performance is achieved, if for p<.97 *
 * simple inversion is used and otherwise the transformation.     *
 * On an IBM/PC 286 inversion should be restricted to p<.90.      *
 *                                                                *
 ******************************************************************
 *                                                                *
 * FUNCTION:    - lsk  samples a random number from the           *
 *                Logarithmic distribution with                   *
 *                parameter  0 < p < 1 .                          *
 * REFERENCE:   - A.W. Kemp (1981): Efficient generation of       *
 *                logarithmically distributed pseudo-random       *
 *                variables, Appl. Statist. 30, 249-253.          *
 * SUBPROGRAMS: - drand(seed) ... (0,1)-Uniform generator with    *
 *                unsigned long integer *seed.                    *
 *                                                                *
 ******************************************************************/
	double u,v,p,q;
	int k;

	if (a != a_prev) {                   // Set-up
		a_prev = a;
		if (a<0.9) t = -a / Math.log(1.0 - a);
		else h=Math.log(1.0 - a);
	}

	u=randomGenerator.raw();
	if (a<0.97) {                        // Inversion/Chop-down 
		k = 1;
		p = t;
		while (u > p) {
			u -= p;
			k++;
			p *= a * (k-1.0)/(double)k;
		}
		return k;
	}

	if (u > a) return 1;                 // Transformation
	u=randomGenerator.raw();
	v = u;
	q = 1.0 - Math.exp(v * h);
	if ( u <= q * q) {
		k = (int) (1 + Math.log(u) / Math.log(q));
		return k;
	}
	if (u > q) return 1;
	return 2;
}
/**
 * Sets the distribution parameter.
 */
public void setState(double p) {
	this.my_p = p;
}
/**
 * Returns a random number from the distribution.
 */
public static double staticNextDouble(double p) {
	synchronized (shared) {
		return shared.nextDouble(p);
	}
}
/**
 * Returns a String representation of the receiver.
 */
public String toString() {
	return this.getClass().getName()+"("+my_p+")";
}
/**
 * Sets the uniform random number generated shared by all <b>static</b> methods.
 * @param randomGenerator the new uniform random number generator to be shared.
 */
private static void xstaticSetRandomGenerator(RandomElement randomGenerator) {
	synchronized (shared) {
		shared.setRandomGenerator(randomGenerator);
	}
}
}
