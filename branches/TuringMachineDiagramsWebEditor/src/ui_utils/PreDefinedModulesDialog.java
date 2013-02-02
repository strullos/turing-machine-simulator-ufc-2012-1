package ui_utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import utils.StringFileReader;
import java.awt.Font;
import java.awt.Color;

public class PreDefinedModulesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<String> m_modules_list;
	private JTextArea m_modules_textArea;
	private HashMap<String,String> m_modules_content;
	private HashMap<String, String> m_modules_path;
	private int m_result;
	/**
	 * Create the dialog.
	 */
	public PreDefinedModulesDialog() {
		m_modules_content = new HashMap<String, String>();
		m_modules_path = new HashMap<String, String>();
		m_result = 0;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JSplitPane splitPane = new JSplitPane();
			contentPanel.add(splitPane);
			{
				m_modules_list = new JList<String>();
				splitPane.setLeftComponent(m_modules_list);
			}
			{
				m_modules_textArea = new JTextArea();
				m_modules_textArea.setForeground(Color.GREEN);
				m_modules_textArea.setBackground(Color.BLACK);
				m_modules_textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 16));
				m_modules_textArea.setEditable(false);
				splitPane.setRightComponent(m_modules_textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						m_result = 1;	
						setVisible(false);
						dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						m_result = 0;		
						setVisible(false);
						dispose();
					}
				});
			}
		}
		m_modules_list.setModel(new DefaultListModel<String>());
		m_modules_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_modules_list.addListSelectionListener(new SelectionChangedActionListener());
		BuildModulesList();
		this.setTitle("Pre-defined Modules");
	}
	
	public int showDialog()
	{
		setVisible(true);
		return m_result;
	}
	
	public void BuildModulesList()
	{
		String pre_defined_modules_path = "/pre_defined_modules/";
		ArrayList<String> files_list = StringFileReader.GetLineArrayFromStream(getClass().getResourceAsStream(pre_defined_modules_path + "index.txt"));
		Iterator<String> files_it = files_list.iterator();
		while(files_it.hasNext()){
			String module_name = files_it.next();
			String module_path = pre_defined_modules_path + module_name;
			String module_content = StringFileReader.ReadFile(getClass().getResourceAsStream(module_path));
			AddModule(module_name, module_content, module_path);
		}
		m_modules_list.setSelectedIndex(0);
	}
	
	public String GetSelectModule()
	{
		return m_modules_list.getSelectedValue();
	}
	
	public String GetSelectedModuleContent()
	{
		return m_modules_content.get(m_modules_list.getSelectedValue());
	}
	
	public void AddModule(String module_name, String module_content, String module_path)
	{
		m_modules_content.put(module_name,  module_content);
		m_modules_path.put(module_name, module_path);
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_modules_list.getModel();
		list_model.addElement(module_name);		
	}
	
	class SelectionChangedActionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			PreDefinedModulesDialog.this.m_modules_textArea.setText(m_modules_content.get(m_modules_list.getSelectedValue()));			
		}		
	}
}
