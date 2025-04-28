package sudoku.common;

public class Utils {
    public static long getMemoryUsed(boolean callGC) {
        Runtime runtime = Runtime.getRuntime();
        if (callGC) {
            runtime.gc();
        }
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static double nanoToMillis(long nano) {
        return nano / 1_000_000.0;
    }

    public static double bytesToKilobytes(long bytes) {
        return bytes / 1024.0;
    }
}
