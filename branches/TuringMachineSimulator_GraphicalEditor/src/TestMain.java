
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.*;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

public class TestMain extends JPanel {
	mxGraph graph;
	mxGraphComponent graphComponent;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestMain(){
		initGUI();
	}
	
	public void initGUI(){
		setSize(800,600);
		graph = new mxGraph();
		
		
		
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setPreferredSize(new Dimension(400,400));
	
		add(graphComponent);
		
		graph.getModel().beginUpdate();
		Object parent = graph.getDefaultParent();
		mxStylesheet stylesheet = graph.getStylesheet();
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_OPACITY, 50);
		style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
		
		stylesheet.putCellStyle("ROUNDED", style);
		Object test = new Object();
		Object v1 = graph.insertVertex(parent, null, "q0", 30, 80, 50, 50, "ROUNDED");
		Object v2 = graph.insertVertex(parent, "a", "q1", 100, 80, 50, 50, "ROUNDED");
		Object e1 = graph.insertEdge(parent, null, "A / >", v1, v2,"labelBackgroundColor=white");
		Object e2 = graph.insertEdge(parent, null, "B / >", v2, v1,"labelBackgroundColor=white");
		
		
		graph.getModel().endUpdate();
		graph.setCellsResizable(false);
		graph.setAllowDanglingEdges(false);
		graph.setAllowLoops(true);
		
		mxParallelEdgeLayout layout = new mxParallelEdgeLayout(graph);
		layout.execute(graph.getDefaultParent());
		
		Object[] edges = graph.getChildEdges(graph.getDefaultParent());
		
		for(Object e : edges){
			System.out.println(graph.getView().getState(e,true).getLabel());
		}
		
	}
}
