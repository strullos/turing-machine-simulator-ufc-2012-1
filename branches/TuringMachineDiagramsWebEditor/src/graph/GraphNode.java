package graph;

public class GraphNode {
	int m_x_pos;
	int m_y_pos;
	String m_node_label;
	public GraphNode(String node_label, int x_pos, int y_pos){
		m_node_label = new String(node_label);
		m_x_pos = x_pos;
		m_y_pos = y_pos;
	}
	
	public String GetLabel()
	{
		return m_node_label;
	}
	
	public int GetXPos()
	{
		return m_x_pos;
	}
	
	public int GetYPos()
	{
		return m_y_pos;
	}
}
