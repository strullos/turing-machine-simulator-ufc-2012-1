import java.util.Vector;


public class Log {
	private Vector<String> content;
	
	Log(){
		content = new Vector<String>();		
	}
	
	public void write(String line){
		content.add(line);
		
	}
	
	public void writeLn(String line){
		content.add(line + "\n");
	}
	
	public void clear(){
		content.clear();
	}
}
