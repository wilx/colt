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

class RWLockRNG extends NoSynchRNG {
  protected final ReadWriteLock lock_;
  public RWLockRNG(ReadWriteLock l) { 
	lock_ = l; 
  }  
  protected final void acquireR() throws InterruptedException {
	if (smode == 0) {
	  lock_.readLock().acquire();
	}
	else {
	  while (!lock_.readLock().attempt(waitTime)) {}
	}
  }  
  protected final void acquireW() throws InterruptedException {
	if (smode == 0) {
	  lock_.writeLock().acquire();
	}
	else {
	  while (!lock_.writeLock().attempt(waitTime)) {}
	}
  }  
  public long get()  { 
	try {
	  acquireR();
	  long l = current_;
	  lock_.readLock().release(); 
	  return l;
	}
	catch(InterruptedException x) { 
	  Thread.currentThread().interrupt(); 
	  return 0;
	}
  }  
  public long next() { 
	long l = 0;
	try {
	  acquireR();
	  l = current_;
	  lock_.readLock().release(); 
	}
	catch(InterruptedException x) { 
	  Thread.currentThread().interrupt(); 
	  return 0;
	}

	l = compute(l);

	try {
	  acquireW();
	  set(l);
	  lock_.writeLock().release(); 
	  return l;
	}
	catch(InterruptedException x) { 
	  Thread.currentThread().interrupt(); 
	  return 0;
	}
  }  
  public void update()  { 
	long l = 0;

	try {
	  acquireR();
	  l = current_;
	  lock_.readLock().release(); 
	}
	catch(InterruptedException x) { 
	  Thread.currentThread().interrupt(); 
	  return;
	}

	l = compute(l);

	try {
	  acquireW();
	  set(l);
	  lock_.writeLock().release(); 
	}
	catch(InterruptedException x) { 
	  Thread.currentThread().interrupt(); 
	}
  }  
}
