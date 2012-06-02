import java.io.FileNotFoundException;
import java.util.Vector;


public interface Module {
	public abstract boolean loadFromFile(String filename) throws FileNotFoundException;
	public abstract boolean loadFromString(Vector<String> content);
	public abstract boolean executeStep(Tape t);
	public abstract String getCurrentState();	
	public abstract boolean processRule(String line);
	public abstract boolean processHeader(String line);
	
	public final Log m_log = new Log();
	public int m_current_line = 1;
}
