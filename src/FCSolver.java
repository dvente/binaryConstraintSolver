import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class FCSolver {

    BinaryCSP problem;
    int nodesExplored;
    Queue<CSPVariable> varQueue;
    Stack<Set<Map<CSPVariable, Set<Integer>>>> pruningStack;

    public FCSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
        varQueue = new LinkedList<CSPVariable>(problem.getVars());
        pruningStack = new Stack<Set<Map<CSPVariable, Set<Integer>>>>();
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

        if (!forwardChecking()) {
            System.out.println("No solution possible");
        }
    }

    // adapted from lecture slides.
    public boolean forwardChecking() {

        if (problem.completeAssignment()) {
            printSolution();
            return true;

        }
        CSPVariable var = varQueue.poll();
        int val = selectValFromDomain(var);
        return branchFCLeft(var, val) || branchFCRight(var, val);
    }

    // just return an element, we can get fancy later
    private int selectValFromDomain(CSPVariable var) {

        return var.getDomain().iterator().next();
    }

    public boolean branchFCLeft(CSPVariable var, int val) {

        var.assign(val);

        if (reviseFutureArcs(var)) {
            varQueue.poll();
            if (forwardChecking()) {
                return true;
            }
        }
        undoPruning();
        unassign(var);
        return false;

    }

    private void undoPruning() {

        // TODO Auto-generated method stub

    }

    private boolean reviseFutureArcs(CSPVariable var) {

        // TODO Auto-generated method stub
        return false;
    }

    public boolean branchFCRight(CSPVariable var, int val) {

        var.removeFromDomain(val);

        if (!var.getDomain().isEmpty()) {
            if (reviseFutureArcs(var)) {
                if (forwardChecking()) {
                    return true;
                }
            }
        }
        undoPruning();
        restoreValue(var, val);
        return false;
    }

    private void restoreValue(CSPVariable var, int val) {

        var.addToDomain(val);

    }
}
