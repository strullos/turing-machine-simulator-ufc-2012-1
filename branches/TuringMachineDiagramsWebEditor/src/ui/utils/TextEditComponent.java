package ui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class TextEditComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea m_text_input;
	public TextEditComponent(String label) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel text_label = new JLabel(label);
		add(text_label, BorderLayout.NORTH);
		
		m_text_input = new JTextArea();
		m_text_input.setForeground(Color.BLACK);
		m_text_input.setBackground(Color.WHITE);
		m_text_input.setFont(new Font("Dialog", Font.PLAIN, 16));
		add(m_text_input, BorderLayout.CENTER);
	}
	
	public String GetText()
	{
		return m_text_input.getText();
	}
	
	public void SetText(String text)
	{
		m_text_input.setText(text);
	}
	
	public boolean IsEmpty()
	{
		return m_text_input.getText().isEmpty();
	}
}
