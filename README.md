# Sudoku Solver - Algorithms and Analysis Project

This repository contains a Java-based Sudoku solver that implements multiple algorithms—Backtracking, Forward Checking, MRV Heuristics, and Dancing Links (Algorithm X). It features performance analysis tools, custom data structures, and automated testing across various puzzle difficulties. Developed as part of our DSA course, the project aims to evaluate algorithm efficiency and scalability on different Sudoku levels.

## Project Info

This project is a Java-based Sudoku Solver that efficiently solves 9x9 puzzles using multiple algorithms. It takes a board with empty cells (`0`s) and returns a completed solution while satisfying all Sudoku rules.

Implemented solvers include:
- **Backtracking**
- **Forward Checking**
- **MRV Heuristics**
- **Dancing Links (Algorithm X)**

The method `solve(int[][] puzzle)` returns a solved board or throws an exception if no solution is found within 2 minutes.

Beyond correctness, the project evaluates performance in terms of execution time and memory usage across different puzzle difficulties. It also uses custom data structures to enhance efficiency.

Developed as part of the **COSC2469 / COSC2722 / COSC2658 / COSC3120 - Algorithms and Analysis / Data Structures and Algorithms** course.

- Language: **Java**  
- Tested with: **JUnit**  
- Build status: **Compiles and runs in console**  
- License: **MIT**

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

## Contributors

This project was developed by:

- [@lucietran03](https://github.com/lucietran03) — Tran Dong Nghi  
- [@chanpark-dev](https://github.com/chanpark-dev) — Chan Yong Park  
- [@VanDel1](https://github.com/VanDel1) — Le Hung  
- [@rakeoohuynh](https://github.com/rakeoohuynh) — Huynh Nguyen Minh Nhu  
- [@nguyentran74](https://github.com/nguyentran74) — Tran Hoang Nguyen

> Originally created as a team project for an Algorithms and Data Structures course at RMIT.

## License

This project is licensed under the [MIT License](./LICENSE).
