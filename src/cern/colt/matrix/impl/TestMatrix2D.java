/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
package cern.colt.matrix.impl;

import cern.colt.list.*;
import cern.colt.map.*;
import edu.cornell.lassp.houle.RngPack.RandomElement;
import cern.colt.matrix.linalg.*;
import cern.colt.matrix.*;
import cern.colt.function.DoubleFunction;
import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.doublealgo.DoubleMatrix2DComparator;
/**
 * Quick and dirty tests.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public class TestMatrix2D {
	private static final cern.jet.math.Functions F = cern.jet.math.Functions.functions;
	private static final cern.colt.matrix.DoubleFactory2D Factory2D = cern.colt.matrix.DoubleFactory2D.dense;
	private static final cern.colt.matrix.DoubleFactory1D Factory1D = cern.colt.matrix.DoubleFactory1D.dense;
	private static final cern.colt.matrix.linalg.Algebra LinearAlgebra = cern.colt.matrix.linalg.Algebra.DEFAULT;
	private static final cern.colt.matrix.doublealgo.Transform Transform = cern.colt.matrix.doublealgo.Transform.transform;
	private static final cern.colt.matrix.linalg.Property Property = cern.colt.matrix.linalg.Property.DEFAULT;
/**
 * Makes this class non instantiable, but still let's others inherit from it.
 */
protected TestMatrix2D() {
	throw new RuntimeException("Non instantiable");
}
/**
 */
public static void doubleTest() {
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = new DenseDoubleMatrix2D(rows,columns);
System.out.println(master);
master.assign(1); // set all cells to 1
System.out.println("\n"+master);
master.viewPart(2,1,2,3).assign(2); // set [2,1] .. [3,3] to 2
System.out.println("\n"+master);

DoubleMatrix2D copyPart = master.viewPart(2,1,2,3).copy();
copyPart.assign(3); // modify an independent copy
copyPart.set(0,0,4); 
System.out.println("\n"+copyPart); // has changed
System.out.println("\n"+master); // master has not changed

DoubleMatrix2D view1 = master.viewPart(0,3,4,2); // [0,3] .. [3,4]
DoubleMatrix2D view2 = view1.viewPart(0,0,4,1); // a view from a view 
System.out.println("\n"+view1);
System.out.println("\n"+view2);
}
/**
 */
public static void doubleTest(int rows, int columns, int initialCapacity, double minLoadFactor, double maxLoadFactor) {
	DoubleMatrix2D matrix = new SparseDoubleMatrix2D(rows,columns,initialCapacity,minLoadFactor,maxLoadFactor);
	System.out.println(matrix);
	
	System.out.println("adding...");
	int i=0;
	for (int column=0; column < columns; column++) {
		for (int row=0; row < rows; row++) {
			//if (i%1000 == 0) { 
 				matrix.set(row,column,i);
			//}
			i++;
		}
	}
	System.out.println(matrix);

	System.out.println("removing...");
	for (int column=0; column < columns; column++) {
		for (int row=0; row < rows; row++) {
			//if (i%1000 == 0) {
				matrix.set(row,column,0);
			//}
		}
	}

	System.out.println(matrix);
	System.out.println("bye bye.");
}
/**
 */
public static void doubleTest10() {
int rows = 6;
int columns = 7; // make a 4*5 matrix
//DoubleMatrix2D master = new DenseDoubleMatrix2D(rows,columns);
DoubleMatrix2D master = Factory2D.ascending(rows,columns);
//Basic.ascending(master);
//master.assign(1); // set all cells to 1
Transform.mult(master,Math.sin(0.3));
System.out.println("\n"+master);
//master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
//System.out.println("\n"+master);

int[] rowIndexes = {0,1,2,3};
int[] columnIndexes = {0,1,2,3};

int[] rowIndexes2 = {3,0,3};
int[] columnIndexes2 = {3,0,3};
DoubleMatrix2D view1 = master.viewPart(1,1,4,5).viewSelection(rowIndexes,columnIndexes);
System.out.println("\nview1="+view1);
DoubleMatrix2D view9 = view1.viewStrides(2,2).viewStrides(2,1);
System.out.println("\nview9="+view9);
view1 = view1.viewSelection(rowIndexes2,columnIndexes2);
System.out.println("\nview1="+view1);
DoubleMatrix2D view2 = view1.viewPart(1,1,2,2);
System.out.println("\nview2="+view2);
DoubleMatrix2D view3 = view2.viewRowFlip();
System.out.println("\nview3="+view3);
view3.assign(Factory2D.ascending(view3.rows(),view3.columns()));
//Basic.ascending(view3);
System.out.println("\nview3="+view3);

//view2.assign(-1);
System.out.println("\nmaster replaced"+master);
System.out.println("\nview1 replaced"+view1);
System.out.println("\nview2 replaced"+view2);
System.out.println("\nview3 replaced"+view3);

}
/**
 */
public static void doubleTest11() {
int rows = 4;
int columns = 5; // make a 1*1 matrix
DoubleMatrix2D master = new DenseDoubleMatrix2D(1,1);
master.assign(2);
System.out.println("\n"+master);

int[] rowIndexes = new int[rows];
int[] columnIndexes = new int[columns];

DoubleMatrix2D view1 = master.viewSelection(rowIndexes,columnIndexes);
System.out.println(view1);

master.assign(1);
System.out.println("\n"+master);
System.out.println(view1);
}
/**
 */
