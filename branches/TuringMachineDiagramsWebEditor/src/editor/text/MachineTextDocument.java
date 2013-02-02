package editor.text;

import java.awt.BorderLayout;

import editor.ModuleTextDocument;

import ui_utils.TextEditComponent;


public class MachineTextDocument extends ModuleTextDocument {	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	public MachineTextDocument()
	{
		m_document_path = "";
		m_module_input = new TextEditComponent("Machine:");
		setLayout(new BorderLayout(0, 0));
		m_input_output_tabbedPane.addTab("Machine Input", m_module_input);
		m_input_output_tabbedPane.addTab("Console", m_console);
		add(m_input_output_tabbedPane);
		add(m_tape_input, BorderLayout.NORTH);	
		m_module_input.SetFocusOnTextArea();
	}	
}
