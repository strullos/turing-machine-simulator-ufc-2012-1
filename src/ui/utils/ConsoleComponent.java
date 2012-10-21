package ui.utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;

public class ConsoleComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea m_console_textArea;

	public ConsoleComponent() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel console_label = new JLabel("Console:");
		add(console_label, BorderLayout.NORTH);
		
		m_console_textArea = new JTextArea();
		m_console_textArea.setColumns(1);
		m_console_textArea.setEditable(false);
		m_console_textArea.setForeground(new Color(0, 255, 0));
		m_console_textArea.setBackground(Color.BLACK);
		m_console_textArea.setFont(new Font("Dialog", Font.PLAIN, 16));
		add(m_console_textArea, BorderLayout.CENTER);
	}
	
	public void SetText(String text)
	{
		m_console_textArea.setText(text);
	}
	
	public void AppendText(String text)
	{
		m_console_textArea.append(text);
	}
}
