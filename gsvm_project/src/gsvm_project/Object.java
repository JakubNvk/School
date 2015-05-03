package gsvm_project;

import static gsvm_project.MatrixOperations.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author JakubNvk
 */
public class Object {

  ArrayList<ArrayList<Integer>> surfaces;
  HashMap<Integer, Vertex> vertices;
  ArrayList<Color> colours;
  ArrayList<Color> shades;
  float x, y, z;
  int vertices_count;
  Color colour;
  Vertex light_vertex;
  boolean isLit;
  boolean isHidden;

  Object() {
    x = 0;
    y = 0;
    z = 0;
    isLit = false;
    isHidden = false;
    vertices_count = 0;
    light_vertex = new Vertex(0, 0, 0);
    colour = new Color((Color.white).getRGB());
    colours = new ArrayList<>();
    shades = new ArrayList<>();
    surfaces = new ArrayList<>();
    light_vertex = new Vertex(0, 0, 0);
    vertices = new HashMap<>();
  }

  /**
   * Add vertex to object.
   *
   * @param vertex vertex to be added
   */
  void addVertex(Vertex vertex) {
    vertices_count++;
    vertices.put(vertices_count, vertex);
  }

  /**
   * Get vertex from object at specified position in vertex list.
   *
   * @param key the key whose associated vertex is to be returned
   * @return the vertex to which the specified key is mapped, or null if this
   * map contains no mapping for the key
   */
  Vertex getVertex(int key) {
    return vertices.get(key);
  }

  /**
   * Add surface to object.
   *
   * @param surface surface to be added
   */
  void addSurface(ArrayList surface) {
    surfaces.add(surface);
    shades.add(colour);
    colours.add(colour);
  }

  /**
   * Get surface from object at specified position in surface list.
   *
   * @param index index of the surface to return
   * @return the surface at the specified position in this list
   */
  ArrayList<Integer> getSurface(int index) {
    return surfaces.get(index);
  }

  /**
   * Rotates object over the X axis.
   *
   * @param angle the angle through which the object should be rotated
   */
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

