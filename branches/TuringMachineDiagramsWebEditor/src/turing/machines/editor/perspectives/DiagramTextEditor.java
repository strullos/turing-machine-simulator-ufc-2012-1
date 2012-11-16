package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import turing.simulator.module.Diagram;
import turing.simulator.tape.Tape;
import ui.utils.ClosableTabComponent;

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
		if(m_diagrams_tabbedPane.getComponentCount() > 0){
			m_diagrams_tabbedPane.addTab("New Diagram" + m_diagrams_tabbedPane.getComponentCount(), null, m_current_diagram_document, null);		
		}else{
			m_diagrams_tabbedPane.addTab("New Diagram", null, m_current_diagram_document, null);		
		}
		m_diagrams_tabbedPane.setSelectedComponent(diagram_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
	}

	@Override
	public void New() 
	{
		NewDiagramDocument();
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
					m_diagrams_tabbedPane.addTab(fc.getSelectedFile().getName().toString(), null, new_diagram_document, null);	
					m_diagrams_tabbedPane.setSelectedComponent(new_diagram_document);
					m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
					m_current_diagram_document.SetModuleText(diagram_text);
					m_current_diagram_document.AppendConsoleText("Diagram file loaded successfully.\n");
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
						m_current_diagram_document.AddRequiredModule(module_file.getName(), module_file.getPath());
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
			m_current_diagram_document.SetConsoleText("Error saving diagram file: empty diagram.\n");
		}else{
			JFileChooser fc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Diagram files (.dt)", "dt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			String file_path;
			if(returnVal == JFileChooser.APPROVE_OPTION){						
				file_path = fc.getSelectedFile().getAbsolutePath().toString() + ".dt";
				FileWriter fstream; 
				try {
					fstream = new FileWriter(file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(m_current_diagram_document.GetModuleText());
					out.close();
					m_current_diagram_document.SetConsoleText("Diagram file saved succesfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}						
			}		
		}
	}

	@Override
	public void Execute() {
		Diagram d = new Diagram();
		m_current_diagram_document.SetConsoleText("");
		d.setModuleFilesFullPath(m_current_diagram_document.GetModulesPath());
		boolean empty_fields = false;
		
		if(m_current_diagram_document.GetModuleText().isEmpty()){
			m_current_diagram_document.SetConsoleText("Error executing: empty diagram.\n");
			empty_fields = true;
		}
		if(m_current_diagram_document.GetTape().isEmpty()){
			m_current_diagram_document.AppendConsoleText("Error executing: empty tape.\n");		
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

}
