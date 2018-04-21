
/*
 * @author 170008773
 */
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * The Class MACSolver.
 */
public class MACSolver {

	/** The problem to be solved. */
	BinaryCSP problem;

	/** The number of branches explored. */
	long branchesExplored;

	/** The number of arcs revised. */
	long arcsRevised;

	/** The queue of variables left to process. */
	Queue<CSPVariable> varQueue;

	/** The pruning stack. */
	Stack<Map<CSPVariable, Set<Integer>>> pruningStack;

	/** The Smallest domain comparator. to be used with the dynamic heuristic */
	public final Comparator<CSPVariable> SmallestDomainComparator = new Comparator<CSPVariable>() {

		@Override
		public int compare(CSPVariable arg0, CSPVariable arg1) {

			return arg0.getDomain().size() - arg1.getDomain().size();
		}

	};

	/** The Order comparator. to be used with the static heuristic */
	public final Comparator<CSPVariable> OrderComparator = new Comparator<CSPVariable>() {

		@Override
		public int compare(CSPVariable arg0, CSPVariable arg1) {

			return arg0.getOrder() - arg1.getOrder();
		}

	};

	/**
	 * Instantiates a new MAC solver.
	 *
	 * @param problem
	 *            the problem to be solved
	 * @param dynamicOrdering
	 *            Whether to use the dynamic ordering
	 */
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
	 * Assign a variable a perticular value.
	 *
	 * @param var
	 *            the variable to be assigned
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
	 *            the variable to be unasigned
	 */
	public void unassign(CSPVariable var) {

		var.setAssigned(false);
	}

	/**
	 * The main method, instantiates the solver with aproporate heuristic.
	 *
	 * @param args
	 *            the arguments
	 */
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

	/**
	 * Solve current problem using MAC3.
	 */
	public void solveCurrentProblem() {

		if (!MAC3()) {
			System.out.println("No solution possible");
		}
	}

	/**
	 * Select a value from the domain of var.
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
	 * AC3. Adapted from the lecture slides
	 *
	 * @return false if a domain wipe out occurs and true otherwise
	 */
	public boolean AC3() {

		Map<CSPVariable, Set<Integer>> pruned = new HashMap<CSPVariable, Set<Integer>>();
		Queue<BinaryArc> q = problem.getArcQueue();
		while (!q.isEmpty()) {
			// revise the arc
			BinaryArc arc = q.poll();
			Set<Integer> deleted = revise(arc);

			// record what was pruned so we can undo later
			if (!pruned.containsKey(arc.getDestination())) {
				pruned.put(arc.getDestination(), new HashSet<Integer>());
			}
			pruned.get(arc.getDestination()).addAll(deleted);

			// was there a wipeout?
			if (arc.getDestination().getDomain().isEmpty()) {
				pruningStack.push(pruned);
				return false;

			}

			// only add arcs where something was revised
			if (!deleted.isEmpty()) {
				for (BinaryArc newArc : problem.getOutgoingArcs(arc.getDestination())) {
					if (!newArc.equals(arc.reverse())) {
						q.offer(newArc);
					}
				}
			}

		}
		pruningStack.push(pruned);
		return true;

	}

	/**
	 * MAC3. Adapted from lecture notes
	 *
	 * @return true, if a solution is found
	 */
	public boolean MAC3() {

		// are we done?
		if (problem.completeAssignment()) {
			assert problem.isConsistent();
			printSolution();
			return true;

		}

		// get a variable, get a value and start branching!
		CSPVariable var = varQueue.peek();
		int val = selectValFromDomain(var);
		return branchMAC3Left(var, val) || branchMAC3Right(var, val);
	}

	/**
	 * Branch MAC 3 left, assigns a value.
	 *
	 * @param var
	 *            the variable to assign a value to
	 * @param val
	 *            the value to be assigned
	 * @return true, if a solution was found
	 */
	public boolean branchMAC3Left(CSPVariable var, int val) {

		branchesExplored++;
		assign(var, val);

		// revise arcs
		if (AC3()) {
			// so far we're good so remove the variable from the queue
			varQueue.remove(var);
			if (MAC3()) {
				return true;
			}
			// didn't find anything so start undoing
			varQueue.offer(var);
		}
		unassign(var);
		undoPruning();
		return false;

	}

	/**
	 * Branch MAC 3 right, sets a variable unequal to the value.
	 *
	 * @param var
	 *            the variable to assert disequality about
	 * @param val
	 *            the value to assert disequality about
	 * @return true, if a solution was found
	 */
	public boolean branchMAC3Right(CSPVariable var, int val) {

		branchesExplored++;
		var.removeFromDomain(val);

		// did a wipe out occur?
		if (!var.getDomain().isEmpty()) {
			// so far so good so start revising
			if (AC3()) {
				// still good so recurse
				if (MAC3()) {
					return true;
				}
			}
			// didn't find anything so start undoing
			undoPruning();
		}
		restoreValue(var, val);
		return false;
	}

	/**
	 * Undo the pruning from the previous step.
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
	 * Restore a value to the domain.
	 *
	 * @param var
	 *            the variable
	 * @param val
	 *            the value to restore
	 */
	private void restoreValue(CSPVariable var, int val) {

		var.addToDomain(val);

	}
}
