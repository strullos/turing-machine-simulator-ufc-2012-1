import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Machine extends Module {
	
	Machine(){ 
		m_rules = new HashMap<String, MachineRule>();
		m_initial_state = "";
		m_current_state = m_initial_state;
		m_variable = "";
		m_variable_value = "";
		m_uses_variable = false;
	}
	
	@Override
	public boolean load(BufferedReader reader) throws IOException {
		String line;		
		while( (line = reader.readLine()) != null ){
			m_current_line++;			
			if(line.equals("\n") || line.equals("\r") || line.equals("\r\n") || line.isEmpty()) continue;
			if(processHeader(line)) continue;
			if(processVarDeclaration(line)) continue;
			if(processRule(line)) continue;
			m_log.writeLn("Failed to load machine file while reading line " + m_current_line + ":" + line);
			return false;
		}	
		return true;
	}	
	
	@Override
	public boolean processHeader(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 2){
			m_initial_state = tokens.nextToken();	
			if(m_initial_state.equals("var")){
				m_initial_state = "";
				return false;
			}
			m_current_state = m_initial_state;
			m_max_steps = Integer.parseInt(tokens.nextToken().toString());
			return true;
		}		
		return false;
	}	
	
	public boolean processVarDeclaration(String line){
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 2){
			if(tokens.nextToken().equals("var")){
				m_variable = tokens.nextToken();
				m_uses_variable = true;
				return true;
			}			
		}
		return false;
	}
	
	@Override
	public boolean processRule(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 4){
			String initial_state = tokens.nextToken();
			String symbol = tokens.nextToken();
			String final_state = tokens.nextToken();
			String action = tokens.nextToken();
			
			if(m_rules.containsKey(initial_state)){
				MachineRule rule = m_rules.get(initial_state);
				if(rule.hasRule(symbol) || rule.hasRule("*")){
					m_log.writeLn("Non-deterministic rule detected on line " + m_current_line);
					m_log.writeLn("Rule: " + initial_state + " " + symbol + " " + final_state + " " + action);
					m_log.writeLn("Conflicts with previously added Rule: " + initial_state + " " + 
					symbol + " " + rule.m_next_states.get(symbol) + " " + rule.m_actions.get(symbol));
					return false;					
				}
				rule.addFinalState(symbol, final_state);
				rule.addAction(symbol, action);
				return true;
			}
			MachineRule rule = new MachineRule(symbol,final_state, action);
			m_rules.put(initial_state, rule);
			return true;
		}
		return false;
	}
	

	@Override
	public boolean executeStep(Tape t) {	
		if(m_steps == m_max_steps){
			m_log.writeLn("Maximum number of steps reached. Aborting execution.");
			return false;
		}		
		if(m_rules.containsKey(m_current_state)){
			MachineRule rule = m_rules.get(m_current_state);
			m_current_state = rule.getNextState(t.readCurrentSymbol());
			m_steps++;
			return rule.applyAction(t);
		}
		return false;
	}	
	
	@Override
	public boolean execute(Tape t){
		m_log.writeLn("Initial state: " + m_initial_state);
		m_log.writeLn("Tape initial configuration: " + t.toString());
		while(executeStep(t)){
			printStep(t);			
		}
		printSummary(t);
		return true;		
	}
	
	@Override
	public void printStep(Tape t) {				
			m_log.writeLn(Integer.toString(m_steps) + "." + m_current_state + ": " + t.toString());
	}	
	
	@Override
	public void printSummary(Tape t) {
		m_log.writeLn("Finished executing in " + m_steps + " steps.");
		m_log.writeLn("Tape final configuration is: " + t.toString());
	}

	@Override
	public String getCurrentState() {
		return m_current_state;
	}	
	
	public void setVariableValue(String value){
		m_variable_value = value;
	}
	
	private HashMap<String, MachineRule> m_rules; //Maps from state to rule
	private String m_initial_state;
	private String m_current_state;
	private String m_variable;
	private String m_variable_value;
	private boolean m_uses_variable;
	
	private class MachineRule
	{
		private HashMap<String,String> m_next_states; //Maps from symbol to final state 
		private HashMap<String,String> m_actions;	  //Maps from symbol to action
		
		public MachineRule(String symbol, String final_state, String action)
		{
			m_next_states = new HashMap<String,String>();
			m_actions = new HashMap<String,String>();
			addFinalState(symbol, final_state);
			addAction(symbol, action);			
		}
		
		public void addFinalState(String symbol, String final_state){			
			m_next_states.put(symbol, final_state);
		}
		
		public void addAction(String symbol, String action){
			m_actions.put(symbol, action);
		}
		
		public boolean hasRule(String symbol){
			return m_actions.containsKey(symbol);
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
			switch(action){
			case '>':
				tape.moveHeadRight();
				return true;			
			case '<':
				tape.moveHeadLeft();
				return true;		
			default:
				if(m_uses_variable){
					if(action == m_variable.charAt(0)){
						action = m_variable_value.charAt(0);
					}
				}
				tape.writeSymbol(action);
				return true;
			}			
		}	
	
	}	
}
