
public class BTSolver {

    BinaryCSP problem;
    int nodesExplored;

    public BTSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
    }

    public static void main(String[] args) {

        String CSPLocation = args[0];
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(CSPLocation);
        BTSolver solver = new BTSolver(reader.readBinaryCSP(CSPLocation));
        solver.solveCurrentProblem();
    }

    public void solveCurrentProblem() {

        if (!backtrack(0)) {
            System.out.println("No solution possible");
        }
        //		System.out.println(problem.toString());
    }

    // adapted from lecture slides. simple backtracking
    public boolean backtrack(int depth) {

        CSPVariable var = problem.getVar(depth);
        for (int value = var.getLowerBound(); value <= var.getUpperBound(); value++) {
            var.assign(value);
            if (problem.isArcConsistent()) {
                if (depth == problem.getNoVariables() - 1) {
                    System.out.println("Solution: ");
                    System.out.println(problem.toString());
                    return true;
                } else {
                    if (!backtrack(depth + 1)) {
                        var.unassign();
                    } else {
                        return true;
                    }
                }
            } else {
                var.unassign();
                continue;
            }
        }
        return false;
    }

}
