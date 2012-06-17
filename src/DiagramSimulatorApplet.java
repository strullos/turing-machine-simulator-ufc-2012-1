


import javax.swing.JApplet;

import turing.simulator.panels.DiagramTextEditor;
import turing.simulator.panels.MachineTextEditor;

@SuppressWarnings("serial")
public class DiagramSimulatorApplet extends JApplet {
	
	private static final int FRAME_WIDTH = 480;
	private static final int FRAME_HEIGHT = 640;
	
	/**
	 * Creates the applet.
	 */
	public DiagramSimulatorApplet() {
		MachineTextEditor machineEditor = new MachineTextEditor();
		DiagramTextEditor diagramEditor = new DiagramTextEditor();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.getContentPane().add(diagramEditor);		
		this.setVisible(true);
	}

}
