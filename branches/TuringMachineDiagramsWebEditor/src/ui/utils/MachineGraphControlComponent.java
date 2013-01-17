package ui.utils;

import graph.Graph;
import graph.GraphNode;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class MachineGraphControlComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Graph m_graph;
	private JButton m_add_node_button;
	
	public MachineGraphControlComponent(Graph graph){
		m_graph = graph;

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));

		JSplitPane controls_splitPane = new JSplitPane();
		controls_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(controls_splitPane, BorderLayout.CENTER);

		JPanel buttons_panel = new JPanel();
		controls_splitPane.setLeftComponent(buttons_panel);
		controls_splitPane.setRightComponent(m_graph.GetGraphOutline());
		buttons_panel.setLayout(new BorderLayout(0, 0));

		JPanel controls_panel = new JPanel();
		buttons_panel.add(controls_panel, BorderLayout.NORTH);
		controls_panel.setLayout(new BoxLayout(controls_panel, BoxLayout.Y_AXIS));

		m_add_node_button = new JButton("Add Node");
		controls_panel.add(m_add_node_button);

		JButton remove_node_button = new JButton("Remove Node");
		controls_panel.add(remove_node_button);
		remove_node_button.addActionListener(new RemoveNodeListener());

		m_add_node_button.addActionListener(new AddNodeListener());
		m_add_node_button.setEnabled(true);
	}
		
	
	class AddNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {		
			MachineGraphControlComponent.this.m_graph.AddNode("q");
		}
		
	};
	
	class RemoveNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			MachineGraphControlComponent.this.m_graph.RemoveSelectedCell();
		}
		
	};
}
