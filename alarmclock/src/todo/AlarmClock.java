package todo;
import done.*;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock{

	private ClockInput	input;
	private ClockOutput	output;
	
	private Thread clockThread;
	private Thread buttonThread;

	SharedData sd;

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		sd = new SharedData(input, output);
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		try {
			buttonThread.interrupt();
			clockThread.interrupt();
			buttonThread.join();
			clockThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//output.console("AlarmClock exit.");
	}

	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		clockThread = new Thread(new TimeHandler(sd));
		buttonThread = new Thread(new ButtonHandler(input, sd));

		clockThread.start(); 
		buttonThread.start();
	}
}

