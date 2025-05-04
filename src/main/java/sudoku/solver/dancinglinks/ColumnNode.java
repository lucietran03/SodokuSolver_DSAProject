package sudoku.solver.dancinglinks;

/**
 * The ColumnNode class is a subclass of Node that represents a column in the Dancing Links matrix.
 * It extends the Node class and stores additional information, including the size of the column
 * and an instance of ColumnId that holds metadata about the column.
 */
public class ColumnNode extends Node {
    /**
     * The size of the column, representing the number of rows in the exact cover matrix.
     * This value is updated as rows are covered or uncovered.
     */
    int size = 0;

    /**
     * The metadata associated with this column, such as the constraint, number, and position.
     * It is used to identify the specific attributes of the column.
     */
    ColumnId info;
}
