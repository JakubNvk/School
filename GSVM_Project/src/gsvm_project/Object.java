package gsvm_project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jn
 */
public class Object {

  ArrayList<ArrayList<Integer>> surfaces;
  HashMap<Integer, Vertex> vertices;
  int vertices_count;

  Object() {
    vertices_count = 0;
    vertices = new HashMap<>();
    surfaces = new ArrayList<>();
  }

  void addVertex(Vertex vertex) {
    vertices_count++;
    vertices.put(vertices_count, vertex);
  }

  Vertex getVertex(int key) {
    return vertices.get(key);
  }

  void addSurface(ArrayList surface) {
    surfaces.add(surface);
  }
  
  ArrayList<Integer> getSurface(int index) {
    return surfaces.get(index);
  }

  void clear() {
    vertices_count = 0;
    vertices.clear();
    surfaces.clear();
  }
}
