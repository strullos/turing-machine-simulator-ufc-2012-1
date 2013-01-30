package ui_utils;

import editor.TuringMachinesEditor;
import graph.Graph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.sun.corba.se.spi.orbutil.fsm.Action;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JComboBox;

public class DiagramGraphControlComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Graph m_graph;
	ItemListComponent m_modules_list;
	private JButton m_add_node_button;
	private JButton m_remove_node_button;
	private JButton m_starting_node_button;
	private JButton m_export_to_dt_button;
	private JComboBox<String> m_modules_comboBox;

	public DiagramGraphControlComponent(Graph graph, ItemListComponent modules_list){
		m_graph = graph;
		m_modules_list = modules_list;

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
		controls_panel.setLayout(new BorderLayout(0, 0));

		JPanel state_controls_panel = new JPanel();
		state_controls_panel.setBorder(new EmptyBorder(10, 10, 5, 10));
		controls_panel.add(state_controls_panel, BorderLayout.NORTH);
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

		m_export_to_dt_button = new JButton("");
		state_controls_panel.add(m_export_to_dt_button);
		m_export_to_dt_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/document-save-3.png")));
		m_export_to_dt_button.setToolTipText("Export to .dt File");
		m_export_to_dt_button.addActionListener(new ExportMtFileListener());

		JPanel graph_controls_panel = new JPanel();
		graph_controls_panel.setBorder(new EmptyBorder(5, 10, 10, 10));
		controls_panel.add(graph_controls_panel, BorderLayout.SOUTH);
		graph_controls_panel.setLayout(new BoxLayout(graph_controls_panel, BoxLayout.X_AXIS));

		JLabel lblState = new JLabel("Diagram State:");
		graph_controls_panel.add(lblState);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		graph_controls_panel.add(horizontalStrut_1);

		m_add_node_button = new JButton("");
		graph_controls_panel.add(m_add_node_button);
		m_add_node_button.setToolTipText("Add Dagraim State");
		m_add_node_button.setIcon(new ImageIcon(MachineGraphControlComponent.class.getResource("/resources/icons/list-add.png")));

		m_add_node_button.addActionListener(new AddNodeListener());
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
		
		JPanel state_module_panel = new JPanel();
		state_module_panel.setBorder(new EmptyBorder(5, 10, 5, 10));
		controls_panel.add(state_module_panel, BorderLayout.WEST);
		state_module_panel.setLayout(new BoxLayout(state_module_panel, BoxLayout.X_AXIS));
		
		JLabel lblModule = new JLabel("Selected State Module:");
		state_module_panel.add(lblModule);
		
		m_modules_comboBox = new JComboBox<String>();
		m_modules_comboBox.setMinimumSize(new Dimension(100, 24));
		state_module_panel.add(m_modules_comboBox);


		m_graph.GetGraphComponent().getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new SelectionChangedListener());	
		m_modules_comboBox.setEnabled(false);
		m_starting_node_button.setEnabled(false);
		m_modules_comboBox.addActionListener(new ModuleComboBoxChangedListener());
	}
	
	public void SetAddNodeButtonEnabled(boolean enabled)
	{
		m_add_node_button.setEnabled(enabled);
	}
	
	public void SetModulesComboBoxEnabled(boolean enabled)
	{
		m_modules_comboBox.setEnabled(enabled);
	}
	
	public void UpdateModulesComboBox()
	{
		m_modules_comboBox.removeAll();
		JList<String> modules = m_modules_list.GetItemsList();
		for(int i = 0; i < modules.getModel().getSize(); i++){
			m_modules_comboBox.insertItemAt(modules.getModel().getElementAt(i), i);			
		}
		m_modules_comboBox.setSelectedIndex(-1);
	}


	class AddNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {		
			//DiagramGraphControlComponent.this.m_graph.AddNode("q");
			String module_selection_header = "Module Selection";
			String selected_module = "";
			JList<String> modules = m_modules_list.GetItemsList();
			String[] modules_string_list = new String[m_modules_list.GetItemsCount()];
			for(int i = 0; i < modules_string_list.length; i++){
				modules_string_list[i] = modules.getModel().getElementAt(i);
			}
			selected_module = (String) JOptionPane.showInputDialog(DiagramGraphControlComponent.this,
					"Please select a Diagram Module to add a new Diagram State",module_selection_header, JOptionPane.INFORMATION_MESSAGE, 
					null, modules_string_list,0);
			if(!selected_module.isEmpty()){
				DiagramGraphControlComponent.this.m_graph.AddNode("X", selected_module);
			}
			modules = null;
			modules_string_list = null;
		}

	};

	class RemoveNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			DiagramGraphControlComponent.this.m_graph.RemoveSelectedCell();
		}

	};

	class SelectionChangedListener implements mxIEventListener
	{
		@Override
		public void invoke(Object arg0, mxEventObject arg1) {
			if(m_graph.GetGraphComponent().getGraph().getSelectionCells().length == 0){
				m_remove_node_button.setEnabled(false);
				m_starting_node_button.setEnabled(false);
				m_modules_comboBox.setEnabled(false);
			}else if(m_graph.GetGraphComponent().getGraph().getSelectionCells().length == 1){
				m_remove_node_button.setEnabled(true);
				m_starting_node_button.setEnabled(true);				
				String selected_node_module = m_graph.GetSelectedNodeModule();
				m_modules_comboBox.setEnabled(true);
				if(selected_node_module != null){					
					JList<String> modules_list  = m_modules_list.GetItemsList();
					for(int i = 0; i < modules_list.getModel().getSize(); i++){
						if(modules_list.getModel().getElementAt(i).equals(selected_node_module)){
							m_modules_comboBox.setSelectedIndex(i);
						}
					}
					modules_list = null;
				}
			}					
		}		
	}

	class SetStartingNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			m_graph.SetStartingNode();
		}
	}

	class ExportMtFileListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
//			String machine_mt_text = m_graph.GenerateTuringMachine();
//			if(machine_mt_text.isEmpty()){
//				TuringMachinesEditor.SetStatusMessage("Machine Graph empty.\n");
//				return;
//			}
//			String machine_graph_file_path;
//			String machine_graph_file_name;
//			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
//			FileNameExtensionFilter filter = new FileNameExtensionFilter(
//					"Diagram files (.dt)", "dt");
//			fc.setFileFilter(filter);
//			fc.setAcceptAllFileFilterUsed(false);
//			int returnVal = fc.showSaveDialog(null);
//			if(returnVal == JFileChooser.APPROVE_OPTION){	
//				machine_graph_file_path = fc.getSelectedFile().getAbsolutePath().toString();
//				if(!machine_graph_file_path.endsWith(".dt")){
//					machine_graph_file_path = machine_graph_file_path + ".dt";
//				}
//				machine_graph_file_name = fc.getSelectedFile().getName();
//				if(!machine_graph_file_name.endsWith(".dt")){
//					machine_graph_file_name = machine_graph_file_name + ".dt";
//				}			
//				FileWriter fstream; 
//				try {
//					fstream = new FileWriter(machine_graph_file_path);
//					BufferedWriter out = new BufferedWriter(fstream);
//					out.write(machine_mt_text);
//					out.close();
//					TuringMachinesEditor.SetStatusMessage("Machine Graph file " + machine_graph_file_path + " exported succesfully.\n");							
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}						
//			}
			m_graph.GenerateTuringDiagram();
		}
	}
	
	class ModuleComboBoxChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String selected_module = (String) m_modules_comboBox.getSelectedItem();
			m_graph.SetModuleOfSelectedNode(selected_module);
		}
		
	}
}
