package sudoku.solver;

import sudoku.solver.dancinglinks.AlgorithmX;

/**
 * The DancingLinks class is a solver for the Sudoku puzzle using the Dancing Links algorithm.
 * It extends the Solver class and implements the solve method, which applies Algorithm X
 * to solve the puzzle.
 * <p>
 * The Dancing Links algorithm is a highly efficient algorithm for solving exact cover problems,
 * and it can be applied to Sudoku as an exact cover problem. This implementation uses the AlgorithmX
 * class to perform the actual solving.
 * <p>
 * Big O Complexity:
 * - The worst-case time complexity of the Dancing Links algorithm is generally considered to be
 *   exponential (O(2^n)), where n is the number of variables. However, this is highly dependent
 *   on the problem instance, and the algorithm's performance can vary significantly with different
 *   Sudoku grids.
 *
 * @see AlgorithmX
 */
public class DancingLinks extends Solver {

    /**
     * Constructs a new DancingLinks solver with the name "Dancing Links".
     * <p>
     * Big O Complexity: O(1) in the worst case, as this is a simple constructor.
     */
    public DancingLinks() {
        super("Dancing Links");
    }

    /**
     * Solves the Sudoku puzzle using the Dancing Links (Algorithm X) method.
     * <p>
     * The method initializes the Algorithm X solver and runs it on the current Sudoku grid
     * to find a solution. It uses the `run` method from the AlgorithmX class to perform the actual
     * solving.
     * <p>
     * Big O Complexity: O(2^n) in the worst case, where n is the number of variables in the exact cover problem.
     *
     * @return true if the puzzle is solved, false otherwise.
     */
    @Override
    public boolean solve() {
        AlgorithmX solver = new AlgorithmX();
        return solver.run(sudoku.getGrid());
    }
}
