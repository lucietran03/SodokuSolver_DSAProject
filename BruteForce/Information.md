# Sudoku Solver - DSA Project

## Overview

This project implements a Sudoku solver using a brute force approach. The solver systematically tries all possible combinations to solve Sudoku puzzles, ensuring correctness through exhaustive search.

---

## Technique Used: Brute Force Algorithm

The brute force algorithm is a straightforward method for solving Sudoku puzzles. It works by:

- Iterating through each empty cell in the grid.
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
1. **Find Empty Cell**: Identify the next empty cell in the grid.
2. **Try Values**: Test all possible values (1-9) for the empty cell.
3. **Validate Placement**: Ensure the value does not violate Sudoku rules (row, column, and 3x3 subgrid constraints).
4. **Recursive Call**: Proceed to solve the rest of the puzzle.
5. **Backtrack**: If no valid value works, undo the last placement and try the next option.

---

## Pseudocode

```java
boolean solveSudoku(int[][] board) {
    int[] emptyCell = findEmptyCell(board);
    if (emptyCell == null) {
        return true; // Puzzle solved
    }

    int row = emptyCell[0];
    int col = emptyCell[1];

    for (int num = 1; num <= 9; num++) {
        if (isValidPlacement(board, row, col, num)) {
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
- `findEmptyCell(board)`: Locates the next empty cell in the grid.
- `isValidPlacement(board, row, col, num)`: Checks if placing `num` in the specified cell is valid.

---

## Advantages and Limitations

### Advantages:
- Simple and easy to implement.
- Guarantees a solution if one exists.

### Limitations:
- Computationally expensive for larger grids or puzzles with fewer clues.
- Inefficient compared to heuristic-based or optimized approaches.

---

This brute force approach serves as a foundational method for solving Sudoku puzzles and can be extended or optimized with additional techniques.