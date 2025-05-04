package performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Utility class for writing Sudoku solver performance results to both
 * TXT and CSV files.
 */
public class WriteFile {

    /**
     * Path to the text output file.
     */
    private static final String TXT_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "txtoutput.txt").toString();

    /**
     * Path to the CSV output file.
     */
    private static final String CSV_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "csvoutput.csv").toString();

    /**
     * Clears both the text and CSV output files by overwriting them with empty
     * content.
     * This is useful before running a new set of tests to ensure no previous data
     * remains.
     * 
     * Worst-case time complexity: O(1) (writing empty content to two small files)
     */
    public static void clearFile() {
        try (FileWriter writerTxt = new FileWriter(TXT_FILEPATH, false);
                FileWriter writerCsv = new FileWriter(CSV_FILEPATH, false)) {

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
    public static void writeFile(String level, boolean status, long time, long memoryUsed, String algorithm,
            String timestamp) {
        writeToTxtFile(TXT_FILEPATH, level, status, time, memoryUsed, algorithm, timestamp);
        writeToCsvFile(CSV_FILEPATH, level, status, time, memoryUsed, algorithm, timestamp);
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
    private static void writeToTxtFile(String filePath, String level, boolean status, long time, long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (isFileEmpty) {
                writer.write(String.format("| %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "Algorithm", "Level", "Status", "Time Taken (ms)", "Memory Used (bytes)", "Time"));
                writer.newLine();
                writer.write(String.format("| %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "---------------------", "----------", "----------", "--------------------",
                        "--------------------", "--------------------"));
                writer.newLine();
            }

            writer.write(String.format("| %-21s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                    algorithm,
                    level,
                    (status ? "Solved" : "Unsolved"),
                    time,
                    memoryUsed,
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
    private static void writeToCsvFile(String filePath, String level, boolean status, long time, long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (isFileEmpty) {
                writer.write("Algorithm,Level,Status,Time Taken (ms),Memory Used (bytes)");
                writer.newLine();
            }

            String csvLine = String.format("%s,%s,%s,%d,%d",
                    algorithm,
                    level,
                    (status ? "Solved" : "Unsolved"),
                    time,
                    memoryUsed);

            writer.write(csvLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}