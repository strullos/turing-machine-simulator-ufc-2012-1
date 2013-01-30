package editor.graph;

import graph.Graph;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui_utils.ConsoleComponent;
import ui_utils.DiagramGraphControlComponent;
import ui_utils.ItemListComponent;
import ui_utils.LineEditComponent;

public class DiagramGraphDocument extends JPanel {
	private static final long serialVersionUID = 1L;
	private ItemListComponent m_modules_list;	
	private HashMap<String, String> m_modules_path;
	private JTabbedPane m_console_and_modules_tabbedPane;	
	private ConsoleComponent m_console;	
	private LineEditComponent m_tape_input;
	private DiagramGraphControlComponent m_graph_controls;

	Graph m_graph;
	public DiagramGraphDocument()
	{
		setLayout(new BorderLayout(0, 0));
		m_graph = new Graph();
		m_modules_path = new HashMap<String, String>();
		m_tape_input = new LineEditComponent("Tape:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:", 
				new NewModuleListener() , 
				new AddModuleListener(), 
				new RemoveModuleListener(), 
				new ModuleSelectionChangedListener(), 
				null, 
				null);


		add(m_tape_input, BorderLayout.NORTH);		

		JSplitPane diagram_editor_splitPane = new JSplitPane();
		diagram_editor_splitPane.setOneTouchExpandable(true);
		add(diagram_editor_splitPane, BorderLayout.CENTER);
		diagram_editor_splitPane.setDividerLocation(750);		

		m_console_and_modules_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		diagram_editor_splitPane.setRightComponent(m_console_and_modules_tabbedPane);		
		diagram_editor_splitPane.setLeftComponent(m_graph.GetGraphComponent());

		m_graph_controls = new DiagramGraphControlComponent(m_graph, m_modules_list);
		m_console_and_modules_tabbedPane.addTab("Outline",null, m_graph_controls, null);
		m_console_and_modules_tabbedPane.addTab("Modules List", null, m_modules_list, null);
		m_console_and_modules_tabbedPane.addTab("Console", null, m_console, null);	
		
		m_graph_controls.SetAddNodeButtonEnabled(false);
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
		//m_graph_controls.AddModuleToList(file_name);
	}	

	class NewModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

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
				DiagramGraphDocument.this.AddRequiredModule(file_name, file_path);
			}
			m_graph_controls.SetAddNodeButtonEnabled((m_modules_list.GetItemsCount() > 0));
			m_graph_controls.UpdateModulesComboBox();
		}

	}

	class RemoveModuleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			m_graph_controls.SetAddNodeButtonEnabled((m_modules_list.GetItemsCount() > 0));
			m_graph_controls.UpdateModulesComboBox();
		}
		//
		//		@Override
		//		public void actionPerformed(ActionEvent e) {
		//			String selected_module = m_modules_list.GetSelectedItem();
		//			m_modules_path.remove(selected_module);			
		//		}
		//		
		
	}

	class ModuleSelectionChangedListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

		//		@Override
		//		public void actionPerformed(ActionEvent e) {
		//			DiagramTextDocument.this.ReadSelectedModule(m_modules_list.GetSelectedItem());			
		//		}

	}
}
