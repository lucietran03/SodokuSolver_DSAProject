package sudoku;

import sudoku.common.*;
import sudoku.model.*;
import sudoku.solver.*;

import java.util.Scanner;

import static sudoku.model.SudokuConstant.N;

/**
 * The Main class is the entry point for solving a Sudoku puzzle using various solver algorithms.
 * It prompts the user for a Sudoku puzzle input, initializes multiple solvers, and measures the performance of each solver
 * by tracking the time and memory usage during the solving process.
 * <p>
 * The program executes the following steps:
 * <ul>
 *     <li>Gets Sudoku puzzle input from the user.</li>
 *     <li>Initializes multiple Sudoku solvers.</li>
 *     <li>For each solver, it attempts to solve the puzzle while measuring execution time and memory usage.</li>
 *     <li>Displays the results including time and memory consumption, and whether the solver was successful or timed out.</li>
 * </ul>
 *
 * Big O Complexity:
 * - The complexity depends on the solving algorithm used. For example:
 *   - **Dancing Links**: Typically O(n^3) for a 9x9 grid (due to matrix manipulations in the algorithm).
 *   - **Basic Backtracking**: Worst case is O(9^(n^2)) where **n** is the grid size. This is due to the recursive nature of the backtracking algorithm.
 *   - **Forward Checking**: Similar to backtracking but optimized with forward checking; worst case is also O(9^(n^2)).
 *   - **MRV Backtracking**: Optimized backtracking with MRV (Minimum Remaining Values); worst case is also O(9^(n^2)) but could perform better on certain puzzles.
 * <p>
 * The worst-case time complexity for the entire program will be governed by the solver with the highest time complexity (typically backtracking or MRV).
 */
public class Main {

    /**
     * Main method to run the Sudoku solver program. It performs the following:
     * <ul>
     *     <li>Prompts the user for a Sudoku puzzle input.</li>
     *     <li>Initializes multiple solvers and solves the puzzle using each solver.</li>
     *     <li>Measures the time and memory usage for each solver.</li>
     *     <li>Displays the results of the solving process.</li>
     * </ul>
     *
     * @param args Command-line arguments (not used in this program).
     * @throws Exception if an error occurs during execution (e.g., invalid input or solver failure).
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = SudokuManager.getSudokuMatrix(scanner);

        // Check if the input Sudoku is valid
        if (input == null) {
            System.out.println("Provided Sudoku is invalid. Exiting the program.");
            return;
        }

        // List of solvers to try
        Solver[] solvers = {
                new DancingLinks(),
                new BasicBacktracking(),
                new ForwardChecking(),
                new MRVBacktracking(),
        };

        // Iterate over each solver
        for (Solver solver : solvers) {
            Sudoku sudoku = new Sudoku(N);
            sudoku.read(input);
            solver.setSudoku(sudoku);

            // Perform garbage collection before solving
            Runtime.getRuntime().gc();
            Thread.sleep(100);

            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            final boolean[] solved = { false };
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(2 * 60 * 1000); // Timeout after 2 minutes

            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            // Display results for the solver
            System.out.println("==> " + solver.getName());

            if (solverThread.isAlive()) {
                solverThread.interrupt();
                System.out.println("Solver failed: Timeout after 2 minutes");
                System.out.println("Time (ms): " + timeUsed / 1_000_000);
            } else {
                System.out.println("Time (ms): " + timeUsed / 1_000_000);
                System.out.println("Memory (KB): " + memoryUsed / 1024);

                if (solver.getName().equals("Dancing Links")) {
                    sudoku.print(); // Print the solved Sudoku grid for Dancing Links solver
                }
            }

            System.out.println("----------");
        }
    }
}
