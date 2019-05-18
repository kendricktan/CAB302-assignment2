package org.qut;


import static org.junit.jupiter.api.Assertions.*;

class MyCanvasTest {


    private static final MyCanvas CANVAS = new MyCanvas();

    @org.junit.jupiter.api.Test
    void resetCommands() {
        MyCanvas testCanvas = new MyCanvas();

        System.out.println("test case reset command");
        testCanvas.addCommand("RECTANGLE");
        testCanvas.resetCommands();
        StringBuilder sb = new StringBuilder();
        assertTrue(testCanvas.getCommands().size() == 0);
    }

    @org.junit.jupiter.api.Test
    void addCommand() {
        MyCanvas testCanvas = new MyCanvas();

        System.out.println("test case add command");
        testCanvas.addCommand("RECTANGLE");
        StringBuilder sb = new StringBuilder();
        for (String str : testCanvas.getCommands()) {sb.append(str);}
        assertEquals ("RECTANGLE", sb.toString());
    }

    @org.junit.jupiter.api.Test
    void undoLastCommand() {
        MyCanvas testCanvas = new MyCanvas();

        System.out.println("test case undo last command");
        testCanvas.addCommand("RECTANGLE");
        testCanvas.addCommand("POLYGON");
        testCanvas.undoLastCommand();
        StringBuilder sb = new StringBuilder();
        for (String str : testCanvas.getCommands()) {sb.append(str);}
        assertEquals ("RECTANGLE", sb.toString());
    }

    @org.junit.jupiter.api.Test
    void getCommands() {
        MyCanvas testCanvas = new MyCanvas();

        System.out.println("test case get command");
        testCanvas.addCommand("RECTANGLE");
        testCanvas.addCommand("POLYGON");
        assertEquals ("[RECTANGLE, POLYGON]", testCanvas.getCommands().toString());
    }

}