package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MachineTextEditor extends EditorPerspective {
	private JTextField tape_textField;
	private JTextArea console_textArea;
	private JTextArea machine_textArea;

	public MachineTextEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));
		
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
		
		JSplitPane machine_text_editor_splitPane = new JSplitPane();
		machine_text_editor_splitPane.setOneTouchExpandable(true);
		add(machine_text_editor_splitPane, BorderLayout.CENTER);
		machine_text_editor_splitPane.setDividerLocation(500);
		
		JPanel console_panel = new JPanel();
		console_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		machine_text_editor_splitPane.setRightComponent(console_panel);
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
		
		machine_textArea = new JTextArea();
		machine_textArea.setFont(new Font("Dialog", Font.PLAIN, 16));
		machine_panel.add(machine_textArea);
		
		JLabel machine_label = new JLabel("Machine:");
		machine_panel.add(machine_label, BorderLayout.NORTH);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Open() {
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Machine files (.mt)", "mt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file_path = fc.getSelectedFile().getAbsolutePath().toString();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file_path));
				String line;
				try {
					while( (line = reader.readLine()) != null ){
						machine_textArea.append(line);		
						machine_textArea.append("\n");
					}
					console_textArea.setText("Machine file loaded successfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void Save() {
		if(machine_textArea.getText().isEmpty()){
			console_textArea.setText("Empty machine.");
		}else{
			JFileChooser fc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Machine files (.mt)", "mt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			String file_path;
			if(returnVal == JFileChooser.APPROVE_OPTION){						
				file_path = fc.getSelectedFile().getAbsolutePath().toString() + ".mt";
				FileWriter fstream; 
				try {
					fstream = new FileWriter(file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(machine_textArea.getText());
					out.close();
					console_textArea.setText("Machine file saved succesfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}						
			}		
		}
	}

	@Override
	public void Execute() {
		Machine m = new Machine();
		boolean empty_fields = false;
		console_textArea.setText("");
		if(machine_textArea.getText().isEmpty()){
			console_textArea.setText("Empty machine.\n");
			empty_fields = true;
		}
		if(tape_textField.getText().isEmpty()){
			console_textArea.append("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( m.loadFromString(machine_textArea.getText())) {
					Tape tape = new Tape(tape_textField.getText());				
					console_textArea.setText("Executing machine on tape: '" + tape.toString() + "'\n\n");
					console_textArea.append(m.getCurrentState() + ": " + tape.toString() + "\n");
					while( m.executeStep(tape) ) {
						console_textArea.append(m.getCurrentState() + ": " + tape.toString() + "\n");
					}
					console_textArea.append("\nStopped execution on " + Integer.toString(m.getSteps()) + " steps on state " + m.getCurrentState());
				} else {
					console_textArea.setText("Failed to process rule - error on line " + Integer.toString(m.getLine()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
