package turing.simulator.log;

public abstract class Log {
	public Log(){}
	public abstract void Write(String text);
	public abstract void WriteLn(String text);
	public abstract void Clear();
}
