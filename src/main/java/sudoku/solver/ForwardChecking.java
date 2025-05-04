package sudoku.solver;

import sudoku.Mycollection.MyMap;
import sudoku.Mycollection.MySet;

/**
 * The ForwardChecking class implements the forward checking algorithm to solve
 * Sudoku puzzles.
 * It extends the Solver class and overrides the solve method to perform the
 * forward checking search.
 * The algorithm reduces the search space by checking the implications of
 * assigning a value to a cell,
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
     * The method initializes the domains of each unassigned cell and calls the
     * helper method to solve the puzzle.
     *
     * @return true if the Sudoku puzzle is solved, false if no solution exists.
     *         Time Complexity (Worst-case): O(9^81)
     */
    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        MyMap<String, MySet<Integer>> domains = initializeDomains(grid); // O(81 * 9 * 27)
        return solveWithFC(grid, domains);
    }

    /**
     * Solves the Sudoku puzzle using forward checking.
     * Time Complexity (Worst-case): O(9^81)
     */
    private boolean solveWithFC(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int[] cell = selectCellWithMRV(grid, domains); // O(81)
        if (cell == null)
            return true;

        int row = cell[0], col = cell[1];
        String key = row + "," + col;

        MySet<Integer> domain = domains.get(key);
        if (domain == null)
            return false;

        MySet<Integer> domainCopy = new MySet<>();
        for (Object obj : domain.toArray()) { // O(9)
            domainCopy.add((Integer) obj); // O(1) per add
        }

        for (Object obj : domainCopy.toArray()) { // Up to 9 iterations
            int num = (Integer) obj;
            if (isValid(row, col, num)) { // O(27)
                grid[row][col] = num;
                MyMap<String, MySet<Integer>> backup = deepCopy(domains); // O(81 * 9)

                if (updateDomains(row, col, num, domains)) { // O(81)
                    if (solveWithFC(grid, domains))
                        return true;
                }

                grid[row][col] = 0;
                domains = backup;
            }
        }

        return false;
    }

    /**
     * Initializes the domains for each unassigned cell.
     *
     * @param grid the Sudoku grid.
     * @return a map of domains for each unassigned cell.
     *         Time Complexity (Worst-case): O(81 * 9 * 27) = O(19683)
     */
    private MyMap<String, MySet<Integer>> initializeDomains(int[][] grid) {
        MyMap<String, MySet<Integer>> domains = new MyMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    MySet<Integer> domain = new MySet<>();
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) { // O(27)
                            domain.add(num); // O(1)
                        }
                    }
                    domains.put(row + "," + col, domain); // O(1)
                }
            }
        }

        return domains;
    }

    /**
     * Updates the domains after assigning a number.
     *
     * @return true if update is valid, false otherwise.
     *         Time Complexity (Worst-case): O(81)
     */
    private boolean updateDomains(int row, int col, int num, MyMap<String, MySet<Integer>> domains) {
        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                String key = row + "," + i;
                if (domains.containsKey(key)) {
                    MySet<Integer> domain = domains.get(key);
                    domain.remove(num); // O(1)
                    if (domain.isEmpty())
                        return false;
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (i != row) {
                String key = i + "," + col;
                if (domains.containsKey(key)) {
                    MySet<Integer> domain = domains.get(key);
                    domain.remove(num);
                    if (domain.isEmpty())
                        return false;
                }
            }
        }

        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (i != row || j != col) {
                    String key = i + "," + j;
                    if (domains.containsKey(key)) {
                        MySet<Integer> domain = domains.get(key);
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
     * Selects a cell using the Minimum Remaining Values (MRV) heuristic.
     *
     * @return coordinates [row, col], or null.
     *         Time Complexity (Worst-case): O(81)
     */
    private int[] selectCellWithMRV(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int minSize = SIZE + 1;
        int[] selected = null;

        for (String key : domains.keySet()) { // O(81)
            MySet<Integer> domain = domains.get(key);
            String[] parts = key.split(","); // O(1)
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            if (grid[row][col] == 0 && domain.size() < minSize) {
                minSize = domain.size();
                selected = new int[] { row, col };
            }
        }

        return selected;
    }

    /**
     * Deep copies the domain map.
     *
     * @param original the original domains map.
     * @return a deep copy of the domains.
     *         Time Complexity (Worst-case): O(81 * 9)
     */
    private MyMap<String, MySet<Integer>> deepCopy(MyMap<String, MySet<Integer>> original) {
        MyMap<String, MySet<Integer>> copy = new MyMap<>();
        for (String key : original.keySet()) { // O(81)
            MySet<Integer> originalSet = original.get(key);
            MySet<Integer> newSet = new MySet<>();
            for (Object num : originalSet.toArray()) { // O(9)
                newSet.add((Integer) num);
            }
            copy.put(key, newSet);
        }
        return copy;
    }

    /**
     * Validates if a number can be placed in a cell.
     *
     * @return true if valid, false otherwise.
     *         Time Complexity (Worst-case): O(27)
     */
    public boolean isValid(int row, int col, int num) {
        int[][] grid = sudoku.getGrid();
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num)
                return false;
        }
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (grid[i][j] == num)
                    return false;
            }
        }
        return true;
    }
}
