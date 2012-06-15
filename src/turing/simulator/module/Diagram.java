package turing.simulator.module;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import turing.simulator.tape.Tape;



public class Diagram extends Module {

	private HashMap<String, Module> m_loaded_modules; //Maps from module name to module object
	private HashMap<String, String> m_modules_path; //Maps from module name to module path;
	private HashMap<String, DiagramRule> m_modules_rules; //Maps from module to rule;
	private HashMap<String, String> m_variables_table; //Stores the name and value of all variables
	private String m_initial_module;
	private String m_current_module;
	
	public Diagram(){
		m_loaded_modules = new HashMap<String, Module>();
		m_modules_path = new HashMap<String, String>();
		m_modules_rules = new HashMap<String, DiagramRule>();
		m_variables_table = new HashMap<String,String>();
		m_initial_module = "";
		m_current_module = "";
		m_module_name = "";
		m_module_path = "this";
		m_loaded = false;
	}
	
	@Override
	protected boolean load(BufferedReader reader) throws IOException {
		String line;		
		while( (line = reader.readLine()) != null ){
			m_current_line++;
			if(processRule(line)) continue;
			if(processHeader(line)) continue;
			if( isEmptyLine(line) ) continue;
			
			if(m_module_name.isEmpty()){
				m_log.writeLn("An error occurred loading diagram file " + m_module_name + "(" + m_module_path + ")" + ", while processing an instruction on line " + m_current_line + ": " + line);
			}else{
				m_log.writeLn(m_module_name + ": " + "An error occurred loading diagram file " + m_module_name + "(" + m_module_path + ")" + ", while processing an instruction on line " + m_current_line + ": " + line);
			}
			return false;
		}	
		m_current_module = m_initial_module;
		m_loaded = true;
		return true;
	}
	
	@Override
	public void reset(){
		m_current_module = m_initial_module;
	}
	
	@Override
	public String getCurrentState() {
		return m_current_module;
	}
	
	@Override
	protected String getFinalState() {		
		String final_state = "";
		if(m_loaded_modules.containsKey(m_current_module)){
			Module last_module = m_loaded_modules.get(m_current_module);
			if(last_module instanceof Diagram){
			
				return last_module.getFinalState();
			}
			if(last_module instanceof Machine){
				return last_module.getFinalState();
			}
		}else{
			return m_current_module;
		}
		return final_state;
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
				if(m_module_name.isEmpty()){
					machine_module.setModuleName(module_name);
				}else{
					machine_module.setModuleName(m_module_name + "->" + module_name);
				}			
				try {
					if(!machine_module.loadFromFile(module_path)){
						return false;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}
				m_loaded_modules.put(module_name, machine_module);
				m_modules_path.put(module_name, module_path);
				m_log.writeLn("Machine " + machine_module.getModuleName() + "(" + machine_module.getModulePath() + ")" + " loaded succesfully.");
				return true;
			}
			if(module_type.equals("diagram")){
				Diagram diagram_module = new Diagram();
				if(m_module_name.isEmpty()){
					diagram_module.setModuleName(module_name);
				}else{
					diagram_module.setModuleName(m_module_name + "." + module_name);
				}				
				try {
					if(!diagram_module.loadFromFile(module_path)){
						return false;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}
				m_loaded_modules.put(module_name, diagram_module);
				m_modules_path.put(module_name, module_path);
				m_log.writeLn("Diagram " + diagram_module.getModuleName() + "(" + diagram_module.getModulePath() + ")" + " loaded succesfully.");
				return true;
			}
			return false;
		}		
		return false;
	}

