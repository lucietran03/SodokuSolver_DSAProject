# Sudoku Solver - Algorithms and Analysis Project

This repository contains a Java-based Sudoku solver that implements multiple algorithms—Backtracking, Forward Checking, MRV Heuristics, and Dancing Links (Algorithm X). It features performance analysis tools, custom data structures, and automated testing across various puzzle difficulties. Developed as part of our DSA course, the project aims to evaluate algorithm efficiency and scalability on different Sudoku levels.

## Project Info

This project involves the design and implementation of a Java-based Sudoku Solver that can efficiently solve 9x9 Sudoku puzzles using advanced algorithmic strategies. The solver takes a partially completed Sudoku board as input (represented as a 2D array of integers) and fills in the empty cells (marked as 0) to produce a valid solution that adheres to all Sudoku constraints.

Our implementation includes multiple solving approaches—Backtracking, Forward Checking, MRV Heuristics, and Dancing Links (Algorithm X)—to explore and compare their efficiency and effectiveness across various difficulty levels. The core method, `solve(int[][] puzzle)`, returns a fully solved 9x9 grid or raises an exception if no solution is found within 2 minutes.

In addition to correctness, the project also emphasizes performance analysis, including memory usage and execution time, allowing a deeper understanding of which algorithms perform best under different conditions. Custom data structures are used where appropriate to optimize performance.

This project was developed as part of the **COSC2469/ COSC2722/ COSC2658/ COSC3120 - Algorithms and Analysis/Data Structures and Algorithms** course and demonstrates applied algorithm design, problem-solving skills, and performance evaluation techniques.

-   Language: **Java**
-   Tested with: **JUnit**
-   Build status: **Compiles and runs successfully in console**
-   License: **MIT**

## Table of Contents

-   [Project Links](#project_links)
-   [Usage](#usage)
-   [How to Run the Tests](#how-to-run-the-tests)
-   [Algorithms Implemented](#algorithms-implemented)
-   [File Structure](#file-structure)
-   [Contributors](#contributing)
-   [License](#license)

## Project Links

-   [Project Presentation](https://rmiteduau-my.sharepoint.com/:v:/g/personal/s3914633_rmit_edu_vn/EaM4ycE-zEFGkZ-zn4lLmZ0B-HAJvi5umk5ZqzM1bvrJlQ?e=s2q4JB)
-   [GitHub Repository](https://github.com/lucietran03/SudokuSolver_DSAProject.git)

## Usage

1.  Clone the repository:
    ```bash
    git clone https://github.com/lucietran03/SudokuSolver_DSAProject.git
    ```
2.  Navigate to the project directory:
    ```bash
    cd SudokuSolver_DSAProject
    ```
3.  Run the solver:
    ```bash
    javac -d out src/main/java/sudoku/Main.java
    java -cp out sudoku.Main
    ```

## How to Run the Tests

There are automated tests available to verify the correctness of all solvers on predefined Sudoku puzzles.

### Steps:

1. Make sure the file `input.txt` (containing test cases) is located at:

    ```bash
    src/main/java/performance/sudokuIO/input.txt
    ```

2. Navigate to the `test/java/sudoku/solver` folder.
    ```bash
    cd test/java/sudoku/solver
    ```
3. Open the file `SudokuTest.java` in your IDE.

4. Click **Run** on the `testAllSolversFromFile()` method or run the full class.

-   This will test **all solvers** (Backtracking, Forward Checking, MRV, Dancing Links) on multiple Sudoku problems (easy to hard).
-   Errors or failed results will be printed in the console with clear messages.

5. If you want to test a specific solver independently, you can run that solver’s class directly in the same test folder or main folder.

## Algorithms Implemented

-   **Backtracking**: Classic recursive solution trying all possibilities.
-   **Forward Checking**: An enhancement of backtracking that eliminates values from domains early.
-   **Minimum Remaining Values (MRV)**: Heuristic to select variables with the fewest legal values.
-   **Dancing Links (DLX)**: Efficient algorithm for exact cover problems using a doubly linked list, based on Donald Knuth's Algorithm X.

## File Structure

```
src
├───main
│   └───java
│       ├───performance
│       │   ├───TestInput.java
│       │   ├───WriteFile.java
│       │   └───sudokuIO
│       │       ├───input.txt
│       │       ├───csvoutput.csv
│       │       └───txtoutput.txt
│       └───sudoku
│           ├───Main.java
│           ├───common
│           │   ├───InputValidator.java
│           │   └───Utils.java
│           ├───model
│           │   ├───Sudoku.java
│           │   ├───SudokuConstant.java
│           │   └───SudokuManager.java
│           ├───Mycollection
│           │   ├───MyLinkedList.java
│           │   ├───MyMap.java
│           │   └───MySet.java
│           └───solver
│               ├───BasicBacktracking.java
│               ├───DancingLinksX.java
│               ├───ForwardChecking.java
│               ├───MRVBacktracking.java
│               └───dancinglinks
│                   ├───AlgorithmX.java
│                   ├───ColumnNode.java
│                   └───Node.java
├───performance-analysis
│   ├───Visualization.ipynb
│   └───visualization_outputs
|       ├───average_memory_taken_algorithms_part1.png
│       ├───average_memory_taken_algorithms_part2.png
│       ├───average_time_taken_algorithms.png
│       ├───correctness_comparison.png
│       ├───correlation_matrix_of_time_and_memory.png
│       └───time_distribution_per_algorithm.png
└───test
    └───java
        └───sudoku
            └───solver
```

## Contributing

The contribution scores for the team members are as follows:

| Member                | S_ID     | Contribution Score |
| --------------------- | -------- | ------------------ |
| Chan Yong Park        | s4021263 | 5                  |
| Le Hung               | s4061665 | 5                  |
| Tran Dong Nghi        | s3914633 | 5                  |
| Huynh Nguyen Minh Nhu | s4104540 | 5                  |
| Tran Hoang Nguyen     | s4054071 | 5                  |

We agree with the contribution score listed above.

## License

This project is licensed under the [MIT License](./LICENSE).
