package turing.simulator.module;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ModuleFactory {
	public static Module loadModule(String path) throws FileNotFoundException {
		Module ret = null;
		
		if( path.endsWith(".mt") )
			ret = new Machine();
		else if( path.endsWith(".dt") )
			ret = new Diagram();
		
		if( ret != null && !ret.loadFromFile(path) )
			ret = null;
			
		return ret;
	}
	
	public static Module loadModuleFromString(String path, String content) throws IOException {
		Module ret = null;
		
		if( path.endsWith(".mt") )
			ret = new Machine();
		else if( path.endsWith(".dt") )
			ret = new Diagram();
		
		if( ret != null && !ret.loadFromString(content) )
			ret = null;
			
		return ret;
	}
}
