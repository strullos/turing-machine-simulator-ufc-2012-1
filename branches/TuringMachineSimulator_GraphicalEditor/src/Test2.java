import javax.swing.JFrame;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Test2 extends JFrame
{

   /**
    * 
    */
   private static final long serialVersionUID = -2707712944901661771L;

   public Test2()
   {
      super("Hello, World!");

      mxGraph graph = new mxGraph();
      Object parent = graph.getDefaultParent();

      graph.getModel().beginUpdate();
      try
      {
         Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
               30);
         Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
               80, 30);
         graph.insertEdge(parent, null, "Edge", v1, v2);
         graph.insertEdge(parent, null, "Edge", v2, v1);
      }
      finally
      {
         graph.getModel().endUpdate();
      }
      
      mxParallelEdgeLayout layout = new mxParallelEdgeLayout(graph);
      layout.execute(graph.getDefaultParent());

      mxGraphComponent graphComponent = new mxGraphComponent(graph);
      getContentPane().add(graphComponent);
   }

   public static void main(String[] args)
   {
      Test2 frame = new Test2();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 320);
      frame.setVisible(true);
   }

}