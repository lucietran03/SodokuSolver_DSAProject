package performance;

import sudoku.model.Sudoku;
import sudoku.solver.BasicBacktracking;
import sudoku.solver.DancingLinks;
import sudoku.solver.ForwardChecking;
import sudoku.solver.MRVBacktracking;
import sudoku.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class TestInput {
    private static Solver[] solvers = {
            new DancingLinks(),
            new BasicBacktracking(),
            new ForwardChecking(),
            new MRVBacktracking(),
    };

    public static void dummyRuns(String inputFilePath, int dummyRuns) throws IOException {
        for (Solver solver : solvers) {
            // Perform dummy runs
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

                    solver.solve(); // Solve the puzzle without recording results
                    currentRun++;
                }
            }
        }
    }

    public static void main(String[] args) {
        String inputFilePath = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "java", "performance", "input.txt").toString();
        int dummyRuns = 10; // Number of dummy runs for JVM warm-up

        try {
            dummyRuns(inputFilePath, dummyRuns);

            for (Solver solver : solvers) {
                // Process the file again for actual runs
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
                        System.gc();
                        // Measure memory usage before solving
                        Runtime runtime = Runtime.getRuntime();
                        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        // Measure time and solve
                        long startTime = System.nanoTime();
                        boolean status = solver.solve();
                        long endTime = System.nanoTime();

                        // Measure memory usage after solving
                        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        long memoryUsed = memoryAfter - memoryBefore;
                        System.gc();

                        // Calculate time taken in microseconds
                        long timeTaken = (endTime - startTime) / 1000;

                        // Define algorithm and timestamp
                        String algorithm = solver.getName();
                        String timestamp = java.time.LocalDateTime.now()
                                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        // Write results to output files
                        WriteFile.writeFile(level, status, timeTaken, memoryUsed, algorithm,
                        timestamp);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}