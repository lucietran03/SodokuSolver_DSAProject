package performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class WriteFile {
    /**
     * Writes the Sudoku solving results to both a text file and a CSV file.
     *
     * @param level          The difficulty level of the Sudoku puzzle (e.g., "easy").
     * @param status         Whether the Sudoku was solved successfully.
     * @param time           The time taken to solve the Sudoku (in milliseconds).
     * @param memoryUsed     The memory used during the solving process (in bytes).
     * @param recursionCount The total number of recursions performed.
     * @param algorithm      The algorithm used to solve the Sudoku.
     * @param timestamp      The timestamp of when the solving occurred.
     */
    public static void writeFile(String level, boolean status, long time, long memoryUsed, int recursionCount, String algorithm, String timestamp) {
        String txtFilePath = Paths.get(System.getProperty("user.dir"), "DancingLinks", "Resources", "output.txt").toString();
        String csvFilePath = Paths.get(System.getProperty("user.dir"), "DancingLinks", "Resources", "output2.csv").toString();

        writeToTxtFile(txtFilePath, level, status, time, memoryUsed, recursionCount, algorithm, timestamp);
        writeToCsvFile(csvFilePath, level, status, time, memoryUsed, recursionCount, algorithm, timestamp);
    }

    private static void writeToTxtFile(String filePath, String level, boolean status, long time, long memoryUsed, int recursionCount, String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write header row if the file is empty
            if (isFileEmpty) {
                writer.write(String.format("| %-10s | %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |",
                        "Level", "Status", "Time Taken (ms)", "Memory Used (bytes)", "Total Recursions", "Algorithm", "Time"));
                writer.newLine();
                writer.write(String.format("| %-10s | %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |",
                        "----------", "----------", "--------------------", "--------------------", "--------------------", "--------------------", "--------------------"));
                writer.newLine();
            }

            // Write results in table format with separators
            writer.write(String.format("| %-10s | %-10s | %-20d | %-20d | %-20d | %-20s | %-20s |",
                    level,
                    (status ? "Solved" : "Unsolved"),
                    time,
                    memoryUsed,
                    recursionCount,
                    algorithm,
                    timestamp));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to text file: " + e.getMessage());
        }
    }

    private static void writeToCsvFile(String filePath, String level, boolean status, long time, long memoryUsed, int recursionCount, String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write header row if the file is empty
            if (isFileEmpty) {
                writer.write("Level,Status,Time Taken (ms),Memory Used (bytes)");
                writer.newLine();
            }

            // Write results in CSV format with only the required fields
            String csvLine = String.format("%s,%s,%d,%d",
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