package ui.utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ItemListComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> m_items_list;
	private JTextArea m_viewer_textArea;
	private JButton m_add_button;
	private JButton m_remove_button;

	public ItemListComponent(String label)
	{
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblLabel = new JLabel(label);
		add(lblLabel, BorderLayout.NORTH);
		
		JSplitPane items_splitPane = new JSplitPane();
		items_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(items_splitPane, BorderLayout.CENTER);
		
		m_items_list = new JList<String>();
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
	}
}
