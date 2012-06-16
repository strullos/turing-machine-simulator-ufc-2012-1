package turing.simulator.panels;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DiagramTextEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tapeInput;

	/**
	 * Create the panel.
	 */
	public DiagramTextEditor() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, "cell 0 0,grow");
		
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new MigLayout("", "[90.00px:n]", "[grow][]"));
		
		JTree modulesTree = new JTree();
		modulesTree.setBorder(new LineBorder(new Color(0, 0, 0)));
		modulesTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Modules") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Machines");
						node_1.add(new DefaultMutableTreeNode("@L"));
						node_1.add(new DefaultMutableTreeNode("@L#"));
						node_1.add(new DefaultMutableTreeNode("@R"));
						node_1.add(new DefaultMutableTreeNode("@R#"));
					add(node_1);
					add(new DefaultMutableTreeNode("Diagrams"));
				}
			}
		));
		leftPanel.add(modulesTree, "cell 0 0,grow");
		
		JButton addModuleButton = new JButton("+ M\u00F3dulo");
		addModuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		leftPanel.add(addModuleButton, "cell 0 1,growx");
		
		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JPanel topPanel = new JPanel();
		rightPanel.add(topPanel, "cell 0 0,grow");
		topPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
		
		JLabel tapeLabel = new JLabel("Fita:");
		topPanel.add(tapeLabel, "cell 0 0");
		
		tapeInput = new JTextField();
		topPanel.add(tapeInput, "cell 0 1,growx");
		tapeInput.setColumns(10);
		
		JPanel middlePanel = new JPanel();
		rightPanel.add(middlePanel, "cell 0 1,grow");
		middlePanel.setLayout(new MigLayout("", "[grow][grow]", "[grow]"));
		
		JPanel diagramPanel = new JPanel();
		middlePanel.add(diagramPanel, "cell 0 0,grow");
		diagramPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel diagramLabel = new JLabel("Diagrama:");
		diagramPanel.add(diagramLabel, "cell 0 0");
		
		JScrollPane diagramScrollPane = new JScrollPane();
		diagramScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		diagramPanel.add(diagramScrollPane, "cell 0 1,grow");
		
		JTextArea diagramInput = new JTextArea();
		diagramScrollPane.setViewportView(diagramInput);
		
		JPanel consolePanel = new JPanel();
		middlePanel.add(consolePanel, "cell 1 0,grow");
		consolePanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel consoleLabel = new JLabel("Console:");
		consolePanel.add(consoleLabel, "cell 0 0");
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		consolePanel.add(consoleScrollPane, "cell 0 1,grow");
		
		JTextArea consoleOutput = new JTextArea();
		consoleOutput.setEditable(false);
		consoleScrollPane.setViewportView(consoleOutput);
		
		JPanel bottomPanel = new JPanel();
		rightPanel.add(bottomPanel, "cell 0 2,grow");
		bottomPanel.setLayout(new MigLayout("", "[][][grow,right]", "[]"));
		
		JButton saveButton = new JButton("Salvar");
		bottomPanel.add(saveButton, "cell 0 0");
		
		JButton loadButton = new JButton("Carregar");
		bottomPanel.add(loadButton, "cell 1 0");
		
		JButton executeButton = new JButton("Executar");
		bottomPanel.add(executeButton, "cell 2 0");

	}

}
