package editor.graph;

import editor.ModuleGraphDocument;
import ui_utils.MachineGraphControlComponent;

public class MachineGraphDocument extends ModuleGraphDocument {
	private static final long serialVersionUID = 1L;
	public MachineGraphDocument() {
		super();
		m_graph_controls = new MachineGraphControlComponent(m_graph);
		m_graph_document_tabbedPane.addTab("Graph Controls",null, m_graph_controls, null);
		m_graph_document_tabbedPane.addTab("Console", null, m_console, null);	
	}
	
	public String ConvertGraphToModule()
	{
		return m_graph.GenerateTuringMachine();
	}
}
