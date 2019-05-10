package org.qut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
    // Draw Commands is a list of strings containing
    // What's been drawn (in order)
    public ArrayList<String> drawCommands = new ArrayList();

    // Current shape to be drawn
    // State mutation is done on MyFrameLayout
    public MyShape.Shape curDrawShape = MyShape.Shape.ELLIPSE;

    // TODO: Encapsulate all the "current" variables into a class?
    // String to store polygon clicked coordinates
    public String curPolygonCoords = "";

    // TODO: Make these into Objects
    public Integer outlineRValue = 0;
    public Integer outlineGValue = 0;
    public Integer outlineBValue = 0;

    public Integer fillRValue = 0;
    public Integer fillGValue = 0;
    public Integer fillBValue = 0;

    // Do we wanna fill the shape?
    // State mutation is done in MyFrameLayout
    // Maybe write a setter/getter for this
    public boolean curFillShape = false;

    // TODO: Make these into objects
    public Double mouseMovedX = -1.0;
    public Double mouseMovedY = -1.0;

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
            else if (cmds[0].toUpperCase().equals("RECTANGLE")) {
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
            else if (cmds[0].toUpperCase().equals("ELLIPSE")) {
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

            // Line
            else if (cmds[0].toUpperCase().equals("LINE")) {
                int x1 = scaleX2WinWidth(cmds[1]);
                int y1 = scaleY2WinHeight(cmds[2]);
                int x2 = scaleX2WinWidth(cmds[3]);
                int y2 = scaleY2WinHeight(cmds[4]);

                g2.drawLine(x1, y1, x2, y2);
            }

            // Polygon
            else if (cmds[0].toUpperCase().equals("POLYGON")) {
                // Slice array
                String[] coords = Arrays.copyOfRange(cmds, 1, cmds.length);

                // Construct n_points
                int n_points = (coords.length / 2);
                int x_points[] = new int[n_points];
                int y_points[] = new int[n_points];

                for (int i = 0; i < coords.length; i += 2) {
                    int x = scaleX2WinWidth(coords[i]);
                    int y = scaleY2WinHeight(coords[i+1]);

                    x_points[i/2] = x;
                    y_points[i/2] = y;
                }

                Polygon poly = new Polygon(x_points, y_points, n_points);

                if (isFilling) {
                    Color curColor = g2.getColor();

                    g2.setColor(Color.decode(fillRgbToHex()));
                    g2.fillPolygon(poly);
                    g2.setColor(curColor);
                }

                g2.drawPolygon(poly);
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

            else if (curDrawShape == MyShape.Shape.ELLIPSE) {
                Ellipse2D ellipse = new Ellipse2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);

                if (curFillShape) {
                    Color curColor = g2.getColor();

                    g2.setColor(Color.decode(fillRgbToHex()));
                    g2.fill(ellipse);
                    g2.setColor(curColor);
                }

                g2.draw(ellipse);
            }

            else if (curDrawShape == MyShape.Shape.LINE) {
                g2.drawLine(x1, y1, x2, y2);
            }
        }

        // If the drawing is polygon, we don't depend on the mousePressedValues
        if (curDrawShape == MyShape.Shape.POLYGON && curPolygonCoords.length() > 0) {
            // Split by space
            String[] coords = curPolygonCoords.split("\\s+");

            int n_points = (coords.length / 2) + 1;
            int x_points[] = new int[n_points];
            int y_points[] = new int[n_points];


            for (int i = 0; i < coords.length; i += 2) {
                int x = scaleX2WinWidth(coords[i]);
                int y = scaleY2WinHeight(coords[i+1]);

                x_points[i/2] = x;
                y_points[i/2] = y;
            }

            x_points[n_points - 1] = scaleX2WinWidth(mouseMovedX);
            y_points[n_points - 1] = scaleY2WinHeight(mouseMovedY);


            Polygon poly = new Polygon(x_points, y_points, n_points);

            if (curFillShape) {
                Color curColor = g2.getColor();

                g2.setColor(Color.decode(fillRgbToHex()));
                g2.fillPolygon(poly);
                g2.setColor(curColor);
            }

            g2.drawPolygon(poly);
        }
    }

    /* Mouse Event Listener  */
    @Override
    public void mousePressed(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE || curDrawShape == MyShape.Shape.LINE) {
            mousePressedX = scaledX;
            mousePressedY = scaledY;
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE || curDrawShape == MyShape.Shape.LINE) {
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
            } else if (curDrawShape == MyShape.Shape.LINE) {
                drawCommands.add("LINE " + mousePressedX + " " + mousePressedY + " " + scaledX + " " + scaledY);
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

        if (curDrawShape == MyShape.Shape.RECTANGLE || curDrawShape == MyShape.Shape.ELLIPSE || curDrawShape == MyShape.Shape.LINE) {
            mouseDraggedX = scaledX;
            mouseDraggedY = scaledY;
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        mouseMovedX = scaledX;
        mouseMovedY = scaledY;

        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Double scaledX = normalizeX(e.getX());
        Double scaledY = normalizeY(e.getY());

        if (curDrawShape == MyShape.Shape.POINT) {
            drawCommands.add("FILL " + outlineRgbToHex());
            drawCommands.add("PLOT " + scaledX + " " + scaledY);
        }

        if (curDrawShape == MyShape.Shape.POLYGON) {
            if (e.getClickCount() == 1) {
                curPolygonCoords += scaledX + " " + scaledY + " ";
            }

            // Double click means polygon drawing ends
            else if (e.getClickCount() == 2) {
                // Add PEN Shape
                drawCommands.add("PEN " + outlineRgbToHex());

                // Add FILL command
                if (curFillShape) {
                    drawCommands.add("FILL " + fillRgbToHex());
                }

                drawCommands.add("POLYGON " + curPolygonCoords);

                // End FILL command
                if (curFillShape) {
                    drawCommands.add("FILL OFF");
                }

                // Reset polygon coordinates
                curPolygonCoords = "";
            }
        }

        repaint();
    }
}
