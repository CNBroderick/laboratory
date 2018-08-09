package org.bklab.vaadin.csdn;

import java.util.ArrayList;
import java.util.Random;

public class Rectangle {
    int height;
    int width;

    public Rectangle(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public static void main(String[] args) {

        ArrayList<Rectangle> rectangles = new ArrayList<>();

        Random r = new Random();

        for (int i = 0; i < r.nextInt(100); i++) {
            rectangles.add(new Rectangle(r.nextInt(), r.nextInt()));
        }

        System.out.println("ArrayList size = " + rectangles.size());

        System.out.println(rectangles.getClass().getName());
        Runtime rt = Runtime.getRuntime();
        System.out.println(rt.availableProcessors());

    }
}
