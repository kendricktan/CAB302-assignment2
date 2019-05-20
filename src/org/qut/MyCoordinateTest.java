package org.qut;

import static org.junit.jupiter.api.Assertions.*;

class MyCoordinateTest {

    private static final MyCoordinate MY_COORDINATE = new MyCoordinate(4.5, 4.5);

    @org.junit.jupiter.api.Test
    void setXY() {
        MyCoordinate testCoordinates = new MyCoordinate(0,0);

        System.out.println("test case set X and Y command");
        testCoordinates.setXY(5,5);
        assertEquals(5, testCoordinates.getX());
        assertEquals(5, testCoordinates.getY());
    }

    @org.junit.jupiter.api.Test
    void getX() {
        MyCoordinate testCoordinates = new MyCoordinate(10,10);

        System.out.println("test case get X command");
        Double testX = testCoordinates.getX();
        assertEquals(Double.valueOf(10), testX);
    }

    @org.junit.jupiter.api.Test
    void setX() {
        MyCoordinate testCoordinates = new MyCoordinate(10,10);

        System.out.println("test case set X command");
        testCoordinates.setX(5);
        assertEquals(5, testCoordinates.getX());
    }

    @org.junit.jupiter.api.Test
    void getY() {
        MyCoordinate testCoordinates = new MyCoordinate(10,10);

        System.out.println("test case get Y command");
        Double testY = testCoordinates.getY();
        assertEquals(Double.valueOf(10), testY);
    }

    @org.junit.jupiter.api.Test
    void setY() {
        MyCoordinate testCoordinates = new MyCoordinate(10,10);

        System.out.println("test case set Y command");
        testCoordinates.setY(5);
        assertEquals(5, testCoordinates.getY());
    }
}