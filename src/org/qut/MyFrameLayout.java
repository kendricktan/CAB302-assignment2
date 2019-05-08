package org.qut;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;

public class MyFrameLayout {
    private final int width = 650;
    private final int height = 670;

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
        canvas.setLayout(new GridLayout(0, 1));
        canvas.setBackground(Color.white);
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

        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        Button btnEllipse = new Button("Ellipse");
        Button btnSquare = new Button("Square");
        Button btnTriangle = new Button("Triangle");
        Button btnRectangle = new Button("Rectangle");
        Button btnPoint = new Button("Point");

        JLabel rLabel = new JLabel("R: ");
        JTextField rTxtbox = new JTextField(3);
        rTxtbox.setText("255");
        rTxtbox.getDocument().addDocumentListener(ensureU8TxtListener(rTxtbox));

        JLabel gLabel = new JLabel("G: ");
        JTextField gTxtbox = new JTextField(3);
        gTxtbox.setText("255");
        gTxtbox.getDocument().addDocumentListener(ensureU8TxtListener(gTxtbox));

        JLabel bLabel = new JLabel("B: ");
        JTextField bTxtbox = new JTextField(3);
        bTxtbox.setText("255");
        bTxtbox.getDocument().addDocumentListener(ensureU8TxtListener(bTxtbox));

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

    // Generates a DocumentListener to ensures the textbox value is a u8 Integer
    private static DocumentListener ensureU8TxtListener (JTextField jtxt) {
        return new DocumentListener() {
            Runnable doCheckU8 = new Runnable() {
                @Override
                public void run() {
                    // Extract Digits only
                    String s = jtxt.getText().replaceAll("\\D", "");

                    // Mutation :(
                    // But such is the Java way of doing things
                    Integer i;

                    // Check within range
                    try {
                        i = Integer.parseInt(s);
                        if (i > 255) {
                            i = 255;
                        }
                        else if (i < 0) {
                            i = 0;
                        }
                    } catch (Exception ex) {
                        i = 0;
                    }

                    // Only update if text is not the same
                    if (!i.toString().equals(jtxt.getText())) {
                        jtxt.setText(i.toString());
                    }
                }
            };

            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(doCheckU8);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(doCheckU8);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(doCheckU8);
            }
        };
    }
}
