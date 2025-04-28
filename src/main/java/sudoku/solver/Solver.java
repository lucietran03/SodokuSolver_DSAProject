package sudoku.solver;

import sudoku.model.Sudoku;

/**
 * The abstract Solver class is the base class for all Sudoku solvers.
 * It provides methods to check if a value can be placed at a specified position
 * in the Sudoku grid without violating Sudoku rules and defines a framework for solving the Sudoku puzzle.
 * <p>
 * Concrete subclasses should implement the {@link #solve()} method to provide specific solving algorithms.
 * <p>
 * Big O Complexity:
 * - **Worst-case time complexity of isValid**: O(n), where **n** is the size of the grid (typically 9 for a 9x9 grid).
 *   - The method checks the row, column, and 3x3 subgrid to verify if placing a given value is valid. This requires examining up to 9 elements in the row, column, and subgrid, resulting in a time complexity of O(n).
 * - **Worst-case time complexity of solve**: This depends on the specific algorithm implemented in subclasses. For example, a backtracking solver could have a worst-case time complexity of O(9^(n^2)), where **n** is the size of the grid.
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
     * Checks if placing a given value at the specified row and column in the Sudoku grid
     * is valid according to Sudoku rules.
     * <p>
     * The validation process checks the following:
     * <ul>
     *     <li>Whether the value already exists in the specified row.</li>
     *     <li>Whether the value already exists in the specified column.</li>
     *     <li>Whether the value already exists in the 3x3 subgrid that contains the specified cell.</li>
     * </ul>
     *
     * @param row   The row index where the value is to be placed (0-based index).
     * @param col   The column index where the value is to be placed (0-based index).
     * @param value The value to be placed in the grid.
     * @return {@code true} if the value can be placed at the specified position without
     *         violating Sudoku rules; {@code false} otherwise.
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
     * @param sudoku The Sudoku puzzle.
     */
    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    /**
     * Returns the name of the solver algorithm.
     *
     * @return The name of the solver.
     */
    public String getName() {
        return name;
    }

    /**
     * Solves the Sudoku puzzle.
     * <p>
     * This is an abstract method that must be implemented by subclasses to provide the
     * specific solving algorithm (e.g., backtracking, dancing links, etc.).
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise.
     */
    public abstract boolean solve();
}
