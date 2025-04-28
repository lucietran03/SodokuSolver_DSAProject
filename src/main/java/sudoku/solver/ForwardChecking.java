package sudoku.solver;

import sudoku.Mycollection.MyMap;
import sudoku.Mycollection.MySet;

/**
 * The ForwardChecking class implements the forward checking algorithm to solve Sudoku puzzles.
 * It extends the Solver class and overrides the solve method to perform the forward checking search.
 * The algorithm reduces the search space by checking the implications of assigning a value to a cell,
 * updating the domains of the remaining unassigned cells accordingly.
 */
public class ForwardChecking extends Solver {

    /**
     * Constructs a new ForwardChecking solver with the name "Forward Checking".
     */
    public ForwardChecking() {
        super("Forward Checking");
    }

    /**
     * Solves the Sudoku puzzle using the forward checking algorithm.
     * The method initializes the domains of each unassigned cell and calls the helper method to solve the puzzle.
     *
     * @return true if the Sudoku puzzle is solved, false if no solution exists.
     */
    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        MyMap<String, MySet<Integer>> domains = initializeDomains(grid);
        return solveWithFC(grid, domains);
    }

    /**
     * Solves the Sudoku puzzle using forward checking.
     * This method selects a cell with the minimum remaining values (MRV), tries all valid values,
     * and updates the domains of other cells. If the puzzle is not solved, it backtracks.
     *
     * @param grid the Sudoku grid.
     * @param domains the domains for unassigned cells.
     * @return true if the puzzle is solved, false if no solution exists.
     */
    private boolean solveWithFC(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int[] cell = selectCellWithMRV(grid, domains);
        if (cell == null) return true; // Puzzle solved

        int row = cell[0], col = cell[1];
        String key = row + "," + col;

        MySet<Integer> domain = domains.get(key);
        if (domain == null) return false; // Safety check

        // Create a copy of the domain to iterate over
        MySet<Integer> domainCopy = new MySet<>();
        for (Object obj : domain.toArray()) {
            domainCopy.add((Integer) obj);
        }

        for (Object obj : domainCopy.toArray()) {
            int num = (Integer) obj;
            if (isValid(row, col, num)) {
                grid[row][col] = num;
                MyMap<String, MySet<Integer>> backup = deepCopy(domains);

                // Forward checking: update domains
                if (updateDomains(row, col, num, domains)) {
                    if (solveWithFC(grid, domains)) return true;
                }

                // Backtrack
                grid[row][col] = 0;
                domains = backup;
            }
        }

        return false;
    }

    /**
     * Initializes the domains for each unassigned cell in the grid.
     * For each unassigned cell, the domain contains all valid numbers (1 to 9) that can be placed in that cell.
     *
     * @param grid the Sudoku grid.
     * @return a map of domains for each unassigned cell.
     */
    private MyMap<String, MySet<Integer>> initializeDomains(int[][] grid) {
        MyMap<String, MySet<Integer>> domains = new MyMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    MySet<Integer> domain = new MySet<>();
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) {
                            domain.add(num);
                        }
                    }
                    domains.put(row + "," + col, domain);
                }
            }
        }

        return domains;
    }

    /**
     * Updates the domains for the cells in the same row, column, and 3x3 box as the current cell.
     * Removes the number from the domains of other affected cells.
     *
     * @param row the row of the current cell.
     * @param col the column of the current cell.
     * @param num the number being placed in the current cell.
     * @param domains the domains map to update.
     * @return true if the update is valid, false if any domain becomes empty.
     */
    private boolean updateDomains(int row, int col, int num, MyMap<String, MySet<Integer>> domains) {
        // Update domains for cells in the same row
        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                String key = row + "," + i;
                if (domains.containsKey(key)) {
                    MySet<Integer> domain = domains.get(key);
                    domain.remove(num);
                    if (domain.isEmpty()) return false;
                }
            }
        }

        // Update domains for cells in the same column
        for (int i = 0; i < SIZE; i++) {
            if (i != row) {
                String key = i + "," + col;
                if (domains.containsKey(key)) {
                    MySet<Integer> domain = domains.get(key);
                    domain.remove(num);
                    if (domain.isEmpty()) return false;
                }
            }
        }

        // Update domains for cells in the same 3x3 box
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (i != row || j != col) {
                    String key = i + "," + j;
                    if (domains.containsKey(key)) {
                        MySet<Integer> domain = domains.get(key);
                        domain.remove(num);
                        if (domain.isEmpty()) return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Selects the next cell to assign a value to using the Minimum Remaining Values (MRV) heuristic.
     * This heuristic selects the cell with the fewest possible values in its domain.
     *
     * @param grid the Sudoku grid.
     * @param domains the domains of all unassigned cells.
     * @return the coordinates of the selected cell [row, col], or null if all cells are assigned.
     */
    private int[] selectCellWithMRV(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int minSize = SIZE + 1;
        int[] selected = null;

        for (String key : domains.keySet()) {
            MySet<Integer> domain = domains.get(key);
            String[] parts = key.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            if (grid[row][col] == 0 && domain.size() < minSize) {
                minSize = domain.size();
                selected = new int[]{row, col};
            }
        }

        return selected;
    }

    /**
     * Creates a deep copy of the domains map to preserve the original state for backtracking.
     *
     * @param original the original domains map.
     * @return a deep copy of the domains map.
     */
    private MyMap<String, MySet<Integer>> deepCopy(MyMap<String, MySet<Integer>> original) {
        MyMap<String, MySet<Integer>> copy = new MyMap<>();
        for (String key : original.keySet()) {
            MySet<Integer> originalSet = original.get(key);
            MySet<Integer> newSet = new MySet<>();
            for (Object num : originalSet.toArray()) {
                newSet.add((Integer) num);
            }
            copy.put(key, newSet);
        }
        return copy;
    }

    /**
     * Checks if placing the number in the specified cell is valid according to Sudoku rules.
     * Validity is checked by ensuring the number does not already exist in the row, column, or 3x3 box.
     *
     * @param row the row of the cell.
     * @param col the column of the cell.
     * @param num the number to place in the cell.
     * @return true if the number is valid, false otherwise.
     */
    public boolean isValid(int row, int col, int num) {
        int[][] grid = sudoku.getGrid();
        // Check row
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num) return false;
        }
        // Check column
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][col] == num) return false;
        }
        // Check 3x3 box
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (grid[i][j] == num) return false;
            }
        }
        return true;
    }
}
