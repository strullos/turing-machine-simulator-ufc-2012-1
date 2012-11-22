package turing.machines.editor.perspectives;

import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import ui.utils.ConsoleComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;


public class MachineTextDocument extends ModuleTextDocument {	
	/**
	 * 
	 */	
	
	private static final long serialVersionUID = 1L;
	private String m_machine_document_path;
	public MachineTextDocument()
	{
		m_machine_document_path = "";
		m_console = new ConsoleComponent();
		m_module_input = new TextEditComponent("Machine:");
		m_tape_input = new LineEditComponent("Tape:");
		
		setLayout(new BorderLayout(0, 0));
		JSplitPane machine_text_editor_splitPane = new JSplitPane();
		machine_text_editor_splitPane.setOneTouchExpandable(true);
		add(machine_text_editor_splitPane);
		machine_text_editor_splitPane.setDividerLocation(500);
		
		machine_text_editor_splitPane.setRightComponent(m_console);
		machine_text_editor_splitPane.setDividerLocation(500);		
		machine_text_editor_splitPane.setLeftComponent(m_module_input);			
		
		add(m_tape_input, BorderLayout.NORTH);		
	}	
	
	public String GetMachineDocumentPath()
	{
		return m_machine_document_path;
	}
	
	public void SetMachineDocumentPath(String path)
	{
		m_machine_document_path = path;
	}
}
