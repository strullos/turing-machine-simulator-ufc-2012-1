package graph;

import java.awt.Color;
import java.awt.Rectangle;
	import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.List;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxConnectorShape;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxRubberband;


public class Graph {
	protected mxGraph m_graph;
	private mxGraphComponent m_graph_component;
	private mxGraphOutline m_graph_outline;
	private mxRubberband m_graph_rubberband;
	private static int m_node_width = 100;
	private static int m_node_height = 100;
	private String m_node_style;
	private mxCell m_starting_node;
	
	class TestStyle implements mxEdgeStyle.mxEdgeStyleFunction{

		@Override
		public void apply(mxCellState state, mxCellState source, mxCellState target,
				List<mxPoint> relatives, List<mxPoint> result) {
			if(state != null)
				System.out.println("State: " + m_graph.getLabel(state.getCell()));
			if(source != null)
				System.out.println("Source: " + m_graph.getLabel(source.getCell()));
			if(target != null)
				System.out.println("Target: " + m_graph.getLabel(target.getCell()));
			if(relatives != null){
				for(int i = 0; i < relatives.size(); i++){
					System.out.println("Relatives: " + relatives.get(i).getX() + " " + relatives.get(i).getY());
				}
			}
			if(result != null){
					if(source != null){
						//
						System.out.println("Source: " + source.getX() + " " + source.getY());
						System.out.println("Point count: " +  state.getAbsolutePointCount());
						List<mxPoint> points = state.getAbsolutePoints();
						for(int i = 0; i < points.size(); i++){
							if(points.get(i) != null){
								System.out.println("Point " +  i +  ": " + points.get(i).getX() + " " + points.get(i).getY());
							}
						}
						if(points.get(points.size() - 1).getX() < source.getX()){
							double x = source.getX() - state.getLength() / 2;
							double y = source.getY();
							result.add(new mxPoint(x, y));
						}else{
							double x = source.getX() + state.getLength() / 2;
							double y = source.getY();
							result.add(new mxPoint(x, y));
						}
						
						
					}
					System.out.println("Edge source: " + state.getOrigin().getX());
					if(state != null){
						System.out.println("Width:" + state.getLength());
						System.out.println("Origin: " + state.getOrigin().getX() + " " + state.getOrigin().getY());
					}
					System.out.print("Result: ");
					for(int i = 0; i < result.size(); i++){
						if(result.get(i) != null){
							System.out.println("Result: " + result.get(i).getX() + " " + result.get(i).getY());
						}
					}
			}
		}
		
	}
	
	class Test2 extends mxConnectorShape{
		@Override
		public void paintShape(mxGraphics2DCanvas canvas,
                mxCellState state){
			canvas.paintRectangle(new Rectangle(100,200), new Color(50,50,50), new Color(100,100,100));
		}
	}
	
	
	public Graph(){		
		m_graph = new mxGraph();		
		m_graph_component = new mxGraphComponent(m_graph) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isPanningEvent(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON2){ //Middle Mouse Button
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
		edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
		edgeStyle.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		edgeStyle.put( mxConstants.STYLE_EDGE, new TestStyle() );
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

//		m_graph_component.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
//
//			@Override
//			public void invoke(Object arg0, mxEventObject arg1) {
//				System.out.println("ADDED!");
//
//			}
//		});
//
//		m_graph_component.addListener(mxEvent.LABEL_CHANGED,new mxEventSource.mxIEventListener() {
//
//			@Override
//			public void invoke(Object arg0, mxEventObject arg1) {
//				System.out.println("RENAMED");
//
//			}
//		});
		
		m_graph_component.addKeyListener(new GraphKeyListener());
		m_graph_component.getGraphControl().addMouseListener(new GraphMouseListener());
		m_graph_component.addMouseWheelListener(new MouseWheelTracker());	
	}	

	
	public void AddNode(String label)
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
	}	

	public void RemoveSelectedCell()
	{
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
	
	class GraphMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
