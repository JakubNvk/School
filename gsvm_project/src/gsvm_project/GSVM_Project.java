package gsvm_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author JakubNvk
 */
class App extends JFrame {

  Canvas canvas;
  Color colour;
  Object object;
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
  JButton choose_colour;
  JButton light_confirm;
  JButton hide;
  JButton unhide;
  JButton render;
  JLabel scale;
  JLabel rotate;
  JLabel translate;
  JLabel light;
  JFileChooser fc;
  JColorChooser cc;
  JTextField light_x;
  JTextField light_y;
  JTextField light_z;

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

    choose_colour = new JButton("Colour");
    choose_colour.setSize(80, 20);
    choose_colour.setLocation(704, 0);
    choose_colour.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        cc = new JColorChooser();
        colour = JColorChooser.showDialog(cc, "Colour", colour);
        object.setColour(colour);
      }
    });
    add(choose_colour);

    light = new JLabel("Light (x, y, z)");
    light.setSize(120, 20);
    light.setLocation(610, 240);
    add(light);

    light_x = new JTextField();
    light_x.setSize(55, 20);
    light_x.setLocation(610, 270);
    add(light_x);

    light_y = new JTextField();
    light_y.setSize(55, 20);
    light_y.setLocation(670, 270);
    add(light_y);

    light_z = new JTextField();
    light_z.setSize(55, 20);
    light_z.setLocation(730, 270);
    add(light_z);

    light_confirm = new JButton("OK");
    light_confirm.setSize(175, 20);
    light_confirm.setLocation(610, 290);
    light_confirm.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          float x = Float.parseFloat(light_x.getText());
          float y = Float.parseFloat(light_y.getText());
          float z = Float.parseFloat(light_z.getText());
          object.setLight(new Vertex(x, y, z));
          object.renderLight();
          repaint();
        } catch (NumberFormatException ex) {
          System.out.println("You entered non-float values.");
        }
      }
    });
    add(light_confirm);

    hide = new JButton("Hide");
    hide.setSize(70, 20);
    hide.setLocation(610, 320);
    hide.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.hideInvisibleFaces();
        repaint();
      }
    });
    add(hide);

    unhide = new JButton("Unhide");
    unhide.setSize(100, 20);
    unhide.setLocation(684, 320);
    unhide.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.unhideInvisibleFaces();
        repaint();
      }
    });
    add(unhide);

    render = new JButton("Render");
    render.setSize(175, 20);
    render.setLocation(610, 350);
    render.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        object.renderColour();
        repaint();
      }
    });
    add(render);
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
