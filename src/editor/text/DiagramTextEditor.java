package editor.text;

import ui_utils.ClosableTabComponent;
import ui_utils.ConfirmationFileChooser;
import ui_utils.ExamplesDialog;
import ui_utils.HelpDialog;
import utils.StringFileReader;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.ConsoleLog;
import core.Diagram;
import core.Machine;
import core.Tape;
import editor.EditorPerspective;
import editor.TuringMachinesEditor;

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
		m_diagrams_tabbedPane.addChangeListener(new TabChangedListener());
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
	public void Open(String file_path) 
	{		
		if(file_path.isEmpty()){
			JFileChooser fc = new JFileChooser(new File("."));		
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Diagram files (.dt)", "dt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(null);		
			if(returnVal == JFileChooser.APPROVE_OPTION){
				file_path = fc.getSelectedFile().getAbsolutePath().toString();	
			}else{
				return;
			}
		}		
		try {
			File selected_file = new File(file_path);
			BufferedReader reader = new BufferedReader(new FileReader(file_path));			
			try {				
				DiagramTextDocument new_diagram_document = new DiagramTextDocument();
				m_current_diagram_document = new_diagram_document;
				m_diagrams_tabbedPane.addTab(selected_file.getName().toString() + " Project", null, new_diagram_document, null);	
				m_diagrams_tabbedPane.setSelectedComponent(new_diagram_document);
				m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));

				TuringMachinesEditor.SetStatusMessage("Diagram file loaded successfully.\n");
				m_current_diagram_document.AddModule(selected_file.getName(), file_path);

				Diagram d = new Diagram();
				d.logs_.AddLog(new ConsoleLog(m_current_diagram_document.GetConsole()));
				d.setLoadPath(selected_file.getParent());

				String diagram_text = new String("");
				String line;
				while( (line = reader.readLine()) != null ){				
					diagram_text += (line + "\n");		
				}
				d.loadFromString(diagram_text);
				m_current_diagram_document.SetModuleText(diagram_text);
				ArrayList<String> dependencies = d.getDependencies();
				for(int i = 0; i < dependencies.size(); i++)
				{
					File module_file = new File(dependencies.get(i));
					m_current_diagram_document.AddModule(module_file.getName(), module_file.getPath());
					module_file = null;
				}
				m_current_diagram_document.SetMainModuleSelected();
				diagram_text = null;
				d = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	public void OpenExample(String example_name, String example_path)
	{
		boolean required_modules = true;
		String line;
		String diagram_text = new String("");
		ArrayList<String> lines = StringFileReader.GetLineArrayFromStream(getClass().getResourceAsStream(example_path));
		Iterator<String> it = lines.iterator();
		while(it.hasNext()){
			line = it.next();
			diagram_text += (line + "\n");	
		}
		DiagramTextDocument new_diagram_document = new DiagramTextDocument();
		m_current_diagram_document = new_diagram_document;
		new_diagram_document.SetModuleText(diagram_text);
		m_diagrams_tabbedPane.addTab(example_name, null, new_diagram_document, null);	
		m_diagrams_tabbedPane.setSelectedComponent(new_diagram_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
		m_current_diagram_document.SetModuleText(diagram_text);
		TuringMachinesEditor.SetStatusMessage("Diagram file loaded successfully.\n");
		m_current_diagram_document.AddModule(example_name, example_path);
		if(!required_modules){
			m_current_diagram_document.AppendConsoleText("Some of the required modules are not available, diagram won't be able to execute. " +
					"\nConsider adding the required modules.\n");
		}
		Diagram d = new Diagram();		
		d.logs_.AddLog(new ConsoleLog(m_current_diagram_document.GetConsole()));
		String example_dir = example_path.replaceAll("/" + example_name ,"");
		d.setLoadPath(example_dir);
		try {
			d.loadFromString(diagram_text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> dependencies = d.getDependencies();
		for(int i = 0; i < dependencies.size(); i++)
		{
			File module_file = new File(dependencies.get(i));
			m_current_diagram_document.AddModule(module_file.getName(), module_file.getPath());
		}
		m_current_diagram_document.SetMainModuleSelected();
	}



	@Override
	public void Save() 
	{
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Error saving file: empty content.\n");
		}else{
			String module_name = m_current_diagram_document.GetSelectedModule();
			String module_path = m_current_diagram_document.GetModulePath(module_name);		
			String module_filename = "";
			String file_extension = "";
			String file_type = "";
			if(module_path.isEmpty()){
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
				if(module_path.isEmpty()) //If the path is still empty the user cancelled the save operation
				{
					return;
				}				
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
				if(!module_filename.isEmpty()){
					m_current_diagram_document.RenameModule(module_name, module_filename);
				}
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
				m_current_diagram_document.GetConsole().AppendText("This diagram project already contains a module called " + module_filename + ".File will be saved, but will not be added to the project.");
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
		//This modified JFileChooser asks for confirmation if the user wants to overwrite the file
		ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
		if(!empty_file_name){
			fc.setSelectedFile(new File(m_current_diagram_document.GetSelectedModule()));
		}
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Machine files (.mt)", "mt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){	
			return fc.getSelectedFile();
		}
		return null;
	}

	private File SelectDiagramFilePath(boolean empty_file_name)
	{
		//This modified JFileChooser asks for confirmation if the user wants to overwrite the file
		ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));		
		if(!empty_file_name){
			fc.setSelectedFile(new File(m_current_diagram_document.GetSelectedModule()));
		}	
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Diagram files (.dt)", "dt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){	
			return fc.getSelectedFile();
		}
		return null;
	}

	@Override
	public void Execute() {
		if(m_current_diagram_document == null){
			return;
		}
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			JOptionPane.showMessageDialog(null, "Empty Diagram", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(m_current_diagram_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");	
			JOptionPane.showMessageDialog(null, "Empty tape", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}
		m_current_diagram_document.ClearConsoleText();
		if(m_current_diagram_document.GetSelectedModule().endsWith(".mt")){
			ExecuteMachine();
		}else if (m_current_diagram_document.GetSelectedModule().endsWith(".dt")){
			ExecuteDiagram();
		}
	}

	private void ExecuteMachine()
	{

		Machine m = new Machine();
		m.logs_.AddLog(new ConsoleLog(m_current_diagram_document.GetConsole()));
		try {
			if( m.loadFromString(m_current_diagram_document.GetModuleText())) {
				Tape tape = new Tape(m_current_diagram_document.GetTape());				
				m.execute(tape);
				tape = null;
				m = null;
				TuringMachinesEditor.SetStatusMessage("Execution finished.\n");
			} 
		} catch (IOException e) {
			e.printStackTrace();
			m = null;
		}
	}

	private void ExecuteDiagram()
	{
		Diagram d = new Diagram();
		d.logs_.AddLog(new ConsoleLog(m_current_diagram_document.GetConsole()));
		d.setModuleFilesFullPath(m_current_diagram_document.GetModulesPath());
		d.setModulesContent(m_current_diagram_document.GetModulesContent());
		try {
			if( d.loadFromString(m_current_diagram_document.GetModuleText()) ) {
				//This clear here is to erase the "loading messages" that may be on the log.
				//This way, only the execution messages are displayed when the diagram executes
				d.ClearLogsList(); 					
				Tape tape = new Tape(m_current_diagram_document.GetTape());												
				d.execute(tape);	
				tape = null;
				d = null;
				TuringMachinesEditor.SetStatusMessage("Execution finished.\n");
			} 
		} catch (IOException e) {
			e.printStackTrace();
			d = null;
		}
	}

	@Override
	public void Help() {
		HelpDialog help_dialog = new HelpDialog();
		help_dialog.SetHelpContent(StringFileReader.ReadFile(getClass().getResourceAsStream("/help/diagram_help.txt")));
		help_dialog.setVisible(true);
	}

	@Override
	public void Examples() {
		ExamplesDialog examples_dialog = new ExamplesDialog();
		examples_dialog.setModal(true);
		int result = examples_dialog.showDialog(); //If result equals to 1, the user confirmed the dialog			
		if(result > 0){
			OpenExample(examples_dialog.GetSelectedExample(), examples_dialog.GetSelectedExamplePath());		
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
	public void HandleKeyEvents(KeyEvent e) {
		if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_M){
			m_current_diagram_document.AddNewModule();
			return;
		}
		if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_K){
			m_current_diagram_document.RemoveModule();
			return;
		}
		if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_P){
			m_current_diagram_document.AddPreDefinedModule();
			return;
		}
		if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_L){
			m_current_diagram_document.ExportAllModules();
			return;
		}
		m_current_diagram_document.HandleKeyEvents(e);
	}
}
