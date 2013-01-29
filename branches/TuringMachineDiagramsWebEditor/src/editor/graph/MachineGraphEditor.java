package editor.graph;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.ConsoleLog;
import core.Machine;
import core.Tape;
import editor.EditorPerspective;
import editor.TuringMachinesEditor;

import ui_utils.ClosableTabComponent;

public class MachineGraphEditor extends EditorPerspective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MachineGraphDocument m_current_machine_graph_document;
	private JTabbedPane m_machines_tabbedPane;

	public MachineGraphEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));

		m_machines_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_machines_tabbedPane, BorderLayout.CENTER);

		MachineGraphDocument machine_document = new MachineGraphDocument();
		if(m_machines_tabbedPane.getComponentCount() > 0){
			m_machines_tabbedPane.addTab("New Graph Machine" + m_machines_tabbedPane.getComponentCount(), null, machine_document, null);		
		}else{
			m_machines_tabbedPane.addTab("New Graph Machine", null, machine_document, null);		
		}
		m_machines_tabbedPane.setSelectedComponent(machine_document);
		m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
		m_current_machine_graph_document = machine_document;
	}

	class TabChangedListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			if( MachineGraphEditor.this.m_machines_tabbedPane.getSelectedIndex() != -1){
				MachineGraphEditor.this.m_current_machine_graph_document = (MachineGraphDocument) MachineGraphEditor.this.m_machines_tabbedPane.getSelectedComponent();
			}else{
				MachineGraphEditor.this.m_current_machine_graph_document = null;
			}						
		}		
	}

	@Override
	public void New() {
		MachineGraphDocument machine_graph_document = new MachineGraphDocument();
		m_current_machine_graph_document = machine_graph_document;
		if(m_machines_tabbedPane.getComponentCount() > 1){
			m_machines_tabbedPane.addTab("New Graph Machine" + m_machines_tabbedPane.getComponentCount(), null, machine_graph_document, null);		
		}else{
			m_machines_tabbedPane.addTab("New Graph Machine", null, machine_graph_document, null);		
		}		
		m_machines_tabbedPane.setSelectedComponent(machine_graph_document);
		m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
	}

	@Override
	public void Open(String path) {
		try {
			m_current_machine_graph_document.m_graph.ImportGraph("v q0 150.0 390.0 true\n" + 
					"v q2 560.0 460.0 false\n" + 
					"v q1 560.0 290.0 false\n" + 
					"e b;a q0 q2 393.71836996037246 494.9835715454741\n" + 
					"e a;> q0 q1 370.0 330.0\n"	);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		JFileChooser fc = new JFileChooser(new File("."));		
		//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		//		        "Machine files (.mt)", "mt");
		//		fc.setFileFilter(filter);
		//		fc.setAcceptAllFileFilterUsed(false);
		//		int returnVal = fc.showOpenDialog(null);
		//		String file_path;
		//		if(returnVal == JFileChooser.APPROVE_OPTION){
		//			file_path = fc.getSelectedFile().getAbsolutePath().toString();
		//			try {
		//				BufferedReader reader = new BufferedReader(new FileReader(file_path));
		//				String line;
		//				try {
		//					String machine_text = new String("");
		//					while( (line = reader.readLine()) != null ){
		//						machine_text += line;		
		//						machine_text += "\n";						
		//					}
		//					MachineTextDocument new_machine_document = new MachineTextDocument();
		//					new_machine_document.SetModuleText(machine_text);
		//					m_machines_tabbedPane.addTab(fc.getSelectedFile().getName().toString(), null, new_machine_document, null);	
		//					m_machines_tabbedPane.setSelectedComponent(new_machine_document);
		//					m_machines_tabbedPane.setTabComponentAt(m_machines_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_machines_tabbedPane));
		//					TuringMachinesEditor.SetStatusMessage("Machine file loaded successfully.\n");
		//				} catch (IOException e) {
		//					e.printStackTrace();
		//				}
		//			} catch (FileNotFoundException e) {
		//				e.printStackTrace();
		//			}
		//		}		
	}

	@Override
	public void Save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void SaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Execute() {
		if(m_current_machine_graph_document == null){
			return;
		}
		Machine m = new Machine();
		m.logs_.AddLog(new ConsoleLog(m_current_machine_graph_document.console()));
		boolean empty_fields = false;
		m_current_machine_graph_document.ClearConsoleText();
		if(m_current_machine_graph_document.GetMachineText().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty machine.\n");
			empty_fields = true;
		}
		if(m_current_machine_graph_document.GetTape().isEmpty()){
			TuringMachinesEditor.SetStatusMessage("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				String machine_text = m_current_machine_graph_document.GetMachineText();
				System.out.println(machine_text);
				if( m.loadFromString(m_current_machine_graph_document.GetMachineText())) {
					Tape tape = new Tape(m_current_machine_graph_document.GetTape());				
					m.execute(tape);
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void Help() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Examples() {
		// TODO Auto-generated method stub

	}

}
