package org.example;

import java.util.*;
import java.util.Scanner;

import org.example.NodeDS.ColumnNode;
import org.example.NodeDS.Node;

public class SudokuSolver {


    private ColumnNode root;
    private List<Node> solution;
    private int[][] grid;
    private int[][] exactCoverMatrix;


    public SudokuSolver(int[][] grid) {
        // Validate input grid
        if (grid == null || grid.length != 9 || !isValidGrid(grid)) {
            throw new IllegalArgumentException("Invalid 9x9 Sudoku grid");
        }
        this.grid = grid;
        solution = new ArrayList<>();
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
        ///  step1.1 define constraints
        int n = 9;
        int constraints = 4; // Row-Column, Row-Number, Column-Number, Box-Number
        int rows = n * n * n; // Possible placements: 9x9 cells x 9 numbers
        int cols = n * n * constraints; // 4 constraints per cell

        /// 324 X 729 MATRIX
        exactCoverMatrix = new int[rows][cols];
        int rowIdx = 0;
        /// step1.2 : create the 729 row and 324 column matrix
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                for (int v = 1; v <= n; v++) {
                    ///  this is used to handle for the provided hint
                    ///  grid[r][c] != 0 meaning that the cell is already filled => dont need to generate possible row
                    if (grid[r][c] != 0 && grid[r][c] != v) continue;
                    /// step1.3 : create the row for the exact cover matrix
                    ///  using the supporter createExactCoverRow to create the row.
                    ///  each row will contains 4 number 1, one for each constraint
                    exactCoverMatrix[rowIdx] = createExactCoverRow(r, c, v, n, cols);
                    rowIdx++;
                }
            }
        }

        /// Trim the matrix to the actual number of rows used
        int[][] trimmedMatrix = new int[rowIdx][cols];
        for (int i = 0; i < rowIdx; i++) {
            System.arraycopy(exactCoverMatrix[i], 0, trimmedMatrix[i], 0, cols);
        }
        exactCoverMatrix = trimmedMatrix;
    }
    /// Create a single row for the exact cover matrix
    private int[] createExactCoverRow(int r, int c, int v, int n, int cols) {
        int[] row = new int[cols];
        int box = (r / 3) * 3 + c / 3;
        ///  THIS IS JUST A FORMULA TO CREATE THE ROW.

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
        /// step2.1 : create the DLX matrix
        int cols = exactCoverMatrix[0].length;
        int rows = exactCoverMatrix.length;

        /// step2.2 :  Initializing header node (column node)
        root = new ColumnNode();
        ColumnNode[] columns = new ColumnNode[cols];
        ColumnNode prev = root;
        for (int i = 0; i < cols; i++) {
            ///  CIRCULAR LINKED LIST , the last node points to the first node
            ///  header : column <-> column0 <-> column2 <-> ... <-> columnN <-> column0
            columns[i] = new ColumnNode();
            columns[i].right = root;
            columns[i].left = prev;
            prev.right = columns[i];
            prev = columns[i];
        }
        root.left = columns[cols - 1];
        columns[cols - 1].right = root;

        ///  step 2.3 : Build the DLX Matrix
        // Build the DLX matrix
        for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
            Node[] nodes = new Node[4];  /// each row have 4 1s => 4 nodes
            int nodeCount = 0;     /// count the number of nodes in the row (will reach 4 after processing the 1s).

            // Create nodes for each 1 in the exact cover matrix row
            for (int j = 0; j < cols; j++) {
                if (exactCoverMatrix[rowIdx][j] == 1) {
                    nodes[nodeCount] = new Node();
                    nodes[nodeCount].rowIdx = rowIdx; // Store row index
                    nodes[nodeCount].column = columns[j];
                    nodes[nodeCount].column.size++;
                    nodeCount++;
                }
            }

            ///  step 2.4 Link nodes horizontally
            ///nodes[0] ↔ nodes[1] ↔ nodes[2] ↔ nodes[3] ↔ nodes[0].
            for (int i = 0; i < nodeCount; i++) {
                nodes[i].right = nodes[(i + 1) % nodeCount];
                nodes[(i + 1) % nodeCount].left = nodes[i];
            }

            ///  step 2.5 Link nodes vertically
            /**
             * ColumnNode
             *   ↑↓
             * nodeN
             *   ↑↓
             * nodeM
             *   ↑↓
             *  ...
             *   ↑↓
             * nodeA
             *   ↑↓
             * ColumnNode
             */
            for (int i = 0; i < nodeCount; i++) {
                Node up = nodes[i].column.up;  /// get the up node of the column (or the last node added)
                nodes[i].up = up; /// link the up node to the current node ( the current node is last node added)
                nodes[i].down = nodes[i].column; /// link the down node to the column header( circular linked list)
                up.down = nodes[i]; /// link the up node to the current node (the current node is last node added)
                nodes[i].column.up = nodes[i]; /// link the column header to the current node (the current node is last node added)
            }
        }
    }

    // Wrapper to build both matrices
    private void buildMatrix() {
        buildExactCoverMatrix();
        buildDLXMatrix();
    }


    /// Step3 : Algorithm X implementation
    private boolean search() {
        /// step3.1 : check if the root is empty
        if (root.right == root) {
            return true;
        }

        /// step3.2 : find the column with the minimum size
        ColumnNode c = null;
        int minSize = Integer.MAX_VALUE;
        for (ColumnNode i = (ColumnNode) root.right; i != root; i = (ColumnNode) i.right) {
            if (i.size < minSize) {
                minSize = i.size;
                c = i;
            }
        }
        if (c == null || c.size == 0) return false;


        ///  Step3.3 : Cover the Selected Column
        /// Remove the selected column and all rows containing nodes in that column from the matrix.
        cover(c);

        /// Step 3.4: Try Each Row
        /// Test each row (placement) that satisfies the selected column, recursively searching for a solution.
        /// Step 3.4.1 : Iterate Over Rows
        for (Node r = c.down; r != c; r = r.down) {
            /// Step 3.4.2 : Add the Row to the Solution
            solution.add(r);
            /// Step 3.4.3 : Cover the Columns
            for (Node j = r.right; j != r; j = j.right) {
                cover(j.column);
            }
            /// Step 3.4.4 : Recursively Search
            if (search()) return true;
            /// Step 3.4.5 : Uncover the Columns
            solution.remove(solution.size() - 1);
            for (Node j = r.left; j != r; j = j.left) {
                uncover(j.column);
            }
        }
        /// Step 3.5 : Uncover the Selected Column
        uncover(c);
        /// Step 3.6 : Return False 
        return false;
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
    ///  final step : get the placement from the row
    // Reverse-engineer (r, c, v) from a row in exactCoverMatrix
    private int[] getPlacementFromRow(int rowIdx) {
        int n = 9;
        int[] row = exactCoverMatrix[rowIdx];

        // Find the row-column constraint (first 81 columns)
        int r = -1, c = -1;
        for (int j = 0; j < 81; j++) {
            if (row[j] == 1) {
                r = j / n;
                c = j % n;
                break;
            }
        }

        // Find the row-number constraint (columns 81 to 161) to get v
        int v = -1;
        for (int j = 81; j < 162; j++) {
            if (row[j] == 1) {
                v = (j - 81) % 9 + 1;
                break;
            }
        }

        return new int[]{r, c, v};
    }

    // Solve the Sudoku puzzle
    public int[][] solve() {
        buildMatrix();
        if (search()) {
            int[][] result = new int[9][9];
            for (Node node : solution) {
                int[] placement = getPlacementFromRow(node.rowIdx);
                result[placement[0]][placement[1]] = placement[2];
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
        Scanner scanner = new Scanner(System.in);
        int[][][] problems = DancingLink_AlgorithmsX.ProblemList.getAllProblems();

        while (true) {
            System.out.println("Choose a Sudoku problem to solve:");
            for (int i = 0; i < problems.length; i++) {
                System.out.println((i + 1) + ". Problem " + (i + 1));
            }
            System.out.println((problems.length + 1) + ". Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == problems.length + 1) {
                System.out.println("Exiting...");
                break;
            }

            if (choice < 1 || choice > problems.length) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int[][] puzzle = problems[choice - 1];
            System.out.println("Sudoku Puzzle:");
            printGrid(puzzle);

            try {
                SudokuSolver solver = new SudokuSolver(puzzle);

                // Start timing
                long startTime = System.nanoTime();

                int[][] solution = solver.solve();

                // End timing
                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                if (solution != null) {
                    System.out.println("\nSolution:");
                    printGrid(solution);
                    System.out.println("\nTime taken to solve: " + duration / 1_000_000 + " ms");
                } else {
                    System.out.println("\nNo solution exists.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}