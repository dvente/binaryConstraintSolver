
/*
 * @author 170008773
 */
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * The Class FCSolver uses simple Forward Checking.
 */
public class FCSolver {

	/** The problem to be solved. */
	BinaryCSP problem;

	/** The number of branches explored. */
	long branchesExplored;

	/** The number of arcs revised. */
	long arcsRevised;

	/** The variable queue. */
	Queue<CSPVariable> varQueue;

	/** The pruning stack. */
	Stack<Map<CSPVariable, Set<Integer>>> pruningStack;

	/** The Smallest domain comparator, to be used as a dynamic heuristic. */
	public final Comparator<CSPVariable> SmallestDomainComparator = new Comparator<CSPVariable>() {

		@Override
		public int compare(CSPVariable arg0, CSPVariable arg1) {

			return arg0.getDomain().size() - arg1.getDomain().size();
		}

	};

	/** The Order comparator, to be used in static heuristics. */
	public final Comparator<CSPVariable> OrderComparator = new Comparator<CSPVariable>() {

		@Override
		public int compare(CSPVariable arg0, CSPVariable arg1) {

			return arg0.getOrder() - arg1.getOrder();
		}

	};

	/**
	 * Instantiates a new FC solver.
	 *
	 * @param problem
	 *            the problem
	 * @param dynamicOrdering
	 *            whether to use the dynamic ordering
	 */
	public FCSolver(BinaryCSP problem, boolean dynamicOrdering) {

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

	/**
	 * Prints the solution.
	 */
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

	/**
	 * Assign a value to a variable.
	 *
	 * @param var
	 *            the variable to assign.
	 * @param value
	 *            the value to assign the variable
	 */
	public void assign(CSPVariable var, int value) {

		assert !var.isAssigned();
		assert var.getDomain().contains(value);
		var.setValue(value);
		var.setAssigned(true);
	}

	/**
	 * Unassign a variable.
	 *
	 * @param var
	 *            the variable
	 */
	public void unassign(CSPVariable var) {

		var.setAssigned(false);
	}

	/**
	 * The main method, instansiated solver and runs it.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		String CSPLocation = args[0];
		String heuristicLocation;
		FCSolver solver;
		BinaryCSPReader reader = new BinaryCSPReader();
		System.out.println(CSPLocation);
		if (args.length != 2) {

			solver = new FCSolver(reader.readBinaryCSP(CSPLocation), true);
			System.out.println("Smallest Domain");
		} else {
			heuristicLocation = args[1];
			solver = new FCSolver(reader.readBinaryCSP(CSPLocation, heuristicLocation), false);
			System.out.println(heuristicLocation);
		}

		solver.solveCurrentProblem();
	}

	/**
	 * Solve current problem using forward checking.
	 */
	public void solveCurrentProblem() {

		if (!forwardChecking()) {
			System.out.println("No solution possible");
		}
	}

	/**
	 * Select a value from the domain of the variable.
	 *
	 * @param var
	 *            the variable to select from
	 * @return a value from the domain of var
	 */
	// just return an element, we can get fancy later
	private int selectValFromDomain(CSPVariable var) {

		return var.getDomain().iterator().next();
	}

	/**
	 * Forward checking.
	 *
	 * @return true, if a solution is found
	 */
	// adapted from lecture slides.
	public boolean forwardChecking() {

		// are we done?
		if (problem.completeAssignment()) {
			assert problem.isConsistent();
			printSolution();
			return true;

		}
		// get a variable, a value and start branching!
		CSPVariable var = varQueue.peek();
		int val = selectValFromDomain(var);
		return branchFCLeft(var, val) || branchFCRight(var, val);
	}

	/**
	 * Branch FC left, assigns a value to a variable.
	 *
	 * @param var
	 *            the variable to be assigned
	 * @param val
	 *            the value to be assigned
	 * @return true, if a solution is found
	 */
	public boolean branchFCLeft(CSPVariable var, int val) {

		branchesExplored++;
		assign(var, val);

		// returns false iff domain whipeout occurs
		if (reviseOutgoingArcs(var)) {
			// we're good so far so remove var from the queue and recurse
			varQueue.remove(var);
			if (forwardChecking()) {
				return true;
			}
			// no game so put it back
			varQueue.offer(var);
		}
		// no solution so go back
		unassign(var);
		undoPruning();
		return false;

	}

	/**
	 * Branch FC right, sets variable to be unequal to the value.
	 *
	 * @param var
	 *            the variable to assert disequality to
	 * @param val
	 *            the value to assert disequality with
	 * @return true, if a solution was found
	 */
	public boolean branchFCRight(CSPVariable var, int val) {

		branchesExplored++;
		var.removeFromDomain(val);

		// domain whipeout?
		if (!var.getDomain().isEmpty()) {
			// can we make deductions?
			if (reviseOutgoingArcs(var)) {
				// we're good so far so recurse
				if (forwardChecking()) {
					return true;
				}
			}
			// no game so undo pruning
			undoPruning();
		}
		restoreValue(var, val);
		return false;
	}

	/**
	 * Undo pruning.
	 */
	private void undoPruning() {

		Map<CSPVariable, Set<Integer>> pruned = pruningStack.pop();
		for (CSPVariable future : pruned.keySet()) {
			for (int val : pruned.get(future)) {
				future.addToDomain(val);
			}
		}
	}

	/**
	 * Revise an arc.
	 *
	 * @param arc
	 *            the arc to be revised
	 * @return the sets the set of all values that were removed from the destination
	 *         domain
	 */
	private Set<Integer> revise(BinaryArc arc) {

		Set<Integer> toDelete = new HashSet<Integer>();
		// if the destination is assigned we don't have to remove anything
		if (arc.getDestination().isAssigned()) {
			return toDelete;
		}

		// check which values are still supported
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

	/**
	 * Revise all outgoing arcs.
	 *
	 * @param originVar
	 *            the origin variable
	 * @return true, if successful
	 */
	private boolean reviseOutgoingArcs(CSPVariable originVar) {

		Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
		boolean consistent = true;
		// get all arcs to check. we'll be revising the mso they need to be in a
		// seperate collection
		Collection<BinaryArc> arcsToRevise = problem.getOutgoingArcs(originVar);
		for (BinaryArc arc : arcsToRevise) {
			// revise the arc
			CSPVariable futureVar = arc.getDestination();
			Set<Integer> deleted = revise(arc);

			// record what was revised so we can undo later
			consistent = !futureVar.getDomain().isEmpty();
			if (!pruned.keySet().contains(futureVar)) {
				pruned.put(futureVar, new HashSet<Integer>());
			}
			pruned.get(futureVar).addAll(deleted);
			if (!consistent) {
				pruningStack.push(pruned);
				return false;
			}

		}
		pruningStack.push(pruned);
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "FCSolver \n" + problem.toString();
	}

	/**
	 * Restore value to a variable.
	 *
	 * @param var
	 *            the variable to restore the value to
	 * @param val
	 *            the value to add to the domain of var
	 */
	private void restoreValue(CSPVariable var, int val) {

		var.addToDomain(val);

	}
}
