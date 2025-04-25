package sudoku.solver;


import sudoku.common.ArrayStack;

public class ForwardChecking extends Solver {
    private boolean[][][] domains;
    private ArrayStack<DomainChange> changeStack = new ArrayStack<>();

    public ForwardChecking() {
        super("Backtracking: MRV + Forward Checking");
        domains = new boolean[SIZE][SIZE][SIZE];
    }

    private static class DomainChange {
        int row, col, val;
        DomainChange(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.val = val;
        }
    }

    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        initializeDomains(grid);
        return solveWithFC(grid);
    }

    private boolean solveWithFC(int[][] grid) {
        int[] cell = selectCellWithMRV(grid);
        if (cell == null) return true;

        int row = cell[0], col = cell[1];
        boolean[] backup = domains[row][col].clone();

        for (int v = 0; v < SIZE; v++) {
            if (domains[row][col][v] && isValid(row, col, v + 1)) {
                grid[row][col] = v + 1;
                int prevSize = changeStack.size();

                removeFromDomains(row, col, v + 1, grid);

                if (forwardCheck(row, col, grid) && solveWithFC(grid))
                    return true;

                grid[row][col] = 0;
                restoreDomains(prevSize);
            }
        }

        domains[row][col] = backup;
        return false;
    }

    private void initializeDomains(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    boolean[] valid = getValidValues(row, col, grid);
                    domains[row][col] = valid;
                }
            }
        }
    }

    private boolean[] getValidValues(int row, int col, int[][] grid) {
        boolean[] valid = new boolean[SIZE];
        for (int i = 0; i < SIZE; i++) valid[i] = true;

        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] != 0) valid[grid[row][i] - 1] = false;
            if (grid[i][col] != 0) valid[grid[i][col] - 1] = false;
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] != 0) valid[grid[r][c] - 1] = false;
            }
        }

        return valid;
    }

    private int[] selectCellWithMRV(int[][] grid) {
        int minCount = SIZE + 1;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    int count = 0;
                    for (int v = 0; v < SIZE; v++) {
                        if (domains[row][col][v]) count++;
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

    private void removeFromDomains(int row, int col, int value, int[][] grid) {
        int v = value - 1;
        for (int i = 0; i < SIZE; i++) {
            mark(row, i, v, grid);
            mark(i, col, v, grid);
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                mark(r, c, v, grid);
            }
        }
    }

    private void mark(int row, int col, int v, int[][] grid) {
        if (grid[row][col] == 0 && domains[row][col][v]) {
            domains[row][col][v] = false;
            changeStack.push(new DomainChange(row, col, v));
        }
    }

    private void restoreDomains(int prevSize) {
        while (changeStack.size() > prevSize) {
            DomainChange change = changeStack.pop();
            domains[change.row][change.col][change.val] = true;
        }
    }

    private boolean forwardCheck(int row, int col, int[][] grid) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) {
                    boolean hasValue = false;
                    for (int v = 0; v < SIZE; v++) {
                        if (domains[r][c][v]) {
                            hasValue = true;
                            break;
                        }
                    }
                    if (!hasValue) return false;
                }
            }
        }
        return true;
    }
}
