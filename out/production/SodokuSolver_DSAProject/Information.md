# Sudoku Solver - DSA Project

## Overview

This project implements a Sudoku solver using advanced algorithmic techniques to efficiently solve Sudoku puzzles. The solver combines **Backtracking**, **Minimum Remaining Values (MRV) Heuristic**, and **Forward Checking** to optimize the solving process.

---

## Techniques Used

### 1. Backtracking Algorithm
Backtracking is a general algorithmic approach for solving constraint satisfaction problems like Sudoku. It works by:

- Trying to place a value in a cell.
- Recursively attempting to solve the rest of the puzzle.
- Backtracking (undoing the last step) if the current placement leads to a dead end.

In the context of Sudoku:
- Fill empty cells one by one.
- Check if the current placement is valid.
- If invalid, undo the placement and try the next possible value.

---

### 2. Heuristics: Minimum Remaining Values (MRV)
MRV is a heuristic that improves the efficiency of backtracking by prioritizing cells with the fewest legal values remaining. This reduces the branching factor and increases the likelihood of finding a solution faster.

Steps in Sudoku:
- For each empty cell, calculate the number of valid numbers that can be placed.
- Prioritize cells with fewer options, as they are more constrained.

---

### 3. Forward Checking
Forward checking is an optimization technique that ensures the validity of future moves after placing a value in a cell.

Steps in Sudoku:
- When a number is placed in a cell, update the possible values for all related cells (same row, column, and 3x3 subgrid).
- If any related cell has no valid numbers left, undo the placement and try the next value.

---

## Combined Approach: Backtracking + MRV + Forward Checking

When combined, these techniques work as follows:
1. Use MRV to select the next cell to fill.
2. Place a value in the cell and update constraints using forward checking.
3. Recursively attempt to solve the puzzle.
4. If forward checking detects a conflict or the recursive call fails, backtrack and try the next value.

This combination significantly reduces the search space and improves the efficiency of solving Sudoku puzzles.

---

## Pseudocode

```python
function solveSudoku(board):
    if board is complete:
        return true

    cell = selectCellUsingMRV(board)  # Choose the cell with the fewest options
    for value in getValidValues(cell, board):
        placeValue(cell, value, board)
        if forwardCheck(board):  # Ensure no conflicts arise
            if solveSudoku(board):  # Recursive call
                return true
        removeValue(cell, value, board)  # Backtrack

    return false
```

This pseudocode outlines the combined approach, leveraging MRV, forward checking, and backtracking to solve Sudoku puzzles efficiently.