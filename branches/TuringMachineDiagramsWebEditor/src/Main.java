import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		Tape t = new Tape("aabb");
		Machine m = new Machine();
		Vector<String> content = new Vector<String>();
		content.add("q0 a q1 >");
		m.loadFromString("q0 a q1 >\nq1 a q2 >");		
		t.moveHeadRight();
		m.execute(t);
	}

}
