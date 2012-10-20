package turing.simulator.panels;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;

public class ModulesEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ModulesEditor() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "cell 0 0,grow");
		
		MachineTextEditor machine_text_editor = new MachineTextEditor();
		DiagramTextEditor diagram_text_editor = new DiagramTextEditor();
		DiagramGraphEditor diagram_graph_editor = new DiagramGraphEditor();
		tabbedPane.addTab("Diagram Text editor", null, diagram_text_editor, null);
		tabbedPane.addTab("Machine Text editor", null, machine_text_editor, null);
		tabbedPane.addTab("Diagram Graph editor", null, diagram_graph_editor, null);
	}

}
