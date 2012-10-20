package turing.machines.editor;

import javax.swing.JApplet;

public class TuringMachinesEditorApplet extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	
	/**
	 * Creates the applet.
	 */
	public TuringMachinesEditorApplet() {
		TuringMachinesEditor turing_machine_editor = new TuringMachinesEditor();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.getContentPane().add(turing_machine_editor);		
		this.setVisible(true);
	}
}
