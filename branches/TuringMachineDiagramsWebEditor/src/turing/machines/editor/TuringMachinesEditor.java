package turing.machines.editor;

import javax.swing.JPanel;

import turing.machines.editor.perspectives.MachineTextEditor;

import java.awt.BorderLayout;

public class TuringMachinesEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EditorToolBar m_tool_bar;
	MachineTextEditor m_machine_text_editor;
	public TuringMachinesEditor() {
		setLayout(new BorderLayout(0, 0));
		InstallToolBar();		
		m_machine_text_editor = new MachineTextEditor("Machine Text Editor");
		this.add(m_machine_text_editor);
		m_tool_bar.AddPerspective(m_machine_text_editor);
	}
	
	private void InstallToolBar()
	{
		m_tool_bar = new EditorToolBar();
		add(m_tool_bar, BorderLayout.NORTH);
	}
}
