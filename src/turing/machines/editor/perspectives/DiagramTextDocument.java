package turing.machines.editor.perspectives;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSplitPane;

import turing.machines.editor.TuringMachinesEditor;
import ui.utils.ConfirmationFileChooser;
import ui.utils.ConsoleComponent;
import ui.utils.ItemListComponent;
import ui.utils.LineEditComponent;
import ui.utils.PreDefinedModulesDialog;
import ui.utils.TextEditComponent;
import utils.StringFileReader;

public class DiagramTextDocument extends ModuleTextDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ItemListComponent m_modules_list;

	private HashMap<String, String> m_modules_path;
	private HashMap<String, String> m_modules_content;
	private boolean m_mark_unsaved_content;
	public DiagramTextDocument() {
		setLayout(new BorderLayout(0, 0));

		m_mark_unsaved_content = true;
		m_modules_content = new HashMap<String, String>();
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_module_input = new TextEditComponent("Editor:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", new NewModuleListener(),
				new AddModuleListener(), 
				new RemoveModuleListener(), 
				new ModuleSelectionChangedListener(),
				new SaveAllListener(),
				new PreDefinedModulesListener());


		add(m_tape_input, BorderLayout.NORTH);		

		JSplitPane diagram_and_modules_splitPane = new JSplitPane();
		diagram_and_modules_splitPane.setOneTouchExpandable(true);	
		diagram_and_modules_splitPane.setDividerLocation(150);		

		diagram_and_modules_splitPane.setLeftComponent(m_modules_list);		
		diagram_and_modules_splitPane.setRightComponent(m_module_input);	

		JSplitPane diagram_editor_and_console_splitPane = new JSplitPane();
		diagram_editor_and_console_splitPane.setOneTouchExpandable(true);	
		diagram_editor_and_console_splitPane.setDividerLocation(500);
		diagram_editor_and_console_splitPane.setLeftComponent(diagram_and_modules_splitPane);
		diagram_editor_and_console_splitPane.setRightComponent(m_console);
		add(diagram_editor_and_console_splitPane, BorderLayout.CENTER);

		m_module_input.SetInputEnabled(false);

		m_module_input.SetDocumentListener(new ContentChangedListener());
	}	

	public void CreateNewModule()
	{
		String module_name = (String)JOptionPane.showInputDialog(
				this,
				"Module name:\n",                  
				"New Module",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"new_module.mt");		
		if(module_name != null){
			if(!module_name.endsWith(".dt") && !module_name.endsWith(".mt")){						
				TuringMachinesEditor.SetStatusMessage("Invalid module extension. Please specify .dt or .mt");
			}else{
				AddModule(module_name, "");
			}				
		}			
	}

	public void SetMainModuleSelected()
	{
		m_modules_list.SetSelectedItem(0);
	}

	public String GetSelectedModule()
	{
		return m_modules_list.GetSelectedItem();
	}	

	public String GetModulePath(String module_name)
	{
		if(m_modules_path.containsKey(module_name))
		{
			return m_modules_path.get(module_name);
		}else{
			return "";
		}
	}

	public void SetModulePath(String module_name, String module_path)
	{
		m_modules_path.put(module_name, module_path);
	}

	public void RenameModule(String original_module_name, String new_module_name)
	{
		Iterator<Entry<String, String>> it = m_modules_path.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			if(pairs.getKey().equals(original_module_name)){
				pairs.getKey().replaceAll(original_module_name, new_module_name);
			}
		}

		it = m_modules_content.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			if(pairs.getKey().equals(original_module_name)){
				pairs.getKey().replaceAll(original_module_name, new_module_name);
			}
		}
		m_modules_list.RenameSelectedItem(new_module_name);
	}

	public boolean CheckDuplicatedModule(String module_name)
	{
		return m_modules_content.containsKey(module_name);
	}

	public HashMap<String,String> GetModulesPath()
	{
		return m_modules_path;
	}

	public HashMap<String, String> GetModulesContent()
	{
		return m_modules_content;
	}	

	public void MarkSelectedItemAsUnsaved()
	{
		if(m_mark_unsaved_content){
			m_modules_list.MarkSelectedItem();
		}		
	}

	public void UnmarkSelectedItem()
	{
		m_modules_list.UnmarkSelectedItem();
	}


	public boolean CheckRequiredModules(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			tokens.nextToken();
			tokens.nextToken();
			String module_file = tokens.nextToken();
			if(module_file.endsWith(".dt") || module_file.endsWith(".mt")){
				if(!m_modules_path.containsKey(module_file)){
					m_console.AppendText("Warning: module file " + module_file + " is not on the available modules list.\n");
					return false;
				}				
			}			
		}		
		return true;
	}

	public void AddModule(String file_name, String file_path)
	{
		//m_console.ClearText();
		if(m_modules_path.containsKey(file_name)){
			TuringMachinesEditor.SetStatusMessage("Already contains a " + file_name + " module. Duplicates are not allowed.");
			return;
		}		
		m_modules_path.put(file_name, file_path);
		m_modules_list.AddItem(file_name);		
		if(file_path == ""){
			MarkSelectedItemAsUnsaved();
		}
		TuringMachinesEditor.SetStatusMessage("Module " + file_name + " added successfully.\n");		
	}	

	private void ReadSelectedModule(String selected_module)
	{		
		String module_text = "";	
		m_module_input.ClearText();
		if(m_modules_content.containsKey(selected_module)){
			this.m_module_input.SetText(m_modules_content.get(selected_module));
		}else{			
			//If the module was loaded from a file, it should contain a filepath. Then we try to read the file
			//and set the module's content to the content of the file
			//If the filepath is empty, it means that the module was not saved yet
			if( m_modules_path.containsKey(selected_module) && m_modules_path.get(selected_module) != ""){	
				StringFileReader file_reader = new StringFileReader();
				module_text = file_reader.ReadFile(m_modules_path.get(selected_module));
				this.m_module_input.SetText(module_text);
			}
			m_modules_content.put(selected_module,module_text);
		}
	}		

	private void UpdateModuleContent(String module_name)
	{
		if(m_modules_content.containsKey(module_name)){
			m_modules_content.put(module_name, m_module_input.GetText());
		}
	}

	class NewModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String input = (String)JOptionPane.showInputDialog(
					DiagramTextDocument.this,
					"Module name:\n",                  
					"New Module",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"new_module.mt");		
			if(input != null){
				if(!input.endsWith(".dt") && !input.endsWith(".mt")){
					TuringMachinesEditor.SetStatusMessage("Invalid module extension. Please specify .dt or .mt");
				}else{
					DiagramTextDocument.this.AddModule(input, "");
				}				
			}			
		}

	}

	class AddModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));		
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Modules files(.mt or .dt)", "mt", "dt");		
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(null);
			String file_path;
			String file_name;
			if(returnVal == JFileChooser.APPROVE_OPTION){			
				file_name = fc.getSelectedFile().getName();
				file_path = fc.getSelectedFile().getAbsolutePath().toString();
				DiagramTextDocument.this.AddModule(file_name, file_path);
			}			
		}

	}

	class RemoveModuleListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String selected_module = m_modules_list.GetSelectedItem();
			int ret = (int)JOptionPane.showConfirmDialog(
					DiagramTextDocument.this,
					"Are you sure you want to remove module: " + selected_module + "?",                  
					"Alert",
					JOptionPane.OK_CANCEL_OPTION
					);
			if(ret == JOptionPane.OK_OPTION){			
				m_modules_list.RemoveSelectedItem();
				m_modules_path.remove(selected_module);	
				m_modules_content.remove(selected_module);
				TuringMachinesEditor.SetStatusMessage("Module: " + selected_module + " removed successfully.\n");
			}		
		}		
	}

	class SaveAllListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();			
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);		
			boolean overwrite_all = false;
			if(returnVal == JFileChooser.APPROVE_OPTION){	
				String path = fc.getSelectedFile().getAbsolutePath();				   
				Iterator<Entry<String, String>> it = m_modules_content.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pairs = (Entry<String, String>)it.next();
					String file_path = path + "/" + pairs.getKey();
					File f = new File(file_path);
					//This code sections asks the user if he wants to overwrite all the files in the directory
					if(f.exists() && !overwrite_all){				        	
						Object[] options = {"Yes",
								"No",
						"Overwrite All"};
						int result = JOptionPane.showOptionDialog(null,
								"File " + pairs.getKey() + " already exists. Overwrite file?",
								"Overwrite Confirmation Dialog",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[2]);
						if(result == JOptionPane.YES_OPTION){
							//Do nothing and overwrite the file in the code below
						}
						if(result == JOptionPane.NO_OPTION){
							continue;
						}
						if(result == JOptionPane.CANCEL_OPTION){ //Overwrite option
							overwrite_all = true;
						}
					}
					FileWriter fstream; 
					try {
						fstream = new FileWriter(file_path);
						BufferedWriter out = new BufferedWriter(fstream);
						out.write(pairs.getValue());
						out.close();							
					} catch (IOException e1) {
						e1.printStackTrace();
					}				       
				}
				TuringMachinesEditor.SetStatusMessage("All modules exported to " + path);
			}
		}		
	}

	class PreDefinedModulesListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			PreDefinedModulesDialog pre_defined_modules = new PreDefinedModulesDialog();
			pre_defined_modules.setModal(true);
			int result = pre_defined_modules.showDialog(); //If result equals to 1, the user confirmed the dialog			
			if(result > 0){
				String module_name = pre_defined_modules.GetSelectModule();
				if(CheckDuplicatedModule(module_name)){
					TuringMachinesEditor.SetStatusMessage("There is already a module " + module_name + " in the project. Duplicated modules are not allowed.");
					return;
				}				
				String content = pre_defined_modules.GetSelectedModuleContent();
				DiagramTextDocument.this.m_modules_content.put(module_name, content);	
				DiagramTextDocument.this.AddModule(module_name, "");			
			}
		}		
	}

	class ModuleSelectionChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			DiagramTextDocument.this.UpdateModuleContent(m_modules_list.GetPreviousSelectedItem());
			//This is to prevent the module from being marked as unsaved just because the user changed selection (the document listener will be called)
			m_mark_unsaved_content = false;
			DiagramTextDocument.this.ReadSelectedModule(m_modules_list.GetSelectedItem());		
			if(m_modules_list.GetSelectedItem() != null){
				DiagramTextDocument.this.m_module_input.SetInputEnabled(true);
			}else{
				DiagramTextDocument.this.m_module_input.SetInputEnabled(false);
				DiagramTextDocument.this.m_module_input.ClearText();
			}
			m_mark_unsaved_content = true;
		}

	}

	//This listener monitors for any change in the content of the module
	class ContentChangedListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			DiagramTextDocument.this.MarkSelectedItemAsUnsaved();			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			DiagramTextDocument.this.MarkSelectedItemAsUnsaved();						
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			DiagramTextDocument.this.MarkSelectedItemAsUnsaved();					
		}

	}
}
