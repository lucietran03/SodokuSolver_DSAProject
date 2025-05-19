// package sudoku.solver.dancinglinks;

// import java.util.ArrayList;

// import sudoku.model.SudokuConstant;

// /**
//  * AlgorithmX class implements Donald Knuth's Algorithm X using the Dancing
//  * Links technique to solve exact cover problems, specifically tailored for
//  * solving Sudoku
//  * puzzles.
//  * <p>
//  * This class provides methods to create an exact cover matrix for Sudoku
//  * constraints, convert the matrix into a Dancing Links structure, and
//  * recursively search for
//  * solutions.
//  */
// public class AlgorithmX {
//     private ColumnNode root = null; // Root node of the Dancing Links structure
//     private ArrayList<Node> solution = new ArrayList<>(); // Stores the solution path
//     int N = SudokuConstant.N; // Size of the Sudoku grid (e.g., 9 for a 9x9 grid)
//     int SIZE = SudokuConstant.SIZE; // Size of each sub-grid (e.g., 3 for a 3x3 sub-grid)

//     /**
//      * Runs the Algorithm X on the given Sudoku grid.
//      *
//      * @param defaultMatrix The initial Sudoku grid with pre-filled values.
//      * @return True if the Sudoku puzzle is solved, false otherwise.
//      * 
//      * Big-O (Worst Case): O(2^(N^2)) - Exponential due to the recursive search.
//      */
//     public boolean run(int[][] defaultMatrix) {
//         byte[][] matrix = createMatrix(defaultMatrix); // Create exact cover matrix
//         ColumnNode linkedMatrix = covertToDLL(matrix); // Convert to Doubly Linked List (DLL)

//         boolean solved = search(0, defaultMatrix); // Start solving the puzzle
//         if (solved) {
//             mapSolvedToGrid(defaultMatrix); // Map the solved result back to the grid
//         }

//         return solved;
//     }

//     /**
//      * Converts the exact cover matrix into a doubly linked list (Dancing Links
//      * structure).
//      *
//      * @param matrix The exact cover matrix representing Sudoku constraints.
//      * @return The root node of the Dancing Links structure.
//      * 
//      * Big-O (Worst Case): O(N^4) - Iterates through the matrix to create nodes.
//      */
//     private ColumnNode covertToDLL(byte[][] matrix) {
//         root = new ColumnNode(); // Root node of the Dancing Links structure
//         ColumnNode current = root;

//         // Create column headers and link them circularly
//         for (int col = 0; col < matrix[0].length; col++) {
//             ColumnId colID = new ColumnId(); // Column metadata
//             if (col < 3 * N * N) {
//                 int digit = (col / (3 * N)) + 1;
//                 colID.number = digit;

//                 int index = col - (digit - 1) * 3 * N;
//                 if (index < N) {
//                     colID.constraint = 0; // Row constraint
//                     colID.position = index;
//                 } else if (index < 2 * N) {
//                     colID.constraint = 1; // Column constraint
//                     colID.position = index - N;
//                 } else {
//                     colID.constraint = 2; // Block constraint
//                     colID.position = index - 2 * N;
//                 }
//             } else {
//                 colID.constraint = 3; // Cell constraint
//                 colID.position = col - 3 * N * N;
//             }

//             current.right = new ColumnNode(); // Create a new column node
//             current.right.left = current;
//             current = (ColumnNode) current.right;
//             current.info = colID;
//             current.head = current;
//         }
//         current.right = root; // Make the column headers circular
//         root.left = current;

//         // Populate the matrix with nodes and link them circularly
//         for (byte[] bytes : matrix) {
//             current = (ColumnNode) root.right;
//             Node lastCreatedElement = null;
//             Node firstElement = null;
//             for (byte aByte : bytes) {
//                 if (aByte == 1) { // If the matrix element is 1, create a node
//                     Node colElement = current;
//                     while (colElement.down != null) {
//                         colElement = colElement.down;
//                     }
//                     colElement.down = new Node();
//                     if (firstElement == null) {
//                         firstElement = colElement.down;
//                     }
//                     colElement.down.up = colElement;
//                     colElement.down.left = lastCreatedElement;
//                     colElement.down.head = current;
//                     if (lastCreatedElement != null) {
//                         colElement.down.left.right = colElement.down;
//                     }
//                     lastCreatedElement = colElement.down;
//                     current.size++;
//                 }
//                 current = (ColumnNode) current.right;
//             }
//             if (lastCreatedElement != null) { // Link the first and last elements in the row
//                 lastCreatedElement.right = firstElement;
//                 firstElement.left = lastCreatedElement;
//             }
//         }

