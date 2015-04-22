package gsvm_project;

import static gsvm_project.MatrixOperations.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jn
 */
public class Object {

  ArrayList<ArrayList<Integer>> surfaces;
  HashMap<Integer, Vertex> vertices;
  float x, y, z;
  int vertices_count;
  Color colour;
  Vertex light_vertex;

  Object() {
    x = 0;
    y = 0;
    z = 0;
    vertices_count = 0;
    light_vertex = new Vertex(0, 0, 0);
    colour = new Color((Color.white).getRGB());
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

  void rotateX(double angle) {
    float[][] rotate_x_matrix = new float[4][4];
    float previous_x = x;
    float previous_y = y;
    float previous_z = z;

    translateX(-x);
    translateY(-y);
    translateZ(-z);

    initMatrix(rotate_x_matrix);
    setMatrixValue(rotate_x_matrix, 0, 0, 1);
    setMatrixValue(rotate_x_matrix, 1, 1, (float) Math.cos(angle));
    setMatrixValue(rotate_x_matrix, 1, 2, (float) Math.sin(angle));
    setMatrixValue(rotate_x_matrix, 2, 1, (float) -Math.sin(angle));
    setMatrixValue(rotate_x_matrix, 2, 2, (float) Math.cos(angle));
    setMatrixValue(rotate_x_matrix, 3, 3, 1);

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(rotate_x_matrix);
        vertices.put(i, vertex);
      }
    }

    translateX(previous_x);
    translateY(previous_y);
    translateZ(previous_z);
  }

  void rotateY(double angle) {
    float[][] rotate_y_matrix = new float[4][4];
    float previous_x = x;
    float previous_y = y;
    float previous_z = z;

    translateX(-x);
    translateY(-y);
    translateZ(-z);

    initMatrix(rotate_y_matrix);
    setMatrixValue(rotate_y_matrix, 0, 0, (float) Math.cos(angle));
    setMatrixValue(rotate_y_matrix, 0, 2, (float) Math.sin(angle));
    setMatrixValue(rotate_y_matrix, 1, 1, 1);
    setMatrixValue(rotate_y_matrix, 2, 0, (float) -Math.sin(angle));
    setMatrixValue(rotate_y_matrix, 2, 2, (float) Math.cos(angle));
    setMatrixValue(rotate_y_matrix, 3, 3, 1);

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(rotate_y_matrix);
        vertices.put(i, vertex);
      }
    }

    translateX(previous_x);
    translateY(previous_y);
    translateZ(previous_z);
  }

  void rotateZ(double angle) {
    float[][] rotate_z_matrix = new float[4][4];
    float previous_x = x;
    float previous_y = y;
    float previous_z = z;

    translateX(-x);
    translateY(-y);
    translateZ(-z);

    initMatrix(rotate_z_matrix);
    setMatrixValue(rotate_z_matrix, 0, 0, (float) Math.cos(angle));
    setMatrixValue(rotate_z_matrix, 0, 1, (float) Math.sin(angle));
    setMatrixValue(rotate_z_matrix, 1, 0, (float) -Math.sin(angle));
    setMatrixValue(rotate_z_matrix, 1, 1, (float) Math.cos(angle));
    setMatrixValue(rotate_z_matrix, 2, 2, 1);
    setMatrixValue(rotate_z_matrix, 3, 3, 1);

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(rotate_z_matrix);
        vertices.put(i, vertex);
      }
    }

    translateX(previous_x);
    translateY(previous_y);
    translateZ(previous_z);
  }

  void translateX(float range) {
    float[][] translate_x_matrix = new float[4][4];

    initMatrix(translate_x_matrix, 1, 1, 1, 1);
    setMatrixValue(translate_x_matrix, 0, 3, range);
    x += range;

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(translate_x_matrix);
        vertices.put(i, vertex);
      }
    }
  }

  void translateY(float range) {
    float[][] translate_y_matrix = new float[4][4];

    initMatrix(translate_y_matrix, 1, 1, 1, 1);
    setMatrixValue(translate_y_matrix, 1, 3, range);
    y += range;

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(translate_y_matrix);
        vertices.put(i, vertex);
      }
    }
  }

  void translateZ(float range) {
    float[][] translate_z_matrix = new float[4][4];

    initMatrix(translate_z_matrix, 1, 1, 1, 1);
    setMatrixValue(translate_z_matrix, 2, 3, range);
    z += range;

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(translate_z_matrix);
        vertices.put(i, vertex);
      }
    }
  }

  void scaleUp() {
    float upscale = (float) 1.1;
    float[][] upscale_matrix = new float[4][4];
    initMatrix(upscale_matrix, upscale, upscale, upscale, 1);

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(upscale_matrix);
        vertices.put(i, vertex);
      }
    }
  }

  void scaleDown() {
    float downscale = (float) 0.9;
    float[][] downscale_matrix = new float[4][4];
    initMatrix(downscale_matrix, downscale, downscale, downscale, 1);

    if (vertices_count > 0) {
      for (int i = 1; i < vertices_count + 1; i++) {
        Vertex vertex = vertices.get(i);

        vertices.remove(i);
        vertex.multiplyVertex(downscale_matrix);
        vertices.put(i, vertex);
      }
    }
  }

  void setLight(Vertex vertex) {
    // TODO
  }

  void hideInvisibleFaces() {
    // TODO
  }

  void unhideInvisibleFaces() {
    // TODO
  }

  void renderColour() {
    // TODO
  }

  void clear() {
    vertices_count = 0;
    vertices.clear();
    surfaces.clear();
  }
}
