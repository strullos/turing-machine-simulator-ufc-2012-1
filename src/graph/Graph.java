package graph;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ui_utils.ConsoleComponent;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxRubberband;


public class Graph {
	protected mxGraph m_graph;
	private mxGraphComponent m_graph_component;
	private mxGraphOutline m_graph_outline;
	@SuppressWarnings("unused")
	private mxRubberband m_graph_rubberband;
	private static int m_node_width = 100;
	private static int m_node_height = 100;
	private String m_node_style;
	private mxCell m_starting_node;
	private boolean m_is_panning_allowed;
	private HashMap<mxCell,String> m_node_modules;
	private ConsoleComponent m_console;
	public Graph(ConsoleComponent console){	
		m_console = console;
		m_node_modules = new HashMap<mxCell,String>();
		m_is_panning_allowed = true;
		m_graph = new mxGraph();		
		m_graph_component = new mxGraphComponent(m_graph) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isPanningEvent(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON2 && m_is_panning_allowed){ //Middle Mouse Button
					return true;
				}
				return false;
			}
		};
		m_graph_rubberband = new mxRubberband(m_graph_component);

		m_node_style = new String("ROUNDED;fillColor=#FFFFFF");

		mxStylesheet stylesheet = m_graph.getStylesheet();
		HashMap<String, Object> style = new HashMap<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_OPACITY, 100);
		style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		style.put(mxConstants.STYLE_FONTSIZE, "30"); //!< Remove this later...
		style.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
		style.put(mxConstants.STYLE_SPACING_TOP, 5);
		style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		stylesheet.putCellStyle("ROUNDED", style);

		HashMap<String, Object> edgeStyle = new HashMap<String, Object>();
		edgeStyle.put(mxConstants.STYLE_SHAPE,    mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTSIZE, "30");
		edgeStyle.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
		edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
		edgeStyle.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		stylesheet.setDefaultEdgeStyle(edgeStyle);	

		m_graph.getModel().endUpdate();
		m_graph.setCellsResizable(true);
		m_graph.setAllowDanglingEdges(false);           
		m_graph.setAllowLoops(true);      
		//m_graph.setAutoSizeCells(true);

		m_graph_component.setGridVisible(true);	
		m_graph_component.setBackground(new Color(255,255,255));
		m_graph_component.setPageVisible(true);
		//	 PageFormat page_format = new PageFormat();
		//	 page_format.setOrientation(PageFormat.LANDSCAPE);
		//	 m_graph_component.setPageFormat(page_format);
		m_graph_component.getViewport().setOpaque(true);
		m_graph_component.getViewport().setBackground(Color.WHITE);

		m_graph_outline = new mxGraphOutline(this.m_graph_component);
		m_graph_component.getConnectionHandler().addListener(mxEvent.CONNECT, new AddCellListener());
		m_graph_component.addKeyListener(new GraphKeyListener());
		m_graph_component.addMouseWheelListener(new MouseWheelTracker());	
	}	


	public mxCell AddNode(String label)
	{
		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		mxCell node = (mxCell)m_graph.insertVertex(parent, null, label ,10, 10, m_node_width, m_node_height, m_node_style);
		m_graph.getModel().endUpdate();
		if(m_graph.getChildCells(m_graph.getDefaultParent(), true, false).length == 1){
			m_starting_node = node;
			node.setStyle("ROUNDED;fillColor=#beffaf");
			m_graph_component.refresh();
		}
		return node;
	}	
	
	public mxCell AddNode(String label, String corresponding_module)
	{
		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		mxCell node = (mxCell)m_graph.insertVertex(parent, null, label ,10, 10, m_node_width, m_node_height, m_node_style);
		m_graph.getModel().endUpdate();
		if(m_graph.getChildCells(m_graph.getDefaultParent(), true, false).length == 1){
			m_starting_node = node;
			node.setStyle("ROUNDED;fillColor=#beffaf");
			m_graph_component.refresh();
		}
		m_node_modules.put(node, corresponding_module);
		return node;
	}	

	public mxCell AddNode(String label, float x, float y, boolean is_starting_node)
	{
		Object parent = m_graph.getDefaultParent();
		m_graph.getModel().beginUpdate();
		mxCell node = (mxCell)m_graph.insertVertex(parent, null, label ,x, y, m_node_width, m_node_height, m_node_style);
		m_graph.getModel().endUpdate();
		if(is_starting_node){
			m_starting_node = node;
			node.setStyle("ROUNDED;fillColor=#beffaf");
			m_graph_component.refresh();
		}
		return node;
	}	

	public mxCell GetNode(String label)
	{
		Object[] cells = m_graph.getChildCells(m_graph.getDefaultParent(),true,false);
		for(int i = 0; i < cells.length; i++){
			mxCell node = (mxCell)cells[i];
			if(m_graph.getLabel(node).equals(label)){
				return node;
			}
		}
		return null;
	}
	
	public String GetSelectedNodeModule()
	{
		mxCell selected_node = (mxCell) m_graph.getSelectionCell();
		if(selected_node != null && selected_node.isVertex()){
			return m_node_modules.get(selected_node);
		}
		return null;		
	}
	
	public void SetModuleOfSelectedNode(String module)
	{
		mxCell selected_node = (mxCell) m_graph.getSelectionCell();
		m_node_modules.put(selected_node, module);
	}


	public void RemoveSelectedCell()
	{
		for(int i = 0; i < m_graph.getSelectionCells().length; i++){
			mxCell node = (mxCell) m_graph.getSelectionCells()[i];
			if(node.isVertex()){
				if(m_node_modules.get(node) != null){
					m_node_modules.remove(node);
				}
			}
		}
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
					m_console.AppendText("Failed to parse edge from " + initial_state + " to " + final_state + "\n");
					m_console.AppendText("Edge: " + edge_label + "\n");
					m_console.AppendText("Expected ; character.\n");
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
	
	public String GenerateTuringDiagram(){
		if( m_graph.getChildCells(m_graph.getDefaultParent(),true,false).length == 0){
			return "";
		}
		String diagram_text = "";
		Object[] cells = m_graph.getChildCells(m_graph.getDefaultParent(),true,false);
		for(int i = 0; i < cells.length; i++){
			mxCell node = (mxCell)cells[i];
			if(node.isVertex()){
				if(m_graph.getLabel(node).equals(m_graph.getLabel(m_starting_node))){
					//Marks the initial state
					diagram_text += "module %" + m_graph.getLabel(node) + " " + m_node_modules.get(node) + "\n";
				}else{
					diagram_text += "module " + m_graph.getLabel(node) + " " + m_node_modules.get(node) + "\n";
				}
			}
		}
		diagram_text += "\n";
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
				if(!edge_label.startsWith("[") && !edge_label.endsWith("]")){
					return "";
				}
				String rule = initial_state + " " + edge_label + " " + final_state;
				diagram_text += rule + "\n";			
			}
		}		
		return diagram_text;
	}

	//#v label x y width height true module (to do)
	//#e label source target x y ...
	public String ExportGraph(){
		String graph_text = "";
		String vertices = "";
		String edges = "";
		Object[] cells = m_graph.getChildCells(m_graph.getDefaultParent(),true,false);
		for(int i = 0; i < cells.length; i++){
			mxCell node = (mxCell)cells[i];
			if(!node.isVertex()){
				continue;
			}
			vertices += "v " + m_graph.getLabel(node) + " " + node.getGeometry().getX() + " " + node.getGeometry().getY();
			if(node == m_starting_node){
				vertices += " true";
			}else{
				vertices += " false";
			}
			//If it is a diagram graph, the node's modules should be stored
			if(m_node_modules.get(node) != null){
				vertices += " ";
				vertices += m_node_modules.get(node);
			}else{
				vertices += " null";
			}
			vertices += "\n";
			for(int j = 0; j < node.getEdgeCount(); j++){
				mxCell edge = (mxCell)node.getEdgeAt(j);	
				if(edge.getSource() != node){
					continue;
				}
				String initial_state = m_graph.getLabel(edge.getSource());
				String final_state = m_graph.getLabel(edge.getTarget());
				String edge_label =  m_graph.getLabel(edge);				

				edges += "e " + edge_label + " " + initial_state + " " + 
						final_state + " " + edge.getGeometry().getPoints().get(0).getX() + " " + 
						edge.getGeometry().getPoints().get(0).getY() + "\n"; 
			}
		}		
		graph_text += vertices;
		graph_text += edges;
		return graph_text;
	}

	public void ImportGraph(String graph_text) throws IOException
	{
		BufferedReader r = new BufferedReader(new StringReader(graph_text));
		String line;
		while((line = r.readLine()) != null){
			if(line.startsWith("v")){
				line = line.substring(line.indexOf(" ") + 1);
				String node_label = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);				
				String node_x = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);				
				String node_y = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);
				String starting_node = line.substring(0,line.indexOf(" "));				
				boolean is_starting_node = false;
				if(starting_node.equals("true")){
					is_starting_node = true;
				}
				if(starting_node.equals("false")){
					is_starting_node = false;
				}
				String node_module = line.substring(line.indexOf(" ") + 1);				
				mxCell node = this.AddNode(node_label,Float.parseFloat(node_x),Float.parseFloat(node_y), is_starting_node);
				if(!node_module.equals("null")){
					m_node_modules.put(node, node_module);
				}
			}
			if(line.startsWith("e")){
				line = line.substring(line.indexOf(" ") + 1);
				String edge_label = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);				
				String edge_source = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);				
				String edge_target = line.substring(0, line.indexOf(" "));
				line = line.substring(line.indexOf(" ") + 1);				
				String edge_x = line.substring(0, line.indexOf(" "));
				String edge_y = line.substring(line.indexOf(" ") + 1);				
				if(GetNode(edge_source) != null && GetNode(edge_target) != null){
					m_graph.getModel().beginUpdate();				
					mxCell edge = (mxCell)m_graph.insertEdge(m_graph.getDefaultParent(), null, edge_label, GetNode(edge_source), GetNode(edge_target));
					mxGeometry g = edge.getGeometry();
					List<mxPoint> points = new ArrayList<mxPoint>();	
					points.add(new mxPoint(Float.parseFloat(edge_x),Float.parseFloat(edge_y)));
					g.setPoints(points);
					m_graph.getModel().endUpdate();
				}

			}
		}
	}
	
	public void SetStartingNode()
	{
		mxCell selected_node = (mxCell)m_graph_component.getGraph().getSelectionCell();
		if(selected_node != null && selected_node.isVertex()){
			m_graph.getModel().beginUpdate();
			m_starting_node.setStyle("ROUNDED;fillColor=#ffffff");
			m_starting_node = selected_node;			
			m_starting_node.setStyle("ROUNDED;fillColor=#beffaf");
			m_graph_component.refresh();
			m_graph.getModel().endUpdate();
			
		}
	}

	protected void mouseWheelMoved(MouseWheelEvent e)
	{
		if (e.getWheelRotation() < 0)
		{
			if(!m_graph_component.getPanningHandler().isActive()){
				m_graph_component.zoomIn();			
			}
		}
		else
		{
			if(!m_graph_component.getPanningHandler().isActive()){
				m_graph_component.zoomOut();	
			}
		}		
	}

	class MouseWheelTracker implements MouseWheelListener
	{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {			
			Graph.this.mouseWheelMoved(e);		
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

	class AddCellListener implements mxIEventListener{
		@Override
		public void invoke(Object arg0, mxEventObject arg1) {
			mxCell edge = (mxCell) arg1.getProperty("cell");
			if(edge.getTarget() != edge.getSource()){
				mxGeometry g = edge.getGeometry();
				List<mxPoint> points = new ArrayList<mxPoint>();	
				double x_origin = edge.getSource().getGeometry().getCenterX();
				double y_origin = edge.getSource().getGeometry().getCenterY();
				double x = edge.getTarget().getGeometry().getCenterX();
				double y =  edge.getTarget().getGeometry().getCenterY();
				double x_dir = x - x_origin;
				double y_dir = y - y_origin;
				double length = Math.sqrt((x_dir*x_dir) + (y_dir*y_dir));
				double x_dir_n = x_dir / length;
				double y_dir_n = y_dir / length;
				double x_point = x_origin + ((length / 2) * x_dir_n);
				double y_point = y_origin + ((length / 2) * y_dir_n);
				double x_dir_np = -y_dir_n;
				double y_dir_np = x_dir_n;
				points.add(0, new mxPoint((x_point + (50 * x_dir_np)), (y_point + (50 * y_dir_np))));
				g.setPoints(points);						
				m_graph_component.refresh();
			}
		}

	}
}
