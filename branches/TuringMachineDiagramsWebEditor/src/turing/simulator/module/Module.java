package turing.simulator.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import turing.simulator.log.LogsList;
import turing.simulator.tape.Tape;
import utils.StringFileReader;


public abstract class Module {
	public final LogsList logs_ = new LogsList();
	protected int m_current_line = 0;
	protected int m_steps = 0;
	protected int m_max_steps;
	protected String m_module_name;
	protected String m_module_parent;
	protected String m_module_path;
	protected static String m_error = new String();
	protected boolean m_loaded;
	protected String m_load_path;
	
	public static int test_steps = 0;
	
	public void ClearLogsList()
	{
		logs_.Clear();
	}
	
	public  void setLoadPath(String load_path)
	{
		m_load_path = load_path;
	}	
	
	public boolean loadFromFile(String filename) throws FileNotFoundException
	{
		StringFileReader file_reader = new StringFileReader();
		String content = file_reader.ReadFile(filename);
		if(content ==  "Unable to read file"){
			return false;
		}
		logs_.WriteLn("Loading file: " + filename);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			setModulePath(filename);
			return load(reader);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}
	protected abstract boolean load(BufferedReader reader) throws IOException;
	
	public boolean loadFromString(String text) throws IOException
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
	
	protected String getModuleFileName(){
		File module_file = new File(m_module_path);
		return module_file.getName();
	}
	
	protected void setInitialStep(int initial_step){
		m_steps = initial_step;
	}
	public int getSteps(){
		return m_steps;
	}
	
	protected abstract String getFinalState();
	public abstract void reset();
	
	public int getLine() {
		return m_current_line;
	}
	
	public abstract ArrayList<String> getDependencies();
}
