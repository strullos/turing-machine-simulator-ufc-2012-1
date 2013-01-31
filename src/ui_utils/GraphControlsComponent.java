package ui_utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import graph.Graph;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public abstract class GraphControlsComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton m_add_node_button;
	protected JButton m_remove_node_button;
	protected JButton m_starting_node_button;
	protected JButton m_export_button;
	protected Graph m_graph;
	protected JPanel m_controls_panel;
	public GraphControlsComponent(Graph graph)
	{
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

		m_controls_panel = new JPanel();
		buttons_panel.add(m_controls_panel, BorderLayout.NORTH);
		m_controls_panel.setLayout(new BorderLayout(0, 0));

		JPanel state_controls_panel = new JPanel();
		state_controls_panel.setBorder(new EmptyBorder(10, 10, 5, 10));
		m_controls_panel.add(state_controls_panel, BorderLayout.NORTH);
		state_controls_panel.setLayout(new BoxLayout(state_controls_panel, BoxLayout.X_AXIS));

		JLabel lblGraph = new JLabel("Graph:");
		state_controls_panel.add(lblGraph);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		state_controls_panel.add(horizontalStrut);

		m_starting_node_button = new JButton("");
		state_controls_panel.add(m_starting_node_button);
		m_starting_node_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/flag-green.png")));
		m_starting_node_button.setToolTipText("Set Starting Module");
		m_starting_node_button.addActionListener(new SetStartingNodeListener());

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setMinimumSize(new Dimension(5, 0));
		horizontalStrut_2.setMaximumSize(new Dimension(5, 32767));
		state_controls_panel.add(horizontalStrut_2);

		m_export_button = new JButton("");
		state_controls_panel.add(m_export_button);
		m_export_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/document-save-3.png")));
		m_export_button.setToolTipText("Export to .dt File");

		JPanel graph_controls_panel = new JPanel();
		graph_controls_panel.setBorder(new EmptyBorder(5, 10, 10, 10));
		m_controls_panel.add(graph_controls_panel, BorderLayout.SOUTH);
		graph_controls_panel.setLayout(new BoxLayout(graph_controls_panel, BoxLayout.X_AXIS));

		JLabel lblState = new JLabel("Diagram State:");
		graph_controls_panel.add(lblState);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		graph_controls_panel.add(horizontalStrut_1);

		m_add_node_button = new JButton("");
		graph_controls_panel.add(m_add_node_button);
		m_add_node_button.setToolTipText("Add Dagraim State");
		m_add_node_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/list-add.png")));
		m_add_node_button.setEnabled(true);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalStrut_3.setMaximumSize(new Dimension(5, 32767));
		horizontalStrut_3.setMinimumSize(new Dimension(5, 0));
		graph_controls_panel.add(horizontalStrut_3);

		m_remove_node_button = new JButton("");
		graph_controls_panel.add(m_remove_node_button);
		m_remove_node_button.setToolTipText("Remove Selected Diagram State or Edge");
		m_remove_node_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/list-remove.png")));
		m_remove_node_button.addActionListener(new RemoveNodeListener());
		m_remove_node_button.setEnabled(false);
		m_starting_node_button.setEnabled(false);
	}
	
	public void SetAddNodeButtonEnabled(boolean enabled)
	{
		m_add_node_button.setEnabled(enabled);
	}
	
	public abstract void Update();
	
	class SetStartingNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			GraphControlsComponent.this.m_graph.SetStartingNode();
		}
	}
	
	class RemoveNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			GraphControlsComponent.this.m_graph.RemoveSelectedCell();
		}

	};
}
