package sudoku.solver;

/**
 * The MRVBacktracking class is a solver for the Sudoku puzzle that uses the
 * Backtracking algorithm
 * with the Minimum Remaining Values (MRV) heuristic to improve the efficiency
 * of solving the puzzle.
 * The MRV heuristic prioritizes selecting the cell with the least number of
 * valid options.
 *
 * <p>
 * Big O Complexity:
 * </p>
 * - **Worst-case time complexity**: O(9^(n^2)), where n is the size of the
 * Sudoku grid.
 * Although the MRV heuristic reduces the search space, in the worst case, the
 * algorithm may explore all possibilities.
 *
 * @see Solver
 */
public class MRVBacktracking extends Solver {

    /**
     * Constructs a new MRVBacktracking solver with the name "Backtracking with
     * MRV".
     */
    public MRVBacktracking() {
        super("Backtracking with MRV");
    }

    /**
     * Solves the Sudoku puzzle using the backtracking algorithm with the MRV
     * heuristic.
     * It recursively selects the cell with the least number of valid options and
     * places a number.
     * The algorithm backtracks when a valid number can't be placed.
     *
     * @return true if the puzzle is solved, false otherwise.
     *         <p>
     *         Big O Complexity: O(9^(n^2)) in the worst case.
     *         </p>
     */
    @Override
    public boolean solve() {
        return solveWithMRV(sudoku.getGrid());
    }

    /**
     * Solves the Sudoku puzzle using backtracking with the MRV heuristic.
     * This method selects the cell with the least valid options, tries placing
     * numbers,
     * and backtracks if necessary.
     *
     * @param grid The current state of the Sudoku grid.
     * @return true if the puzzle is solved, false otherwise.
     *         <p>
     *         Big O Complexity: O(9^(n^2)) in the worst case.
     *         </p>
     */
    private boolean solveWithMRV(int[][] grid) {
        int[] cell = selectCellWithMRV(grid);
        if (cell == null)
            return true; // Puzzle solved

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
     * This method iterates through all unassigned cells and counts the number of
     * valid numbers
     * that can be placed in each cell. It selects the cell with the least valid
     * options.
     *
     * @param grid The current state of the Sudoku grid.
     * @return The row and column of the cell with the least valid options, or null
     *         if no unassigned cells exist.
     *         <p>
     *         Big O Complexity: O(n^2 * n) = O(n^3) in the worst case, where n is
     *         the size of the grid.
     *         </p>
     */
    private int[] selectCellWithMRV(int[][] grid) {
        int minCount = SIZE + 1;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) { // Only consider unassigned cells
                    int count = 0;
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num))
                            count++; // Count valid numbers
                    }
                    if (count < minCount) {
                        minCount = count;
                        selected = new int[] { row, col }; // Select the cell with the least valid options
                    }
                }
            }
        }

        return selected; // Return the most constrained cell
    }
}
