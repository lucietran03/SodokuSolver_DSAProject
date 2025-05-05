package sudoku.solver;

// import sudoku.Mycollection.MyList;

// import sudoku.Mycollection.MyMap;
// import sudoku.Mycollection.MySet;

import java.util.*;;

public class ForwardChecking extends Solver {

    public ForwardChecking() {
        super("Forward Checking");
    }

    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        Map<String, Set<Integer>> domains = initializeDomains(grid);
        return solveWithFC(grid, domains);
    }

    private boolean solveWithFC(int[][] grid, Map<String, Set<Integer>> domains) {
        int[] cell = selectCellWithMRV(grid, domains);
        if (cell == null)
            return true; // puzzle solved

        int row = cell[0], col = cell[1];
        String key = row + "," + col;

        for (int num : new HashSet<>(domains.get(key))) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;
                Map<String, Set<Integer>> backup = deepCopy(domains);

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

    private Map<String, Set<Integer>> initializeDomains(int[][] grid) {
        Map<String, Set<Integer>> domains = new HashMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    Set<Integer> domain = new HashSet<>();
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

    private boolean updateDomains(int row, int col, int num, Map<String, Set<Integer>> domains) {
        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                String key = row + "," + i;
                if (domains.containsKey(key)) {
                    Set<Integer> domain = domains.get(key);
                    domain.remove(num);
                    if (domain.isEmpty())
                        return false;
                }
            }

            if (i != row) {
                String key = i + "," + col;
                if (domains.containsKey(key)) {
                    Set<Integer> domain = domains.get(key);
                    domain.remove(num);
                    if (domain.isEmpty())
                        return false;
                }
            }
        }

        // 3x3 box
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (i != row || j != col) {
                    String key = i + "," + j;
                    if (domains.containsKey(key)) {
                        Set<Integer> domain = domains.get(key);
                        domain.remove(num);
                        if (domain.isEmpty())
                            return false;
                    }
                }
            }
        }

        return true;
    }

    private int[] selectCellWithMRV(int[][] grid, Map<String, Set<Integer>> domains) {
        int minSize = SIZE + 1;
        int[] selected = null;

        for (String key : domains.keySet()) {
            Set<Integer> domain = domains.get(key);
            if (grid[Integer.parseInt(key.split(",")[0])][Integer.parseInt(key.split(",")[1])] == 0
                    && domain.size() < minSize) {
                minSize = domain.size();
                String[] parts = key.split(",");
                selected = new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
            }
        }

        return selected;
    }

    private Map<String, Set<Integer>> deepCopy(Map<String, Set<Integer>> original) {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }
}