import Model.Sudoku;

public class SudokuSolver {
    /**
     * The main method to run the Sudoku solver.
     * Reads the puzzle size and grid from the input, solves the puzzle, and prints
     * the solution.
     *
     * @param args Command-line arguments. If provided, the first argument is
     *             treated as the input file path.
     * @throws Exception If an error occurs during execution.
     */
    public static void main(String args[]) throws Exception {
        // Khai báo input cứng
        String input = "387205619419080502625319874063597241900804003204001905100960350796003428508402100";
        int puzzleSize = 9; // hoặc tự tính sqrt(input.length()) nếu muốn linh động
    
        if (puzzleSize > 100 || puzzleSize < 1) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }
    
        Sudoku s = new Sudoku(puzzleSize);
    
        s.read(input);
    
        long startTime = System.currentTimeMillis();
        s.solve();
        long endTime = System.currentTimeMillis();
    
        System.out.println(endTime - startTime);
        // s.print();
    }    
}
