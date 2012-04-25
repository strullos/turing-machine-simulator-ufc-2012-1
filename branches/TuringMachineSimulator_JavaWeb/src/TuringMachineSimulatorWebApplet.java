import javax.swing.JApplet;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.FileChooserUI;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

import teoria.simulador.modulo.Diagrama;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public class TuringMachineSimulatorWebApplet extends JApplet  {
	private JTextField fita_textField;
	private JTextArea maquina_textArea;
	private JTextArea resultado_textArea;
	private JButton btnExecutar;
	private String caminho_arquivo;
	private JTextField log_textField;
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
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		
		JPanel maquina_panel = new JPanel();
		tabbedPane.addTab("Maquina", null, maquina_panel, null);
		
		JLabel lblNewLabel = new JLabel("Fita:");
		
		JScrollPane maquina_scrollPane = new JScrollPane();
		
		JScrollPane resultado_scrollPane = new JScrollPane();
		
		resultado_textArea = new JTextArea();
		resultado_textArea.setEditable(false);
		resultado_scrollPane.setViewportView(resultado_textArea);
		maquina_panel.setLayout(new MigLayout("", "[56px][1px][40px,grow][314.00px][89px]", "[20px][14px][175px][14px][163px][23px][][][]"));
		maquina_panel.add(lblNewLabel, "cell 0 0,growx,aligny center");
		
		fita_textField = new JTextField();
		maquina_panel.add(fita_textField, "cell 3 0 2 1,growx,aligny top");
		fita_textField.setColumns(10);
		maquina_panel.add(maquina_scrollPane, "cell 0 2 5 1,grow");
		
		maquina_textArea = new JTextArea();
		maquina_textArea.setText("q0 1000 1000\r\n\r\nq0 # q1 >");
		maquina_scrollPane.setViewportView(maquina_textArea);
		maquina_panel.add(resultado_scrollPane, "cell 0 4 5 3,grow");
		
		JLabel lblMaquina = new JLabel("Maquina:");
		maquina_panel.add(lblMaquina, "cell 0 1,alignx left,aligny top");
		
		JLabel lblResultado = new JLabel("Resultado");
		maquina_panel.add(lblResultado, "cell 0 3 3 1,growx,aligny top");	
		
		btnExecutar = new JButton("Executar");
		btnExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!fita_textField.getText().isEmpty()){
					resultado_textArea.setText(null);				
					Diagrama d = new Diagrama();
					d.carregar_diagrama_buffer(maquina_textArea.getText());				
					d.executar(fita_textField.getText());	
				}else{
					log_textField.setText(null);
					log_textField.setText("A fita está vazia.");				
				}
			}
		});
		
		JButton btnCarregarMquina = new JButton("Carregar M\u00E1quina");
		btnCarregarMquina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					caminho_arquivo = fc.getSelectedFile().getAbsolutePath().toString();
					if(caminho_arquivo.endsWith(".mt")){
						maquina_textArea.setText(null);
						try {
							BufferedReader br = new BufferedReader(new FileReader(caminho_arquivo));
							String linha = br.readLine().toString();
							while(linha != null){
								maquina_textArea.append(linha);
								maquina_textArea.append("\n");
								linha = br.readLine();
							}
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else{
						log_textField.setText(null);
						log_textField.setText("Selecione um arquivo .mt");
					}
				}
			}
		});
		
		JLabel lblLog = new JLabel("Log:");
		maquina_panel.add(lblLog, "cell 0 7");
		
		log_textField = new JTextField();
		log_textField.setEditable(false);
		maquina_panel.add(log_textField, "cell 2 7 3 1,growx");
		log_textField.setColumns(10);
		maquina_panel.add(btnCarregarMquina, "cell 0 8");
		maquina_panel.add(btnExecutar, "cell 4 8,growx,aligny top");
		
	
		
		JPanel diagrama_panel = new JPanel();
		tabbedPane.addTab("Diagrama", null, diagrama_panel, null);
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
		PrintStream outStream = new PrintStream(console_stream, true);
		System.setOut(outStream);		
		
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
