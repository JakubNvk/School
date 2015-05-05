package gsvm_project;

/**
 * @author jn
 */
public class Vertex {

  float x;
  float y;
  float z;
  float k;

  Vertex(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.k = 1;
  }

  /**
   * Multiply vertex with given matrix. 
  * 
   * @param matrix multiplication matrix
   */
  void multiplyVertex(float[][] matrix) {
    float new_x = 0, new_y = 0, new_z = 0;

    new_x += x * matrix[0][0];
    new_x += y * matrix[0][1];
    new_x += z * matrix[0][2];
    new_x += k * matrix[0][3];

    new_y += x * matrix[1][0];
    new_y += y * matrix[1][1];
    new_y += z * matrix[1][2];
    new_y += k * matrix[1][3];

    new_z += x * matrix[2][0];
    new_z += y * matrix[2][1];
    new_z += z * matrix[2][2];
    new_z += k * matrix[2][3];

    x = new_x;
    y = new_y;
    z = new_z;
  }
}
