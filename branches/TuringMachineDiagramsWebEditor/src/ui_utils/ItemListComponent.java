package ui_utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class ItemListComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> m_items_list;
	private JButton m_add_button;
	private JButton m_remove_button;
	private ActionListener m_selection_listener;
	private String m_previous_selection;
	private JButton m_save_all_button;
	private JButton m_pre_defined_modules_button;

	public ItemListComponent(String label, ActionListener new_item_listener, 
			ActionListener add_listener, 
			ActionListener remove_listener, 
			ActionListener selection_listener,
			ActionListener save_all_listener,
			ActionListener pre_defined_listener)
	{
		m_previous_selection = new String("");
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
		
		
		m_add_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/list-add.png")));
		m_add_button.setToolTipText("Add existing module");
		m_add_button.setMinimumSize(new Dimension(30, 30));
		m_add_button.setMaximumSize(new Dimension(30, 30));
		buttons_panel.add(m_add_button);
		
		
		m_remove_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/list-remove.png")));
		m_remove_button.setToolTipText("Remove selected module");
		m_remove_button.setMinimumSize(new Dimension(30, 30));
		m_remove_button.setMaximumSize(new Dimension(30, 30));
		buttons_panel.add(m_remove_button);		
	
		m_remove_button.setEnabled(false);
		
		m_save_all_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/document-save-all.png")));
		m_save_all_button.setToolTipText("Save all modules to a folder");
		m_save_all_button.setMinimumSize(new Dimension(30, 30));
		m_save_all_button.setMaximumSize(new Dimension(30, 30));
		
		m_save_all_button.setEnabled(true);
		buttons_panel.add(m_save_all_button);
		
		m_pre_defined_modules_button = new JButton(new ImageIcon(getClass().getResource("/resources/icons/applications-other.png")));
		m_pre_defined_modules_button.setToolTipText("Import pre-defined modules");
	
		m_pre_defined_modules_button.setMinimumSize(new Dimension(30, 30));
		m_pre_defined_modules_button.setMaximumSize(new Dimension(30, 30));
		m_pre_defined_modules_button.setEnabled(true);
		buttons_panel.add(m_pre_defined_modules_button);
		
//		m_items_list.setCellRenderer(new ColoredListCellRenderer());
		
		m_add_button.addActionListener(add_listener);
		m_remove_button.addActionListener(remove_listener);
		m_save_all_button.addActionListener(save_all_listener);
		m_pre_defined_modules_button.addActionListener(pre_defined_listener);
	}
	
	public void SetSelectedItem(int index)
	{
		m_items_list.setSelectedIndex(0);
	}
	
	public String GetSelectedItem()
	{
		String item =  m_items_list.getSelectedValue();
		if(item != null){
			if(item.startsWith("*"))
			{
				return item.substring(1);
			}
		}		
		return item;
	}
	
	//This method can be called in the SelectionChanged listener method
	//to get the item that was previously selected
	//The previous value is updated to the current selection in the end of the listener method
	public String GetPreviousSelectedItem()
	{
		return m_previous_selection;
	}
	
	public void AddItem(String item)
	{	
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
		int pos = list_model.getSize();
		list_model.add(pos, item);					
		m_items_list.setSelectedValue(item, true);
		if(m_previous_selection == ""){
			m_previous_selection = item;
		}		
		
	}	
	
	public void RemoveSelectedItem()
	{
		int index;
		if((index = m_items_list.getSelectedIndex()) != -1){
			m_items_list.clearSelection();
			DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
			list_model.remove(index);			
		}		
	}
	
	public void MarkSelectedItem()
	{
		String item = m_items_list.getSelectedValue();
		if(item != null){
			if(item.startsWith("*")){ //Item already marked
				return;
			}
			String marked_item = "*" + item;
			int index;
			if((index = m_items_list.getSelectedIndex()) != -1){
				DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
				list_model.set(index, marked_item);	
			}		
		}
	}
	
	public void RenameSelectedItem(String new_name)
	{
		int index;
		if((index = m_items_list.getSelectedIndex()) != -1){
			DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
			list_model.set(index, new_name);	
		}		
	}
	
	public void UnmarkSelectedItem()
	{
		String item = m_items_list.getSelectedValue();	
		if(!item.startsWith("*")){ //Item is not marked
			return;
		}
		int index;
		if((index = m_items_list.getSelectedIndex()) != -1){
			DefaultListModel<String> list_model = (DefaultListModel<String>) m_items_list.getModel();
			list_model.set(index, item.substring(1));	
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
			if(m_items_list.getModel().getSize() == 0){
				m_save_all_button.setEnabled(false);
			}else{
				m_save_all_button.setEnabled(true);
			}
			m_previous_selection = m_items_list.getSelectedValue();		
			if(m_previous_selection != null && m_previous_selection.startsWith("*")){
				m_previous_selection = m_previous_selection.substring(1);
			}
		}		
	}
}
