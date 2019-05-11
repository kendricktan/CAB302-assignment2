package org.qut;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class MyFrameComponentFactory {

    /**
     * Creates a JButton to select the tools (e.g. polygon, line, plot, etc)
     *
     * @param title The title of the button
     * @param fn    Callback function that does state changes
     * @return The constructed JButton tool
     */
    public static JButton createToolButton(String title, Function<Void, Void> fn) {
        JButton btn = new JButton(title);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener((e) -> {
            fn.apply(null);
        });
        return btn;
    }

    /**
     * Creates a JButton to select colors
     *
     * @param fn Callback function that does state changes
     * @return
     */
    public static JButton createColorPickerJButton(String dialogTitle, Color bgColor, Function<Color, Void> fn) {
        JButton btn = new JButton("      ");
        btn.setFocusable(false);
        btn.setSize(20, 20);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setBackground(bgColor);
        btn.addActionListener((e) -> {
            Color c = JColorChooser.showDialog(null, dialogTitle, bgColor);
            if (c != null) {
                fn.apply(c);
            }
        });

        return btn;
    }

    /**
     * Creates a JSeperator
     *
     * @return jseperator
     */
    public static JSeparator createJSeperator() {
        JSeparator s = new JSeparator(SwingConstants.VERTICAL);
        s.setFocusable(false);
        s.setPreferredSize(new Dimension(2, 25));

        return s;
    }
}
