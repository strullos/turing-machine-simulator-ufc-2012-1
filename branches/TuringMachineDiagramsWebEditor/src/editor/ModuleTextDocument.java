package editor;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ui_utils.ConsoleComponent;
import ui_utils.LineEditComponent;
import ui_utils.TextEditComponent;

public abstract class ModuleTextDocument extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ConsoleComponent console_;	
	protected TextEditComponent m_module_input;	
	protected LineEditComponent m_tape_input;
	protected boolean m_switch_to_input;
	protected JTabbedPane m_input_output_tabbedPane;
	
	public ModuleTextDocument()
	{		
		m_switch_to_input = false;
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
		console_.SetText(console_text);
	}
	
	public void AppendConsoleText(String text)
	{
		console_.AppendText(text);
	}
	
	public void ClearConsoleText()
	{
		console_.SetText("");
	}
	
	public String GetTape()
	{
		return m_tape_input.GetText();
	}	
	
	public ConsoleComponent console()
	{
		return console_;
	}
	
	public void SwitchConsoleAndInput()
	{
		if(m_switch_to_input){
			m_input_output_tabbedPane.setSelectedIndex(0);
			m_switch_to_input = false;
			m_module_input.SetFocusOnTextArea();
		}else{
			m_input_output_tabbedPane.setSelectedIndex(1);
			m_switch_to_input = true;
			m_tape_input.SetFocusOnTextField();
		}
	}
	
	public void HandleKeyEvents(KeyEvent e)
	{
		if(e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F4){
			SwitchConsoleAndInput();
		}
	}
}
