package org.qut;

import static org.junit.jupiter.api.Assertions.*;

class MyCanvasTest {


    private static final MyCanvas CANVAS = new MyCanvas();

    @org.junit.jupiter.api.Test
    void resetCommands() {
        System.out.println("test case reset commands");
        CANVAS.addCommand("RECTANGLE");
        CANVAS.resetCommands();
        StringBuilder sb = new StringBuilder();
        assertTrue(CANVAS.getDrawCommands().size() == 0);
    }

    @org.junit.jupiter.api.Test
    void addCommand() {
        System.out.println("test case add commands");
        CANVAS.addCommand("RECTANGLE");
        StringBuilder sb = new StringBuilder();
        for (String str : CANVAS.getDrawCommands()) {sb.append(str);}
        assertEquals ("RECTANGLE", sb.toString());
    }

    @org.junit.jupiter.api.Test
    void undoLastCommand() {
        System.out.println("test case undo last command");
        CANVAS.addCommand("RECTANGLE");
        CANVAS.addCommand("POLYGON");
        CANVAS.undoLastCommand();
        StringBuilder sb = new StringBuilder();
        for (String str : CANVAS.getDrawCommands()) {sb.append(str);}
        assertEquals ("RECTANGLE", sb.toString());
    }

    @org.junit.jupiter.api.Test
    void getCommands() {
        System.out.println("test case get commands");
        CANVAS.addCommand("RECTANGLE");
        CANVAS.addCommand("POLYGON");
        assertEquals ("[RECTANGLE, POLYGON]", CANVAS.getCommands().toString());
    }

    @org.junit.jupiter.api.Test
    void paintComponent() {


    }

    @org.junit.jupiter.api.Test
    void mousePressed() {
    }

    @org.junit.jupiter.api.Test
    void mouseReleased() {
    }

    @org.junit.jupiter.api.Test
    void mouseEntered() {
    }

    @org.junit.jupiter.api.Test
    void mouseDragged() {
    }

    @org.junit.jupiter.api.Test
    void mouseMoved() {
    }

    @org.junit.jupiter.api.Test
    void mouseExited() {
    }

    @org.junit.jupiter.api.Test
    void mouseClicked() {
    }
}