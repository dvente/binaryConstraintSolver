import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class FCSolver {

    BinaryCSP problem;
    int nodesExplored;
    List<CSPVariable> varList;
    Stack<Map<CSPVariable, Set<Integer>>> pruningStack;

    public FCSolver(BinaryCSP problem) {

        super();
        this.problem = problem;
        varList = new LinkedList<CSPVariable>(problem.getVars());
        pruningStack = new Stack<Map<CSPVariable, Set<Integer>>>();
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
        CSPVariable var = varList.iterator().next();
        int val = selectValFromDomain(var);
        return branchFCLeft(var, val) || branchFCRight(var, val);
    }

    // just return an element, we can get fancy later
    private int selectValFromDomain(CSPVariable var) {

        return var.getDomain().iterator().next();
    }

    public boolean branchFCLeft(CSPVariable var, int val) {

        assign(var, val);

        if (reviseFutureArcs(var)) {
            varList.remove(var);
            if (forwardChecking()) {
                return true;
            }
            varList.add(var);
        }
        undoPruning();
        unassign(var);
        return false;

    }

    private void undoPruning() {

        Map<CSPVariable, Set<Integer>> pruned = pruningStack.pop();
        for (CSPVariable future : pruned.keySet()) {
            for (int val : pruned.get(future)) {
                future.addToDomain(val);
            }
        }
    }

    private boolean reviseFutureArcs(CSPVariable currentVar) {

        Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
        boolean consistent = true;
        Collection<BinaryConstraint> arcsToRevise = problem.getContraints(currentVar);
        for (BinaryConstraint constr : arcsToRevise) {
            CSPVariable futureVar = constr.getOtherVar(currentVar);
            Set<Integer> deleted = revise(constr, currentVar);
            consistent = !futureVar.getDomain().isEmpty();
            if (!consistent) {
                pruningStack.push(pruned);
                return false;
            }
            if (!pruned.keySet().contains(futureVar)) {
                pruned.put(futureVar, new HashSet<Integer>());
            }
            pruned.get(futureVar).addAll(deleted);
        }
        pruningStack.push(pruned);
        return true;

        //        boolean hasPruned = false;
        //        Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
        //
        //        for (CSPVariable future : problem.getContraints().get(current).keySet()) {
        //            BinaryConstraint constr = problem.getContraints().get(current).get(future);
        //            for (int val : future.getDomain()) {
        //                if (!constr.hasSupport(current.getValue())) {
        //                    hasPruned = true;
        //                    future.removeFromDomain(val);
        //                    if (!pruned.keySet().contains(future)) {
        //                        pruned.put(future, new HashSet());
        //                    }
        //                    pruned.get(future).add(val);
        //                }
        //            }
        //        }
        //        pruningStack.push(pruned);
        //        return hasPruned;

    }

    private Set<Integer> revise(BinaryConstraint constr, CSPVariable currentVar) {

        CSPVariable futureVar = constr.getOtherVar(currentVar);
        Set<Integer> toDelete = new HashSet<Integer>();
        for (int futureVal : futureVar.getDomain()) {
            if (!constr.isSupported(futureVar, futureVal)) {
                toDelete.add(futureVal);
            }
        }
        futureVar.removeFromDomain(toDelete);

        return toDelete;
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
