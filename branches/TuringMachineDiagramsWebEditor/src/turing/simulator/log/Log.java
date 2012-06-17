package turing.simulator.log;
import java.util.Vector;


public class Log {
	private Vector<String> content;
	
	public Log(){
		content = new Vector<String>();		
	}
	
	public void write(String line){
		content.add(line);
		
	}
	
	public void writeLn(String line){
		content.add(line + "\n");
		System.out.println(line);
	}
	
	public void clear(){
		content.clear();
	}
	
	public String getText(){
		StringBuffer text = new StringBuffer();
		for(int i = 0; i < content.size(); i++){
			text.append(content.elementAt(i));
		}
		return text.toString();
	}
}
