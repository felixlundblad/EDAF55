package lift;

import java.util.LinkedList;

public class LiftSimulator {

	public static void main(String[] args) {
		LinkedList<PersonHandler> persons = new LinkedList<PersonHandler>();
		LiftView lv = new LiftView();
		LiftData ld = new LiftData(lv);
		LiftHandler lh = new LiftHandler(ld);

		int size = 10;

		for (int i = 0; i < size; i++) {
			PersonHandler p = new PersonHandler(ld, lv);
			p.start();
			persons.add(p); 
		}

		lh.start();
		
	}

}
