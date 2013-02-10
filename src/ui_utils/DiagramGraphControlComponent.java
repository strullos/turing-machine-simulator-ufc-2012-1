package ui_utils;

import editor.TuringMachinesEditor;
import graph.Graph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import javax.swing.JLabel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class DiagramGraphControlComponent extends GraphControlsComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private JTextField m_selected_module_textField;
	private HashMap<String, String> m_modules_path;
	private HashMap<String, String> m_modules_content;
	private HashMap<mxCell, String> m_nodes_modules;
	public DiagramGraphControlComponent(Graph graph, HashMap<String, String> modules_path, HashMap<String,String> modules_content)
	{
		super(graph);
		m_selected_module_textField = new JTextField();
		m_selected_module_textField.setEditable(false);
		m_selected_module_textField.setMinimumSize(new Dimension(100,20));
		m_selected_module_textField.setPreferredSize(new Dimension(100,20));
		
		m_export_button.addActionListener(new ExportDtFileListener());
		m_add_node_button.addActionListener(new AddNodeListener());
		JPanel state_module_panel = new JPanel();
		state_module_panel.setBorder(new EmptyBorder(5, 10, 5, 10));
		m_controls_panel.add(state_module_panel, BorderLayout.WEST);
		state_module_panel.setLayout(new BoxLayout(state_module_panel, BoxLayout.X_AXIS));
		JLabel lblModule = new JLabel("Selected State Module:");
		state_module_panel.add(lblModule);
		state_module_panel.add(m_selected_module_textField);
		m_graph.GetGraphComponent().getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new SelectionChangedListener());	
		
		m_modules_path = modules_path;
		m_modules_content = modules_content;
		m_nodes_modules = new HashMap<mxCell,String>();
	}
	
	class AddNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {		
			DiagramNewStateDialog new_state_dialog = new DiagramNewStateDialog();
			new_state_dialog.setModal(true);
			int result = new_state_dialog.showDialog(); //If result equals to 1, the user confirmed the dialog			
			if(result == 1){
				String module_path = new_state_dialog.GetSelectedModulePath();
				if(module_path.isEmpty()){ //If path is empty, it is a pre-defined module
					m_modules_content.put(new_state_dialog.GetSelectedModule(), new_state_dialog.GetSelectedModuleContent());
				}else{
					m_modules_path.put(new_state_dialog.GetSelectedModule(), module_path);
				}
				mxCell node = DiagramGraphControlComponent.this.m_graph.AddNode(new_state_dialog.GetLabel(), new_state_dialog.GetSelectedModule());
				m_nodes_modules.put(node,  new_state_dialog.GetSelectedModule());
			}
		}

	};

	class SelectionChangedListener implements mxIEventListener
	{
		@Override
		public void invoke(Object arg0, mxEventObject arg1) {
			if(m_graph.GetGraphComponent().getGraph().getSelectionCells().length == 0){
				m_remove_node_button.setEnabled(false);
				m_starting_node_button.setEnabled(false);
				m_selected_module_textField.setEnabled(false);
				m_selected_module_textField.setText("");
			}else if(m_graph.GetGraphComponent().getGraph().getSelectionCells().length == 1){
				m_remove_node_button.setEnabled(true);
				m_starting_node_button.setEnabled(true);				
				m_selected_module_textField.setEnabled(true);
				mxCell node = (mxCell)m_graph.GetGraphComponent().getGraph().getSelectionCells()[0];
				m_selected_module_textField.setText(m_nodes_modules.get(node));
				m_selected_module_textField.setEnabled(true);
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

	@Override
	public void Update() {
		// TODO Auto-generated method stub
	}
}
