package gsvm_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author jn
 */
class App extends JFrame {

  Object object;
  Canvas canvas;
  JLabel scale;
  JLabel rotate;
  JLabel translate;
  JButton load;
  JButton rotate_x_plus;
  JButton rotate_y_plus;
  JButton rotate_z_plus;
  JButton rotate_x_minus;
  JButton rotate_y_minus;
  JButton rotate_z_minus;
  JButton translate_x_plus;
  JButton translate_y_plus;
  JButton translate_z_plus;
  JButton translate_x_minus;
  JButton translate_y_minus;
  JButton translate_z_minus;
  JButton scale_up;
  JButton scale_down;
  JFileChooser fc;

  App() {
    super();

    object = new Object();
    setSize(800, 425);
    setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas = new Canvas(object);
    add(canvas);

    load = new JButton("Load");
    load.setSize(80, 20);
    load.setLocation(610, 0);
    load.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        File obj_file = null;
        fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("*.obj files", "obj"));
        object.clear();

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          obj_file = fc.getSelectedFile();
        }

        try {
          Scanner read = new Scanner(obj_file).useLocale(Locale.ENGLISH);
          while (read.hasNextLine() && read.hasNext()) {
            switch (read.next()) {
              case "v":
                float x = read.nextFloat();
                float y = read.nextFloat();
                float z = read.nextFloat();
                object.addVertex(new Vertex(x, y, z));
                break;
              case "f":
                ArrayList<Integer> faces = new ArrayList<>();
                while (read.hasNextInt()) {
                  faces.add(read.nextInt());
                }
                object.addSurface(faces);
                break;
              default:
                read.nextLine();
            }
          }
        } catch (FileNotFoundException ex) {
          System.out.println("Object file not found!");
        }
        repaint();
      }
    });
    add(load);

    rotate = new JLabel("Rotate");
    rotate.setSize(80, 20);
    rotate.setLocation(610, 30);
    add(rotate);

    rotate_x_plus = new JButton("+x");
    rotate_x_plus.setSize(55, 20);
    rotate_x_plus.setLocation(610, 50);
    rotate_x_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateX(Math.PI / 36);
        repaint();
      }
    });
    add(rotate_x_plus);

    rotate_y_plus = new JButton("+y");
    rotate_y_plus.setSize(55, 20);
    rotate_y_plus.setLocation(670, 50);
    rotate_y_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateY(Math.PI / 36);
        repaint();
      }
    });
    add(rotate_y_plus);

    rotate_z_plus = new JButton("+z");
    rotate_z_plus.setSize(55, 20);
    rotate_z_plus.setLocation(730, 50);
    rotate_z_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateZ(Math.PI / 36);
        repaint();
      }
    });
    add(rotate_z_plus);

    rotate_x_minus = new JButton("-x");
    rotate_x_minus.setSize(55, 20);
    rotate_x_minus.setLocation(610, 80);
    rotate_x_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateX(-Math.PI / 36);
        repaint();
      }
    });
    add(rotate_x_minus);

    rotate_y_minus = new JButton("-y");
    rotate_y_minus.setSize(55, 20);
    rotate_y_minus.setLocation(670, 80);
    rotate_y_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateY(-Math.PI / 36);
        repaint();
      }
    });
    add(rotate_y_minus);

    rotate_z_minus = new JButton("-z");
    rotate_z_minus.setSize(55, 20);
    rotate_z_minus.setLocation(730, 80);
    rotate_z_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.rotateZ(-Math.PI / 36);
        repaint();
      }
    });
    add(rotate_z_minus);

    translate = new JLabel("Translate");
    translate.setSize(80, 20);
    translate.setLocation(610, 110);
    add(translate);

    translate_x_plus = new JButton("+x");
    translate_x_plus.setSize(55, 20);
    translate_x_plus.setLocation(610, 130);
    translate_x_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateX((float) 0.1);
        repaint();
      }
    });
    add(translate_x_plus);

    translate_y_plus = new JButton("+y");
    translate_y_plus.setSize(55, 20);
    translate_y_plus.setLocation(670, 130);
    translate_y_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateY((float) 0.1);
        repaint();
      }
    });
    add(translate_y_plus);

    translate_z_plus = new JButton("+z");
    translate_z_plus.setSize(55, 20);
    translate_z_plus.setLocation(730, 130);
    translate_z_plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateZ((float) 0.1);
        repaint();
      }
    });
    add(translate_z_plus);

    translate_x_minus = new JButton("-x");
    translate_x_minus.setSize(55, 20);
    translate_x_minus.setLocation(610, 160);
    translate_x_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateX((float) -0.1);
        repaint();
      }
    });
    add(translate_x_minus);

    translate_y_minus = new JButton("-y");
    translate_y_minus.setSize(55, 20);
    translate_y_minus.setLocation(670, 160);
    translate_y_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateY((float) -0.1);
        repaint();
      }
    });
    add(translate_y_minus);

    translate_z_minus = new JButton("-z");
    translate_z_minus.setSize(55, 20);
    translate_z_minus.setLocation(730, 160);
    translate_z_minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.translateZ((float) -0.1);
        repaint();
      }
    });
    add(translate_z_minus);

    scale = new JLabel("Scale");
    scale.setSize(80, 20);
    scale.setLocation(610, 190);
    add(scale);

    scale_up = new JButton("+");
    scale_up.setSize(82, 20);
    scale_up.setLocation(610, 210);
    scale_up.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.scaleUp();
        repaint();
      }
    });
    add(scale_up);

    scale_down = new JButton("-");
    scale_down.setSize(82, 20);
    scale_down.setLocation(704, 210);
    scale_down.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.scaleDown();
        repaint();
      }
    });
    add(scale_down);
  }
}

class MatrixOperations {

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

public class GSVM_Project {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        App frame = new App();
        frame.setVisible(true);
      }
    });
  }
}
