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
import javax.swing.JScrollPane;

import utils.StringFileReader;

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
		BuildExamplesList();
		this.setTitle("Examples");
	}
	
	public int showDialog()
	{
		setVisible(true);
		return m_result;
	}
	
	public void BuildExamplesList()
	{
		String examples_path = "/examples/";
		ArrayList<String> files_list = StringFileReader.GetLineArrayFromStream(getClass().getResourceAsStream(examples_path + "index.txt"));
		Iterator<String> files_it = files_list.iterator();
		while(files_it.hasNext()){
			String example_name = files_it.next();
			String example_path = examples_path + example_name;			
			AddExample(example_name, example_path);
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
	
	public void AddExample(String example_name, String example_path)
	{
		String diagram_name = example_name + ".dt";
		String example_filename =  example_path + "/" + diagram_name;	
		m_examples_path.put(diagram_name, example_path + "/" + example_name + ".dt");
		DefaultListModel<String> list_model = (DefaultListModel<String>) m_examples_list.getModel();
		list_model.addElement(diagram_name);	
		
		String example_content = StringFileReader.ReadFile(getClass().getResourceAsStream(example_filename));
		m_examples_content.put(diagram_name, example_content);
	}
	
	
	class SelectionChangedActionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ExamplesDialog.this.m_example_textArea.setText(m_examples_content.get(m_examples_list.getSelectedValue()));			
		}		
	}
}
