package sudoku;

import sudoku.common.Utils;
import sudoku.model.*;
import sudoku.solver.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = SudokuManager.getSudokuMatrix(scanner);

        if (input == null) {
            System.out.println("Provided Sudoku is invalid. Exiting the program.");
            return;
        }

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
