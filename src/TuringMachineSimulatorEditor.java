import javax.swing.JApplet;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;


public class TuringMachineSimulatorEditor extends JApplet {
	private JTextField txtFita;

	/**
	 * Create the applet.
	 */
	public TuringMachineSimulatorEditor() {
		this.resize(new Dimension(800,600));
		getContentPane().setLayout(new MigLayout("", "[600px,grow]", "[800px]"));
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Editor de Máquinas", null, panel, null);
		panel.setLayout(new MigLayout("", "[70px,grow][356px,grow]", "[258px,grow,fill][15px][19px][15px][62px,fill][]"));
		MachineEditorFrame f = new MachineEditorFrame();
		panel.add(f, "cell 0 0 2 1,grow");
		
		JLabel lblFita = new JLabel("Fita:");
		panel.add(lblFita, "cell 0 1,growx,aligny top");
		
		txtFita = new JTextField();
		txtFita.setText("Fita");
		panel.add(txtFita, "cell 0 2 2 1,growx,aligny top");
		txtFita.setColumns(10);
		
		JLabel lblConsole = new JLabel("Console:");
		panel.add(lblConsole, "cell 0 3,growx,aligny top");
		
		JTextArea console_textArea = new JTextArea();
		console_textArea.setBackground(Color.LIGHT_GRAY);
		panel.add(console_textArea, "cell 0 4 2 1,growx,aligny top");
		
		
	}
}
