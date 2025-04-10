/**
 * Singleton input Handler that handles all user's input
 */

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler {
    private static InputHandler instance;
    private Scanner scanner;

    // Private constructor to prevent direct instantiation
    private InputHandler() {
        scanner = new Scanner(System.in);
    }

    // Public method to provide access to the single instance
    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    public String getCommand() {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                return "Q"; // Default to quit if no line is available
            }
        } catch (NoSuchElementException e) {
            System.out.println("No input available, exiting the game.");
            return "Q";
        }
    }


    public int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear the invalid input
            }
        }
        scanner.nextLine();
        return input;
    }

    // Method to get string input
    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.next(); // Read a single word (without whitespace)
    }

    public String getLineInput(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // Consume any lingering newline character
        return scanner.nextLine();
    }

    public double getDoubleInput(String prompt) {
        double input = 0.0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextDouble();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
        scanner.nextLine();
        return input;
    }
}