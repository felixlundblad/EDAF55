package todo;
import done.*;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock{

	private ClockInput	input;
	private ClockOutput	output;
	private Semaphore	signal; 
	// Declare thread objects here..
	//private Thread t1;
	private Thread clockThread;
	private Thread buttonThread;

	SharedData sd = new SharedData();

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		signal = input.getSemaphoreInstance();
		//t1 = new Thread();
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
		output.console("AlarmClock exit.");
	}

	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		// Delete/replace the following test/demo code;
		// make something happen by exercising the IO:

		// Create thread objects here...
		clockThread = new Thread(new TimeHandler());
		buttonThread = new Thread(new ButtonHandler());


		// Create threads of execution by calling start...
		clockThread.start(); 
		buttonThread.start();
	}

	class InputOutputTest implements Runnable {
		public void run() {
			long curr; int time, mode; boolean flag;
			output.console("Click on GUI to obtain key presses!");
			while (!Thread.currentThread().isInterrupted()) {
				curr = System.currentTimeMillis();
				time = input.getValue();
				flag = input.getAlarmFlag();
				mode = input.getChoice();
				output.doAlarm();
				output.console(curr, time, flag, mode);
				if (time == 120000) break; // Swe: Bryter f�r middag
				signal.take();
			}
			output.console("IO-test terminated #");
		}
	}



	class TimeHandler implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(1000 - System.currentTimeMillis()%1000);
				} catch (InterruptedException e) {
					break;
				}
				sd.incTime();
				
				if(sd.getTime() == sd.getAlarmTime()) sd.turnOnAlarmBeep();
				
				if(input.getAlarmFlag() && sd.isAlarmBeeping() && sd.alarmCounter() < 20) {
					output.doAlarm();
					sd.incAlarmCounter();
				}else {
					sd.turnOffAlarmBeep();
				}
			}
		}

	}

	class ButtonHandler implements Runnable{
		int oldChoice;
		public void run() {
			oldChoice = input.getChoice();
			int currChoice;
			while(!Thread.currentThread().isInterrupted()) { //May be unnecessary
				try{
					input.getSemaphoreInstance().take();
				}catch(Error e){
					break;
				}
				currChoice = input.getChoice();
				switch (currChoice) {
				case ClockInput.SHOW_TIME:
					System.out.println("SHOW_TIME : ");
					sd.turnOffAlarmBeep();
					break;

				case ClockInput.SET_ALARM:
					System.out.println("SET_ALARM : ");
					sd.turnOffAlarmBeep();
					break;

				case ClockInput.SET_TIME:
					System.out.println("SET_TIME : ");
					sd.turnOffAlarmBeep();
					break;
				}

				if(oldChoice == ClockInput.SET_TIME && input.getChoice() == ClockInput.SHOW_TIME) {
					sd.setTime(input.getValue());
				}else if(oldChoice == ClockInput.SET_ALARM && input.getChoice() == ClockInput.SHOW_TIME){
					sd.setAlarm(input.getValue());
				}
				oldChoice = currChoice;
			}
		}
	}

	class SharedData{

		/*
		 * Attributes for the time
		 */
		private int hours;
		private int minutes;
		private int seconds;
		private int alarmTime;
		private int alarmCounter;
		
		private boolean alarmBeep;

		private MutexSem mutex; // Locks setTime or incTime

		public SharedData() {
			hours = 0;
			minutes = 0;
			seconds = 0;
			mutex = new MutexSem();
			alarmBeep = false;
			alarmCounter = 0;
		}

		public int alarmCounter() {
			return alarmCounter;
		}

		public void incAlarmCounter() {
			alarmCounter++;
		}

		public void setAlarm(int alarmTime) {
			this.alarmTime = alarmTime;			
		}

		public int getAlarmTime() {
			return alarmTime;
		}

		public void turnOffAlarmBeep() {
			alarmBeep = false;
			alarmCounter = 0;
		}
		
		
		public void turnOnAlarmBeep() {
			alarmBeep = true;
		}
		
		public boolean isAlarmBeeping() {
			return alarmBeep;
		}

		int getHours() {
			return hours;
		}

		int getMinutes() {
			return minutes;
		}

		int getSeconds() {
			return seconds;
		}

		int getTime() {
			return 10000*hours + 100*minutes + seconds;
		}

		public void incTime() {
			mutex.take();
			seconds++;
			if(seconds >= 60) {
				seconds-= 60;
				minutes++;
			}
			if(minutes >= 60) {
				minutes -= 60;
				hours++;
			}
			if(hours >= 24)	hours -= 24;

			System.out.println(10000*hours + 100*minutes + seconds);
			mutex.give();

			/*
			 * Multiplication done to get the numbers on the right place
			 */
			output.showTime(10000*hours + 100*minutes + seconds); 
		}

		public void setTime(int hhmmss) {
			mutex.take();
			hours = hhmmss/10000;
			minutes = (hhmmss - hours*10000)/100;
			seconds = hhmmss - minutes*100 - hours*10000;
			mutex.give();
		}

	}
}

