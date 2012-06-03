import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;


public abstract class Module {
	protected boolean loadFromFile(String filename) throws FileNotFoundException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		try {
			return load(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	public abstract boolean load(BufferedReader reader) throws IOException;
	protected boolean loadFromString(String text) throws IOException
	{
		BufferedReader reader = new BufferedReader(new StringReader(text));	
		return load(reader);	
		
	}
	public abstract void printStep(Tape t);
	public abstract void printSummary(Tape t);
	
	public abstract boolean executeStep(Tape t);
	public abstract boolean execute(Tape t);
	public abstract String getCurrentState();	
	public abstract boolean processRule(String line);
	public abstract boolean processHeader(String line);
	
	protected final Log m_log = new Log();
	protected int m_current_line = 0;
	protected int m_steps = 0;
	protected int m_max_steps;
}
