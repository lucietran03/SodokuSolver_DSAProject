package sudoku.model;

/**
 * The Sudoku class represents a Sudoku puzzle and provides methods to read,
 * solve, and print the puzzle.
 * It uses the Algorithm X method to solve the puzzle.
 */
public class Sudoku {

    /**
     * The size of the smaller sub-grid (e.g., 3 for a standard 9x9 Sudoku).
     */
    int SIZE;

    /**
     * The size of the entire grid (SIZE * SIZE).
     */
    int N;

    /**
     * The 2D array representing the Sudoku grid.
     */
    int[][] grid;

    /**
     * Constructs a Sudoku object with the specified sub-grid size.
     * Initializes the grid with all zeros.
     *
     * @param size The size of the smaller sub-grid.
     * Time Complexity: O(N^2) in the worst case, where N is the size of the entire grid.
     */
    public Sudoku(int size) {
        SIZE = size;
        N = SudokuConstant.N;  // Use the constant N value

        grid = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = 0;
    }

    /**
     * Returns the Sudoku grid.
     *
     * @return The 2D array representing the Sudoku grid.
     * Time Complexity: O(1).
     */
    public int[][] getGrid() {
        return grid;
    }

    /**
     * Reads the Sudoku grid from the input string.
     * Each cell value is read from the string and populated into the grid.
     *
     * @param input The input string representing the Sudoku grid.
     * @throws Exception If the input length is invalid.
     * Time Complexity: O(N^2) in the worst case, where N is the size of the entire grid.
     */
    public void read(String input) throws Exception {
        if (input.length() != N * N) { // N is 9, so N*N is 81
            throw new IllegalArgumentException("Input length must be " + (N * N));
        }

        int index = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                char c = input.charAt(index);
                grid[i][j] = Character.getNumericValue(c);
                index++;
            }
        }
    }

    /**
     * Prints the Sudoku grid in a formatted manner.
     * Includes grid lines to separate sub-grids.
     * Time Complexity: O(N^2) in the worst case, where N is the size of the entire grid.
     */
    public void print() {
        for (int r = 0; r < SIZE; r++) {
            if (r % 3 == 0) {
                System.out.println("+-------+-------+-------+");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c % 3 == 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[r][c] + " ");
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }

    /**
     * Checks if the Sudoku puzzle is solved.
     * Validates rows, columns, and 3x3 subgrids for duplicates.
     *
     * @return True if the Sudoku is solved, false otherwise.
     * Time Complexity: O(N^2) in the worst case, where N is the size of the entire grid.
     */
    public boolean isSolved() {
        for (int i = 0; i < N; i++) {
            if (!checkRowAndColumn(i)) {
                return false;
            }

            if (!checkSubgrid(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Helper method to check a row and its corresponding column for duplicates.
     *
     * @param index The index of the row and column to check.
     * @return True if no duplicates are found, false otherwise.
     * Time Complexity: O(N) in the worst case, where N is the size of the entire grid.
     */
    private boolean checkRowAndColumn(int index) {
        boolean[] checkRow = new boolean[N + 1];
        boolean[] checkCol = new boolean[N + 1];
        for (int i = 0; i < N; i++) {
            int rowValue = this.grid[index][i];
            int colValue = this.grid[i][index];

            if (rowValue != 0 && checkRow[rowValue]) {
                return false;
            }
            checkRow[rowValue] = true;

            if (colValue != 0 && checkCol[colValue]) {
                return false;
            }
            checkCol[colValue] = true;
        }
        return true;
    }

    /**
     * Helper method to check a 3x3 subgrid for duplicates.
     *
     * @param index The index of the subgrid to check.
     * @return True if no duplicates are found, false otherwise.
     * Time Complexity: O(N) in the worst case, where N is the size of the entire grid.
     */
    private boolean checkSubgrid(int index) {
        boolean[] checkBox = new boolean[N + 1];
        int boxRowStart = (index / 3) * 3;
        int boxColStart = (index % 3) * 3;

        for (int row = boxRowStart; row < boxRowStart + 3; row++) {
            for (int col = boxColStart; col < boxColStart + 3; col++) {
                int value = this.grid[row][col];
                if (value != 0 && checkBox[value]) {
                    return false;
                }
                checkBox[value] = true;
            }
        }
        return true;
    }
}
