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
 * This class is responsible for benchmarking various Sudoku solving algorithms
 * by performing dummy runs to warm up the JVM and actual runs to record
 * performance metrics.
 */
public class TestInput {
    private static final Solver[] solvers = {
            new DancingLinks(),
            new BasicBacktracking(),
            new ForwardChecking(),
            new MRVBacktracking(),
    };

    /**
     * Performs a series of dummy runs on the provided input file to warm up the
     * JVM.
     * This helps achieve more accurate performance measurements during actual runs.
     *
     * @param inputFilePath the path to the file containing Sudoku puzzles.
     * @param dummyRuns     the number of dummy runs to perform per solver.
     * @throws IOException if there is an issue reading the input file.
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

                    solver.solve(); // Warm-up run, result not recorded
                    currentRun++;
                }
            }
        }
    }

    /**
     * The main method that executes dummy runs for warm-up and actual runs to
     * collect performance data on solving Sudoku puzzles using multiple algorithms.
     *
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        String inputFilePath = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "java", "performance", "input.txt").toString();
        int dummyRuns = 10; // JVM warm-up run count

        try {
            dummyRuns(inputFilePath, dummyRuns);
            WriteFile.clearFile(); // Clear previous output before actual test

            for (Solver solver : solvers) {
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts.length != 2)
                            continue;

                        String level = parts[0].trim();
                        String puzzle = parts[1].trim();

                        Sudoku sudoku = new Sudoku(9);
                        try {
                            sudoku.read(puzzle);
                            solver.setSudoku(sudoku);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        System.gc(); // Suggest garbage collection

                        // Memory before
                        Runtime runtime = Runtime.getRuntime();
                        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        // Timing and solving
                        long startTime = System.nanoTime();
                        boolean status = solver.solve();
                        long endTime = System.nanoTime();

                        // Memory after
                        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        long memoryUsed = memoryAfter - memoryBefore;
                        System.gc();

                        // Time in microseconds
                        long timeTaken = (endTime - startTime) / 1000;

                        // Metadata
                        String algorithm = solver.getName();
                        String timestamp = java.time.LocalDateTime.now()
                                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        // Output
                        WriteFile.writeFile(level, status, timeTaken, memoryUsed, algorithm, timestamp);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}