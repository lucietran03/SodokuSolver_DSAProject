package sudoku.model;

/**
 * The {@code SudokuConstant} class contains constants used in the Sudoku solver application.
 * It defines the size of the Sudoku grid, the total number of cells, and predefined Sudoku puzzles
 * of varying difficulty levels.
 *
 * <p>Constants:
 * <ul>
 *   <li>{@code SIZE}: Represents the size of a sub-grid in the Sudoku puzzle (e.g., 3x3).</li>
 *   <li>{@code N}: Represents the total number of cells in a row or column (e.g., 9 for a 9x9 grid).</li>
 *   <li>{@code EASY}, {@code MEDIUM}, {@code HARD}, {@code EVIL}: Strings representing Sudoku puzzles
 *       of different difficulty levels.</li>
 * </ul>
 *
 * <p>Complexity:
 * <ul>
 *   <li>Accessing constants is an O(1) operation as they are stored in memory.</li>
 * </ul>
 */
public class SudokuConstant {
    public static final int SIZE = 3;
    public static final int N = 9;

    public static final String EASY = "020507000040208701000001480150000200960702100080006940206070514518004360007100009";
    public static final String MEDIUM = "040308100210065000600000070903046781104829506805000020400000600000602047080030000";
    public static final String HARD = "500003000031007402480090030000004290604082000250000046379000684000000003000730009";
    public static final String EVIL = "000000000090060800010400030950300008000500200040900600205000000080070100001040002";
}