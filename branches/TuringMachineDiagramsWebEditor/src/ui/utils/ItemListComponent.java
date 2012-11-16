package ui.utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;

public class ItemListComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> m_items_list;
	private JButton m_add_button;
	private JButton m_remove_button;
	private ActionListener m_add_listener;
	private ActionListener m_remove_listener;	
	private ActionListener m_selection_listener;
	private JButton m_new_button;

	public ItemListComponent(String label, ActionListener add_listener, ActionListener remove_listener, ActionListener selection_listener)
	{
		m_add_listener = add_listener;
		m_remove_listener = remove_listener;
		m_selection_listener = selection_listener;
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblLabel = new JLabel(label);
		add(lblLabel, BorderLayout.NORTH);
		
		m_items_list = new JList<String>();
		m_items_list.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(m_items_list, BorderLayout.CENTER);
		m_items_list.setModel(new DefaultListModel<String>());
		m_items_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		m_items_list.addListSelectionListener(new SelectionChangedListener());
		
		JPanel buttons_panel = new JPanel();
		buttons_panel.setPreferredSize(new Dimension(30, 10));
		buttons_panel.setMaximumSize(new Dimension(30, 32767));
		buttons_panel.setMinimumSize(new Dimension(30, 10));
		add(buttons_panel, BorderLayout.EAST);
		buttons_panel.setLayout(new BoxLayout(buttons_panel, BoxLayout.Y_AXIS));
		
		m_new_button = new JButton("N");
		m_new_button.addActionListener(new NewItemActionListener());
		buttons_panel.add(m_new_button);
		
		m_add_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/list-add.png")));
		m_add_button.setMinimumSize(new Dimension(30, 30));
		m_add_button.setMaximumSize(new Dimension(30, 30));
		buttons_panel.add(m_add_button);
		
		m_add_button.addActionListener(new AddActionListener());
		
		m_remove_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/list-remove.png")));
		m_remove_button.setMinimumSize(new Dimension(30, 30));
		m_remove_button.setMaximumSize(new Dimension(30, 30));
		buttons_panel.add(m_remove_button);		
		m_remove_button.addActionListener(new RemoveActionListener());
		m_remove_button.setEnabled(false);
	}
	
	public String GetSelectedItem()
	{
		return m_items_list.getSelectedValue();
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
		}		
	}
	
	class NewItemActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = (String)JOptionPane.showInputDialog(
                    ItemListComponent.this,
                    "Module name:\n",                  
                    "New Module",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "new_module.dt");		
			if(input != null){
				ItemListComponent.this.AddItem(input);
			}
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
