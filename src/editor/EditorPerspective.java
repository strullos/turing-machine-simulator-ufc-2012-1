package editor;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public abstract class EditorPerspective extends JPanel {
	/**
	 * This class represents the many editor perspectives the TuringEditor may have. For example,
	 * the MachineTextEditor perspective, the DiagramTextEditorPerspective, the DiagramGraphEditor perspective, etc...
	 * If a new perspective has to be created, the programmer only has to create a new specialization of this class
	 * and implement the abstract methods (that are called by the toolbar)
	 * TODO: Maybe implement listeners and actions to bind the toolbar buttons instead of abstract methods... (?)
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
	
	public abstract void New(); //!< This method is called when the new button is pressed, on the current perspective
	public abstract void Open(String file_path);  //!<  * open button is pressed *
	public abstract void Save();  //!<  * save button is pressed *
	public abstract void SaveAs(); //!< * save as button is pressed *
	public abstract void Execute();  //!<  * execute button is pressed *
	public abstract void Help(); //!< This method should display the help of the perspective and it's called when the "Help" button is pressed
	public abstract void Examples();
	public abstract void HandleKeyEvents(KeyEvent e);
}
