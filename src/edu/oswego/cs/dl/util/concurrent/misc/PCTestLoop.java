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

class PCTestLoop extends TestLoop {
  final Channel primaryChannel;
  final Channel sharedChannel;

  public PCTestLoop(RNG sh, RNG pri, Fraction pshr, int it, 
	CyclicBarrier br, Channel shChan, Channel priChan) {
	super(sh, pri, pshr, it, br);
	sharedChannel = shChan;
	primaryChannel = priChan;
  }  
  public Runnable testLoop(final boolean isProducer) {
	return new Runnable() {
	  public void run() {
		int delta = -1;
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY+delta);
		int itersPerBarrier = RNG.itersPerBarrier.get();
		try { 
		  
		  int nshared = (int)(iters * pshared.asDouble());
		  int nprimary = iters - nshared;
		  int idx = firstidx;
		  
		  barrier.barrier(); 
		  
		  ChanRNG target = (ChanRNG)(primary);
		  
		  for (int i = iters; i > 0; --i) {
			++idx;
			if (i % itersPerBarrier == 0)
			  primary.exchange();
			else {
			  Channel c;
			
			  if (nshared > 0 && useShared[idx % useShared.length]) {
				--nshared;
				c = sharedChannel;
			  }
			  else {
				--nprimary;
				c = primaryChannel;
			  }
			  
			  long rnd;
			  if (isProducer) 
				rnd = target.producerNext(c);
			  else 
				rnd = target.consumerNext(c);
			  
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
		  catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		  }
		  catch (BrokenBarrierException ex) { 
		  }
		  finally {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		  }
		}
	  }
	};
  }  
}
