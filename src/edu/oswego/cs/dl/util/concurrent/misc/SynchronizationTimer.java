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

/**
 *
 *  This program records times for various fine-grained synchronization
 *  schemes, and provides some ways of measuring them over different
 *  context parameters.
 *
 *  <p>
 *   Quick start: 
 *  <ol>
 *   <li>javac -d <em>base of some CLASSPATH</em> *.java <br>
 *      You'll need Swing (JFC). (This
 *      program currently imports the javax.swing versions.
 *      You can edit imports to instead use other versions.)
 *   <li>java edu.oswego.cs.dl.util.concurrent.misc.SynchronizationTimer <br>
 *   <li> Click<em> start</em>.
 *    Clicking <em>stop</em> cancels the run. Cancellation can take
 *    a while when there are a lot of threads.
 *   <li>For more explanation about tested classes, see
 *    <a href="http://gee.cs.oswego.edu/dl/classes/edu/oswego/cs/dl/util/concurrent/intro.html">Documentation for util.concurrent</a>
 *  </ol>
 *
 *  <p>
 *   Synchronization schemes are tested around implementations and
 *   subclasses of <code>RNG</code>, which is just a hacked random
 *   number generator class.  Objects of this class have just enough
 *   state and require just enough computation to be reasonable minimal
 *   targets.  (Additionally, random numbers are needed a lot in these
 *   kinds of time tests, so basing them on objects that produce random
 *   numbers is convenient.)  Computation of each random number is
 *   padded a bit with an adjustable compute loop running a random
 *   number of times to avoid getting schedulers locked into
 *   uninteresting patterns.
 *
 *  <p>
 *   Each iteration of each test ultimately somehow calls 
 *   the random number generation
 *   method of an RNG. The time listed is the average time it took to do
 *   one iteration, in microseconds. These are just based on wallclock
 *   time (System.currentTimeMillis()). Thread
 *   construction time is <em>NOT</em> included in these times. 
 *   In tests with many threads, construction and other bookkeeping
 *   can take longer than the tests themselves.
 *  <p>
 *   Results are listed in a table, and optionally printed on standard output.
 *   You can redirect standard output to save to a file. 
 *  <p>
 *   The total amount of ``real'' computation reported in each cell is
 *   the same. Thus, the unobtainably ideal pattern of results would be
 *   for every cell of the table to be the same (and very small).
 *  <p>
 *  A thread pool (PooledExecutor) is used to manage the threads used in
 *  test runs. The current number of active threads is indicated in
 *  the panel. It should normally be at most three plus the number of threads used in the
 *  indicated test column (there are at most three overhead threads per run), although
 *  it may transiently climb, and is larger in those tests that
 *  generate their own internal threads (for example ThreadedExceutor). If the
 *  indicated size fails to return to zero within about 10 seconds of 
 *  either hitting stop
 *  or the end of a run, you may have a
 *  problem with interruption handling on your Java VM. 
 *
 *  <p>
 *  This program cannot
 *  tell you how busy your computer is while running tests. 
 *  You can run a utility program (for
 *  example <code>perfmeter</code> or <code>top</code> on unix) 
 *  alongside this program
 *  to find out.
 *  <p>
 *   A number of control parameters can be changed at any time.
 *   Most combinations of parameter settings create contexts
 *   that are completely unrepresentative of those seen in practical
 *   applications. However, they can be set to provide rough analogs of
 *   real applications, and the results used as rough guesses about
 *   performance impact. Also, do not be too upset about slow
 *   performance on tests representing situations
 *   that would never occur in practice.
 *   <p>
 *
 *   You can control parameters by clicking any of the following,
 *   at any time. (You can even change parameters
 *   while tests are running, in which case they will take
 *   effect as soon as possible. Most controls momentarily stall
 *   while test objects and threads are being constructed, to avoid
 *   inconsistencies during test runs.)
 *  <dl>
 *
 *   <dt> Number of threads
 *
 *   <dd> Controls concurrency.  The indicated number of threads are
 *   started simultaneously and then waited out.
 *
 *   <dt>Contention. 
 *
 *   <dd>Percent sharing among threads. Zero percent means that each
 *   thread has its own RNG object, so there is no
 *   interference among threads.  The zero
 *   percent case thus shows the cost of synchronization mechanics that
 *   happen to never be needed.
 *   100 percent sharing means that all
 *   threads call methods on the same object, so each thread will have to
 *   wait until the RNG objects are not being used by others. 
 *   In between is in between: Only the given percentage of calls are
 *   made to shared RNG objects; others are to unshared. 
 *   Contention in classes that use Channels works slightly differently:
 *   The Channels are shared, not the base RNG objects. (Another way
 *   of looking at it is that tests demonstrate effects of multiple
 *   producers and consumers on the same channel.)
 *
 *   <dt>Classes
 *   <dd>You can choose to only test the indicated classes. You can 
 *    probably figure out how to add more classes to run yourself.
 *
 *   <dt>Calls per thread per test
 *
 *   <dd>Specifies number of iterations per thread per test.  The listed
 *   times are averages over these iterations. The default seems to
 *   provide precise enough values for informal testing purposes.
 *   You should expect to see a fair amount of variation across
 *   repeated runs.
 *   If you get zeroes printed in any cell, this means that the
 *   test ran too fast to measure in milleconds, so you should increase the 
 *   iterations value.
 *
 *   <dt> Computations per call
 *
 *   <dd>Specifies length of each call by setting an internal looping
 *   parameter inside each RNG object. Shorter calls lead to shorter
 *   times between synchronization measures. Longer calls, along with
 *   high contention can be used to force timeouts to occur.
 *
 *   <dt> Iterations per barrier.
 *
 *   <dd> Specifies the number of iterations performed by each thread until
 *    a synchronization barrier is forced with other threads, forcing
 *    it to wait for other threads to reach the same number of iterations. This
 *    controls the amount of interaction (versus contention) among threads.
 *    Setting to a value greater than the number of iterations per test
 *    effectively disables barriers.
 *
 *   <dt> Threads per barrier
 *
 *   <dd> Specifies how many threads are forced to synchronize at each
 *   barrier point. Greater numbers cause more threads to wait for each
 *   other at barriers. Setting to 1 means that a thread only has to
 *   wait for itself, which means not to wait at all.
 *
 *   <dt>Lock mode
 *
 *   <dd>For classes that support it, this controls whether mutual
 *   exclusion waits are done via standard blocking synchronization, or
 *   a loop repeatedly calling a timed wait.
 *
 *   <dt>Producer mode
 *
 *   <dd>For classes that support it, this controls whether producers
 *   perform blocking puts versus loops repeatedly calling offer.
 *
 *   <dt>Consumer mode
 *
 *   <dd>For classes that support it, this controls whether consumers
 *   perform blocking takes versus loops repeatedly calling poll.
 *
 *   <dt> Timeouts
 *
 *   <dd> Specifies the duration of timeouts used in timeout mode.  A
 *   value of zero results in pure spin-loops.
 *
 *   <dt>Producer/consumer rates.
 *
 *   <dd>For tests involving producer/consumer pairs, this controls
 *   whether the producer is much faster, about the same speed, or much
 *   slower than the consumer. This is implemented by having the
 *   producer do all, half, or none of the actual calls to update, in
 *   addition to adding elements to channel.
 *
 *   <dt>Buffer capacity 
 *
 *   <dd>For tests involving finite capacity
 *   buffers, this controls maximum buffer size.
 *
 *  </dl>
 *
 *  <p>
 *   To scaffold all this, the RNG class is defined in
 *   layers. Each RNG has internal non-public methods that do the actual
 *   computation, and public methods that call the internal ones.  The
 *   particular classes run in tests might change over time, but
 *   currently includes the following general kinds:
 *
 *  <dl>
 *
 *
 *   <dt> Using built-in synchronization
 *   <dd> Versions of RNG classes that use (or don't use)
 *    synchronized methods and/or blocks. Also some tests of
 *    simple SynchronizedVariables. Tests that would not
 *    be thread-safe are not run when there is more than one
 *    thread and non-zero contention.
 *
 *
 *   <dt> Using <code>Sync</code> classes as locks
 *   <dd> Classes protecting public methods via Semaphores, mutexes, etc.
 *    In each case, the outer public methods delegate actions to
 *    another RNG object, surrounded by acquire/release/etc. The
 *    class called SDelegated does this using builtin
 *    synchronization rather than Sync locks
 *    so might be a useful comparison.
 *
 *   <dt> Using Channels
 *   <dd> These classes work a little bit differently than the others.
 *    Each test arranges that half of the threads behave as producers,
 *    and half as consumers. Each test iteration puts/takes an RNG object
 *    through a channel before or after executing its update
 *    method. When the number of threads is one, each producer
 *    simply consumers its own object. Some Channels (notably
 *    SynchronousChannels) cannot be used with only one thread,
 *    in which case the test is skipped.
 *
 *   <dt> Using Executors
 *   <dd> These classes arrange for each RNG update to occur
 *    as an executable command. Each test iteration passes a
 *    command to an Executor, which eventually executes it.
 *    Execution is overlapped: Each iteration starts a new 
 *    command, and then waits for the previous command to complete.
 *
 *  </dl>
 *
 *  <p>
 *
 *   The test code is ugly; it has just evolved over the years.  Sorry.
**/