//         // Link the last column elements with their corresponding column headers
//         current = (ColumnNode) root.right;
//         for (int i = 0; i < matrix[0].length; i++) {
//             Node colElement = current;
//             while (colElement.down != null) {
//                 colElement = colElement.down;
//             }
//             colElement.down = current;
//             current.up = colElement;
//             current = (ColumnNode) current.right;
//         }
//         return root;
//     }

//     /**
//      * Creates the exact cover matrix for the Sudoku grid based on its constraints.
//      *
//      * @param initialMatrix The initial Sudoku grid with pre-filled values.
//      * @return A 2D byte array representing the exact cover matrix.
//      * 
//      * Big-O (Worst Case): O(N^4) - Iterates through all cells and constraints.
//      */
//     private byte[][] createMatrix(int[][] initialMatrix) {
//         int[][] clues = null; // Stores the pre-filled clues in the Sudoku grid
//         ArrayList<int[]> cluesList = new ArrayList<>();
//         int counter = 0;

//         // Extract pre-filled clues from the initial grid
//         for (int r = 0; r < N; r++) {
//             for (int c = 0; c < N; c++) {
//                 if (initialMatrix[r][c] > 0) {
//                     cluesList.add(new int[] { initialMatrix[r][c], r, c });
//                     counter++;
//                 }
//             }
//         }
//         clues = new int[counter][];
//         for (int i = 0; i < counter; i++) {
//             clues[i] = cluesList.get(i);
//         }

//         byte[][] matrix = new byte[N * N * N][4 * N * N]; // Exact cover matrix

//         // Populate the matrix based on Sudoku constraints
//         for (int d = 0; d < N; d++) {
//             for (int r = 0; r < N; r++) {
//                 for (int c = 0; c < N; c++) {
//                     if (!filled(d, r, c, clues)) {
//                         int rowIndex = c + (N * r) + (N * N * d);
//                         int blockIndex = ((c / SIZE) + ((r / SIZE) * SIZE));

//                         int colIndexRow = 3 * N * d + r;
//                         int colIndexCol = 3 * N * d + N + c;
//                         int colIndexBlock = 3 * N * d + 2 * N + blockIndex;
//                         int colIndexSimple = 3 * N * N + (c + N * r);

//                         matrix[rowIndex][colIndexRow] = 1;
//                         matrix[rowIndex][colIndexCol] = 1;
//                         matrix[rowIndex][colIndexBlock] = 1;
//                         matrix[rowIndex][colIndexSimple] = 1;
//                     }
//                 }
//             }
//         }

//         return matrix;
//     }

//     /**
//      * Checks if a digit is already placed in the Sudoku grid based on pre-filled
//      * clues.
//      * This method ensures that constraints for row, column, and block are satisfied
//      * by
//      * a specific digit placement.
//      *
//      * @param digit   The digit to check.
//      * @param row     The row index in the Sudoku grid.
//      * @param col     The column index in the Sudoku grid.
//      * @param prefill The pre-filled clues in the Sudoku grid.
//      * @return True if the cell is filled by a clue, false if not.
//      * 
//      * Big-O (Worst Case): O(N^2) - Iterates through all pre-filled clues.
//      */
//     private boolean filled(int digit, int row, int col, int[][] prefill) {
//         boolean filled = false;
//         if (prefill != null) {
//             for (int[] num : prefill) {
//                 int d = num[0] - 1;
//                 int r = num[1];
//                 int c = num[2];
//                 int blockStartIndexCol = (c / SIZE) * SIZE;
//                 int blockEndIndexCol = blockStartIndexCol + SIZE;
//                 int blockStartIndexRow = (r / SIZE) * SIZE;
//                 int blockEndIndexRow = blockStartIndexRow + SIZE;
//                 if (d != digit && row == r && col == c) {
//                     filled = true;
//                 } else if ((d == digit) && (row == r || col == c) && !(row == r && col == c)) {
//                     filled = true;
//                 } else if (d == digit && row > blockStartIndexRow && row < blockEndIndexRow && col > blockStartIndexCol
//                         && col < blockEndIndexCol && !(row == r)) {
//                     filled = true;
//                 }
//             }
//         }
//         return filled;
//     }

