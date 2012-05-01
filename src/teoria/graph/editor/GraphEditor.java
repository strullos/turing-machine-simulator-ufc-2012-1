package teoria.graph.editor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;


public abstract class GraphEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected mxGraph m_graph;
	protected mxGraphComponent m_graphComponent;
	protected String m_nome_diagrama;
	protected Vector<Object> m_vertices;
	protected int m_count = 0;	
	protected static int m_tamanho_estado = 50; 
	protected JTextField arquivo_textField;
	
	public GraphEditor(){
		m_vertices = new Vector<Object>();
	}
	
	public abstract String pegaNomeDiagrama();
	
	
	void carregaGraph(String arquivo) throws IOException{
		m_nome_diagrama = arquivo;
		m_vertices.removeAllElements();
		m_count = 0;
		BufferedReader reader = new BufferedReader(new FileReader(arquivo));
		String linha;
		String[] tokens;
		String nome;
		float x;
		float y;
		String v1;
		String v2;
		Hashtable<String, Object> nos = new Hashtable<String, Object>();
		while((linha = reader.readLine()) != null){
			if(linha.startsWith("#o")){
				tokens = linha.split("\\s");
				if(tokens.length == 4){
					nome = tokens[1];
					x = Float.valueOf(tokens[2]);
					y = Float.valueOf(tokens[3]);
					nos.put(nome,adicionarVertice(nome, x, y));
				}
			}
			if(linha.startsWith("#e")){
				tokens = linha.split("\\s");
				if(tokens.length == 4){
					nome = tokens[1];
					v1 = tokens[2];
					v2 = tokens[3];
					adicionarAresta(nome, nos.get(v1), nos.get(v2));
				}
			}
		}
	}
	
	void salvaGraph(BufferedWriter out) throws IOException{
		Object[] objs = m_graph.getChildVertices(m_graph.getDefaultParent());
		mxCell vertice;
		mxCell edge;
		for(Object o : objs){		
			vertice = (mxCell)o;
			out.write("#o " + vertice.getValue().toString() + " " + vertice.getGeometry().getX() + " " + vertice.getGeometry().getY() +  "\r\n");
		}
		objs =  m_graph.getChildEdges(m_graph.getDefaultParent());
		for(Object o : objs){		
			edge = (mxCell)o;
			out.write("#e " + edge.getValue().toString() + " " + edge.getSource().getValue().toString() + " " + edge.getTarget().getValue().toString() + "\r\n");
		}
		out.write("\r\n");
	}
	
	void ajustaArestas(){
		mxParallelEdgeLayout layout = new mxParallelEdgeLayout(m_graph);
		layout.execute(m_graph.getDefaultParent());
	}
	
	Object adicionarVertice(String nome, float x, float y){
		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		Object vertice;
		if(m_count == 0){
			vertice = m_graph.insertVertex(parent, null, nome, x, y, m_tamanho_estado, m_tamanho_estado, "ROUNDED;fillColor=#FF0000");
		}else{
			vertice = m_graph.insertVertex(parent, null, nome, x, y, m_tamanho_estado, m_tamanho_estado, "ROUNDED");
		}								
		m_graph.getModel().endUpdate();
		m_vertices.add(vertice);
		m_count++;
		return vertice;
	}
	
	void adicionarAresta(String label,  Object v1, Object v2){
		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		m_graph.insertEdge(parent, null,label, v1, v2, "");
		m_graph.getModel().endUpdate();
	}
	
	void resetaGraph(){
		m_graph.removeCells(m_graph.getChildCells(m_graph.getDefaultParent(), true, true));
	}
	
	void iniciaGraph()
	{
		m_graph = new mxGraph();/* {
			@Override
			public boolean isValidSource(Object arg0) {
				mxCell cell = (mxCell)arg0;
				if(cell != null){
					if(cell.getValue().toString().equals("#END")){
						return true;
					}
				}
				return super.isValidSource(arg0);
			}
		};*/
		
		m_graphComponent = new mxGraphComponent(m_graph);
		m_graph.getModel().beginUpdate();
		mxStylesheet stylesheet = m_graph.getStylesheet();
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_OPACITY, 50);
		style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
		style.put(mxConstants.STYLE_FONTSIZE, "20");
	
		
		Hashtable<String, Object> edge = new Hashtable<String, Object>();	
		edge = (Hashtable<String,Object>)stylesheet.getDefaultEdgeStyle();
		edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");		
		edge.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		edge.put(mxConstants.STYLE_FONTSIZE, "16");
		stylesheet.setDefaultEdgeStyle(edge);
		stylesheet.putCellStyle("ROUNDED", style);
		m_graph.getModel().endUpdate();
		m_graph.setCellsResizable(false);
		m_graph.setAllowDanglingEdges(false);		
		m_graph.setAllowLoops(true);		
		
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
		m_graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener(){
			@Override
			public void invoke(Object arg0, mxEventObject evt) {
				mxCell cell = (mxCell) evt.getProperty("cell");
				cell.setValue("[a,b]");					
				ajustaArestas();
				
			}
		});		
	}
}
