package lift;

public class LiftHandler extends Thread{
	private LiftData ld;
	
	public LiftHandler(LiftData ld) {
		this.ld = ld;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				ld.moveLift();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Temporary solution
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
	}

}
