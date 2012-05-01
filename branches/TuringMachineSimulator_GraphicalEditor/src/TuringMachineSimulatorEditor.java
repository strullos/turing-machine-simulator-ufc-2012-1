import javax.swing.JApplet;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;

import teoria.graph.editor.DiagramGraphEditor;
import teoria.graph.editor.MachineGraphEditor;
import teoria.simulador.modulo.Diagrama;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;


public class TuringMachineSimulatorEditor extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MachineGraphEditor m_maquina_editor;
	private DiagramGraphEditor m_diagrama_editor;
	private JTextField fita_textField;
	private JTabbedPane tabbedPane;

	/**
	 * Create the applet.
	 */
	public TuringMachineSimulatorEditor() {
		this.resize(new Dimension(800,600));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPane);		
		
		JPanel console_panel = new JPanel();
		splitPane.setRightComponent(console_panel);
		console_panel.setLayout(new MigLayout("", "[22px][16px][4px][9px][398px,grow,fill][4px][75px,fill]", "[20px][14px][35px,grow,fill][23px,grow,fill][grow,fill][grow,fill][]"));
		splitPane.getBottomComponent().setMinimumSize(new Dimension(0,200));
		
		JLabel label = new JLabel("Fita:");
		console_panel.add(label, "cell 0 0,alignx left,growy");
		
		fita_textField = new JTextField();
		fita_textField.setText("aabb");
		fita_textField.setColumns(10);
		console_panel.add(fita_textField, "cell 4 0 3 1,growx,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		console_panel.add(scrollPane, "cell 0 2 7 4,grow");
		
		JTextArea console_textArea = new JTextArea();
		scrollPane.setViewportView(console_textArea);
		console_textArea.setRows(1);
		console_textArea.setColumns(1);
		console_textArea.setEditable(false);
		console_textArea.setBackground(Color.WHITE);
		TextAreaOutputStream console_stream = new TextAreaOutputStream(console_textArea);
		
		JLabel label_1 = new JLabel("Console:");
		console_panel.add(label_1, "cell 0 1 3 1,alignx left,aligny top");		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);
		
		JButton button = new JButton("Executar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fita = fita_textField.getText();
				Diagrama d = new Diagrama();
				String arquivo;
				if(tabbedPane.getSelectedIndex() == 0){
					arquivo = m_diagrama_editor.pegaNomeDiagrama();
				}else{
					arquivo = m_maquina_editor.pegaNomeDiagrama();
				}
				d.carregar(arquivo);		
				d.imprime_diagrama();
				d.executar(fita);
			}
		});
		
		console_panel.add(button, "cell 6 6,alignx left,aligny top");
		
		JPanel editorDiagrama_panel = new JPanel();
		tabbedPane.addTab("Editor de Diagramas", null, editorDiagrama_panel, null);
		editorDiagrama_panel.setLayout(new MigLayout("", "[513.00px,grow]", "[301.00px,grow,fill]"));
		m_diagrama_editor = new DiagramGraphEditor();
		editorDiagrama_panel.add(m_diagrama_editor, "cell 0 0,growx,aligny top");
		
		JPanel editorMaquina_panel = new JPanel();
		tabbedPane.addTab("Editor de Máquinas", null, editorMaquina_panel, null);		
		editorMaquina_panel.setLayout(new MigLayout("", "[70px,grow][356px,grow]", "[251.00px,grow,fill][25.00px][19px][15px]"));
		m_maquina_editor = new MachineGraphEditor();
		
		editorMaquina_panel.add(m_maquina_editor, "cell 0 0 2 4,grow");
		PrintStream outStream = new PrintStream(console_stream, true);
		System.setOut(outStream);			
	}
}
