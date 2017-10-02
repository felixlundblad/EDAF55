package queue;

import java.util.LinkedList;

class YourMonitor {
	private int nCounters;
	private int qNum;
	private int servedNum;
	private LinkedList<Integer> clercList;
	private int clercFree;
	// Put your attributes here...

	YourMonitor(int n) { 
		nCounters = n;
		qNum = 0;
		servedNum = 0;
		clercList = new LinkedList();
		// Initialize your attributes here...
	}

	/**
	 * Return the next queue number in the intervall 0...99. 
	 * There is never more than 100 customers waiting.
	 */
	synchronized int customerArrived() { 
		int qNum = this.qNum;
		this.qNum++;
		if(qNum == 100) qNum = 0;
		return qNum;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any. 
	 */
	synchronized void clerkFree(int id) { 
		if(!clercList.contains(id)){
			servedNum++;
			if(servedNum >= 100) servedNum = 0;
			clercList.add(id);
			clercFree = id;
		}
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then
	 * return the queue number of next customer to serve and the counter 
	 * number of the engaged clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException { 
		DispData d = new DispData();
		d.ticket = servedNum;
		d.counter = this.clercFree; // Kolla på det här med clercFree och clercList
		return d;
	}
}
