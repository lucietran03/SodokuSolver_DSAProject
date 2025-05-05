package performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import sudoku.common.Utils;
import sudoku.model.Sudoku;
import sudoku.solver.*;
import static sudoku.model.SudokuConstant.N;

/**
 * Benchmarks different Sudoku solving algorithms on multiple puzzles.
 * It performs warm-up runs, real executions, and logs time and memory usage.
 *
 * Solver complexities (worst-case):
 * - BasicBacktracking: O(9^(N*N)) — brute-force search.
 * - ForwardChecking: O(9^(N*N)) — with pruning via domain filtering.
 * - MRVBacktracking: O(9^(N*N)) — heuristic-based with better practical
 * performance.
 * - DancingLinks (DLX): O(N) per operation — optimized for exact cover,
 * exponential overall.
 */
public class TestInput {

    /** Path to the input file with level:puzzle entries */
    private static final String inputFilePath = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "sudokuIO", "input.txt").toString();

    /** Number of warm-up runs per solver to reduce JIT impact */
    private static final int dummyRuns = 10;

    /** Array of solver strategies to benchmark */
    private static final Solver[] solvers = {
            new DancingLinks(),
            new BasicBacktracking(),
            new ForwardChecking(),
            new MRVBacktracking(),
    };

    /**
     * Performs warm-up executions for each solver on a subset of puzzles.
     *
     * @param inputFilePath path to input puzzles
     * @param dummyRuns     number of runs per solver
     * @throws IOException if file access fails
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

                    solver.solve();
                    currentRun++;
                }
            }
        }
    }

    /**
     * Executes the solver with timing and memory usage measurement.
     *
     * @param solver the Sudoku solver to benchmark
     * @return array [solved:1|0, time in microseconds, memory in bytes]
     */
    public static long[] realRunning(Solver solver) {
        try {
            Utils.getMemoryUsed(true); // Trigger GC
            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            final boolean[] solved = { false };
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(2 * 60 * 1000); // 2-minute timeout

            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            if (solverThread.isAlive()) {
                solverThread.interrupt(); // Force stop
                return new long[] { 0L, timeUsed / 1000, memoryUsed };
            }

            boolean isActuallySolved = false;
            try {
                isActuallySolved = solver.getSudoku().isSolved();
            } catch (Exception e) {
                System.out.println("Error checking solved state: " + e.getMessage());
            }

            return new long[] {
                    (solved[0] && isActuallySolved) ? 1L : 0L,
                    timeUsed / 1000,
                    memoryUsed
            };

        } catch (Exception e) {
            System.out.println("Error running solver: " + e.getMessage());
            return new long[] { 0L, 0L, 0L };
        }
    }

    /**
     * Runs benchmarking on all puzzles using all solvers.
     * Records results in output file with timestamps.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        try {
            dummyRuns(inputFilePath, dummyRuns);
            WriteFile.clearFile();

            int sudokuId = 1;

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

                        WriteFile.writeFile(sudokuId, level, status, timeTaken, memoryUsed, algorithm, timestamp);
                    }

                    sudokuId++;
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}
