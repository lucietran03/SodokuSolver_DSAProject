package sudoku.solver;

public class MRVBacktracking extends Solver {

    public MRVBacktracking() {
        super("Backtracking with MRV");
    }

    @Override
    public boolean solve() {
        return solveWithMRV(sudoku.getGrid());
    }

    private boolean solveWithMRV(int[][] grid) {
        int[] cell = selectCellWithMRV(grid);
        if (cell == null) return true; // puzzle solved

        int row = cell[0], col = cell[1];
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;

                if (solveWithMRV(grid))
                    return true;

                grid[row][col] = 0; // backtrack
            }
        }
        return false;
    }

    private int[] selectCellWithMRV(int[][] grid) {
        int minCount = SIZE + 1;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    int count = 0;
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) count++;
                    }
                    if (count < minCount) {
                        minCount = count;
                        selected = new int[]{row, col};
                    }
                }
            }
        }

        return selected;
    }
}
