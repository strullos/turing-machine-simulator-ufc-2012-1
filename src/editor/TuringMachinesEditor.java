package editor;

import javax.swing.JPanel;


import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyboardDispatched());
	}

	public static void SetStatusMessage(String message)
	{
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		m_status_textField.setText(sdf.format(cal.getTime()) + " - " + message);
		sdf = null;
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
	
	private class KeyboardDispatched implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_N){
				m_current_perspective.New();
				return true;
			}	
			//Execute shortcut
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_R){
				m_current_perspective.Execute();
				return true;
			}	
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_S){
				m_current_perspective.Save();
				return true;
			}
			if(e.isControlDown() && e.isShiftDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_S){
				m_current_perspective.SaveAs();
				return true;
			}
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_O){
				m_current_perspective.Open("");
				return true;
			}
		
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_H){
				m_current_perspective.Help();
				return true;
			}
			if(e.isControlDown() && e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_E){
				m_current_perspective.Examples();
				return true;
			}
			m_current_perspective.HandleKeyEvents(e);			
			return false;
		}
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

			if(m_current_perspective == m_diagram_graph_editor){
				m_tool_bar.SetExampleButtonEnabled(false);
			}
			if(m_current_perspective == m_machine_graph_editor){
				m_tool_bar.SetExampleButtonEnabled(false);
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
