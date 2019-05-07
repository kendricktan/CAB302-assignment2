package org.qut;

import javax.swing.*;
import java.awt.*;

public class MyCanvas extends JPanel {
    public MyCanvas() {
        super();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw line
        g.setColor(Color.black);
        g.drawRect(0, 0, 50, 50);

        g.setColor(Color.red);
        g.drawOval(100, 100, 50, 50);
    }
}
