import Model.Sudoku;
import Resources.WriteFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        String input = "387205619419080502625319874063597241900804003204001905100960350796003428508402100";
        int puzzleSize = 9;

        if (puzzleSize > 100 || puzzleSize < 1) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku s = new Sudoku(puzzleSize);
        s.read(input);

        // Measure memory usage before solving
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Measure time and solve
        long startTime = System.currentTimeMillis();
        boolean status = s.solve(); // Solve the puzzle and get the status
        long endTime = System.currentTimeMillis();

        // Measure memory usage after solving
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        // Calculate time taken
        long timeTaken = endTime - startTime;

        // Get recursion count
        int recursionCount = s.getRecursionCount();

        // Get current timestamp
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dtf);

        // Define level and algorithm
        String level = "easy"; // Example level
        String algorithm = "Dancing Links"; // Example algorithm

        // Write results to file, including the question string
        WriteFile.writeFile(level, status, timeTaken, memoryUsed, recursionCount, algorithm, timestamp);

        // Print results
        System.out.println("Status: " + (status ? "Solved" : "Unsolved"));
        System.out.println("Time Taken: " + timeTaken + " ms");
        System.out.println("Memory Used: " + memoryUsed + " bytes");
        System.out.println("Total Recursions: " + recursionCount);

        s.print();
    }
}