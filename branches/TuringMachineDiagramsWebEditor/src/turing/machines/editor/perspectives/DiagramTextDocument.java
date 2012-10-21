package turing.machines.editor.perspectives;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;

import ui.utils.ConsoleComponent;
import ui.utils.ItemListComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;

public class DiagramTextDocument extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConsoleComponent m_console;
	private TextEditComponent m_diagram_input;
	private LineEditComponent m_tape_input;
	private ItemListComponent m_modules_list;
	
	private HashMap<String, String> m_modules_path;	
	public DiagramTextDocument() {
		setLayout(new BorderLayout(0, 0));
		
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_diagram_input = new TextEditComponent("Diagram:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", new AddModuleListener(), new RemoveModuleListener(), new ModuleSelectionChangedListener());
		
	
		add(m_tape_input, BorderLayout.NORTH);		
		
		JSplitPane diagram_editor_splitPane = new JSplitPane();
		add(diagram_editor_splitPane, BorderLayout.CENTER);
		diagram_editor_splitPane.setDividerLocation(500);
		
		
		diagram_editor_splitPane.setLeftComponent(m_diagram_input);
		
		JTabbedPane console_and_modules_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		diagram_editor_splitPane.setRightComponent(console_and_modules_tabbedPane);		

		console_and_modules_tabbedPane.addTab("Console", null, m_console, null);		
		console_and_modules_tabbedPane.addTab("Modules List", null, m_modules_list, null);
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

}
