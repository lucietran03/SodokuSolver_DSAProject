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

    private int[] selectCellWithMRV(int[][] grid, MyMap<String, MySet<Integer>> domains) {
        int minSize = SIZE + 1;
        int[] selected = null;

        // Implement a simple way to iterate through map entries
        // Assuming your MyMap has some way to get all keys
        // This is a placeholder - you'll need to adapt it to your MyMap implementation
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

    private MyMap<String, MySet<Integer>> deepCopy(MyMap<String, MySet<Integer>> original) {
        MyMap<String, MySet<Integer>> copy = new MyMap<>();

        // Assuming your MyMap has some way to iterate through entries
        // This is a placeholder - adapt to your MyMap implementation
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