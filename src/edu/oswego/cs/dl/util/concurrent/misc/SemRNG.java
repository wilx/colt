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

class SemRNG extends SyncDelegatedRNG {
  public SemRNG() { super(new Semaphore(1)); }  
}
