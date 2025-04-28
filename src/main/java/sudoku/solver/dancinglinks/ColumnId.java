package sudoku.solver.dancinglinks;

/**
 * The ColumnId class represents the ID of a column in the Dancing Links matrix,
 * which holds three attributes: constraint, number, and position.
 * These are used for storing the specific attributes of a column in the matrix.
 */
public class ColumnId {
    /**
     * The constraint type associated with this column (e.g., row, column, block constraint).
     * Default value is -1 if not set.
     */
    int constraint = -1;

    /**
     * The number associated with this column, representing the digit placed in the Sudoku puzzle.
     * Default value is -1 if not set.
     */
    int number = -1;

    /**
     * The position of the cell in the Sudoku grid, used for mapping the solution back.
     * Default value is -1 if not set.
     */
    int position = -1;
}
