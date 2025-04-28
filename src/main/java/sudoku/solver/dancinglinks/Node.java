package sudoku.solver.dancinglinks;

/**
 * The Node class represents a single node in the Dancing Links matrix.
 * Each node is part of a doubly linked list and is linked to its neighbors (left, right, up, down).
 * It also holds a reference to its associated ColumnNode.
 */
public class Node {
    /**
     * The node to the left in the doubly linked list.
     * Points to the previous node in the row or column.
     */
    Node left;

    /**
     * The node to the right in the doubly linked list.
     * Points to the next node in the row or column.
     */
    Node right;

    /**
     * The node above in the doubly linked list.
     * Points to the previous node in the column.
     */
    Node up;

    /**
     * The node below in the doubly linked list.
     * Points to the next node in the column.
     */
    Node down;

    /**
     * A reference to the ColumnNode that this node is part of.
     * It provides metadata such as the constraint type and number for the column.
     */
    ColumnNode head;
}
