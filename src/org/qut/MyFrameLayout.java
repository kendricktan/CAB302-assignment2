package org.qut;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.concurrent.Flow;

public class MyFrameLayout {
    private final int width = 650;
    private final int height = 650;

    private final int hudWidth = width;
    private final int hudHeight = 200;

    private static JFrame frame;
    private static MyCanvas canvas;
    private static JMenuBar menubar;

    // Constructor
    public MyFrameLayout() {
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
        canvas.setBackground(Color.white);
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas, BorderLayout.CENTER);
    }

    // Create Heads Up Display
    // Basically all the buttons e.g.
    // New, Save, Load, Circle, Square
    public static void createHUD() {
        Panel p = new Panel();

        JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
        separator1.setPreferredSize(new Dimension(2, 25));

        JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
        separator2.setPreferredSize(new Dimension(2, 25));

        // Align items to the left
        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        Button btnEllipse = new Button("Ellipse");
        Button btnSquare = new Button("Square");
        Button btnTriangle = new Button("Triangle");
        Button btnRectangle = new Button("Rectangle");
        Button btnPoint = new Button("Point");

        JTextField rTxtbox = new JTextField(3);
        JLabel rLabel = new JLabel("R: ");
        JTextField gTxtbox = new JTextField(3);
        JLabel gLabel = new JLabel("G: ");
        JTextField bTxtbox = new JTextField(3);
        JLabel bLabel = new JLabel("B: ");

        JCheckBox isFilledCheckbox = new JCheckBox("Fill");

        p.add(btnEllipse);
        p.add(btnSquare);
        p.add(btnTriangle);
        p.add(btnRectangle);
        p.add(btnPoint);

        p.add(Box.createHorizontalStrut(3));
        p.add(separator1);
        p.add(isFilledCheckbox);
        p.add(separator2);
        p.add(Box.createHorizontalStrut(3));

        p.add(rLabel);
        p.add(rTxtbox);
        p.add(gLabel);
        p.add(gTxtbox);
        p.add(bLabel);
        p.add(bTxtbox);

        frame.add(p, BorderLayout.NORTH); //f.add(p);
    }
}
