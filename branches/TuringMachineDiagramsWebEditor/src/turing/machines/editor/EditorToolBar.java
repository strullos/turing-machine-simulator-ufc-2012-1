package turing.machines.editor;

import java.awt.Dimension;
import java.awt.event.ActionListener;
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
	private JButton m_examples_button;	

	public EditorToolBar(ActionListener new_action_listener,
			ActionListener open_action_listener,
			ActionListener save_action_listener,
			ActionListener save_as_action_listener,
			ActionListener execute_action_listener,
			ActionListener perspective_changed_action_listener,
			ActionListener help_action_listener,
			ActionListener examples_action_listener)
	{
		JButton new_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-new-7.png")));
		new_button.setToolTipText("New");
		
		this.add(new_button);
		JButton open_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-open-7.png")));		
		open_button.setToolTipText("Open");
		
		this.add(open_button);		
		JButton save_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-save-5.png")));
		save_button.setToolTipText("Save");
		
		this.add(save_button);
		
		JButton save_as_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/document-save-as-6.png")));		
		save_as_button.setToolTipText("Save As");
		
		add(save_as_button);
		
		this.addSeparator();
		JButton execute_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/media-playback-start-3.png")));
		execute_button.setToolTipText("Execute");
		
		this.add(execute_button);		
		JLabel perspective_label = new JLabel("Perspective: ");
		this.add(perspective_label);
		m_perspectives_combobox = new JComboBox<String>();	
		m_perspectives_combobox.setMaximumSize(new Dimension(250,100));
		
		this.add(m_perspectives_combobox);
		
		m_examples_button = new JButton("Examples");
		add(m_examples_button);
		m_examples_button.addActionListener(examples_action_listener);
		this.addSeparator();
		
		this.setFloatable(false);			
		
		JButton help_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/toolbar/help.png")));
		help_button.setToolTipText("Help");		
		add(help_button);		
		help_button.addActionListener(help_action_listener);
		
		new_button.addActionListener(new_action_listener);
		open_button.addActionListener(open_action_listener);
		save_button.addActionListener(save_action_listener);
		save_as_button.addActionListener(save_as_action_listener);
		execute_button.addActionListener(execute_action_listener);
		m_perspectives_combobox.addActionListener(perspective_changed_action_listener);
	}	
	
	public String GetCurrentPerspective()
	{
		if(m_perspectives_combobox.getSelectedIndex() != -1){
			return (String) m_perspectives_combobox.getSelectedItem();
		}
		return null;		
	}
	
	public void AddPerspective(String new_perspective)
	{
		m_perspectives_combobox.addItem(new_perspective);			
	}	
	
	public void SetExampleButtonEnabled(boolean enabled)
	{
		m_examples_button.setEnabled(enabled);
	}
}
