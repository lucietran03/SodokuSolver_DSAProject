package sudoku.solver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sudoku.model.*;

class SudokuTest {

    @Test
    void testDancingLinksSolver() throws Exception {
        testSolver(new DancingLinks());  // Test the Dancing Links solver
    }

    @Test
    void testBasicBacktrackingSolver() throws Exception {
        testSolver(new BasicBacktracking());  // Test the Basic Backtracking solver
    }

    @Test
    void testForwardCheckingSolver() throws Exception {
        testSolver(new ForwardChecking());  // Test the Forward Checking solver
    }

    @Test
    void testMRVBacktrackingSolver() throws Exception {
        testSolver(new MRVBacktracking());  // Test the MRV Backtracking solver
    }

    // This method tests the solver and checks if it solves the puzzle correctly
    private void testSolver(Solver solver) throws Exception {
        // Input Sudoku puzzle as a string
        String input = "000000000090060800010400030950300008000500200040900600205000000080070100001040002";  // Example Sudoku input

        // Create a new Sudoku instance and load the input
        Sudoku sudoku = new Sudoku(SudokuConstant.N);
        sudoku.read(input);

        // Set the Sudoku puzzle to the solver
        solver.setSudoku(sudoku);

        // Solve the puzzle
        boolean solved = solver.solve();  // Run the solver

        // Check if the solver solved the Sudoku correctly
        assertTrue(solved, "The Sudoku puzzle was not solved correctly!");

        // Verify that the Sudoku puzzle is solved correctly
        assertTrue(sudoku.isSolved(), "The Sudoku puzzle was not solved correctly!");
    }
}
