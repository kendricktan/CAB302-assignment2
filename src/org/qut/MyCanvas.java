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
    public Integer curRValue = 0;
    public Integer curGValue = 0;
    public Integer curBValue = 0;
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
    public String rgbToHex() {
        return String.format("#%02x%02x%02x", curRValue, curGValue, curBValue);
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

        // TODO:
        // Rectangle, Square, Triangle, etc
        for (String curCommand: drawCommands) {
            String[] cmds = curCommand.split("\\s+");

            // Change color
            if (cmds[0].toUpperCase().equals("FILL")) {
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

                g2.drawRect(x1, y1, x2 - x1, y2 - y1);
            }

            // Ellipse
            if (cmds[0].toUpperCase().equals("ELLIPSE")) {
                int x1 = scaleX2WinWidth(cmds[1]);
                int y1 = scaleY2WinHeight(cmds[2]);
                int x2 = scaleX2WinWidth(cmds[3]);
                int y2 = scaleY2WinHeight(cmds[4]);

                g2.draw(new Ellipse2D.Double(x1, y1, x2 - x1, y2 - y1));
            }

        }

        // If we're "previewing" the draw state
        if (mousePressedX >= 0.0 && mousePressedY >= 0.0 && mouseDraggedX >= 0.0 && mouseDraggedY >= 0.0) {
            g2.setColor(Color.decode(rgbToHex()));

            int x1 = scaleX2WinWidth(mousePressedX);
            int x2 = scaleX2WinWidth(mouseDraggedX);
            int y1 = scaleY2WinHeight(mousePressedY);
            int y2 = scaleY2WinHeight(mouseDraggedY);

            int xMin = Math.min(x1, x2);
            int yMin = Math.min(y1, y2);

            int xMax = Math.max(x1, x2);
            int yMax = Math.max(y1, y2);

            if (curDrawShape == MyShape.Shape.RECTANGLE) {
                g2.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);
            }

            if (curDrawShape == MyShape.Shape.ELLIPSE) {
                g2.draw(new Ellipse2D.Double(xMin, yMin, xMax - xMin, yMax - yMin));
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
            drawCommands.add("FILL " + rgbToHex());

            if (curFillShape) {
                // TODO: Add fill logic
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
            drawCommands.add("FILL " + rgbToHex());
            drawCommands.add("PLOT " + scaledX + " " + scaledY);
        }

        repaint();
    }
}
