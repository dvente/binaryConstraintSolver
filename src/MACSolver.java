import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class MACSolver {

    BinaryCSP problem;
    long branchesExplored;
    long arcsRevised;
    Queue<CSPVariable> varQueue;
    Stack<Map<CSPVariable, Set<Integer>>> pruningStack;

    public final Comparator<CSPVariable> SmallestDomainComparator = new Comparator<CSPVariable>() {

        @Override
        public int compare(CSPVariable arg0, CSPVariable arg1) {

            return arg0.getDomain().size() - arg1.getDomain().size();
        }

    };

    // use for static orderings
    public final Comparator<CSPVariable> OrderComparator = new Comparator<CSPVariable>() {

        @Override
        public int compare(CSPVariable arg0, CSPVariable arg1) {

            return arg0.getOrder() - arg1.getOrder();
        }

    };

    public MACSolver(BinaryCSP problem, boolean dynamicOrdering) {

        super();
        this.problem = problem;
        if (dynamicOrdering) {
            varQueue = new PriorityQueue<CSPVariable>(SmallestDomainComparator);
        } else {
            varQueue = new PriorityQueue<CSPVariable>(OrderComparator);
        }

        varQueue.addAll(problem.getVars());
        pruningStack = new Stack<Map<CSPVariable, Set<Integer>>>();
    }

    public void printSolution() {

        StringBuffer result = new StringBuffer();
        result.append("Branches explored: " + branchesExplored + "\n");
        result.append("Arcs Revised: " + arcsRevised + "\n");
        result.append("Number of Variables: " + problem.getNoVariables() + "\n");
        result.append("Number of Constraints: " + Integer.toString(problem.getNoConstraints()) + "\n");
        result.append("Solution: \n");
        for (int i = 0; i < problem.getVars().size(); i++) {
            result.append(problem.getVars().get(i).toString() + "\n");
        }
        System.out.println(result);
    }

    public void assign(CSPVariable var, int value) {

        assert !var.isAssigned();
        assert var.getDomain().contains(value);
        var.setValue(value);
        var.setAssigned(true);
    }

    public void unassign(CSPVariable var) {

        var.setAssigned(false);
    }

    public static void main(String[] args) {

        String CSPLocation = args[0];
        String heuristicLocation;
        MACSolver solver;
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(CSPLocation);
        if (args.length != 2) {

            solver = new MACSolver(reader.readBinaryCSP(CSPLocation), true);
            System.out.println("Smallest Domain");
        } else {
            heuristicLocation = args[1];
            solver = new MACSolver(reader.readBinaryCSP(CSPLocation, heuristicLocation), false);
            System.out.println(heuristicLocation);
        }

        solver.solveCurrentProblem();
    }

    public void solveCurrentProblem() {

        if (!MAC3()) {
            System.out.println("No solution possible");
        }
    }

    // just return an element, we can get fancy later
    private int selectValFromDomain(CSPVariable var) {

        return var.getDomain().iterator().next();
    }

    public boolean AC3() {

        Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
        Queue<BinaryArc> q = problem.getArcQueue();
        while (!q.isEmpty()) {
            BinaryArc arc = q.poll();
            Set<Integer> deleted = revise(arc);

            if (!pruned.containsKey(arc.getDestination())) {
                pruned.put(arc.getDestination(), new HashSet<Integer>());
            }
            pruned.get(arc.getDestination()).addAll(deleted);

            if (arc.getDestination().getDomain().isEmpty()) {
                pruningStack.push(pruned);
                return false;

            }

            if (!deleted.isEmpty()) {
                for (BinaryArc newArc : problem.getArcs().get(arc.getDestination()).values()) {
                    if (!newArc.equals(arc.reverse())) {
                        q.offer(newArc);
                    }
                }
            }

        }
        pruningStack.push(pruned);
        return true;

    }

    public boolean MAC3() {

        if (problem.completeAssignment()) {
            assert problem.isConsistent();
            printSolution();
            return true;

        }
        CSPVariable var = varQueue.peek();
        int val = selectValFromDomain(var);
        return branchMAC3Left(var, val) || branchMAC3Right(var, val);
    }

    public boolean branchMAC3Left(CSPVariable var, int val) {

        branchesExplored++;
        assign(var, val);

        if (AC3()) {
            varQueue.remove(var);
            if (MAC3()) {
                return true;
            }
            varQueue.offer(var);
        }
        unassign(var);
        undoPruning();
        return false;

    }

    public boolean branchMAC3Right(CSPVariable var, int val) {

        branchesExplored++;
        var.removeFromDomain(val);

        if (!var.getDomain().isEmpty()) {
            if (AC3()) {
                if (MAC3()) {
                    return true;
                }
            }
            undoPruning();
        }
        restoreValue(var, val);
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

    private Set<Integer> revise(BinaryArc arc) {

        Set<Integer> toDelete = new HashSet<Integer>();
        if (arc.getDestination().isAssigned()) {
            return toDelete;
        }
        for (int futureVal : arc.getDestination().getDomain()) {
            if (!arc.isSupported(futureVal)) {
                toDelete.add(futureVal);
            }
        }
        arc.getDestination().removeFromDomain(toDelete);
        if (!toDelete.isEmpty()) {
            arcsRevised++;
        }

        return toDelete;
    }

    //    private boolean reviseFutureArcs(CSPVariable currentVar) {
    //
    //        Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
    //        boolean consistent = true;
    //        Collection<BinaryConstraint> arcsToRevise = problem.getContraints(currentVar);
    //        for (BinaryConstraint constr : arcsToRevise) {
    //            CSPVariable futureVar = constr.getOtherVar(currentVar);
    //            Set<Integer> deleted = revise(constr, currentVar);
    //            consistent = !futureVar.getDomain().isEmpty();
    //            if (!pruned.keySet().contains(futureVar)) {
    //                pruned.put(futureVar, new HashSet<Integer>());
    //            }
    //            pruned.get(futureVar).addAll(deleted);
    //            if (!consistent) {
    //                pruningStack.push(pruned);
    //                return false;
    //            }
    //
    //        }
    //        pruningStack.push(pruned);
    //        return true;
    //
    //    }

    @Override
    public String toString() {

        return "FCSolver \n" + problem.toString();
    }

    private void restoreValue(CSPVariable var, int val) {

        var.addToDomain(val);

    }
}
