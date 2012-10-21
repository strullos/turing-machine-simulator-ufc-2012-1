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
import javax.swing.JTabbedPane;

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
	public DiagramTextDocument() {
		setLayout(new BorderLayout(0, 0));
		
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_module_input = new TextEditComponent("Diagram:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", new AddModuleListener(), new RemoveModuleListener(), new ModuleSelectionChangedListener());
		
	
		add(m_tape_input, BorderLayout.NORTH);		
		
		JSplitPane diagram_editor_splitPane = new JSplitPane();
		add(diagram_editor_splitPane, BorderLayout.CENTER);
		diagram_editor_splitPane.setDividerLocation(500);
		
		
		diagram_editor_splitPane.setLeftComponent(m_module_input);
		
		JTabbedPane console_and_modules_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		diagram_editor_splitPane.setRightComponent(console_and_modules_tabbedPane);		

		console_and_modules_tabbedPane.addTab("Console", null, m_console, null);		
		console_and_modules_tabbedPane.addTab("Modules List", null, m_modules_list, null);
	}	
	
	public HashMap<String,String> GetModulesPath()
	{
		return m_modules_path;
	}
	
	public boolean CheckRequiredModules(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String module_type = tokens.nextToken();
			tokens.nextToken();
			String module_file = tokens.nextToken();
			if(module_type.equals("diagram") || module_type.equals("machine")){
				if(!m_modules_path.containsKey(module_file)){
					m_console.AppendText("Warning: module file " + module_file + " is not on the available modules list.\n");
					return false;
				}				
			}			
		}		
		return true;
	}
	
	private void AddRequiredModule(String file_name, String file_path)
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
				m_modules_list.SetViewerContent(module_text);
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class ModuleSelectionChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			DiagramTextDocument.this.ReadSelectedModule(m_modules_list.GetSelectedItem());			
		}
		
	}
	
//	private void exportDiagram()
//	{		
//		if(diagramInput.getText().isEmpty()){
//			consoleOutput.setText("Error exporting diagram file: empty diagram.");
//		}else{
//			JFileChooser fc = new JFileChooser(new File("."));
//			FileNameExtensionFilter filter = new FileNameExtensionFilter(
//			        "Diagram file(.dt)", ".dt");
//			fc.setFileFilter(filter);
//			fc.setAcceptAllFileFilterUsed(false);
//			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//			int returnVal = fc.showSaveDialog(null);
//			String file_path;
//			if(returnVal == JFileChooser.APPROVE_OPTION){				
//				String file_directory =  fc.getSelectedFile().getAbsolutePath().toString() + "_contents";				
//				File diagram_folder = new File(file_directory);				
//				String file_name = fc.getSelectedFile().getName() + ".dt";
//				file_path = file_directory + "\\" + file_name;				
//				FileWriter fstream; 				
//				try {
//					diagram_folder.mkdir();						
//					if(exportRequiredModules(file_directory)){
//						fstream = new FileWriter(file_path);
//						BufferedWriter out = new BufferedWriter(fstream);
//						out.write(diagramInput.getText());
//						out.close();				
//						consoleOutput.setText("Diagram file saved succesfully.\n");
//					}else{
//						consoleOutput.setText("Failed to export diagram.\n");
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//						
//			}		
//		}
//	}
//	
//	private boolean exportRequiredModules(String path)
//	{
//		Collection<String> modules = m_modules_path.values();
//		Iterator<String> it = modules.iterator();		
//		while(it.hasNext()){			
//			File source = new File(it.next().toString());
//			File destination = new File(path + "\\" + source.getName());
//			try {
//				Files.copy(source.toPath(), destination.toPath());
//			} catch (IOException e) {
//				e.printStackTrace();
//				consoleOutput.setText("Problem exporting required modules.\n");
//				return false;
//			}			
//		}	
//		consoleOutput.setText("Required modules exported succesfully.\n");
//		return true;
//	}
//	
//	private void importDiagram()
//	{		
//		JFileChooser fc = new JFileChooser(new File("."));		
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//		        "Diagram files (.dt)", "dt");
//		fc.setFileFilter(filter);
//		fc.setAcceptAllFileFilterUsed(false);
//		int returnVal = fc.showOpenDialog(null);
//		String file_path;
//		String file_name;
//		boolean required_modules = true;
//		if(returnVal == JFileChooser.APPROVE_OPTION){
//			reset();
//			file_path = fc.getSelectedFile().getAbsolutePath().toString();
//			file_name = fc.getSelectedFile().getName();
//			try {
//				BufferedReader reader = new BufferedReader(new FileReader(file_path));
//				String line;
//				try {
//					importRequiredModules(file_name, file_path);
//					while( (line = reader.readLine()) != null ){
//						if(!checkRequiredModules(line)){
//							required_modules = false;
//						}
//						diagramInput.append(line);		
//						diagramInput.append("\n");
//					}				
//					consoleOutput.append("Diagram file loaded successfully.\n");
//					if(!required_modules){
//						consoleOutput.append("Some of the required modules are not available," +
//								" diagram won't be able to execute. Consider adding the required modules.\n");
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	//TODO: Import only required modules, not all modules in the path
//	private boolean importRequiredModules(String diagram_name, String path)
//	{
//		String dir;
//		if(path.lastIndexOf("\\") > path.lastIndexOf("/"))		{
//			dir = path.substring(0, path.lastIndexOf("\\"));
//		}else{
//			dir = path.substring(0, path.lastIndexOf("/"));
//		}
//		File directory = new File(dir);
//	    File[] children = directory.listFiles();
//	    if (children != null) {
//	        for (File child : children) {
//	        	String file_name = child.getName();
//	        	String file_path = child.getAbsolutePath();
//	        	if((file_name.endsWith(".mt") || file_name.endsWith(".dt")) && !file_name.equals(diagram_name)){
//	        		addRequiredModule(file_name, file_path);
//	        	}
//	        }
//	    }
//		return true;
//	}
//	
//	private void reset()
//	{
//		m_modules_path.clear();
//		consoleOutput.setText("");
//		diagramInput.setText("");		
//	}	

}
