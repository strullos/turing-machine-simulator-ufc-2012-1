package editor.graph;

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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.ConsoleLog;
import core.Diagram;
import core.Tape;

import editor.EditorPerspective;
import editor.TuringMachinesEditor;
import ui_utils.ClosableTabComponent;
import ui_utils.ConfirmationFileChooser;
import ui_utils.HelpDialog;
import utils.StringFileReader;

public class DiagramGraphEditor extends EditorPerspective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DiagramGraphDocument m_current_diagram_graph_document;
	private JTabbedPane m_diagrams_tabbedPane;

	public DiagramGraphEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));

		m_diagrams_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_diagrams_tabbedPane, BorderLayout.CENTER);

		DiagramGraphDocument diagram_document = new DiagramGraphDocument();
		m_current_diagram_graph_document = diagram_document;
		if(m_diagrams_tabbedPane.getComponentCount() > 0){
			m_diagrams_tabbedPane.addTab("New Diagram" + m_diagrams_tabbedPane.getComponentCount(), null, diagram_document, null);		
		}else{
			m_diagrams_tabbedPane.addTab("New Diagram", null, diagram_document, null);		
		}
		m_diagrams_tabbedPane.setSelectedComponent(diagram_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
		m_diagrams_tabbedPane.addChangeListener(new TabChangedListener());
	}

	class TabChangedListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			if( DiagramGraphEditor.this.m_diagrams_tabbedPane.getSelectedIndex() != -1){
				DiagramGraphEditor.this.m_current_diagram_graph_document = (DiagramGraphDocument) DiagramGraphEditor.this.m_diagrams_tabbedPane.getSelectedComponent();
			}else{
				DiagramGraphEditor.this.m_current_diagram_graph_document = null;
			}						
		}		
	}

	@Override
	public void New() {
		DiagramGraphDocument diagram_graph_document = new DiagramGraphDocument();
		m_current_diagram_graph_document = diagram_graph_document;
		if(m_diagrams_tabbedPane.getComponentCount() > 1){
			m_diagrams_tabbedPane.addTab("New Diagram Graph" + m_diagrams_tabbedPane.getComponentCount(), null, diagram_graph_document, null);		
		}else{
			m_diagrams_tabbedPane.addTab("New Diagram Graph", null, diagram_graph_document, null);		
		}		
		m_diagrams_tabbedPane.setSelectedComponent(diagram_graph_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
	}


	@Override
	public void Save() {
		if(m_current_diagram_graph_document == null){
			return;
		}
		String machine_graph_text = m_current_diagram_graph_document.ConvertGraphToModule();
		if(machine_graph_text.isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty diagram graph.");
		}else{
			String diagram_path = m_current_diagram_graph_document.GetDocumentPath();
			String diagram_name = "";
			if(diagram_path.isEmpty()){
				ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Diagram Graph files (.gdt)", "gdt");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){	
					diagram_path = fc.getSelectedFile().getAbsolutePath().toString();
					if(!diagram_path.endsWith(".gdt")){
						diagram_path = diagram_path + ".gdt";
					}
					diagram_name = fc.getSelectedFile().getName();
					if(!diagram_name.endsWith(".gdt")){
						diagram_name = diagram_name + ".gdt";
					}
					m_current_diagram_graph_document.SetDocumentPath(diagram_path);
				}else{
					return;
				}
			}			
			FileWriter fstream; 
			try {
				fstream = new FileWriter(diagram_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(m_current_diagram_graph_document.GetGraph().ExportGraph());
				out.close();
				TuringMachinesEditor.SetStatusMessage("Diagram Graph file " + diagram_path + " saved succesfully.\n");
				if(!diagram_name.isEmpty()){ //If machine name is empty the file was already saved so we do not need to save it again
					m_diagrams_tabbedPane.setTitleAt(m_diagrams_tabbedPane.getSelectedIndex(), diagram_name);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public void Execute() {
		if(m_current_diagram_graph_document == null){
			return;
		}
		m_current_diagram_graph_document.ClearConsoleText();
		if(m_current_diagram_graph_document.ConvertGraphToModule().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty Diagram Graph.\n");
			JOptionPane.showMessageDialog(null, "Empty Diagram Graph", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(m_current_diagram_graph_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			JOptionPane.showMessageDialog(null, "Empty tape", "Error",  JOptionPane.ERROR_MESSAGE);
			return;
		}	
		m_current_diagram_graph_document.GoToConsoleTab();
		Diagram d = new Diagram();
		d.setModuleFilesFullPath(m_current_diagram_graph_document.GetModulesPath());
		d.setModulesContent(m_current_diagram_graph_document.GetModulesContent());
		d.logs_.AddLog(new ConsoleLog(m_current_diagram_graph_document.GetConsole()));
		try {
			if( d.loadFromString(m_current_diagram_graph_document.ConvertGraphToModule())) {
				Tape tape = new Tape(m_current_diagram_graph_document.GetTape());				
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
	public void SaveAs() {
		if(m_current_diagram_graph_document == null){
			return;
		}
		String diagram_graph_text = m_current_diagram_graph_document.ConvertGraphToModule();
		if(diagram_graph_text.isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty diagram graph.");
		}else{
			String diagram_path = m_current_diagram_graph_document.GetDocumentPath();
			String diagram_name = "";
			if(diagram_path.isEmpty()){
				ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Diagram Graph files (.gdt)", "gdt");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){	
					diagram_path = fc.getSelectedFile().getAbsolutePath().toString();
					if(!diagram_path.endsWith(".gdt")){
						diagram_path = diagram_path + ".gdt";
					}
					diagram_name = fc.getSelectedFile().getName();
					if(!diagram_name.endsWith(".gdt")){
						diagram_name = diagram_name + ".gdt";
					}
					m_current_diagram_graph_document.SetDocumentPath(diagram_path);
				}else{
					return;
				}
			}			
			FileWriter fstream; 
			try {
				String diagram_content = m_current_diagram_graph_document.GetGraph().ExportGraph();
				fstream = new FileWriter(diagram_path);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(diagram_content);
				out.close();
				TuringMachinesEditor.SetStatusMessage("Diagram Graph file " + diagram_path + " saved succesfully.\n");
				New();
				if(!diagram_name.isEmpty()){ //If diagram name is empty the file was already saved so we do not need to save it again
					m_diagrams_tabbedPane.setTitleAt(m_diagrams_tabbedPane.getSelectedIndex(), diagram_name);
				}		
				m_current_diagram_graph_document.SetDocumentPath(diagram_path);
				m_current_diagram_graph_document.GetGraph().ImportGraph(diagram_content);
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public void Help() {
		HelpDialog help_dialog = new HelpDialog();
		help_dialog.SetHelpContent(StringFileReader.ReadFile(getClass().getResourceAsStream("/help/diagram_graph_help.txt")));
		help_dialog.setVisible(true);	
	}

	@Override
	public void Open(String path) {
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Diagram Graph files (.gdt)", "gdt");
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
					String diagram_graph_text = new String("");
					while( (line = reader.readLine()) != null ){
						diagram_graph_text += line;		
						diagram_graph_text += "\n";						
					}
					DiagramGraphDocument new_machine_document = new DiagramGraphDocument();
					new_machine_document.GetGraph().ImportGraph(diagram_graph_text);					
					m_diagrams_tabbedPane.addTab(fc.getSelectedFile().getName().toString(), null, new_machine_document, null);	
					m_diagrams_tabbedPane.setSelectedComponent(new_machine_document);
					m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
					diagram_graph_text = null;

					File selected_file = new File(file_path);
					Diagram d = new Diagram();
					d.setLoadPath(selected_file.getParent());
					selected_file = null;
					String diagram_text = new_machine_document.ConvertGraphToModule();
					d.loadFromString(diagram_text);
					
					ArrayList<String> dependencies = d.getDependencies();
					for(int i = 0; i < dependencies.size(); i++)
					{
						File module_file = new File(dependencies.get(i));
						m_current_diagram_graph_document.AddModule(module_file.getName(), module_file.getPath());
						module_file = null;
					}
					if(!d.GetError().isEmpty()){
						TuringMachinesEditor.SetStatusMessage("Error: " +  d.GetError());
					}else{
						TuringMachinesEditor.SetStatusMessage("Diagram graph file loaded successfully.\n");	
					}
					diagram_text = null;
					d = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void Examples() {
		// TODO Auto-generated method stub		
	}


	@Override
	public void HandleKeyEvents(KeyEvent e) {
		m_current_diagram_graph_document.HandleKeyEvents(e);
	}

}
