package org.qut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener, ComponentListener {
    // Draw Commands is a list of strings containing
    // What's been drawn (in order)
    public ArrayList<String> drawCommands;

    public MyShape.Shape curDrawShape;
    public Integer curRValue;
    public Integer curGValue;
    public Integer curBValue;
    public boolean curFillShape;

    // Constructor
    public MyCanvas() {
        super();

        drawCommands = new ArrayList();

        curFillShape = false;
        curDrawShape = MyShape.Shape.ELLIPSE;
        curRValue = 0;
        curGValue = 0;
        curBValue = 0;

        addMouseMotionListener(this);
        addMouseListener(this);
        addComponentListener(this);
    }

    /* Helper functions */
    public String rgbToHex() {
        return String.format("#%02x%02x%02x", curRValue, curGValue, curBValue);
    }

    /* Getter Setters */
    public Integer getCurRValue() {
        return curRValue;
    }

    public void setCurRValue(Integer curRValue) {
        this.curRValue = curRValue;
    }

    public Integer getCurGValue() {
        return curGValue;
    }

    public void setCurGValue(Integer curGValue) {
        this.curGValue = curGValue;
    }

    public Integer getCurBValue() {
        return curBValue;
    }

    public void setCurBValue(Integer curBValue) {
        this.curBValue = curBValue;
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

            // Change colorv
            if (cmds[0].toUpperCase().equals("FILL")) {
                g2.setColor(Color.decode(cmds[1]));
            }

            // Point
            if (cmds[0].toUpperCase().equals("PLOT")) {
                int x = (int) (Double.valueOf(cmds[1]) * getWidth());
                int y = (int) (Double.valueOf(cmds[2]) * getHeight());
                g2.drawLine(x, y, x, y);
            }
        }

    }

    @Override
    public void componentResized(ComponentEvent e) {
        System.out.println("Resized, width: " + e.getComponent().getWidth() + ", height: " + e.getComponent().getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /* Mouse Event Listener  */
    @Override
    public void mousePressed(MouseEvent e) {
        Double scaledX = Double.valueOf(e.getX()) / this.getWidth();
        Double scaledY = Double.valueOf(e.getY()) / this.getHeight();
        if (curDrawShape == MyShape.Shape.POINT) {
            drawCommands.add("FILL " + rgbToHex());
            drawCommands.add("PLOT " + scaledX + " " + scaledY);
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
