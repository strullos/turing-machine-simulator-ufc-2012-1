package ui_utils;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LineEditComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField m_lineEdit_textField;
	
	public LineEditComponent(String label){
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel lineEdit_label = new JLabel(label);
		add(lineEdit_label);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(5, 0));
		horizontalStrut.setMinimumSize(new Dimension(5, 0));
		horizontalStrut.setMaximumSize(new Dimension(5, 32767));
		add(horizontalStrut);
		
		m_lineEdit_textField = new JTextField();
		add(m_lineEdit_textField);
		m_lineEdit_textField.setColumns(10);	
	}
	
	public String GetText()
	{
		return  m_lineEdit_textField.getText();
	}
	
	public void SetFocusOnTextField()
	{
		m_lineEdit_textField.grabFocus();
	}
}
