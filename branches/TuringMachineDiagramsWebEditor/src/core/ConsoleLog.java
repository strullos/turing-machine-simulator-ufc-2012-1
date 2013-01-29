package core;

import ui_utils.ConsoleComponent;

public class ConsoleLog extends Log {
	ConsoleComponent console_;
	public ConsoleLog(ConsoleComponent console){
		console_ = console;
	}
	@Override
	public void Write(String text) {
		console_.AppendText(text);
	}

	@Override
	public void WriteLn(String text) {
		console_.AppendText(text + "\n");
	}

	@Override
	public void Clear() {
		console_.ClearText();
	}

}
