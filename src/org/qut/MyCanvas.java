package org.qut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
    // Draw Commands is a list of strings containing
    // What's been drawn (in order)
    public ArrayList<String> drawCommands = new ArrayList();

    public MyShape.Shape curDrawShape = MyShape.Shape.ELLIPSE;

    public Integer outlineRValue = 0;
    public Integer outlineGValue = 0;
    public Integer outlineBValue = 0;

    public Integer fillRValue = 0;
    public Integer fillGValue = 0;
    public Integer fillBValue = 0;

    public boolean curFillShape = false;

    public Double mousePressedX = -1.0;
    public Double mousePressedY = -1.0;

    public Double mouseDraggedX = -1.0;
    public Double mouseDraggedY = -1.0;

    // Constructor
    public MyCanvas() {
        super();

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /* Helper functions */
    public String outlineRgbToHex() {
        return String.format("#%02x%02x%02x", outlineRValue, outlineGValue, outlineBValue);
    }

    public String fillRgbToHex() {
        return String.format("#%02x%02x%02x", fillRValue, fillGValue, fillBValue);
    }

    /* Scales x value to window width value
        x must be between 0.0 and 1.0
    */
    public int scaleX2WinWidth (Double x) {
        return (int) (x * getWidth());
    }

    public int scaleX2WinWidth (String x) {
        return scaleX2WinWidth(Double.valueOf(x));
    }

    public int scaleY2WinHeight (Double y) {
        return (int) (y * getHeight());
    }

    public int scaleY2WinHeight (String y) {
        return scaleY2WinHeight(Double.valueOf(y));
    }

    public Double normalizeX (int x) {
        return Double.valueOf(x) / this.getWidth();
    }

    public Double normalizeY (int y) {
        return Double.valueOf(y) / this.getHeight();
    }

    /* Component Listener */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // G2 allows stroke width
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        // Do we wanna fill up stuff?
        boolean isFilling = false;
        String fillingColor = "#FFFFFF";

        // TODO: Polgyon
        for (String curCommand: drawCommands) {
            String[] cmds = curCommand.split("\\s+");

            // Filling
            if (cmds[0].toUpperCase().equals("FILL")) {
                if (cmds[1].toUpperCase().equals("OFF")) {
                    isFilling = false;
                }
                else {
                    isFilling = true;
                    fillingColor = cmds[1];
                }
            }

            // Change color
            if (cmds[0].toUpperCase().equals("PEN")) {
                g2.setColor(Color.decode(cmds[1]));
            }

            // Point
            if (cmds[0].toUpperCase().equals("PLOT")) {
                int x = scaleX2WinWidth(cmds[1]);
                int y = scaleY2WinHeight(cmds[2]);
                g2.drawLine(x, y, x, y);
            }

            // Rectangle
            if (cmds[0].toUpperCase().equals("RECTANGLE")) {
                int x1 = scaleX2WinWidth(cmds[1]);
                int y1 = scaleY2WinHeight(cmds[2]);
                int x2 = scaleX2WinWidth(cmds[3]);
                int y2 = scaleY2WinHeight(cmds[4]);

                int xMin = Math.min(x1, x2);
                int yMin = Math.min(y1, y2);

                int xMax = Math.max(x1, x2);
                int yMax = Math.max(y1, y2);

                // If fill
                if (isFilling) {
                    // Get current color to revert back to the outline color
                    Color prevColor = g2.getColor();
                    g2.setColor(Color.decode(fillingColor));

                    // Fill with filling color
                    g2.fillRect(xMin, yMin, xMax - xMin, yMax - yMin);

                    // Revert back to outline color
                    g2.setColor(prevColor);
                }

                g2.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);
            }

            // Ellipse
            if (cmds[0].toUpperCase().equals("ELLIPSE")) {
                int x1 = scaleX2WinWidth(cmds[1]);
                int y1 = scaleY2WinHeight(cmds[2]);
                int x2 = scaleX2WinWidth(cmds[3]);
                int y2 = scaleY2WinHeight(cmds[4]);

                int xMin = Math.min(x1, x2);
                int yMin = Math.min(y1, y2);

                int xMax = Math.max(x1, x2);
                int yMax = Math.max(y1, y2);

                Ellipse2D ellipse = new Ellipse2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);

                // If fill
                if (isFilling) {
                    // Get current color to revert back to the outline color
                    Color prevColor = g2.getColor();
                    g2.setColor(Color.decode(fillingColor));

                    // Fill with filling color
                    g2.fill(ellipse);

                    // Revert back to outline color
                    g2.setColor(prevColor);
                }

                g2.draw(ellipse);
            }

        }

        // If we're "previewing" the draw state
        // Read the latest values
        if (mousePressedX >= 0.0 && mousePressedY >= 0.0 && mouseDraggedX >= 0.0 && mouseDraggedY >= 0.0) {
            g2.setColor(Color.decode(outlineRgbToHex()));

            int x1 = scaleX2WinWidth(mousePressedX);
            int x2 = scaleX2WinWidth(mouseDraggedX);
            int y1 = scaleY2WinHeight(mousePressedY);
            int y2 = scaleY2WinHeight(mouseDraggedY);

            int xMin = Math.min(x1, x2);
            int yMin = Math.min(y1, y2);

            int xMax = Math.max(x1, x2);
            int yMax = Math.max(y1, y2);

            if (curDrawShape == MyShape.Shape.RECTANGLE) {
                if (curFillShape) {
                    Color curColor = g2.getColor();

                    g2.setColor(Color.decode(fillRgbToHex()));
                    g2.fillRect(xMin, yMin, xMax - xMin, yMax - yMin);
                    g2.setColor(curColor);
                }

                g2.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);
            }

            if (curDrawShape == MyShape.Shape.ELLIPSE) {
                Ellipse2D ellipse = new Ellipse2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);

                if (curFillShape) {
                    Color curColor = g2.getColor();

                    g2.setColor(Color.decode(fillRgbToHex()));
                    g2.fill(ellipse);
                    g2.setColor(curColor);
                }

                g2.draw(ellipse);
            }
        }
    }

    /* Mouse Event Listener  */
    @Override
    public void mousePressed(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE) {
            mousePressedX = scaledX;
            mousePressedY = scaledY;
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE) {
            // Add PEN Shape
            drawCommands.add("PEN " + outlineRgbToHex());

            // Add FILL command
            if (curFillShape) {
                drawCommands.add("FILL " + fillRgbToHex());
            }

            Double startX = Math.min(mousePressedX, scaledX);
            Double startY = Math.min(mousePressedY, scaledY);
            Double endX = Math.max(mousePressedX, scaledX);
            Double endY = Math.max(mousePressedY, scaledY);

            // Add RECTANGLE command
            if (curDrawShape == MyShape.Shape.RECTANGLE) {
                drawCommands.add("RECTANGLE " + startX + " " + startY + " " + endX + " " + endY);
            } else if (curDrawShape == MyShape.Shape.ELLIPSE) {
                drawCommands.add("ELLIPSE " + startX + " " + startY + " " + endX + " " + endY);
            }

            if (curFillShape) {
                drawCommands.add("FILL OFF");
            }

            // Reset mouse presses / drag
            mousePressedX = -1.0;
            mousePressedY = -1.0;

            mouseDraggedX = -1.0;
            mouseDraggedY = -1.0;
        }

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE) {
            mouseDraggedX = scaledX;
            mouseDraggedY = scaledY;
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Mouse pressed only happens
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.POINT) {
            drawCommands.add("FILL " + outlineRgbToHex());
            drawCommands.add("PLOT " + scaledX + " " + scaledY);
        }

        repaint();
    }
}
