package ui_utils;

import editor.TuringMachinesEditor;
import graph.Graph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MachineGraphControlComponent extends GraphControlsComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MachineGraphControlComponent(Graph graph){
		super(graph);
		m_graph.GetGraphComponent().getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new SelectionChangedListener());			
		m_add_node_button.addActionListener(new AddNodeListener());
		m_export_button.addActionListener(new ExportMtFileListener());
	}
	
	public void Update()
	{
		return;
	}

	class AddNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {		
			MachineGraphControlComponent.this.m_graph.AddNode("q");
		}

	};

	class SelectionChangedListener implements mxIEventListener
	{
		@Override
		public void invoke(Object arg0, mxEventObject arg1) {
			if(m_graph.GetGraphComponent().getGraph().getSelectionCells().length == 0){
				m_remove_node_button.setEnabled(false);
				m_starting_node_button.setEnabled(false);
			}else{
				m_remove_node_button.setEnabled(true);
				m_starting_node_button.setEnabled(false);
			}			
		}		
	}

	class ExportMtFileListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String machine_mt_text = m_graph.GenerateTuringMachine();
			if(machine_mt_text.isEmpty()){
				TuringMachinesEditor.SetStatusMessage("Machine Graph empty.\n");
				return;
			}
			String machine_graph_file_path;
			String machine_graph_file_name;
			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Machine files (.mt)", "mt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){	
				machine_graph_file_path = fc.getSelectedFile().getAbsolutePath().toString();
				if(!machine_graph_file_path.endsWith(".mt")){
					machine_graph_file_path = machine_graph_file_path + ".mt";
				}
				machine_graph_file_name = fc.getSelectedFile().getName();
				if(!machine_graph_file_name.endsWith(".mt")){
					machine_graph_file_name = machine_graph_file_name + ".mt";
				}			
				FileWriter fstream; 
				try {
					fstream = new FileWriter(machine_graph_file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(machine_mt_text);
					out.close();
					TuringMachinesEditor.SetStatusMessage("Machine Graph file " + machine_graph_file_path + " exported succesfully.\n");							
				} catch (IOException ex) {
					ex.printStackTrace();
				}						
			}
		}
	}
}
