import java.util.concurrent.Semaphore;

import se.lth.cs.realtime.semaphore.MutexSem;

class Test extends Thread{
	MutexSem mutEx;
	int time;
	
	
	public Test(){
		mutEx = new MutexSem();
		time = 500;
		
	}
	
	public void start() {
		Thread t = new Thread(new TestThread());
		Thread t2 = new Thread(new TestSecondThread());
		
		t.start();
		t2.start();
	}

	class TestThread implements Runnable{
		public void run() {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			while(true) {
//				try {				
//					System.out.println("in thread one");
//					Thread.sleep(1000);
//				}catch(InterruptedException e) {
//					System.out.println(e.getMessage());
//				}
				mutEx.take();
				System.out.println(1);
				holdUp();
				System.out.println(2);
				holdUp();
				System.out.println(3);
				holdUp();
				System.out.println(4);
				holdUp();
				System.out.println(5);
				holdUp();
				mutEx.give();
			}
		}
	}
	
	class TestSecondThread implements Runnable{
		public void run() {
			while(true) {
//				try {				
//					System.out.println("in thread two");
//					Thread.sleep(1000);
//				}catch(InterruptedException e) {
//					System.out.println(e.getMessage());
//				}
				mutEx.take();
				System.out.println('a');
				holdUp();
				System.out.println('b');
				holdUp();
				System.out.println('c');
				holdUp();
				System.out.println('d');
				holdUp();
				System.out.println('e');
				holdUp();
				mutEx.give();
			}
		}
	}	
	
	void holdUp() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Test test = new Test();
		test.start();
	}
	
}