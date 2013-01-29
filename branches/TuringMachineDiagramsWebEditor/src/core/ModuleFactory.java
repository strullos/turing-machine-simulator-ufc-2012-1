package core;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ModuleFactory {
	public static String error_ = new String("");
	
	public static Module loadModule(String path) throws FileNotFoundException {
		Module ret = null;		
		
		if( path.endsWith(".mt") )
			ret = new Machine();
		else if( path.endsWith(".dt") ){
			ret = new Diagram();
			String load_path = path.substring(0, path.lastIndexOf("/"));
			ret.setLoadPath(load_path);
		}
		
		if( ret != null && !ret.loadFromFile(path) ){
			error_ = Module.m_error;
			ret = null;
		}
			
		return ret;
	}
	
	public static Module loadModuleFromString(String path, String content) throws IOException {
		Module ret = null;
		
		if(content.isEmpty()){
			error_ = "Empty module.";
			return null;
		}
		
		if( path.endsWith(".mt") )
			ret = new Machine();
		else if( path.endsWith(".dt") )
			ret = new Diagram();	
					
		if( ret != null && !ret.loadFromString(content) ){
			error_ = Module.m_error;
			ret = null;		
		}
		
		return ret;
	}
}
