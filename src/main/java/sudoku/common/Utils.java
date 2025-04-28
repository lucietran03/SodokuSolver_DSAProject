package sudoku.common;

/**
 * Utility class providing methods for memory usage calculation and unit conversions.
 */
public class Utils {

    /**
     * Calculates the memory currently used by the JVM.
     * Optionally triggers garbage collection before measurement.
     *
     * @param callGC If true, triggers garbage collection before measuring memory usage.
     * @return The amount of memory used by the JVM in bytes.
     * <p>
     * Worst-case Time Complexity: O(n), where n is the number of objects in memory,
     * due to the potential garbage collection process.
     */
    public static long getMemoryUsed(boolean callGC) {
        Runtime runtime = Runtime.getRuntime();
        if (callGC) {
            runtime.gc();
        }

        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Converts a time duration from nanoseconds to milliseconds.
     *
     * @param nano The time duration in nanoseconds.
     * @return The equivalent time duration in milliseconds.
     *
     * Time Complexity: O(1)
     */
    public static double nanoToMillis(long nano) {
        return nano / 1_000_000.0;
    }

    /**
     * Converts a size from bytes to kilobytes.
     *
     * @param bytes The size in bytes.
     * @return The equivalent size in kilobytes.
     *
     * Time Complexity: O(1)
     */
    public static double bytesToKilobytes(long bytes) {
        return bytes / 1024.0;
    }
}
