package performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Utility class for writing Sudoku solver performance results to both
 * TXT and CSV files.
 * 
 * Provides methods for writing the results, clearing files, and generating
 * summary reports in multiple formats.
 */
public class WriteFile {

    /**
     * Path to the text output file.
     */
    private static final String TXT_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "sudokuIO", "txtoutput.txt").toString();

    /**
     * Path to the CSV output file.
     */
    private static final String CSV_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "sudokuIO", "csvoutput.csv").toString();

    /**
     * Clears the content of the text, CSV, and summary output files by overwriting
     * them with empty content.
     * This method is useful for resetting the files before running a new set of
     * tests to ensure no residual data remains.
     * Additionally, it writes a header row to the summary file for better
     * readability.
     * 
     * Worst-case time complexity: O(1) (writing empty content and a small header
     * row to the files)
     */
    public static void clearFile() {
        try (FileWriter writerTxt = new FileWriter(TXT_FILEPATH, false);
                FileWriter writerCsv = new FileWriter(CSV_FILEPATH, false);) {

            writerTxt.write("");
            writerCsv.write("");

        } catch (IOException e) {
            System.err.println("Error clearing files: " + e.getMessage());
        }
    }

    /**
     * Writes the Sudoku solving results to both the text and CSV files.
     *
     * @param level      The difficulty level of the Sudoku puzzle (e.g., "easy",
     *                   "medium").
     * @param status     Whether the Sudoku was solved successfully (true) or not
     *                   (false).
     * @param time       The time taken to solve the Sudoku in milliseconds.
     * @param memoryUsed The amount of memory used during solving, in bytes.
     * @param algorithm  The name of the algorithm used to solve the puzzle.
     * @param timestamp  The timestamp when the solving was performed.
     * 
     *                   Worst-case time complexity: O(n) (where n is the size of
     *                   the data being written to both files)
     */
    public static void writeFile(int sudokuId, String level, boolean status, long time, long memoryUsed,
            String algorithm,
            String timestamp) {
        writeToTxtFile(TXT_FILEPATH, sudokuId, level, status, time, memoryUsed, algorithm, timestamp);
        writeToCsvFile(CSV_FILEPATH, sudokuId, level, status, time, memoryUsed, algorithm, timestamp);
    }

    /**
     * Writes the results to a formatted table in a plain text file.
     *
     * @param filePath   Path to the output text file.
     * @param level      The difficulty level of the Sudoku.
     * @param status     Solving status: true if solved, false otherwise.
     * @param time       Time taken in milliseconds.
     * @param memoryUsed Memory used in bytes.
     * @param algorithm  Solver algorithm name.
     * @param timestamp  Timestamp of the execution.
     * 
     *                   Worst-case time complexity: O(n) (where n is the size of
     *                   the data being written to the text file)
     */
    private static void writeToTxtFile(String filePath, int id, String level, boolean status, long time,
            long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (isFileEmpty) {
                writer.write(String.format("| %-5s | %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "ID", "Algorithm", "Level", "Status", "Time Taken (ms)", "Memory Used (KB)", "Time"));
                writer.newLine();
                writer.write(String.format("| %-5s | %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "-----", "---------------------", "----------", "----------", "--------------------",
                        "--------------------", "--------------------"));
                writer.newLine();
            }

            // Chia memoryUsed cho 1024 để chuyển đổi sang KB
            long memoryInKB = memoryUsed / 1024;

            writer.write(String.format("| %-5s | %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                    id,
                    algorithm,
                    level,
                    (status ? "Solved" : "Unsolved"),
                    time,
                    memoryInKB, // Sử dụng memoryInKB thay vì memoryUsed
                    timestamp));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to text file: " + e.getMessage());
        }
    }

    /**
     * Writes the results to a CSV file.
     *
     * @param filePath   Path to the output CSV file.
     * @param level      The difficulty level of the Sudoku.
     * @param status     Solving status: true if solved, false otherwise.
     * @param time       Time taken in milliseconds.
     * @param memoryUsed Memory used in bytes.
     * @param algorithm  Solver algorithm name.
     * @param timestamp  Timestamp of the execution.
     * 
     *                   Worst-case time complexity: O(n) (where n is the size of
     *                   the data being written to the CSV file)
     */
    private static void writeToCsvFile(String filePath, int id, String level, boolean status, long time,
            long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (isFileEmpty) {
                writer.write("Sudoku ID,Algorithm,Level,Status,Time Taken (ms),Memory Used (KB)");
                writer.newLine();
            }

            // Chia memoryUsed cho 1024 để chuyển đổi sang KB
            long memoryInKB = memoryUsed / 1024;

            String csvLine = String.format("%d,%s,%s,%s,%d,%d",
                    id,
                    algorithm,
                    level,
                    (status ? "Solved" : "Unsolved"),
                    time,
                    memoryInKB); // Sử dụng memoryInKB thay vì memoryUsed

            writer.write(csvLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}