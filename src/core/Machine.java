package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Machine extends Module {	
	private HashMap<String, MachineRule> m_rules; //Maps from state to rule
	private String m_initial_state;
	private String m_current_state;
	private HashMap<String, String> m_variables;

	public Machine(){
		m_rules = new HashMap<String, MachineRule>();
		m_initial_state = "";
		m_current_state = m_initial_state;
		m_module_name = "";
		m_variables = new HashMap<String, String>();
	}	


	@Override
	protected boolean load(BufferedReader reader) {
		String line;
		m_steps = 0;	
		try {
			while( (line = reader.readLine()) != null ){
				m_current_line++;			
				if(line.equals("\n") || line.equals("\r") || line.equals("\r\n") || line.isEmpty() || line.startsWith("//")) continue;
				if(processHeader(line)) continue;
				if(processVarDeclaration(line)) continue;
				if(processRule(line)) continue;
				logs_.WriteLn("Failed to load machine " + m_module_name + " file while reading line " + m_current_line + ":" + line);
				m_error = "Failed to load machine " + m_module_name + " file while reading line " + m_current_line + ":" + line;
				return false;
			}
		} catch (IOException e) {
			logs_.WriteLn("Can't read stream from input");
			e.printStackTrace();
		}		
		return true;
	}	

	@Override
	public boolean processHeader(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		int tokens_count = tokens.countTokens();
		String first_token = tokens.nextToken();		
		if((tokens_count == 2) && (!first_token.equals("var"))){
			m_initial_state = first_token;	
			m_current_state = m_initial_state;
			m_max_steps = Integer.parseInt(tokens.nextToken().toString());
			return true;
		}		
		return false;
	}	

	public boolean processVarDeclaration(String line){
		StringTokenizer tokens = new StringTokenizer(line);
		int tokens_count = tokens.countTokens();
		String first_token = tokens.nextToken();
		if(tokens_count == 2 && first_token.equals("var")){
			String var = tokens.nextToken();
			m_variables.put(var, "");
			return true;
		}
		return false;
	}

	@Override
	public boolean processRule(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 4){
			String initial_state = tokens.nextToken();
			String symbol = tokens.nextToken();
			String variable = "";
			if(symbol.contains("=") && symbol.length() >= 3){
				variable = symbol.substring(0, symbol.indexOf("="));
				symbol = symbol.substring(symbol.indexOf("=")+1);
			}
			String final_state = tokens.nextToken();
			String action = tokens.nextToken();

			if(m_rules.containsKey(initial_state)){
				MachineRule rule = m_rules.get(initial_state);
				if(rule.hasRule(symbol) || rule.hasRule("*")){
					logs_.WriteLn("Non-deterministic rule detected on line " + m_current_line);
					logs_.WriteLn("Rule: " + initial_state + " " + symbol + " " + final_state + " " + action);
					logs_.WriteLn("Conflicts with previously added Rule: " + initial_state + " " + 
							symbol + " " + rule.m_next_states.get(symbol) + " " + rule.m_actions.get(symbol));
					return false;					
				}
				rule.addTransition(symbol, final_state, action, variable);
				return true;
			}
			MachineRule rule = new MachineRule(symbol,final_state, action, variable, m_variables);
			m_rules.put(initial_state, rule);
			return true;
		}
		return false;
	}

	@Override
	public boolean execute(Tape t){
		Module.test_steps = 1;
		logs_.WriteLn("Initial state: " + m_initial_state);
		logs_.WriteLn("Tape initial configuration: " + t.toString());
		while(executeStep(t)){
			printStep(t);	
			Module.test_steps++;	
		}
		printSummary(t);
		return true;		
	}

	@Override
	public boolean executeStep(Tape t) {	
		if(m_steps == m_max_steps){
			logs_.WriteLn("Maximum number of steps reached. Aborting execution.");
			return false;
		}		
		if(m_rules.containsKey(m_current_state)){
			MachineRule rule = m_rules.get(m_current_state);
			String m_next_state = rule.getNextState(t.readCurrentSymbol());
			if(m_next_state != null){
				m_current_state = m_next_state;
				m_steps++;
				return rule.applyAction(t);
			}
		}
		return false;
	}	

	@Override
	public void printStep(Tape t) {	
		String format_string = "%-4s %-3s %s";
		String step_count = Integer.toString(Module.test_steps) + ".";
		String step_info = m_current_state;
		String step_tape = t.toString();
		String formatted_string = String.format(format_string,step_count,step_info,step_tape);
		logs_.WriteLn(formatted_string);			
	}	

	@Override
	public void printSummary(Tape t) {
		logs_.WriteLn("Finished executing in " + m_steps + " steps.");
		logs_.WriteLn("Tape final configuration is: " + t.toString());
	}

	@Override
	public String getCurrentState() {
		return m_current_state;
	}	

	@Override
	public String getFinalState() {
		return "<" + m_module_name + "," + m_current_state + ">";
	}	

	@Override
	public void reset(){
		m_current_state = m_initial_state;
		m_steps = 0;
	}

	public void setVariableValue(String variable, String value){
		m_variables.put(variable,value);
	}	

	public ArrayList<String> getDependencies()
	{
		return new ArrayList<String>();
	}

	private class MachineRule
	{
		private HashMap<String,String> m_next_states; //Maps from symbol to final state 
		private HashMap<String,String> m_actions;	  //Maps from symbol to action
		private HashMap<String, String> m_symbol_variables; //Maps from symbol to variable, meaning that the symbol will be stored on the variable
		private HashMap<String, String> m_variables_values;

		public MachineRule(String symbol, String final_state, String action, String variable, HashMap<String,String> variables_values)
		{
			m_next_states = new HashMap<String,String>();
			m_actions = new HashMap<String,String>();
			m_symbol_variables = new HashMap<String, String>();
			m_variables_values = variables_values;
			addTransition(symbol, final_state, action, variable);
		}

		public void addTransition(String symbol, String final_state, String action, String variable){
			m_next_states.put(symbol, final_state);
			m_actions.put(symbol, action);
			if(!variable.isEmpty()){
				m_symbol_variables.put(symbol, variable);
			}
		}


		public boolean hasRule(String symbol){
			return (m_actions.containsKey(symbol) || m_next_states.containsKey("*"));
		}

		public String getNextState(String symbol){
			if(m_next_states.containsKey("*")){
				return m_next_states.get("*");
			}else{
				return m_next_states.get(symbol);			
			}			
		}

		public boolean applyAction(Tape tape)
		{	
			String symbol = tape.readCurrentSymbol();
			char action;
			if(m_actions.containsKey(symbol)){										
				action = m_actions.get(symbol).charAt(0);				
			}else{
				if(m_actions.containsKey("*")){
					action = m_actions.get("*").charAt(0);		
				}else{
					return false;
				}
			}
			if(m_symbol_variables.containsKey(symbol)){
				String var = m_symbol_variables.get(symbol);
				m_variables_values.put(var, symbol);
			}
			switch(action){
			case '>':
				tape.moveHeadRight();
				return true;			
			case '<':
				tape.moveHeadLeft();
				return true;		
			default:
				if(!m_variables_values.isEmpty()){
					String action_str = "" + action;
					if(m_variables_values.containsKey(action_str)){
						action = m_variables_values.get(action_str).charAt(0);
					}
				}
				tape.writeSymbol(action);
				return true;
			}			
		}	
	}

}
