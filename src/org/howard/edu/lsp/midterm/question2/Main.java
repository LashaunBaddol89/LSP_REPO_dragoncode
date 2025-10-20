package org.howard.edu.lsp.midterm.question2;

public class Main {
    public static void main(String[] args) {
        // Normal calculations
        System.out.println("Circle radius 3.0 → area = " + AreaCalculator.area(3.0));
        System.out.println("Rectangle 5.0 x 2.0 → area = " + AreaCalculator.area(5.0, 2.0));
        System.out.println("Triangle base 10, height 6 → area = " + AreaCalculator.area(10, 6));
        System.out.println("Square side 4 → area = " + AreaCalculator.area(4));

        // Exception demonstration
        try {
            System.out.println("Circle radius -2.0 → area = " + AreaCalculator.area(-2.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
     * Overloading vs Separate Methods Explanation:
     * 
     * Method overloading is preferred here because all methods share
     * the same general purpose — calculating an area — but differ by
     * parameter type and count. It keeps the API concise and intuitive.
     * Using separate names like circleArea() or rectangleArea() would
     * clutter the class and make it less consistent for future extensions.
     */
}
