package sudoku.solver;

import sudoku.Mycollection.MyMap;
import sudoku.Mycollection.MySet;

/**
 * A Solver that implements the Forward Checking algorithm for Sudoku solving.
 */
public class ForwardChecking extends Solver {

    /**
     * Constructor for the ForwardChecking class.
     */
    public ForwardChecking() {
        super("Forward Checking");
    }

    /**
     * Solves the Sudoku puzzle using the Forward Checking algorithm.
     * 
     * @return true if the puzzle is solved, false otherwise.
     * @BigO O(n^2) - Solving the puzzle involves recursive calls for each empty
     *       cell in the grid,
     *       which can lead to exploring multiple possibilities.
     */
    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        MyMap<String, MySet<Integer>> domains = initializeDomains(grid);
        return solveWithFC(grid, domains);
    }

    /**
     * Solves the Sudoku puzzle using Forward Checking recursively.
     * 
     * @param grid    The current Sudoku grid.
     * @param domains The current domain of possible values for each cell.
     * @return true if the puzzle is solved, false otherwise.
     * @BigO O(n^2) - Each recursive call checks all the possible values in the
     *       domain of a cell,
     *       and iterates over the grid to apply forward checking.
     */
    private boolean solveWithFC(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int[] cell = selectCellWithMRV(grid, domains);
        if (cell == null)
            return true; // puzzle solved

        int row = cell[0], col = cell[1];
        String key = row + "," + col;
        MySet<Integer> domain = domains.get(key);

        // Create a copy of the domain to iterate over
        MySet<Integer> domainCopy = new MySet<>();
        for (int i = 1; i <= SIZE; i++) {
            if (domain.contains(i)) {
                domainCopy.add(i);
            }
        }

        for (int num = 1; num <= SIZE; num++) {
            if (domainCopy.contains(num) && isValid(row, col, num)) {
                grid[row][col] = num;
                MyMap<String, MySet<Integer>> backup = deepCopy(domains);

                // Forward checking
                if (updateDomains(row, col, num, domains)) {
                    if (solveWithFC(grid, domains))
                        return true;
                }

                // backtrack
                grid[row][col] = 0;
                domains = backup;
            }
        }

        return false;
    }

    /**
     * Initializes the domains for each empty cell in the Sudoku grid.
     * 
     * @param grid The current Sudoku grid.
     * @return A map of domains for each empty cell.
     * @BigO O(n^2) - The method iterates over the entire grid to initialize
     *       domains.
     */
    private MyMap<String, MySet<Integer>> initializeDomains(int[][] grid) {
        MyMap<String, MySet<Integer>> domains = new MyMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    MySet<Integer> domain = new MySet<>();
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num))
                            domain.add(num);
                    }
                    domains.put(row + "," + col, domain);
                }
            }
        }

        return domains;
    }

    /**
     * Updates the domains of the cells affected by placing a number in a specific
     * cell.
     * 
     * @param row     The row of the placed number.
     * @param col     The column of the placed number.
     * @param num     The number placed in the cell.
     * @param domains The current domains of all cells.
     * @return true if the domains are updated successfully, false if any domain
     *         becomes empty.
     * @BigO O(n) - For each row, column, and box, the method iterates through at
     *       most n elements to update domains.
     */
    private boolean updateDomains(int row, int col, int num, MyMap<String, MySet<Integer>> domains) {
        // Check row
        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                String key = row + "," + i;
                MySet<Integer> domain = domains.get(key);
                if (domain != null) {
                    domain.remove(num);
                    if (domain.isEmpty())
                        return false;
                }
            }
        }

        // Check column
        for (int i = 0; i < SIZE; i++) {
            if (i != row) {
                String key = i + "," + col;
                MySet<Integer> domain = domains.get(key);
                if (domain != null) {
                    domain.remove(num);
                    if (domain.isEmpty())
                        return false;
                }
            }
        }

        // Check 3x3 box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (i != row || j != col) {
                    String key = i + "," + j;
                    MySet<Integer> domain = domains.get(key);
                    if (domain != null) {
                        domain.remove(num);
                        if (domain.isEmpty())
                            return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Selects the cell with the Minimum Remaining Values (MRV) heuristic.
     * 
     * @param grid    The current Sudoku grid.
     * @param domains The current domains of all cells.
     * @return The row and column indices of the selected cell, or null if no empty
     *         cell remains.
     * @BigO O(n^2) - Iterates over the entire grid to find the cell with the
     *       minimum remaining values.
     */
    private int[] selectCellWithMRV(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int minSize = SIZE + 1;
        int[] selected = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String key = row + "," + col;
                if (grid[row][col] == 0 && domains.containsKey(key)) {
                    MySet<Integer> domain = domains.get(key);
                    if (domain.size() < minSize) {
                        minSize = domain.size();
                        selected = new int[] { row, col };
                    }
                }
            }
        }

        return selected;
    }

    /**
     * Creates a deep copy of the given domains map.
     * 
     * @param original The original domains map.
     * @return A new map with copies of the domains.
     * @BigO O(n^2) - Iterates over the grid and copies each domain, which takes
     *       O(n) for each cell.
     */
    private MyMap<String, MySet<Integer>> deepCopy(MyMap<String, MySet<Integer>> original) {
        MyMap<String, MySet<Integer>> copy = new MyMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String key = row + "," + col;
                if (original.containsKey(key)) {
                    MySet<Integer> originalSet = original.get(key);
                    MySet<Integer> newSet = new MySet<>();
                    // Copy each element
                    for (int num = 1; num <= SIZE; num++) {
                        if (originalSet.contains(num)) {
                            newSet.add(num);
                        }
                    }
                    copy.put(key, newSet);
                }
            }
        }

        return copy;
    }
}