package sudoku.solver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sudoku.model.*;

/**
 * Unit test class for testing different Sudoku solvers.
 * The tests check if the solvers are able to solve a sample Sudoku puzzle correctly.
 * Each solver is tested by providing a predefined Sudoku puzzle as input.
 * <p>
 * This class includes tests for the following solvers:
 * <ul>
 *     <li>Dancing Links</li>
 *     <li>Basic Backtracking</li>
 *     <li>Forward Checking</li>
 *     <li>MRV Backtracking</li>
 * </ul>
 * <p>
 * Big O Complexity of Solvers:
 * - The time complexity of these solvers depends on the specific algorithm used. Generally, the backtracking-based solvers will have an exponential time complexity in the worst case (O(9^(n^2))).
 */
class SudokuTest {

    /**
     * Test method for the Dancing Links solver.
     * It verifies if the solver correctly solves a Sudoku puzzle.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testDancingLinksSolver() throws Exception {
        testSolver(new DancingLinks());  // Test the Dancing Links solver
    }

    /**
     * Test method for the Basic Backtracking solver.
     * It verifies if the solver correctly solves a Sudoku puzzle.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testBasicBacktrackingSolver() throws Exception {
        testSolver(new BasicBacktracking());  // Test the Basic Backtracking solver
    }

    /**
     * Test method for the Forward Checking solver.
     * It verifies if the solver correctly solves a Sudoku puzzle.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testForwardCheckingSolver() throws Exception {
        testSolver(new ForwardChecking());  // Test the Forward Checking solver
    }

    /**
     * Test method for the MRV Backtracking solver.
     * It verifies if the solver correctly solves a Sudoku puzzle.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testMRVBacktrackingSolver() throws Exception {
        testSolver(new MRVBacktracking());  // Test the MRV Backtracking solver
    }

    /**
     * Helper method that tests a solver and checks if it solves the puzzle correctly.
     * The method runs the solver, then asserts that the puzzle is solved and valid.
     *
     * @param solver The solver to test.
     * @throws Exception if an error occurs during the test.
     */
    private void testSolver(Solver solver) throws Exception {
        // Input Sudoku puzzle as a string (0 represents empty cells)
        String input = "000000000090060800010400030950300008000500200040900600205000000080070100001040002";  // Example Sudoku input

        // Create a new Sudoku instance and load the input puzzle
        Sudoku sudoku = new Sudoku(SudokuConstant.N);
        sudoku.read(input);

        // Set the Sudoku puzzle to the solver
        solver.setSudoku(sudoku);

        // Solve the puzzle using the solver
        boolean solved = solver.solve();  // Run the solver

        // Check if the puzzle was solved successfully
        assertTrue(solved, "The Sudoku puzzle was not solved correctly!");

        // Verify that the Sudoku puzzle is solved and the grid is valid
        assertTrue(sudoku.isSolved(), "The Sudoku puzzle was not solved correctly!");
    }
}
