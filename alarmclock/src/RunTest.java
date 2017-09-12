
public class RunTest {

	public static void main(String[] args) {
		Thread t = new Thread(new Test());
		t.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			try {				
				System.out.println("in main");
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
