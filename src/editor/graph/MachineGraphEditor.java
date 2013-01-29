package editor.graph;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.ConsoleLog;
import core.Machine;
import core.Tape;
import editor.EditorPerspective;
import editor.TuringMachinesEditor;

import ui_utils.ClosableTabComponent;
import ui_utils.ConfirmationFileChooser;
import ui_utils.HelpDialog;
import utils.StringFileReader;

public class MachineGraphEditor extends EditorPerspective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MachineGraphDocument m_current_machine_graph_document;
	private JTabbedPane m_machines_tabbedPane;

	public MachineGraphEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));

		m_machines_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_machines_tabbedPane, BorderLayout.CENTER);

		MachineGraphDocument machine_document = new MachineGraphDocument();
		if(m_machines_tabbedPane.getComponentCount() > 0){
			m_machines_tabbedPane.addTab("New Graph Machine" + m_machines_tabbedPane.getComponentCount(), null, machine_document, null);		
		}else{
			m_machines_tabbedPane.addTab("New Graph Machine", null, machine_document, null);		
		}
		m_machines_tabbedPane.setSelectedComponent(machine_document);
		m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
		m_current_machine_graph_document = machine_document;
		m_machines_tabbedPane.addChangeListener(new TabChangedListener());
	}

	class TabChangedListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			if( MachineGraphEditor.this.m_machines_tabbedPane.getSelectedIndex() != -1){
				MachineGraphEditor.this.m_current_machine_graph_document = (MachineGraphDocument) MachineGraphEditor.this.m_machines_tabbedPane.getSelectedComponent();
			}else{
				MachineGraphEditor.this.m_current_machine_graph_document = null;
			}						
		}		
	}

	@Override
	public void New() {
		MachineGraphDocument machine_graph_document = new MachineGraphDocument();
		m_current_machine_graph_document = machine_graph_document;
		if(m_machines_tabbedPane.getComponentCount() > 1){
			m_machines_tabbedPane.addTab("New Graph Machine" + m_machines_tabbedPane.getComponentCount(), null, machine_graph_document, null);		
		}else{
			m_machines_tabbedPane.addTab("New Graph Machine", null, machine_graph_document, null);		
		}		
		m_machines_tabbedPane.setSelectedComponent(machine_graph_document);
		m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
	}

	@Override
	public void Open(String path) {
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Machine Graph files (.gmt)", "gmt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file_path = fc.getSelectedFile().getAbsolutePath().toString();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file_path));
				String line;
				try {
					String machine_text = new String("");
					while( (line = reader.readLine()) != null ){
						machine_text += line;		
						machine_text += "\n";						
					}
					MachineGraphDocument new_machine_document = new MachineGraphDocument();
					new_machine_document.m_graph.ImportGraph(machine_text);					
					m_machines_tabbedPane.addTab(fc.getSelectedFile().getName().toString(), null, new_machine_document, null);	
					m_machines_tabbedPane.setSelectedComponent(new_machine_document);
					m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
					TuringMachinesEditor.SetStatusMessage("Machine file loaded successfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void Save() {
		if(m_current_machine_graph_document == null){
			return;
		}
		String machine_graph_text = m_current_machine_graph_document.GetMachineText();
		if(machine_graph_text.isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.");
		}else{
			String machine_path = m_current_machine_graph_document.GetMachineGraphDocumentPath();
			String machine_name = "";
			if(machine_path.isEmpty()){
				ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Machine Graph files (.gmt)", "gmt");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){	
					machine_path = fc.getSelectedFile().getAbsolutePath().toString();
					if(!machine_path.endsWith(".gmt")){
						machine_path = machine_path + ".gmt";
					}
					machine_name = fc.getSelectedFile().getName();
					if(!machine_name.endsWith(".gmt")){
						machine_name = machine_name + ".gmt";
					}
					m_current_machine_graph_document.SetMachineDocumentPath(machine_path);
				}else{
					return;
				}
			}			
			FileWriter fstream; 
			try {
				fstream = new FileWriter(machine_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(m_current_machine_graph_document.m_graph.ExportGraph());
				out.close();
				TuringMachinesEditor.SetStatusMessage("Machine Graph file " + machine_path + " saved succesfully.\n");
				if(!machine_name.isEmpty()){ //If machine name is empty the file was already saved so we do not need to save it again
					m_machines_tabbedPane.setTitleAt(m_machines_tabbedPane.getSelectedIndex(), machine_name);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public void SaveAs() {
		if(m_current_machine_graph_document == null){
			return;
		}
		String machine_graph_text = m_current_machine_graph_document.GetMachineText();
		if(machine_graph_text.isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.");
		}else{
			String machine_path = m_current_machine_graph_document.GetMachineGraphDocumentPath();
			String machine_name = "";
			if(machine_path.isEmpty()){
				ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Machine Graph files (.gmt)", "gmt");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){	
					machine_path = fc.getSelectedFile().getAbsolutePath().toString();
					if(!machine_path.endsWith(".gmt")){
						machine_path = machine_path + ".gmt";
					}
					machine_name = fc.getSelectedFile().getName();
					if(!machine_name.endsWith(".gmt")){
						machine_name = machine_name + ".gmt";
					}
					m_current_machine_graph_document.SetMachineDocumentPath(machine_path);
				}else{
					return;
				}
			}			
			FileWriter fstream; 
			try {
				String machine_content = m_current_machine_graph_document.m_graph.ExportGraph();
				fstream = new FileWriter(machine_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(machine_content);
				out.close();
				TuringMachinesEditor.SetStatusMessage("Machine Graph file " + machine_path + " saved succesfully.\n");
				New();
				if(!machine_name.isEmpty()){ //If machine name is empty the file was already saved so we do not need to save it again
					m_machines_tabbedPane.setTitleAt(m_machines_tabbedPane.getSelectedIndex(), machine_name);
				}		
				m_current_machine_graph_document.SetMachineDocumentPath(machine_path);
				m_current_machine_graph_document.m_graph.ImportGraph(machine_content);
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public void Execute() {
		if(m_current_machine_graph_document == null){
			return;
		}
		Machine m = new Machine();
		m.logs_.AddLog(new ConsoleLog(m_current_machine_graph_document.console()));
		boolean empty_fields = false;
		m_current_machine_graph_document.ClearConsoleText();
		if(m_current_machine_graph_document.GetMachineText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			empty_fields = true;
		}
		if(m_current_machine_graph_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				String machine_text = m_current_machine_graph_document.GetMachineText();
				System.out.println(machine_text);
				if( m.loadFromString(m_current_machine_graph_document.GetMachineText())) {
					Tape tape = new Tape(m_current_machine_graph_document.GetTape());				
					m.execute(tape);
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		TuringMachinesEditor.SetStatusMessage("Execution finished.\n");
	}

	@Override
	public void Help() {
		HelpDialog help_dialog = new HelpDialog();
		help_dialog.SetHelpContent(StringFileReader.ReadFile(getClass().getResourceAsStream("/help/machine_graph_help.txt")));
		help_dialog.setVisible(true);	
	}

	@Override
	public void Examples() {
		// TODO Auto-generated method stub
	}
}