	@Override
	public boolean processRule(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String initial_module = tokens.nextToken();
			if(initial_module.equals("machine") || initial_module.equals("diagram")){
				return false;
			}
			
			
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
				m_variables_table.put(set_variable, "");
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
	public boolean execute(Tape t) {	
		boolean executing = true;
		if(m_loaded){
			//printStep(t);
			//m_steps++;
			while(executing){
				if(!m_loaded_modules.containsKey(m_current_module)){
					break;
				}
				Module current_module = m_loaded_modules.get(m_current_module);		
				current_module.setInitialStep(m_steps);		
				current_module.reset();
				if((current_module instanceof Diagram)){					    						
					   m_log.writeLn("Diagram " + current_module.getModuleName() + ":");
				}
				printStep(t);
				while(current_module.executeStep(t)){
				   if(!(current_module instanceof Diagram)){
				    	printStep(t);
				    	m_steps++;	    
				    }
				}		
			  m_steps = current_module.getSteps();
			  executing = false;
			  if(m_modules_rules.containsKey(m_current_module)){
				  String curr_symbol = t.readCurrentSymbol();
				  DiagramRule rule = m_modules_rules.get(m_current_module);
				  if(rule.hasTransition(curr_symbol)){
					  m_current_module = rule.getNexModule(curr_symbol);	
					  if(!m_loaded_modules.containsKey(m_current_module)){
						 printStep(t);
		  				 break;
		  			  }
					  executing = true;
					  if(rule.setsVariable(curr_symbol)){
						  String var = rule.getSetVariable(curr_symbol);
						  m_variables_table.put(var, curr_symbol);
					  }
					  if(rule.sendsVariable(curr_symbol)){
						  String var = rule.getSentVariable(curr_symbol);
						  Module module = m_loaded_modules.get(m_current_module);
						  if(module instanceof Machine){
							  Machine m = (Machine)module;
							  m.setVariableValue(m_variables_table.get(var));
						  }
					  }
				  }
			  }
			}		
			printSummary(t);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean executeStep(Tape t) {		
	    Module current_module = m_loaded_modules.get(m_current_module);
	    current_module.setInitialStep(m_steps);
	    current_module.reset();
	    if(!(current_module instanceof Diagram)){
	    	printStep(t);
	    	m_steps++;	    
	    }else{
	    	m_log.writeLn("Diagram " + current_module.getModuleName() + ": ");
	    }
	    while(current_module.executeStep(t)){	    	
	    	printStep(t);
	    	m_steps++;
	    }
	    m_steps = current_module.getSteps();
	    if(m_modules_rules.containsKey(m_current_module)){
	    	String curr_symbol = t.readCurrentSymbol();
	    	DiagramRule rule = m_modules_rules.get(m_current_module);
	    	if(rule.hasTransition(curr_symbol)){
	  	    	m_current_module =rule.getNexModule(curr_symbol);
	  	    	if(!m_loaded_modules.containsKey(m_current_module)){
	  				return false;
	  			}
	  	    	if(rule.setsVariable(curr_symbol)){
					String var = rule.getSetVariable(curr_symbol);
					m_variables_table.put(var, curr_symbol);
	  	    	}
	  	    	if(rule.sendsVariable(curr_symbol)){
					String var = rule.getSentVariable(curr_symbol);
					Module module = m_loaded_modules.get(m_current_module);
					if(module instanceof Machine){
						Machine m = (Machine)module;
						m.setVariableValue(m_variables_table.get(var));
					}
	  	    	}
	  	    	return true;		  	    	
	  	    }
	    }	  
	    return false;		
	}

	@Override
	public void printStep(Tape t) {
		Module current_module = m_loaded_modules.get(m_current_module);
		if(m_loaded_modules.containsKey(m_current_module)){
			m_log.writeLn(Integer.toString(Module.test_steps) + ".\t\t<"+ m_current_module + "," +  current_module.getCurrentState() + ">:\t\t\t\t" + t.toString());			
		}else{
			m_log.writeLn(Integer.toString(Module.test_steps) + ".\t\t<"+ m_current_module + ">:\t\t\t\t" + t.toString());
		}		
		Module.test_steps++;		
	}

	@Override
	public void printSummary(Tape t) {
		m_log.writeLn("Finished executing in " + (Module.test_steps-1) + " steps.");
		m_log.writeLn("Tape final configuration is: " + t.toString());	
		if(m_loaded_modules.containsKey(m_current_module)){
			m_log.writeLn("Stopped on module: " + m_loaded_modules.get(m_current_module).getModuleName());
			m_log.writeLn("Final state is: " + m_loaded_modules.get(m_current_module).getFinalState());
		}else{
			m_log.writeLn("Stopped on module: " + m_current_module);
		}
	}	
	
	private boolean isEmptyLine(String line) {
		return line.equals("\n") || line.equals("\r") || line.equals("\r\n") || line.isEmpty();
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
		
		public boolean setsVariable(String symbol){
			return m_set_variables.containsKey(symbol);
		}
		
		public boolean sendsVariable(String symbol){
			return m_sent_variables.containsKey(symbol);
		}
		
		public String getSetVariable(String symbol){
			return m_set_variables.get(symbol);
		}
		
		public String getSentVariable(String symbol){
			return m_sent_variables.get(symbol);
		}
		
		//Checks if the rule already has a transition for the given symbol
		public boolean hasTransition(String symbol){
			return m_next_modules.containsKey(symbol);
		}	
		
		public String getNexModule(String symbol){
			return m_next_modules.get(symbol);
		}
	}
}
