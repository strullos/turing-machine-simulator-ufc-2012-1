package editor.text;

import ui_utils.ClosableTabComponent;
import ui_utils.ConfirmationFileChooser;
import ui_utils.HelpDialog;
import utils.StringFileReader;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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

import core.ConsoleLog;
import core.Machine;
import core.Tape;
import editor.EditorPerspective;
import editor.TuringMachinesEditor;

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

		New();
		m_machines_tabbedPane.addChangeListener(new TabChangedListener());
	}

	@Override
	public void New() {
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
		if(m_current_machine_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			JOptionPane.showMessageDialog(null, "Empty machine", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(m_current_machine_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			JOptionPane.showMessageDialog(null, "Empty tape", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}
		Machine m = new Machine();
		m.logs_.AddLog(new ConsoleLog(m_current_machine_document.console()));
		m_current_machine_document.ClearConsoleText();
		try {
			if( m.loadFromString(m_current_machine_document.GetModuleText())) {
				Tape tape = new Tape(m_current_machine_document.GetTape());				
				m.execute(tape);
				m = null;
				tape = null;
			} 
		} catch (IOException e) {
			e.printStackTrace();
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
				New();
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
		help_dialog.SetHelpContent(StringFileReader.ReadFile(getClass().getResourceAsStream("/help/machine_help.txt")));
		help_dialog.setVisible(true);	
	}

	@Override
	public void Examples() {
		// TODO Auto-generated method stub

	}

	@Override
	public void HandleKeyEvents(KeyEvent e) {
		m_current_machine_document.HandleKeyEvents(e);
	}

}
