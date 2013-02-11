package ui_utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;



public class DiagramNewStateDialog extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField label_textField;
	private JTextField selected_module_textField;
	private String m_selected_module = "";
	private String m_selected_module_path = "";
	private String m_selected_module_content = "";
	
	private JButton m_ok_button;
	private JButton m_cancel_button;
	private JButton m_pre_defined_button;
	private JButton m_add_module_button;
	public DiagramNewStateDialog() {
		super();
		setBounds(100, 100, 387, 178);
		
		JPanel contentPanel = new JPanel();
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel label_panel = new JPanel();
		contentPanel.add(label_panel);
		label_panel.setLayout(new BoxLayout(label_panel, BoxLayout.X_AXIS));
		
		JLabel lblLabel = new JLabel("Label:");
		label_panel.add(lblLabel);
		
		label_textField = new JTextField();
		label_textField.setMaximumSize(new Dimension(2147483647, 25));
		label_panel.add(label_textField);
		label_textField.setColumns(10);
		
		JPanel current_module_panel = new JPanel();
		contentPanel.add(current_module_panel);
		current_module_panel.setLayout(new BoxLayout(current_module_panel, BoxLayout.X_AXIS));
		
		JLabel selected_module_label = new JLabel("Selected Module:");
		current_module_panel.add(selected_module_label);
		
		selected_module_textField = new JTextField();
		selected_module_textField.setEditable(false);
		selected_module_textField.setMaximumSize(new Dimension(2147483647, 25));
		current_module_panel.add(selected_module_textField);
		selected_module_textField.setColumns(10);
		
		JPanel modules_panel = new JPanel();
		contentPanel.add(modules_panel);
		modules_panel.setLayout(new BoxLayout(modules_panel, BoxLayout.X_AXIS));
		
		m_add_module_button = new JButton("+");
		modules_panel.add(m_add_module_button);
		
		m_pre_defined_button = new JButton("Pre-Defined Modules");
		modules_panel.add(m_pre_defined_button);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		modules_panel.add(horizontalGlue_1);
		
		JPanel buttonsPanel = new JPanel();
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		buttonsPanel.add(horizontalGlue);
		
		m_ok_button = new JButton("OK");
		m_ok_button.setPreferredSize(new Dimension(85, 25));
		m_ok_button.setMaximumSize(new Dimension(85, 25));
		m_ok_button.setMinimumSize(new Dimension(85, 25));
		buttonsPanel.add(m_ok_button);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(5, 0));
		buttonsPanel.add(horizontalStrut);
		
		m_cancel_button = new JButton("Cancel");
		m_cancel_button.setMinimumSize(new Dimension(85, 25));
		m_cancel_button.setMaximumSize(new Dimension(85, 25));
		m_cancel_button.setPreferredSize(new Dimension(85, 25));
		buttonsPanel.add(m_cancel_button);
		
		this.setTitle("Add new Diagram State");
		
		m_ok_button.addActionListener(new OkButtonListener());
		m_cancel_button.addActionListener(new CancelButtonListener());
		m_pre_defined_button.addActionListener(new PreDefinedButtonListener());
		m_add_module_button.addActionListener(new AddButtonListener());
		
		m_ok_button.setEnabled(false);
		
		label_textField.getDocument().addDocumentListener(new LabelListener());
	}

	
	
	public String GetLabel()
	{
		return label_textField.getText();
	}
	
	public String GetSelectedModule()
	{
		return m_selected_module;
	}
	
	public String GetSelectedModulePath()
	{
		return m_selected_module_path;
	}
	
	public String GetSelectedModuleContent()
	{
		return m_selected_module_content;
	}
	
	class OkButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_result = 1;	
			setVisible(false);
			dispose();
		}
	}
	
	
	class CancelButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			m_result = 0;		
			setVisible(false);
			dispose();
		}
	}
	
	class AddButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			ConfirmationFileChooser fc = new ConfirmationFileChooser(new File("."));		
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Modules files(.mt or .dt)", "mt", "dt");		
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){			
				m_selected_module = fc.getSelectedFile().getName();
				m_selected_module_path = fc.getSelectedFile().getAbsolutePath().toString();
				selected_module_textField.setText(m_selected_module_path);
				label_textField.grabFocus();
				if(!label_textField.getText().isEmpty()){
					m_ok_button.setEnabled(true);
				}
				m_can_accept = m_ok_button.isEnabled();
			}		
			
		}
	}
	
	class PreDefinedButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			PreDefinedModulesDialog pre_defined_modules = new PreDefinedModulesDialog();
			pre_defined_modules.setModal(true);
			int result = pre_defined_modules.showDialog(); //If result equals to 1, the user confirmed the dialog			
			if(result > 0){
				m_selected_module = pre_defined_modules.GetSelectModule();
				m_selected_module_content = pre_defined_modules.GetSelectedModuleContent();
				m_selected_module_path = "";
				selected_module_textField.setText(m_selected_module);
				label_textField.grabFocus();
				if(!label_textField.getText().isEmpty()){
					m_ok_button.setEnabled(true);
				}
				m_can_accept = m_ok_button.isEnabled();
			}
		}
	}
	
	class LabelListener implements DocumentListener
	{
		@Override
		public void insertUpdate(DocumentEvent e) {
			if(!DiagramNewStateDialog.this.label_textField.getText().isEmpty() 
					&& !DiagramNewStateDialog.this.selected_module_textField.getText().isEmpty()){
				DiagramNewStateDialog.this.m_ok_button.setEnabled(true);
			}else{
				DiagramNewStateDialog.this.m_ok_button.setEnabled(false);
			}
			m_can_accept = m_ok_button.isEnabled();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if(!DiagramNewStateDialog.this.label_textField.getText().isEmpty() 
					&& !DiagramNewStateDialog.this.selected_module_textField.getText().isEmpty()){
				DiagramNewStateDialog.this.m_ok_button.setEnabled(true);
			}else{
				DiagramNewStateDialog.this.m_ok_button.setEnabled(false);
			}
			m_can_accept = m_ok_button.isEnabled();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if(!DiagramNewStateDialog.this.label_textField.getText().isEmpty() 
					&& !DiagramNewStateDialog.this.selected_module_textField.getText().isEmpty()){
				DiagramNewStateDialog.this.m_ok_button.setEnabled(true);
			}else{
				DiagramNewStateDialog.this.m_ok_button.setEnabled(false);
			}
			m_can_accept = m_ok_button.isEnabled();
		}
		
	}
}
