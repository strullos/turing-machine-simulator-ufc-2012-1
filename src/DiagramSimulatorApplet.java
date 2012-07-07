import javax.swing.JApplet;

import turing.simulator.panels.ModulesEditor;

@SuppressWarnings("serial")
public class DiagramSimulatorApplet extends JApplet {
	
	private static final int FRAME_WIDTH = 480;
	private static final int FRAME_HEIGHT = 640;
	
	/**
	 * Creates the applet.
	 */
	public DiagramSimulatorApplet() {
		ModulesEditor modules_editor = new ModulesEditor();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.getContentPane().add(modules_editor);		
		this.setVisible(true);
	}

}