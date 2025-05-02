import Model.Sudoku;
import Resources.WriteFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestInput {
    public static void main(String[] args) {
        String inputFilePath = "DancingLinks/Resources/input.txt";
        int dummyRuns = 10; // Number of dummy runs for JVM warm-up

        try {
            // Perform dummy runs
            try (BufferedReader dummyReader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;
                int currentRun = 0;

                while ((line = dummyReader.readLine()) != null && currentRun < dummyRuns) {
                    String[] parts = line.split(":");
                    if (parts.length != 2) continue;

                    String puzzle = parts[1].trim();

                    Sudoku sudoku = new Sudoku(9);
                    try {
                        sudoku.read(puzzle);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    sudoku.solve(); // Solve the puzzle without recording results
                    currentRun++;
                }
            }

            // Process the file again for actual runs
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length != 2) continue;

                    String level = parts[0].trim();
                    String puzzle = parts[1].trim();

                    Sudoku sudoku = new Sudoku(9);
                    try {
                        sudoku.read(puzzle);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.gc();
                    // Measure memory usage before solving
                    Runtime runtime = Runtime.getRuntime();
                    long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

                    // Measure time and solve
                    long startTime = System.nanoTime();
                    boolean status = sudoku.solve();
                    long endTime = System.nanoTime();

                    // Measure memory usage after solving
                    long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                    long memoryUsed = memoryAfter - memoryBefore;
                    System.gc();

                    // Calculate time taken in microseconds
                    long timeTaken = (endTime - startTime) / 1000;

                    // Get recursion count
                    int recursionCount = sudoku.getRecursionCount();

                    // Define algorithm and timestamp
                    String algorithm = "Dancing Links";
                    String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    // Write results to output files
                    WriteFile.writeFile(level, status, timeTaken, memoryUsed, recursionCount, algorithm, timestamp);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}