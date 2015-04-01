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
import javax.swing.SwingUtilities;

/**
 * @author jn
 */
class App extends JFrame {

  Object object;
  Canvas canvas;
  JButton load;
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
        object.clear();

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          obj_file = fc.getSelectedFile();
        }

        try {
          Scanner read = new Scanner(obj_file).useLocale(Locale.ENGLISH);
          /*
           * Je smutne, ze modernemu operacnemu systemu ako je Windows
           * musime povedat, ze to co budeme citat je, in fact, v anglictine.
           * Jazyk systemu anglictina? Urcite idem nacitavat cinske znaky..
           * Thank Linus for Linux.
           */
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
  }
}

public class GSVM_Project {

  public static void initMatrix(float[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        matrix[i][j] = 0;
      }
    }
  }

  public static float[][] multiplyMatrices(float[][] matrix_a,
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