public class SynchronizationTimer {

  /**
   * Information about classes to be tested
   **/
  static class TestedClass { 
	final String name; 
	final Class cls; 
	final boolean multipleOK; 
	final boolean singleOK;
	final Class buffCls;
	Boolean enabled_ = new Boolean(true);

	synchronized void setEnabled(Boolean b) { enabled_ = b; }
	synchronized Boolean getEnabled() { return enabled_; }

	synchronized void toggleEnabled() {
	  boolean enabled = enabled_.booleanValue();
	  enabled_ = new Boolean(!enabled);
	}
	
	synchronized boolean isEnabled(int nthreads, Fraction shared) { 
	  boolean enabled = enabled_.booleanValue();
	  if (!enabled) return false;
	  if (!singleOK && nthreads <= 1) return false;
	  if (!multipleOK && nthreads > 1 && shared.compareTo(0) > 0) return false;
	  return true;
	}
	
	
	TestedClass(String n, Class c, boolean m, boolean sok) {
	  name = n; cls = c; multipleOK = m; singleOK = sok; 
	  buffCls = null;
	}
	
	TestedClass(String n, Class c, boolean m, boolean sok, Class bc) {
	  name = n; cls = c; multipleOK = m; singleOK = sok; 
	  buffCls = bc;
	}
	
