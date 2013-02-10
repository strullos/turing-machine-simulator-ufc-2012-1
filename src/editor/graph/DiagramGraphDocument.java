package editor.graph;

import editor.ModuleGraphDocument;
import editor.TuringMachinesEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui_utils.DiagramGraphControlComponent;
import ui_utils.ItemListComponent;
import ui_utils.PreDefinedModulesDialog;

public class DiagramGraphDocument extends ModuleGraphDocument {
	private static final long serialVersionUID = 1L;
	private ItemListComponent m_modules_list;	
	private HashMap<String, String> m_modules_path;	
	private HashMap<String, String> m_modules_content;
	public DiagramGraphDocument()
	{
		super();
		m_modules_path = new HashMap<String, String>();
		m_modules_content = new HashMap<String, String>();
		
		m_modules_list = new ItemListComponent("Modules:", 
				new NewModuleListener() , 
				new AddModuleListener(), 
				new RemoveModuleListener(), 
				new ModuleSelectionChangedListener(),
				new SaveAllListener(),
				new PreDefinedModulesListener());
		
		m_input_output_tabbedPane.addTab("Diagram Graph", m_graph_splitPane);
		m_input_output_tabbedPane.addTab("Console", m_console);
		
		m_graph_controls = new DiagramGraphControlComponent(m_graph, m_modules_path, m_modules_content);
		m_graph_splitPane.setOneTouchExpandable(true);
		m_graph_splitPane.setDividerLocation(325);	
		m_graph_splitPane.setLeftComponent(m_graph_controls);	
		m_graph_splitPane.setRightComponent(m_graph.GetGraphComponent());
		add(m_input_output_tabbedPane, BorderLayout.CENTER);
	}
	
	public String ConvertGraphToModule()
	{
		return m_graph.GenerateTuringDiagram();
	}

	public void AddModule(String file_name, String file_path)
	{
		if(m_modules_path.containsKey(file_name)){
			m_console.AppendText("Already contains a " + file_name + " module. Duplicates are not allowed.");
			return;
		}
		m_modules_path.put(file_name, file_path);
		m_modules_list.AddItem(file_name);		
		m_console.AppendText("Module " + file_name + " added successfully.\n");		
		m_graph_controls.Update();
	}

	public HashMap<String,String> GetModulesPath()
	{
		return m_modules_path;
	}
	
	public HashMap<String,String> GetModulesContent()
	{
		return m_modules_content;
	}
	
	public boolean CheckDuplicatedModule(String module_name)
	{
		return m_modules_content.containsKey(module_name);
	}

	class NewModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
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
				DiagramGraphDocument.this.AddModule(file_name, file_path);
			}
		}

	}

	class RemoveModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_graph_controls.SetAddNodeButtonEnabled((m_modules_list.GetItemsCount() > 0));
			m_graph_controls.Update();
		}
	}

	class ModuleSelectionChangedListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {

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
				DiagramGraphDocument.this.m_modules_content.put(module_name, content);	
				DiagramGraphDocument.this.AddModule(module_name, "");
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
}
