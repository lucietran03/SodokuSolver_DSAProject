package org.example.NodeDS;



// DLX Node class for the dancing links structure
public class Node {
    public Node left, right, up, down;
    public org.example.NodeDS.ColumnNode column;
    public int rowIdx; // Store the exactCoverMatrix row index

    public Node() {
        left = right = up = down = this;
    }
}