import java.util.LinkedList;
import java.util.Queue;

public class FCSolver {

    BinaryCSP problem;
    int nodesExplored;
    Queue<CSPVariable> varQueue;

    public FCSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
        varQueue = new LinkedList<CSPVariable>(problem.getVars());
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
        FCSolver solver = new FCSolver(reader.readBinaryCSP(CSPLocation));
        solver.solveCurrentProblem();
    }

    public void solveCurrentProblem() {

        if (!FC()) {
            System.out.println("No solution possible");
        }
    }

    // adapted from lecture slides.
    public boolean FC() {

        if (completeAssignment()) {
                printSolution()
                exit()
        }
            CSPVariable var = varQueue.poll();
            int val = selectValFromDomain(var);
            branchFCLeft(varList, var, val)
            branchFCRight(varList, var, val)
    }

    // just return an element, we can get fancy later
    private int selectValFromDomain(CSPVariable var) {

        return var.getDomain().iterator().next();
    }

    public boolean branchFCLeft(varList, CSPVarible var, int val) {
        var.assign(val);

        if( reviseFutureArcs(varList, var)) {
            ForwardChecking(varList- var);
        }
        undoPruning();
        var.unassign(var);
    }

    public boolean branchFCRight(varList, CSPVarible var, int val) {
        var.removeFromDomain(val);

        if(!var.getDomain().isEmpty()) {
            if(reviseFutureArcs(varList, var)) {
                FC(varList);
            }
        }
        undoPruning()
        restoreValue(var, val)
    }
}
