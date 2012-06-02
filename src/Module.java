import java.io.FileNotFoundException;
import java.util.Vector;


public abstract class Module {
	public abstract boolean loadFromFile(String filename) throws FileNotFoundException;
	public abstract boolean load(Vector<String> content);
	public abstract boolean loadFromString(String content);
	public abstract void printStep(Tape t);
	public abstract void printSummary(Tape t);
	
	public abstract boolean executeStep(Tape t);
	public abstract boolean execute(Tape t);
	public abstract String getCurrentState();	
	public abstract boolean processRule(String line);
	public abstract boolean processHeader(String line);
	
	protected final Log m_log = new Log();
	protected int m_current_line = 0;
	protected int m_steps = 1;
	protected int m_max_steps;
}