public static void doubleTest12() {
DoubleMatrix2D A,B,C,D,E,F,G,H,I,J;
A = Factory2D.make(2,3,9);
B = Factory2D.make(4,3,8);
C = Factory2D.appendRows(A,B);
System.out.println("\nA="+A);
System.out.println("\nB="+B);
System.out.println("\nC="+C);
D = Factory2D.make(3,2,7);
E = Factory2D.make(3,4,6);
F = Factory2D.appendColumns(D,E);
System.out.println("\nD="+D);
System.out.println("\nE="+E);
System.out.println("\nF="+F);
G = Factory2D.appendRows(C,F);
System.out.println("\nG="+G);
H = Factory2D.ascending(2,3);
System.out.println("\nH="+H);
I = Factory2D.repeat(H,2,3);
System.out.println("\nI="+I);
}
/**
 */
public static void doubleTest13() {
double[] values = {0, 1, 2, 3};
DoubleMatrix1D matrix = new DenseDoubleMatrix1D(values);
System.out.println(matrix);

// Sum( x[i]*x[i] ) 
System.out.println(matrix.viewSelection(
	new cern.colt.function.DoubleProcedure() {
		public final boolean apply(double a) { return a % 2 == 0; }
	}
));
//--> 14

// Sum( x[i]*x[i] ) 
System.out.println(matrix.aggregate(F.plus,F.square));
//--> 14

// Sum( x[i]*x[i]*x[i] ) 
System.out.println(matrix.aggregate(F.plus,F.pow(3)));
//--> 36

// Sum( x[i] ) 
System.out.println(matrix.aggregate(F.plus,F.identity));
//--> 6

// Min( x[i] ) 
System.out.println(matrix.aggregate(F.min,F.identity));
//--> 0

// Max( Sqrt(x[i]) / 2 ) 
System.out.println(matrix.aggregate(F.max,F.chain(F.div(2),F.sqrt)));
//--> 0.8660254037844386

// Number of all cells with 0 <= value <= 2
System.out.println(matrix.aggregate(F.plus,F.between(0,2)));
//--> 3

// Number of all cells with 0.8 <= Log2(value) <= 1.2
System.out.println(matrix.aggregate(F.plus,F.chain(F.between(0.8,1.2),F.log2)));
//--> 1

// Product( x[i] )
System.out.println(matrix.aggregate(F.mult,F.identity));
//--> 0

// Product( x[i] ) of all x[i] > limit
final double limit = 1;
DoubleFunction f = new DoubleFunction() {
	public final double apply(double a) { return a>limit ? a : 1; }
};
System.out.println(matrix.aggregate(F.mult,f));
//--> 6

// Sum( (x[i]+y[i])^2 )
DoubleMatrix1D otherMatrix1D = matrix.copy();
System.out.println(matrix.aggregate(otherMatrix1D, F.plus, F.chain(F.square,F.plus)));
// --> 56


matrix.assign(F.plus(1));
otherMatrix1D = matrix.copy();
//otherMatrix1D.zMult(3);
System.out.println(matrix);
System.out.println(otherMatrix1D);
// Sum(Math.PI * Math.log(otherMatrix1D[i] / matrix[i]))
System.out.println(matrix.aggregate(otherMatrix1D, F.plus, F.chain(F.mult(Math.PI),F.chain(F.log,F.swapArgs(F.div)))));
// or, perhaps less error prone and more readable: 
System.out.println(matrix.aggregate(otherMatrix1D, F.plus,
   new DoubleDoubleFunction() {
	  public double apply(double a, double b) { return Math.PI*Math.log(b/a); }
   }
)
);

DoubleMatrix3D x = cern.colt.matrix.DoubleFactory3D.dense.ascending(2,2,2);
System.out.println(x);

// Sum( x[slice,row,col]*x[slice,row,col] ) 
System.out.println(x.aggregate(F.plus,F.square));
//--> 140


DoubleMatrix3D y = x.copy();
// Sum( (x[i]+y[i])^2 )
System.out.println(x.aggregate(y, F.plus, F.chain(F.square,F.plus)));
//--> 560

System.out.println(matrix.assign(F.random()));
System.out.println(matrix.assign(new cern.jet.random.Poisson(5, cern.jet.random.Poisson.makeDefaultGenerator())));

}
/**
 */
public static void doubleTest14(int r1, int c, int r2) {
double[] values = {0, 1, 2, 3};
DoubleMatrix2D a = DoubleFactory2D.dense.ascending(r1,c);
DoubleMatrix2D b = Transform.mult(DoubleFactory2D.dense.ascending(c,r2), -1);


//System.out.println(a);
//System.out.println(b);
//System.out.println(Basic.product(a,b));
a.assign(0);
b.assign(0);

cern.colt.Timer timer = new cern.colt.Timer().start();
LinearAlgebra.mult(a,b);
timer.stop().display();
}
/**
 */
public static void doubleTest15(int size, int runs) {
System.out.println("\n\n");
double[][] values = 
{
	{ 0, 5, 9 },
	{ 2, 6, 10 },
	{ 3, 7, 11 }
};

//DoubleMatrix2D A = Factory2D.make(values);
DoubleMatrix2D A = Factory2D.make(size,size);
double value = 5;
for (int i=size; --i >= 0; ) {
	A.setQuick(i,i, value);
}
A.viewRow(0).assign(value);

//DoubleMatrix2D A = Factory2D.makeIdentity(size,size);


//DoubleMatrix2D A = Factory2D.makeAscending(size,size).assign(new cern.jet.random.engine.MersenneTwister());
cern.colt.Timer timer = new cern.colt.Timer().start();
DoubleMatrix2D inv = null;
for (int run=0; run<runs; run++) {
	 inv = LinearAlgebra.inverse(A);
}
timer.stop().display();

/*
timer.reset().start();
for (int run=0; run<runs; run++) {
	new Jama.Matrix(A.toArray()).inverse();
}
timer.stop().display();
*/
//System.out.println("A="+A);
//System.out.println("inverse(A)="+inv);
//System.out.println("formatted inverse(A)="+ new Jama.Matrix(inv.toArray()));

/*
-1.0000000000000018, 2.000000000000007, -1.0000000000000047
2.000000000000007, -6.750000000000024, 4.500000000000016
-1.000000000000004, 3.7500000000000133, -2.500000000000009
*/
}
/**
 */
