import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class Machine implements IModule {
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
		
		return false;
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
	
	
	
	
	private class MachineRule
	{
		public char m_symbol;
		public String m_final_state;
		public char m_action;
		
		public MachineRule(char symbol, String final_state, char action)
		{
			m_symbol = symbol;
			m_final_state = final_state;
			m_action = action;
		}
		
		public boolean applyAction(Tape tape)
		{
			switch(m_action){
			case '>':
				tape.moveHeadRight();
				return true;			
			case '<':
				tape.moveHeadLeft();
				return true;		
			default:
				tape.writeSymbol(m_action);
				return true;
			}			
		}
	}	
}
