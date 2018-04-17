
public class BTSolver {

    BinaryCSP problem;
    int nodesExplored;

    public BTSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
    }

    public void printSolution() {

        assert problem.isConsistent();

        StringBuffer result = new StringBuffer();
        result.append("Branches explored: " + nodesExplored + "\n");
        result.append("Number of Variables: " + problem.getNoVariables() + "\n");
        result.append("Number of Constraints: " + Integer.toString(problem.getNoConstraints()) + "\n");
        result.append("Solution: \n");
        for (int i = 0; i < problem.getVars().size(); i++) {
            result.append(problem.getVars().get(i).toString() + "\n");
        }
        System.out.println(result);
    }

    @Override
    public String toString() {

        return "BTSolver \n" + problem.toString();
    }

    public void assign(CSPVariable var, int value) {

        var.assign(value);
        nodesExplored++;
    }

    public void unassign(CSPVariable var) {

        var.unassign();
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
        for (int value : var.getDomain()) {
            assign(var, value);
            boolean consistent = true;
            for (int i = 0; i < depth; i++) {
                consistent = problem.isArcConsistent(problem.getVar(i), var);
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
