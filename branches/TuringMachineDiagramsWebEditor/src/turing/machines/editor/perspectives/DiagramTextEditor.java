package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import turing.machines.editor.TuringMachinesEditor;
import turing.simulator.module.Diagram;
import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;
import ui.utils.ClosableTabComponent;
import ui.utils.ConfirmationFileChooser;
import ui.utils.HelpDialog;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DiagramTextEditor extends EditorPerspective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DiagramTextDocument m_current_diagram_document;

	private JTabbedPane m_diagrams_tabbedPane;
	public DiagramTextEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));
		
		m_diagrams_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_diagrams_tabbedPane, BorderLayout.CENTER);
		
		NewDiagramDocument();
	}
	
	public void NewDiagramDocument()
	{
		DiagramTextDocument diagram_document = new DiagramTextDocument();
		m_current_diagram_document = diagram_document;
		m_current_diagram_document.AddModule("diagram.dt", "");
		if(m_diagrams_tabbedPane.getComponentCount() > 1){
			m_diagrams_tabbedPane.addTab("New Diagram Project" + m_diagrams_tabbedPane.getComponentCount(), null, m_current_diagram_document, null);		
		}else{
			m_diagrams_tabbedPane.addTab("New Diagram Project", null, m_current_diagram_document, null);		
		}
		m_diagrams_tabbedPane.setSelectedComponent(diagram_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
	}

	@Override
	public void New() 
	{
		String[] options = { "Module", "Diagram Project" };
		String input = (String)JOptionPane.showInputDialog(
				this,
                "Create new:\n",   
                "New Item",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                "Module");
		if(input != null){
			if(input == "Module"){
				m_current_diagram_document.CreateNewModule();
			}
			if(input == "Diagram Project"){
				NewDiagramDocument();
			}
		}
	}

	@Override
	public void Open() 
	{
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Diagram files (.dt)", "dt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		boolean required_modules = true;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file_path = fc.getSelectedFile().getAbsolutePath().toString();		
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file_path));
				String line;
				try {
					String diagram_text = new String("");
					while( (line = reader.readLine()) != null ){
//						if(!m_current_diagram_document.CheckRequiredModules(line)){
//							required_modules = false;
//						}
						diagram_text += (line + "\n");		
					}
					DiagramTextDocument new_diagram_document = new DiagramTextDocument();
					m_current_diagram_document = new_diagram_document;
					new_diagram_document.SetModuleText(diagram_text);
					m_diagrams_tabbedPane.addTab(fc.getSelectedFile().getName().toString() + " Project", null, new_diagram_document, null);	
					m_diagrams_tabbedPane.setSelectedComponent(new_diagram_document);
					m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
					m_current_diagram_document.SetModuleText(diagram_text);
					TuringMachinesEditor.SetStatusMessage("Diagram file loaded successfully.\n");
					m_current_diagram_document.AddModule(fc.getSelectedFile().getName(), file_path);
					if(!required_modules){
						m_current_diagram_document.AppendConsoleText("Some of the required modules are not available, diagram won't be able to execute. " +
								"\nConsider adding the required modules.\n");
					}
					Diagram d = new Diagram();
					d.setLoadPath(fc.getSelectedFile().getParent());
					d.loadFromString(diagram_text);
					ArrayList<String> dependencies = d.getDependencies();
					for(int i = 0; i < dependencies.size(); i++)
					{
						File module_file = new File(dependencies.get(i));
						m_current_diagram_document.AddModule(module_file.getName(), module_file.getPath());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void Save() 
	{
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Error saving diagram file: empty diagram.\n");
		}else{
			String module_name = m_current_diagram_document.GetSelectedModule();
			String module_path = m_current_diagram_document.GetModulePath(module_name);		
			String module_filename = "";
			String file_extension = "";
			String file_type = "";
			if(module_path == ""){
				File target_file;
				if(module_name.endsWith(".mt")){
					target_file = SelectMachineFilePath(true);
					if(target_file == null){
						return;
					}
					module_path = target_file.getAbsolutePath().toString();
					if(!module_path.endsWith(".mt")){
						module_path += ".mt";						
					}
					file_type = "Machine file: ";
					file_extension = ".mt";
				}else if(module_name.endsWith(".dt")){
					target_file = SelectDiagramFilePath(true);
					if(target_file == null){
						return;
					}
					module_path = target_file.getAbsolutePath().toString();
					
					if(!module_path.endsWith(".dt")){
						module_path += ".dt";
					}	
					file_type = "Diagram file: ";
					file_extension = ".dt";
				}else{
					return;
				}
				module_filename = target_file.getName();
				if(!module_filename.contains(file_extension)){
					module_filename += file_extension;
				}
			}
			if(module_path.isEmpty()) //If the path is empty the user cancelled the save operation
			{
				return;
			}
			if(m_current_diagram_document.CheckDuplicatedModule(module_filename)){
				TuringMachinesEditor.SetStatusMessage("This diagram project already contains a module called " + module_filename + ". Please save it with a different name.");
				return;
			}
			FileWriter fstream; 
			try {
				fstream = new FileWriter(module_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(m_current_diagram_document.GetModuleText());
				out.close();
				TuringMachinesEditor.SetStatusMessage(file_type +  module_path + " saved succesfully.\n");
				m_current_diagram_document.UnmarkSelectedItem();
				m_current_diagram_document.SetModulePath(module_name, module_path);	
				m_current_diagram_document.RenameModule(module_name, module_filename);
			} catch (IOException e) {
				e.printStackTrace();
			}								
		}
	}
	
	@Override
	public void SaveAs() {
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Error saving diagram file: empty diagram.\n");
		}else{
			String module_name = m_current_diagram_document.GetSelectedModule();
			String module_path = "";
			String module_filename = "";
			String file_extension = "";
			String file_type = "";
			File target_file;
			if(module_name.endsWith(".mt")){
				target_file = SelectMachineFilePath(true);
				if(target_file == null){
					return;
				}
				module_path = target_file.getAbsolutePath().toString();
				if(!module_path.endsWith(".mt")){
					module_path += ".mt";
				}
				file_type = "Machine file: ";
				file_extension = ".mt";
			}else if(module_name.endsWith(".dt")){
				target_file = SelectDiagramFilePath(true);
				if(target_file == null){
					return;
				}
				module_path = target_file.getAbsolutePath().toString();
				if(!module_path.endsWith(".dt")){
					module_path += ".dt";
				}	
				file_type = "Diagram file: ";
				file_extension = ".dt";
			}else{
				return;
			}
			module_filename = target_file.getName();
			if(!module_filename.contains(file_extension)){
				module_filename += file_extension;
			}
			if(module_path.isEmpty()) //If the path is empty the user cancelled the save operation
			{
				return;
			}
			boolean duplicated_module = false;
			if(m_current_diagram_document.CheckDuplicatedModule(module_filename)){
				m_current_diagram_document.m_console.AppendText("This diagram project already contains a module called " + module_filename + ".File will be saved, but will not be added to the project.");
				duplicated_module = true;
			}
			FileWriter fstream; 
			try {
				fstream = new FileWriter(module_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(m_current_diagram_document.GetModuleText());
				out.close();
				if(!duplicated_module){
					m_current_diagram_document.AddModule(module_filename, module_path);
				}	
				TuringMachinesEditor.SetStatusMessage(file_type +  module_path + " saved succesfully.\n");
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}
	
	private File SelectMachineFilePath(boolean empty_file_name)
	{	
//		String machine_path = "";
		//This modified JFileChooser asks for confirmation if the user wants to overwrite the file
		ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
		if(!empty_file_name){
			fc.setSelectedFile(new File(m_current_diagram_document.GetSelectedModule()));
		}
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Machine files (.mt)", "mt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){	
//			machine_path = fc.getSelectedFile().getAbsolutePath().toString();
//			if(!machine_path.endsWith(".mt")){
//				machine_path += ".mt";
//			}	
			return fc.getSelectedFile();
		}
		return null;
	}
	
	private File SelectDiagramFilePath(boolean empty_file_name)
	{
//		String diagram_path = "";
		//This modified JFileChooser asks for confirmation if the user wants to overwrite the file
		ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));		
		if(!empty_file_name){
			fc.setSelectedFile(new File(m_current_diagram_document.GetSelectedModule()));
		}	
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Diagram files (.dt)", "dt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){	
//			diagram_path = fc.getSelectedFile().getAbsolutePath().toString();
//			if(!diagram_path.endsWith(".dt")){
//				diagram_path += ".dt";
//			}	
			return fc.getSelectedFile();
		}
		return null;
	}

	@Override
	public void Execute() {
		if(m_current_diagram_document.GetSelectedModule().endsWith(".mt")){
			ExecuteMachine();
		}else if (m_current_diagram_document.GetSelectedModule().endsWith(".dt")){
			ExecuteDiagram();
		}
	}
	
	private void ExecuteMachine()
	{
		if(m_current_diagram_document == null){
			return;
		}
		Machine m = new Machine();
		boolean empty_fields = false;
		m_current_diagram_document.ClearConsoleText();
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			empty_fields = true;
		}
		if(m_current_diagram_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( m.loadFromString(m_current_diagram_document.GetModuleText())) {
					Tape tape = new Tape(m_current_diagram_document.GetTape());				
					m.execute(tape);
					m_current_diagram_document.AppendConsoleText(m.getLog().getText());
				} else {
					m_current_diagram_document.SetConsoleText("Failed to process rule - error on line " + Integer.toString(m.getLine()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void ExecuteDiagram()
	{
		Diagram d = new Diagram();
		m_current_diagram_document.SetConsoleText("");
		d.setModuleFilesFullPath(m_current_diagram_document.GetModulesPath());
		d.setModulesContent(m_current_diagram_document.GetModulesContent());
		boolean empty_fields = false;
		
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Error executing: empty module.\n");
			empty_fields = true;
		}
		if(m_current_diagram_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Error executing: empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( d.loadFromString(m_current_diagram_document.GetModuleText()) ) {
					//This clear here is to erase the "loading messages" that may be on the log.
					//This way, only the execution messages are displayed when the diagram executes
					d.clearLog(); 					
					Tape tape = new Tape(m_current_diagram_document.GetTape());		
					m_current_diagram_document.AppendConsoleText("Initial module: "+ d.getCurrentState() + "\n");
					m_current_diagram_document.AppendConsoleText("Initial tape: " + tape.toString() + "\n\n");					
					d.execute(tape);	
					m_current_diagram_document.AppendConsoleText(d.getLog().getText());					
				} else {
					m_current_diagram_document.AppendConsoleText(d.getLog().getText());	
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
			if( DiagramTextEditor.this.m_diagrams_tabbedPane.getSelectedIndex() != -1){
				DiagramTextEditor.this.m_current_diagram_document = (DiagramTextDocument) DiagramTextEditor.this.m_diagrams_tabbedPane.getSelectedComponent();
			}else{
				DiagramTextEditor.this.m_current_diagram_document = null;
			}						
		}		
		
	}

	@Override
	public void Help() {
		HelpDialog help_dialog = new HelpDialog();
		help_dialog.SetHelpContent("Diagram Help");
		help_dialog.setVisible(true);
	}

	

}
