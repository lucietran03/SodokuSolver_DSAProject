package performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class WriteFile {
    private static final String TXT_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "txtoutput.txt").toString();

    private static final String CSV_FILEPATH = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "csvoutput.csv").toString();

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
     * Writes the Sudoku solving results to both a text file and a CSV file.
     *
     * @param level          The difficulty level of the Sudoku puzzle (e.g.,
     *                       "easy").
     * @param status         Whether the Sudoku was solved successfully.
     * @param time           The time taken to solve the Sudoku (in milliseconds).
     * @param memoryUsed     The memory used during the solving process (in bytes).
     * @param recursionCount The total number of recursions performed.
     * @param algorithm      The algorithm used to solve the Sudoku.
     * @param timestamp      The timestamp of when the solving occurred.
     */
    public static void writeFile(String level, boolean status, long time, long memoryUsed, String algorithm,
            String timestamp) {
        writeToTxtFile(TXT_FILEPATH, level, status, time, memoryUsed, algorithm, timestamp);
        writeToCsvFile(CSV_FILEPATH, level, status, time, memoryUsed, algorithm, timestamp);
    }

    private static void writeToTxtFile(String filePath, String level, boolean status, long time, long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write header row if the file is empty
            if (isFileEmpty) {
                writer.write(String.format("| %-20s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "Algorithm", "Level", "Status", "Time Taken (ms)", "Memory Used (bytes)",
                        "Time"));
                writer.newLine();
                writer.write(String.format("| %-20s | %-10s | %-10s | %-20s | %-20s | %-20s |",
                        "----------", "----------", "--------------------", "--------------------",
                        "--------------------", "--------------------"));
                writer.newLine();
            }

            // Write results in table format with separators
            writer.write(String.format("| %-20s | %-10s | %-10s | %-20s | %-20s | %-20s |",
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

    private static void writeToCsvFile(String filePath, String level, boolean status, long time, long memoryUsed,
            String algorithm, String timestamp) {
        File file = new File(filePath);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write header row if the file is empty
            if (isFileEmpty) {
                writer.write("Algorithm,Level,Status,Time Taken (ms),Memory Used (bytes)");
                writer.newLine();
            }

            // Write results in CSV format with only the required fields
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