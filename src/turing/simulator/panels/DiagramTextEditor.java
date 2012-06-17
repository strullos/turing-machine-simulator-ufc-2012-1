package turing.simulator.panels;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
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

public class DiagramTextEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tapeInput;	
	private JTree modulesTree;
	
	private HashMap<String, String> m_modules_path;	
	private JTextArea consoleOutput;
	private JTextArea diagramInput;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("serial")
	public DiagramTextEditor() {
		m_modules_path = new HashMap<String,String>();
		
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, "cell 0 0,grow");
		
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new MigLayout("", "[194.00px:n,grow]", "[][grow][]"));
		
		JLabel availableModulesLabel = new JLabel("Available Modules:");
		leftPanel.add(availableModulesLabel, "cell 0 0");
		
		modulesTree = new JTree();
		modulesTree.setBorder(new LineBorder(new Color(0, 0, 0)));
		modulesTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Modules") {
				{
					add(new DefaultMutableTreeNode("Machines"));
					add(new DefaultMutableTreeNode("Diagrams"));
				}
			}
		));		
	
		leftPanel.add(modulesTree, "cell 0 1,grow");
		
		JButton addModuleButton = new JButton("+ Module");
		addModuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addModule();
			}
		});
		leftPanel.add(addModuleButton, "flowx,cell 0 2,alignx left");
		
		JButton removeModuleButton = new JButton("- Module");
		removeModuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeNode();
			}
		});
		leftPanel.add(removeModuleButton, "cell 0 2,alignx left");
		
		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
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
		middlePanel.add(editorSplitPane, "flowx,cell 0 0,grow");
		
		JPanel diagramPanel = new JPanel();
		editorSplitPane.setLeftComponent(diagramPanel);
		diagramPanel.setLayout(new MigLayout("", "[200px:n,grow]", "[][grow]"));
		
		JLabel diagramLabel = new JLabel("Diagram:");
		diagramPanel.add(diagramLabel, "cell 0 0");
		
		JScrollPane diagramScrollPane = new JScrollPane();
		diagramScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		diagramPanel.add(diagramScrollPane, "cell 0 1,grow");
		
		diagramInput = new JTextArea();
		diagramScrollPane.setViewportView(diagramInput);
		
		JPanel consolePanel = new JPanel();
		editorSplitPane.setRightComponent(consolePanel);
		consolePanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel consoleLabel = new JLabel("Console:");
		consolePanel.add(consoleLabel, "cell 0 0");
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		consolePanel.add(consoleScrollPane, "cell 0 1,grow");
		
		consoleOutput = new JTextArea();
		consoleOutput.setForeground(Color.GREEN);
		consoleOutput.setBackground(Color.BLACK);
		consoleOutput.setEditable(false);
		consoleScrollPane.setViewportView(consoleOutput);
		
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
		        "Arquivos de Módulos(.mt ou .dt)", "mt", "dt");		
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
		m_modules_path.put(file_name, file_path);
		String prefix = "";
		if(file_name.endsWith(".mt")){
			prefix = "Machines";				
		}
		if(file_name.endsWith(".dt")){			
			prefix = "Diagrams";				
		}
		if(!prefix.isEmpty()){
			addTreeNode(file_name,prefix,0);	
			addTreeNode(file_path,file_name,0);
			consoleOutput.append("Module " + file_name + " added successfully.\n");				
		}
	}
	
	private void removeNode(){
		TreePath path = modulesTree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		String node_name = node.toString();
		if(node_name.equals("Diagrams") || node_name.equals("Machines")){
			consoleOutput.append("Can't remove category nodes.\n");
			return;
		}
		DefaultTreeModel model = (DefaultTreeModel)modulesTree.getModel();
		if(node.isLeaf()){
			node_name = node.getParent().toString();
			node = (DefaultMutableTreeNode)node.getParent();
		}
		model.removeNodeFromParent(node);		
		m_modules_path.remove(node_name);
		expandTreeNodes();		
		consoleOutput.append("Node " + node_name + "removed succesfully.");
	}
	
	private void addTreeNode(String file_name, String prefix, int start_row){
		TreePath path = modulesTree.getNextMatch(prefix, start_row, Position.Bias.Forward);	
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(file_name);
		DefaultTreeModel model = (DefaultTreeModel)modulesTree.getModel();
		model.insertNodeInto(new_node,node,node.getChildCount());
		expandTreeNodes();
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
	}
	
	private void expandTreeNodes()
	{
		for(int i = 0; i < modulesTree.getRowCount(); i++){
			modulesTree.expandRow(i);
		}
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
	
	private void exportDiagram(){		
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
	
	private boolean exportRequiredModules(String path){
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
	
	private void importDiagram(){		
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
		resetTree();
	}	
	
	private void resetTree(){
		TreePath diagrams_path = modulesTree.getNextMatch("Diagrams", 0, Position.Bias.Forward);	
		DefaultMutableTreeNode diagrams_node = (DefaultMutableTreeNode)diagrams_path.getPathComponent(1);
		diagrams_node.removeAllChildren();
		TreePath machines_path = modulesTree.getNextMatch("Machines", 0, Position.Bias.Forward);	
		DefaultMutableTreeNode machines_node =  (DefaultMutableTreeNode)machines_path.getPathComponent(1);
		machines_node.removeAllChildren();		
		DefaultTreeModel model = (DefaultTreeModel)modulesTree.getModel();
		model.reload();
	}
}