public static void doubleTest17(int size) {
System.out.println("\n\n");

//DoubleMatrix2D A = Factory2D.make(values);
DoubleMatrix2D A = Factory2D.ascending(3,4);
DoubleMatrix2D B = Factory2D.ascending(2,3);
DoubleMatrix2D C = Factory2D.ascending(1,2);
B.assign(F.plus(A.zSum()));
C.assign(F.plus(B.zSum()));


/*
System.out.println("\n"+A);
System.out.println("\n"+B);
System.out.println("\n"+C);
System.out.println("\n"+Factory2D.diag(A,B,C));
*/

//System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A.toString()));
//System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(B.toString()));
//System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(C.toString()));
//System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(Factory2D.diagonal(A,B,C).toString()));
}
/**
 */
public static void doubleTest18(int size) {
System.out.println("\n\n");
int s = 2;

//DoubleMatrix2D A = Factory2D.make(values);
DoubleMatrix2D A00, A01, A02, A10, A11, A12, A20, A21, A22, empty;
empty = Factory2D.make(0,0);

A00 = Factory2D.ascending(s,s);
//A01 = empty;
A01 = Factory2D.ascending(s,s).assign(F.plus(A00.getQuick(s-1,s-1)));
A02 = Factory2D.ascending(s,s).assign(F.plus(A01.getQuick(s-1,s-1)));
A10 = Factory2D.ascending(s,s).assign(F.plus(A02.getQuick(s-1,s-1)));
A11 = null;
//A11 = Factory2D.ascending(s,s).assign(F.plus(A10.getQuick(s-1,s-1)));
A12 = Factory2D.ascending(s,s).assign(F.plus(A10.getQuick(s-1,s-1)));
//A12 = Factory2D.ascending(s,s).assign(F.plus(A11.getQuick(s-1,s-1)));
A20 = Factory2D.ascending(s,s).assign(F.plus(A12.getQuick(s-1,s-1)));
A21 = empty;
//A21 = Factory2D.ascending(s,s).assign(F.plus(A20.getQuick(s-1,s-1)));
A22 = Factory2D.ascending(s,s).assign(F.plus(A20.getQuick(s-1,s-1)));
//A22 = Factory2D.ascending(s,s).assign(F.plus(A21.getQuick(s-1,s-1)));


//B.assign(F.plus(A.zSum()));
//C.assign(F.plus(B.zSum()));




System.out.println("\n"+A00);
System.out.println("\n"+A01);
System.out.println("\n"+A02);
System.out.println("\n"+A10);
System.out.println("\n"+A11);
System.out.println("\n"+A12);
System.out.println("\n"+A20);
System.out.println("\n"+A21);
System.out.println("\n"+A22);
//System.out.println("\n"+Factory2D.make33(A00,A01,A02,A10,A11,A12,A20,A21,A22));


/*
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A00.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A01.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A02.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A10.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A11.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A12.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A20.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A21.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A22.toString()));

System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(Factory2D.make33(A00,A01,A02,A10,A11,A12,A20,A21,A22).toString()));
*/
}
/**
 */
public static void doubleTest19() {
System.out.println("\n\n");
DoubleMatrix2D A;
int k;
int uk;
int lk;

double[][] values5 = 
{
	{ 0, 0, 0, 0 },
	{ 0, 0, 0, 0 },
	{ 0, 0, 0, 0 },
	{ 0, 0, 0, 0 }
};
A = Factory2D.make(values5);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);

System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

double[][] values4 = 
{
	{ 1, 0, 0, 0 },
	{ 0, 0, 0, 0 },
	{ 0, 0, 0, 0 },
	{ 0, 0, 0, 1 }
};
A = Factory2D.make(values4);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

double[][] values1 = 
{
	{ 1, 1, 0, 0 },
	{ 1, 1, 1, 0 },
	{ 0, 1, 1, 1 },
	{ 0, 0, 1, 1 }
};
A = Factory2D.make(values1);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));


double[][] values6 = 
{
	{ 0, 1, 1, 1 },
	{ 0, 1, 1, 1 },
	{ 0, 0, 0, 1 },
	{ 0, 0, 0, 1 }
};
A = Factory2D.make(values6);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

double[][] values7 = 
{
	{ 0, 0, 0, 0 },
	{ 1, 1, 0, 0 },
	{ 1, 1, 0, 0 },
	{ 1, 1, 1, 1 }
};
A = Factory2D.make(values7);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));


double[][] values2 = 
{
	{ 1, 1, 0, 0 },
	{ 0, 1, 1, 0 },
	{ 0, 1, 0, 1 },
	{ 1, 0, 1, 1 }
};
A = Factory2D.make(values2);
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

double[][] values3 = 
{
	{ 1, 1, 1, 0 },
	{ 0, 1, 0, 0 },
	{ 1, 1, 0, 1 },
	{ 0, 0, 1, 1 }
};
A = Factory2D.make(values3); 
k = cern.colt.matrix.linalg.Property.DEFAULT.semiBandwidth(A);
uk = cern.colt.matrix.linalg.Property.DEFAULT.upperBandwidth(A);
lk = cern.colt.matrix.linalg.Property.DEFAULT.lowerBandwidth(A);
System.out.println("\n\nupperBandwidth="+uk);
System.out.println("lowerBandwidth="+lk);
System.out.println("bandwidth="+k+" "+A);
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));





}
/**
 */
