package org.qut;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FrameLayout {
    private final int width = 650;
    private final int height = 650;

    private static JFrame frame;
    private static MyCanvas canvas;
    private static JMenuBar menubar;


    public FrameLayout() {
        frame = new JFrame("CAB302 Assignment 2");

        createDrawFrame(width, height);
        createMenuBar();
        createHUD();

        frame.pack();
        frame.setVisible(true);
        frame.setSize(width, height);
    }

    public static void createMenuBar() {
        menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Load"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JMenuItem("Save as..."));
        fileMenu.add(new JMenuItem("Export As PNG"));

        menubar.add(fileMenu);

        frame.setJMenuBar(menubar);
    }

    public static void createDrawFrame(int width, int height) {
        canvas = new MyCanvas();

        // See mycanvas.repaintComponent
        // to see how the line gets drawn
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas, BorderLayout.CENTER);
    }

    // Create Heads Up Display
    // Basically all the buttons e.g.
    // New, Save, Load, Circle, Square
    public static void createHUD() {
        Panel p = new Panel();

        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        Button btnCircle = new Button("Circle");
        Button btnSquare = new Button("Square");
        Button btnTriangle = new Button("Triangle");
        Button btnRectangle = new Button("Rectangle");
        Button btnPoint = new Button("Point");

        p.add(btnCircle);
        p.add(btnSquare);
        p.add(btnTriangle);
        p.add(btnRectangle);
        p.add(btnPoint);

        frame.add(p, BorderLayout.NORTH); //f.add(p);
    }
}
