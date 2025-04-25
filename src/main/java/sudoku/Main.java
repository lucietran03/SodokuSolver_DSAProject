package sudoku;

import sudoku.common.*;
import sudoku.model.*;
import sudoku.solver.*;

import java.util.Scanner;

import static sudoku.model.SudokuConstant.N;

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
            Sudoku sudoku = new Sudoku(N);
            sudoku.read(input);
            solver.setSudoku(sudoku);

            Runtime.getRuntime().gc();
            Thread.sleep(100);

            long beforeMemory = Utils.getMemoryUsed(false);
            long startTime = System.nanoTime();

            final boolean[] solved = {false};
            Thread solverThread = new Thread(() -> {
                solved[0] = solver.solve();
            });

            solverThread.start();
            solverThread.join(2 * 60 * 1000);

            long timeUsed = System.nanoTime() - startTime;
            long afterMemory = Utils.getMemoryUsed(false);
            long memoryUsed = afterMemory - beforeMemory;

            System.out.println("==> " + solver.getName());

            if (solverThread.isAlive()) {
                solverThread.interrupt();
                System.out.println("Solver failed: Timeout after 2 minutes");
                System.out.println("Time (ms): " + timeUsed / 1_000_000);
            } else {
                System.out.println("Time (ms): " + timeUsed / 1_000_000);
                System.out.println("Memory (KB): " + memoryUsed / 1024);
                sudoku.print();

//                if (solver.getName().equals("Dancing Links")) {
//                    sudoku.print();
//                }
            }

            System.out.println("----------");
        }
    }
}