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

// -------------------------------------------------------------


abstract class RNG implements Serializable, Comparable {
  static final int firstSeed = 4321;
  static final int rmod = 2147483647;
  static final int rmul = 16807;

  static int lastSeed = firstSeed;
  static final int smod = 32749;
  static final int smul = 3125;

  static final Object constructionLock = RNG.class;

  // Use construction lock for all params to disable
  // changes in midst of construction of test objects.

  static final SynchronizedInt computeLoops = 
	new SynchronizedInt(16, constructionLock);
  static final SynchronizedInt syncMode = 
	new SynchronizedInt(0, constructionLock);
  static final SynchronizedInt producerMode = 
	new SynchronizedInt(0, constructionLock);
  static final SynchronizedInt consumerMode = 
	new SynchronizedInt(0, constructionLock);
  static final SynchronizedInt bias = 
	new SynchronizedInt(0, constructionLock);
  static final SynchronizedLong timeout = 
	new SynchronizedLong(100, constructionLock);
  static final SynchronizedInt exchangeParties = 
	new SynchronizedInt(1, constructionLock);
  static final SynchronizedInt sequenceNumber = 
	new SynchronizedInt(0, constructionLock);
  static final SynchronizedInt itersPerBarrier = 
	new SynchronizedInt(0, constructionLock);

  static Rendezvous[] exchangers_;

  final int cloops = computeLoops.get();
  final int pcBias = bias.get();
  final int smode = syncMode.get();
  final int pmode = producerMode.get();
  final int cmode = consumerMode.get();
  final long waitTime = timeout.get();
  Rendezvous exchanger_ = null;

  public int compareTo(Object other) {
	int h1 = hashCode();
	int h2 = other.hashCode();
	if (h1 < h2) return -1;
	else if (h1 > h2) return 1;
	else return 0;
  }  
  protected final long compute(long l) { 
	int loops = (int)((l & 0x7FFFFFFF) % (cloops * 2)) + 1;
	for (int i = 0; i < loops; ++i) l = (l * rmul) % rmod;
	return (l == 0)? firstSeed : l; 
  }  
  public void exchange() throws InterruptedException {
	Rendezvous ex = getExchanger(); 
	Runnable r = (Runnable)(ex.rendezvous(new UpdateCommand(this)));
	if (r != null) r.run();
  }  
  public long get()    { return internalGet(); }  
  synchronized Rendezvous getExchanger() {
	if (exchanger_ == null) {
	  synchronized (constructionLock) {
		int idx = sequenceNumber.increment();
		exchanger_ = exchangers_[idx % exchangers_.length];
	  }
	}
	return exchanger_;
  }  
  abstract protected long internalGet();  
  abstract protected void internalUpdate();  
  public long next()   { internalUpdate(); return internalGet(); }  
  static long nextSeed() {
	synchronized(constructionLock) {
	  long s = lastSeed;
	  lastSeed = (lastSeed * smul) % smod;
	  if (lastSeed == 0) 
		lastSeed = (int)(System.currentTimeMillis());
	  return s;
	}
  }  
  static void reset(int nthreads) {
	synchronized(constructionLock) {
	  sequenceNumber.set(-1);
	  int parties = exchangeParties.get();
	  if (nthreads < parties) parties = nthreads;
	  if (nthreads % parties != 0) 
		throw new Error("need even multiple of parties");
	  exchangers_ = new Rendezvous[nthreads / parties];
	  for (int i = 0; i < exchangers_.length; ++i) {
		exchangers_[i] = new Rendezvous(parties);
	  }
	}
  }  
  abstract protected void set(long l);  
  public void update() { internalUpdate();  }  
}
