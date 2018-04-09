
public class BTSolver {

    BinaryCSP problem;
    int nodesExplored;

    public BTSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
    }

    public void printSolution() {

        StringBuffer result = new StringBuffer();
        result.append("Nodes explored: " + nodesExplored + "\n");
        result.append("Solution: \n");
        for (int i = 0; i < problem.getVars().size(); i++) {
            result.append(problem.getVars().get(i).toString());
        }
        System.out.println(result);
    }

    public void assign(CSPVariable var, int value) {

        assert !var.isAssigned();
        assert var.getDomain().contains(value);
        var.setValue(value);
        nodesExplored++;
        var.setAssigned(true);
    }

    public void unassign(CSPVariable var) {

        var.setAssigned(false);
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
        } else {
            printSolution();
        }
    }

    // adapted from lecture slides. simple backtracking
    public boolean backtrack(int depth) {

        CSPVariable var = problem.getVar(depth);
        for (int value = var.getLowerBound(); value <= var.getUpperBound(); value++) {
            assign(var, value);
            boolean consistent = true;
            for (int i = 0; i < depth; i++) {
                consistent = problem.isArcConsistent(i, depth);
                if (!consistent) {
                    break;
                }
            }
            if (consistent) {
                if (depth == problem.getNoVariables() - 1) {
                    return true;
                } else {
                    if (!backtrack(depth + 1)) {
                        unassign(var);
                    } else {
                        return true;
                    }
                }
            } else {
                unassign(var);
                continue;
            }
        }
        return false;
    }

}
