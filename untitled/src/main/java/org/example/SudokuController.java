package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuController {
    @FXML
    GridPane gridPane;
    @FXML
    Button solveButton;
    @FXML
    Button returnButton;
    @FXML
    Button resetButton;
    @FXML
    Label timer;
    private int[][] puzzle;
    long duration;

    public void loadPuzzle(String difficulty) {
        int[][][] problems = ProblemList.getAllProblems();
        switch (difficulty) {
            case "Easy":
                puzzle = problems[0];
                break;
            case "Medium":
                puzzle = problems[1];
                break;
            case "Hard":
                puzzle = problems[2];
                break;
            case "Very Hard":
                puzzle = problems[3];
                break;
            case "Super Hard":
                puzzle = problems[4];
                break;
        }
        displayPuzzle(puzzle);
    }

    public void loadPuzzleByIndex(int index) {
        int[][][] problems = ProblemList.getAllProblems();
        if (index >= 0 && index < problems.length) {
            puzzle = problems[index];
            displayPuzzle(puzzle);
        }
    }

    private void displayPuzzle(int[][] puzzle) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String cellId = String.format("#cell%d%d", row, col);
                TextField cell = (TextField) gridPane.lookup(cellId);
                if (cell != null) {
                    if (puzzle[row][col] != 0) {
                        cell.setText(String.valueOf(puzzle[row][col]));
                        if (this.puzzle[row][col] == 0) {
                            // Mark as solution number
                            cell.getStyleClass().add("solution");
                            cell.setEditable(false);
                        } else {
                            // Mark as hint number
                            cell.getStyleClass().remove("solution");
                            cell.setEditable(false);
                        }
                    } else {
                        cell.setText("");
                        cell.setEditable(true);
                        cell.getStyleClass().remove("solution");
                    }
                }
            }
        }
        timer.setText("Time: " + duration / 1000000000.0 + " seconds");
    }

    @FXML
    public void handleSolveButton() {
        // Create a new grid based on user input
        int[][] userGrid = new int[9][9];
        boolean validInput = true;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String cellId = String.format("#cell%d%d", row, col);
                TextField cell = (TextField) gridPane.lookup(cellId);

                if (cell != null) {
                    String text = cell.getText().trim();
                    if (text.isEmpty()) {
                        userGrid[row][col] = 0; // Empty cell
                    } else {
                        try {
                            int value = Integer.parseInt(text);
                            if (value < 1 || value > 9) {
                                validInput = false; // Invalid number
                            }
                            userGrid[row][col] = value;
                        } catch (NumberFormatException e) {
                            validInput = false; // Non-numeric input
                        }
                    }
                }
            }
        }

        if (!validInput) {
            System.out.println("Invalid input. Please enter numbers between 1 and 9.");
            timer.setText("ERROR: Invalid input");
            return;
        }

        // Solve the Sudoku puzzle
        SudokuSolver solver = new SudokuSolver(userGrid);
        long startTime = System.nanoTime();
        int[][] solution = solver.solve();
        long endTime = System.nanoTime();
        duration = endTime - startTime;

        if (solution != null) {
            displayPuzzle(solution); // Display the solved puzzle
        } else {
            System.out.println("No solution exists for the provided puzzle.");
        }
    }
    @FXML void returnButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void prepareForCustomInput() {
        puzzle = new int[9][9]; // Initialize an empty puzzle
        displayPuzzle(puzzle); // Display an empty grid for user input
    }
    @FXML
    public void handleResetButton() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String cellId = String.format("#cell%d%d", row, col);
                TextField cell = (TextField) gridPane.lookup(cellId);
                if (cell != null) {
                    cell.clear(); // Clear the text in the cell
                    cell.getStyleClass().remove("solution"); // Remove the solution style if applied
                    cell.setEditable(true); // Make the cell editable
                }
            }
        }
    }
}