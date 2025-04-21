package BasicBacktracking;

public class SudokuSolverBasic {
    private static final int SIZE = 9;
    private int[][] grid;

    public SudokuSolverBasic(int[][] grid) {
        this.grid = grid;
    }

    public boolean solve() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            grid[row][col] = num;

                            if (solve())
                                return true;

                            grid[row][col] = 0; // backtrack
                        }
                    }
                    return false; // không số nào hợp lệ
                }
            }
        }
        return true; // hoàn thành
    }

    private boolean isValid(int row, int col, int value) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == value || grid[i][col] == value)
                return false;
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == value)
                    return false;
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

        SudokuSolverBasic solver = new SudokuSolverBasic(puzzle); // Tạo đối tượng giải Sudoku

        if (solver.solve()) { // Nếu giải được Sudoku
            System.out.println("Sudoku Solved:"); // In thông báo
            solver.printGrid(); // In lưới Sudoku đã giải
        } else {
            System.out.println("No solution found."); // In thông báo nếu không giải được
        }
    }
}
