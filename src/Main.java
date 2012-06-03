import java.io.IOException;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		Tape t = new Tape("aabb");
		Machine m = new Machine();		
		try {
			m.loadFromString(
					"q0 1000\n" +
					"q0 * q1 >\n" +
					"q1 a q2 >\n" +
					"q2 a q3 b");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		m.execute(t);
	}

}
