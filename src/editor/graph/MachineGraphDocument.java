package editor.graph;

import java.awt.BorderLayout;

import editor.ModuleGraphDocument;
import graph.MachineGraph;
import ui_utils.MachineGraphControlComponent;

public class MachineGraphDocument extends ModuleGraphDocument {
	private static final long serialVersionUID = 1L;
	public MachineGraphDocument() {
		super();
		m_graph = new MachineGraph(m_console);
		m_graph_controls = new MachineGraphControlComponent(m_graph);
		m_graph_splitPane.setOneTouchExpandable(true);
		m_graph_splitPane.setDividerLocation(325);	
		m_graph_splitPane.setLeftComponent(m_graph_controls);	
		m_graph_splitPane.setRightComponent(m_graph.GetGraphComponent());
		
		m_input_output_tabbedPane.addTab("Machine Graph", m_graph_splitPane);
		m_input_output_tabbedPane.addTab("Console", m_console);
		add(m_input_output_tabbedPane, BorderLayout.CENTER);
	}
	
	public String ConvertGraphToModule()
	{
		return m_graph.GenerateTuringMachine();
	}
}
