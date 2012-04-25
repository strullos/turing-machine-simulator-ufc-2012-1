import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;

import teoria.simulador.modulo.Diagrama;

import java.io.PrintStream;


public class TuringMachineSimulatorWebApplet extends JApplet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caminho_arquivo;
	private JTextField diagrama_fita_textField;
	private JTextField diagrama_log_textField;
	private JTextField arquivo_diagrama_textField;
	private Diagrama diagrama_turing;
	private JTextArea diagrama_resultado_textArea;

	/**
	 * Create the applet.
	 */
	public TuringMachineSimulatorWebApplet() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
	
		
		JPanel diagrama_panel = new JPanel();
		getContentPane().add(diagrama_panel);
		diagrama_panel.setLayout(new MigLayout("", "[22px][11px][15px][5px][66px][265px][119px]", "[20px][20px][23px][324px][20px][23px]"));
		
		diagrama_fita_textField = new JTextField();
		diagrama_fita_textField.setColumns(10);
		diagrama_panel.add(diagrama_fita_textField, "cell 4 0 3 1,growx,aligny top");
		
		JLabel arquivo_diagrama_lblNewLabel = new JLabel("Arquivo diagrama:");
		diagrama_panel.add(arquivo_diagrama_lblNewLabel, "cell 0 1,growx,aligny center");
		
		arquivo_diagrama_textField = new JTextField();
		diagrama_panel.add(arquivo_diagrama_textField, "cell 4 1 3 1,growx,aligny top");
		arquivo_diagrama_textField.setColumns(10);
		
		JLabel label_1 = new JLabel("Resultado");
		diagrama_panel.add(label_1, "cell 0 2 3 1,alignx left,aligny center");
		
		JLabel label_2 = new JLabel("Log:");
		diagrama_panel.add(label_2, "cell 0 4,alignx left,aligny center");
		
		diagrama_log_textField = new JTextField();
		diagrama_log_textField.setEditable(false);
		diagrama_log_textField.setColumns(10);
		diagrama_panel.add(diagrama_log_textField, "cell 4 4 3 1,growx,aligny top");
		
		JScrollPane diagrama_resultado_scrollPane = new JScrollPane();
		diagrama_panel.add(diagrama_resultado_scrollPane, "cell 0 3 7 1,grow");
		
		diagrama_resultado_textArea = new JTextArea();
		diagrama_resultado_textArea.setEditable(false);
		diagrama_resultado_scrollPane.setViewportView(diagrama_resultado_textArea);
		
		TextAreaOutputStream console_stream = new TextAreaOutputStream(diagrama_resultado_textArea);
		
		JButton btnCarregarDiagrama = new JButton("Carregar Diagrama");
		btnCarregarDiagrama.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();			
				int returnVal = fc.showOpenDialog(null);				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					caminho_arquivo = fc.getSelectedFile().getAbsolutePath().toString();
					if(carrega_diagrama(caminho_arquivo)){
						diagrama_log_textField.setText("Diagrama carregado com sucesso.");
					}else{
						diagrama_log_textField.setText("Falha ao carregar diagrama: " + arquivo_diagrama_textField.getText().toString());
					}
				}
			}
		});
		diagrama_panel.add(btnCarregarDiagrama, "cell 6 2,growx,aligny top");
		
		JButton diagrama_executar_btn = new JButton("Executar Diagrama");
		diagrama_executar_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String caminho_arquivo = arquivo_diagrama_textField.getText().toString();
				if(carrega_diagrama(caminho_arquivo)){
					diagrama_log_textField.setText("Diagrama carregado com sucesso.");
					if(diagrama_fita_textField.getText().isEmpty()){
						diagrama_log_textField.setText(null);
						diagrama_log_textField.setText("Fita vazia.");
					}else{
						diagrama_turing.executar(diagrama_fita_textField.getText().toString());
						diagrama_turing = null;
					}
				}else{
					diagrama_log_textField.setText("Falha ao carregar diagrama: " + arquivo_diagrama_textField.getText().toString());
				}				
			}
		});
		diagrama_panel.add(diagrama_executar_btn, "cell 6 5,alignx center,aligny top");
		
		JLabel label = new JLabel("Fita:");
		diagrama_panel.add(label, "cell 0 0,alignx left,aligny center");
		PrintStream outStream = new PrintStream(console_stream, true);
		System.setOut(outStream);		
	}
	
	private boolean carrega_diagrama(String caminho_arquivo){
		if(diagrama_turing == null){
			diagrama_resultado_textArea.setText(null);		
			arquivo_diagrama_textField.setText(caminho_arquivo);
			if(caminho_arquivo.endsWith(".mt") || caminho_arquivo.endsWith(".dt")){
				diagrama_turing = new Diagrama();
				if(diagrama_turing.carregar(caminho_arquivo)){
					diagrama_log_textField.setText(null);
					diagrama_log_textField.setText("Diagrama carregado com sucesso. Pront para executar.");
					return true;
				}
			}else{
				diagrama_log_textField.setText(null);
				diagrama_log_textField.setText("Arquivo invalido.");
				return false;
			}
		}		
		return true;
	}
}
