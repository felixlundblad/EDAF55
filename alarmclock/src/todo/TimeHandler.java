package todo;

class TimeHandler implements Runnable {
	private SharedData sd;
	
	public TimeHandler(SharedData sd){
		this.sd = sd;
	}
	
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(1000 - System.currentTimeMillis()%1000);
			} catch (InterruptedException e) {
				break;
			}
			sd.incTime();
		}
	}
}