//     /**
//      * Executes the recursive search method for solving the Sudoku puzzle. This
//      * method performs a depth-first search by choosing a column, covering it, and
//      * recursively exploring possible row combinations.
//      *
//      * @param k    The current level of recursion (depth of the search).
//      * @param grid The current Sudoku grid being solved.
//      * @return True if a solution is found, false otherwise.
//      * 
//      * Big-O (Worst Case): O(2^(N^2)) - Exponential due to the recursive nature of the search.
//      */
//     private boolean search(int k, int[][] grid) {
//         if (root.right == root) {
//             return true;
//         }

//         ColumnNode c = choose(); // Select the next column to cover
//         cover(c); // Cover the chosen column

//         for (Node r = c.down; r != c; r = r.down) {
//             solution.add(r);

//             // Cover the columns of the current row
//             for (Node j = r.right; j != r; j = j.right) {
//                 cover(j.head);
//             }

//             if (search(k + 1, grid)) {
//                 return true;
//             }

//             // If no solution is found, backtrack by uncovering columns
//             r = solution.removeLast();
//             c = r.head;

//             for (Node j = r.left; j != r; j = j.left) {
//                 uncover(j.head);
//             }
//         }

//         uncover(c); // Uncover the chosen column if no solution is found
//         return false;
//     }

//     /**
//      * Chooses the column with the smallest size to optimize the search by reducing
//      * the number of possibilities.
//      *
//      * @return The column node with the smallest size.
//      * 
//      * Big-O (Worst Case): O(N^2) - Iterates through all columns to find the smallest.
//      */
//     private ColumnNode choose() {
//         ColumnNode rightOfRoot = (ColumnNode) root.right;
//         ColumnNode smallest = rightOfRoot;
//         while (rightOfRoot.right != root) {
//             rightOfRoot = (ColumnNode) rightOfRoot.right;
//             if (rightOfRoot.size < smallest.size) {
//                 smallest = rightOfRoot;
//             }
//         }
//         return smallest;
//     }

//     /**
//      * Covers a column in the Dancing Links structure, removing it from the matrix
//      * and removing all rows associated with the column.
//      *
//      * @param column The column node to be covered.
//      * 
//      * Big-O (Worst Case): O(N^2) - Iterates through all rows and nodes in the column.
//      */
//     private void cover(Node column) {
//         column.right.left = column.left;
//         column.left.right = column.right;

//         Node curRow = column.down;
//         while (curRow != column) {
//             Node curNode = curRow.right;
//             while (curNode != curRow) {
//                 curNode.down.up = curNode.up;
//                 curNode.up.down = curNode.down;
//                 curNode.head.size--;
//                 curNode = curNode.right;
//             }
//             curRow = curRow.down;
//         }
//     }

//     /**
//      * Uncovers a column in the Dancing Links structure, restoring it and its
//      * associated rows to the matrix.
//      *
//      * @param column The column node to be uncovered.
//      * 
//      * Big-O (Worst Case): O(N^2) - Iterates through all rows and nodes in the column.
//      */
//     private void uncover(Node column) {
//         Node curRow = column.up;
//         while (curRow != column) {
//             Node curNode = curRow.left;
//             while (curNode != curRow) {
//                 curNode.head.size++;
//                 curNode.down.up = curNode;
//                 curNode.up.down = curNode;
//                 curNode = curNode.left;
//             }
//             curRow = curRow.up;
//         }
//         column.right.left = column;
//         column.left.right = column;
//     }

//     /**
//      * Maps the solution found by Algorithm X back to the Sudoku grid.
//      *
//      * @param grid The Sudoku grid to be updated with the solved values.
//      * 
//      * Big-O (Worst Case): O(N^2) - Iterates through all cells in the grid.
//      */
//     private void mapSolvedToGrid(int[][] grid) {
//         int[] result = new int[N * N];
//         for (Node node : solution) {
//             int number = -1;
//             int cellNo = -1;
//             Node next = node;
//             do {
//                 if (next.head.info.constraint == 0) {
//                     number = next.head.info.number;
//                 } else if (next.head.info.constraint == 3) {
//                     cellNo = next.head.info.position;
//                 }
//                 next = next.right;
//             } while (node != next);
//             result[cellNo] = number;
//         }
//         int resultCounter = 0;
//         for (int r = 0; r < N; r++) {
//             for (int c = 0; c < N; c++) {
//                 grid[r][c] = result[resultCounter];
//                 resultCounter++;
//             }
//         }
//     }
// }

package sudoku.solver.dancinglinks;

import java.util.*;

public class AlgorithmX {


    private ColumnNode root;
    private List<Node> solution;
    private int[][] grid;
    private int[][] exactCoverMatrix;


    public AlgorithmX(int[][] grid) {
        // Validate input grid
        if (!isValidGrid(grid)) {
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
}