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
		
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_module_input = new TextEditComponent("Editor:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", new AddModuleListener(), new RemoveModuleListener(), new ModuleSelectionChangedListener());
		
	
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
		if(!m_modules_path.containsKey(selected_module)){
			return;
		}
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(m_modules_path.get(selected_module)));
			String line;
			try {
				while((line = reader.readLine()) != null){
					module_text += (line + "\n");						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			m_modules_path.remove(selected_module);			
		}
		
	}
	
	class ModuleSelectionChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
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
