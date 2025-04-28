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
     */
    public Sudoku(int size) {
        SIZE = size;
        N = size * size;

        grid = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    /**
     * Reads the Sudoku grid from the input stream.
     * Each cell value is read using the readInteger method.
     *
     * @param input The input stream to read from.
     * @throws Exception If an error occurs while reading from the input stream.
     */
    public void read(String input) throws Exception {
        if (input.length() != N) { // N là 81
            throw new IllegalArgumentException("Input length must be " + (N));
        }

        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = input.charAt(index);
                grid[i][j] = Character.getNumericValue(c);
                index++;
            }
        }
    }

    /**
     * Prints the Sudoku grid in a formatted manner.
     * Includes grid lines to separate sub-grids.
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

    public boolean isSolved() {
        // Check rows, columns, and 3x3 subgrids
        for (int i = 0; i < N; i++) {
            // Check row and column for duplicates
            if (!checkRowAndColumn(i)) {
                return false;
            }

            // Check the corresponding 3x3 subgrid for duplicates
            if (!checkSubgrid(i)) {
                return false;
            }
        }

        return true;  // No duplicates found, Sudoku is valid
    }

    // Helper method to check a row and its corresponding column for duplicates
    private boolean checkRowAndColumn(int index) {
        boolean[] checkRow = new boolean[N + 1];  // To track seen numbers in the row
        boolean[] checkCol = new boolean[N + 1];  // To track seen numbers in the column
        for (int i = 0; i < N; i++) {
            int rowValue = this.grid[index][i];
            int colValue = this.grid[i][index];

            // Check for duplicates in the row
            if (rowValue != 0 && checkRow[rowValue]) {
                return false;  // Duplicate in the row
            }
            checkRow[rowValue] = true;

            // Check for duplicates in the column
            if (colValue != 0 && checkCol[colValue]) {
                return false;  // Duplicate in the column
            }
            checkCol[colValue] = true;
        }
        return true;
    }

    // Helper method to check a 3x3 subgrid for duplicates
    private boolean checkSubgrid(int index) {
        boolean[] checkBox = new boolean[N + 1];  // To track seen numbers in the subgrid
        int boxRowStart = (index / 3) * 3;
        int boxColStart = (index % 3) * 3;

        for (int row = boxRowStart; row < boxRowStart + 3; row++) {
            for (int col = boxColStart; col < boxColStart + 3; col++) {
                int value = this.grid[row][col];
                if (value != 0 && checkBox[value]) {
                    return false;  // Duplicate found in the 3x3 subgrid
                }
                checkBox[value] = true;
            }
        }
        return true;
    }
}
