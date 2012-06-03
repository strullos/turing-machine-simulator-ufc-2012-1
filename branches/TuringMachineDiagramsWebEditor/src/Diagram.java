import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;


public class Diagram extends Module {

	private HashMap<String, Module> m_loaded_modules; //Maps from module name to module object
	private HashMap<String, String> m_modules_path; //Maps from module name to module path;
	private HashMap<String, DiagramRule> m_modules_rules; //Maps from module to rule;
	private HashMap<String, String> m_variables_table; //Stores the name and value of all variables
	private String m_initial_module;
	private String m_current_module;
	
	Diagram(){
		m_loaded_modules = new HashMap<String, Module>();
		m_modules_path = new HashMap<String, String>();
		m_modules_rules = new HashMap<String, DiagramRule>();
		m_variables_table = new HashMap<String,String>();
		m_initial_module = "";
		m_current_module = "";
	}
	@Override
	public boolean executeStep(Tape t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processRule(String line) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processHeader(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String module_type = tokens.nextToken();	
			if(module_type.equals("machine")){
				return true;
			}
			if(module_type.equals("diagrama")){
				return true;
			}
			return false;
		}		
		return false;
	}

	@Override
	public boolean execute(Tape t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printStep(Tape t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printSummary(Tape t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean load(BufferedReader reader) throws IOException {
		String line;		
		while( (line = reader.readLine()) != null ){
			m_current_line++;			
			if(line.equals("\n") || line.equals("\r") || line.equals("\r\n") || line.isEmpty()) continue;
			if(processHeader(line)) continue;			
			if(processRule(line)) continue;
			m_log.writeLn("Failed to load diagram file while reading line " + m_current_line + ":" + line);
			return false;
		}	
		return true;
	}
	
	
	
	private class DiagramRule
	{
		boolean m_updates_variable;
		boolean m_sends_variable;
		HashMap<String, String> m_next_modules; //Maps from symbol to module;
		String m_sent_variable;
		String m_updated_variable;		
	}

}