public static void doubleTest19(int size) {
System.out.println("\n\n");
int s = 2;

//DoubleMatrix2D A = Factory2D.make(values);
DoubleMatrix2D A00, A01, A02, A10, A11, A12, A20, A21, A22, empty;
empty = Factory2D.make(0,0);

A00 = Factory2D.ascending(s,s);
//A01 = empty;
A01 = Factory2D.ascending(s,s).assign(F.plus(A00.getQuick(s-1,s-1)));
A02 = Factory2D.ascending(s,s).assign(F.plus(A01.getQuick(s-1,s-1)));
A10 = Factory2D.ascending(s,s).assign(F.plus(A02.getQuick(s-1,s-1)));
A11 = null;
//A11 = Factory2D.ascending(s,s).assign(F.plus(A10.getQuick(s-1,s-1)));
A12 = Factory2D.ascending(s,s).assign(F.plus(A10.getQuick(s-1,s-1)));
//A12 = Factory2D.ascending(s,s).assign(F.plus(A11.getQuick(s-1,s-1)));
A20 = Factory2D.ascending(s,s).assign(F.plus(A12.getQuick(s-1,s-1)));
A21 = empty;
//A21 = Factory2D.ascending(s,s).assign(F.plus(A20.getQuick(s-1,s-1)));
A22 = Factory2D.ascending(s,s).assign(F.plus(A20.getQuick(s-1,s-1)));
//A22 = Factory2D.ascending(s,s).assign(F.plus(A21.getQuick(s-1,s-1)));


//B.assign(F.plus(A.zSum()));
//C.assign(F.plus(B.zSum()));




System.out.println("\n"+A00);
System.out.println("\n"+A01);
System.out.println("\n"+A02);
System.out.println("\n"+A10);
System.out.println("\n"+A11);
System.out.println("\n"+A12);
System.out.println("\n"+A20);
System.out.println("\n"+A21);
System.out.println("\n"+A22);
//System.out.println("\n"+Factory2D.make33(A00,A01,A02,A10,A11,A12,A20,A21,A22));


/*
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A00.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A01.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A02.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A10.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A11.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A12.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A20.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A21.toString()));
System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(A22.toString()));

System.out.println("\n"+cern.colt.matrixpattern.Converting.toHTML(Factory2D.make33(A00,A01,A02,A10,A11,A12,A20,A21,A22).toString()));
*/
}
/**
 */
public static void doubleTest2() {
// using a map
int[]    keys   = {0    , 3     , 100000, 9   };
double[] values = {100.0, 1000.0, 70.0  , 71.0};

int size = keys.length;
AbstractIntDoubleMap map = new OpenIntDoubleHashMap(size*2, 0.2, 0.5);

for (int i=0; i<keys.length; i++) {
   map.put(keys[i], (int)values[i]);
}

System.out.println(map.containsKey(3));
System.out.println(map.get(3));

System.out.println(map.containsKey(4));
System.out.println(map.get(4));

System.out.println(map.containsValue((int)71.0));
System.out.println(map.keyOf((int)71.0));

System.out.println(map);
}
/**
 */
public static void doubleTest20() {
System.out.println("\n\n");
DoubleMatrix2D A;
int k;
int uk;
int lk;

double[][] values1 =
{
	{ 0, 1, 0, 0 },
	{ 3, 0, 2, 0 },
	{ 0, 2, 0, 3 },
	{ 0, 0, 1, 0 }
};
A = Factory2D.make(values1);

System.out.println("\n\n"+LinearAlgebra.toVerboseString(A));
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));


double[][] values2 =
{
	{ 1.0000000000000167, -0.3623577544766736, -0.3623577544766736 },
	{ 0                 ,  0.9320390859672374, -0.3377315902755755 },
	{ 0                 ,  0                 ,  0.8686968577706282 },
	{ 0                 ,  0                 ,  0                  },
	{ 0                 ,  0                 ,  0                  }
};

A = Factory2D.make(values2);

System.out.println("\n\n"+LinearAlgebra.toVerboseString(A));
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

double[][] values3 =
{
	{ 611,  196, -192,  407,   -8,  -52,  -49,   29 },
	{ 196,  899,  113, -192,  -71,  -43,   -8,  -44 },
	{-192,  113,  899,  196,   61,   49,    8,   52 },
	{ 407, -192,  196,  611,    8,   44,   59,  -23 },
	{  -8,  -71,   61,    8,  411, -599,  208,  208 },
	{ -52,  -43,   49,   44, -599,  411,  208,  208 },
	{ -49,   -8,    8,   59,  208,  208,   99, -911 },
	{  29,  -44,   52,  -23,  208,  208, -911,   99 }
};


A = Factory2D.make(values3);

System.out.println("\n\n"+LinearAlgebra.toVerboseString(A));
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));

//Exact eigenvalues from Westlake (1968), p.150 (ei'vectors given too):
double a = Math.sqrt(10405); double b = Math.sqrt(26);
double[] e = {-10*a,   0,   510-100*b,  1000,   1000,   510+100*b, 1020,   10*a};
System.out.println(Factory1D.dense.make(e));


}
/**
 */
public static void doubleTest21() {
System.out.println("\n\n");
DoubleMatrix2D A;
int k;
int uk;
int lk;

double[][] values1 =
{
	{ 1/3, 2/3, Math.PI, 0 },
	{ 3, 9, 0, 0 },
	{ 0, 2, 7, 0 },
	{ 0, 0, 3, 9 }
};
A = Factory2D.make(values1);
System.out.println(A);
System.out.println(new cern.colt.matrix.doublealgo.Formatter(null).toString(A));

//System.out.println("\n\n"+LinearAlgebra.toVerboseString(A));
//System.out.println(new LUDecomposition(A));
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));
}
/**
 */
