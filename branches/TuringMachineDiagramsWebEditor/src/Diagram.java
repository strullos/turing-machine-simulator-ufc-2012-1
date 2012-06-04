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
		m_module_name = "";
		m_loaded = false;
	}
	
	@Override
	public String getCurrentState() {
		return m_module_name;
	}

	@Override
	public boolean processRule(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String initial_module = tokens.nextToken();
			if(m_initial_module.isEmpty()){
				m_initial_module = initial_module;
			}
			if(initial_module.startsWith("%")){
				initial_module = initial_module.substring(1);
				m_initial_module = initial_module;
			}
			
			String symbols = tokens.nextToken();
			String next_module = tokens.nextToken();
			if( !(symbols.startsWith("[") && symbols.endsWith("]")) ){
				m_log.writeLn("Error reading rule's symbols: missing '[' and/or ']' ");
				return false;
			}
			symbols = symbols.substring(1, symbols.length()-1);
			boolean sets_variable = false;
			String set_variable = "";
			if(symbols.contains("=")){
				int pos = symbols.indexOf("=");				
				sets_variable = true;
				//If the formatting is right, the variable should be the 
				//first character after the "["
				set_variable = symbols.substring(0, pos); 
				symbols = symbols.substring(pos+1);
			}	
			String sent_variable = "";
			boolean sends_variable = false;
			if(next_module.contains("(")){
				if(next_module.contains(")")){
					sends_variable = true;
					int pos = next_module.indexOf("(");
					int pos2 = next_module.indexOf(")");
					sent_variable = next_module.substring(pos+1, pos2);
					next_module = next_module.substring(0,pos);
				}else{
					m_log.writeLn("Error reading next module: missing ')' ");
					return false;
				}
			}
			if(next_module.contains(")")){
				m_log.writeLn("Error reading next module: missing '(' ");
				return false;
			}
			
			
			DiagramRule rule;
			if(m_modules_rules.containsKey(initial_module)){
				rule = m_modules_rules.get(initial_module);				
			}else{
				rule = new DiagramRule();
				m_modules_rules.put(initial_module, rule);
			}
			
			StringTokenizer symbols_tokens = new StringTokenizer(symbols,",");
			String current_symbol;
			while(symbols_tokens.hasMoreTokens()){
				current_symbol = symbols_tokens.nextToken();
				if(rule.hasTransition(current_symbol)){
					m_log.writeLn("Non-determinism detected: Current rule conflicts with previously added rule. Aborting loading.");
					return false;
				}
				rule.addTransition(current_symbol, next_module, 
						sets_variable, sends_variable, 
						set_variable, sent_variable);
			}			
			return true;
		}
		return false;
	}

	@Override
	public boolean processHeader(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String module_type = tokens.nextToken();
			String module_name = tokens.nextToken();
			String module_path = tokens.nextToken();
			if(m_loaded_modules.containsKey(module_name)){
				m_log.writeLn("Duplicated module name detected on line: " + m_current_line);
				m_log.writeLn("Aborting load.");
				return false;				
			}
			if(module_type.equals("machine")){
				Module machine_module = new Machine();
				machine_module.setModuleName(module_name);
				try {
					machine_module.loadFromFile(module_path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}
				m_loaded_modules.put(module_name, machine_module);
				m_modules_path.put(module_name, module_path);
				m_log.writeLn("Module " + module_name + " loaded succesfully.");
				return true;
			}
			if(module_type.equals("diagram")){
				Diagram diagram_module = new Diagram();
				diagram_module.setModuleName(module_name);
				try {
					diagram_module.loadFromFile(module_path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}
				m_loaded_modules.put(module_name, diagram_module);
				m_modules_path.put(module_name, module_path);
				m_log.writeLn("Module " + module_name + " loaded succesfully.");
				return true;
			}
			return false;
		}		
		return false;
	}

	@Override
	public boolean execute(Tape t) {	
		boolean executing = true;
		if(m_loaded){
			printStep(t);
			while(executing){
				Module current_module = m_loaded_modules.get(m_current_module);		
				current_module.setInitialStep(m_steps+1);
				m_steps++;
				while(current_module.executeStep(t)){
					printStep(t);
					m_steps++;
				}		
			  m_steps = current_module.getSteps();
			  if(m_modules_rules.containsKey(m_current_module)){
				  if(m_modules_rules.get(m_current_module).hasTransition(t.readCurrentSymbol())){
					  m_current_module = m_modules_rules.get(m_current_module).getNexModule(t.readCurrentSymbol());
					 
				  }else{
					  executing = false;
				  }
			  }else{
				  executing = false;
			  }
			
			}
		
			printSummary(t);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean executeStep(Tape t) {
		if(!m_loaded_modules.containsKey(m_current_module)){
			return false;
		}
	    Module current_module = m_loaded_modules.get(m_current_module);
	    printStep(t);
	    while(current_module.executeStep(t)){	    	
	    	printStep(t);
	    	m_steps++;
	    }
	    if(m_modules_rules.get(m_current_module).hasTransition(t.readCurrentSymbol())){
	    	m_current_module = m_modules_rules.get(m_current_module).getNexModule(t.readCurrentSymbol());
	    	return true;	
	    }
	    return false;		
	}

	@Override
	public void printStep(Tape t) {
		Module current_module = m_loaded_modules.get(m_current_module);
		m_log.writeLn(Integer.toString(m_steps) + ".<"+ m_module_name + "." + m_current_module + "," +  current_module.getCurrentState() + ">: " + t.toString());
	}

	@Override
	public void printSummary(Tape t) {
		m_log.writeLn("Finished executing in " + (m_steps-1) + " steps.");
		m_log.writeLn("Tape final configuration is: " + t.toString());
		m_log.writeLn("Stopped on module: " + m_current_module);
		m_log.writeLn("With state: " + m_loaded_modules.get(m_current_module).getCurrentState());
	}

	@Override
	public boolean load(BufferedReader reader) throws IOException {
		String line;		
		while( (line = reader.readLine()) != null ){
			m_current_line++;			
			if(line.equals("\n") || line.equals("\r") || line.equals("\r\n") || line.isEmpty()) continue;
			if(processHeader(line)) continue;			
			if(processRule(line)) continue;
			m_log.writeLn("Failed to load diagram " + m_module_name + " file while reading line " + m_current_line + ": " + line);
			return false;
		}	
		m_current_module = m_initial_module;
		m_loaded = true;
		return true;
	}
	
	
	
	private class DiagramRule
	{	
		HashMap<String, String> m_next_modules; //Maps from symbol to module;
		HashMap<String, String> m_set_variables; //Maps from symbol to the variable that is set
		HashMap<String, String> m_sent_variables; //Maps from symbol to the variable that is sent
		
		DiagramRule(){
			m_next_modules = new HashMap<String, String>();
			m_set_variables = new HashMap<String, String>();
			m_sent_variables = new HashMap<String, String>();
		}
		
		public void addTransition(String symbol, String next_module, 
				boolean sets_variable, boolean sends_variable, 
				String set_variable, String sent_variable){
			m_next_modules.put(symbol, next_module);
			if(sets_variable){
				m_set_variables.put(symbol,set_variable);
			}
			if(sends_variable){
				m_sent_variables.put(symbol, sent_variable);
			}
		}
		//Checks if the rule already has a transition for the given symbol
		public boolean hasTransition(String symbol){
			return m_next_modules.containsKey(symbol);
		}
		
		public boolean setsVariable(String symbol){
			return m_set_variables.containsKey(symbol);
		}
		
		public boolean sendsVariable(String symbol){
			return m_sent_variables.containsKey(symbol);				
		}
		
		public String getNexModule(String symbol){
			return m_next_modules.get(symbol);
		}
		
		
	}

}
