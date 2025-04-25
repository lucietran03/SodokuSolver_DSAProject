package sudoku.model;

import sudoku.common.Utils;

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

    private void printTimeAndMemory(long startTime, long startMemory, long endTime, long endMemory) {
        long timeElapsed = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.printf("Time: %.2f ms\n", Utils.nanoToMillis(timeElapsed));
        System.out.printf("Memory used: %.2f KB\n\n", Utils.bytesToKilobytes(memoryUsed));
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
}
