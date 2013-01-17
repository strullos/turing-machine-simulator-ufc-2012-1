package turing.machines.editor.perspectives;

import graph.Graph;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ui.utils.ConsoleComponent;
import ui.utils.LineEditComponent;
import ui.utils.MachineGraphControlComponent;

public class MachineGraphDocument extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTabbedPane m_console_and_controls_tabbedPane;	
	private ConsoleComponent m_console;	
	private LineEditComponent m_tape_input;
	private MachineGraphControlComponent m_graph_controls;
	
	Graph m_graph;
	public MachineGraphDocument() {
		setLayout(new BorderLayout(0, 0));
		m_graph = new Graph();
		m_tape_input = new LineEditComponent("Tape:");
		m_console = new ConsoleComponent();
	
		add(m_tape_input, BorderLayout.NORTH);		
		
		JSplitPane diagram_editor_splitPane = new JSplitPane();
		diagram_editor_splitPane.setOneTouchExpandable(true);
		add(diagram_editor_splitPane, BorderLayout.CENTER);
		diagram_editor_splitPane.setDividerLocation(750);		
		
		m_console_and_controls_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		diagram_editor_splitPane.setRightComponent(m_console_and_controls_tabbedPane);		
		diagram_editor_splitPane.setLeftComponent(m_graph.GetGraphComponent());

		m_graph_controls = new MachineGraphControlComponent(m_graph);
		m_console_and_controls_tabbedPane.addTab("Graph Controls",null, m_graph_controls, null);
		m_console_and_controls_tabbedPane.addTab("Console", null, m_console, null);			
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
	
	public String GetMachineText()
	{
		return m_graph.GenerateTuringMachine();
	}
	
	public String GetTape()
	{
		return m_tape_input.GetText();
	}	
	
	public ConsoleComponent console()
	{
		return m_console;
	}

}