public static void doubleTest22() {
System.out.println("\n\n");
DoubleMatrix2D A;
int k;
int uk;
int lk;

double[][] values1 =
{
	{ 1/3, 2/3, Math.PI, 0 },
	{ 3, 9, 0, 0 },
	{ 0, 2, 7, 0 },
	{ 0, 0, 3, 9 }
};
A = Factory2D.make(values1);
System.out.println(A);
System.out.println(Property.isDiagonallyDominantByRow(A));
System.out.println(Property.isDiagonallyDominantByColumn(A));
Property.generateNonSingular(A);
System.out.println(A);
System.out.println(Property.isDiagonallyDominantByRow(A));
System.out.println(Property.isDiagonallyDominantByColumn(A));

//System.out.println("\n\n"+LinearAlgebra.toVerboseString(A));
//System.out.println(new LUDecomposition(A));
//System.out.println("\n\nbandwidth="+k+" "+cern.colt.matrixpattern.Converting.toHTML(A.toString()));
}
/**
 */
public static void doubleTest23(int runs, int size, double nonZeroFraction, boolean dense) {
System.out.println("\n\n");
System.out.println("initializing...");
DoubleMatrix2D A, LU, I, Inv;
DoubleMatrix1D b, solved;

double mean = 5.0;
double stdDev = 3.0; 
cern.jet.random.Normal random = new cern.jet.random.Normal(mean, stdDev, new cern.jet.random.engine.MersenneTwister());

System.out.println("sampling...");
double value = 2;
if (dense) 
	A = Factory2D.dense.sample(size,size, value, nonZeroFraction);
else
	A = Factory2D.sparse.sample(size,size, value, nonZeroFraction);
b = A.like1D(size).assign(1);

//A.assign(random);
//A.assign(F.rint); // round
System.out.println("generating invertible matrix...");
Property.generateNonSingular(A);

//I = Factory2D.identity(size);

LU = A.like();
solved = b.like();
//Inv = Factory2D.make(size,size);

LUDecompositionQuick lu = new LUDecompositionQuick();

System.out.println("benchmarking assignment...");
cern.colt.Timer timer = new cern.colt.Timer().start();
LU.assign(A);
solved.assign(b);
timer.stop().display();

LU.assign(A);
lu.decompose(LU);

System.out.println("benchmarking LU...");
timer.reset().start();
for (int i=runs; --i >=0; ) {
	solved.assign(b);
	//Inv.assign(I);
	//lu.decompose(LU);
	lu.solve(solved);
	//lu.solve(Inv);
}
timer.stop().display();

//System.out.println("A="+A);
//System.out.println("LU="+LU);
//System.out.println("U="+lu.getU());
//System.out.println("L="+lu.getL());
System.out.println("done.");
	
}
/**
 */
public static void doubleTest24(int runs, int size, boolean dense) {
System.out.println("\n\n");
System.out.println("initializing...");
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 2;
double omega = 1.25;
final double alpha = omega * 0.25;
final double beta = 1-omega;
A = factory.make(size,size,value);

cern.colt.function.Double9Function function = new cern.colt.function.Double9Function() {
	public final double apply(double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22) {
		return alpha*a11 + beta*(a01+a10+a12+a21);
	}
};
cern.colt.Timer timer = new cern.colt.Timer().start();

System.out.println("benchmarking stencil...");
for (int i=0; i<runs; i++) {
	A.zAssign8Neighbors(A,function);
}
//A.zSum4Neighbors(A,alpha,beta,runs);
timer.stop().display();
//System.out.println("A="+A);
A=null;

double[][] B =  factory.make(size,size,value).toArray();
timer.reset().start();

System.out.println("benchmarking stencil scimark...");
for (int i=0; i<runs; i++) {
//	jnt.scimark2.SOR.execute(omega, B, runs);
}
timer.stop().display();


System.out.println("done.");
	
}
/**
 */
public static void doubleTest25(int size) {

	
System.out.println("\n\n");
System.out.println("initializing...");
boolean dense = true;
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 0.5;
A = factory.make(size,size,value);
Property.generateNonSingular(A);
cern.colt.Timer timer = new cern.colt.Timer().start();

System.out.println(A);
System.out.println(Algebra.ZERO.inverse(A));

timer.stop().display();

System.out.println("done.");
	
}
/**
 */
public static void doubleTest26(int size) {

	
System.out.println("\n\n");
System.out.println("initializing...");
boolean dense = true;
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 0.5;
A = factory.make(size,size,value);
Property.generateNonSingular(A);
cern.colt.Timer timer = new cern.colt.Timer().start();

DoubleMatrix2DComparator fun = new DoubleMatrix2DComparator() {
	public int compare(DoubleMatrix2D a, DoubleMatrix2D b) {
		return a.zSum() == b.zSum() ? 1 : 0;
	}
};

System.out.println(A);
System.out.println(Algebra.ZERO.inverse(A));

timer.stop().display();

System.out.println("done.");
	
}
/**
 */
public static void doubleTest27() {

	
System.out.println("\n\n");
System.out.println("initializing...");

int rows=51;
int columns=10;
double[][] trainingSet = new double[columns][rows];
for (int i=columns; --i >= 0; ) trainingSet[i][i]=2.0;

int patternIndex        = 0;
int unitIndex           = 0;

DoubleMatrix2D patternMatrix       = null;
DoubleMatrix2D transposeMatrix     = null;
DoubleMatrix2D QMatrix             = null;
DoubleMatrix2D inverseQMatrix      = null;
DoubleMatrix2D pseudoInverseMatrix = null;
DoubleMatrix2D weightMatrix        = null;

// form a matrix with the columns as training vectors
patternMatrix = DoubleFactory2D.dense.make (rows, columns);

// copy the patterns into the matrix
for (patternIndex=0;patternIndex<columns;patternIndex++) {
	for (unitIndex=0;unitIndex<rows;unitIndex++) {
		patternMatrix.setQuick (unitIndex, patternIndex, trainingSet[patternIndex][unitIndex]);
	}
}

transposeMatrix     = Algebra.DEFAULT.transpose (patternMatrix);
QMatrix             = Algebra.DEFAULT.mult (transposeMatrix,patternMatrix);
inverseQMatrix      = Algebra.DEFAULT.inverse (QMatrix);
pseudoInverseMatrix = Algebra.DEFAULT.mult (inverseQMatrix,transposeMatrix);
weightMatrix        = Algebra.DEFAULT.mult (patternMatrix,pseudoInverseMatrix);
System.out.println("done.");
	
}
/**
 */
