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
	 * This class implements the toolbar on top of the application.
	 * It contains references to all perspectives and allows the user to change the current perspective at any moment.
	 * Whenever the perspective is changed, the toolbar buttons behaviour may change also (e.g Save a .mt or save .dt)
	 * So, the toolbar buttons always connects with the methods of the current perspective.
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> m_perspectives_combobox;	
	private HashMap<ToolBarListenerType, ActionListener> m_listeners;

	public EditorToolBar()
	{
		m_listeners = new HashMap<ToolBarListenerType, ActionListener>();
		AddToolBarButtons();
		this.setFloatable(false);
	}
	
	public void RegisterListener(ToolBarListenerType type, ActionListener listener)
	{
		m_listeners.put(type, listener);
	}
	
	public String GetCurrentPerspective()
	{
		return (String) m_perspectives_combobox.getSelectedItem();
	}
	
	public void AddPerspective(String new_perspective)
	{
		m_perspectives_combobox.addItem(new_perspective);			
	}
	
	private void AddToolBarButtons()
	{
		JButton new_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-new-7.png")));
		new_button.setToolTipText("New");
		new_button.addActionListener(new NewActionListener());
		this.add(new_button);
		JButton open_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-open-7.png")));		
		open_button.setToolTipText("Open");
		open_button.addActionListener(new OpenActionListener());
		this.add(open_button);		
		JButton save_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-save-5.png")));
		save_button.setToolTipText("Save");
		save_button.addActionListener(new SaveActionListener());
		this.add(save_button);
		
		JButton save_as_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-save-as-6.png")));		
		save_as_button.setToolTipText("Save As");
		save_as_button.addActionListener(new SaveAsActionListener());
		add(save_as_button);
		
		this.addSeparator();
		JButton execute_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/media-playback-start-3.png")));
		execute_button.setToolTipText("Execute");
		execute_button.addActionListener(new ExecuteActionListener());
		this.add(execute_button);		
		JLabel perspective_label = new JLabel("Perspective: ");
		this.add(perspective_label);
		m_perspectives_combobox = new JComboBox<String>();	
		m_perspectives_combobox.setMaximumSize(new Dimension(250,100));
		m_perspectives_combobox.addActionListener(new ComboboxSelectionChangedListener());
		this.add(m_perspectives_combobox);
		this.addSeparator();
	}	
	
	
	class NewActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.NEW_FILE)){
				m_listeners.get(ToolBarListenerType.NEW_FILE).actionPerformed(e);
			}
			
		}
		
	}
	
	class OpenActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.OPEN_FILE)){
				m_listeners.get(ToolBarListenerType.OPEN_FILE).actionPerformed(e);
			}	
		}		
	};
	
	class SaveActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.SAVE_FILE)){
				m_listeners.get(ToolBarListenerType.SAVE_FILE).actionPerformed(e);
			}
			
		}		
	}
	
	class SaveAsActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.SAVE_AS_FILE)){
				m_listeners.get(ToolBarListenerType.SAVE_AS_FILE).actionPerformed(e);
			}
			
		}
		
	}
	
	class ExecuteActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.EXECUTE)){
				m_listeners.get(ToolBarListenerType.EXECUTE).actionPerformed(e);
			}
		}
		
	}
	
	class ComboboxSelectionChangedListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_listeners.containsKey(ToolBarListenerType.PERSPECTIVE_CHANGED)){				
				m_listeners.get(ToolBarListenerType.PERSPECTIVE_CHANGED).actionPerformed(e);
			}	
		}		
	};
	

	
}
