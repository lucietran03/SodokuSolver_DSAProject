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
 * The Main class serves as the entry point for the Sudoku Solver application.
 * It reads a Sudoku puzzle from the user, validates it, and attempts to solve
 * it
 * using multiple solving algorithms. Each solver's performance is measured in
 * terms
 * of time and memory usage.
 */
public class Main {

    /**
     * The main method is the entry point of the application. It orchestrates the
     * process of reading a Sudoku puzzle, validating it, and solving it using
     * different algorithms. The performance of each solver is measured and
     * displayed.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws Exception If an error occurs during execution.
     *
     *                   Big-O Complexity (Worst Case):
     *                   - Reading and validating the Sudoku puzzle: O(N^2), where N
     *                   is the size of the Sudoku grid.
     *                   - Solving the Sudoku puzzle: Depends on the solver used.
     *                   For example:
     *                   - DancingLinks: O(2^(N^2)) in the worst case.
     *                   - BasicBacktracking: O(9^(N^2)) in the worst case.
     *                   - ForwardChecking: O(9^(N^2)) in the worst case.
     *                   - MRVBacktracking: O(9^(N^2)) in the worst case.
     *                   - Overall: Dominated by the solving algorithm's complexity.
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
            Sudoku sudoku = new Sudoku(N);
            sudoku.read(input);
            solver.setSudoku(sudoku);

            Runtime.getRuntime().gc();
            Thread.sleep(100);

            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            final boolean[] solved = { false };
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(2 * 60 * 1000);

            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            System.out.println("==> " + solver.getName());

            if (!solverThread.isAlive()) {
                if (solved[0]) {
                    System.out.println("Time (ms): " + timeUsed / 1_000_000);
                    System.out.println("Memory (KB): " + memoryUsed / 1024);
                    sudoku.print();
                } else {
                    System.out.println("Solver failed: Could not solve the Sudoku.");
                    System.out.println("Time (ms): " + timeUsed / 1_000_000);
                }
            } else {
                System.out.println("Solver failed: Timeout after 2 minutes.");
                System.out.println("Time (ms): " + timeUsed / 1_000_000);
                solverThread.interrupt(); 
            }

            System.out.println("----------");
        }
    }
}