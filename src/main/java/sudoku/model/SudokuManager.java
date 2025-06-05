package sudoku.model;

import java.util.Scanner;

import sudoku.common.InputValidator;

/**
 * This method prompts the user to select a default Sudoku matrix difficulty
 * level
 * and returns the corresponding Sudoku matrix as a string.
 * <p>
 * The user is presented with four difficulty options: Easy, Medium, Hard, Evil
 * Based on the user's choice, the method retrieves the appropriate Sudoku
 * matrix
 * from the {@code SudokuConstant} class.
 * If the user provides an invalid choice (not between 1 and 4), the method
 * prints an error message and returns {@code null}.
 *
 * <p>
 * <b>Time Complexity:</b> O(1) in the worst case, as the method performs a
 * constant
 * number of operations regardless of the input.
 */
public class SudokuManager {

    // Method to get the Sudoku matrix from the user
    public static String getSudokuMatrix(Scanner scanner) {
        System.out.println("Choose a default Sudoku matrix or input your own:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.println("4. Evil");
        System.out.println("5. Input your own Sudoku matrix");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String sudokuMatrix = "";

        if (choice > 0 && choice < 6) {
            switch (choice) {
                case 1:
                    sudokuMatrix = SudokuConstant.EASY;
                    break;
                case 2:
                    sudokuMatrix = SudokuConstant.MEDIUM;
                    break;
                case 3:
                    sudokuMatrix = SudokuConstant.HARD;
                    break;
                case 4:
                    sudokuMatrix = SudokuConstant.EVIL;
                    break;
                case 5:
                    System.out.println("Please input your Sudoku matrix as a single string:");
                    sudokuMatrix = scanner.nextLine();
                    try {
                        InputValidator.validateInput(sudokuMatrix);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return null;
            }
        } else {
            System.out.println("Invalid choice.");
            return null;
        }

        return sudokuMatrix;
    }
}
