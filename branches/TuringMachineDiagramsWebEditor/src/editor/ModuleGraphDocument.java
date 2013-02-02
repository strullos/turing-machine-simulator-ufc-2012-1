package editor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import graph.Graph;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ui_utils.ConsoleComponent;
import ui_utils.GraphControlsComponent;
import ui_utils.LineEditComponent;


public abstract class ModuleGraphDocument extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ConsoleComponent m_console;	
	protected LineEditComponent m_tape_input;
	protected Graph m_graph;
	protected String m_graph_document_path;
	protected JTabbedPane m_input_output_tabbedPane;	
	protected GraphControlsComponent m_graph_controls;
	protected JSplitPane m_graph_splitPane;
	protected boolean m_switch_to_input;
	
	public ModuleGraphDocument() {
		m_switch_to_input = false;
		m_graph_document_path = "";
		m_console = new ConsoleComponent();
		m_graph = new Graph(m_console);
		m_tape_input = new LineEditComponent("Tape:");
		m_input_output_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		setLayout(new BorderLayout(0, 0));
		add(m_tape_input, BorderLayout.NORTH);		
		
		m_graph_splitPane = new JSplitPane();
	}
	
	public Graph GetGraph()
	{
		return m_graph;
	}
	
	public void GoToConsoleTab()
	{
		m_input_output_tabbedPane.setSelectedIndex(m_input_output_tabbedPane.getComponentCount() - 1);
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
	
	public ConsoleComponent console()
	{
		return m_console;
	}
	
	public String GetGraphDocumentPath()
	{
		return m_graph_document_path;
	}
	
	public void SetDocumentPath(String path)
	{
		m_graph_document_path = path;
	}
	
	abstract public String ConvertGraphToModule();	
	
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
		if(e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F4){
			SwitchConsoleAndInput();
		}
	}
}
