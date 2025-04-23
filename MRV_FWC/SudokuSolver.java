package MRV_FWC;

public class SudokuSolver {
    private static final int SIZE = 9;
    private int[][] grid;
    private boolean[][][] domains; // domains[row][col][value] = true nếu value+1 hợp lệ cho ô (row, col)

    public SudokuSolver(int[][] grid) {
        this.grid = grid;
        this.domains = new boolean[SIZE][SIZE][SIZE]; // Miền giá trị là 1–9, ta dùng index 0–8
        initializeDomains();
    }

    private void initializeDomains() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    boolean[] valid = getValidValues(row, col);
                    for (int i = 0; i < SIZE; i++) {
                        domains[row][col][i] = valid[i];
                    }
                }
            }
        }
    }

    private boolean[] getValidValues(int row, int col) {
        boolean[] valid = new boolean[SIZE]; // từ 1–9, tức index 0–8
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

    private int[] selectCellWithMRV() {
        int minSize = 10;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    int count = 0;
                    for (int v = 0; v < SIZE; v++) {
                        if (domains[row][col][v]) count++;
                    }
                    if (count < minSize) {
                        minSize = count;
                        selected = new int[]{row, col};
                    }
                }
            }
        }
        return selected;
    }

    public boolean solve() {
        int[] cell = selectCellWithMRV();
        if (cell == null) return true; // đã xong

        int row = cell[0], col = cell[1];
        boolean[] backup = new boolean[SIZE];
        for (int i = 0; i < SIZE; i++) backup[i] = domains[row][col][i];

        for (int v = 0; v < SIZE; v++) {
            if (domains[row][col][v] && isValid(row, col, v + 1)) {
                grid[row][col] = v + 1;

                boolean[][][] removed = new boolean[SIZE][SIZE][SIZE];
                removeFromDomains(row, col, v + 1, removed);

                if (forwardCheck(row, col)) {
                    if (solve()) return true;
                }

                grid[row][col] = 0;
                restoreDomains(removed);
            }
        }

        for (int i = 0; i < SIZE; i++) domains[row][col][i] = backup[i];

        return false;
    }

    private boolean isValid(int row, int col, int value) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == value || grid[i][col] == value) return false;
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == value) return false;
            }
        }

        return true;
    }

    private void removeFromDomains(int row, int col, int value, boolean[][][] removed) {
        int v = value - 1;

        for (int i = 0; i < SIZE; i++) {
            markRemoved(row, i, v, removed);
            markRemoved(i, col, v, removed);
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                markRemoved(r, c, v, removed);
            }
        }
    }

    private void markRemoved(int row, int col, int v, boolean[][][] removed) {
        if (grid[row][col] == 0 && domains[row][col][v]) {
            domains[row][col][v] = false;
            removed[row][col][v] = true;
        }
    }

    private void restoreDomains(boolean[][][] removed) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (int v = 0; v < SIZE; v++) {
                    if (removed[row][col][v]) {
                        domains[row][col][v] = true;
                    }
                }
            }
        }
    }

    private boolean forwardCheck(int row, int col) {
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

    public void printGrid() {
        for (int r = 0; r < SIZE; r++) {
            if (r % 3 == 0) {
                System.out.println("+-------+-------+-------+");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c % 3 == 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[r][c] + " ");
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }    

    public static void main(String[] args) {
        int[][] puzzle = { // Lưới Sudoku cần giải
                { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
                { 6, 0, 0, 1, 9, 5, 0, 0, 0 },
                { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
                { 8, 0, 0, 0, 6, 0, 0, 0, 3 },
                { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
                { 7, 0, 0, 0, 2, 0, 0, 0, 6 },
                { 0, 6, 0, 0, 0, 0, 2, 8, 0 },
                { 0, 0, 0, 4, 1, 9, 0, 0, 5 },
                { 0, 0, 0, 0, 8, 0, 0, 7, 9 }
        };

        SudokuSolver solver = new SudokuSolver(puzzle); // Tạo đối tượng giải Sudoku

        if (solver.solve()) { // Nếu giải được Sudoku
            System.out.println("Sudoku Solved:"); // In thông báo
            solver.printGrid(); // In lưới Sudoku đã giải
        } else {
            System.out.println("No solution found."); // In thông báo nếu không giải được
        }
    }
}
