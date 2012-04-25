import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import teoria.simulador.maquina.Maquina;
import teoria.simulador.modulo.Diagrama;
import teoria.simulador.modulo.IModulo;
import teoria.simulador.modulo.ModuloFactory;


//public class GUIMain extends JFrame {
//	public static void main(String[] args){
//		EventQueue.invokeLater(new Runnable(){
//			public void run(){
//				GUIMain self = new GUIMain();
//				self.init();
//				self.setSize(500,150);
//				self.setVisible(true);
//				self.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//			}
//		});
//	}
//	
public class GUIMain extends JApplet implements ActionListener {
	private JTextArea entrada_maquina;
	private JTextArea saida_maquina;
	private JButton executa_botao;
	private JLabel entrada_label;
	private JLabel saida_label;
	@Override
	public void actionPerformed(ActionEvent evt) {
		saida_maquina.setText(null);
		String conteudo = entrada_maquina.getText();
		Diagrama d = new Diagrama();
		d.carregar_diagrama_buffer(conteudo);
		d.executar("aabb");		
	}
	
	@Override
	public void init(){
		this.setSize(640,480);
		this.entrada_label = new JLabel("Digite sua maquina de turing: ");
		this.saida_label = new JLabel("Resultado: ");
		this.entrada_maquina = new JTextArea(10, 20);
		this.saida_maquina = new JTextArea(10,20);
		this.executa_botao = new JButton("Executar");
		this.executa_botao.setMaximumSize(new Dimension(50,50));
		JScrollPane entrada_painel = new JScrollPane(this.entrada_maquina, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane saida_painel = new JScrollPane(this.saida_maquina, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		entrada_painel.setSize(100, 100);
		this.add(entrada_label);
		this.add(entrada_painel);
		this.add(saida_label);
		this.add(saida_painel);
		this.add(executa_botao);
		this.setLayout(new GridLayout(5, 1));	
		redirectSystemStreams();
		executa_botao.addActionListener(this);
	}	
	
	private void updateTextArea(final String text){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				saida_maquina.append(text);
			}
		});
	}

	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char)b));
				
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}
			
			@Override
			public void write(byte[] b) throws IOException  {
				write(b, 0, b.length);
			}
		};
		System.setOut(new PrintStream(out, true));
	}
}
