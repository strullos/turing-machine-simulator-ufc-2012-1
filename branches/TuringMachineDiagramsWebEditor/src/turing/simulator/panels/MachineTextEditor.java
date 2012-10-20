package turing.simulator.panels;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import turing.simulator.module.Machine;
import turing.simulator.tape.Tape;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.border.LineBorder;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import java.awt.Dimension;

public class MachineTextEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tapeInput;
	private JTextArea machineInput;
	private JTextArea consoleOutput;

	/**
	 * Create the panel.
	 */
	public MachineTextEditor() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[228.00,grow,left][228.00,grow,right]", "[40.00][197.00,grow,top][]"));
		
		JPanel top = new JPanel();
		add(top, "cell 0 0 2 1,grow");
		top.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		JLabel labelTape = new JLabel("Tape:");
		top.add(labelTape, "cell 0 0,alignx left");
		
		tapeInput = new JTextField();
		top.add(tapeInput, "cell 0 1,growx");
		tapeInput.setColumns(10);
		
		JPanel middle = new JPanel();
		add(middle, "cell 0 1 2 1,grow");
		middle.setLayout(new BoxLayout(middle, BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		middle.add(splitPane);
		
		JPanel editorPanel = new JPanel();
		editorPanel.setPreferredSize(new Dimension(300, 10));
		editorPanel.setMinimumSize(new Dimension(200, 10));
		splitPane.setLeftComponent(editorPanel);
		editorPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel machineLabel = new JLabel("Machine:");
		editorPanel.add(machineLabel, "cell 0 0");
		machineLabel.setForeground(Color.BLACK);
		machineLabel.setBackground(Color.WHITE);
		
		JScrollPane editorScrollPane = new JScrollPane();
		editorPanel.add(editorScrollPane, "cell 0 1,grow");
		
		machineInput = new JTextArea();
		editorScrollPane.setViewportView(machineInput);
		
		JPanel consolePanel = new JPanel();
		consolePanel.setMinimumSize(new Dimension(200, 10));
		splitPane.setRightComponent(consolePanel);
		consolePanel.setBorder(null);
		consolePanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel consoleLabel = new JLabel("Console:");
		consolePanel.add(consoleLabel, "cell 0 0");
		consoleLabel.setBackground(Color.LIGHT_GRAY);
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consolePanel.add(consoleScrollPane, "cell 0 1,grow");
		
		consoleOutput = new JTextArea();
		consoleOutput.setEditable(false);
		consoleOutput.setBackground(new Color(0, 0, 0));
		consoleOutput.setForeground(new Color(0, 255, 0));
		consoleScrollPane.setViewportView(consoleOutput);
		
		JPanel bottom = new JPanel();
		add(bottom, "cell 0 2 2 1,grow");
		bottom.setLayout(new MigLayout("", "[left][105.00,grow,left][grow]", "[]"));
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMachineFile();
			}
		});
		bottom.add(saveButton, "cell 0 0,alignx left");
		
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadMachineFile();
			}
		});
		bottom.add(loadButton, "cell 1 0,alignx left");
		
		JButton executeButton = new JButton("Execute");
		
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				executeButtonAction();
			}
		});
		
		bottom.add(executeButton, "cell 2 0,alignx right");

	}
	
	
	private void executeButtonAction() {
		Machine m = new Machine();
		boolean empty_fields = false;
		consoleOutput.setText("");
		if(machineInput.getText().isEmpty()){
			consoleOutput.setText("Empty machine.\n");
			empty_fields = true;
		}
		if(tapeInput.getText().isEmpty()){
			consoleOutput.append("Empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( m.loadFromString(machineInput.getText())) {
					Tape tape = new Tape(tapeInput.getText());				
					consoleOutput.setText("Executing machine on tape: '" + tape.toString() + "'\n\n");
					consoleOutput.append(m.getCurrentState() + ": " + tape.toString() + "\n");
					while( m.executeStep(tape) ) {
						consoleOutput.append(m.getCurrentState() + ": " + tape.toString() + "\n");
					}
					consoleOutput.append("\nStopped execution on " + Integer.toString(m.getSteps()) + " steps on state " + m.getCurrentState());
				} else {
					consoleOutput.setText("Failed to process rule - error on line " + Integer.toString(m.getLine()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadMachineFile() {
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
						machineInput.append(line);		
						machineInput.append("\n");
					}
					consoleOutput.setText("Machine file loaded successfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveMachineFile() {
		if(machineInput.getText().isEmpty()){
			consoleOutput.setText("Empty machine.");
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
					out.write(machineInput.getText());
					out.close();
					consoleOutput.setText("Machine file saved succesfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}		
		}
	}

}
