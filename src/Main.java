import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tape t = new Tape("aabb");
		Machine m = new Machine();
		Vector<String> content = new Vector<String>();
		content.add("q0 a q1 >");
		m.loadFromString(content);
		System.out.println(t.toString());
		t.moveHeadRight();
		m.executeStep(t);
		System.out.println(t.toString());

	}

}
