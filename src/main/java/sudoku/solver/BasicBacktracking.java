package sudoku.solver;

public class BasicBacktracking extends Solver {

    public BasicBacktracking() {
        super("Backtracking");
    }

    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            grid[row][col] = num;

                            if (solve())
                                return true;

                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

}
