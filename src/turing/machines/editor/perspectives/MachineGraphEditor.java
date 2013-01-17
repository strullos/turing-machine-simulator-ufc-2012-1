package turing.machines.editor.perspectives;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import turing.machines.editor.EditorPerspective;
import turing.machines.editor.TuringMachinesEditor;
import turing.simulator.log.ConsoleLog;
import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;
import ui.utils.ClosableTabComponent;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Open(String file_path) {
		// TODO Auto-generated method stub
		
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
