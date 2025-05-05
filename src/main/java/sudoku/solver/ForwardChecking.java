package sudoku.solver;

import sudoku.Mycollection.MyMap;
import sudoku.Mycollection.MySet;
import sudoku.Mycollection.MyList;

/**
 * Optimized Forward Checking solver with MRV heuristic
 */
public class ForwardChecking extends Solver {

    private static class Cell {
        final int row, col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Cell))
                return false;
            Cell other = (Cell) obj;
            return row == other.row && col == other.col;
        }
    }

    public ForwardChecking() {
        super("Forward Checking");
    }

    @Override
    public boolean solve() {
        int[][] grid = sudoku.getGrid();
        MyMap<Cell, MySet<Integer>> domains = initializeDomains(grid);
        return solveWithFC(grid, domains);
    }

    private boolean solveWithFC(int[][] grid, MyMap<Cell, MySet<Integer>> domains) {
        if (domains.size() == 0)
            return true;

        Cell cell = selectCellWithMRV(grid, domains);
        if (cell == null)
            return true;

        MySet<Integer> domain = domains.get(cell);
        MyList<Integer> domainCopy = new MyList<>();
        for (Object num : domain.toArray()) {
            domainCopy.add((Integer) num);
        }

        for (int num : domainCopy) {
            grid[cell.row][cell.col] = num;
            domains.remove(cell);

            MyList<Cell> modified = new MyList<>();
            if (updateDomains(cell, num, domains, modified)) {
                if (solveWithFC(grid, domains)) {
                    return true;
                }
            }

            // Backtrack
            grid[cell.row][cell.col] = 0;
            domains.put(cell, domain);
            restoreDomains(num, domains, modified);
        }

        return false;
    }

    private MyMap<Cell, MySet<Integer>> initializeDomains(int[][] grid) {
        MyMap<Cell, MySet<Integer>> domains = new MyMap<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    Cell cell = new Cell(row, col);
                    MySet<Integer> domain = new MySet<>();

                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(grid, row, col, num)) {
                            domain.add(num);
                        }
                    }

                    domains.put(cell, domain);
                }
            }
        }

        return domains;
    }

    private boolean updateDomains(Cell cell, int num,
            MyMap<Cell, MySet<Integer>> domains,
            MyList<Cell> modified) {
        // Check row
        for (int col = 0; col < SIZE; col++) {
            if (col != cell.col) {
                Cell current = new Cell(cell.row, col);
                if (updateDomain(current, num, domains, modified) == false) {
                    return false;
                }
            }
        }

        // Check column
        for (int row = 0; row < SIZE; row++) {
            if (row != cell.row) {
                Cell current = new Cell(row, cell.col);
                if (updateDomain(current, num, domains, modified) == false) {
                    return false;
                }
            }
        }

        // Check box
        int boxRow = cell.row / 3 * 3;
        int boxCol = cell.col / 3 * 3;

        for (int row = boxRow; row < boxRow + 3; row++) {
            for (int col = boxCol; col < boxCol + 3; col++) {
                if (row != cell.row || col != cell.col) {
                    Cell current = new Cell(row, col);
                    if (updateDomain(current, num, domains, modified) == false) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean updateDomain(Cell cell, int num,
            MyMap<Cell, MySet<Integer>> domains,
            MyList<Cell> modified) {
        if (domains.containsKey(cell)) {
            MySet<Integer> domain = domains.get(cell);
            if (domain.contains(num)) {
                domain.remove(num);
                modified.add(cell);
                if (domain.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void restoreDomains(int num,
            MyMap<Cell, MySet<Integer>> domains,
            MyList<Cell> modified) {
        for (Cell cell : modified) {
            domains.get(cell).add(num);
        }
    }

    private Cell selectCellWithMRV(int[][] grid, MyMap<Cell, MySet<Integer>> domains) {
        int minSize = Integer.MAX_VALUE;
        Cell selected = null;

        for (Cell cell : domains.keySet()) {
            int size = domains.get(cell).size();
            if (size < minSize) {
                minSize = size;
                selected = cell;
            }
        }

        return selected;
    }

    private boolean isValid(int[][] grid, int row, int col, int num) {
        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (grid[row][c] == num)
                return false;
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (grid[r][col] == num)
                return false;
        }

        // Check box
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;

        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (grid[r][c] == num)
                    return false;
            }
        }

        return true;
    }
}