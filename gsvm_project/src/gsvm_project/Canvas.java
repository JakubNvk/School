package gsvm_project;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author JakubNvk
 */
public class Canvas extends JPanel {

  Object object;

  /**
   * Creates a 600x400 graphical canvas with white background.
   * 
   * @param object object that contains model data
   */
  public Canvas(Object object) {
    this.object = object;

    setSize(600, 400);
    setLocation(0, 0);
    setBackground(Color.white);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    int index = 0;
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

      if (!object.isHidden) {
        g.drawPolygon(x, y, vertices_size);
      } else {
        Vertex v0 = vertices.get(0);
        Vertex v1 = vertices.get(1);
        Vertex v2 = vertices.get(2);
        float[] vector_a = {v2.x - v1.x, v2.y - v1.y, v2.z - v1.z};
        float[] vector_b = {v1.x - v0.x, v1.y - v0.y, v1.z - v0.z};
        float[] vector_normal = {
          vector_a[1] * vector_b[2] - vector_a[2] * vector_b[1],
          vector_a[2] * vector_b[0] - vector_a[0] * vector_b[2],
          vector_a[0] * vector_b[1] - vector_a[1] * vector_b[0]};

        if (vector_normal[2] >= 0) {
          g.setColor(object.getColour(index));
          g.fillPolygon(x, y, vertices_size);
          g.setColor(Color.black);
          g.drawPolygon(x, y, vertices_size);
        }
      }
      index++;
    }
  }
}
