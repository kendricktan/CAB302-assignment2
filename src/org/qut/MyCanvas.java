package org.qut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MyCanvas extends JPanel implements MouseListener {
    // Draw Commands is a list of strings containing
    // What's been drawn (in order)
    public ArrayList drawCommands = new ArrayList();

    // Current command is the command that is being executed
    // And changes based on mouse cursorMovement
    public String currentCommand = "";

    // Constructor
    public MyCanvas() {
        super();
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // TODO:
        // Read from arraylist and parse the commands

        // Draw line
        g.setColor(Color.black);
        g.drawRect(0, 0, 50, 50);

        g.setColor(Color.red);
        g.drawOval(100, 100, 50, 50);
    }

    // Mouse Event Listener Methods
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed; # of clicks: "
                + e.getClickCount());
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released; # of clicks: "
                + e.getClickCount());
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")");
    }
}
