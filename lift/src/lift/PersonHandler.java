package lift;

public class PersonHandler extends Thread {
	private static int LEVELS = 7;

	private int entryLevel;
	private int exitLevel;

	private LiftData ld;

	public PersonHandler(LiftData ld, LiftView lv) {
		this.ld = ld;
		entryLevel = (int) ( LEVELS * Math.random());
		while(entryLevel == (exitLevel = (int) ( LEVELS * Math.random())));
	}


	@Override
	public void run() {
		while(true) {
			try {
				double a = 46000 * Math.random();
				//System.out.println((int)a/1000 + " seconds until arrival");
				sleep((int) (a));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			entryLevel = (int) ( LEVELS * Math.random());
			while(entryLevel == (exitLevel = (int) ( LEVELS * Math.random())));	
			try {
				ld.useLift(entryLevel, exitLevel);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
