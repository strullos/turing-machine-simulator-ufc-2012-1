import java.awt.Dimension;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;

public class MachineEditorFrame extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private mxGraph maquina_graph;
	private mxGraphComponent maquina_graphComponent;
	
	private Vector<Object> m_vertices;
	private int count = 0;
	
	private static int tamanho = 50; 
	
	
	/**
	 * Create the frame.
	 */
	public MachineEditorFrame() {
		m_vertices = new Vector<Object>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new MigLayout("", "[grow,fill]", "[555.00,grow][]"));
		
		JPanel editor_panel = new JPanel();
		add(editor_panel, "cell 0 0,grow");
		
		JPanel buttons_panel = new JPanel();
		add(buttons_panel, "cell 0 1,grow");
		
		JButton btnNovoNo = new JButton("Adicionar Nó");
		btnNovoNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Object parent = maquina_graph.getDefaultParent();
				maquina_graph.getModel().beginUpdate();
				Object vertice;
				if(count == 0){
					vertice = maquina_graph.insertVertex(parent, null, "q" + String.valueOf(count), 0, 0, tamanho, tamanho, "ROUNDED;fillColor=#FF0000");
				}else{
					vertice = maquina_graph.insertVertex(parent, null, "q" + String.valueOf(count), 0, 0, tamanho, tamanho, "ROUNDED");
				}								
				maquina_graph.getModel().endUpdate();
				m_vertices.add(vertice);
				count++;
			}
		});
		buttons_panel.setLayout(new MigLayout("", "[123px][220px,grow][78px][97px][50px][94px][95px]", "[::25px]"));
		buttons_panel.add(btnNovoNo, "cell 0 0,alignx left,aligny top");
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					salvaArquivoMt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		buttons_panel.add(btnSalvar, "cell 6 0,alignx left,aligny top");
		
		iniciaGraph();
		editor_panel.setLayout(new MigLayout("", "[549px,grow,fill]", "[258px,grow,fill]"));
		
		editor_panel.add(maquina_graphComponent, "cell 0 0,alignx center,aligny center");
		
		
	}
	
	void iniciaGraph()
	{
		maquina_graph = new mxGraph();		
		maquina_graphComponent = new mxGraphComponent(maquina_graph);
		maquina_graph.getModel().beginUpdate();
		mxStylesheet stylesheet = maquina_graph.getStylesheet();
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_OPACITY, 50);
		style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
		style.put(mxConstants.STYLE_FONTSIZE, "20");
	
		
		Hashtable<String, Object> edge = new Hashtable<String, Object>();	
		edge = (Hashtable<String,Object>)stylesheet.getDefaultEdgeStyle();
		edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");		
		edge.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		edge.put(mxConstants.STYLE_FONTSIZE, "24");
		stylesheet.setDefaultEdgeStyle(edge);
		stylesheet.putCellStyle("ROUNDED", style);
		maquina_graph.getModel().endUpdate();
		maquina_graph.setCellsResizable(false);
		maquina_graph.setAllowDanglingEdges(false);		
		maquina_graph.setAllowLoops(true);		
		
//	    HashMap<String, Object> edge = new HashMap<String, Object>();
//	    edge.put(mxConstants.STYLE_ROUNDED, true);
//	    edge.put(mxConstants.STYLE_ORTHOGONAL, false);
//	    edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
//		edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
//		edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
//		edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
//		edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
//		edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); 
//		edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");  
//		edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");
//		mxStylesheet edgeStyle = new mxStylesheet();    
//		edgeStyle.setDefaultEdgeStyle(edge);    
//		maquina_graph.setStylesheet(edgeStyle);
		
		maquina_graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener(){
			@Override
			public void invoke(Object arg0, mxEventObject evt) {	
				mxCell cell = (mxCell) evt.getProperty("cell");
				cell.setValue("SIMBOLO;ACAO");
				ajustaArestas();
			}		
		});		
	}
	
	void ajustaArestas(){
		mxParallelEdgeLayout layout = new mxParallelEdgeLayout(maquina_graph);
		layout.execute(maquina_graph.getDefaultParent());
	}
	
	void salvaArquivoMt() throws IOException{
		FileWriter fstream = new FileWriter("test.txt");
		BufferedWriter out = new BufferedWriter(fstream);		
		Object[] objs = maquina_graph.getChildVertices(maquina_graph.getDefaultParent());
		mxCell vertice;
		mxCell edge;
		String simbolo;
		String acao;
		String transicao;
		String estado_final;
		int pos_separador;
		for(Object o : objs){
			vertice = (mxCell)o;
			
			int num_edges = vertice.getEdgeCount();
			if(num_edges > 0){
				for(int i = 0; i < num_edges; i++){
					out.write(vertice.getValue().toString() + " ");
					edge = (mxCell)vertice.getEdgeAt(i);
					estado_final = edge.getTarget().getValue().toString();
					transicao = edge.getValue().toString();
					pos_separador = transicao.indexOf(";");
					simbolo = transicao.substring(0,pos_separador);
					acao = transicao.substring(pos_separador+1,transicao.length());
					out.write(simbolo + " " + estado_final + " " + acao + "\n");
				}				
			}else{
				out.write(vertice.getValue().toString() + " NULL " + " NULL " + " NULL ");
			}
		}
		out.close();
	}
}
