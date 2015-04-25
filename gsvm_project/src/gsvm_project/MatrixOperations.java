package gsvm_project;

/**
 * @author JakubNvk
 */
public class MatrixOperations {

  /**
   * Initialize matrix with 0 values.
   * 
   * @param matrix - matrix to be initialized
   */
  static void initMatrix(float[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        matrix[i][j] = 0;
      }
    }
  }

  /**
   * Initialize matrix with x, y, z, k values.
   * 
   * @param matrix - matrix to be initialized
   * @param x - value on [0][0] position in matrix
   * @param y - value on [1][1] position in matrix
   * @param z - value on [2][2] position in matrix
   * @param k - value on [3][3] position in matrix
   */
  static void initMatrix(float[][] matrix, float x, float y, float z,
      float k) {
    initMatrix(matrix);
    matrix[0][0] = x;
    matrix[1][1] = y;
    matrix[2][2] = z;
    matrix[3][3] = k;
  }

  /**
   * Multiply two matrices.
   * 
   * @param matrix_a - first matrix to be multiplied
   * @param matrix_b - second matrix to be multiplied
   * @return matrix which is result of multiplication of matrix_a with matrix_b
   */
  static float[][] multiplyMatrices(float[][] matrix_a,
      float[][] matrix_b) {

    float[][] matrix_c = new float[4][4];
    initMatrix(matrix_c);

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        float value = 0;
        for (int k = 0; k < 4; k++) {
          value += matrix_a[k][j] * matrix_b[i][k];
        }
        matrix_c[i][j] = value;
      }
    }
    return matrix_c;
  }

  /**
   * Set value to [row][column] in matrix.
   * 
   * @param matrix - matrix to be edited
   * @param row - row of the matrix
   * @param column - column of the matrix
   * @param value - value to be set
   */
  static void setMatrixValue(float[][] matrix, int row, int column,
      float value) {
    matrix[row][column] = value;
  }
}
