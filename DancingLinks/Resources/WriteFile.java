package Resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WriteFile {
    /**
     * Writes the status, time, memory used, recursion count, and question string to a file.
     * Appends the results to the file instead of overwriting it, with a timestamp and separator.
     *
     * @param status         Whether the Sudoku was solved successfully.
     * @param time           The time taken to solve the Sudoku (in milliseconds).
     * @param memoryUsed     The memory used during the solving process (in bytes).
     * @param recursionCount The total number of recursions performed.
     * @param question       The Sudoku puzzle question string.
     */
    public static void writeFile(boolean status, long time, long memoryUsed, int recursionCount, String question) {
        // Use an absolute path for the file
        String filePath = System.getProperty("user.dir") + "/src/main/java/DancingLinks/Resources/output.txt";

        // Ensure the output directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
                return; // Exit method to avoid further errors
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write timestamp for clarity
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("Sudoku Solver Results - " + LocalDateTime.now().format(dtf));
            writer.newLine();

            // Write results
            writer.write("Question: " + question);
            writer.newLine();
            writer.write("Status: " + (status ? "Solved" : "Unsolved"));
            writer.newLine();
            writer.write("Time Taken: " + time + " ms");
            writer.newLine();
            writer.write("Memory Used: " + memoryUsed + " bytes");
            writer.newLine();
            writer.write("Total Recursions: " + recursionCount);
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}