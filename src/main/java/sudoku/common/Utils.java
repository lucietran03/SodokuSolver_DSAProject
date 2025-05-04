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
}
