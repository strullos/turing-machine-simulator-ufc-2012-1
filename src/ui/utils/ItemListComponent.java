package ui.utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ItemListComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> m_items_list;
	private JTextArea m_viewer_textArea;
	private JButton m_add_button;
	private JButton m_remove_button;
	private ActionListener m_add_listener;
	private ActionListener m_remove_listener;	
	private ActionListener m_selection_listener;

	public ItemListComponent(String label, ActionListener add_listener, ActionListener remove_listener, ActionListener selection_listener)
	{
		m_add_listener = add_listener;
		m_remove_listener = remove_listener;
		m_selection_listener = selection_listener;
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblLabel = new JLabel(label);
		add(lblLabel, BorderLayout.NORTH);
		
		JSplitPane items_splitPane = new JSplitPane();
		items_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(items_splitPane, BorderLayout.CENTER);
		
		m_items_list = new JList<String>();
		m_items_list.setModel(new DefaultListModel<String>());
		m_items_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		items_splitPane.setLeftComponent(m_items_list);
		
		m_viewer_textArea = new JTextArea();
		m_viewer_textArea.setEditable(false);
		items_splitPane.setRightComponent(m_viewer_textArea);
		items_splitPane.setDividerLocation(300);
		
		JPanel buttons_panel = new JPanel();
		add(buttons_panel, BorderLayout.SOUTH);
		buttons_panel.setLayout(new BoxLayout(buttons_panel, BoxLayout.X_AXIS));
		
		m_add_button = new JButton(new ImageIcon("../resources/icons/list-add.png"));
		buttons_panel.add(m_add_button);
		
		m_remove_button = new JButton(new ImageIcon("../resources/icons/list-remove.png"));
		buttons_panel.add(m_remove_button);		
		
		m_add_button.addActionListener(new AddActionListener());
		m_remove_button.addActionListener(new RemoveActionListener());
		m_remove_button.setEnabled(false);
		
		m_items_list.addListSelectionListener(new SelectionChangedListener());
	}
	
	public String GetSelectedItem()
	{
		return m_items_list.getSelectedValue();
	}
	
	public void SetViewerContent(String text)
	{
		m_viewer_textArea.setText(text);
	}
	
	public void AddItem(String item)
	{
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
		int pos = list_model.getSize();
		list_model.add(pos, item);			
		m_items_list.setSelectedValue(item, true);
	}	
	
	private void RemoveSelectedItem()
	{
		int index;
		if((index = m_items_list.getSelectedIndex()) != -1){
			m_items_list.clearSelection();
			DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
			list_model.remove(index);
			m_viewer_textArea.setText("");
		}		
	}
	
	class AddActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {			
			m_add_listener.actionPerformed(e);					
		}
		
	}
	
	class RemoveActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_remove_listener.actionPerformed(e);
			RemoveSelectedItem();
		}
		
	}
	
	class SelectionChangedListener implements ListSelectionListener
	{	
		@Override
		public void valueChanged(ListSelectionEvent e) {
			m_selection_listener.actionPerformed(null);		
			if(m_items_list.getSelectedIndex() != -1){
				ItemListComponent.this.m_remove_button.setEnabled(true);
			}else{
				ItemListComponent.this.m_remove_button.setEnabled(false);
			}
		}
		
	}
}
