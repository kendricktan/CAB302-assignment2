package org.qut;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Function;

public class MyFrameLayout {
    private final int width = 800;
    private final int height = 650;

    private static JFrame frame;
    private static MyCanvas canvas;
    private static JMenuBar menubar;

    // Buttons
    private static JButton btnEllipse, btnPolygon, btnRectangle, btnLine, btnPoint;

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

        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnEllipse = new JButton("Ellipse");
        btnEllipse.setBorder(BorderFactory.createEmptyBorder());
        btnEllipse.addActionListener(btnActionListener(btnEllipse, MyShape.Shape.ELLIPSE));

        btnPolygon = new JButton("Polygon");
        btnPolygon.setBorder(BorderFactory.createEmptyBorder());
        btnPolygon.addActionListener(btnActionListener(btnPolygon, MyShape.Shape.POLYGON));

        btnRectangle = new JButton("Rectangle");
        btnRectangle.setBorder(BorderFactory.createEmptyBorder());
        btnRectangle.addActionListener(btnActionListener(btnRectangle, MyShape.Shape.RECTANGLE));

        btnLine = new JButton("Line");
        btnLine.setBorder(BorderFactory.createEmptyBorder());
        btnLine.addActionListener(btnActionListener(btnLine, MyShape.Shape.LINE));

        btnPoint = new JButton("Point");
        btnPoint.setBorder(BorderFactory.createEmptyBorder());
        btnPoint.addActionListener(btnActionListener(btnPoint, MyShape.Shape.POINT));

        // Click button
        btnEllipse.doClick();

        JCheckBox isFilledCheckbox = new JCheckBox("Fill");
        isFilledCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                canvas.curFillShape = e.getStateChange() == ItemEvent.SELECTED;
            }
        });

        // Add buttons
        p.add(btnEllipse);
        p.add(btnLine);
        p.add(btnPolygon);
        p.add(btnRectangle);
        p.add(btnPoint);

        // Add padding + separator
        p.add(Box.createHorizontalStrut(3));
        p.add(newJSeperator());

        // Add outline color
        p.add(new JLabel("(Outline) R: "));
        p.add(newU8TextField("0", (x) -> { canvas.outlineRValue = x; return null; }));
        p.add(new JLabel("G: "));
        p.add(newU8TextField("0", (x) -> { canvas.outlineGValue = x; return null; }));
        p.add(new JLabel("B: "));
        p.add(newU8TextField("0", (x) -> { canvas.outlineBValue = x; return null; }));

        // Add padding + separator
        p.add(newJSeperator());
        p.add(Box.createHorizontalStrut(3));

        // Add fill + color selector
        p.add(isFilledCheckbox);
        p.add(new JLabel("R: "));
        p.add(newU8TextField("0", (x) -> { canvas.fillRValue = x; return null; }));
        p.add(new JLabel("G: "));
        p.add(newU8TextField("0", (x) -> { canvas.fillGValue = x; return null; }));
        p.add(new JLabel("B: "));
        p.add(newU8TextField("0", (x) -> { canvas.fillBValue = x; return null; }));

        frame.add(p, BorderLayout.NORTH); //f.add(p);
    }

    // Textbox "Factory"
    private static JTextField newU8TextField (String defaultVal, Function<Integer, Void> fn) {
        JTextField txtbox = new JTextField(3);
        txtbox.setText(defaultVal);
        txtbox.getDocument().addDocumentListener(ensureU8TxtListener(
                txtbox, fn)
        );

        return txtbox;
    }

    // Seperator "Factory"
    private static JSeparator newJSeperator() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(2, 25));

        return s;
    }


    // ActionListner "Factory"
    private static ActionListener btnActionListener (JButton btn, MyShape.Shape shape) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deselect buttons
                btnEllipse.setBorder(BorderFactory.createEmptyBorder());
                btnPoint.setBorder(BorderFactory.createEmptyBorder());
                btnRectangle.setBorder(BorderFactory.createEmptyBorder());
                btnPolygon.setBorder(BorderFactory.createEmptyBorder());
                btnLine.setBorder(BorderFactory.createEmptyBorder());

                // Select current button
                btn.setBorder(BorderFactory.createLineBorder(Color.black, 2));
                canvas.curDrawShape = shape;
            }
        };
    }

    // DocumentListener "Factory"
    private static DocumentListener ensureU8TxtListener (JTextField jtxt, Function<Integer, Void> fn) {
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

                    // Apply callback function
                    // (Mainly to update canvas's r, g, or b value
                    fn.apply(i);
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
