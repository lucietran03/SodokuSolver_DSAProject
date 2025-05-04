package performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import sudoku.model.Sudoku;
import sudoku.solver.BasicBacktracking;
import sudoku.solver.DancingLinks;
import sudoku.solver.ForwardChecking;
import sudoku.solver.MRVBacktracking;
import sudoku.solver.Solver;

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
        System.gc(); // Attempt garbage collection

        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime();
        boolean status = solver.solve();
        long endTime = System.nanoTime();

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        long timeTaken = (endTime - startTime) / 1000; // microseconds
        return new long[] { status ? 1L : 0L, timeTaken, memoryUsed };
    }

    /**
     * Special benchmarking for BasicBacktracking by running it 100 times.
     * Used for more stable average metrics due to its simple implementation.
     *
     * @param solver instance of BasicBacktracking
     * @return array of [status (1 if any run succeeded), avgTime(μs),
     *         avgMemory(bytes)]
     */
    public static long[] backTrackingSolve(Solver solver) {
        long totalTime = 0;
        long totalMemory = 0;
        int successCount = 0;
        int runCount = 100;

        for (int i = 0; i < runCount; i++) {
            long[] result = realRunning(solver);
            boolean status = result[0] != 0;
            totalTime += result[1];
            totalMemory += result[2];
            if (status)
                successCount++;
        }

        long avgTime = totalTime / runCount;
        long avgMemory = totalMemory / runCount;
        long status = successCount > 0 ? 1L : 0L;

        return new long[] { status, avgTime, avgMemory };
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
                        Sudoku sudoku = new Sudoku(9);
                        try {
                            sudoku.read(puzzle);
                            solver.setSudoku(sudoku);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        long[] result;
                        if (solver instanceof BasicBacktracking) {
                            result = backTrackingSolve(solver);
                        } else {
                            result = realRunning(solver);
                        }

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