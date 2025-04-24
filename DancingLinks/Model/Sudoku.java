package Model;

import Algorithm.AlgorithmX;

import java.io.*;

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

    /**
     * Solves the Sudoku puzzle using the Algorithm X method.
     */
    public void solve() {
        AlgorithmX solver = new AlgorithmX();
        solver.run(grid);
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
