package gsvm_project;

/**
 * @author jn
 */
public class MatrixOperations {

  static void initMatrix(float[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        matrix[i][j] = 0;
      }
    }
  }

  static void initMatrix(float[][] matrix, float x, float y, float z,
      float k) {
    initMatrix(matrix);
    matrix[0][0] = x;
    matrix[1][1] = y;
    matrix[2][2] = z;
    matrix[3][3] = k;
  }

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

  static void setMatrixValue(float[][] matrix, int row, int column,
      float value) {
    matrix[row][column] = value;
  }
}
