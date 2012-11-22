package turing.machines.editor;

import javax.swing.JPanel;

import turing.machines.editor.perspectives.DiagramGraphEditor;
import turing.machines.editor.perspectives.DiagramTextEditor;
import turing.machines.editor.perspectives.MachineTextEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTextField;

public class TuringMachinesEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditorToolBar m_tool_bar;
	private MachineTextEditor m_machine_text_editor;
	private DiagramTextEditor m_diagram_text_editor;
	private DiagramGraphEditor m_diagram_graph_editor;
	private HashMap<String, EditorPerspective> m_perspectives;
	private EditorPerspective m_current_perspective;
	public static JTextField m_status_textField;
	
	public TuringMachinesEditor() {
		setLayout(new BorderLayout(0, 0));
		InstallToolBar();
		m_perspectives = new HashMap<String, EditorPerspective>();	
		
		m_status_textField = new JTextField();
		m_status_textField.setEditable(false);
		add(m_status_textField, BorderLayout.SOUTH);
		m_status_textField.setColumns(10);
		
		m_machine_text_editor = new MachineTextEditor("Machine Text Editor");
		m_diagram_text_editor = new DiagramTextEditor("Diagram Text Editor");
		m_diagram_graph_editor = new DiagramGraphEditor("Diagram Graph Editor");
		
		m_perspectives.put("Machine Text Editor", m_machine_text_editor);
		m_perspectives.put("Diagram Text Editor", m_diagram_text_editor);
		m_perspectives.put("Diagram Graph Editor", m_diagram_graph_editor);		
	
		//m_tool_bar.AddPerspective(m_machine_text_editor.Name());
		m_tool_bar.AddPerspective(m_diagram_text_editor.Name());
		//m_tool_bar.AddPerspective(m_diagram_graph_editor.Name());
		this.add(m_perspectives.get(m_tool_bar.GetCurrentPerspective()));
		
		m_current_perspective = m_diagram_text_editor;
		
		m_tool_bar.RegisterListener(ToolBarListenerType.NEW_FILE, new NewActionListener());
		m_tool_bar.RegisterListener(ToolBarListenerType.OPEN_FILE, new OpenActionListener());
		m_tool_bar.RegisterListener(ToolBarListenerType.SAVE_FILE, new SaveActionListener());
		m_tool_bar.RegisterListener(ToolBarListenerType.SAVE_AS_FILE, new SaveAsActionListener());
		m_tool_bar.RegisterListener(ToolBarListenerType.EXECUTE, new ExecuteActionListener());
		m_tool_bar.RegisterListener(ToolBarListenerType.PERSPECTIVE_CHANGED, new PerspectiveChangedListener());
	}
	
	public static void SetStatusMessage(String message)
	{
		m_status_textField.setText(message);
	}
	
	private void InstallToolBar()
	{
		m_tool_bar = new EditorToolBar();
		add(m_tool_bar, BorderLayout.NORTH);
	}
	
	class PerspectiveChangedListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String current_perspective = m_tool_bar.GetCurrentPerspective();
			TuringMachinesEditor.this.remove(m_current_perspective);
			TuringMachinesEditor.this.m_current_perspective = TuringMachinesEditor.this.m_perspectives.get(current_perspective);	
			TuringMachinesEditor.this.add(m_current_perspective);	
			TuringMachinesEditor.this.revalidate();
			TuringMachinesEditor.this.repaint();					
		}		
	}
	
	class NewActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.New();
			
		}
		
	}
	
	class OpenActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.Open();			
		}		
	};
	
	class SaveActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.Save();			
		}
		
	}
	
	class SaveAsActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.SaveAs();			
		}		
	}
	
	class ExecuteActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.Execute();			
		}
		
	}
}
