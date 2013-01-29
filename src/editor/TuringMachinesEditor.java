package editor;

import javax.swing.JPanel;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTextField;


import editor.graph.DiagramGraphEditor;
import editor.graph.MachineGraphEditor;
import editor.text.DiagramTextEditor;
import editor.text.MachineTextEditor;

public class TuringMachinesEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditorToolBar m_tool_bar;
	private MachineTextEditor m_machine_text_editor;
	private MachineGraphEditor m_machine_graph_editor;
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
		m_machine_graph_editor = new MachineGraphEditor("Machine Graph Editor");
		m_diagram_text_editor = new DiagramTextEditor("Diagram Text Editor");
		m_diagram_graph_editor = new DiagramGraphEditor("Diagram Graph Editor");
		
		m_perspectives.put("Machine Text Editor", m_machine_text_editor);
		m_perspectives.put("Machine Graph Editor", m_machine_graph_editor);
		m_perspectives.put("Diagram Text Editor", m_diagram_text_editor);
		m_perspectives.put("Diagram Graph Editor", m_diagram_graph_editor);		
	
		
		m_tool_bar.AddPerspective(m_machine_text_editor.Name());
		m_tool_bar.AddPerspective(m_machine_graph_editor.Name());
		m_tool_bar.AddPerspective(m_diagram_text_editor.Name());
		m_tool_bar.AddPerspective(m_diagram_graph_editor.Name());
		this.add(m_perspectives.get(m_tool_bar.GetCurrentPerspective()));
		
		m_current_perspective = m_machine_text_editor;	
		m_tool_bar.SetExampleButtonEnabled(false);
	}
	
	public static void SetStatusMessage(String message)
	{
		m_status_textField.setText(message);
	}
	
	private void InstallToolBar()
	{
		m_tool_bar = new EditorToolBar(new NewActionListener(), 
				new OpenActionListener(), 
				new SaveActionListener(), 
				new SaveAsActionListener(),
				new ExecuteActionListener(), 
				new PerspectiveChangedListener(), 
				new HelpActionListener(),
				new ExamplesActionListener());
		
		add(m_tool_bar, BorderLayout.NORTH);
	}
	
	class PerspectiveChangedListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String current_perspective = m_tool_bar.GetCurrentPerspective();
			if(current_perspective != null && m_current_perspective != null && m_perspectives.get(current_perspective) != m_current_perspective){
				TuringMachinesEditor.this.remove(m_current_perspective);				
				TuringMachinesEditor.this.m_current_perspective = TuringMachinesEditor.this.m_perspectives.get(current_perspective);	
				TuringMachinesEditor.this.add(m_current_perspective);	
				TuringMachinesEditor.this.revalidate();
				TuringMachinesEditor.this.repaint();
			}
			if(m_current_perspective == m_machine_text_editor){
				m_tool_bar.SetExampleButtonEnabled(false);
			}
			if(m_current_perspective == m_diagram_text_editor){
				m_tool_bar.SetExampleButtonEnabled(true);
			}
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
			m_current_perspective.Open("");			
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
	
	class HelpActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.Help();			
		}		
	}
	
	class ExamplesActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_current_perspective.Examples();			
		}
		
	}
}