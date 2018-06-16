/*
 * @author 170008773 The Class BTSolver.
 */
public class BTSolver {

    /** The problem to solve. */
    BinaryCSP problem;

    /** The nodes number of explored. */
    long nodesExplored;

    /**
     * Instantiates a new Backtrack solver.
     *
     * @param problem
     *            the problem
     */
    public BTSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
    }

    /**
     * Prints the solution.
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "BTSolver \n" + problem.toString();
    }

    /**
     * Assign a value to a variable.
     *
     * @param var
     *            the variable to be assigned
     * @param value
     *            the value to assing the variable
     */
    public void assign(CSPVariable var, int value) {

        var.assign(value);
        nodesExplored++;
    }

    /**
     * Unassign a variable.
     *
     * @param var
     *            the variable to be unassigned
     */
    public void unassign(CSPVariable var) {

        var.unassign();
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {

        String CSPLocation = args[0];
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(CSPLocation);
        BTSolver solver = new BTSolver(reader.readBinaryCSP(CSPLocation));
        solver.solveCurrentProblem();
    }

    /**
     * Solve current problem.
     */
    public void solveCurrentProblem() {

        if (!backtrack(0)) {
            System.out.println("No solution possible");
        } else {
            printSolution();
        }
    }

    /**
     * Recursive backtracking, adapted from lecture slides.
     *
     * @param depth
     *            the depth of the recursion
     * @return true, if a solution was found
     */
    public boolean backtrack(int depth) {

        // get variable
        CSPVariable var = problem.getVar(depth);

        // try all values in the domain
        for (int value : var.getDomain()) {
            assign(var, value);
            boolean consistent = true;

            // check if all constraints are still happy
            for (int i = 0; i < depth; i++) {
                consistent = problem.isArcConsistent(problem.getVar(i), var);
                if (!consistent) {
                    break;
                }
            }
            if (consistent) {
                // are we done?
                if (depth == problem.getNoVariables() - 1) {
                    return true;
                } else {
                    // recurse
                    if (!backtrack(depth + 1)) {
                        unassign(var);
                    } else {
                        return true;
                    }
                }
            } else {
                // backtrack, we tried everything
                unassign(var);
                continue;
            }
        }
        return false;
    }

}
