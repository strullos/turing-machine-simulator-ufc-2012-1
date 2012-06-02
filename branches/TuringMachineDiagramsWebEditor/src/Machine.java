import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;


public class Machine implements Module {
	
	Machine(){ 
		m_rules = new HashMap<String, MachineRule>();
		m_initial_state = "q0";
		m_current_state = m_initial_state;
	}
	
	@Override
	public boolean loadFromFile(String filename) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Vector<String> content = new Vector<String>();
		try {
			String line = null;;
			while( (line = reader.readLine()) != null){
				content.add(line);
			}
			
			return loadFromString(content);
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loadFromString(Vector<String> content) {
		Iterator<String> it = content.iterator();
		String line;		
		while( it.hasNext() ){
			line = it.next();
			if(processHeader(line)) continue;
			if(processRule(line)) continue;
			m_log.writeLn("Failed to load machine file while reading line " + m_current_line + ":" + line);
			return false;
		}
		return false;
	}
	
	@Override
	public boolean processHeader(String line) {
		// TODO Auto-generated method stub
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
					symbol + " " + rule.m_final_states.get(symbol) + " " + rule.m_actions.get(symbol));
					return false;					
				}
				rule.addFinalState(symbol, final_state);
				rule.addAction(symbol, action);
				return true;
			}
			
			m_rules.put(initial_state, new MachineRule(symbol,final_state,action));
			return true;
		}
		return false;
	}
	

	@Override
	public boolean executeStep(Tape t) {		
		MachineRule rule = m_rules.get(m_current_state);
		if(rule != null){
			m_current_state = rule.m_final_states.get(t.readCurrentSymbol());
			return rule.applyAction(t);
		}
		return false;
	}

	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	
	private HashMap<String, MachineRule> m_rules; //Maps from state
	private String m_initial_state;
	private String m_current_state;
	
	private class MachineRule
	{
		public HashMap<String,String> m_final_states; //Maps from symbol to final state 
		public HashMap<String,String> m_actions;	  //Maps from symbol to action
		
		public MachineRule(String symbol, String final_state, String action)
		{
			m_final_states = new HashMap<String,String>();
			m_actions = new HashMap<String,String>();
			addFinalState(symbol, final_state);
			addAction(symbol, action);
		}
		
		public void addFinalState(String symbol, String final_state){			
			m_final_states.put(symbol, final_state);
		}
		
		public void addAction(String symbol, String action){
			m_actions.put(symbol, action);
		}
		
		public boolean hasRule(String symbol){
			return m_actions.containsKey(symbol);
		}
	
		
		public boolean applyAction(Tape tape)
		{	
			String symbol = tape.readCurrentSymbol();
			if(m_actions.containsKey(symbol) || m_actions.containsKey("*")){			
				char action = m_actions.get(symbol).charAt(0);
				
				switch(action){
				case '>':
					tape.moveHeadRight();
					return true;			
				case '<':
					tape.moveHeadLeft();
					return true;		
				default:
					tape.writeSymbol(action);
					return true;
				}			
			}
			return false;
		}	
	
	}	
	
	
}
