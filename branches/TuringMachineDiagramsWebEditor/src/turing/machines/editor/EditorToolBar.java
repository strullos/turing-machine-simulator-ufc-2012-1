package turing.machines.editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;



public class EditorToolBar extends JToolBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> m_perspectives_combobox;
	private EditorPerspective m_current_perspective;
	private HashMap<String, EditorPerspective> m_perspectives;

	public EditorToolBar()
	{		
		m_perspectives = new HashMap<String, EditorPerspective>();
		AddToolBarButtons();
		this.setFloatable(false);
	}
	
	public void AddPerspective(EditorPerspective new_perspective)
	{
		m_perspectives_combobox.addItem(new_perspective.Name());	
		m_perspectives.put(new_perspective.Name(), new_perspective);
		SetCurrentPerspective();
	}
	
	private void AddToolBarButtons()
	{
		JButton open_button = new JButton(new ImageIcon("../resources/icons/toolbar/document-open-7.png"));		
		open_button.addActionListener(new OpenActionListener());
		this.add(open_button);		
		JButton save_button = new JButton(new ImageIcon("../resources/icons/toolbar/document-save-5.png"));
		save_button.addActionListener(new SaveActionListener());
		this.add(save_button);
		this.addSeparator();
		JLabel perspective_label = new JLabel("Perspective: ");
		this.add(perspective_label);
		m_perspectives_combobox = new JComboBox<String>();	
		m_perspectives_combobox.setMaximumSize(new Dimension(250,100));
		m_perspectives_combobox.addActionListener(new ComboboxSelectionChangedListener());
		this.add(m_perspectives_combobox);
		this.addSeparator();
		JButton execute_button = new JButton(new ImageIcon("../resources/icons/toolbar/media-playback-start-3.png"));
		execute_button.addActionListener(new ExecuteActionListener());
		this.add(execute_button);		
	}
	
	private void SetCurrentPerspective()
	{
		String selected_perspective = (String) m_perspectives_combobox.getSelectedItem();
		m_current_perspective = m_perspectives.get(selected_perspective);
	}
	
	class OpenActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			EditorToolBar.this.m_current_perspective.Open();			
		}		
	};
	
	class SaveActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			EditorToolBar.this.m_current_perspective.Save();
			
		}
		
	}
	
	class ExecuteActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			EditorToolBar.this.m_current_perspective.Execute();			
		}
		
	}
	
	class ComboboxSelectionChangedListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			EditorToolBar.this.SetCurrentPerspective();			
		}		
	};
	

	
}
