package sudoku.solver;

import sudoku.solver.dancinglinks.AlgorithmX;

public class DancingLinks extends Solver {
    public DancingLinks() {
        super("Dancing Links");
    }

    @Override
    public boolean solve() {
        AlgorithmX solver = new AlgorithmX();
        return solver.run(sudoku.getGrid());
    }
}
