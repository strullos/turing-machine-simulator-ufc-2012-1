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
import javax.swing.JScrollPane;

public class ExamplesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<String> m_examples_list;
	private HashMap<String, String> m_examples_path;
	private HashMap<String, String> m_examples_content;
	private JTextArea m_example_textArea;
	private int m_result;
	/**
	 * Create the dialog.
	 */
	public ExamplesDialog() {
		m_examples_path = new HashMap<String, String>();
		m_examples_content = new HashMap<String, String>();
		
		m_result = 0;
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JSplitPane splitPane = new JSplitPane();
			contentPanel.add(splitPane);
			{
				m_examples_list = new JList<String>();
				splitPane.setLeftComponent(m_examples_list);
			}
			{
				JScrollPane content_scrollPane = new JScrollPane();
				splitPane.setRightComponent(content_scrollPane);
				{
					m_example_textArea = new JTextArea();
					content_scrollPane.setViewportView(m_example_textArea);
				}
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
		m_examples_list.setModel(new DefaultListModel<String>());
		m_examples_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_examples_list.addListSelectionListener(new SelectionChangedActionListener());
		BuildExamplesList("../examples");
		this.setTitle("Examples");
	}
	
	public int showDialog()
	{
		setVisible(true);
		return m_result;
	}
	
	public void BuildExamplesList(String path)
	{
		File directory = new File(path);
		File[] file_list = directory.listFiles();
		for(int i = 0; i < file_list.length; i++){
			File current_dir = file_list[i];
			if(current_dir.isDirectory()){
				String example_file_path = current_dir + "/" + current_dir.getName() + ".dt"; //Example files should have the same name as the folder
				if(new File(example_file_path).exists()){
					AddExample(current_dir.getName(), current_dir.getAbsolutePath());
					ReadExampleContent(current_dir, current_dir.getName());
				}				
			}
		}
		m_examples_list.setSelectedIndex(0);
	}
	
	public String GetSelectedExample()
	{
		return m_examples_list.getSelectedValue();
	}
	
	public String GetSelectedExamplePath()
	{
		return m_examples_path.get(m_examples_list.getSelectedValue());
	}
	
	public String GetSelectedExampleContent()
	{
		return m_examples_content.get(m_examples_list.getSelectedValue());
	}
	
	public void AddExample(String module_name, String module_path)
	{
		m_examples_path.put(module_name, module_path + "/" + module_name + ".dt");
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_examples_list.getModel();
		list_model.addElement(module_name);		
	}
	
	@SuppressWarnings("resource")
	public void ReadExampleContent(File example_path, String example_name)
	{		
		BufferedReader reader;
		try {
			String example_file_path = example_path + "/" + example_name + ".dt";
			reader = new BufferedReader(new FileReader(example_file_path));
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
			m_examples_content.put(example_name,content);
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
			ExamplesDialog.this.m_example_textArea.setText(m_examples_content.get(m_examples_list.getSelectedValue()));			
		}		
	}
}
