package sudoku.solver.dancinglinks;

import java.util.ArrayList;
import java.util.Iterator;
import sudoku.model.SudokuConstant;

/**
 * AlgorithmX class implements Donald Knuth's Algorithm X using the Dancing
 * Links technique to solve exact cover problems, specifically tailored for solving Sudoku
 * puzzles.
 * <p>
 * This class provides methods to create an exact cover matrix for Sudoku
 * constraints, convert the matrix into a Dancing Links structure, and recursively search for
 * solutions.
 */
public class AlgorithmX {
    private ColumnNode root = null; // Root node of the Dancing Links structure
    private ArrayList<Node> solution = new ArrayList<>(); // Stores the solution path
    int N = SudokuConstant.N; // Size of the Sudoku grid (e.g., 9 for a 9x9 grid)
    int SIZE = SudokuConstant.SIZE; // Size of each sub-grid (e.g., 3 for a 3x3 sub-grid)

    /**
     * Runs the Algorithm X on the given Sudoku grid.
     *
     * @param defaultMatrix The initial Sudoku grid with pre-filled values.
     * @return True if the Sudoku puzzle is solved, false otherwise.
     */
    public boolean run(int[][] defaultMatrix) {
        byte[][] matrix = createMatrix(defaultMatrix); // Create exact cover matrix
        ColumnNode linkedMatrix = covertToDLL(matrix); // Convert to Doubly Linked List (DLL)

        boolean solved = search(0, defaultMatrix); // Start solving the puzzle
        if (solved) {
            mapSolvedToGrid(defaultMatrix); // Map the solved result back to the grid
        }

        return solved;
    }

    /**
     * Converts the exact cover matrix into a doubly linked list (Dancing Links
     * structure).
     *
     * @param matrix The exact cover matrix representing Sudoku constraints.
     * @return The root node of the Dancing Links structure.
     */
    private ColumnNode covertToDLL(byte[][] matrix) {
        root = new ColumnNode(); // Root node of the Dancing Links structure
        ColumnNode current = root;

        // Create column headers and link them circularly
        for (int col = 0; col < matrix[0].length; col++) {
            ColumnId colID = new ColumnId(); // Column metadata
            if (col < 3 * N * N) {
                int digit = (col / (3 * N)) + 1;
                colID.number = digit;

                int index = col - (digit - 1) * 3 * N;
                if (index < N) {
                    colID.constraint = 0; // Row constraint
                    colID.position = index;
                } else if (index < 2 * N) {
                    colID.constraint = 1; // Column constraint
                    colID.position = index - N;
                } else {
                    colID.constraint = 2; // Block constraint
                    colID.position = index - 2 * N;
                }
            } else {
                colID.constraint = 3; // Cell constraint
                colID.position = col - 3 * N * N;
            }

            current.right = new ColumnNode(); // Create a new column node
            current.right.left = current;
            current = (ColumnNode) current.right;
            current.info = colID;
            current.head = current;
        }
        current.right = root; // Make the column headers circular
        root.left = current;

        // Populate the matrix with nodes and link them circularly
        for (byte[] bytes : matrix) {
            current = (ColumnNode) root.right;
            Node lastCreatedElement = null;
            Node firstElement = null;
            for (byte aByte : bytes) {
                if (aByte == 1) { // If the matrix element is 1, create a node
                    Node colElement = current;
                    while (colElement.down != null) {
                        colElement = colElement.down;
                    }
                    colElement.down = new Node();
                    if (firstElement == null) {
                        firstElement = colElement.down;
                    }
                    colElement.down.up = colElement;
                    colElement.down.left = lastCreatedElement;
                    colElement.down.head = current;
                    if (lastCreatedElement != null) {
                        colElement.down.left.right = colElement.down;
                    }
                    lastCreatedElement = colElement.down;
                    current.size++;
                }
                current = (ColumnNode) current.right;
            }
            if (lastCreatedElement != null) { // Link the first and last elements in the row
                lastCreatedElement.right = firstElement;
                firstElement.left = lastCreatedElement;
            }
        }

        // Link the last column elements with their corresponding column headers
        current = (ColumnNode) root.right;
        for (int i = 0; i < matrix[0].length; i++) {
            Node colElement = current;
            while (colElement.down != null) {
                colElement = colElement.down;
            }
            colElement.down = current;
            current.up = colElement;
            current = (ColumnNode) current.right;
        }
        return root;
    }

