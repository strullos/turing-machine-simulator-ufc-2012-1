package teoria.graph.editor;

import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;

import com.mxgraph.model.mxCell;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class MachineGraphEditor extends GraphEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private JTextField arquivo_textField;
	
	/**
	 * Create the frame.
	 */
	public MachineGraphEditor() {
		m_vertices = new Vector<Object>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new MigLayout("", "[48.00px,fill][grow,fill]", "[263.00px,grow][27.00px]"));
		
		JPanel editor_panel = new JPanel();
		add(editor_panel, "cell 0 0 2 1,grow");
		
		iniciaGraph();
		editor_panel.setLayout(new MigLayout("", "[549px,grow,fill][150px:n:150px,fill]", "[258px,grow,fill]"));
		
		editor_panel.add(m_graphComponent, "cell 0 0,alignx center,aligny center");		
		
		JPanel panel = new JPanel();
		editor_panel.add(panel, "cell 1 0,grow");
		panel.setLayout(new MigLayout("", "[133px]", "[23px][23px][]"));
		
		JButton btnNovoNo = new JButton("Adicionar N\u00F3");
		panel.add(btnNovoNo, "cell 0 0,growx,aligny top");
		
		JButton btnSalvar = new JButton("Salvar .mt");
		panel.add(btnSalvar, "cell 0 1,growx,aligny top");
		
		JButton btnCarregar = new JButton("Carregar .mt");
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));		
				//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);
				String dir;
				if(returnVal == JFileChooser.APPROVE_OPTION){
					dir = fc.getSelectedFile().getAbsolutePath().toString();
					arquivo_textField.setText(dir);
					try {
						carregaGraph(dir);
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}		
			}
		});
		panel.add(btnCarregar, "cell 0 2,growx");
		
		JLabel lblArquivo = new JLabel("Arquivo:");
		add(lblArquivo, "flowx,cell 0 1,alignx trailing");
		
		arquivo_textField = new JTextField();
		add(arquivo_textField, "cell 1 1,growx");
		arquivo_textField.setColumns(10);
		btnSalvar.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser fc = new JFileChooser(new File("."));		
					//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showSaveDialog(null);
					String dir;
					if(returnVal == JFileChooser.APPROVE_OPTION){						
						dir = fc.getSelectedFile().getAbsolutePath().toString();
						arquivo_textField.setText(dir);
						salvaArquivoMt(dir);						
					}		
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNovoNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String nome = "q" + String.valueOf(m_count);
				adicionarVertice(nome,0, 0);
			}
		});
	}	
	
	
	void salvaArquivoMt(String arquivo) throws IOException{
		m_nome_diagrama = arquivo;
		FileWriter fstream = new FileWriter(m_nome_diagrama);
		BufferedWriter out = new BufferedWriter(fstream);		
		Object[] objs = m_graph.getChildVertices(m_graph.getDefaultParent());
		mxCell vertice;
		mxCell edge;
		
		String simbolos;
		String[] simbolos_tokens;
		String acao;
		String transicao;
		String estado_final;
		int pos_separador = 0;
		boolean primeiro_estado = true;
		salvaGraph(out);
		out.write("\r\n");
		//Itera sobre os estados
		for(Object o : objs){		
			vertice = (mxCell)o;	
			if(primeiro_estado){
				out.write(vertice.getValue().toString() + " 1000 " + "1000\r\n\r\n");
				primeiro_estado = false;
			}
			int num_edges = vertice.getEdgeCount();
			//Verifica o numero de arestas do estado
			if(num_edges > 0){
				for(int i = 0; i < num_edges; i++){					
					edge = (mxCell)vertice.getEdgeAt(i);
					//Se a aresta realmente sai do estado(vertice) atual
					if(edge.getSource().getValue() == vertice.getValue()){
						estado_final = edge.getTarget().getValue().toString();
						transicao = edge.getValue().toString();
						pos_separador = transicao.indexOf(";");				
						acao = transicao.substring(pos_separador+1,transicao.length());
						simbolos = transicao.substring(0,pos_separador);
						//Para cada simbolo, na regra,sera escrita uma linha no arquivo
						if(simbolos.contains("[") && simbolos.contains("]")){
							simbolos = simbolos.substring(1, simbolos.length() - 1);
							simbolos_tokens = simbolos.split("\\,");
							for(int j = 0; j < simbolos_tokens.length; j++){
								out.write(vertice.getValue().toString() + " " + simbolos_tokens[j] + " " + estado_final + " " + acao + "\r\n");
							}
						}
					}else{
						//out.write(vertice.getValue().toString() + " NULL " + " NULL " + " NULL ");
					}
				}				
			}else{
				//out.write(vertice.getValue().toString() + " NULL " + " NULL " + " NULL ");
			}
		}
		out.close();
	}


	@Override
	public String pegaNomeDiagrama() {
		return arquivo_textField.getText();
	}		
}