    if (isLit) {
      renderLight();
    }
  }

  /**
   * Rotates object over the Y axis.
   *
   * @param angle the angle through which the object should be rotated
   */
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

    if (isLit) {
      renderLight();
    }
  }

  /**
   * Rotates object over the Z axis.
   *
   * @param angle the angle through which the object should be rotated
   */
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

    if (isLit) {
      renderLight();
    }
  }

  /**
   * Translates object over X axis.
   *
   * @param range length by which the vector is to be moved
   */
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

      if (isLit) {
        renderLight();
      }
    }
  }

  /**
   * Translates object over Y axis.
   *
   * @param range length by which the vector is to be moved
   */
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

      if (isLit) {
        renderLight();
      }
    }
  }

  /**
   * Translates object over Z axis.
   *
   * @param range length by which the vector is to be moved
   */
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

      if (isLit) {
        renderLight();
      }
    }
  }

  /**
   * Scales the object up.
   */
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

  /**
   * Scales the object down.
   */
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

  /**
   * Hides invisible faces of the object.
   */
  void hideInvisibleFaces() {
    for (int i = 0; i < colours.size(); i++) {
      colours.set(i, Color.white);
    }
    isHidden = true;
  }

  /**
   * Shows invisible faces of the object.
   */
  void unhideInvisibleFaces() {
    isHidden = false;
    isLit = false;
  }

  /**
   * Set vertex of light which is shining on object.
   *
   * @param light_vertex light vertex to be set
   */
  void setLight(Vertex light_vertex) {
    this.light_vertex = light_vertex;
  }

  /**
   * Render object with set light.
   */
  void renderLight() {
    if (!isHidden) {
      hideInvisibleFaces();
    }

    int index = 0;
    for (ArrayList<Integer> surface : surfaces) {
      ArrayList<Vertex> surface_vertices = new ArrayList<>();
      for (Integer surface_point : surface) {
        surface_vertices.add(getVertex(surface_point));
      }

      float average_x = 0;
      float average_y = 0;
      float average_z = 0;
      for (Vertex vertex : surface_vertices) {
        average_x += vertex.x;
        average_y += vertex.y;
        average_z += vertex.z;
      }

      int surface_size = surface_vertices.size();
      average_x = average_x / surface_size;
      average_y = average_y / surface_size;
      average_z = average_z / surface_size;
      Vertex v0 = surface_vertices.get(0);
      Vertex v1 = surface_vertices.get(1);
      Vertex v2 = surface_vertices.get(2);

      float[] vector_a = {v1.x - v0.x, v1.y - v0.y, v1.z - v0.z};
      float[] vector_b = {v2.x - v1.x, v2.y - v1.y, v2.z - v1.z};
      float[] vector_n = {
        vector_a[1] * vector_b[2] - vector_a[2] * vector_b[1],
        vector_a[2] * vector_b[0] - vector_a[0] * vector_b[2],
        vector_a[0] * vector_b[1] - vector_a[1] * vector_b[0]};

      float normalize = (float) Math.sqrt(Math.pow(vector_n[0], 2)
          + Math.pow(vector_n[1], 2) + Math.pow(vector_n[2], 2));
      vector_n[0] = vector_n[0] / normalize;
      vector_n[1] = vector_n[1] / normalize;
      vector_n[2] = vector_n[2] / normalize;

      float vector_l[] = new float[3];
      vector_l[0] = average_x - light_vertex.x;
      vector_l[1] = average_y - light_vertex.y;
      vector_l[2] = average_z - light_vertex.z;
      normalize = (float) Math.sqrt(Math.pow(vector_l[0], 2)
          + Math.pow(vector_l[1], 2) + Math.pow(vector_l[2], 2));
      vector_l[0] = average_x - light_vertex.x / normalize;
      vector_l[1] = average_y - light_vertex.y / normalize;
      vector_l[2] = average_z - light_vertex.z / normalize;

      float intensity = (float) Math.cos(vector_n[0] * vector_l[0]
          + vector_n[1] * vector_l[1] + vector_n[2] * vector_l[2]);
      int color_r = colours.get(index).getRed();
      int color_g = colours.get(index).getGreen();
      int color_b = colours.get(index).getBlue();
      int new_r, new_g, new_b;

      if (color_r * intensity < 0) {
        new_r = 0;
      } else if (color_r * intensity > color_r) {
        new_r = color_r;
      } else {
        new_r = (int) (color_r * intensity);
      }
      if (color_g * intensity < 0) {
        new_g = 0;
      } else if (color_g * intensity > color_g) {
        new_g = color_g;
      } else {
        new_g = (int) (color_g * intensity);
      }
      if (color_b * intensity < 0) {
        new_b = 0;
      } else if (color_b * intensity > color_b) {
        new_b = color_b;
      } else {
        new_b = (int) (color_b * intensity);
      }
      colour = new Color(new_r, new_g, new_b);
      shades.set(index, colour);
      index++;

      isLit = true;
    }
  }

  /**
   * Set object colour.
   *
   * @param colour new colour of object
   */
  void setColour(Color colour) {
    this.colour = colour;
  }

  /**
   * Get object colour.
   *
   * @param index index of surface
   * @return object colour if the object is not lit, shade colour if the object
   * is lit
   */
  Color getColour(int index) {
    if (!isLit) {
      return colours.get(index);
    } else {
      return shades.get(index);
    }
  }

  /**
   * Render object with set colour.
   */
  void renderColour() {
    isHidden = true;
    for (int i = 0; i < surfaces.size(); i++) {
      colours.set(i, colour);
    }
  }

  /**
   * Reset object data.
   */
  void clear() {
    x = 0;
    y = 0;
    z = 0;
    isLit = false;
    isHidden = false;
    light_vertex = new Vertex(0, 0, 0);
    colour = new Color((Color.white).getRGB());
    vertices_count = 0;
    vertices.clear();
    surfaces.clear();
    colours.clear();
    shades.clear();
  }
}
