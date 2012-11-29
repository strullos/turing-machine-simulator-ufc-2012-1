package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import turing.machines.editor.TuringMachinesEditor;
import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;
import ui.utils.ClosableTabComponent;
import ui.utils.ConfirmationFileChooser;
import ui.utils.HelpDialog;
import utils.StringFileReader;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTabbedPane;

public class MachineTextEditor extends EditorPerspective {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MachineTextDocument m_current_machine_document;
	private JTabbedPane m_machines_tabbedPane;

	public MachineTextEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));		
		
		
		m_machines_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_machines_tabbedPane, BorderLayout.CENTER);	
		
		NewMachineDocument();
		m_machines_tabbedPane.addChangeListener(new TabChangedListener());
	}
	
	public void NewMachineDocument() 
	{
		MachineTextDocument machine_text_document = new MachineTextDocument();
		m_current_machine_document = machine_text_document;
		if(m_machines_tabbedPane.getComponentCount() > 1){
			m_machines_tabbedPane.addTab("New Machine" + m_machines_tabbedPane.getComponentCount(), null, machine_text_document, null);		
		}else{
			m_machines_tabbedPane.addTab("New Machine", null, machine_text_document, null);		
		}		
		m_machines_tabbedPane.setSelectedComponent(machine_text_document);
		m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
	}
	
	@Override
	public void New() {
		NewMachineDocument();		
	}

	@Override
	public void Open(String path) {
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Machine files (.mt)", "mt");
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
					MachineTextDocument new_machine_document = new MachineTextDocument();
					new_machine_document.SetModuleText(machine_text);
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
		if(m_current_machine_document == null){
			return;
		}
		if(m_current_machine_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.");
		}else{
			String machine_path = m_current_machine_document.GetMachineDocumentPath();
			String machine_name = "";
			if(machine_path.isEmpty()){
				ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Machine files (.mt)", "mt");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){	
					machine_path = fc.getSelectedFile().getAbsolutePath().toString();
					if(!machine_path.endsWith(".mt")){
						machine_path = machine_path + ".mt";
					}
					machine_name = fc.getSelectedFile().getName();
					if(!machine_name.endsWith(".mt")){
						machine_name = machine_name + ".mt";
					}
					m_current_machine_document.SetMachineDocumentPath(machine_path);
				}else{
					return;
				}
			}			
			FileWriter fstream; 
			try {
				fstream = new FileWriter(machine_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(m_current_machine_document.GetModuleText());
				out.close();
				TuringMachinesEditor.SetStatusMessage("Machine file " + machine_path + " saved succesfully.\n");
				if(!machine_name.isEmpty()){ //If machine name is empty the file was already saved so we do not need to save it again
					m_machines_tabbedPane.setTitleAt(m_machines_tabbedPane.getSelectedIndex(), machine_name);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public void Execute() {
		if(m_current_machine_document == null){
			return;
		}
		Machine m = new Machine();
		boolean empty_fields = false;
		m_current_machine_document.ClearConsoleText();
		if(m_current_machine_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			empty_fields = true;
		}
		if(m_current_machine_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( m.loadFromString(m_current_machine_document.GetModuleText())) {
					Tape tape = new Tape(m_current_machine_document.GetTape());				
					m.execute(tape);
					m_current_machine_document.AppendConsoleText(m.getLog().getText());
				} else {
					m_current_machine_document.SetConsoleText("Failed to process rule - error on line " + Integer.toString(m.getLine()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class TabChangedListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			if( MachineTextEditor.this.m_machines_tabbedPane.getSelectedIndex() != -1){
				MachineTextEditor.this.m_current_machine_document = (MachineTextDocument) MachineTextEditor.this.m_machines_tabbedPane.getSelectedComponent();
			}else{
				MachineTextEditor.this.m_current_machine_document = null;
			}						
		}		
		
	}

	@Override
	public void SaveAs() {
		if(m_current_machine_document == null){
			return;
		}
		if(m_current_machine_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.");
		}else{
			String machine_path = "";
			String machine_name = "";
			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Machine files (.mt)", "mt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){	
				machine_path = fc.getSelectedFile().getAbsolutePath().toString();
				if(!machine_path.endsWith(".mt")){
					machine_path = machine_path + ".mt";
				}
				machine_name = fc.getSelectedFile().getName();
				if(!machine_name.endsWith(".mt")){
					machine_name = machine_name + ".mt";
				}					
			}else{
				return;
			}
			FileWriter fstream; 
			try {
				String machine_content = m_current_machine_document.GetModuleText();
				fstream = new FileWriter(machine_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(machine_content);
				out.close();
				TuringMachinesEditor.SetStatusMessage("Machine file " + machine_path + " saved succesfully.\n");
				NewMachineDocument();
				if(!machine_name.isEmpty()){ //If machine name is empty the file was already saved so we do not need to save it again
					m_machines_tabbedPane.setTitleAt(m_machines_tabbedPane.getSelectedIndex(), machine_name);
				}		
				m_current_machine_document.SetMachineDocumentPath(machine_path);
				m_current_machine_document.SetModuleText(machine_content);
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
		
	}

	@Override
	public void Help() {
		HelpDialog help_dialog = new HelpDialog();
		StringFileReader file_reader = new StringFileReader();
		help_dialog.SetHelpContent(file_reader.ReadFile(getClass().getResourceAsStream("/help/machine_help.txt")));
		help_dialog.setVisible(true);	
	}

	@Override
	public void Examples() {
		// TODO Auto-generated method stub
		
	}
	
}
