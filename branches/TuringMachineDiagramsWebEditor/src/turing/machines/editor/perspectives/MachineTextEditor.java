package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;
import ui.utils.ClosableTabComponent;

import java.awt.BorderLayout;

import javax.swing.JButton;
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
		if(m_machines_tabbedPane.getComponentCount() > 0){
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
	public void Open() {
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
					new_machine_document.SetMachineText(machine_text);
					m_machines_tabbedPane.addTab(fc.getSelectedFile().getName().toString(), null, new_machine_document, null);	
					m_machines_tabbedPane.setSelectedComponent(new_machine_document);
					m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
					m_current_machine_document.SetConsoleText("Machine file loaded successfully.\n");
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
		if(m_current_machine_document.GetMachineText().isEmpty()){
			m_current_machine_document.SetConsoleText("Empty machine.");
		}else{
			JFileChooser fc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Machine files (.mt)", "mt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			String file_path;
			if(returnVal == JFileChooser.APPROVE_OPTION){						
				file_path = fc.getSelectedFile().getAbsolutePath().toString() + ".mt";
				FileWriter fstream; 
				try {
					fstream = new FileWriter(file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(m_current_machine_document.GetMachineText());
					out.close();
					m_current_machine_document.SetConsoleText("Machine file saved succesfully.\n");
					m_machines_tabbedPane.setTitleAt(m_machines_tabbedPane.getSelectedIndex(), fc.getSelectedFile() + ".mt");
				} catch (IOException e) {
					e.printStackTrace();
				}						
			}		
		}
	}

	@Override
	public void Execute() {
		Machine m = new Machine();
		boolean empty_fields = false;
		m_current_machine_document.ClearConsoleText();
		if(m_current_machine_document.GetMachineText().isEmpty()){
			m_current_machine_document.SetConsoleText("Empty machine.\n");
			empty_fields = true;
		}
		if(m_current_machine_document.GetTape().isEmpty()){
			m_current_machine_document.AppendConsoleText("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( m.loadFromString(m_current_machine_document.GetMachineText())) {
					Tape tape = new Tape(m_current_machine_document.GetTape());				
					m_current_machine_document.SetConsoleText("Executing machine on tape: '" + tape.toString() + "'\n\n");
					m_current_machine_document.AppendConsoleText(m.getCurrentState() + ": " + tape.toString() + "\n");
					while( m.executeStep(tape) ) {
						m_current_machine_document.AppendConsoleText(m.getCurrentState() + ": " + tape.toString() + "\n");
					}
					m_current_machine_document.AppendConsoleText("\nStopped execution on " + Integer.toString(m.getSteps()) + " steps on state " + m.getCurrentState());
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
			MachineTextEditor.this.m_current_machine_document = (MachineTextDocument) MachineTextEditor.this.m_machines_tabbedPane.getSelectedComponent();			
		}		
		
	}
	
}
