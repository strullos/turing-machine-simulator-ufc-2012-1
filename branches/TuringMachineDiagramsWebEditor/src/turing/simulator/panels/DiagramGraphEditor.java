package turing.simulator.panels;

import graph.Graph;
import graph.GraphNode;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;

public class DiagramGraphEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Graph m_graph;

	/**
	 * Create the panel.
	 */
	public DiagramGraphEditor() {
		m_graph = new Graph();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLayeredPane layeredPane = new JLayeredPane();
		add(layeredPane);
	
		
		GraphNode node = new GraphNode("TESTE",100,100);
		m_graph.AddNode(node);
		
	}
}
