package ui.utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;

public class HelpDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea m_help_content_textArea;

	/**
	 * Create the dialog.
	 */
	public HelpDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			m_help_content_textArea = new JTextArea();
			m_help_content_textArea.setEditable(false);
			contentPanel.add(m_help_content_textArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton close_button = new JButton("Close");
				close_button.setActionCommand("Close");
				close_button.addActionListener(new CloseActionListener());
				buttonPane.add(close_button);
				getRootPane().setDefaultButton(close_button);
				
			}
		}
	}
	
	public void SetHelpContent(String content)
	{
		m_help_content_textArea.setText(content);
	}
	
	class CloseActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			HelpDialog.this.dispose();
		}
		
	}

}
