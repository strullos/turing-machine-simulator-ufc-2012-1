package graph;


public class GraphDirectedEdge {
	private GraphNode m_source;
	private GraphNode m_destiny;
	private String m_edge_label;
	public GraphDirectedEdge(GraphNode source, GraphNode destiny, String edge_label) 
	{
		m_source = source;
		m_destiny = destiny;
		m_edge_label = edge_label;
	}
	
	public String GetLabel() 
	{
		return m_edge_label;
	}
	
	public String GetSourceLabel()
	{
		return m_source.GetLabel();
	}
	
	public String GetDestinyLabel()
	{
		return m_destiny.GetLabel();
	}
}
