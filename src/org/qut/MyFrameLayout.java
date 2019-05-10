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

        btnPoint = new JButton("Plot");
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

        // Add tool buttons
        p.add(btnEllipse);
        p.add(btnLine);
        p.add(btnPolygon);
        p.add(btnRectangle);
        p.add(btnPoint);

        // Add padding + separator
        p.add(Box.createHorizontalStrut(3));
        p.add(newJSeperator());

        // Select PEN color
        JButton outlineColorBtn = new JButton("      ");
        outlineColorBtn.setSize(20, 20);
        outlineColorBtn.setBorder(BorderFactory.createEmptyBorder());
        outlineColorBtn.setBackground(canvas.penColor);
        outlineColorBtn.addActionListener((e) -> {
            Color c = JColorChooser.showDialog(frame, "Select PEN color", canvas.penColor);
            if (c != null) {
                canvas.penColor = c;
                outlineColorBtn.setBackground(canvas.penColor);
            }
        });
        p.add(new JLabel("PEN Color: "));
        p.add(outlineColorBtn);

        // Add padding + separator
        p.add(newJSeperator());
        p.add(Box.createHorizontalStrut(3));

        // Select FILL Color
        // Select outline color
        JButton fillColorBtn = new JButton("      ");
        fillColorBtn.setSize(20, 20);
        fillColorBtn.setBorder(BorderFactory.createEmptyBorder());
        fillColorBtn.setBackground(canvas.fillColor);
        fillColorBtn.addActionListener((e) -> {
            Color c = JColorChooser.showDialog(frame, "Select FILL color", canvas.fillColor);
            if (c != null) {
                canvas.fillColor = c;
                fillColorBtn.setBackground(canvas.fillColor);
            }
        });

        p.add(new JLabel("FILL Color: "));
        p.add(fillColorBtn);
        p.add(isFilledCheckbox);

        frame.add(p, BorderLayout.NORTH);
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
}
