package graph;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxPanningHandler;

public class Graph {
	protected mxGraph m_graph;
	private mxGraphComponent m_graph_component;
	private HashMap<String, GraphNode> m_nodes;
	private static int m_node_width = 25;
	private static int m_node_height = 25;
	private String m_node_style;
	
	public Graph(){
		m_graph = new mxGraph();		
		m_graph_component = new mxGraphComponent(m_graph);
		m_node_style = new String("ROUNDED;fillColor=#FF0000");
		m_nodes = new HashMap<String,GraphNode>();
		
	 mxStylesheet stylesheet = m_graph.getStylesheet();
	 HashMap<String, Object> style = new HashMap<String, Object>();
     style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
     style.put(mxConstants.STYLE_OPACITY, 50);
     style.put(mxConstants.STYLE_FONTCOLOR, "#FFFFFF");
     style.put(mxConstants.STYLE_FONTSIZE, "8");
 
         
//         HashMap<String, Object> edge = new HashMap<String, Object>();       
//         edge = (HashMap<String,Object>)stylesheet.getDefaultEdgeStyle();
//         edge.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");           
//     edge.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
//     edge.put(mxConstants.STYLE_FONTSIZE, "16");
//     stylesheet.setDefaultEdgeStyle(edge);
     stylesheet.putCellStyle("ROUNDED", style);
     m_graph.getModel().endUpdate();
     m_graph.setCellsResizable(false);
     m_graph.setAllowDanglingEdges(false);           
     m_graph.setAllowLoops(true);      
	 m_graph_component.setGridVisible(true);	
	 m_graph_component.setBackground(new Color(255,255,255));
    
     MouseWheelListener wheelTracker = new MouseWheelListener()	{
			/**
			 * 
			 */
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				Graph.this.mouseWheelMoved(e);
			}
		};    	
		
	 m_graph_component.addMouseWheelListener(wheelTracker);
	 
	 mxPanningHandler panning_handler = new mxPanningHandler(m_graph_component){
		 private int m_x_pos;
		 private int m_y_pos;		 
		 public void mousePressed(MouseEvent e)
		 {
			 m_x_pos = e.getX();
			 m_y_pos = e.getY();
		 }
		 public void mouseDragged(MouseEvent e)
		 {
			 int current_x = e.getX();
			 int current_y = e.getY();			 
			
			 mxPoint current_translation = Graph.this.m_graph.getView().getTranslate();
			 double delta_x = current_x - m_x_pos;
			 double delta_y = current_y - m_y_pos;
			 double x_translation = current_translation.getX() + delta_x;
			 double y_translation = current_translation.getY() + delta_y;
			 System.out.print("X: " + delta_x + " Y: " + delta_y + "\n");
			 Graph.this.m_graph.getView().setTranslate(new mxPoint(x_translation,y_translation));			 
		 }
	 };	 
	 m_graph_component.addMouseMotionListener(panning_handler);
	 
	 m_graph_component.getViewport().setOpaque(true);
	 m_graph_component.getViewport().setBackground(Color.WHITE);
	}	

	public void AddNode(GraphNode node)
	{
		m_nodes.put(node.GetLabel(), node);
		Object parent = m_graph.getDefaultParent();
        m_graph.getModel().beginUpdate();
        m_graph.insertVertex(parent, null, node.GetLabel() ,node.GetXPos(), node.GetYPos(), m_node_width, m_node_height, m_node_style);
        m_graph.getModel().endUpdate();		
	}
	
	public boolean ContainsNode(String label)
	{
		return m_nodes.containsKey(label);
	}
	
	public mxGraphComponent GetGraphComponent()
	{
		return m_graph_component;
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
	
}
