package sudoku;

import sudoku.common.Utils;
import sudoku.model.Sudoku;
import sudoku.solver.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String input = "800000000003600000070090200050007000000045700000100030001000068008500010090000400"; // ví dụ

        Solver[] solvers = {
                new DancingLinks(),
                new BasicBacktracking(),
                new ForwardChecking(),
                new MRVBacktracking(),
        };

        for (Solver solver : solvers) {
            Sudoku sudoku = new Sudoku(9);
            sudoku.read(input);
            solver.setSudoku(sudoku);

            Runtime.getRuntime().gc();

            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            solver.solve();

            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;


            System.out.println("==> " + solver.getName());
            System.out.println("Time (ms): " + timeUsed / 1_000_000);
            System.out.println("Memory (KB): " + memoryUsed / 1024);

            if (solver.getName().equals("Dancing Links")) {
                sudoku.print();
            }

            System.out.println("----------");
        }
    }
}
