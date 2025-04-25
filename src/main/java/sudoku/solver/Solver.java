package sudoku.solver;

import sudoku.model.Sudoku;

public abstract class Solver {
    protected Sudoku sudoku;
    protected static final int SIZE = 9;
    protected String name;

    public Solver(String name) {
        this.name = name;
    }

    /**
     * Checks if placing a given value at the specified row and column in the Sudoku grid
     * is valid according to Sudoku rules.
     *
     * @param row   The row index where the value is to be placed (0-based index).
     * @param col   The column index where the value is to be placed (0-based index).
     * @param value The value to be placed in the grid.
     * @return {@code true} if the value can be placed at the specified position without
     *         violating Sudoku rules; {@code false} otherwise.
     */
    public boolean isValid(int row, int col, int value) {
        int[][] grid = sudoku.getGrid();

        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == value || grid[i][col] == value)
                return false;
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == value)
                    return false;
            }
        }

        return true;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public String getName() {
        return name;
    }

    public abstract boolean solve();
}
