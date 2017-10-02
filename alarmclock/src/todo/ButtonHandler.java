package todo;

import done.ClockInput;

class ButtonHandler implements Runnable{
	int oldChoice;
	ClockInput input;
	SharedData sd;
	
	public ButtonHandler(ClockInput input, SharedData sd) {
		this.input = input;
		this.sd = sd;
	}
	
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