public static void doubleTest28() {
	double[] data={1,2,3,4,5,6};
	double[][] arrMatrix = 
	{ 
		{ 1, 2, 3, 4, 5, 6},
		{ 2, 3, 4, 5, 6, 7}
	};
	DoubleFactory2D f = DoubleFactory2D.dense;
	DoubleMatrix1D vector = new DenseDoubleMatrix1D(data);
	DoubleMatrix2D matrix = f.make(arrMatrix);
	DoubleMatrix1D res = vector.like(matrix.rows());
	
	matrix.zMult(vector,res);

	System.out.println(res);
}
/**
 */
public static void doubleTest28(DoubleFactory2D f) {
	double[] data={1,2,3,4,5,6};
	double[][] arrMatrix = 
	{ 
		{ 1, 2, 3, 4, 5, 6},
		{ 2, 3, 4, 5, 6, 7}
	};
	
	DoubleMatrix1D vector = new DenseDoubleMatrix1D(data);
	DoubleMatrix2D matrix = f.make(arrMatrix);
	DoubleMatrix1D res = vector.like(matrix.rows());
	
	matrix.zMult(vector,res);

	System.out.println(res);
}
/**
 */
public static void doubleTest29(int size) {
/*
	
System.out.println("\n\n");
System.out.println("initializing...");
boolean dense = false;
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 0.5;	

DoubleMatrix2D C = Factory2D.dense.sample(size,size,value,1);

A = factory.make(size,size);
System.out.print("A assign C... ");
cern.colt.Timer timer = new cern.colt.Timer().start();
A.assign(C);
timer.stop().display();

System.out.print("A getquick... ");
timer.reset().start();
double sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=A.getQuick(i,j);
	}
}
timer.stop().display();
System.out.println(sum);
//System.out.println(A);

System.out.print("sci set3... ");
JSci.maths.DoubleSparseMatrix B = new JSci.maths.DoubleSparseMatrix(size);
timer.reset().start();
//for (int i=size; --i>=0; ) {
//	for (int j=size; --j>=0; ) {
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		B.setElement3(i,j,C.getQuick(i,j));
	}
}
//System.out.println(A);
timer.stop().display();

System.out.print("sci get3... ");
timer.reset().start();
sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=B.getElement3(i,j);
	}
}
System.out.println(sum);
timer.stop().display();

JSci.maths.DoubleVector vec = new JSci.maths.DoubleVector(size);

System.out.print("sci mult3... ");
timer.reset().start();
B.multiply3(vec);
timer.stop().display();


System.out.println("done.");
*/	
}
/**
 */
public static void doubleTest29(int size,DoubleFactory2D f) {
	
	DoubleMatrix2D x = new DenseDoubleMatrix2D(size,size).assign(0.5);
	DoubleMatrix2D matrix = f.sample(size,size,0.5,0.001);
	
	cern.colt.Timer timer = new cern.colt.Timer().start();
	DoubleMatrix2D res = matrix.zMult(x,null);
	timer.stop().display();
	
	//System.out.println(res);
}
/**
 */
public static void doubleTest29(DoubleFactory2D f) {
	double[][] data = 
	{ 
		{ 6, 5, 4 },
		{ 7, 6, 3 },
		{ 6, 5, 4 },
		{ 7, 6, 3 },
		{ 6, 5, 4 },
		{ 7, 6, 3 }
	};
	
	double[][] arrMatrix = 
	{ 
		{ 1, 2, 3, 4, 5, 6},
		{ 2, 3, 4, 5, 6, 7}
	};
	
	DoubleMatrix2D x = new DenseDoubleMatrix2D(data);
	DoubleMatrix2D matrix = f.make(arrMatrix);
	
	DoubleMatrix2D res = matrix.zMult(x,null);

	System.out.println(res);
}
/**
 */
public static void doubleTest3() {
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = new DenseDoubleMatrix2D(rows,columns);
System.out.println(master);
master.assign(1); // set all cells to 1
System.out.println("\n"+master);
master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
System.out.println("\n"+master);

DoubleMatrix2D flip1 = master.viewColumnFlip();
System.out.println("flip around columns="+flip1);
DoubleMatrix2D flip2 = flip1.viewRowFlip();
System.out.println("further flip around rows="+flip2);

flip2.viewPart(0,0,2,2).assign(3);
System.out.println("master replaced"+master);
System.out.println("flip1 replaced"+flip1);
System.out.println("flip2 replaced"+flip2);


/*
DoubleMatrix2D copyPart = master.copyPart(2,1,2,3);
copyPart.assign(3); // modify an independent copy
copyPart.set(0,0,4);
System.out.println("\n"+copyPart); // has changed
System.out.println("\n"+master); // master has not changed

DoubleMatrix2D view1 = master.viewPart(0,3,4,2); // [0,3] .. [3,4]
DoubleMatrix2D view2 = view1.viewPart(0,0,4,1); // a view from a view 
System.out.println("\n"+view1);
System.out.println("\n"+view2);
*/
}
/**
 */
