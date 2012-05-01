package teoria.graph.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class DiagramGraphEditor extends GraphEditor {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;			
		
		private JTextField txtNome;
		
		private Vector<String> m_lista_de_modulos;

		private JComboBox<String> modulos_comboBox;
		
		private Vector<DiagramaNode> m_nos;
		private JTextField arquivo_textField;
		
		
		/**
		 * Create the frame.
		 */
		public DiagramGraphEditor() {
			m_lista_de_modulos = new Vector<String>();
			m_nos = new Vector<DiagramaNode>();
			setBackground(Color.LIGHT_GRAY);
			setLayout(new MigLayout("", "[25.00,fill][grow,fill]", "[555.00,grow][][]"));
			
			JPanel editor_panel = new JPanel();
			add(editor_panel, "cell 0 0 2 1,grow");
			
			iniciaGraph();
			editor_panel.setLayout(new MigLayout("", "[258.00px,grow,fill][150px:n:150px,grow,fill]", "[67.00px,grow,fill]"));
			
			editor_panel.add(m_graphComponent, "cell 0 0,alignx center,aligny center");		
			
			JPanel propriedades_panel = new JPanel();
			editor_panel.add(propriedades_panel, "cell 1 0,grow");
			propriedades_panel.setLayout(new MigLayout("", "[200px]", "[14px][20px][14px][20px][][][][]"));
			
			JLabel lblNome = new JLabel("Nome:");
			propriedades_panel.add(lblNome, "cell 0 0,alignx left,aligny top");
			
			txtNome = new JTextField();
			txtNome.setEnabled(false);
			propriedades_panel.add(txtNome, "cell 0 1,growx,aligny top");
			txtNome.setColumns(10);
			
			JLabel lblMdulo = new JLabel("M\u00F3dulo:");
			propriedades_panel.add(lblMdulo, "cell 0 2,alignx left,aligny top");
			
			modulos_comboBox = new JComboBox<String>();
			modulos_comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					String selecionado = modulos_comboBox.getSelectedItem().toString();
					if(selecionado.equals("#END")){
						txtNome.setText("#END");
						txtNome.setEnabled(false);
					}else{						
						txtNome.setEnabled(true);
					}
				}
			});
			modulos_comboBox.setEnabled(false);
			propriedades_panel.add(modulos_comboBox, "cell 0 3,growx,aligny top");
			
			JButton btnNovoNo = new JButton("Adicionar N\u00F3");
			propriedades_panel.add(btnNovoNo, "cell 0 4,grow");
			
			JButton btnProcurarMdulos = new JButton("Procurar M\u00F3dulos");
			propriedades_panel.add(btnProcurarMdulos, "cell 0 5,growx");
			
			JButton btnSalvar = new JButton("Salvar .dt");
			propriedades_panel.add(btnSalvar, "cell 0 6,growx");
			
			JButton btnCarregar = new JButton("Carregar .dt");
			btnCarregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(new File("."));					
					int returnVal = fc.showOpenDialog(null);
					String caminho_arquivo;
					if(returnVal == JFileChooser.APPROVE_OPTION){
						caminho_arquivo = fc.getSelectedFile().getAbsolutePath().toString();
						arquivo_textField.setText(caminho_arquivo);
						try {
							int index1 = caminho_arquivo.lastIndexOf("/");
							int index2 = caminho_arquivo.lastIndexOf("\\");
							int index3;
							if(index2 > index1){
								index3 = index2;
							}else{
								index3 = index1;
							}
							String dir = caminho_arquivo.substring(0, index3);
							procuraModulos(dir);
							resetaGraph();
							carregaGraph(caminho_arquivo);
						} catch (IOException e1) {
							e1.printStackTrace();
						}						
					}			
				}
			});
			propriedades_panel.add(btnCarregar, "cell 0 7,growx");
			
			arquivo_textField = new JTextField();
			add(arquivo_textField, "flowy,cell 1 1,growx");
			arquivo_textField.setColumns(10);
			
			JLabel lblArquivo = new JLabel("Arquivo:");
			add(lblArquivo, "cell 0 1,alignx left");
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
							salvaArquivoDt(dir);						
						}		
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			btnProcurarMdulos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fc = new JFileChooser(new File("."));						
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showOpenDialog(null);
					String dir;
					if(returnVal == JFileChooser.APPROVE_OPTION){
						dir = fc.getSelectedFile().getAbsolutePath().toString();
						procuraModulos(dir);						
					}					
				}
			});
			btnNovoNo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!txtNome.getText().isEmpty()){
						Object parent = m_graph.getDefaultParent();
						m_graph.getModel().beginUpdate();					
						String nome = txtNome.getText();
						String modulo = modulos_comboBox.getSelectedItem().toString();						
						DiagramaNode novo_no = new DiagramaNode();
						novo_no.m_nome = nome;
						novo_no.m_arquivo_modulo = modulo;
						Object vertice;
						if(modulo.equals("#END")){
							vertice = m_graph.insertVertex(parent, null, "#END", 0, 0, m_tamanho_estado, m_tamanho_estado, "ROUNDED");
							m_graph.getModel().endUpdate();
							return;
						}
						if(m_count == 0){
							vertice = m_graph.insertVertex(parent, null, nome, 0, 0, m_tamanho_estado, m_tamanho_estado, "ROUNDED;fillColor=#FF0000");
						}else{
							vertice = m_graph.insertVertex(parent, null, nome, 0, 0, m_tamanho_estado, m_tamanho_estado, "ROUNDED");
						}			
						novo_no.m_vertice = vertice;
						m_nos.add(novo_no);
						m_graph.getModel().endUpdate();
						m_count++;
					}else{
						System.out.println("Digite um nome para o novo nó.");
					}
				}
			});
		}
		
		
		
		void ajustaArestas(){
			mxParallelEdgeLayout layout = new mxParallelEdgeLayout(m_graph);
			layout.execute(m_graph.getDefaultParent());
		}
		
		void salvaArquivoDt(String arquivo) throws IOException{
			m_nome_diagrama = arquivo;
			FileWriter fstream = new FileWriter(m_nome_diagrama);
			BufferedWriter out = new BufferedWriter(fstream);		
			mxCell vertice;
			mxCell edge;
			
			String simbolos;
			String modulo_final;
			//Itera sobre os estados
			salvaGraph(out);
			out.write("\r\n");
			for(DiagramaNode n : m_nos){
				out.write("modulo " + n.m_nome + " " + n.m_arquivo_modulo + "\r\n");
			}
			out.write("\r\n");
			for(DiagramaNode n : m_nos){
				vertice = (mxCell)n.m_vertice;
				int num_edges = vertice.getEdgeCount();
				if(num_edges > 0){
					for(int i = 0; i < num_edges; i++){
						edge = (mxCell)vertice.getEdgeAt(i);
						if(edge.getSource().getValue() ==  vertice.getValue()){
							modulo_final = edge.getTarget().getValue().toString();
							simbolos = edge.getValue().toString();
							out.write(n.m_nome + " " + simbolos + " " + modulo_final + "\r\n");
						}
					}
				}
			}
			out.close();
		}	
		
		
		public void procuraModulos(String dir){
			modulos_comboBox.setEnabled(false);
			modulos_comboBox = new JComboBox<String>();
			txtNome.setEnabled(false);
			File actual = new File(dir);
			//Procura no diretorio atual por arquivos .mt
	        for( File f : actual.listFiles(new FileFilter(){
	        				public boolean accept(File file){ 
	        					return file.getName().endsWith(".mt"); 
	        					}
	        				})){        
	            m_lista_de_modulos.add(f.getName());
	            System.out.println("Modulo " + f.getName() + " encontrado.");
	        }
	        if(m_lista_de_modulos.isEmpty()){
	        	System.out.println("Nenhum modulo encontrado no diretorio.");
	        }else{	        	
	        	for(String s : m_lista_de_modulos){
	        		modulos_comboBox.addItem(s);
	        	}
	        	modulos_comboBox.setEnabled(true);
	        	txtNome.setEnabled(true);
	        	modulos_comboBox.addItem("#END");
	        }
		}
		
		@Override
		public String pegaNomeDiagrama() {
			return arquivo_textField.getText();
		}	
}
