import java.io.FileNotFoundException;

import core.Module;
import core.ModuleFactory;
import core.SystemOutputLog;
import core.Tape;


public class SimulatorConsole {
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
			if(m == null){
				System.out.println("Failed to load file.");
				System.out.println(ModuleFactory.error_);
				return;
			}
			m.logs_.AddLog(new SystemOutputLog());
			Tape t = new Tape(args[1]);
			m.execute(t);
			m = null;
			t = null;
		}
	}
}
