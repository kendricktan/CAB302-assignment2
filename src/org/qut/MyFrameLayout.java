package org.qut;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static javax.swing.JOptionPane.showMessageDialog;

public class MyFrameLayout {
    // Frame Details
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
        frame.setFocusable(true);
        frame.requestFocus();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    canvas.undoLastCommand();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public static void createMenuBar() {
        menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newJMI = new JMenuItem("New");
        newJMI.addActionListener((e) -> {
            canvas.resetCommands();
        });

        JMenuItem loadJMI = new JMenuItem("Load");
        loadJMI.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "VEC Files", "vec", "VEC");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(frame);

            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader in = new BufferedReader(fileReader);
                    String line = in.readLine();

                    // Reset canvas draw
                    canvas.resetCommands();

                    while(line != null){
                        canvas.addCommand(line);
                        line = in.readLine();
                    }
                } catch (Exception ex) {
                    showMessageDialog(null, "Error reading file: " + ex.toString());
                }

            }
        });

        JMenuItem saveJMI = new JMenuItem("Save");
        saveJMI.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {

                String filename = fileChooser.getSelectedFile().toString();
                if (!filename .endsWith(".vec")) {
                    filename += ".vec";
                }

                File file = new File(filename);

                // Save to file
                try {
                    FileWriter fileWriter = new FileWriter(file);

                    for (String cmd: canvas.getCommands()) {
                        fileWriter.write(cmd + "\n");
                    }

                    fileWriter.close();

                } catch (Exception ex) {
                    showMessageDialog(null, "Error saving file: " + ex.toString());
                }
            }
        });

        JMenuItem exportBMPJMI = new JMenuItem("Export as BMP");
        exportBMPJMI.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().toString();
                if (!filename .endsWith(".bmp")) {
                    filename += ".bmp";
                }

                File file = new File(filename);

                // Save to file
                try {
                    Graphics g = canvas.getGraphics();

                    BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = image.createGraphics();
                    canvas.paint(g2);

                    RenderedImage rendImage = image;
                    ImageIO.write(rendImage, "bmp", file);

                    g2.dispose();

                } catch (Exception ex) {
                    showMessageDialog(null, "Error saving file: " + ex.toString());
                }
            }
        });

        JMenuItem undoJMI = new JMenuItem("Undo (Ctrl+Z)");
        undoJMI.addActionListener((e) -> {
            canvas.undoLastCommand();
        });

        fileMenu.add(newJMI);
        fileMenu.add(loadJMI);
        fileMenu.add(saveJMI);
        fileMenu.add(exportBMPJMI);
        fileMenu.addSeparator();
        fileMenu.add(undoJMI);

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
        btnEllipse.setFocusable(false);
        btnEllipse.setBorder(BorderFactory.createEmptyBorder());
        btnEllipse.addActionListener(btnActionListener(btnEllipse, MyShape.Shape.ELLIPSE));

        btnPolygon = new JButton("Polygon");
        btnPolygon.setFocusable(false);
        btnPolygon.setBorder(BorderFactory.createEmptyBorder());
        btnPolygon.addActionListener(btnActionListener(btnPolygon, MyShape.Shape.POLYGON));

        btnRectangle = new JButton("Rectangle");
        btnRectangle.setFocusable(false);
        btnRectangle.setBorder(BorderFactory.createEmptyBorder());
        btnRectangle.addActionListener(btnActionListener(btnRectangle, MyShape.Shape.RECTANGLE));

        btnLine = new JButton("Line");
        btnLine.setFocusable(false);
        btnLine.setBorder(BorderFactory.createEmptyBorder());
        btnLine.addActionListener(btnActionListener(btnLine, MyShape.Shape.LINE));

        btnPoint = new JButton("Plot");
        btnPoint.setFocusable(false);
        btnPoint.setBorder(BorderFactory.createEmptyBorder());
        btnPoint.addActionListener(btnActionListener(btnPoint, MyShape.Shape.POINT));

        // Click button
        btnEllipse.doClick();

        JCheckBox isFilledCheckbox = new JCheckBox("Fill");
        isFilledCheckbox.setFocusable(false);
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
        outlineColorBtn.setFocusable(false);
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
        fillColorBtn.setFocusable(false);
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