public static void doubleTest30() {
	double[][] data = 
	{ 
		{ 6, 5 },
		{ 7, 6 },
	};
	
	double[] x = { 1, 2 };
	double[] y = { 3, 4 };

	DoubleMatrix2D A = new DenseDoubleMatrix2D(data);
	SeqBlas.seqBlas.dger(1,new DenseDoubleMatrix1D(x), new DenseDoubleMatrix1D(y), A);

	System.out.println(A);
}
/**
 */
public static void doubleTest30(int size) {

int[] values = { 0, 2, 3, 5, 7};
IntArrayList list = new IntArrayList(values);
int val = 3;
int sum=0;
cern.colt.Timer timer = new cern.colt.Timer().start();
for (int i=size; --i>=0; ) {
	int k = list.binarySearchFromTo(val,0,values.length-1);
	System.out.println(list+", "+val+" --> "+k);
	sum+=k;
}
timer.stop().display();
//System.out.println("sum = "+sum);


/*
System.out.println("\n\n");
System.out.println("initializing...");
boolean dense = false;
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 0.5;	

DoubleMatrix2D C = Factory2D.dense.sample(size,size,value,0.01);

A = factory.make(size,size);
cern.colt.Timer timer = new cern.colt.Timer().start();
A.assign(C);
timer.stop().display();

timer.reset().start();
double sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=A.getQuick(i,j);
	}
}
timer.stop().display();
System.out.println(sum);
//System.out.println(A);

JSci.maths.DoubleSparseMatrix B = new JSci.maths.DoubleSparseMatrix(size);
timer.reset().start();
for (int i=size; --i>=0; ) {
	for (int j=size; --j>=0; ) {
//for (int i=0; i<size; i++) {
//	for (int j=0; j<size; j++ ) {
		B.setElement2(i,j,C.getQuick(i,j));
	}
}
//System.out.println(A);
timer.stop().display();

timer.reset().start();
sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=B.getElement2(i,j);
	}
}
System.out.println(sum);
timer.stop().display();

System.out.println("done.");

*/
}
/**
 */
public static void doubleTest30(int size, int val) {

//int[] values = { 0, 2};
int[] values = {2};
IntArrayList list = new IntArrayList(values);
int l = values.length-1;
int sum=0;
cern.colt.Timer timer = new cern.colt.Timer().start();
for (int i=size; --i>=0; ) {
	int k = cern.colt.Sorting.binarySearchFromTo(values,val,0,l);
	//int k = list.binarySearchFromTo(val,0,l);
	//System.out.println(list+", "+val+" --> i="+k+", -i-1="+(-k-1));
	sum+=k;
}
timer.stop().display();
System.out.println("sum = "+sum);


/*
System.out.println("\n\n");
System.out.println("initializing...");
boolean dense = false;
DoubleMatrix2D A;
DoubleFactory2D factory;
if (dense) 
	factory = Factory2D.dense;
else 
	factory = Factory2D.sparse;
	
double value = 0.5;	

DoubleMatrix2D C = Factory2D.dense.sample(size,size,value,0.01);

A = factory.make(size,size);
cern.colt.Timer timer = new cern.colt.Timer().start();
A.assign(C);
timer.stop().display();

timer.reset().start();
double sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=A.getQuick(i,j);
	}
}
timer.stop().display();
System.out.println(sum);
//System.out.println(A);

JSci.maths.DoubleSparseMatrix B = new JSci.maths.DoubleSparseMatrix(size);
timer.reset().start();
for (int i=size; --i>=0; ) {
	for (int j=size; --j>=0; ) {
//for (int i=0; i<size; i++) {
//	for (int j=0; j<size; j++ ) {
		B.setElement2(i,j,C.getQuick(i,j));
	}
}
//System.out.println(A);
timer.stop().display();

timer.reset().start();
sum=0;
for (int i=0; i<size; i++) {
	for (int j=0; j<size; j++ ) {
		sum+=B.getElement2(i,j);
	}
}
System.out.println(sum);
timer.stop().display();

System.out.println("done.");

*/
}
/**
 */
public static void doubleTest31(int size) {

System.out.println("\ninit");
DoubleMatrix1D a = Factory1D.dense.descending(size);
DoubleMatrix1D b = new WrapperDoubleMatrix1D(a);
DoubleMatrix1D c = b.viewPart(2,3);
DoubleMatrix1D d = c.viewFlip();
//DoubleMatrix1D c = b.viewFlip();
//DoubleMatrix1D d = c.viewFlip();
d.set(0,99);
b = b.viewSorted();
System.out.println("a = "+a);
System.out.println("b = "+b);
System.out.println("c = "+c);
System.out.println("d = "+d);

System.out.println("done");

}
/**
 */
public static void doubleTest32() {
	double[][] data = 
	{ 
		{ 1, 4, 0 },
		{ 6, 2, 5 },
		{ 0, 7, 3 },
		{ 0, 0, 8 },
		{ 0, 0, 0 },
		{ 0, 0, 0 }
	};
	
	DoubleMatrix2D x = new TridiagonalDoubleMatrix2D(data);
	

	System.out.println("\n\n\n"+x);
	System.out.println("\n"+new DenseDoubleMatrix2D(data));
}
/**
 */
public static void doubleTest4() {
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = new DenseDoubleMatrix2D(rows,columns);
System.out.println(master);
master.assign(1); // set all cells to 1
DoubleMatrix2D view = master.viewPart(2,0,2,3).assign(2);
System.out.println("\n"+master);
System.out.println("\n"+view);
Transform.mult(view,3);
System.out.println("\n"+master);
System.out.println("\n"+view);


/*
DoubleMatrix2D copyPart = master.copyPart(2,1,2,3);
copyPart.assign(3); // modify an independent copy
copyPart.set(0,0,4);
System.out.println("\n"+copyPart); // has changed
System.out.println("\n"+master); // master has not changed

DoubleMatrix2D view1 = master.viewPart(0,3,4,2); // [0,3] .. [3,4]
DoubleMatrix2D view2 = view1.viewPart(0,0,4,1); // a view from a view 
System.out.println("\n"+view1);
System.out.println("\n"+view2);
*/
}
/**
 */
