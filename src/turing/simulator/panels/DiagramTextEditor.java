package turing.simulator.panels;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import turing.simulator.module.Diagram;
import turing.simulator.tape.Tape;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Dimension;

public class DiagramTextEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tapeInput;	
	
	private HashMap<String, String> m_modules_path;	
	
	DefaultListModel<String> modulesListModel;
	private JTextArea consoleOutput;
	private JTextArea diagramInput;
	private JList<String> modulesList;
	private JButton removeModuleButton;
	private JTextArea moduleViewertextArea;

	/**
	 * Create the panel.
	 */
	public DiagramTextEditor() {
		m_modules_path = new HashMap<String,String>();
		modulesListModel = new DefaultListModel<String>();
		
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, "cell 0 0,grow");
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(220, 10));
		leftPanel.setMinimumSize(new Dimension(220, 10));
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new MigLayout("", "[194.00px:n,grow]", "[][grow][]"));
		
		JButton addModuleButton = new JButton("+ Module");
		addModuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addModule();
			}
		});
		
		JSplitPane modulesSplitPane = new JSplitPane();
		modulesSplitPane.setResizeWeight(0.6);
		modulesSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		leftPanel.add(modulesSplitPane, "cell 0 1,grow");		
		
		
		JScrollPane modulesListScrollPane = new JScrollPane();
		modulesSplitPane.setLeftComponent(modulesListScrollPane);
		
		modulesList = new JList<String>();
		modulesListScrollPane.setViewportView(modulesList);
		modulesList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int index = modulesList.getSelectedIndex();
				if(index == -1){
					removeModuleButton.setEnabled(false);
				}else{
					removeModuleButton.setEnabled(true);
					displaySelectedModule();
				}
			}
		});
		modulesList.setModel(modulesListModel);
		modulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		
		JLabel availableModulesLabel = new JLabel("Available Modules:");
		modulesListScrollPane.setColumnHeaderView(availableModulesLabel);
		
		JScrollPane modulesViwerScrollPane = new JScrollPane();
		modulesSplitPane.setRightComponent(modulesViwerScrollPane);
		
		JLabel lblModuleViewer = new JLabel("Module Viewer:");
		modulesViwerScrollPane.setColumnHeaderView(lblModuleViewer);
		
		moduleViewertextArea = new JTextArea();
		moduleViewertextArea.setEditable(false);
		modulesViwerScrollPane.setViewportView(moduleViewertextArea);
		leftPanel.add(addModuleButton, "flowx,cell 0 2,alignx left");
		
		removeModuleButton = new JButton("- Module");
		removeModuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeModule();
			}
		});
		removeModuleButton.setEnabled(false);
		leftPanel.add(removeModuleButton, "cell 0 2,alignx left");				
		
		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new MigLayout("", "[grow]", "[19.00][grow][]"));
		
		JPanel topPanel = new JPanel();
		rightPanel.add(topPanel, "cell 0 0,grow");
		topPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
		
		JLabel tapeLabel = new JLabel("Tape:");
		topPanel.add(tapeLabel, "cell 0 0");
		
		tapeInput = new JTextField();
		topPanel.add(tapeInput, "cell 0 1,growx");
		tapeInput.setColumns(10);
		
		JPanel middlePanel = new JPanel();
		rightPanel.add(middlePanel, "cell 0 1,grow");
		middlePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JSplitPane editorSplitPane = new JSplitPane();
		editorSplitPane.setResizeWeight(0.2);
		middlePanel.add(editorSplitPane, "flowx,cell 0 0,grow");
		
		JPanel diagramPanel = new JPanel();
		editorSplitPane.setLeftComponent(diagramPanel);
		diagramPanel.setLayout(new MigLayout("", "[200px:n,grow]", "[grow]"));
		
		JScrollPane diagramScrollPane = new JScrollPane();
		diagramScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		diagramPanel.add(diagramScrollPane, "cell 0 0,grow");
		
		diagramInput = new JTextArea();
		diagramScrollPane.setViewportView(diagramInput);
		
		JLabel diagramLabel = new JLabel("Diagram:");
		diagramScrollPane.setColumnHeaderView(diagramLabel);
		
		JPanel consolePanel = new JPanel();
		editorSplitPane.setRightComponent(consolePanel);
		consolePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		consolePanel.add(consoleScrollPane, "cell 0 0,grow");
		
		consoleOutput = new JTextArea();
		consoleOutput.setForeground(Color.GREEN);
		consoleOutput.setBackground(Color.BLACK);
		consoleOutput.setEditable(false);
		consoleScrollPane.setViewportView(consoleOutput);
		
		JLabel consoleLabel = new JLabel("Console:");
		consoleScrollPane.setColumnHeaderView(consoleLabel);
		
		JPanel bottomPanel = new JPanel();
		rightPanel.add(bottomPanel, "cell 0 2,grow");
		bottomPanel.setLayout(new MigLayout("", "[][][][][grow,right]", "[]"));
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDiagramFile();
			}
		});
		bottomPanel.add(saveButton, "cell 0 0,alignx left");
		
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDiagramFile();
			}
		});
		bottomPanel.add(loadButton, "cell 1 0,alignx left");
		
		JButton executeButton = new JButton("Execute");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				executeDiagram();
			}
		});
		
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportDiagram();
			}
		});
		bottomPanel.add(exportButton, "cell 2 0,alignx left");
		
		JButton importButton = new JButton("Import");
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importDiagram();
			}
		});
		bottomPanel.add(importButton, "cell 3 0,alignx left");
		bottomPanel.add(executeButton, "cell 4 0,alignx right");
	}
	
	private void addModule(){
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Arquivos de M�dulos(.mt ou .dt)", "mt", "dt");		
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		String file_name;
		if(returnVal == JFileChooser.APPROVE_OPTION){			
			file_name = fc.getSelectedFile().getName();
			file_path = fc.getSelectedFile().getAbsolutePath().toString();
			addRequiredModule(file_name, file_path);
		}
	}
	
	private void addRequiredModule(String file_name, String file_path){
		if(m_modules_path.containsKey(file_name)){
			consoleOutput.append("Already contains a " + file_name + " module. Duplicates are not allowed.");
			return;
		}
		addModule(file_name);
		m_modules_path.put(file_name, file_path);
		consoleOutput.append("Module " + file_name + " added successfully.\n");				
	}
	
	private void addModule(String module_name)
	{
		DefaultListModel<String> list_model = (DefaultListModel<String>) modulesList.getModel();
		int pos = list_model.getSize();
		list_model.add(pos, module_name);	
	}
	
	private void removeModule(){	
		int index = modulesList.getSelectedIndex();
		if(index != -1){
			String module_name = modulesList.getModel().getElementAt(index);
			DefaultListModel<String> modules_list_model = (DefaultListModel<String>) modulesList.getModel();
			modules_list_model.remove(index);
			
			m_modules_path.remove(index);		
			moduleViewertextArea.setText("");
			consoleOutput.append("Module " + module_name + " removed succesfully.\n");
		}		
	}		
	
	private void displaySelectedModule() 
	{
		int index = modulesList.getSelectedIndex();
		if(index != -1){	
			moduleViewertextArea.setText("");
			String module_name = modulesList.getModel().getElementAt(index);
			String module_text = "";
			try {
				BufferedReader reader = new BufferedReader(new FileReader(m_modules_path.get(module_name)));
				String line;
				try {
					while((line = reader.readLine()) != null){
						module_text += (line + "\n");
							
					}
					moduleViewertextArea.setText(module_text);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	private void executeDiagram(){
		Diagram d = new Diagram();
		d.setModuleFilesFullPath(m_modules_path);
		boolean empty_fields = false;
		consoleOutput.setText("");
		if(diagramInput.getText().isEmpty()){
			consoleOutput.setText("Error executing: empty diagram.\n");
			empty_fields = true;
		}
		if(tapeInput.getText().isEmpty()){
			consoleOutput.append("Error executing: empty tape.\n");		
			empty_fields = true;
		}
		if(!empty_fields){
			try {
				if( d.loadFromString(diagramInput.getText()) ) {
					Tape tape = new Tape(tapeInput.getText());				
					consoleOutput.setText("Executing diagram with tape: " + tape.toString() + "'\n\n");
					consoleOutput.append(d.getCurrentState() + ": " + tape.toString() + "\n");
					d.execute(tape);					
					consoleOutput.append(d.getLog().getText());					
				} else {
					consoleOutput.append(d.getLog().getText());	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		d = null;
	}	
	
	
	private void saveDiagramFile(){
		if(diagramInput.getText().isEmpty()){
			consoleOutput.setText("Error saving diagram file: empty diagram.\n");
		}else{
			JFileChooser fc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Diagram files (.dt)", "dt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			String file_path;
			if(returnVal == JFileChooser.APPROVE_OPTION){						
				file_path = fc.getSelectedFile().getAbsolutePath().toString() + ".dt";
				FileWriter fstream; 
				try {
					fstream = new FileWriter(file_path);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(diagramInput.getText());
					out.close();
					consoleOutput.setText("Diagram file saved succesfully.\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}		
		}
	}
	
	private void loadDiagramFile(){
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Diagram files (.dt)", "dt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		boolean required_modules = true;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file_path = fc.getSelectedFile().getAbsolutePath().toString();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file_path));
				String line;
				try {
					while( (line = reader.readLine()) != null ){
						if(!checkRequiredModules(line)){
							required_modules = false;
						}
						diagramInput.append(line);		
						diagramInput.append("\n");
					}
					consoleOutput.append("Diagram file loaded successfully.\n");
					if(!required_modules){
						consoleOutput.append("Some of the required modules are not available," +
								" diagram won't be able to execute. Consider adding the required modules.\n");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean checkRequiredModules(String line)
	{
		StringTokenizer tokens = new StringTokenizer(line);
		if(tokens.countTokens() == 3){
			String module_type = tokens.nextToken();
			tokens.nextToken();
			String module_file = tokens.nextToken();
			if(module_type.equals("diagram") || module_type.equals("machine")){
				if(!m_modules_path.containsKey(module_file)){
					consoleOutput.append("Warning: module file " + module_file + " is not on the available modules list.\n");
					return false;
				}				
			}			
		}		
		return true;
	}
	
	private void exportDiagram()
	{		
		if(diagramInput.getText().isEmpty()){
			consoleOutput.setText("Error exporting diagram file: empty diagram.");
		}else{
			JFileChooser fc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Diagram file(.dt)", ".dt");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(null);
			String file_path;
			if(returnVal == JFileChooser.APPROVE_OPTION){				
				String file_directory =  fc.getSelectedFile().getAbsolutePath().toString() + "_contents";				
				File diagram_folder = new File(file_directory);				
				String file_name = fc.getSelectedFile().getName() + ".dt";
				file_path = file_directory + "\\" + file_name;				
				FileWriter fstream; 				
				try {
					diagram_folder.mkdir();						
					if(exportRequiredModules(file_directory)){
						fstream = new FileWriter(file_path);
						BufferedWriter out = new BufferedWriter(fstream);
						out.write(diagramInput.getText());
						out.close();				
						consoleOutput.setText("Diagram file saved succesfully.\n");
					}else{
						consoleOutput.setText("Failed to export diagram.\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}		
		}
	}
	
	private boolean exportRequiredModules(String path)
	{
		Collection<String> modules = m_modules_path.values();
		Iterator<String> it = modules.iterator();		
		while(it.hasNext()){			
			File source = new File(it.next().toString());
			File destination = new File(path + "\\" + source.getName());
			try {
				Files.copy(source.toPath(), destination.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				consoleOutput.setText("Problem exporting required modules.\n");
				return false;
			}			
		}	
		consoleOutput.setText("Required modules exported succesfully.\n");
		return true;
	}
	
	private void importDiagram()
	{		
		JFileChooser fc = new JFileChooser(new File("."));		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Diagram files (.dt)", "dt");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		String file_path;
		String file_name;
		boolean required_modules = true;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			reset();
			file_path = fc.getSelectedFile().getAbsolutePath().toString();
			file_name = fc.getSelectedFile().getName();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file_path));
				String line;
				try {
					importRequiredModules(file_name, file_path);
					while( (line = reader.readLine()) != null ){
						if(!checkRequiredModules(line)){
							required_modules = false;
						}
						diagramInput.append(line);		
						diagramInput.append("\n");
					}				
					consoleOutput.append("Diagram file loaded successfully.\n");
					if(!required_modules){
						consoleOutput.append("Some of the required modules are not available," +
								" diagram won't be able to execute. Consider adding the required modules.\n");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	//TODO: Import only required modules, not all modules in the path
	private boolean importRequiredModules(String diagram_name, String path)
	{
		String dir;
		if(path.lastIndexOf("\\") > path.lastIndexOf("/"))		{
			dir = path.substring(0, path.lastIndexOf("\\"));
		}else{
			dir = path.substring(0, path.lastIndexOf("/"));
		}
		File directory = new File(dir);
	    File[] children = directory.listFiles();
	    if (children != null) {
	        for (File child : children) {
	        	String file_name = child.getName();
	        	String file_path = child.getAbsolutePath();
	        	if((file_name.endsWith(".mt") || file_name.endsWith(".dt")) && !file_name.equals(diagram_name)){
	        		addRequiredModule(file_name, file_path);
	        	}
	        }
	    }
		return true;
	}
	
	private void reset()
	{
		m_modules_path.clear();
		consoleOutput.setText("");
		diagramInput.setText("");		
	}	
}
