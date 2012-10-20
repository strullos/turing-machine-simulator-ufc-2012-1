package turing.simulator.panels;

import graph.Graph;
import graph.GraphNode;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JList;

import com.mxgraph.view.mxGraph;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseWheelEvent;

import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.MouseWheelListener;
import javax.swing.JLayeredPane;

public class DiagramGraphEditor extends JPanel {

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
