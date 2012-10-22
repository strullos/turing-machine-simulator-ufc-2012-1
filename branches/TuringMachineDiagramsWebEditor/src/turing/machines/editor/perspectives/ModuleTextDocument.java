package turing.machines.editor.perspectives;

import javax.swing.JPanel;

import ui.utils.ConsoleComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;

public class ModuleTextDocument extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ConsoleComponent m_console;	
	protected TextEditComponent m_module_input;	
	protected LineEditComponent m_tape_input;
	
	public ModuleTextDocument()
	{		
	}
	
	public String GetModuleText()
	{
		return m_module_input.GetText();
	}
	
	public void SetModuleText(String machine_text)
	{
		m_module_input.SetText(machine_text);
	}
	
	public void SetConsoleText(String console_text)
	{
		m_console.SetText(console_text);
	}
	
	public void AppendConsoleText(String text)
	{
		m_console.AppendText(text);
	}
	
	public void ClearConsoleText()
	{
		m_console.SetText("");
	}
	
	public String GetTape()
	{
		return m_tape_input.GetText();
	}	
	
}
