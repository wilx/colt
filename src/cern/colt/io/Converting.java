/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
*/
package cern.colt.io;

//import java.io.*; // in fact not really needed. Just to work around a bug in javadoc (would not generate links to JDK doc)
/**
Efficient portable array conversions for high performance I/O through bulk data 
transfer rather than piece-wise transfer. Avoids deep non-inlined synchronized 
call chains in the java.io package. The following conversions are supported: 
<ul>
  <li>for all primitive data types: <tt>type[] --> byte[]</tt>, as well as <tt>type   --> byte[]</tt></li>
  <li>for all primitive data types: <tt>byte[] --> type[]</tt>, as well as <tt>byte[] --> type</tt></li>
</ul>
This <b>exactly mimics</b> the semantics of {@link java.io.DataInputStream} and 
{@link java.io.DataOutputStream}. In fact, this is a copy&paste of the JDK1.3 
source code of these two classes, with efficient loops written around.
Not only for disk I/O, but also for other kinds of serialization, e.g. high bandwidth networking.
<h3>Fast writing:</h3>
<ul>
  <li>Convert your data into a byte[] using 
	<ul>
	  <li>for all primitive data types: type[] --> byte[]: Example: <tt>toByte(double[] 
		src, int srcPos, byte[] dest, int destPos, int length)</tt></li>
	  <li>for all primitive data types: type --> byte[]: Example: <tt>toByte(double 
		src, byte[] dest, int destPos)</tt></li>
	</ul>
  <li>then write it with a single call {@link java.io.FileOutputStream#writeBytes(byte 
	b[], int off, int len)}, perhaps buffered.</li>
</ul>
<h3>Fast reading:</h3>
<ul>
  <li>Read your data into a byte[] with a single call {@link java.io.FileInputStream#readBytes(byte 
	b[], int off, int len)}, perhaps buffered. 
  <li>Then convert it into the desired data type using 
	<ul>
	  <li>for all primitive data types: byte[] --> type[]: Example: <tt>toDouble(byte[] 
		src, int srcPos, double[] dest, int destPos, int length)</tt></li>
	  <li>for all primitive data types: byte[] --> type: Example: <tt>toDouble(byte[] 
		src, int srcPos)</tt></li>
	</ul>
  </li>
</ul>
The method arguments are exactly as used in {@link System#arraycopy}, except for 
the <tt>length</tt> argument which is always given in bytes!
Please don't flame for not writing redundant javadoc for each and every method.
Run main() for tests and benchmarks.
Converting ints and longs is fastest --> some 50-75 MB/s, floats and doubles --> some 15-35 MB/s


@author wolfgang.hoschek@cern.ch
@version 1.0, 28-May-2000
*/
public class Converting {
protected Converting() {}
/**
 * Test implementation for correctness and benchmarks (outputs MB/s converted back and forth).
 */
public static void main(String[] args) {
	//int size = 5;
	//int runs = 1000;
	//boolean print = false;

	int k=0;
	int runs = Integer.parseInt(args[k++]); // how often to repeat measurements
	int size = Integer.parseInt(args[k++]); // number of elements to convert
	//boolean print = new Boolean(args[k++]).booleanValue();

	int[] i1 = new int[size];
	int[] i2 = new int[size];
	double[] d1 = new double[size];
	double[] d2 = new double[size];
	long[] l1 = new long[size];
	long[] l2 = new long[size];
	float[] f1 = new float[size];
	float[] f2 = new float[size];

	cern.colt.list.IntArrayList iList1 = new cern.colt.list.IntArrayList(i1);
	cern.colt.list.IntArrayList iList2 = new cern.colt.list.IntArrayList(i2);
	cern.colt.list.DoubleArrayList dList1 = new cern.colt.list.DoubleArrayList(d1);
	cern.colt.list.DoubleArrayList dList2 = new cern.colt.list.DoubleArrayList(d2);
	cern.colt.list.LongArrayList lList1 = new cern.colt.list.LongArrayList(l1);
	cern.colt.list.LongArrayList lList2 = new cern.colt.list.LongArrayList(l2);
	cern.colt.list.FloatArrayList fList1 = new cern.colt.list.FloatArrayList(f1);
	cern.colt.list.FloatArrayList fList2 = new cern.colt.list.FloatArrayList(f2);

	byte[] bi = new byte[size*4];
	byte[] bd = new byte[size*8];
	byte[] bl = new byte[size*8];
	byte[] bf = new byte[size*4];

	//final int[] x = { -2, 0, 9, -11, -3, 2 };
	cern.jet.random.engine.MersenneTwister rand = new cern.jet.random.engine.MersenneTwister();
	
	int c = 8;
	cern.colt.Timer[] timer = new cern.colt.Timer[c];
	for (int i=0; i<c; i++) timer[i] = new cern.colt.Timer();
	for (int run=0; run<runs; run++) {
		for (int i = 0; i <size; i++) {
			i1[i] = rand.nextInt();
			//i2[i] = i1[i];
			d1[i] = rand.nextDouble();
			//d2[i] = d1[i];
		}

		int v = 0;
		timer[v].start();
		write(i1,0,bi,0,size*4); // i1 --> bi
		timer[v++].stop();

		timer[v].start();
		read(bi,0,i2,0,size*4); // bi --> i2
		timer[v++].stop();

		if (!(iList1.equals(iList2))) throw new InternalError("oops");

		timer[v].start();
		write(d1,0,bd,0,size*8); // d1 --> bd
		timer[v++].stop();

		timer[v].start();
		read(bd,0,d2,0,size*8); // bd --> d2
		timer[v++].stop();

		if (!(dList1.equals(dList2))) throw new InternalError("oops");

		timer[v].start();
		write(l1,0,bl,0,size*8);
		timer[v++].stop();

		timer[v].start();
		read(bl,0,l2,0,size*8);
		timer[v++].stop();

		if (!(lList1.equals(lList2))) throw new InternalError("oops");

		timer[v].start();
		write(f1,0,bf,0,size*4);
		timer[v++].stop();

		timer[v].start();
		read(bf,0,f2,0,size*4); 
		timer[v++].stop();

		if (!(fList1.equals(fList2))) throw new InternalError("oops");

		//System.out.print(".");
		//if (print) System.out.println("after="+list+"\n\n\n\n");
	}
	System.out.println();
	float MB = size*runs/1000000.0f;
	int v = 0;
	System.out.println("write int[]    --> byte[]: "+ timer[v] + ", " + MB*4/timer[v++].seconds() +" MB/s");
	System.out.println("read  byte[]   --> int[]: "+ timer[v] + ", " + MB*4/timer[v++].seconds() +" MB/s");
	System.out.println("write double[] --> byte[]: "+ timer[v] + ", " + MB*8/timer[v++].seconds() +" MB/s");
	System.out.println("read  byte[]   --> double[]: "+ timer[v] + ", " + MB*8/timer[v++].seconds() +" MB/s");
	
	System.out.println("write long[]   --> byte[]: "+ timer[v] + ", " + MB*8/timer[v++].seconds() +" MB/s");
	System.out.println("read  byte[]   --> long[]: "+ timer[v] + ", " + MB*8/timer[v++].seconds() +" MB/s");
	System.out.println("write float[]  --> byte[]: "+ timer[v] + ", " + MB*4/timer[v++].seconds() +" MB/s");
	System.out.println("read  byte[]   --> float[]: "+ timer[v] + ", " + MB*4/timer[v++].seconds() +" MB/s");

	System.out.println("no bug detected.\n\n");
}
public static void read(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
	System.arraycopy(src,srcPos,dest,destPos,length);
}
public static void read(byte[] src, int srcPos, char[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		dest[destPos++] = (char)((b0 << 8) + (b1 << 0));
		length -= 2;
	}
}
public static void read(byte[] src, int srcPos, double[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		int b2 = src[srcPos++] & 0xFF;
		int b3 = src[srcPos++] & 0xFF;
		int b4 = src[srcPos++] & 0xFF;
		int b5 = src[srcPos++] & 0xFF;
		int b6 = src[srcPos++] & 0xFF;
		int b7 = src[srcPos++] & 0xFF;
		dest[destPos++] = Double.longBitsToDouble(
			((long)((b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0)) << 32) 
			+ 
			(((b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0)) & 0xFFFFFFFFL)
		);
		// = ((long)(readInt) << 32) + (readInt & 0xFFFFFFFFL)
		length -= 8;
	}
}
public static void read(byte[] src, int srcPos, float[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		int b2 = src[srcPos++] & 0xFF;
		int b3 = src[srcPos++] & 0xFF;
		dest[destPos++] = Float.intBitsToFloat( (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0) );
		length -= 4;
	}
}
public static void read(byte[] src, int srcPos, int[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		int b2 = src[srcPos++] & 0xFF;
		int b3 = src[srcPos++] & 0xFF;
		dest[destPos++] = (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0);
		length -= 4;
	}
}
public static void read(byte[] src, int srcPos, long[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		int b2 = src[srcPos++] & 0xFF;
		int b3 = src[srcPos++] & 0xFF;
		int b4 = src[srcPos++] & 0xFF;
		int b5 = src[srcPos++] & 0xFF;
		int b6 = src[srcPos++] & 0xFF;
		int b7 = src[srcPos++] & 0xFF;
		dest[destPos++] = 
			((long)((b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0)) << 32) 
			+ 
			(((b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0)) & 0xFFFFFFFFL);
		// = ((long)(readInt) << 32) + (readInt & 0xFFFFFFFFL)
		length -= 8;
	}
}
public static void read(byte[] src, int srcPos, short[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		int b1 = src[srcPos++] & 0xFF;
		dest[destPos++] = (short)((b0 << 8) + (b1 << 0));
		length -= 2;
	}
}
public static void read(byte[] src, int srcPos, boolean[] dest, int destPos, int length) {
	while (length > 0) {
		int b0 = src[srcPos++] & 0xFF;
		dest[destPos++] = (b0 != 0);
		length--;
	}
}
public static boolean readBoolean(byte[] src, int srcPos) {
	return (src[srcPos] & 0xFF) != 0;
}
public static byte readByte(byte[] src, int srcPos) {
	return src[srcPos];
}
public static char readChar(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	return (char)((b0 << 8) + (b1 << 0));
}
public static double readDouble(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	int b2 = src[srcPos++] & 0xFF;
	int b3 = src[srcPos++] & 0xFF;
	int b4 = src[srcPos++] & 0xFF;
	int b5 = src[srcPos++] & 0xFF;
	int b6 = src[srcPos++] & 0xFF;
	int b7 = src[srcPos++] & 0xFF;
	return Double.longBitsToDouble(
		((long)((b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0)) << 32) 
		+ 
		(((b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0)) & 0xFFFFFFFFL)
	);
}
public static float readFloat(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	int b2 = src[srcPos++] & 0xFF;
	int b3 = src[srcPos++] & 0xFF;
	return Float.intBitsToFloat( (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0) );
}
public static int readInt(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	int b2 = src[srcPos++] & 0xFF;
	int b3 = src[srcPos++] & 0xFF;
	return (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0);
}
public static long readLong(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	int b2 = src[srcPos++] & 0xFF;
	int b3 = src[srcPos++] & 0xFF;
	int b4 = src[srcPos++] & 0xFF;
	int b5 = src[srcPos++] & 0xFF;
	int b6 = src[srcPos++] & 0xFF;
	int b7 = src[srcPos++] & 0xFF;
	return 
		((long)((b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0)) << 32) 
		+ 
		(((b4 << 24) + (b5 << 16) + (b6 << 8) + (b7 << 0)) & 0xFFFFFFFFL);
}
public static short readShort(byte[] src, int srcPos) {
	int b0 = src[srcPos++] & 0xFF;
	int b1 = src[srcPos++] & 0xFF;
	return (short)((b0 << 8) + (b1 << 0));
}
public static int readUnsignedByte(byte[] src, int srcPos) {
	return src[srcPos];
}
public static int readUnsignedShort(byte[] src, int srcPos) {
	int b0 = src[srcPos++];
	int b1 = src[srcPos++];
	return (b0 << 8) + (b1 << 0);
}
public static void write(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
	System.arraycopy(src,srcPos,dest,destPos,length); 
}
public static void write(char[] src, int srcPos, byte[] dest, int destPos, int length) {
	char v; 
	while (length > 0) {
		v = src[srcPos++];
		dest[destPos++] = (byte) ((v >>> 8) & 0xFF);
		dest[destPos++] = (byte) ((v >>> 0) & 0xFF);
		length -= 2;
	}
}
public static void write(double[] src, int srcPos, byte[] dest, int destPos, int length) {
	// doubleToLongBits is native, so cannot be inlined with standard techniques.
	// JDK still has no routine batching many native doubleToLongBits calls into a single one...
	// Same for floatToIntBits and in the reverse direction.
	long v;  
	while (length > 0) {
		v = Double.doubleToLongBits(src[srcPos++]);
		dest[destPos++] = (byte) ((int)(v >>> 56) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 48) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 40) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 32) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 24) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 16) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>>  8) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>>  0) & 0xFF);
		length -= 8;
	}
}
public static void write(float[] src, int srcPos, byte[] dest, int destPos, int length) {
	int v;  
	while (length > 0) {
		v = Float.floatToIntBits(src[srcPos++]);
		dest[destPos++] = (byte) ((v >>> 24) & 0xFF);
		dest[destPos++] = (byte) ((v >>> 16) & 0xFF);
		dest[destPos++] = (byte) ((v >>>  8) & 0xFF);
		dest[destPos++] = (byte) ((v >>>  0) & 0xFF);
		length -= 4;
	}
}
public static void write(int[] src, int srcPos, byte[] dest, int destPos, int length) {
	int v; 
	while (length > 0) {
		v = src[srcPos++];
		dest[destPos++] = (byte) ((v >>> 24) & 0xFF);
		dest[destPos++] = (byte) ((v >>> 16) & 0xFF);
		dest[destPos++] = (byte) ((v >>>  8) & 0xFF);
		dest[destPos++] = (byte) ((v >>>  0) & 0xFF);
		length -= 4;
	}
}
public static void write(long[] src, int srcPos, byte[] dest, int destPos, int length) {
	long v; 
	while (length > 0) {
		v = src[srcPos++];
		dest[destPos++] = (byte) ((int)(v >>> 56) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 48) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 40) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 32) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 24) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>> 16) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>>  8) & 0xFF);
		dest[destPos++] = (byte) ((int)(v >>>  0) & 0xFF);
		length -= 8;
	}
}
public static void write(short[] src, int srcPos, byte[] dest, int destPos, int length) {
	short v; 
	while (length > 0) {
		v = src[srcPos++];
		dest[destPos++] = (byte) ((v >>> 8) & 0xFF);
		dest[destPos++] = (byte) ((v >>> 0) & 0xFF);
		length -= 2;
	}
}
public static void write(boolean[] src, int srcPos, byte[] dest, int destPos, int length) {
	boolean v; 
	while (length > 0) {
		v = src[srcPos++];
		dest[destPos++] = (byte) (v ? 1 : 0);
		length--;
	}
}
public static void write(byte v, byte[] dest, int destPos) {
	dest[destPos] = v;
}
public static void write(char v, byte[] dest, int destPos) {
	dest[destPos++] = (byte) ((v >>> 8) & 0xFF);
	dest[destPos++] = (byte) ((v >>> 0) & 0xFF);
}
public static void write(double src, byte[] dest, int destPos) {
	long v = Double.doubleToLongBits(src);  
	dest[destPos++] = (byte) ((int)(v >>> 56) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 48) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 40) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 32) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 24) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 16) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>>  8) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>>  0) & 0xFF);
}
public static void write(float vv, byte[] dest, int destPos) {
	int v = Float.floatToIntBits(vv);
	dest[destPos++] = (byte) ((v >>> 24) & 0xFF);
	dest[destPos++] = (byte) ((v >>> 16) & 0xFF);
	dest[destPos++] = (byte) ((v >>>  8) & 0xFF);
	dest[destPos++] = (byte) ((v >>>  0) & 0xFF);
}
public static void write(int v, byte[] dest, int destPos) {
	dest[destPos++] = (byte) ((v >>> 24) & 0xFF);
	dest[destPos++] = (byte) ((v >>> 16) & 0xFF);
	dest[destPos++] = (byte) ((v >>>  8) & 0xFF);
	dest[destPos++] = (byte) ((v >>>  0) & 0xFF);
}
public static void write(long v, byte[] dest, int destPos) {
	dest[destPos++] = (byte) ((int)(v >>> 56) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 48) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 40) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 32) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 24) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>> 16) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>>  8) & 0xFF);
	dest[destPos++] = (byte) ((int)(v >>>  0) & 0xFF);
}
public static void write(short v, byte[] dest, int destPos) {
	dest[destPos++] = (byte) ((v >>> 8) & 0xFF);
	dest[destPos++] = (byte) ((v >>> 0) & 0xFF);
}
public static void write(boolean v, byte[] dest, int destPos) {
	dest[destPos] = (byte) (v ? 1 : 0);
}
}
