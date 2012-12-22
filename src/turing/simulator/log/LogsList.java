package turing.simulator.log;

import java.util.ArrayList;
import java.util.Iterator;

public class LogsList {
	private ArrayList<Log> logs_;
	public LogsList(){
		logs_ = new ArrayList<Log>();
	}
	
	public void AddLog(Log log_writer){
		logs_.add(log_writer);
	}
	
	public void RemoveLog(int index){
		logs_.remove(index);
	}
	
	public void Write(String text){
		Iterator<Log> logs_it = logs_.iterator();
		while(logs_it.hasNext()){
			Log l = logs_it.next();
			l.Write(text);
		}
	}
	
	public void WriteLn(String text){
		Iterator<Log> logs_it = logs_.iterator();
		while(logs_it.hasNext()){
			Log l = logs_it.next();
			l.WriteLn(text);
		}
	}
	
	public void Clear(){
		Iterator<Log> logs_it = logs_.iterator();
		while(logs_it.hasNext()){
			Log l = logs_it.next();
			l.Clear();
		}
	}
}
