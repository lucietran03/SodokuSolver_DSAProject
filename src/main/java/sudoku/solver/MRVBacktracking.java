package sudoku.solver;

/**
 * The MRVBacktracking class is a solver for the Sudoku puzzle that uses the Backtracking algorithm
 * with the Minimum Remaining Values (MRV) heuristic to solve the puzzle more efficiently.
 * <p>
 * The MRV heuristic helps by choosing the cell with the least number of valid options remaining,
 * which can guide the backtracking search towards the solution more efficiently by prioritizing
 * the most constrained cells first.
 * <p>
 * Big O Complexity:
 * - **Worst-case time complexity**: The worst-case time complexity is **O(9^(n^2))**, where **n** is the size of the Sudoku grid. This is because the backtracking algorithm may attempt to try all possible combinations of numbers for each cell in the worst case. The MRV heuristic helps reduce the search space by making smarter choices but does not change the fact that, in the worst case, all possibilities might need to be explored.
 *
 * @see Solver
 */
public class MRVBacktracking extends Solver {

    /**
     * Constructs a new MRVBacktracking solver with the name "Backtracking with MRV".
     */
    public MRVBacktracking() {
        super("Backtracking with MRV");
    }

    /**
     * Solves the Sudoku puzzle using backtracking with the MRV heuristic.
     * The MRV heuristic is used to select the cell with the least number of valid options,
     * potentially speeding up the solution process by prioritizing the most constrained cells.
     *
     * @return true if the puzzle is solved, false otherwise.
     */
    @Override
    public boolean solve() {
        return solveWithMRV(sudoku.getGrid());
    }

    /**
     * Solves the Sudoku puzzle using the backtracking algorithm with the MRV heuristic.
     * This method recursively selects cells with the least number of valid options,
     * places a number, and backtracks if necessary.
     *
     * @param grid The current state of the Sudoku grid.
     * @return true if the puzzle is solved, false otherwise.
     */
    private boolean solveWithMRV(int[][] grid) {
        int[] cell = selectCellWithMRV(grid);
        if (cell == null) return true; // Puzzle solved

        int row = cell[0], col = cell[1];
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;

                if (solveWithMRV(grid)) // Recursively solve with the new grid state
                    return true;

                grid[row][col] = 0; // Backtrack
            }
        }
        return false;
    }

    /**
     * Selects the cell with the minimum number of valid options (MRV heuristic).
     * This method iterates through all unassigned cells and counts the number of valid numbers
     * that can be placed in each cell. It selects the cell with the least number of valid options.
     *
     * @param grid The current state of the Sudoku grid.
     * @return The row and column of the cell with the least valid options, or null if no unassigned cells exist.
     */
    private int[] selectCellWithMRV(int[][] grid) {
        int minCount = SIZE + 1;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) { // Only consider unassigned cells
                    int count = 0;
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) count++; // Count valid numbers
                    }
                    if (count < minCount) {
                        minCount = count;
                        selected = new int[]{row, col}; // Select the cell with the least valid options
                    }
                }
            }
        }

        return selected; // Return the most constrained cell
    }
}