    /**
     * Creates the exact cover matrix for the Sudoku grid based on its constraints.
     *
     * @param initialMatrix The initial Sudoku grid with pre-filled values.
     * @return A 2D byte array representing the exact cover matrix.
     */
    private byte[][] createMatrix(int[][] initialMatrix) {
        int[][] clues = null; // Stores the pre-filled clues in the Sudoku grid
        ArrayList<int[]> cluesList = new ArrayList<>();
        int counter = 0;

        // Extract pre-filled clues from the initial grid
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (initialMatrix[r][c] > 0) {
                    cluesList.add(new int[] { initialMatrix[r][c], r, c });
                    counter++;
                }
            }
        }
        clues = new int[counter][];
        for (int i = 0; i < counter; i++) {
            clues[i] = cluesList.get(i);
        }

        byte[][] matrix = new byte[N * N * N][4 * N * N]; // Exact cover matrix

        // Populate the matrix based on Sudoku constraints
        for (int d = 0; d < N; d++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    if (!filled(d, r, c, clues)) {
                        int rowIndex = c + (N * r) + (N * N * d);
                        int blockIndex = ((c / SIZE) + ((r / SIZE) * SIZE));

                        int colIndexRow = 3 * N * d + r;
                        int colIndexCol = 3 * N * d + N + c;
                        int colIndexBlock = 3 * N * d + 2 * N + blockIndex;
                        int colIndexSimple = 3 * N * N + (c + N * r);

                        matrix[rowIndex][colIndexRow] = 1;
                        matrix[rowIndex][colIndexCol] = 1;
                        matrix[rowIndex][colIndexBlock] = 1;
                        matrix[rowIndex][colIndexSimple] = 1;
                    }
                }
            }
        }

        return matrix;
    }

    /**
     * Checks if a digit is already placed in the Sudoku grid based on pre-filled clues.
     * This method ensures that constraints for row, column, and block are satisfied by
     * a specific digit placement.
     *
     * @param digit   The digit to check.
     * @param row     The row index in the Sudoku grid.
     * @param col     The column index in the Sudoku grid.
     * @param prefill The pre-filled clues in the Sudoku grid.
     * @return True if the cell is filled by a clue, false if not.
     */
    private boolean filled(int digit, int row, int col, int[][] prefill) {
        boolean filled = false;
        if (prefill != null) {
            for (int[] num : prefill) {
                int d = num[0] - 1;
                int r = num[1];
                int c = num[2];
                int blockStartIndexCol = (c / SIZE) * SIZE;
                int blockEndIndexCol = blockStartIndexCol + SIZE;
                int blockStartIndexRow = (r / SIZE) * SIZE;
                int blockEndIndexRow = blockStartIndexRow + SIZE;
                if (d != digit && row == r && col == c) {
                    filled = true;
                } else if ((d == digit) && (row == r || col == c) && !(row == r && col == c)) {
                    filled = true;
                } else if (d == digit && row > blockStartIndexRow && row < blockEndIndexRow && col > blockStartIndexCol && col < blockEndIndexCol && !(row == r)) {
                    filled = true;
                }
            }
        }
        return filled;
    }

    /**
     * Executes the recursive search method for solving the Sudoku puzzle. This
     * method performs a depth-first search by choosing a column, covering it, and
     * recursively exploring possible row combinations.
     *
     * @param k    The current level of recursion (depth of the search).
     * @param grid The current Sudoku grid being solved.
     * @return True if a solution is found, false otherwise.
     */
    private boolean search(int k, int[][] grid) {
        if (root.right == root) {
            return true;
        }

        ColumnNode c = choose(); // Select the next column to cover
        cover(c); // Cover the chosen column

        for (Node r = c.down; r != c; r = r.down) {
            solution.add(r);

            // Cover the columns of the current row
            for (Node j = r.right; j != r; j = j.right) {
                cover(j.head);
            }

            if (search(k + 1, grid)) {
                return true;
            }

            // If no solution is found, backtrack by uncovering columns
            r = solution.removeLast();
            c = r.head;

            for (Node j = r.left; j != r; j = j.left) {
                uncover(j.head);
            }
        }

        uncover(c); // Uncover the chosen column if no solution is found
        return false;
    }

    /**
     * Chooses the column with the smallest size to optimize the search by reducing
     * the number of possibilities.
     *
     * @return The column node with the smallest size.
     */
    private ColumnNode choose() {
        ColumnNode rightOfRoot = (ColumnNode) root.right;
        ColumnNode smallest = rightOfRoot;
        while (rightOfRoot.right != root) {
            rightOfRoot = (ColumnNode) rightOfRoot.right;
            if (rightOfRoot.size < smallest.size) {
                smallest = rightOfRoot;
            }
        }
        return smallest;
    }

    /**
     * Covers a column in the Dancing Links structure, removing it from the matrix
     * and removing all rows associated with the column.
     *
     * @param column The column node to be covered.
     */
    private void cover(Node column) {
        column.right.left = column.left;
        column.left.right = column.right;

        Node curRow = column.down;
        while (curRow != column) {
            Node curNode = curRow.right;
            while (curNode != curRow) {
                curNode.down.up = curNode.up;
                curNode.up.down = curNode.down;
                curNode.head.size--;
                curNode = curNode.right;
            }
            curRow = curRow.down;
        }
    }

    /**
     * Uncovers a column in the Dancing Links structure, restoring it and its
     * associated rows to the matrix.
     *
     * @param column The column node to be uncovered.
     */
    private void uncover(Node column) {
        Node curRow = column.up;
        while (curRow != column) {
            Node curNode = curRow.left;
            while (curNode != curRow) {
                curNode.head.size++;
                curNode.down.up = curNode;
                curNode.up.down = curNode;
                curNode = curNode.left;
            }
            curRow = curRow.up;
        }
        column.right.left = column;
        column.left.right = column;
    }

    /**
     * Maps the solution found by Algorithm X back to the Sudoku grid.
     *
     * @param grid The Sudoku grid to be updated with the solved values.
     */
    private void mapSolvedToGrid(int[][] grid) {
        int[] result = new int[N * N];
        for (Node node : solution) {
            int number = -1;
            int cellNo = -1;
            Node next = node;
            do {
                if (next.head.info.constraint == 0) {
                    number = next.head.info.number;
                } else if (next.head.info.constraint == 3) {
                    cellNo = next.head.info.position;
                }
                next = next.right;
            } while (node != next);
            result[cellNo] = number;
        }
        int resultCounter = 0;
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                grid[r][c] = result[resultCounter];
                resultCounter++;
            }
        }
    }
}
