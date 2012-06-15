import java.io.FileNotFoundException;

import turing.simulator.module.Diagram;
import turing.simulator.tape.Tape;



public class Main {
	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {		
		System.out.println("Editor de Máquinas de Turing - Web - Comp UFC 2012.1");
//		DiagramSimulatorApplet app = new DiagramSimulatorApplet();
//		app.show();
		Diagram d = new Diagram();
		Tape t = new Tape("aabb");
		try {
			d.loadFromFile("test2.dt");
			d.execute(t);
			//d.printSummary(t);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