	static final TestedClass dummy = 
	  new TestedClass("", null, false, false);

	static final TestedClass[]  classes = {
	  
	  new TestedClass("NoSynchronization", NoSynchRNG.class, false, true),
	  new TestedClass("PublicSynchronization", PublicSynchRNG.class, true, true),
	  new TestedClass("NestedSynchronization", AllSynchRNG.class, true, true),
	  
	  new TestedClass("SDelegated", SDelegatedRNG.class, true, true),
	  
	  new TestedClass("SynchLongUsingSet", SynchLongRNG.class, true, true),
	  new TestedClass("SynchLongUsingCommit", AClongRNG.class, true, true),
	  
	  new TestedClass("Semaphore", SemRNG.class, true, true),
	  new TestedClass("WaiterPrefSemaphore", WpSemRNG.class, true, true),
	  new TestedClass("FIFOSemaphore", FifoRNG.class, true, true),
	  new TestedClass("PrioritySemaphore", PrioritySemRNG.class, true, true),
	  new TestedClass("Mutex", MutexRNG.class, true, true),
	  new TestedClass("ReentrantLock", RlockRNG.class, true, true),
	  
	  new TestedClass("WriterPrefRWLock", WpRWlockRNG.class, true, true),
	  new TestedClass("ReaderPrefRWLock", ReaderPrefRWlockRNG.class, true, true),
	  new TestedClass("FIFORWLock", FIFORWlockRNG.class, true, true),
	  new TestedClass("ReentrantRWL", ReentrantRWlockRNG.class, true, true),
	  
	  
	  
	  new TestedClass("LinkedQueue", ChanRNG.class, true, true, 
					  LinkedQueue.class),
	  new TestedClass("WaitFreeQueue", ChanRNG.class, true, true, 
					  WaitFreeQueue.class),
	  new TestedClass("BoundedLinkedQueue", ChanRNG.class, true, true,
					  BoundedLinkedQueue.class),
	  new TestedClass("BoundedBuffer", ChanRNG.class, true, true,
					  BoundedBuffer.class),
	  new TestedClass("CondVarBoundedBuffer", ChanRNG.class, true, true,
					  CVBuffer.class),
	  new TestedClass("BoundedPriorityQueue", ChanRNG.class, true, true,
					  BoundedPriorityQueue.class),
	  new TestedClass("Slot", ChanRNG.class, true, true,
					  Slot.class),
	  //    new TestedClass("FIFOSlot", ChanRNG.class, true, true, FIFOSlot.class),
	  new TestedClass("SynchronousChannel", ChanRNG.class, true, false,
					  SynchronousChannel.class),

	  
	  new TestedClass("DirectExecutor", DirectExecutorRNG.class, true, true),
	  new TestedClass("SemaphoreLckExecutor", LockedSemRNG.class, true, true),
	  new TestedClass("QueuedExecutor", QueuedExecutorRNG.class, true, true),
	  new TestedClass("ThreadedExecutor", ThreadedExecutorRNG.class, true, true),
	  new TestedClass("PooledExecutor", PooledExecutorRNG.class, true, true),
	  
	  //      new TestedClass("Pipe", ChanRNG.class, true, true, PipedChannel.class),
	};
  }



  // test parameters

  static final int[] nthreadsChoices = { 
	1, 
	2, 
	4, 
	8, 
	16, 
	32, 
	64, 
	128, 
	256, 
	512, 
	1024 
  };

  static final int BLOCK_MODE = 0;
  static final int TIMEOUT_MODE = 1;

  static final int[] syncModes = { BLOCK_MODE, TIMEOUT_MODE, };

  static final int PRECISION = 10; // microseconds
	
  static class ThreadInfo {
	final String name;
	final int number;
	Boolean enabled;
	ThreadInfo(int nthr) {
	  number = nthr;
	  name = p2ToString(nthr);
	  enabled = new Boolean(true);
	}

	synchronized Boolean getEnabled() { return enabled; }
	synchronized void setEnabled(Boolean v) { enabled = v; }
	synchronized void toggleEnabled() {
	  enabled = new Boolean(!enabled.booleanValue());
	}
  }

  final ThreadInfo[] threadInfo = new ThreadInfo[nthreadsChoices.length];

  // This used to be a JTable datamodel, but now just part of this class ...

  final static int headerRows = 1;
  final static int classColumn = 0;
  final static int headerColumns = 1;
  final int tableRows = TestedClass.classes.length + headerRows;
  final int tableColumns = nthreadsChoices.length + headerColumns;
  
  final JComponent[][] resultTable_ = new JComponent[tableRows][tableColumns];
  
  final SynchronizedInt nextClassIdx_ = new SynchronizedInt(0);
  final SynchronizedInt nextThreadIdx_ = new SynchronizedInt(0);


