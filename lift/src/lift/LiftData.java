package lift;

public class LiftData {
	private LiftView lv;
	private int here; 			
	private int next;		
	private int [] waitEntry ;	
	private int [] waitExit; 	
	private int load; 			
	private int waiting;

	private static final int MAX_LOAD = 4;
	private static final int TOP_FLOOR = 6;
	private static final int BOTTOM_FLOOR = 0;

	private boolean goingUp;

	public LiftData(LiftView lv) {
		goingUp = true;
		here = 0;
		next = 0;
		load = 0;
		waiting = 0;
		waitEntry = new int[TOP_FLOOR + 1];
		waitExit = new int[TOP_FLOOR + 1];
		this.lv = lv;
	}

	public void useLift(int entryLevel, int exitLevel) throws InterruptedException {
		enterLift(entryLevel);
		exitLift(exitLevel);
		//System.out.println("Done with lift");
	}

	private synchronized void enterLift(int entryLevel) throws InterruptedException {
		//System.out.println("Waiting at floor: " + entryLevel);
		waiting++;
		waitEntry[entryLevel]++;
		//drawPeople();
		lv.drawLevel(entryLevel, waitEntry[entryLevel]);
		notifyAll();
		while (entryLevel != here || load >= MAX_LOAD) {
			notifyAll();
			wait();
		}
		//Get in the lift here

		//System.out.println("Entering elevator: " + here);
		waitEntry[here]--;
		waiting--;
		load++;
		lv.drawLevel(here, waitEntry[here]);
		lv.drawLift(here, load);
	}

	private synchronized void exitLift(int exitLevel) throws InterruptedException {
		//System.out.println("Getting of at level " + exitLevel);
		waitExit[exitLevel]++;
		while (exitLevel != here) {
			notifyAll();
			wait();
		}
		// Get off the lift here
		load--;
		waitExit[exitLevel]--;
		lv.drawLift(here, load);
	}

	// This method starves the others
	public synchronized void moveLift() throws InterruptedException {
//		System.out.println("moveLift");
		do{
			wait();
			notifyAll();
		}while(waiting == 0 && load == 0);
		drawPeople();
		while(waitExit[here] > 0){
			notifyAll();
			//System.out.println("Waiting for people to exit.");
			wait();
		}
		while(load < MAX_LOAD && waitEntry[here] > 0) {
			notifyAll();
			//System.out.println("Waiting for people to enter.");
			wait();
		}

		//System.out.println("Moving lift from " + here + " to " + next);
		drawPeople();
		checkIfGoingUp();
		lv.moveLift(here, next);
		here = next;
		drawPeople();
		notifyAll();
//		if(here == TOP_FLOOR || here == BOTTOM_FLOOR) goingUp = !goingUp; 
	}
	
	private void checkIfGoingUp() {
		if(here == TOP_FLOOR) {
			goingUp = false;
			next--;
			return;
		}else if(here == BOTTOM_FLOOR) {
			goingUp = true;
			next++;
			return;
		}
		
		int i;
		if(goingUp) {
			for (i = here; i < waitEntry.length; i++) {
				if(waitEntry[i] != 0 || waitExit[i] != 0) {
					goingUp = true;
					next++;
					return;
				}
			}
			goingUp = false;
			next--;
			return;
		}else {
			for (i = here; i >= 0; i--) {
				if(waitEntry[i] != 0 || waitExit[i] != 0) {
					goingUp = false;
					next--;
					return;
				}
			}
			goingUp = true;
			next++;
			return;
		}
	}

	private void drawPeople(){
		for(int i = 0; i < TOP_FLOOR; i++){
			lv.drawLevel(i, waitEntry[i]);
			if(i == here){
				lv.drawLift(i, load);
			}
		}
	}

}
