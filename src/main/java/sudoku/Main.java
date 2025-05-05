package sudoku;

import java.util.Scanner;

import sudoku.common.Utils;
import sudoku.model.Sudoku;
import static sudoku.model.SudokuConstant.N;
import sudoku.model.SudokuManager;
import sudoku.solver.BasicBacktracking;
import sudoku.solver.DancingLinks;
import sudoku.solver.ForwardChecking;
import sudoku.solver.MRVBacktracking;
import sudoku.solver.Solver;

/**
 * The entry point for the Sudoku solver application.
 * This class provides an interface to run multiple Sudoku solving algorithms
 * and measure their performance in terms of execution time and memory usage.
 */
public class Main {
    /**
     * Timeout limit in minutes for each solver.
     */
    private static final int TIMEOUT_MINUTES = 2;

    /**
     * The main method to start the application.
     * Reads a Sudoku puzzle from input, then attempts to solve it using
     * several algorithms, printing out performance metrics and solutions.
     *
     * @param args Command line arguments (not used).
     * @throws Exception if input or processing fails.
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = SudokuManager.getSudokuMatrix(scanner);

        if (input == null) {
            System.out.println("Provided Sudoku is invalid. Exiting the program.");
            return;
        }

        Solver[] solvers = {
                new DancingLinks(),
                new BasicBacktracking(),
                new ForwardChecking(),
                new MRVBacktracking(),
        };

        for (Solver solver : solvers) {
            solveAndMeasure(solver, input);
            System.out.println("----------");
        }
    }

    /**
     * Executes a Sudoku solver on the given input and measures its performance.
     *
     * @param solver The solver instance to use.
     * @param input  The input string representing the Sudoku puzzle.
     */
    private static void solveAndMeasure(Solver solver, String input) {
        try {
            Sudoku sudoku = new Sudoku(N);
            sudoku.read(input);
            solver.setSudoku(sudoku);

            // Prepare memory measurement
            Utils.getMemoryUsed(true);
            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            // Run solver with timeout
            final boolean[] solved = { false };
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(TIMEOUT_MINUTES * 60 * 1000);

            // Measure time and memory
            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            // Output results
            System.out.println("==> " + solver.getName());

            if (solverThread.isAlive()) {
                solverThread.interrupt();
                System.out.println("Timeout after " + TIMEOUT_MINUTES + " minutes!");
            } else if (!solved[0]) {
                System.out.println("No solution found");
            } else {
                System.out.println("Solved successfully!");
                System.out.printf("Time: %.2f ms\n", timeUsed / 1_000_000.0);
                System.out.printf("Memory: %.2f KB\n", memoryUsed / 1024.0);
                if (sudoku.isSolved()) {
                    sudoku.print();
                } else {
                    System.out.println("Solution is invalid!");
                }

            }
        } catch (Exception e) {
            System.out.println("Error running solver: " + e.getMessage());
        }
    }
}