  ClockDaemon timeDaemon = new ClockDaemon();
  
  private final SynchronizedRef contention_ = new SynchronizedRef(null);
  private final SynchronizedInt loopsPerTest_ = new SynchronizedInt(0);

  private final SynchronizedBoolean echoToSystemOut = 
	  new SynchronizedBoolean(false);


  private final JButton startstop_ = new JButton("Start");
  
  private WaitableInt testNumber_ = new WaitableInt(1);

  private SynchronizedBoolean running_ = new SynchronizedBoolean(false);

  // prevent odd class-gc problems on some VMs?
  class PrintStart implements Runnable {
	public void run() {
	  startstop_.setText("Start");
	} 
  } 


  /*
  void old_endTestSeries() {
	running_.set(false);
	SwingUtilities.invokeLater(new Runnable() {
	  public void run() {
		startstop_.setText("Start");
	  } } );
  }
  */

  class TestSeries implements Runnable {
	final int firstclass;
	final int firstnthreads;

	TestSeries() { 
	  firstclass = 0;
	  firstnthreads = 0;
	}

	TestSeries(final int firstc, final int firstnt) { 
	  firstclass = firstc;
	  firstnthreads = firstnt;
	}

	public void run() {
	  Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

	  try {
		int t = firstnthreads; 
		int c = firstclass;

		if (t < nthreadsChoices.length &&
			c < TestedClass.classes.length) {

		  for (;;) {

			
			// these checks are duplicated in OneTest, but added here
			// to minimize unecessary thread construction, 
			// which can skew results

			if (threadEnabled(t)) {

			  TestedClass entry = TestedClass.classes[c];
		
			  int nthreads = nthreadsChoices[t];
			  int iters = loopsPerTest_.get();
			  Fraction pshr = (Fraction)(contention_.get());
		
			  if (entry.isEnabled(nthreads, pshr)) {

				runOneTest(new OneTest(c, t));
			  }
			}

			if (++c >= TestedClass.classes.length) {
			  c = 0;
			  if (++t >= nthreadsChoices.length) 
				break;
			}

			nextClassIdx_.set(c);
			nextThreadIdx_.set(t);
			
		  }
		}

	  }
	  catch (InterruptedException ex) { 
		Thread.currentThread().interrupt();
	  }
	  finally {
		endTestSeries();
	  }
	}
  }

  static class BarrierTimer implements Runnable {
	private long startTime_ = 0;
	private long endTime_ = 0;

	public synchronized long getTime() {
	  return endTime_ - startTime_;
	}

	public synchronized void run() {
	  long now = System.currentTimeMillis();
	  if (startTime_ == 0) 
		startTime_ = now;
	  else
		endTime_ = now;
	}
  }
	  
  class OneTest implements Runnable {
	final int clsIdx; 
	final int nthreadsIdx; 

	OneTest(int idx, int t) {
	  clsIdx = idx; 
	  nthreadsIdx = t; 
	}

	public void run() {
	  Thread.currentThread().setPriority(Thread.NORM_PRIORITY-3);

	  boolean wasInterrupted = false;

	  final TestedClass entry = TestedClass.classes[clsIdx];

	  final JLabel cell = (JLabel)(resultTable_[clsIdx+1][nthreadsIdx+1]);
	  final Color oldfg =  cell.getForeground();

	  try {


		if (Thread.interrupted()) return;
		if (!threadEnabled(nthreadsIdx)) return;
		
		int nthreads = nthreadsChoices[nthreadsIdx];
		int iters = loopsPerTest_.get();
		Fraction pshr = (Fraction)(contention_.get());
		
		if (!entry.isEnabled(nthreads, pshr))  return;

		BarrierTimer timer = new BarrierTimer();
		CyclicBarrier barrier = new CyclicBarrier(nthreads+1, timer);

		Class cls = entry.cls;
		Class chanCls = entry.buffCls;

		try {
		  SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
			  cell.setForeground(Color.blue);
			  cell.setText("RUN");
			  cell.repaint();
			}
		  });
		}
		catch (InvocationTargetException ex) {
		  ex.printStackTrace();
		  System.exit(-1);
		}
		synchronized (RNG.constructionLock) {
		  RNG.reset(nthreads);

		  if (chanCls == null) {
			RNG shared = (RNG)(cls.newInstance());
			for (int k = 0; k < nthreads; ++k) {
			  RNG pri = (RNG)(cls.newInstance());
			  TestLoop l = new TestLoop(shared, pri, pshr, iters, barrier);
			  Threads.pool.execute(l.testLoop());
			}
		  }
		  else {
			Channel shared = (Channel)(chanCls.newInstance());
			if (nthreads == 1) {
			  ChanRNG single = (ChanRNG)(cls.newInstance());
			  single.setSingle(true);
			  PCTestLoop l = new PCTestLoop(single.getDelegate(), single, pshr,
											iters, barrier,
											shared, shared);
			  Threads.pool.execute(l.testLoop(true));
			}
			else if (nthreads % 2 != 0) 
			  throw new Error("Must have even number of threads!");
			else {
			  int npairs = nthreads / 2;
			  
			  for (int k = 0; k < npairs; ++k) {
				ChanRNG t = (ChanRNG)(cls.newInstance());
				t.setSingle(false);
				Channel chan = (Channel)(chanCls.newInstance());
				
				PCTestLoop l = new PCTestLoop(t.getDelegate(), t, pshr, 
											  iters, barrier,
											  shared, chan);
				
				Threads.pool.execute(l.testLoop(false));
				Threads.pool.execute(l.testLoop(true));
				
			  }
			}
		  }

		  if (echoToSystemOut.get()) {
			System.out.print(
							 entry.name + " " +
							 nthreads + "T " +
							 pshr + "S " +
							 RNG.computeLoops.get() + "I " +
							 RNG.syncMode.get() + "Lm " +
							 RNG.timeout.get() + "TO " +
							 RNG.producerMode.get() + "Pm " +
							 RNG.consumerMode.get() + "Cm " +
							 RNG.bias.get() + "B " +
							 DefaultChannelCapacity.get() + "C " +
							 RNG.exchangeParties.get() + "Xp " +
							 RNG.itersPerBarrier.get() + "Ib : "
							 );
		  }

		}
		
