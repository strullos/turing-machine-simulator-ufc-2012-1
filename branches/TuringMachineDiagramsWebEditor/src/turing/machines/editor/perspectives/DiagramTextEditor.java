package turing.machines.editor.perspectives;

import turing.machines.editor.EditorPerspective;
import ui.utils.ClosableTabComponent;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DiagramTextEditor extends EditorPerspective {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DiagramTextDocument m_current_diagram_document;

	private JTabbedPane m_diagrams_tabbedPane;
	public DiagramTextEditor(String name) {
		super(name);
		setLayout(new BorderLayout(0, 0));
		
		m_diagrams_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(m_diagrams_tabbedPane, BorderLayout.CENTER);
		
		NewDiagramDocument();
	}
	
	public void NewDiagramDocument()
	{
		DiagramTextDocument diagram_document = new DiagramTextDocument();
		m_current_diagram_document = diagram_document;
		if(m_diagrams_tabbedPane.getComponentCount() > 0){
			m_diagrams_tabbedPane.addTab("New Machine" + m_diagrams_tabbedPane.getComponentCount(), null, m_current_diagram_document, null);		
		}else{
			m_diagrams_tabbedPane.addTab("New Machine", null, m_current_diagram_document, null);		
		}
		m_diagrams_tabbedPane.setSelectedComponent(diagram_document);
		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
	}

	@Override
	public void New() {
		NewDiagramDocument();
	}

	@Override
	public void Open() {
	}

	@Override
	public void Save() {
	}

	@Override
	public void Execute() {
	}
	
	class TabChangedListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			if( DiagramTextEditor.this.m_diagrams_tabbedPane.getSelectedIndex() != -1){
				DiagramTextEditor.this.m_current_diagram_document = (DiagramTextDocument) DiagramTextEditor.this.m_diagrams_tabbedPane.getSelectedComponent();
			}else{
				DiagramTextEditor.this.m_current_diagram_document = null;
			}						
		}		
		
	}

}
