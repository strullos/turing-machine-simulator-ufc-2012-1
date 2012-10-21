package turing.machines.editor.perspectives;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import ui.utils.ConsoleComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;


public class MachineTextDocument extends JPanel {	
	/**
	 * 
	 */
	private ConsoleComponent m_console;	
	private TextEditComponent m_machine_input;	
	private LineEditComponent m_tape_input;
	
	private static final long serialVersionUID = 1L;
	public MachineTextDocument()
	{
		m_console = new ConsoleComponent();
		m_machine_input = new TextEditComponent("Machine:");
		m_tape_input = new LineEditComponent("Tape:");
		
		setLayout(new BorderLayout(0, 0));
		JSplitPane machine_text_editor_splitPane = new JSplitPane();
		machine_text_editor_splitPane.setOneTouchExpandable(true);
		add(machine_text_editor_splitPane);
		machine_text_editor_splitPane.setDividerLocation(500);
		
		machine_text_editor_splitPane.setRightComponent(m_console);
		machine_text_editor_splitPane.setDividerLocation(500);		
		machine_text_editor_splitPane.setLeftComponent(m_machine_input);			
		
		add(m_tape_input, BorderLayout.NORTH);		
	}
	
	public String GetMachineText()
	{
		return m_machine_input.GetText();
	}
	
	public void SetMachineText(String machine_text)
	{
		m_machine_input.SetText(machine_text);
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
