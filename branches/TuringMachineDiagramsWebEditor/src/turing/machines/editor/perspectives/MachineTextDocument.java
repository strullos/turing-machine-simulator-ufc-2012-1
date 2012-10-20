package turing.machines.editor.perspectives;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;


public class MachineTextDocument extends JPanel {	
	/**
	 * 
	 */
	private JTextArea console_textArea;
	private JTextArea machine_textArea;
	private JTextField tape_textField;
	private static final long serialVersionUID = 1L;
	public MachineTextDocument()
	{
		setLayout(new BorderLayout(0, 0));
		JSplitPane machine_text_editor_splitPane = new JSplitPane();
		machine_text_editor_splitPane.setOneTouchExpandable(true);
		add(machine_text_editor_splitPane);
		machine_text_editor_splitPane.setDividerLocation(500);
		
		JPanel console_panel = new JPanel();
		console_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		machine_text_editor_splitPane.setRightComponent(console_panel);
		machine_text_editor_splitPane.setDividerLocation(500);
		console_panel.setLayout(new BorderLayout(0, 0));
		
		console_textArea = new JTextArea();
		console_textArea.setEditable(false);
		console_textArea.setFont(new Font("Dialog", Font.PLAIN, 18));
		console_textArea.setForeground(new Color(0, 255, 0));
		console_textArea.setBackground(Color.BLACK);
		console_panel.add(console_textArea);
		
		JLabel console_label = new JLabel("Console:");
		console_panel.add(console_label, BorderLayout.NORTH);
		
		JPanel machine_panel = new JPanel();
		machine_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		machine_text_editor_splitPane.setLeftComponent(machine_panel);
		machine_panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblMachine = new JLabel("Machine:");
		machine_panel.add(lblMachine, BorderLayout.NORTH);
		
		machine_textArea = new JTextArea();
		machine_panel.add(machine_textArea, BorderLayout.CENTER);
		
		JPanel tape_panel = new JPanel();
		tape_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(tape_panel, BorderLayout.NORTH);
		tape_panel.setLayout(new BoxLayout(tape_panel, BoxLayout.X_AXIS));
		
		JLabel tape_label = new JLabel("Tape:");
		tape_panel.add(tape_label);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(5, 0));
		horizontalStrut.setMinimumSize(new Dimension(5, 0));
		horizontalStrut.setMaximumSize(new Dimension(5, 32767));
		tape_panel.add(horizontalStrut);
		
		tape_textField = new JTextField();
		tape_panel.add(tape_textField);
		tape_textField.setColumns(10);		
	}
	
	public String GetMachineText()
	{
		return machine_textArea.getText();
	}
	
	public void SetMachineText(String machine_text)
	{
		machine_textArea.setText(machine_text);
	}
	
	public void SetConsoleText(String console_text)
	{
		console_textArea.setText(console_text);
	}
	
	public void AppendConsoleText(String text)
	{
		console_textArea.append(text);
	}
	
	public void ClearConsoleText()
	{
		console_textArea.setText("");
	}
	
	public String GetTape()
	{
		return tape_textField.getText();
	}

}
