package ui.utils;

import graph.Graph;
import graph.GraphNode;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import com.mxgraph.swing.mxGraphOutline;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

public class GraphControlComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Graph m_graph;
	private JList<String> m_modules_list;

	private JButton m_add_node_button;
	public GraphControlComponent(Graph graph) {
		m_graph = graph;
		
		m_modules_list = new JList<String>();
		DefaultListModel<String> list_model = new DefaultListModel<String>();
		m_modules_list.setModel(list_model);
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
		//controls_splitPane.setDividerLocation(400);
		
		m_add_node_button.addActionListener(new AddNodeListener());
		m_add_node_button.setEnabled(false);
	}
	
	public void AddModuleToList(String module)
	{
		DefaultListModel<String> model = (DefaultListModel<String>) m_modules_list.getModel();
		model.addElement(module);
		m_add_node_button.setEnabled(true);
	}
	
	class AddNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {		
			Object[] modules = new Object[m_modules_list.getModel().getSize()];
			for(int i = 0; i < m_modules_list.getModel().getSize(); i++)
			{
				modules[i] = (Object)m_modules_list.getModel().getElementAt(i);
			}
			String selected_module = (String) JOptionPane.showInputDialog(null,"Select the node's module:",
					"Module Selection", JOptionPane.PLAIN_MESSAGE,null,modules, modules[0]);
			if(selected_module != null && selected_module.length() > 0)
			{
				String node_name = "New Node";
				int i = 1;
				while(m_graph.ContainsNode(node_name)){
					node_name += i;
					i++;
				}
				GraphNode node = new GraphNode(node_name, selected_module, 0,0);
				GraphControlComponent.this.m_graph.AddNode(node);			
			}
		}
		
	};
	
	class RemoveNodeListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_graph.RemoveSelectedNode();			
		}
		
	}
}
