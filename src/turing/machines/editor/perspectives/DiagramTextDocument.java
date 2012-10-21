package turing.machines.editor.perspectives;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;

import ui.utils.ConsoleComponent;
import ui.utils.ItemListComponent;
import ui.utils.LineEditComponent;
import ui.utils.TextEditComponent;

public class DiagramTextDocument extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConsoleComponent m_console;
	private TextEditComponent m_diagram_input;
	private LineEditComponent m_tape_input;
	private ItemListComponent m_modules_list;
	public DiagramTextDocument() {
		setLayout(new BorderLayout(0, 0));
		
		m_tape_input = new LineEditComponent("Tape:");
		m_diagram_input = new TextEditComponent("Diagram:");
		m_console = new ConsoleComponent();
		m_modules_list = new ItemListComponent("Modules:");
		
	
		add(m_tape_input, BorderLayout.NORTH);		
		
		JSplitPane diagram_editor_splitPane = new JSplitPane();
		add(diagram_editor_splitPane, BorderLayout.CENTER);
		diagram_editor_splitPane.setDividerLocation(500);
		
		
		diagram_editor_splitPane.setLeftComponent(m_diagram_input);
		
		JTabbedPane console_and_modules_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		diagram_editor_splitPane.setRightComponent(console_and_modules_tabbedPane);		

		console_and_modules_tabbedPane.addTab("Console", null, m_console, null);		
		console_and_modules_tabbedPane.addTab("Modules List", null, m_modules_list, null);
	}

}
