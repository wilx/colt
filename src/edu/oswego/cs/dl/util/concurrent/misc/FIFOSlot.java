package edu.oswego.cs.dl.util.concurrent.misc;

import  edu.oswego.cs.dl.util.concurrent.*;


// demo showing one way to make special channels

public class FIFOSlot implements BoundedChannel {
  private final Slot slot_;

  public FIFOSlot() {
	try {
	  slot_ = new Slot(FIFOSemaphore.class);
	}
	catch (Exception ex) {
	  ex.printStackTrace();
	  throw new Error("Cannot make Slot?");
	}
  }  
  public int capacity() { return 1; }  
  public boolean offer(Object item, long msecs) throws InterruptedException {
	return slot_.offer(item, msecs);
  }  
  public Object peek() {
	return slot_.peek();
  }  
  public Object poll(long msecs) throws InterruptedException {
	return slot_.poll(msecs);
  }  
  public void put(Object item) throws InterruptedException { 
	slot_.put(item); 
  }  
  public Object take() throws InterruptedException { 
	return slot_.take(); 
  }  
}
