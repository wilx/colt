package edu.oswego.cs.dl.util.concurrent.misc;

/*
  File: SynchronizationTimer.java

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  7Jul1998   dl               Create public version
  16Jul1998  dl               fix intialization error for compute loops
							  combined into one frame
							  misc layout and defaults changes
							  increase printed precision
							  overlap get/set in Executor tests
							  Swap defaults for swing import
							  Active thread counts reflect executors
  30Aug1998 dl                Misc revisions to mesh with 1.1.0
  27jan1999 dl                Eliminate GC calls                             
*/

// Swap the following sets of imports if necessary.

import javax.swing.*;
import javax.swing.border.*;

//import com.sun.java.swing.*;
//import com.sun.java.swing.border.*;

import  edu.oswego.cs.dl.util.concurrent.*;
import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  java.net.*;
import  java.lang.reflect.*;

class TestLoop {

  final RNG shared;
  final RNG primary;
  final int iters;
  final Fraction pshared;
  final CyclicBarrier barrier;
  final boolean[] useShared;
  final int firstidx;

  public TestLoop(RNG sh, RNG pri, Fraction pshr, int it, CyclicBarrier br) {
	shared = sh; 
	primary = pri; 
	pshared = pshr; 
	iters = it; 
	barrier = br; 

	firstidx = (int)(primary.get());

	int num = (int)(pshared.numerator());
	int denom = (int)(pshared.denominator());

	if (num == 0 || primary == shared) {
	  useShared = new boolean[1];
	  useShared[0] = false;
	}
	else if (num >= denom) {
	  useShared = new boolean[1];
	  useShared[0] = true;
	}
	else {
	  // create bool array and randomize it.
	  // This ensures that always same number of shared calls.

	  // denom slots is too few. iters is too many. an arbitrary compromise is:
	  int xfactor = 1024 / denom;
	  if (xfactor < 1) xfactor = 1;
	  useShared = new boolean[denom * xfactor];
	  for (int i = 0; i < num * xfactor; ++i) 
		useShared[i] = true;
	  for (int i = num * xfactor; i < denom  * xfactor; ++i) 
		useShared[i] = false;

	  for (int i = 1; i < useShared.length; ++i) {
		int j = ((int) (shared.next() & 0x7FFFFFFF)) % (i + 1);
		boolean tmp = useShared[i];
		useShared[i] = useShared[j];
		useShared[j] = tmp;
	  }
	}
  }  
  public Runnable testLoop() {
	return new Runnable() {
	  public void run() {
		int itersPerBarrier = RNG.itersPerBarrier.get();
		try {
		  int delta = -1;
		  if (primary.getClass().equals(PrioritySemRNG.class)) {
			delta = 2 - (int)((primary.get() % 5));
		  }
		  Thread.currentThread().setPriority(Thread.NORM_PRIORITY+delta);
		  
		  int nshared = (int)(iters * pshared.asDouble());
		  int nprimary = iters - nshared;
		  int idx = firstidx;
		  
		  barrier.barrier();
		  
		  for (int i = iters; i > 0; --i) {
			++idx;
			if (i % itersPerBarrier == 0)
			  primary.exchange();
			else {
			  
			  RNG r;
			  
			  if (nshared > 0 && useShared[idx % useShared.length]) {
				--nshared;
				r = shared;
			  }
			  else {
				--nprimary;
				r = primary;
			  }
			  long rnd = r.next();
			  if (rnd % 2 == 0 && Thread.currentThread().isInterrupted()) 
				break;
			}
		  }
		}
		catch (BrokenBarrierException ex) {
		}
		catch (InterruptedException ex) {
		  Thread.currentThread().interrupt();
		}
		finally {
		  try {
			barrier.barrier();
		  }
		  catch (BrokenBarrierException ex) { 
		  }
		  catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		  }
		  finally {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		  }

		}
	  }
	};
  }  
}
