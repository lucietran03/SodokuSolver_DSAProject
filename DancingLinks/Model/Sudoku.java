package Model;

import Algorithm.AlgorithmX;

import java.io.*;

/**
 * The Sudoku class represents a Sudoku puzzle and provides methods to read,
 * solve, and print the puzzle.
 * It uses the Algorithm X method to solve the puzzle.
 */
public class Sudoku {
    int theRecurstionCount;
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
    public Sudoku(int gridSize) {
        N = gridSize; // Full grid size (e.g., 9 for 9x9)
        SIZE = (int) Math.sqrt(gridSize); // Sub-grid size (e.g., 3 for 3x3)
        if (SIZE * SIZE != gridSize) {
            throw new IllegalArgumentException("Grid size must be a perfect square (e.g., 9 for a 9x9 Sudoku).");
        }
        grid = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = 0;
    }

    /**
     * Solves the Sudoku puzzle using the Algorithm X method.
     */
    public boolean solve() {
        AlgorithmX solver = new AlgorithmX();
        solver.run(grid); // Attempt to solve the puzzle using Algorithm X
        this.theRecurstionCount = solver.getRecursionCount();
        return isSolved(); // Verify if the grid is solved correctly
    }

    /**
     * Reads an integer from the input stream. If the input is "x", it is treated as
     * 0.
     *
     * @param in The input stream to read from.
     * @return The integer value read from the input stream.
     * @throws Exception If an error occurs while reading from the input stream.
     */
    static int readInteger(InputStream in) throws Exception {
        int result = 0;
        boolean success = false;

        while (!success) {
            String word = readWord(in);

            try {
                result = Integer.parseInt(word);
                success = true;
            } catch (Exception e) {
                if (word.compareTo("x") == 0) {
                    result = 0;
                    success = true;
                }
            }
        }

        return result;
    }

    /**
     * Reads a word (sequence of non-whitespace characters) from the input stream.
     *
     * @param in The input stream to read from.
     * @return The word read from the input stream.
     * @throws Exception If an error occurs while reading from the input stream.
     */
    static String readWord(InputStream in) throws Exception {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        while (whiteSpace.indexOf(currentChar) > -1) {
            currentChar = in.read();
        }

        while (whiteSpace.indexOf(currentChar) == -1) {
            result.append((char) currentChar);
            currentChar = in.read();
        }
        return result.toString();
    }

    /**
     * Reads the Sudoku grid from the input stream.
     * Each cell value is read using the readInteger method.
     *
     * @param input The input stream to read from.
     * @throws Exception If an error occurs while reading from the input stream.
     */
    public void read(String input) throws Exception {
        if (input.length() != N * N) {
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
     * Prints a string with a fixed width by padding it with spaces.
     *
     * @param text  The text to print.
     * @param width The fixed width for the text.
     */
    void printFixedWidth(String text, int width) {
        for (int i = 0; i < width - text.length(); i++)
            System.out.print(" ");
        System.out.print(text);
    }

    /**
     * Prints the Sudoku grid in a formatted manner.
     * Includes grid lines to separate sub-grids.
     */
    public void print() {
        for (int r = 0; r < N; r++) {
            if (r % SIZE == 0) {
                System.out.println("+-------+-------+-------+");
            }
            for (int c = 0; c < N; c++) {
                if (c % SIZE == 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[r][c] + " ");
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }

    /**
     * Checks if the Sudoku grid is completely solved.
     *
     * @return True if all cells are filled (non-zero), false otherwise.
     */
    public boolean isSolved() {
        // Check for any empty cells (zeros)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }

        // Check rows
        for (int i = 0; i < N; i++) {
            boolean[] seen = new boolean[N + 1];
            for (int j = 0; j < N; j++) {
                int num = grid[i][j];
                if (num < 1 || num > N || seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }

        // Check columns
        for (int j = 0; j < N; j++) {
            boolean[] seen = new boolean[N + 1];
            for (int i = 0; i < N; i++) {
                int num = grid[i][j];
                if (num < 1 || num > N || seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }

        // Check 3x3 blocks
        for (int blockRow = 0; blockRow < SIZE; blockRow++) {
            for (int blockCol = 0; blockCol < SIZE; blockCol++) {
                boolean[] seen = new boolean[N + 1];
                for (int i = blockRow * SIZE; i < blockRow * SIZE + SIZE; i++) {
                    for (int j = blockCol * SIZE; j < blockCol * SIZE + SIZE; j++) {
                        int num = grid[i][j];
                        if (num < 1 || num > N || seen[num]) {
                            return false;
                        }
                        seen[num] = true;
                    }
                }
            }
        }

        return true;
    }

    public int getRecursionCount() {
        return theRecurstionCount;
    }
}
