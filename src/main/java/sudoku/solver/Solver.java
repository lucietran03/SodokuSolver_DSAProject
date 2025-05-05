package sudoku.solver;

import sudoku.model.Sudoku;

/**
 * The abstract Solver class serves as the base class for all Sudoku solvers.
 * It defines methods for checking if a value can be placed in a specific
 * position
 * on the Sudoku grid and provides a structure for solving the puzzle.
 * <p>
 * Concrete subclasses should implement the {@link #solve()} method to define
 * specific solving algorithms.
 * </p>
 * <p>
 * Big O Complexity:
 * </p>
 * - **Worst-case time complexity of isValid**: O(n), where **n** is the size of
 * the grid (typically 9 for a 9x9 grid).
 * - **Worst-case time complexity of solve**: Depends on the specific algorithm
 * used in subclasses, e.g., O(9^(n^2)) for backtracking.
 */
public abstract class Solver {
    protected Sudoku sudoku;
    protected static final int SIZE = 9;
    protected String name;

    /**
     * Constructs a new Solver with the specified name.
     *
     * @param name The name of the solver algorithm.
     */
    public Solver(String name) {
        this.name = name;
    }

    /**
     * Checks if placing a given value at the specified position in the Sudoku grid
     * is valid according to Sudoku rules.
     * <p>
     * Checks the row, column, and 3x3 subgrid to ensure no conflicts.
     * </p>
     *
     * Big O Complexity: O(n), where **n** is the size of the grid (typically 9 for
     * a 9x9 grid).
     * - The method iterates through the row, column, and 3x3 subgrid, each
     * requiring up to 9 checks.
     *
     * @param row   The row index where the value is to be placed (0-based index).
     * @param col   The column index where the value is to be placed (0-based
     *              index).
     * @param value The value to be placed in the grid.
     * @return {@code true} if the value can be placed without violating Sudoku
     *         rules, {@code false} otherwise.
     */
    public boolean isValid(int row, int col, int value) {
        int[][] grid = sudoku.getGrid();

        // Check if the value already exists in the row or column
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == value || grid[i][col] == value)
                return false;
        }

        // Check if the value already exists in the 3x3 subgrid
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == value)
                    return false;
            }
        }

        return true;
    }

    /**
     * Sets the Sudoku puzzle to be solved by this solver.
     *
     * Big O Complexity: O(1).
     * - This method simply assigns a reference to the Sudoku object, which is a
     * constant-time operation.
     *
     * @param sudoku The Sudoku puzzle to solve.
     */
    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    /**
     * Retrieves the current Sudoku puzzle being solved.
     *
     * Big O Complexity: O(1).
     * - This method simply returns a reference to the Sudoku object, which is a
     * constant-time operation.
     *
     * @return The {@link Sudoku} object representing the current puzzle.
     */
    public Sudoku getSudoku() {
        return sudoku;
    }

    /**
     * Returns the name of the solver algorithm.
     *
     * Big O Complexity: O(1).
     * - This method simply returns a string, which is a constant-time operation.
     *
     * @return The name of the solver algorithm.
     */
    public String getName() {
        return name;
    }

    /**
     * Solves the Sudoku puzzle.
     * <p>
     * Abstract method that must be implemented by subclasses to define the
     * specific solving algorithm (e.g., backtracking, dancing links, etc.).
     * </p>
     *
     * Big O Complexity: Depends on the specific algorithm implemented in
     * subclasses.
     * - For example, a backtracking solver could have a worst-case time complexity
     * of O(9^(n^2)),
     * where **n** is the size of the grid.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise.
     */
    public abstract boolean solve();
}