public static void doubleTest5() {
	/*
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = new DenseDoubleMatrix2D(rows,columns);
System.out.println(master);
master.assign(1); // set all cells to 1
DoubleMatrix2D view = master.viewPart(2,0,2,3);
view.assign(0);
for (int i=0; i<rows; i++) {
	for (int j=0; j<columns; j++) {
		boolean hasIndex = view.hasIndex(master.index(i,j));
		System.out.println("("+i+","+j+"):"+hasIndex);
	}
}
System.out.println("\n"+master);
System.out.println("\n"+view);
IntArrayList rowList = new IntArrayList();
IntArrayList columnList = new IntArrayList();
DoubleArrayList valueList = new DoubleArrayList();
master.getNonZeros(rowList,columnList,valueList);
System.out.println(rowList);
System.out.println(columnList);
System.out.println(valueList);
System.out.println(master.toStringSparse());
*/
}
/**
 */
public static void doubleTest6() {
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = Factory2D.ascending(rows,columns);
//master.assign(1); // set all cells to 1
System.out.println("\n"+master);
master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
System.out.println("\n"+master);

int[] indexes = {0,1,3,0,1,2};
DoubleMatrix1D view1 = master.viewRow(0).viewSelection(indexes);
System.out.println("view1="+view1);
DoubleMatrix1D view2 = view1.viewPart(0,3);
System.out.println("view2="+view2);

view2.viewPart(0,2).assign(-1);
System.out.println("master replaced"+master);
System.out.println("flip1 replaced"+view1);
System.out.println("flip2 replaced"+view2);


/*
DoubleMatrix2D copyPart = master.copyPart(2,1,2,3);
copyPart.assign(3); // modify an independent copy
copyPart.set(0,0,4);
System.out.println("\n"+copyPart); // has changed
System.out.println("\n"+master); // master has not changed

DoubleMatrix2D view1 = master.viewPart(0,3,4,2); // [0,3] .. [3,4]
DoubleMatrix2D view2 = view1.viewPart(0,0,4,1); // a view from a view 
System.out.println("\n"+view1);
System.out.println("\n"+view2);
*/
}
/**
 */
public static void doubleTest7() {
int rows = 4;
int columns = 5; // make a 4*5 matrix
DoubleMatrix2D master = Factory2D.ascending(rows,columns);
//master.assign(1); // set all cells to 1
System.out.println("\n"+master);
//master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
//System.out.println("\n"+master);

int[] rowIndexes = {0,1,3,0};
int[] columnIndexes = {0,2};
DoubleMatrix2D view1 = master.viewSelection(rowIndexes,columnIndexes);
System.out.println("view1="+view1);
DoubleMatrix2D view2 = view1.viewPart(0,0,2,2);
System.out.println("view2="+view2);

view2.assign(-1);
System.out.println("master replaced"+master);
System.out.println("flip1 replaced"+view1);
System.out.println("flip2 replaced"+view2);

}
/**
 */
public static void doubleTest8() {
int rows = 2;
int columns = 3; // make a 4*5 matrix
DoubleMatrix2D master = Factory2D.ascending(rows,columns);
//master.assign(1); // set all cells to 1
System.out.println("\n"+master);
//master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
//System.out.println("\n"+master);

DoubleMatrix2D view1 = master.viewDice();
System.out.println("view1="+view1);
DoubleMatrix2D view2 = view1.viewDice();
System.out.println("view2="+view2);

view2.assign(-1);
System.out.println("master replaced"+master);
System.out.println("flip1 replaced"+view1);
System.out.println("flip2 replaced"+view2);

}
/**
 */
public static void doubleTest9() {
int rows = 2;
int columns = 3; // make a 4*5 matrix
DoubleMatrix2D master = Factory2D.ascending(rows,columns);
//master.assign(1); // set all cells to 1
System.out.println("\n"+master);
//master.viewPart(2,0,2,3).assign(2); // set [2,1] .. [3,3] to 2
//System.out.println("\n"+master);

DoubleMatrix2D view1 = master.viewRowFlip();
System.out.println("view1="+view1);
DoubleMatrix2D view2 = view1.viewRowFlip();
System.out.println("view2="+view2);

view2.assign(-1);
System.out.println("master replaced"+master);
System.out.println("flip1 replaced"+view1);
System.out.println("flip2 replaced"+view2);

}
/**
 */
public static void main(String[] args) {
	int runs = Integer.parseInt(args[0]);
	int val = Integer.parseInt(args[1]);
	doubleTest30(runs, val);
	/*
	int runs = Integer.parseInt(args[0]);
	int size = Integer.parseInt(args[1]);
	double nonZeroFraction = new Double(args[2]).doubleValue();
	boolean dense = args[3].equals("dense");
	//doubleTest23(runs, size, nonZeroFraction, dense);
	doubleTest24(runs, size, dense);
	*/
}
/**
 */
public static void testMax() {
  double[] temp = new double[2];
  
  temp[0] = 8.9;
  temp[1] = 1;
 
  DenseDoubleMatrix1D d1Double = new DenseDoubleMatrix1D(temp);
  
  hep.aida.bin.DynamicBin1D d1ynamicBin = cern.colt.matrix.doublealgo.Statistic.bin(d1Double);
  
  double max = d1ynamicBin.max();
  
  System.out.println("max = "+ max);
}
}
