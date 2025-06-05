package sudoku.solver;

import org.junit.jupiter.api.Test;
import sudoku.common.InputValidator;
import sudoku.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest {

    /** Path to the file containing the test case */
    private static final String TESTCASE_FILE = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "java", "performance", "sudokuIO", "input.txt").toString();
    private static final int N = SudokuConstant.N;

    /**
     * Test method runs all solvers on test cases read from file.
     *
     * @throws Exception if there is an error reading the file or solving sudoku.
     */
    @Test
    void testAllSolversFromFile() throws Exception {
        List<String> errors = new ArrayList<>();

        List<Solver> solvers = List.of(
                new DancingLinksX(),
                new BasicBacktracking(),
                new ForwardChecking(),
                new MRVBacktracking());

        try (BufferedReader reader = new BufferedReader(new FileReader(TESTCASE_FILE))) {
            String line;
            int testId = 1;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(":");
                if (parts.length != 2) {
                    errors.add("Test case line format error at line " + testId + ": " + line);
                    continue;
                }

                String level = parts[0].trim();
                String puzzle = parts[1].trim();

                try {
                    InputValidator.validateInput(puzzle);
                } catch (IllegalArgumentException e) {
                    errors.add("Invalid input at test " + testId + " (" + level + "): " + e.getMessage());
                    continue;
                }

                for (Solver solver : solvers) {
                    Sudoku sudoku = new Sudoku(N);
                    sudoku.read(puzzle);
                    solver.setSudoku(sudoku);

                    boolean solved = solver.solve();
                    if (!solved || !sudoku.isSolved()) {
                        errors.add("❌ Solver " + solver.getName() + " failed on test " + testId + " (" + level + ")");
                    }
                }

                testId++;
            }
        }

        if (!errors.isEmpty()) {
            errors.forEach(System.err::println);
            fail("Some test cases failed. See error log above.");
        }
    }
}
