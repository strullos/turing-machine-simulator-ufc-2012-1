package turing.machines.editor;

import javax.swing.JPanel;

public abstract class EditorPerspective extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_name;
	public EditorPerspective(String name)
	{
		m_name = new String(name);
	}
	
	public String Name()
	{
		return m_name;
	}
	
	public abstract void Open();
	public abstract void Save();
	public abstract void Execute();
}
