package sudoku.solver;

import sudoku.solver.dancinglinks.AlgorithmX;

/**
 * The DancingLinks class is an implementation of a Sudoku solver using the 
 * Dancing Links algorithm, which is a technique for solving the exact cover 
 * problem efficiently. This class extends the Solver base class.
 * 
 * <p>Time Complexity (Worst Case):</p>
 * <ul>
 *   <li>Constructor: O(1)</li>
 *   <li>{@link #solve()}: O(2^n), where n is the number of constraints to satisfy.</li>
 * </ul>
 */
public class DancingLinks extends Solver {

    /**
     * Constructs a new DancingLinks solver instance with the name "Dancing Links".
     * 
     * <p>Time Complexity: O(1)</p>
     */
    public DancingLinks() {
        super("Dancing Links");
    }

    /**
     * Solves the Sudoku puzzle using the Dancing Links algorithm.
     * 
     * @return {@code true} if the Sudoku puzzle is solved successfully, 
     *         {@code false} otherwise.
     * 
     * <p>Time Complexity (Worst Case): O(2^n), where n is the number of constraints to satisfy.</p>
     */
    @Override
    public boolean solve() {
        AlgorithmX solver = new AlgorithmX();
        return solver.run(sudoku.getGrid());
    }
}
