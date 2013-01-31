package ui_utils;

import editor.TuringMachinesEditor;
import graph.Graph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JComboBox;

public class DiagramGraphControlComponent extends GraphControlsComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	ItemListComponent m_modules_list;
	private JComboBox<String> m_modules_comboBox;
	public DiagramGraphControlComponent(Graph graph, ItemListComponent modules_list)
	{
		super(graph);
		m_modules_comboBox = new JComboBox<String>();
		m_modules_list = modules_list;
		m_export_button.addActionListener(new ExportDtFileListener());
		m_add_node_button.addActionListener(new AddNodeListener());
		m_modules_comboBox.setEnabled(false);
		m_modules_comboBox.addActionListener(new ModuleComboBoxChangedListener());
		m_modules_comboBox = new JComboBox<String>();
		m_modules_comboBox.setMinimumSize(new Dimension(100, 24));
		m_modules_comboBox.setPreferredSize(new Dimension(100, 24));
		JPanel state_module_panel = new JPanel();
		state_module_panel.setBorder(new EmptyBorder(5, 10, 5, 10));
		m_controls_panel.add(state_module_panel, BorderLayout.WEST);
		state_module_panel.setLayout(new BoxLayout(state_module_panel, BoxLayout.X_AXIS));
		JLabel lblModule = new JLabel("Selected State Module:");
		state_module_panel.add(lblModule);
		state_module_panel.add(m_modules_comboBox);
		m_graph.GetGraphComponent().getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new SelectionChangedListener());	
		m_modules_comboBox.setEnabled(false);
	}
	
	public void SetModulesComboBoxEnabled(boolean enabled)
	{
		m_modules_comboBox.setEnabled(enabled);
	}
	
	public void Update()
	{
		UpdateModulesComboBox();
		SetAddNodeButtonEnabled((m_modules_list.GetItemsCount() > 0));
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
			String module_selection_header = "Module Selection";
			String selected_module = "";
			JList<String> modules = m_modules_list.GetItemsList();
			String[] modules_string_list = new String[m_modules_list.GetItemsCount()];
			for(int i = 0; i < modules_string_list.length; i++){
				modules_string_list[i] = modules.getModel().getElementAt(i);
			}
			selected_module = (String) JOptionPane.showInputDialog(DiagramGraphControlComponent.this,
					"Please select a Diagram Module to add a new state",module_selection_header, JOptionPane.INFORMATION_MESSAGE, 
					null, modules_string_list,0);
			if(!selected_module.isEmpty()){
				DiagramGraphControlComponent.this.m_graph.AddNode("X", selected_module);
			}
			modules = null;
			modules_string_list = null;
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

	class ExportDtFileListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String diagram_dt_text = m_graph.GenerateTuringDiagram();
			if(diagram_dt_text.isEmpty()){
				TuringMachinesEditor.SetStatusMessage("Diagram Graph empty.\n");
				return;
			}
			String diagram_graph_file_path;
			String diagram_graph_file_name;
			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Diagram Graph files (.dt)", "dt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){	
				diagram_graph_file_path = fc.getSelectedFile().getAbsolutePath().toString();
				if(!diagram_graph_file_path.endsWith(".dt")){
					diagram_graph_file_path = diagram_graph_file_path + ".dt";
				}
				diagram_graph_file_name = fc.getSelectedFile().getName();
				if(!diagram_graph_file_name.endsWith(".dt")){
					diagram_graph_file_name = diagram_graph_file_name + ".dt";
				}			
				FileWriter fstream; 
				try {
					fstream = new FileWriter(diagram_graph_file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(diagram_dt_text);
					out.close();
					TuringMachinesEditor.SetStatusMessage("Diagram Graph file " + diagram_graph_file_path + " exported succesfully.\n");							
				} catch (IOException ex) {
					ex.printStackTrace();
				}						
			}
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
