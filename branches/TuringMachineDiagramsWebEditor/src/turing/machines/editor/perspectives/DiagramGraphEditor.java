//package turing.machines.editor.perspectives;
//
//import java.awt.BorderLayout;
//
//import javax.swing.JTabbedPane;
//
//import turing.machines.editor.EditorPerspective;
//import ui.utils.ClosableTabComponent;
//
//public class DiagramGraphEditor extends EditorPerspective {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	public DiagramGraphEditor(String name) {
//		super(name);
//		setLayout(new BorderLayout(0, 0));
//		
//		JTabbedPane m_diagrams_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		add(m_diagrams_tabbedPane, BorderLayout.CENTER);
//		
//		DiagramGraphDocument diagram_document = new DiagramGraphDocument();
//		if(m_diagrams_tabbedPane.getComponentCount() > 0){
//			m_diagrams_tabbedPane.addTab("New Diagram" + m_diagrams_tabbedPane.getComponentCount(), null, diagram_document, null);		
//		}else{
//			m_diagrams_tabbedPane.addTab("New Diagram", null, diagram_document, null);		
//		}
//		m_diagrams_tabbedPane.setSelectedComponent(diagram_document);
//		m_diagrams_tabbedPane.setTabComponentAt(m_diagrams_tabbedPane.getSelectedIndex(),new ClosableTabComponent(m_diagrams_tabbedPane));
//	}
//
//	@Override
//	public void New() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void Open() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void Save() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void Execute() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void SaveAs() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void Help() {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
