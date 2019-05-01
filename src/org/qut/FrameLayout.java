package org.qut;


import javax.swing.*;
import java.awt.*;

public class FrameLayout {
    private final int width = 650;
    private final int height = 650;

    private static JFrame frame;
    private static Canvas canvas;

    public FrameLayout() {
        frame = new JFrame("CAB302 Assignment 2");

        createDrawFrame(width, height);
        createHUD();

        frame.pack();
        frame.setVisible(true);
        frame.setSize(width, height);
    }

    public static void createDrawFrame(int width, int height) {
        canvas = new Canvas();

        canvas.setBackground(Color.blue);
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas, BorderLayout.CENTER);
    }

    // Create Heads Up Display
    public static void createHUD() {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());

        Button btnNew = new Button("New");
        Button btnLoad = new Button("Load");

        p.add(btnNew);
        p.add(btnLoad);

        frame.add(p, BorderLayout.NORTH); //f.add(p);
    }
}
