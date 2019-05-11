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

/**
 * Application UI code
 * Logic for interactions between the canvas and the
 * tools (button, menuitems) etc are also located here
 */
public class MyFrameLayout {
    // Frame Details
    private final int width = 800;
    private final int height = 650;

    private JFrame frame;
    private MyCanvas canvas;

    // Buttons
    private JButton btnEllipse, btnPolygon, btnRectangle, btnLine, btnPlot, penColorBtn, fillColorBtn;

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

    public void createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newJMI = new JMenuItem("New");
        newJMI.addActionListener((e) -> {
            canvas.resetCommands();
        });

        JMenuItem loadJMI = new JMenuItem("Load");
        loadJMI.addActionListener((e) -> {
            loadVecFile();
        });

        JMenuItem saveJMI = new JMenuItem("Save");
        saveJMI.addActionListener((e) -> {
            saveToVecFile();
        });

        JMenuItem exportBMPJMI = new JMenuItem("Export as BMP");
        exportBMPJMI.addActionListener((e) -> {
            exportCanvasAsBMP();
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


    public void createDrawFrame(int width, int height) {
        canvas = new MyCanvas();

        // See mycanvas.repaintComponent
        // to see how the line gets drawn
        canvas.setLayout(new GridLayout(0, 1));
        canvas.setBackground(Color.white);
        canvas.setFocusable(false);

        frame.add(canvas, BorderLayout.CENTER);
    }

    // Create Heads Up Display
    public void createHUD() {
        Panel p = new Panel();

        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create tool buttons
        btnEllipse = MyFrameComponentFactory.createToolButton("Ellipse", (n) -> {
            deselectToolButtons();
            selectToolButton(btnEllipse);
            canvas.curDrawShape = MyShape.Shape.ELLIPSE;
            return null;
        });

        btnPolygon = MyFrameComponentFactory.createToolButton("Polygon", (n) -> {
            deselectToolButtons();
            selectToolButton(btnPolygon);
            canvas.curDrawShape = MyShape.Shape.POLYGON;
            return null;
        });

        btnRectangle = MyFrameComponentFactory.createToolButton("Rectangle", (n) -> {
            deselectToolButtons();
            selectToolButton(btnRectangle);
            canvas.curDrawShape = MyShape.Shape.RECTANGLE;
            return null;
        });

        btnLine = MyFrameComponentFactory.createToolButton("Line", (n) -> {
            deselectToolButtons();
            selectToolButton(btnLine);
            canvas.curDrawShape = MyShape.Shape.LINE;
            return null;
        });

        btnPlot = MyFrameComponentFactory.createToolButton("Plot", (n) -> {
            deselectToolButtons();
            selectToolButton(btnPlot);
            canvas.curDrawShape = MyShape.Shape.POINT;
            return null;
        });

        // Click button
        btnEllipse.doClick();

        JCheckBox isFilledCheckbox = new JCheckBox("Fill");
        isFilledCheckbox.setFocusable(false);
        isFilledCheckbox.addItemListener((e) -> {
            canvas.curFillShape = e.getStateChange() == ItemEvent.SELECTED;
        });

        // Add tool buttons
        p.add(btnPlot);
        p.add(btnLine);
        p.add(btnEllipse);
        p.add(btnRectangle);
        p.add(btnPolygon);

        // Add padding + separator
        p.add(Box.createHorizontalStrut(3));
        p.add(MyFrameComponentFactory.createJSeperator());
        p.add(Box.createHorizontalStrut(3));

        // Select PEN color
        p.add(new JLabel("PEN Color: "));
        penColorBtn = MyFrameComponentFactory.createColorPickerJButton(
                "Select PEN Color",
                canvas.penColor,
                (c) -> {
                    canvas.penColor = c;
                    penColorBtn.setBackground(c);
                    return null;
                }
        );
        p.add(penColorBtn);

        // PADDING
        p.add(Box.createHorizontalStrut(3));
        p.add(MyFrameComponentFactory.createJSeperator());
        p.add(Box.createHorizontalStrut(3));

        // Select FILL Color
        p.add(new JLabel("FILL Color: "));
        fillColorBtn = MyFrameComponentFactory.createColorPickerJButton(
                "Select FILL Color",
                canvas.fillColor,
                (c) -> {
                    canvas.fillColor = c;
                    fillColorBtn.setBackground(c);
                    return null;
                }
        );
        p.add(fillColorBtn);
        p.add(isFilledCheckbox);

        // Add panel to frame
        frame.add(p, BorderLayout.NORTH);
    }

    // Exports canvas as BMP
    private void exportCanvasAsBMP() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().toString();
            if (!filename.endsWith(".bmp")) {
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
    }

    // Saves canvas vec commands to file
    private void saveToVecFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {

            String filename = fileChooser.getSelectedFile().toString();
            if (!filename.endsWith(".vec")) {
                filename += ".vec";
            }

            File file = new File(filename);

            // Save to file
            try {
                FileWriter fileWriter = new FileWriter(file);

                for (String cmd : canvas.getCommands()) {
                    fileWriter.write(cmd + "\n");
                }

                fileWriter.close();

            } catch (Exception ex) {
                showMessageDialog(null, "Error saving file: " + ex.toString());
            }
        }
    }

    // Loads vector stuff into canvas
    private void loadVecFile() {
        JFileChooser chooser = new JFileChooser();
        // Filter our VEC files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "VEC Files", "vec", "VEC");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader in = new BufferedReader(fileReader);
                String line = in.readLine();

                // Reset canvas draw
                canvas.resetCommands();

                while (line != null) {
                    canvas.addCommand(line);
                    line = in.readLine();
                }
            } catch (Exception ex) {
                showMessageDialog(null, "Error reading file: " + ex.toString());
            }

        }
    }

    // Remove border drawing on all buttons
    private void deselectToolButtons() {
        btnEllipse.setBorder(BorderFactory.createEmptyBorder());
        btnPlot.setBorder(BorderFactory.createEmptyBorder());
        btnRectangle.setBorder(BorderFactory.createEmptyBorder());
        btnPolygon.setBorder(BorderFactory.createEmptyBorder());
        btnLine.setBorder(BorderFactory.createEmptyBorder());
    }

    // Adds border drawing to specified button
    private void selectToolButton(JButton btn) {
        btn.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }
}
