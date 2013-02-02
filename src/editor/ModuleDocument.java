package editor;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ui_utils.ConsoleComponent;
import ui_utils.LineEditComponent;


public abstract class ModuleDocument extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ConsoleComponent m_console = new ConsoleComponent();	
	protected LineEditComponent m_tape_input = new LineEditComponent("Tape:");
	protected boolean m_switch_to_input = false;
	protected JTabbedPane m_input_output_tabbedPane = new JTabbedPane();		
	protected String m_document_path;
	
	public ModuleDocument() 
	{

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

	public ConsoleComponent GetConsole()
	{
		return m_console;
	}

	public String GetDocumentPath()
	{
		return m_document_path;
	}

	public void SetDocumentPath(String path)
	{
		m_document_path = path;
	}

	public void SwitchConsoleAndInput()
	{
		if(m_switch_to_input){
			m_input_output_tabbedPane.setSelectedIndex(0);
			m_switch_to_input = false;
		}else{
			m_input_output_tabbedPane.setSelectedIndex(1);
			m_switch_to_input = true;
			m_tape_input.SetFocusOnTextField();
		}
	}

	public void HandleKeyEvents(KeyEvent e)
	{
		if(e.getID() == KeyEvent.KEY_RELEASED){
			switch(e.getKeyCode()){
			case KeyEvent.VK_F4:
				SwitchConsoleAndInput();
				break;
			case KeyEvent.VK_EQUALS:
				if(e.isControlDown()){
					IncreaseFontSize();
				}
				break;
			case KeyEvent.VK_MINUS:
				if(e.isControlDown()){
					DecreaseFontSize();
				}
				break;
			}
		}
	}
	
	protected void IncreaseFontSize()
	{
		m_console.IncreaseFontSize();
	}
	
	protected void DecreaseFontSize()
	{
		m_console.DecreaseFontSize();
	}
}
