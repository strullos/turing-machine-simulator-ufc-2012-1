package graph;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;

public class Graph {
	protected mxGraph m_graph;
	private mxGraphComponent m_graph_component;
	private mxGraphOutline m_graph_outline;
	private static int m_node_width = 25;
	private static int m_node_height = 25;
	private String m_node_style;
	private mxCell m_starting_node;
	
	
	public Graph(){
		m_graph = new mxGraph();		
		m_graph_component = new mxGraphComponent(m_graph);
		m_node_style = new String("ROUNDED;fillColor=#FFFFFF");

		mxStylesheet stylesheet = m_graph.getStylesheet();
		HashMap<String, Object> style = new HashMap<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_OPACITY, 100);
		style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		style.put(mxConstants.STYLE_FONTSIZE, "12"); //!< Remove this later...
		style.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
		style.put(mxConstants.STYLE_SPACING_TOP, 5);
		style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		//style.put(mxConstants.STYLE_FONTSIZE, "12");


		//         HashMap<String, Object> edge = new HashMap<String, Object>();       
		//         edge = (HashMap<String,Object>)stylesheet.getDefaultEdgeStyle();
		//         edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");           
		//     edge.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		//     edge.put(mxConstants.STYLE_FONTSIZE, "16");
		//     stylesheet.setDefaultEdgeStyle(edge);
		stylesheet.putCellStyle("ROUNDED", style);
		
		
		HashMap<String, Object> edgeStyle = new HashMap<String, Object>();
		//edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
		edgeStyle.put(mxConstants.STYLE_SHAPE,    mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
		stylesheet.setDefaultEdgeStyle(edgeStyle);
		
		m_graph.getModel().endUpdate();
		m_graph.setCellsResizable(true);
		m_graph.setAllowDanglingEdges(false);           
		m_graph.setAllowLoops(true);      
		//m_graph.setAutoSizeCells(true);


		m_graph_component.setGridVisible(true);	
		m_graph_component.setBackground(new Color(255,255,255));



		m_graph_component.addMouseWheelListener(new MouseWheelTracker());	

		m_graph_component.setPageVisible(true);
		//	 PageFormat page_format = new PageFormat();
		//	 page_format.setOrientation(PageFormat.LANDSCAPE);
		//	 m_graph_component.setPageFormat(page_format);
		m_graph_component.getViewport().setOpaque(true);
		m_graph_component.getViewport().setBackground(Color.WHITE);

		m_graph_outline = new mxGraphOutline(this.m_graph_component);

		m_graph_component.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {

			@Override
			public void invoke(Object arg0, mxEventObject arg1) {
				System.out.println("ADDED!");

			}
		});

		m_graph_component.addListener(mxEvent.LABEL_CHANGED,new mxEventSource.mxIEventListener() {

			@Override
			public void invoke(Object arg0, mxEventObject arg1) {
				System.out.println("RENAMED");

			}
		});
		
		m_graph_component.addKeyListener(new GraphKeyListener());
	}	


	public void AddNode(String label)
	{
		//		m_nodes.put(node.GetLabel(), node);
		//		Object parent = m_graph.getDefaultParent();
		//        m_graph.getModel().beginUpdate();
		//        mxCell new_vertex = (mxCell) m_graph.insertVertex(parent, null, node.GetLabel() ,node.GetXPos(), node.GetYPos(), m_node_width, m_node_height, m_node_style);
		//        //mxCell new_vertex = (mxCell) m_graph.insertVertex(parent, null, node.GetLabel() ,node.GetXPos(), node.GetYPos(), 500, 500, m_node_style);//!< Remove this later...
		//        m_cell_node_map.put(new_vertex, node);
		//        m_graph.getModel().endUpdate();  

		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		mxCell node = (mxCell)m_graph.insertVertex(parent, null, label ,0, 0, m_node_width, m_node_height, m_node_style);
		m_graph.getModel().endUpdate();
		if(m_graph.getChildCells(m_graph.getDefaultParent(), true, false).length == 1){
			m_starting_node = node;
			node.setStyle("ROUNDED;fillColor=#DDDDDD");
			m_graph_component.refresh();
		}
	}	

	public void RemoveSelectedCell()
	{
		////		GraphNode selected_node = GetSelectedNode();
		////		m_nodes.remove(selected_node.GetLabel());
		////		m_cell_node_map.remove(m_graph.getSelectionCells());
		////		m_graph.removeCells(m_graph.getSelectionCells());
		//		mxCell selected_vertex = (mxCell) m_graph.getSelectionCell();
		//		System.out.println(selected_vertex.getEdgeCount());
		//		mxCell edge = (mxCell) selected_vertex.getEdgeAt(0);
		//		if(edge.getSource() == selected_vertex){
		//			System.out.println("IT IS SOURCE");
		//		}else{
		//			System.out.println("NOT SOURCE");
		//		}
		m_graph.removeCells(m_graph.getSelectionCells());
	}

	public boolean ContainsNode(String label)
	{
		return false;
	}

	public mxGraphComponent GetGraphComponent()
	{
		return m_graph_component;
	}

//	public GraphNode GetSelectedNode()
//	{
//		return m_cell_node_map.get(m_graph.getSelectionCell());
//	}

	public mxGraphOutline GetGraphOutline()
	{
		return m_graph_outline;
	}
	
	public String GenerateTuringMachine(){
		if( m_graph.getChildCells(m_graph.getDefaultParent(),true,false).length == 0){
			return "";
		}
		String machine_text = m_graph.getLabel(m_starting_node) +  " 1000\n\n";
		Object[] cells = m_graph.getChildCells(m_graph.getDefaultParent(),true,false);
		for(int i = 0; i < cells.length; i++){
			mxCell node = (mxCell)cells[i];
			for(int j = 0; j < node.getEdgeCount(); j++){
				mxCell edge = (mxCell)node.getEdgeAt(j);	
				if(edge.getSource() != node){
					continue;
				}
				String initial_state = m_graph.getLabel(edge.getSource());
				String final_state = m_graph.getLabel(edge.getTarget());
				String edge_label =  m_graph.getLabel(edge);
				if(edge_label.indexOf(";") == -1){
					return "";
				}
				String symbol = edge_label.substring(0, edge_label.indexOf(";"));
				String action = edge_label.substring(edge_label.indexOf(";") + 1);
				String rule = initial_state + " " + symbol + " " + final_state + " " + action;
				machine_text += rule;
				machine_text += "\n";
			}
		}		
		return machine_text;
	}

	protected void mouseWheelMoved(MouseWheelEvent e)
	{
		if (e.getWheelRotation() < 0)
		{
			m_graph_component.zoomIn();			
		}
		else
		{
			m_graph_component.zoomOut();			
		}		
	}

	class MouseWheelTracker implements MouseWheelListener
	{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {			
			//if(e.isControlDown()){
			Graph.this.mouseWheelMoved(e);		
			//}				
		}
	};  
	
	class GraphKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE){
				Graph.this.RemoveSelectedCell();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

}
