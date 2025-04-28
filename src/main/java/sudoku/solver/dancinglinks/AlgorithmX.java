package sudoku.solver.dancinglinks;
// This class implements the Algorithm X using Dancing Links (DLX) to solve the Sudoku puzzle.
// Algorithm X is a recursive, nondeterministic, depth-first, backtracking algorithm for solving the exact cover problem.
// The implementation uses a doubly linked list to represent the sparse matrix and solve the Sudoku constraints.

import java.util.ArrayList;
import java.util.Iterator;
import sudoku.model.SudokuConstant;

/**
 * AlgorithmX class implements Donald Knuth's Algorithm X using the Dancing
 * Links technique
 * to solve exact cover problems, specifically tailored for solving Sudoku
 * puzzles.
 *
 * This class provides methods to create an exact cover matrix for Sudoku
 * constraints,
 * convert the matrix into a Dancing Links structure, and recursively search for
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
     */
    public boolean run(int[][] defaultMatrix) {
        byte[][] matrix = createMatrix(defaultMatrix); // Tạo exact cover matrix
        ColumnNode linkedMatrix = covertToDLL(matrix); // Convert sang DLL

        boolean solved = search(0, defaultMatrix); // Bắt đầu giải
        if (solved) {
            mapSolvedToGrid(defaultMatrix); // Ánh xạ kết quả ra lại grid
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
        for (int row = 0; row < matrix.length; row++) {
            current = (ColumnNode) root.right;
            Node lastCreatedElement = null;
            Node firstElement = null;
            for (int col = 0; col < matrix[row].length; col++) {
                if (matrix[row][col] == 1) { // If the matrix element is 1, create a node
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
            clues[i] = (int[]) cluesList.get(i);
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
     * Checks if a cell is already filled based on the pre-filled clues.
     *
     * @param digit   The digit to check.
     * @param row     The row index of the cell.
     * @param col     The column index of the cell.
     * @param prefill The pre-filled clues in the Sudoku grid.
     * @return True if the cell is already filled, false otherwise.
     */
    private boolean filled(int digit, int row, int col, int[][] prefill) {
        boolean filled = false;
        if (prefill != null) {
            for (int i = 0; i < prefill.length; i++) {
                int d = prefill[i][0] - 1;
                int r = prefill[i][1];
                int c = prefill[i][2];
                int blockStartIndexCol = (c / SIZE) * SIZE;
                int blockEndIndexCol = blockStartIndexCol + SIZE;
                int blockStartIndexRow = (r / SIZE) * SIZE;
                int blockEndIndexRow = blockStartIndexRow + SIZE;
                if (d != digit && row == r && col == c) {
                    filled = true;
                } else if ((d == digit) && (row == r || col == c) && !(row == r && col == c)) {
                    filled = true;
                } else if ((d == digit) && (row > blockStartIndexRow) && (row < blockEndIndexRow)
                        && (col > blockStartIndexCol) && (col < blockEndIndexCol) && !(row == r && col == c)) {
                    filled = true;
                }
            }
        }
        return filled;
    }

    /**
     * Recursive search method to solve the Sudoku using Algorithm X.
     *
     * @param k    The depth of the search tree.
     * @param grid The Sudoku grid to be solved.
     */
    private boolean search(int k, int[][] grid) {
        if (root.right == root) {
            return true;
        }

        ColumnNode c = choose();
        cover(c);

        for (Node r = c.down; r != c; r = r.down) {
            solution.add(r);

            for (Node j = r.right; j != r; j = j.right) {
                cover(j.head);
            }

            if (search(k + 1, grid)) {
                return true;
            }

            r = solution.remove(solution.size() - 1);
            c = r.head;

            for (Node j = r.left; j != r; j = j.left) {
                uncover(j.head);
            }
        }

        uncover(c);
        return false;
    }


    /**
     * Chooses the column with the smallest size (heuristic for optimization).
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
     * Covers a column in the Dancing Links structure, removing it and its
     * associated rows
     * from the matrix.
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
     * associated rows
     * to the matrix.
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
     * Maps the solved solution back to the Sudoku grid.
     *
     * @param grid The Sudoku grid to be updated with the solved solution.
     */
    private void mapSolvedToGrid(int[][] grid) {
        int[] result = new int[N * N];
        for (Iterator<Node> it = solution.iterator(); it.hasNext();) {
            int number = -1;
            int cellNo = -1;
            Node element = (Node) it.next();
            Node next = element;
            do {
                if (next.head.info.constraint == 0) {
                    number = next.head.info.number;
                } else if (next.head.info.constraint == 3) {
                    cellNo = next.head.info.position;
                }
                next = next.right;
            } while (element != next);
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

