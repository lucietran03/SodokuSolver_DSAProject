package org.example;

import java.util.*;

public class SudokuSolver_Ver2 {
    // DLX Node class for the dancing links structure
    static class Node {
        Node left, right, up, down;
        ColumnNode column;
        int row, col, value;

        Node() {
            left = right = up = down = this;
        }
    }

    // ColumnNode for the column headers in the DLX matrix
    static class ColumnNode extends Node {
        int size;
        ColumnNode() {
            super();
            column = this;
        }
    }

    private ColumnNode root;
    private List<Node> solution;
    private int[][] grid;
    private int[][] exactCoverMatrix;
    private List<int[]> rowMetadata; // Stores (r, c, v) for each row

    public SudokuSolver_Ver2(int[][] grid) {
        // Validate input grid
        if (grid == null || grid.length != 9 || !isValidGrid(grid)) {
            throw new IllegalArgumentException("Invalid 9x9 Sudoku grid");
        }
        this.grid = grid;
        solution = new ArrayList<>();
        rowMetadata = new ArrayList<>();
    }

    // Validate the grid: 9x9, values 0-9
    private boolean isValidGrid(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            if (grid[i] == null || grid[i].length != 9) {
                return false;
            }
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] < 0 || grid[i][j] > 9) {
                    return false;
                }
            }
        }
        return true;
    }

    /// STEP1 : create the exact cover matrix
    // Build the exact cover matrix as an int 2D array
    private void buildExactCoverMatrix() {
        ///  step1.1 define constraints, refer to question1 in step1.1
        int n = 9;
        int constraints = 4; // Row-Column, Row-Number, Column-Number, Box-Number
        int rows = n * n * n; // Possible placements: 9x9 cells x 9 numbers
        int cols = n * n * constraints; // 4 constraints per cell

        /// 324 X 729 MATRIX refer to question1.1 in step1
        exactCoverMatrix = new int[rows][cols];
        int rowIdx = 0;
        /// step1.2 : create the 729 row and 324 column matrix
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                for (int v = 1; v <= n; v++) {
                    ///  this is used to handle for the provided hint
                    ///  grid[r][c] != 0 meaning that the cell is already filled => dont need to generate possible row
                    ///  for example : if we have 30hints, what actually row we have ? refer to question 1.4
                    if (grid[r][c] != 0 && grid[r][c] != v) continue;
                    /// step1.3 : create the row for the exact cover matrix
                    ///  using the support createExactCoverRow to create the row.
                    ///  each row will contains 4 number 1, one for each constraint
                    exactCoverMatrix[rowIdx] = createExactCoverRow(r, c, v, n, cols);
                    rowMetadata.add(new int[]{r, c, v}); // Store metadata
                    rowIdx++;
                }
            }
        }

        /// Trim the matrix to the actual number of rows used
        ///  refer to question 1.4
        int[][] trimmedMatrix = new int[rowIdx][cols];
        for (int i = 0; i < rowIdx; i++) {
            System.arraycopy(exactCoverMatrix[i], 0, trimmedMatrix[i], 0, cols);
        }
        exactCoverMatrix = trimmedMatrix;
    }
    ///
    // Create a single row for the exact cover matrix
    private int[] createExactCoverRow(int r, int c, int v, int n, int cols) {
        int[] row = new int[cols];
        int box = (r / 3) * 3 + c / 3;
        ///  THIS IS JUST A FORMULA TO CREATE THE ROW, REFER TO THE FORMULA step1.3

        // Constraint 1: Row-Column (each cell has exactly one number)
        int col1 = r * n + c;
        // Constraint 2: Row-Number (each number appears once per row)
        int col2 = n * n + r * n + v - 1;
        // Constraint 3: Column-Number (each number appears once per column)
        int col3 = n * n * 2 + c * n + v - 1;
        // Constraint 4: Box-Number (each number appears once per box)
        int col4 = n * n * 3 + box * n + v - 1;

        ///  EACH ROW HAVE 4 1s, one for each constraint ( 4 contrainsts )

        row[col1] = 1;
        row[col2] = 1;
        row[col3] = 1;
        row[col4] = 1;

        return row;
    }

    // Build the DLX matrix from the exact cover matrix
    private void buildDLXMatrix() {
        int cols = exactCoverMatrix[0].length;
        int rows = exactCoverMatrix.length;

        // Initialize header
        root = new ColumnNode();
        ColumnNode[] columns = new ColumnNode[cols];
        ColumnNode prev = root;
        for (int i = 0; i < cols; i++) {
            columns[i] = new ColumnNode();
            columns[i].right = root;
            columns[i].left = prev;
            prev.right = columns[i];
            prev = columns[i];
        }
        root.left = columns[cols - 1];
        columns[cols - 1].right = root;

        // Build the DLX matrix
        for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
            Node[] nodes = new Node[4];
            int nodeCount = 0;

            // Get (r, c, v) from metadata
            int[] metadata = rowMetadata.get(rowIdx);
            int r = metadata[0];
            int c = metadata[1];
            int v = metadata[2];

            // Create nodes for each 1 in the exact cover matrix row
            for (int j = 0; j < cols; j++) {
                if (exactCoverMatrix[rowIdx][j] == 1) {
                    nodes[nodeCount] = new Node();
                    nodes[nodeCount].row = r;
                    nodes[nodeCount].col = c;
                    nodes[nodeCount].value = v;
                    nodes[nodeCount].column = columns[j];
                    nodes[nodeCount].column.size++;
                    nodeCount++;
                }
            }

            // Link nodes horizontally
            for (int i = 0; i < nodeCount; i++) {
                nodes[i].right = nodes[(i + 1) % nodeCount];
                nodes[(i + 1) % nodeCount].left = nodes[i];
            }

            // Link nodes vertically
            for (int i = 0; i < nodeCount; i++) {
                Node up = nodes[i].column.up;
                nodes[i].up = up;
                nodes[i].down = nodes[i].column;
                up.down = nodes[i];
                nodes[i].column.up = nodes[i];
            }
        }
    }

    // Wrapper to build both matrices
    private void buildMatrix() {
        buildExactCoverMatrix();
        buildDLXMatrix();
    }

    // Cover a column in the DLX matrix
    private void cover(ColumnNode c) {
        c.right.left = c.left;
        c.left.right = c.right;
        for (Node i = c.down; i != c; i = i.down) {
            for (Node j = i.right; j != i; j = j.right) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.column.size--;
            }
        }
    }

    // Uncover a column in the DLX matrix
    private void uncover(ColumnNode c) {
        for (Node i = c.up; i != c; i = i.up) {
            for (Node j = i.left; j != i; j = j.left) {
                j.column.size++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
    }

    // Algorithm X implementation
    private boolean search() {
        if (root.right == root) {
            return true;
        }

        // Choose column with smallest size
        ColumnNode c = null;
        int minSize = Integer.MAX_VALUE;
        for (ColumnNode i = (ColumnNode) root.right; i != root; i = (ColumnNode) i.right) {
            if (i.size < minSize) {
                minSize = i.size;
                c = i;
            }
        }

        if (c == null || c.size == 0) return false;

        cover(c);
        for (Node r = c.down; r != c; r = r.down) {
            solution.add(r);
            for (Node j = r.right; j != r; j = j.right) {
                cover(j.column);
            }
            if (search()) return true;
            solution.remove(solution.size() - 1);
            for (Node j = r.left; j != r; j = j.left) {
                uncover(j.column);
            }
        }
        uncover(c);
        return false;
    }

    // Solve the Sudoku puzzle
    public int[][] solve() {
        buildMatrix();
        if (search()) {
            int[][] result = new int[9][9];
            for (Node node : solution) {
                result[node.row][node.col] = node.value;
            }
            return result;
        }
        return null;
    }

    // Print the grid
    public static void printGrid(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] == 0 ? "." : grid[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Sample Sudoku puzzle
        int[][] puzzle = {
                {5,3,0,0,7,0,0,0,0},
                {6,0,0,1,9,5,0,0,0},
                {0,9,8,0,0,0,0,6,0},
                {8,0,0,0,6,0,0,0,3},
                {4,0,0,8,0,3,0,0,1},
                {7,0,0,0,2,0,0,0,6},
                {0,6,0,0,0,0,2,8,0},
                {0,0,0,4,1,9,0,0,5},
                {0,0,0,0,8,0,0,7,9}
        };

        System.out.println("Sudoku Puzzle:");
        printGrid(puzzle);

        try {
            SudokuSolver solver = new SudokuSolver(puzzle);
            int[][] solution = solver.solve();

            if (solution != null) {
                System.out.println("\nSolution:");
                printGrid(solution);
            } else {
                System.out.println("\nNo solution exists.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}