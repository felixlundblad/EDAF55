package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;

public class SharedData {

	/*
	 * Attributes for the time
	 */
	private int hours;
	private int minutes;
	private int seconds;
	private int alarmTime;
	private int alarmCounter;

	private boolean alarmBeep;
	
	private ClockInput input;
	private ClockOutput output;
	
	private MutexSem mutex; // Locks setTime or incTime

	public SharedData(ClockInput input, ClockOutput output) {
		mutex = new MutexSem();
		mutex.take();
		hours = 0;
		minutes = 0;
		seconds = 0;
		alarmBeep = false;
		alarmCounter = 0;
		this.input = input;
		this.output = output;
		mutex.give();
	}

	public int alarmCounter() {
		return alarmCounter;
	}

	public void incAlarmCounter() {
		mutex.take();
		alarmCounter++;
		mutex.give();
	}

	public void setAlarm(int alarmTime) {
		mutex.take();
		this.alarmTime = alarmTime;			
		mutex.give();
	}

	public int getAlarmTime() {
		return alarmTime;
	}

	public void turnOffAlarmBeep() {
		mutex.take();
		alarmBeep = false;
		alarmCounter = 0;
		mutex.give();
	}


	public void turnOnAlarmBeep() {
		mutex.take();
		alarmBeep = true;
		mutex.give();
	}

	public boolean isAlarmBeeping() {
		return alarmBeep;
	}

	//		int getHours() {
	//			return hours;
	//		}
	//
	//		int getMinutes() {
	//			return minutes;
	//		}
	//
	//		int getSeconds() {
	//			return seconds;
	//		}

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

		if(getTime() == getAlarmTime()) turnOnAlarmBeep();

		if(input.getAlarmFlag() && isAlarmBeeping() && alarmCounter() < 20) {
			output.doAlarm();
			incAlarmCounter();
		}else {
			turnOffAlarmBeep();
		}

		/*
		 * Multiplication done to get the numbers on the right place
		 */
		output.showTime(10000*hours + 100*minutes + seconds); 

		mutex.give();

	}

	public void setTime(int hhmmss) {
		mutex.take();
		hours = hhmmss/10000;
		minutes = (hhmmss - hours*10000)/100;
		seconds = hhmmss - minutes*100 - hours*10000;
		mutex.give();
	}

}

