package ui_utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;


public class Dialog extends JDialog {

	/**
	 * 
	 */
	protected int m_result;
	protected boolean m_can_accept = false;
	private static final long serialVersionUID = 1L;
	public Dialog()
	{
		this.getRootPane().registerKeyboardAction(new EscapeButtonListener(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.getRootPane().registerKeyboardAction(new EnterButtonListener(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	public int showDialog()
	{
		setVisible(true);
		return m_result;
	}

	class EnterButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(m_can_accept){
				m_result = 1;	
				setVisible(false);
				dispose();
			}
		}
	}

	class EscapeButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			m_result = 0;	
			setVisible(false);
			dispose();
		}
	}

}
