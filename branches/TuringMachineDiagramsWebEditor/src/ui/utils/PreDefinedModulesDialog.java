package ui.utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
		BuildModulesList("../pre_defined_modules");
		this.setTitle("Pre-defined Modules");
	}
	
	public int showDialog()
	{
		setVisible(true);
		return m_result;
	}
	
	public void BuildModulesList(String path)
	{
		File directory = new File(path);
		File[] file_list = directory.listFiles();
		for(int i = 0; i < file_list.length; i++){
			File current_file = file_list[i];
			if(current_file.getName().endsWith(".mt")  || current_file.getName().endsWith(".dt")){
				AddModule(current_file.getName(), current_file.getAbsolutePath());
				ReadModuleContent(current_file, current_file.getName());
			}
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
	
	public void AddModule(String module_name, String module_path)
	{
		m_modules_content.put(module_name,  "");
		m_modules_path.put(module_name, module_path);
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_modules_list.getModel();
		list_model.addElement(module_name);		
	}
	
	public void ReadModuleContent(File module_file, String module_name)
	{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(module_file.getAbsolutePath()));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		String line;
		String content = "";
		try {
			while( (line = reader.readLine()) != null ){
				content += line + "\n";
			}
			m_modules_content.put(module_name,content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}		
	}
	
	class SelectionChangedActionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			PreDefinedModulesDialog.this.m_modules_textArea.setText(m_modules_content.get(m_modules_list.getSelectedValue()));			
		}		
	}
}
