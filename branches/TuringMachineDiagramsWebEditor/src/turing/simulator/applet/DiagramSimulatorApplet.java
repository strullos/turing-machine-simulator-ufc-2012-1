package turing.simulator.applet;


import javax.swing.JApplet;

import turing.simulator.panels.ExecuteMachinePanel;

@SuppressWarnings("serial")
public class DiagramSimulatorApplet extends JApplet {
	
	private static final int FRAME_WIDTH = 480;
	private static final int FRAME_HEIGHT = 640;
	
	/**
	 * Create the applet.
	 */
	public DiagramSimulatorApplet() {
		ExecuteMachinePanel panel = new ExecuteMachinePanel();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.getContentPane().add(panel);
		
		this.setVisible(true);
	}

}
