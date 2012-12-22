import java.io.FileNotFoundException;

import turing.simulator.log.SystemOutputLog;
import turing.simulator.module.Module;
import turing.simulator.module.ModuleFactory;
import turing.simulator.tape.Tape;

public class Main {
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {		
		System.out.println("Turing Machines Editor - Web Applet - Comp UFC 2012.1");
		if(args.length != 2){
			System.out.println("Usage: -machine.mt (or diagram.dt) -tape");
		}else{		
			Module m = ModuleFactory.loadModule(args[0]);
			m.logs_.AddLog(new SystemOutputLog());
			Tape t = new Tape(args[1]);
			m.execute(t);
			m = null;
			t = null;
		}
	}
}