		// Uncomment if AWT doesn't update right
		//        Thread.sleep(100);

		barrier.barrier(); // start

		barrier.barrier(); // stop

		long tm = timer.getTime();
		long totalIters = nthreads * iters;
		double dns = tm * 1000.0 * PRECISION / totalIters;
		long ns = Math.round(dns);

		setTime(ns, clsIdx, nthreadsIdx);

		if (echoToSystemOut.get()) {
		  System.out.println(formatTime(ns, true));
		}

	  }
	  catch (BrokenBarrierException ex) { 
		wasInterrupted = true;
	  }
	  catch (InterruptedException ex) {
		wasInterrupted = true;
		Thread.currentThread().interrupt();
	  }
	  catch (Exception ex) { 
		ex.printStackTrace();
		System.out.println("Construction Exception?");
		System.exit(-1);
	  }
	  finally {
		final boolean clear = wasInterrupted;
		SwingUtilities.invokeLater(new Runnable() {
		  public void run() {
			if (clear) cell.setText("");
			cell.setForeground(oldfg);
			cell.repaint();
		  }
		});

		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		endOneTest();
	  }
	}
  }

  public SynchronizationTimer() { 
	for (int i = 0; i < threadInfo.length; ++i) 
	  threadInfo[i] = new ThreadInfo(nthreadsChoices[i]);

  }  
  JComboBox barrierBox() {
	final int[] itersPerBarrierChoices = { 
	  1,
	  2,
	  4,
	  8,
	  16,
	  32,
	  64,
	  128,
	  256,
	  512,
	  1024,
	  2 * 1024,
	  4 * 1024,
	  8 * 1024,
	  16 * 1024,
	  32 * 1024,
	  64 * 1024, 
	  128 * 1024, 
	  256 * 1024, 
	  512 * 1024, 
	  1024 * 1024,
	};
	
	JComboBox barrierComboBox = new JComboBox();
	
	for (int j = 0; j < itersPerBarrierChoices.length; ++j) {
	  String lab = p2ToString(itersPerBarrierChoices[j]) + 
		" iterations per barrier";
	  barrierComboBox.addItem(lab);
	}
	barrierComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.itersPerBarrier.set(itersPerBarrierChoices[idx]);
	  }
	});
	
	RNG.itersPerBarrier.set(itersPerBarrierChoices[12]);
	barrierComboBox.setSelectedIndex(12);
	return barrierComboBox;
  }  
  JComboBox biasBox() {
	final int[] biasChoices = { 
	  -1, 
	  0, 
	  1 
	};
	
	
	JComboBox biasComboBox = new JComboBox();
	
	for (int j = 0; j < biasChoices.length; ++j) {
	  String lab = biasToString(biasChoices[j]);
	  biasComboBox.addItem(lab);
	}
	biasComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.bias.set(biasChoices[idx]);
	  }
	});
	
	RNG.bias.set(biasChoices[1]);
	biasComboBox.setSelectedIndex(1);
	return biasComboBox;
  }  
  static String biasToString(int b) {
	String sms;
	if (b < 0) sms =       "slower producer";
	else if (b == 0) sms = "balanced prod/cons rate";
	else if (b > 0) sms =  "slower consumer";
	else sms = "No such bias";
	return sms;
  }  
  void cancel() { 
	//  not stable enough to cancel during construction
	synchronized (RNG.constructionLock) {
	  try {
		Threads.pool.interruptAll();
	  }
	  catch(Exception ex) {
		System.out.println("\nException during cancel:\n" + ex);
		return;
	  }
	}
  }  
  JComboBox capacityBox() {
	
	final int[] bufferCapacityChoices = {
	  1,
	  4,
	  64,
	  256,
	  1024,
	  4096,
	  16 * 1024,
	  64 * 1024,
	  256 * 1024,
	  1024 * 1024,
	};
	
	JComboBox bcapComboBox = new JComboBox();
	
	for (int j = 0; j < bufferCapacityChoices.length; ++j) {
	  String lab = p2ToString(bufferCapacityChoices[j]) + 
		" element bounded buffers";
	  bcapComboBox.addItem(lab);
	}
	bcapComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		DefaultChannelCapacity.set(bufferCapacityChoices[idx]);
	  }
	});
	
	
	DefaultChannelCapacity.set(bufferCapacityChoices[3]);
	bcapComboBox.setSelectedIndex(3);
	return bcapComboBox;
  }  
  void clearTable() {
	for (int i = 1; i < tableRows; ++i) {
	  for (int j = 1; j < tableColumns; ++j) {
		((JLabel)(resultTable_[i][j])).setText("");
	  }
	}
  }  
  JComboBox cloopBox() {
	final int[] computationsPerCallChoices = { 
	  1,
	  2,
	  4,
	  8,
	  16,
	  32,
	  64,
	  128,
	  256,
	  512,
	  1024,
	  2 * 1024,
	  4 * 1024,
	  8 * 1024,
	  16 * 1024,
	  32 * 1024,
	  64 * 1024,
	};
	
	JComboBox cloopComboBox = new JComboBox();
	
	for (int j = 0; j < computationsPerCallChoices.length; ++j) {
	  String lab = p2ToString(computationsPerCallChoices[j]) + 
		" computations per call";
	  cloopComboBox.addItem(lab);
	}
	cloopComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.computeLoops.set(computationsPerCallChoices[idx]);
	  }
	});
	
	RNG.computeLoops.set(computationsPerCallChoices[3]);
	cloopComboBox.setSelectedIndex(3);
	return cloopComboBox;
  }  
  JComboBox consumerSyncModePanel() {
	JComboBox consumerSyncModeComboBox = new JComboBox();
	
	for (int j = 0; j < syncModes.length; ++j) {
	  String lab = "Consumers: " + modeToString(syncModes[j]);
	  consumerSyncModeComboBox.addItem(lab);
	}
	consumerSyncModeComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.consumerMode.set(syncModes[idx]);
	  }
	});
	
	RNG.consumerMode.set(syncModes[0]);
	consumerSyncModeComboBox.setSelectedIndex(0);
	return consumerSyncModeComboBox;
  }  
  JComboBox contentionBox() {
	final  Fraction[] contentionChoices = { 
	  new Fraction(0, 1),
	  new Fraction(1, 16),
	  new Fraction(1, 8),
	  new Fraction(1, 4),
	  new Fraction(1, 2),
	  new Fraction(1, 1)
	};
	
	JComboBox contentionComboBox = new JComboBox();
	
	for (int j = 0; j < contentionChoices.length; ++j) {
	  String lab = contentionChoices[j].asDouble() * 100.0 + 
		"% contention/sharing";
	  contentionComboBox.addItem(lab);
	}
	contentionComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		contention_.set(contentionChoices[idx]);
	  }
	});
	
	contention_.set(contentionChoices[3]);
	contentionComboBox.setSelectedIndex(3);
	return contentionComboBox;
  }  
  private void endOneTest() {
	testNumber_.increment();
  }  
  void endTestSeries() {
	running_.set(false);
	SwingUtilities.invokeLater(new PrintStart());
  }  
  JComboBox exchangeBox() {
	final int[] exchangerChoices = { 
	  1,
	  2,
	  4,
	  8,
	  16,
	  32,
	  64,
	  128,
	  256,
	  512,
	  1024,
	};
	
	JComboBox exchComboBox = new JComboBox();
	
	for (int j = 0; j < exchangerChoices.length; ++j) {
	  String lab = p2ToString(exchangerChoices[j]) + 
		" max threads per barrier";
	  exchComboBox.addItem(lab);
	}
	exchComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.exchangeParties.set(exchangerChoices[idx]);
	  }
	});
	
	RNG.exchangeParties.set(exchangerChoices[1]);
	exchComboBox.setSelectedIndex(1);
	return exchComboBox;
  }  
  static String formatTime(long ns, boolean showDecimal) {
	long intpart = ns / PRECISION;
	long decpart = ns % PRECISION;
	if (!showDecimal) {
	  if (decpart >= PRECISION/2)
		++intpart;
	  return Long.toString(intpart);
	}
	else {
	  String sint = Long.toString(intpart);
	  String sdec = Long.toString(decpart);
	  if (decpart == 0) {
		int z = PRECISION;
		while (z > 10) {
		  sdec = "0" + sdec;
		  z /= 10;
		}
	  }
	  String ts = sint + "." + sdec;
	  return ts;
	}
  }  
  JComboBox itersBox() {
	final int[] loopsPerTestChoices = { 
	  1,
	  16,
	  256,
	  1024,
	  2 * 1024, 
	  4 * 1024, 
	  8 * 1024, 
	  16 * 1024,
	  32 * 1024,
	  64 * 1024, 
	  128 * 1024, 
	  256 * 1024, 
	  512 * 1024, 
	  1024 * 1024, 
	};
	
	JComboBox precComboBox = new JComboBox();
	
	for (int j = 0; j < loopsPerTestChoices.length; ++j) {
	  String lab = p2ToString(loopsPerTestChoices[j]) + 
		" calls per thread per test";
	  precComboBox.addItem(lab);
	}
	precComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		loopsPerTest_.set(loopsPerTestChoices[idx]);
	  }
	});
	
	loopsPerTest_.set(loopsPerTestChoices[7]);
	precComboBox.setSelectedIndex(7);
	return precComboBox;
  }  
  /** Start up this application **/
  public static void main(String[] args) {

	JFrame frame = new JFrame("Times per call in microseconds"); 

	frame.addWindowListener(new WindowAdapter() {
	  public void windowClosing(WindowEvent e) {System.exit(0);}
	});
  
	frame.getContentPane().add(new SynchronizationTimer().mainPanel());
	frame.pack();
	frame.setVisible(true);
  }  
  JPanel mainPanel() {
	new PrintStart(); // classloader bug workaround
	JPanel paramPanel = new JPanel();
	paramPanel.setLayout(new GridLayout(5, 3));

	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(1, 3));
	
	startstop_.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent evt) {
		if (running_.get()) 
		  cancel();
		else {
		  try { 
			startTestSeries(new TestSeries());  
		  }
		  catch (InterruptedException ex) { 
			endTestSeries(); 
		  }
		}
	  }});
	
	paramPanel.add(startstop_);
	
	JPanel p1 = new JPanel();
	p1.setLayout(new GridLayout(1, 2));
	
	JButton continueButton = new JButton("Continue");

	continueButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent evt) {
		if (!running_.get()) {
		  try { 
			startTestSeries(new TestSeries(nextClassIdx_.get(),
										   nextThreadIdx_.get()));  
		  }
		  catch (InterruptedException ex) { 
			endTestSeries(); 
		  }
		}
	  }});

	p1.add(continueButton);

	JButton clearButton = new JButton("Clear cells");
	
	clearButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent evt) {
		clearTable();
	  }
	});

	p1.add(clearButton);

	paramPanel.add(p1);

	JPanel p3 = new JPanel();
	p3.setLayout(new GridLayout(1, 2));
	
	JButton setButton = new JButton("All classes");
	
	setButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent evt) {
		setChecks(true);
	  }
	});

	p3.add(setButton);


	JButton unsetButton = new JButton("No classes");
	
	unsetButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent evt) {
		setChecks(false);
	  }
	});

	p3.add(unsetButton);
	paramPanel.add(p3);

	JPanel p2 = new JPanel();
	//    p2.setLayout(new GridLayout(1, 2));
	p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));


	JCheckBox consoleBox = new JCheckBox("Console echo");
	consoleBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		echoToSystemOut.complement();
	  }
	});

	

	JLabel poolinfo  = new JLabel("Active threads:      0");

	p2.add(poolinfo);
	p2.add(consoleBox);

	paramPanel.add(p2);

	paramPanel.add(contentionBox());
	paramPanel.add(itersBox());
	paramPanel.add(cloopBox());
	paramPanel.add(barrierBox());
	paramPanel.add(exchangeBox());
	paramPanel.add(biasBox());
	paramPanel.add(capacityBox());
	paramPanel.add(timeoutBox());
	paramPanel.add(syncModePanel());
	paramPanel.add(producerSyncModePanel());
	paramPanel.add(consumerSyncModePanel());

	startPoolStatus(poolinfo);

	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	JPanel tblPanel = resultPanel();

	mainPanel.add(tblPanel);
	mainPanel.add(paramPanel);
	return mainPanel;
  }  
  // misc formatting utilities

  static String modeToString(int m) {
	String sms;
	if (m == BLOCK_MODE) sms = "block";
	else if (m == TIMEOUT_MODE) sms = "timeout";
	else sms = "No such mode";
	return sms;
  }  
  static String p2ToString(int n) { // print power of two
	String suf = "";
	if (n >= 1024) {
	  n = n / 1024;
	  suf = "K";
	  if (n >= 1024) {
		n = n / 1024;
		suf = "M";
	  }
	}
	return n + suf;
  }  
  JComboBox producerSyncModePanel() {
	JComboBox producerSyncModeComboBox = new JComboBox();
	
	for (int j = 0; j < syncModes.length; ++j) {
	  String lab = "Producers: " + modeToString(syncModes[j]);
	  producerSyncModeComboBox.addItem(lab);
	}
	producerSyncModeComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.producerMode.set(syncModes[idx]);
	  }
	});
	
	RNG.producerMode.set(syncModes[0]);
	producerSyncModeComboBox.setSelectedIndex(0);
	return producerSyncModeComboBox;
  }  
  JPanel resultPanel() {

	JPanel[] colPanel = new JPanel[tableColumns];
	for (int col = 0; col < tableColumns; ++col) {
	  colPanel[col] = new JPanel();
	  colPanel[col].setLayout(new GridLayout(tableRows, 1));
	  if (col != 0)
		colPanel[col].setBackground(Color.white);
	}

	Color hdrbg = colPanel[0].getBackground();
	Border border = new LineBorder(hdrbg);

	Font font = new Font("Dialog", Font.PLAIN, 12);
	Dimension labDim = new Dimension(40, 16);
	Dimension cbDim = new Dimension(154, 16);

	JLabel cornerLab = new JLabel(" Classes      \\      Threads");
	cornerLab.setMinimumSize(cbDim);
	cornerLab.setPreferredSize(cbDim);
	cornerLab.setFont(font);
	resultTable_[0][0] = cornerLab;
	colPanel[0].add(cornerLab);
	
	for (int col = 1; col < tableColumns; ++col) {
	  final int nthreads = col - headerColumns;
	  JCheckBox tcb = new JCheckBox(threadInfo[nthreads].name, true);
	  tcb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		  threadInfo[nthreads].toggleEnabled();
		}});
	  
	  
	  tcb.setMinimumSize(labDim);
	  tcb.setPreferredSize(labDim);
	  tcb.setFont(font);
	  tcb.setBackground(hdrbg);
	  resultTable_[0][col] = tcb;
	  colPanel[col].add(tcb);
	}
	
	
	for (int row = 1; row < tableRows; ++row) {
	  final int cls = row - headerRows;
	  
	  JCheckBox cb = new JCheckBox(TestedClass.classes[cls].name, true); 
	  cb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		  TestedClass.classes[cls].toggleEnabled();
		}});
	  
	  resultTable_[row][0] = cb;
	  cb.setMinimumSize(cbDim);
	  cb.setPreferredSize(cbDim);
	  cb.setFont(font);
	  colPanel[0].add(cb);
	  
	  for (int col = 1; col < tableColumns; ++col) {
		int nthreads = col - headerColumns;
		JLabel lab = new JLabel("");
		resultTable_[row][col] = lab;
		
		lab.setMinimumSize(labDim);
		lab.setPreferredSize(labDim);
		lab.setBorder(border); 
		lab.setFont(font);
		lab.setBackground(Color.white);
		lab.setForeground(Color.black);
		lab.setHorizontalAlignment(JLabel.RIGHT);
		
		colPanel[col].add(lab);
	  }
	}
	
	JPanel tblPanel = new JPanel();
	tblPanel.setLayout(new BoxLayout(tblPanel, BoxLayout.X_AXIS));
	for (int col = 0; col < tableColumns; ++col) {
	  tblPanel.add(colPanel[col]);
	}
	
	return tblPanel;
	
  }  
  private void runOneTest(Runnable tst) throws InterruptedException { 
	int nt = testNumber_.get(); 
	Threads.pool.execute(tst);
	testNumber_.whenNotEqual(nt, null);
  }  
  void setChecks(final boolean setting) {
	for (int i = 0; i < TestedClass.classes.length; ++i) {
	  TestedClass.classes[i].setEnabled(new Boolean(setting));
	  ((JCheckBox)resultTable_[i+1][0]).setSelected(setting);
	}
  }  
  void setTime(final long ns, int clsIdx, int nthrIdx) {
	int row = clsIdx+headerRows;
	int col = nthrIdx+headerColumns;
	final JLabel cell = (JLabel)(resultTable_[row][col]);

	SwingUtilities.invokeLater(new Runnable() {
	  public void run() { 
		cell.setText(formatTime(ns, true)); 
	  } 
	});
  }  
  void startPoolStatus(final JLabel status) {
	Runnable updater = new Runnable() {
	  int lastps = 0;
	  public void run() {
		final int ps = Threads.activeThreads.get();
		if (lastps != ps) {
		  lastps = ps;
		  SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			  status.setText("Active threads: " + ps);
			} } );
		}
	  }
	};
	timeDaemon.executePeriodically(250, updater, false);
  }  
  void startTestSeries(Runnable tst) throws InterruptedException {
	running_.set(true);
	startstop_.setText("Stop");
	Threads.pool.execute(tst);
  }  
  JComboBox syncModePanel() {
	JComboBox syncModeComboBox = new JComboBox();
	
	for (int j = 0; j < syncModes.length; ++j) {
	  String lab = "Locks: " + modeToString(syncModes[j]);
	  syncModeComboBox.addItem(lab);
	}
	syncModeComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.syncMode.set(syncModes[idx]);
	  }
	});
	
	RNG.syncMode.set(syncModes[0]);
	syncModeComboBox.setSelectedIndex(0);
	return syncModeComboBox;
  }  
  boolean threadEnabled(int nthreads) {
	return threadInfo[nthreads].getEnabled().booleanValue();
  }  
  JComboBox timeoutBox() {
	
	
	final long[] timeoutChoices = {
	  0,
	  1,
	  10,
	  100,
	  1000,
	  10000,
	  100000,
	};
	
	
	JComboBox timeoutComboBox = new JComboBox();
	
	for (int j = 0; j < timeoutChoices.length; ++j) {
	  String lab = timeoutChoices[j] + " msec timeouts";
	  timeoutComboBox.addItem(lab);
	}
	timeoutComboBox.addItemListener(new ItemListener() {
	  public void itemStateChanged(ItemEvent evt) {
		JComboBox src = (JComboBox)(evt.getItemSelectable());
		int idx = src.getSelectedIndex();
		RNG.timeout.set(timeoutChoices[idx]);
	  }
	});
	
	RNG.timeout.set(timeoutChoices[3]);
	timeoutComboBox.setSelectedIndex(3);
	return timeoutComboBox;
  }  
}
