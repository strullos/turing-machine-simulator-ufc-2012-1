package graph;

import java.util.HashMap;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;

import ui_utils.ConsoleComponent;

public class DiagramGraph extends Graph {

	public DiagramGraph(ConsoleComponent console) 
	{
		super(console);
		m_node_style = new String("shape=rectangle;fillColor=#FFFFFF");
		mxStylesheet stylesheet = m_graph.getStylesheet();
		m_graph.getModel().beginUpdate();
		HashMap<String, Object> style = new HashMap<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		style.put(mxConstants.STYLE_OPACITY, 100);
		style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		style.put(mxConstants.STYLE_FONTSIZE, "30"); //!< Remove this later...
		style.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
		style.put(mxConstants.STYLE_SPACING_TOP, 5);
		style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		stylesheet.setDefaultVertexStyle(style);

		HashMap<String, Object> edgeStyle = new HashMap<String, Object>();
		edgeStyle.put(mxConstants.STYLE_SHAPE,    mxConstants.SHAPE_CURVE);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTSIZE, "30");
		edgeStyle.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
		edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
		edgeStyle.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#000000");
		stylesheet.setDefaultEdgeStyle(edgeStyle);	

		m_graph.getModel().endUpdate();
	}
}
