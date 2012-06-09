package turing.simulator.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;

@SuppressWarnings("serial")
public class ExecuteMachinePanel extends JPanel {
	private JButton executeButton = new JButton("Executar!");
	private JTextArea code = new JTextArea(22, 10);
	private JTextArea output = new JTextArea(22, 10);
	private JTextField input = new JTextField(40);
	private JLabel codeLabel = new JLabel("Quádruplas");
	private JLabel outputLabel = new JLabel("Saída");
	private JLabel inputLabel = new JLabel("Entrada da fita:");
	
	public ExecuteMachinePanel() {
		this.build();
	}
	
	private void build() {
		/** Panels and Frames **/
//		JFrame frame = new JFrame();
//		frame.setSize(640, 480);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		executeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExecuteMachinePanel.this.executeButtonAction();
			}
		});
		
		/** Panels and Frames **/
		JPanel top = new JPanel();
		
		JPanel texts = new JPanel();
		texts.setLayout(new BoxLayout(texts, BoxLayout.X_AXIS));
		
		JPanel textPanelLeft = new JPanel();
		textPanelLeft.setLayout(new BoxLayout(textPanelLeft, BoxLayout.Y_AXIS));
		
		JPanel textPanelRight = new JPanel();
		textPanelRight.setLayout(new BoxLayout(textPanelRight, BoxLayout.Y_AXIS));
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		/************************/

		/******** Widgets *******/
		output.setBackground(Color.BLACK);
		output.setForeground(Color.WHITE);
				
		JScrollPane scroll1 = new JScrollPane(code);
		scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JScrollPane scroll2 = new JScrollPane(output);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/************************/
		
		top.add(inputLabel);
		top.add(input);
		textPanelLeft.add(codeLabel);
		textPanelLeft.add(scroll1);
		textPanelRight.add(outputLabel);
		textPanelRight.add(scroll2);
		texts.add(textPanelLeft);
		texts.add(textPanelRight);
		bottom.add(BorderLayout.WEST, executeButton);
		
//		frame.getContentPane().add(top);
//		frame.getContentPane().add(texts);
//		frame.getContentPane().add(bottom);
		
		this.add(top);
		this.add(texts);
		this.add(bottom);
		
		code.requestFocus();
//		frame.setVisible(true);
	}
	
	private void executeButtonAction() {
		Machine m = new Machine();
		if( m.load(new BufferedReader(new StringReader(code.getText()))) ) {
			Tape tape = new Tape(input.getText());
			
			output.setText("");
			output.setText("Executando máquina na fita '" + tape.toString() + "'\n");
			while( m.executeStep(tape) ) {
				output.append(m.getCurrentState() + ": " + tape.toString() + "\n");
			}
			output.append("\nExecução encerrada em " + Integer.toString(m.getSteps()) + " passos no estado " + m.getCurrentState() + "!");
		}
	}
}
