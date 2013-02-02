package ui_utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;

public class TextEditComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea m_text_input;
	private Font m_font;
	private float m_current_font_size = 16;
	private float m_font_size_increase = 4;
	public TextEditComponent(String label) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel text_label = new JLabel(label);
		add(text_label, BorderLayout.NORTH);
		
		JScrollPane text_input_scrollPane = new JScrollPane();
		add(text_input_scrollPane, BorderLayout.CENTER);
		
		m_text_input = new JTextArea();
		m_text_input.setCaretColor(Color.GREEN);
		text_input_scrollPane.setViewportView(m_text_input);
		m_text_input.setForeground(Color.GREEN);
		m_text_input.setBackground(Color.BLACK);
		m_font = new Font("DejaVu Sans Mono", Font.PLAIN, 16);
		m_text_input.setFont(m_font);
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
	
	public void SetInputEnabled(boolean enabled)
	{
		m_text_input.setEnabled(enabled);
	}
	
	public void ClearText()
	{
		m_text_input.setText("");
	}
	
	public void SetDocumentListener(DocumentListener listener)
	{
		m_text_input.getDocument().addDocumentListener(listener);
	}
	
	public void SetFocusOnTextArea()
	{
		m_text_input.grabFocus();
	}
	
	public void IncreaseFontSize()
	{
		m_current_font_size+=m_font_size_increase;
		m_font = m_font.deriveFont((float) m_current_font_size);
		m_text_input.setFont(m_font);
	}
	
	public void DecreaseFontSize()
	{
		m_current_font_size-=m_font_size_increase;
		m_font = m_font.deriveFont((float) m_current_font_size);
		m_text_input.setFont(m_font);
	}
}
