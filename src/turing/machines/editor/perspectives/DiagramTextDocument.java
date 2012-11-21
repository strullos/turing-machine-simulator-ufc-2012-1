package turing.machines.editor.perspectives;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSplitPane;

import ui.utils.ConsoleComponent;
import ui.utils.ItemListComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;

public class DiagramTextDocument extends ModuleTextDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ItemListComponent m_modules_list;
	
	private HashMap<String, String> m_modules_path;
	private HashMap<String, String> m_modules_content;
	public DiagramTextDocument() {
		setLayout(new BorderLayout(0, 0));
		
		m_modules_content = new HashMap<String, String>();
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_module_input = new TextEditComponent("Editor:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", new NewModuleListener(),new AddModuleListener(), new RemoveModuleListener(), new ModuleSelectionChangedListener());
		
	
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
				m_console.AppendText("Invalid module extension. Please specify .dt or .mt");
			}else{
				AddRequiredModule(module_name, "");
			}				
		}			
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
	
	public HashMap<String,String> GetModulesPath()
	{
		return m_modules_path;
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
	
	public void AddRequiredModule(String file_name, String file_path)
	{
		//m_console.ClearText();
		if(m_modules_path.containsKey(file_name)){
			m_console.AppendText("Already contains a " + file_name + " module. Duplicates are not allowed.");
			return;
		}
		m_modules_path.put(file_name, file_path);
		m_modules_list.AddItem(file_name);		
		m_console.AppendText("Module " + file_name + " added successfully.\n");		
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
				try {							
					BufferedReader reader = new BufferedReader(new FileReader(m_modules_path.get(selected_module)));
					String line;
					try {
						while((line = reader.readLine()) != null){
							module_text += (line + "\n");						
						}
						this.m_module_input.SetText(module_text);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
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
					m_console.AppendText("Invalid module extension. Please specify .dt or .mt");
				}else{
					DiagramTextDocument.this.AddRequiredModule(input, "");
				}				
			}			
		}
		
	}
	
	class AddModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(new File("."));		
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
				DiagramTextDocument.this.AddRequiredModule(file_name, file_path);
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
				m_console.AppendText("Module: " + selected_module + " removed successfully.\n");
			}		
		}		
	}
	
	class ModuleSelectionChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			DiagramTextDocument.this.UpdateModuleContent(m_modules_list.GetPreviousSelectedItem());
			DiagramTextDocument.this.ReadSelectedModule(m_modules_list.GetSelectedItem());		
			if(m_modules_list.GetSelectedItem() != null){
				DiagramTextDocument.this.m_module_input.SetInputEnabled(true);
			}else{
				DiagramTextDocument.this.m_module_input.SetInputEnabled(false);
				DiagramTextDocument.this.m_module_input.ClearText();
			}
		}
		
	}
}
