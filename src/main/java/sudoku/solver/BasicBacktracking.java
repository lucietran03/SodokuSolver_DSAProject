package sudoku.solver;

/**
 * The BasicBacktracking class implements the backtracking algorithm to solve Sudoku puzzles.
 * It extends the Solver class and overrides the solve method to perform the backtracking search.
 * The algorithm fills the Sudoku grid by trying all possible values for empty cells,
 * and recursively explores all valid configurations.
 */
public class BasicBacktracking extends Solver {

    /**
     * Constructs a new BasicBacktracking solver with the name "Backtracking".
     */
    public BasicBacktracking() {
        super("Backtracking");
    }

    /**
     * Solves the Sudoku puzzle using the backtracking algorithm.
     * It iterates over all cells in the grid, and for each empty cell, it tries numbers from 1 to 9.
     * If a number is valid, it places the number and proceeds recursively to solve the next cell.
     * If a number of leads to an invalid configuration, it backtracks by resetting the cell and trying the next number.
     *
     * @return true if the Sudoku puzzle is solved, false if no solution exists.
     */
    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();

        // Traverse through each cell of the grid
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // If the current cell is empty
                if (grid[row][col] == 0) {
                    // Try numbers from 1 to 9
                    for (int num = 1; num <= 9; num++) {
                        // Check if placing the number is valid
                        if (isValid(row, col, num)) {
                            grid[row][col] = num;

                            // Recursively solve the rest of the grid
                            if (solve())
                                return true;

                            // If placing the number doesn't lead to a solution, backtrack
                            grid[row][col] = 0;
                        }
                    }
                    // If no number is valid, return false to backtrack further
                    return false;
                }
            }
        }
        // If all cells are filled, the puzzle is solved
        return true;
    }
}
