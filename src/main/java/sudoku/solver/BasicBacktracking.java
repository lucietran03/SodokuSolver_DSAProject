package sudoku.solver;

/**
 * Implements the backtracking algorithm to solve Sudoku puzzles.
 * Extends the Solver class and overrides the solve method.
 */
public class BasicBacktracking extends Solver {

    /**
     * Constructs a BasicBacktracking solver with the name "Backtracking".
     * 
     * Time Complexity: O(1) - Constructor execution is constant time.
     */
    public BasicBacktracking() {
        super("Backtracking");
    }

    /**
     * Solves the Sudoku puzzle using backtracking.
     * Tries all possible numbers for empty cells and backtracks if needed.
     *
     * @return true if the puzzle is solved, false otherwise.
     * 
     *         Time Complexity (Worst Case): O(9^(N*N)) - For an N x N grid, each
     *         cell may try all 9 numbers.
     */
    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            grid[row][col] = num;

                            if (solve())
                                return true;

                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
