package sudoku.solver;

import sudoku.Mycollection.MyMap;
import sudoku.Mycollection.MySet;

public class ForwardChecking extends Solver {

    public ForwardChecking() {
        super("Forward Checking");
    }

    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        MyMap<String, MySet<Integer>> domains = initializeDomains(grid);
        return solveWithFC(grid, domains);
    }

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

    // Implementation of isValid (if not provided by Solver)
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