import java.io.FileNotFoundException;
import java.util.Vector;


public interface IModule {
	public abstract boolean loadFromFile(String filename) throws FileNotFoundException;
	public abstract boolean loadFromString(Vector<String> content);
	public abstract boolean executeStep(Tape t);
	public abstract String getCurrentState();	
}
