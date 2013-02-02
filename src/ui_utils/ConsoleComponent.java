package ui_utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;

public class ConsoleComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea m_console_textArea;
	
	private Font m_font = new Font("DejaVu Sans Mono", Font.PLAIN, 16);
	private float m_current_font_size = 16;
	private float m_font_size_increase = 4;

	public ConsoleComponent() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel console_label = new JLabel("Console:");
		add(console_label, BorderLayout.NORTH);
		
		JScrollPane console_scrollPane = new JScrollPane();
		add(console_scrollPane, BorderLayout.CENTER);
		
		m_console_textArea = new JTextArea();
		console_scrollPane.setViewportView(m_console_textArea);
		m_console_textArea.setColumns(1);
		m_console_textArea.setEditable(false);
		m_console_textArea.setForeground(new Color(0, 255, 0));
		m_console_textArea.setBackground(Color.BLACK);
		m_console_textArea.setFont(m_font);
	}
	
	public void SetText(String text)
	{
		m_console_textArea.setText(text);
	}
	
	public void AppendText(String text)
	{
		m_console_textArea.append(text);
	}	
	
	public void ClearText()
	{
		m_console_textArea.setText("");
	}
	
	public void IncreaseFontSize()
	{
		m_current_font_size+=m_font_size_increase;
		m_font = m_font.deriveFont((float) m_current_font_size);
		m_console_textArea.setFont(m_font);
	}
	
	public void DecreaseFontSize()
	{
		m_current_font_size-=m_font_size_increase;
		m_font = m_font.deriveFont((float) m_current_font_size);
		m_console_textArea.setFont(m_font);
	}
}
