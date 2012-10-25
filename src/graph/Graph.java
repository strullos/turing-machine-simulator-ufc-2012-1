package graph;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.util.HashMap;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxPanningHandler;
import com.sun.org.apache.bcel.internal.generic.LAND;

public class Graph {
	protected mxGraph m_graph;
	private mxGraphComponent m_graph_component;
	private mxGraphOutline m_graph_outline;
	private HashMap<String, GraphNode> m_nodes;
	private HashMap<mxCell, GraphNode> m_cell_node_map;
	private static int m_node_width = 25;
	private static int m_node_height = 25;
	private String m_node_style;
	
	public Graph(){
		m_graph = new mxGraph();		
		m_graph_component = new mxGraphComponent(m_graph);
		m_node_style = new String("ROUNDED;fillColor=#FF0000");
		m_nodes = new HashMap<String,GraphNode>();
		m_cell_node_map = new HashMap<mxCell, GraphNode>();
		
	 mxStylesheet stylesheet = m_graph.getStylesheet();
	 HashMap<String, Object> style = new HashMap<String, Object>();
     style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
     style.put(mxConstants.STYLE_OPACITY, 100);
     style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
     style.put(mxConstants.STYLE_FONTSIZE, "12");
 
         
//         HashMap<String, Object> edge = new HashMap<String, Object>();       
//         edge = (HashMap<String,Object>)stylesheet.getDefaultEdgeStyle();
//         edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");           
//     edge.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
//     edge.put(mxConstants.STYLE_FONTSIZE, "16");
//     stylesheet.setDefaultEdgeStyle(edge);
     stylesheet.putCellStyle("ROUNDED", style);
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
	}	

	public void AddNode(GraphNode node)
	{
		m_nodes.put(node.GetLabel(), node);
		Object parent = m_graph.getDefaultParent();
        m_graph.getModel().beginUpdate();
        mxCell new_vertex = (mxCell) m_graph.insertVertex(parent, null, node.GetLabel() ,node.GetXPos(), node.GetYPos(), m_node_width, m_node_height, m_node_style);
        m_cell_node_map.put(new_vertex, node);
        m_graph.getModel().endUpdate();		
	}	
	
	public void RemoveSelectedNode()
	{
		GraphNode selected_node = GetSelectedNode();
		m_nodes.remove(selected_node.GetLabel());
		m_cell_node_map.remove(m_graph.getSelectionCells());
		m_graph.removeCells(m_graph.getSelectionCells());
	}
	
	public boolean ContainsNode(String label)
	{
		return m_nodes.containsKey(label);
	}
	
	public mxGraphComponent GetGraphComponent()
	{
		return m_graph_component;
	}
	
	public GraphNode GetSelectedNode()
	{
		return m_cell_node_map.get(m_graph.getSelectionCell());
	}
	
	public mxGraphOutline GetGraphOutline()
	{
		return m_graph_outline;
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
	
}
