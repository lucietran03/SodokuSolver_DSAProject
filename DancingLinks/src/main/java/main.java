import Model.Sudoku;

public class Main {
    /**
     * The main method to run the Sudoku solver.
     * Reads the puzzle size and grid from the input, solves the puzzle, and prints
     * the solution.
     *
     * @param args Command-line arguments. If provided, the first argument is
     *             treated as the input file path.
     * @throws Exception If an error occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        int puzzleSize = 9;
        Sudoku s = new Sudoku(puzzleSize);

        // Hardcoded input (81 digits, 0 = empty)
        String input = "387205619419080502625319874063597241900804003204001905100960350796003428508402100";

        if (input.length() != puzzleSize * puzzleSize) {
            System.out.println("Error: Invalid input length.");
            return;
        }

        int[][] board = new int[puzzleSize][puzzleSize];

        for (int i = 0; i < input.length(); i++) {
            int row = i / puzzleSize;
            int col = i % puzzleSize;
            char ch = input.charAt(i);
            board[row][col] = ch - '0'; // convert char to int
        }

        // Giả sử class Sudoku có method setBoard
        s.setBoard(board);

        long startTime = System.currentTimeMillis();
        s.solve();
        long endTime = System.currentTimeMillis();

        System.out.println("Solved in " + (endTime - startTime) + "ms");

        s.print(); // In ra kết quả
    }
}
