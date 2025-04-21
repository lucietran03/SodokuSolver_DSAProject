# Sudoku Solver - DSA Project

## Overview

This project implements a Sudoku solver using a basic backtracking approach. The solver systematically explores all possible placements for numbers in the Sudoku grid, ensuring correctness by adhering to Sudoku rules.

---

## Technique Used: Backtracking Algorithm

The backtracking algorithm is a recursive method for solving Sudoku puzzles. It works by:

- Identifying the next empty cell in the grid.
- Trying all possible values (1-9) for the cell.
- Recursively solving the rest of the puzzle.
- Backtracking (undoing the last step) if the current placement leads to a dead end.

In the context of Sudoku:
- Fill empty cells one by one.
- Check if the current placement is valid.
- If invalid, undo the placement and try the next possible value.

---

## Implementation Details

### Key Steps in the Algorithm:
1. **Find Empty Cell**: Locate the next empty cell in the grid.
2. **Try Values**: Test all possible values (1-9) for the empty cell.
3. **Validate Placement**: Ensure the value does not violate Sudoku rules (row, column, and 3x3 subgrid constraints).
4. **Recursive Call**: Attempt to solve the rest of the puzzle.
5. **Backtrack**: If no valid value works, undo the last placement and try the next option.

---

## Pseudocode

```java
public boolean solveSudoku(int[][] board) {
    int[] emptyCell = findEmptyCell(board);
    if (emptyCell == null) {
        return true; // Puzzle solved
    }

    int row = emptyCell[0];
    int col = emptyCell[1];

    for (int num = 1; num <= 9; num++) {
        if (isValid(board, row, col, num)) {
            board[row][col] = num;

            if (solveSudoku(board)) {
                return true;
            }

            board[row][col] = 0; // Backtrack
        }
    }

    return false; // Trigger backtracking
}
```

### Helper Functions:
- `findEmptyCell(board)`: Finds the next empty cell in the grid.
- `isValid(board, row, col, num)`: Checks if placing `num` in the specified cell is valid according to Sudoku rules.

---

## Advantages and Limitations

### Advantages:
- Straightforward and easy to implement.
- Guarantees a solution if one exists.

### Limitations:
- Computationally expensive for larger grids or puzzles with fewer clues.
- Inefficient compared to heuristic-based or optimized approaches.

---

This backtracking approach provides a foundational method for solving Sudoku puzzles and can be enhanced with additional optimizations or heuristics.