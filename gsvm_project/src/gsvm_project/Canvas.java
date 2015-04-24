package gsvm_project;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author jn
 */
public class Canvas extends JPanel {

  Object object;

  public Canvas(Object object) {
    this.object = object;

    setSize(600, 400);
    setLocation(0, 0);
    setBackground(Color.white);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (ArrayList<Integer> surface : object.surfaces) {
      ArrayList<Vertex> vertices = new ArrayList<>();
      for (Integer vertex_index : surface) {
        vertices.add(object.getVertex(vertex_index));
      }

      int vertices_size = vertices.size();
      int x[] = new int[vertices_size];
      int y[] = new int[vertices_size];
      int i = 0;
      for (Vertex vertex : vertices) {
        x[i] = (int) (vertex.x * 100) + 300;
        y[i] = (int) (-vertex.y * 100) + 200;
        i++;
      }
      g.drawPolygon(x, y, vertices_size);
    }
  }
}
