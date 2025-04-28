package sudoku.model;

import java.util.Scanner;

public class SudokuManager {

    // Method to get the Sudoku matrix from the user
    public static String getSudokuMatrix(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1. Enter a custom Sudoku matrix");
        System.out.println("2. Choose a default Sudoku matrix");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String sudokuMatrix = "";

        if (choice == 1) {
            // Enter a custom Sudoku matrix
            System.out.println("Enter the Sudoku matrix (81 characters): ");
            sudokuMatrix = scanner.nextLine();

            if (sudokuMatrix.length() != 81) {
                System.out.println("Invalid matrix length. The matrix must have 81 characters.");
                return null;
            }

        } else if (choice == 2) {
            // Choose a default Sudoku matrix
            System.out.println("Choose difficulty level:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.println("4. Evil");

            int difficulty = scanner.nextInt();
            switch (difficulty) {
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
