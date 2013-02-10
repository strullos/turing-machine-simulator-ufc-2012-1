package editor;

import java.awt.BorderLayout;
import graph.Graph;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ui_utils.ConsoleComponent;
import ui_utils.GraphControlsComponent;
import ui_utils.LineEditComponent;


public abstract class ModuleGraphDocument extends ModuleDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Graph m_graph;

	protected GraphControlsComponent m_graph_controls;
	protected JSplitPane m_graph_splitPane;
	
	public ModuleGraphDocument() {
		m_document_path = "";
		m_console = new ConsoleComponent();
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
	
	abstract public String ConvertGraphToModule();	
}
