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

class ChanRNG extends DelegatedRNG {

  boolean single_;

  ChanRNG() {
	setDelegate(new PublicSynchRNG());
  }  
  public long consumerNext(Channel c) throws InterruptedException {
	RNG r = null;
	if (cmode == 0) {
	  r =  (RNG)(c.take());
	}
	else {
	  while (r == null) r = (RNG)(c.poll(waitTime));
	}
	
	if (pcBias == 0) {
	  r.update();
	}
	else if (pcBias > 0) {
	  r.update();
	  r.update();
	}
	return r.get();
  }  
  public synchronized boolean isSingle() { return single_; }  
  public long producerNext(Channel c) throws InterruptedException {
	RNG r = getDelegate();
	if (isSingle()) {
	  c.put(r);
	  r = (RNG)(c.take());
	  r.update();
	}
	else {
	  if (pcBias < 0) {
		r.update();
		r.update(); // update consumer side too
	  }
	  else if (pcBias == 0) {
		r.update();
	  }
	  
	  if (pmode == 0) {
		c.put(r);
	  }
	  else {
		while (!(c.offer(r, waitTime))) {}
	  }
	}
	return r.get();
  }  
  public synchronized void setSingle(boolean s) { single_ = s; }  
}
