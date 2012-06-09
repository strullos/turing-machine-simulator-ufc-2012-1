package turing.simulator.module;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import turing.simulator.log.Log;
import turing.simulator.tape.Tape;


public abstract class Module {
	protected final Log m_log = new Log();
	protected int m_current_line = 0;
	protected int m_steps = 0;
	protected int m_max_steps;
	protected String m_module_name;
	protected String m_module_parent;
	protected String m_module_path;
	protected boolean m_loaded;
	
	public static int test_steps = 0;
	
	protected boolean loadFromFile(String filename) throws FileNotFoundException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		try {
			setModulePath(filename);
			return load(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	protected abstract boolean load(BufferedReader reader) throws IOException;
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
	protected abstract boolean processRule(String line);
	protected abstract boolean processHeader(String line);
	protected void setModuleName(String name) {
		m_module_name = name;
	}
	protected void setModulePath(String path){
		m_module_path = path;
	}
	protected String getModuleName(){
		return m_module_name;
	}
	
	protected String getModulePath(){
		return m_module_path;
	}
	
	protected void setInitialStep(int initial_step){
		m_steps = initial_step;
	}
	public int getSteps(){
		return m_steps;
	}
	
	protected abstract String getFinalState();
	
	public int getLine() {
		return m_current_line;
	}
}
