package performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import sudoku.common.Utils;
import sudoku.model.Sudoku;
import sudoku.solver.BasicBacktracking;
import sudoku.solver.DancingLinks;
import sudoku.solver.ForwardChecking;
import sudoku.solver.MRVBacktracking;
import sudoku.solver.Solver;
import static sudoku.model.SudokuConstant.N;

/**
 * This class benchmarks the performance of different Sudoku solving algorithms
 * by running each solver on multiple Sudoku puzzles, recording average
 * solving time and memory usage.
 *
 * Big-O (Worst Case) Analysis of Solvers:
 * - BasicBacktracking: O(9^(N*N)) — brute-force, exponential in worst case.
 * - ForwardChecking: O(9^(N*N)), but with early pruning reduces actual run
 * time.
 * - MRVBacktracking: O(9^(N*N)), uses heuristics like MRV and degree
 * to lower branching.
 * - DancingLinks (DLX): O(N), theoretically fast with sparse matrix
 * backtracking, but still exponential worst case.
 * 
 */
public class TestInput {

    /**
     * Path to the input file that contains Sudoku puzzles in level:puzzle format
     */
    private static final String inputFilePath = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "sudokuIO", "input.txt").toString();

    /** Number of dummy warm-up runs to prepare JVM and reduce JIT effects */
    private static final int dummyRuns = 10;

    /** Array of solvers to be benchmarked */
    private static final Solver[] solvers = {
            new DancingLinks(),
            new BasicBacktracking(),
            new ForwardChecking(),
            new MRVBacktracking(),
    };

    /**
     * Performs warm-up runs with each solver to allow JVM optimizations (e.g., JIT
     * compilation).
     *
     * @param inputFilePath the path to the file with Sudoku puzzles
     * @param dummyRuns     number of warm-up runs per solver
     * @throws IOException if file cannot be read
     */
    public static void dummyRuns(String inputFilePath, int dummyRuns) throws IOException {
        for (Solver solver : solvers) {
            try (BufferedReader dummyReader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;
                int currentRun = 0;

                while ((line = dummyReader.readLine()) != null && currentRun < dummyRuns) {
                    String[] parts = line.split(":");
                    if (parts.length != 2)
                        continue;

                    String puzzle = parts[1].trim();
                    Sudoku sudoku = new Sudoku(9);

                    try {
                        sudoku.read(puzzle);
                        solver.setSudoku(sudoku);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    solver.solve(); // Warm-up execution
                    currentRun++;
                }
            }
        }
    }

    /**
     * Executes a real Sudoku solve with timing and memory measurements.
     *
     * @param solver the solver to be tested
     * @return an array of [solved:1|0, timeTaken in microseconds, memoryUsed in
     *         bytes]
     */
    public static long[] realRunning(Solver solver) {
        try {
            // Prepare measurement
            Utils.getMemoryUsed(true);
            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            // Run solver with timeout
            final boolean[] solved = { false };
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(2 * 60 * 1000);

            // Get results
            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            // Handle timeout or failure
            if (solverThread.isAlive()) {
                solverThread.interrupt();
                return new long[] { 0L, timeUsed / 1000, memoryUsed };
            }

            return new long[] {
                    solved[0] ? 1L : 0L,
                    timeUsed / 1000, // Convert to microseconds
                    memoryUsed
            };

        } catch (Exception e) {
            System.out.println("Error running solver: " + e.getMessage());
            return new long[] { 0L, 0L, 0L }; // Return zeros on error
        }
    }

    /**
     * Main execution: performs warm-up, benchmarking, and logs results.
     * Reads puzzles, benchmarks solvers, records individual and summary statistics.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            dummyRuns(inputFilePath, dummyRuns);
            WriteFile.clearFile(); // Prepare output

            int sudokuId = 1; // Start counting puzzles from 1

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length != 2)
                        continue;

                    String level = parts[0].trim();
                    String puzzle = parts[1].trim();

                    for (Solver solver : solvers) {
                        Sudoku sudoku = new Sudoku(N);
                        try {
                            sudoku.read(puzzle);
                            solver.setSudoku(sudoku);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        long[] result = realRunning(solver);

                        String algorithm = solver.getName();
                        String timestamp = java.time.LocalDateTime.now()
                                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        boolean status = result[0] != 0;
                        long timeTaken = result[1];
                        long memoryUsed = result[2];

                        // Add sudokuId to the output
                        WriteFile.writeFile(sudokuId, level, status, timeTaken, memoryUsed, algorithm, timestamp);
                    }

                    sudokuId++; // Increase puzzle id for next puzzle
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}