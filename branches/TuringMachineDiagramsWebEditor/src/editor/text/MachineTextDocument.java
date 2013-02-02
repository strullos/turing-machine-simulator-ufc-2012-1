package editor.text;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import editor.ModuleTextDocument;

import ui_utils.ConsoleComponent;
import ui_utils.LineEditComponent;
import ui_utils.TextEditComponent;


public class MachineTextDocument extends ModuleTextDocument {	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	private String m_machine_document_path;
	public MachineTextDocument()
	{
		super();
		m_machine_document_path = "";
		console_ = new ConsoleComponent();
		m_module_input = new TextEditComponent("Machine:");
		m_tape_input = new LineEditComponent("Tape:");
		
		setLayout(new BorderLayout(0, 0));
		m_input_output_tabbedPane = new JTabbedPane();		
		m_input_output_tabbedPane.addTab("Machine Input", m_module_input);
		m_input_output_tabbedPane.addTab("Console", console_);
		add(m_input_output_tabbedPane);
		add(m_tape_input, BorderLayout.NORTH);	
		m_module_input.SetFocusOnTextArea();
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
