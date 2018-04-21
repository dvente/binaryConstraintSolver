
/*
 * @author 170008773
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * The Class BinaryCSP.
 */
public final class BinaryCSP {

	/** The list of variables. */
	private List<CSPVariable> varList;

	/** The arcs. */
	private Map<CSPVariable, Map<CSPVariable, BinaryArc>> arcs;

	/**
	 * Instantiates a new binary CSP.
	 *
	 * @param variables
	 *            the variables
	 * @param c
	 *            the constraint arcs
	 */
	public BinaryCSP(CSPVariable[] variables, Map<CSPVariable, Map<CSPVariable, BinaryArc>> c) {

		varList = new ArrayList<CSPVariable>(Arrays.asList(variables));
		arcs = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append("CSP:\n");
		for (int i = 0; i < varList.size(); i++) {
			result.append(varList.get(i).toString() + "\n");
		}
		for (Map<CSPVariable, BinaryArc> baMap : arcs.values()) {
			for (BinaryArc ba : baMap.values()) {
				result.append(ba + "\n");
			}

		}
		return result.toString();
	}

	/**
	 * Gets the number of variables.
	 *
	 * @return the number of variables in the problem
	 */
	public int getNoVariables() {

		return varList.size();
	}

	/**
	 * Gets the variable.
	 *
	 * @param i
	 *            the index of the variable to be returned
	 * @return the variable at index i
	 */
	public CSPVariable getVar(int i) {

		return varList.get(i);
	}

	/**
	 * Gets the list of variables.
	 *
	 * @return the list containing all variables in the problem.
	 */
	public List<CSPVariable> getVars() {

		return varList;
	}

	/**
	 * Checks if the whole problem is arc consistent.
	 *
	 * @return true, if the problem is arc consistent
	 */
	public boolean isArcConsistent() {

		for (Map<CSPVariable, BinaryArc> baMap : arcs.values()) {
			for (BinaryArc ba : baMap.values()) {
				if (!ba.isConsistent()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if an arc is consistent.
	 *
	 * @param origin
	 *            the origin variable
	 * @param destination
	 *            the destination variable
	 * @return true, if the arc is consistent
	 */
	public boolean isArcConsistent(CSPVariable origin, CSPVariable destination) {

		// does the arc exist?
		if (!arcs.containsKey(origin) || !arcs.get(origin).containsKey(destination)) {
			// if not it is consistent!
			return true;

		}

		return arcs.get(origin).get(destination).isConsistent();

	}

	/**
	 * Checks if the problem is consistent.
	 *
	 * @return true, if the problem is consistent
	 */
	public boolean isConsistent() {

		boolean consistent = true;
		// just check if all the arcs are consistent
		for (CSPVariable first : arcs.keySet()) {
			for (CSPVariable second : arcs.get(first).keySet()) {
				BinaryArc ba = arcs.get(first).get(second);
				consistent = consistent && ba.isConsistent();

			}
		}

		return consistent;
	}

	/**
	 * Checks whether every variable in the problem is assigned.
	 *
	 * @return true, if the problem is fully assigned
	 */
	public boolean completeAssignment() {

		boolean done = true;
		for (CSPVariable var : varList) {
			done = var.isAssigned() && done;
		}
		return done;
	}

	/**
	 * Gets all the constraint arcs.
	 *
	 * @return the arcs
	 */
	public Map<CSPVariable, Map<CSPVariable, BinaryArc>> getArcs() {

		return arcs;
	}

	/**
	 * Gets all the constraint arcs in a queue, in no perticular order.
	 *
	 * @return the arc queue
	 */
	public Queue<BinaryArc> getArcQueue() {

		Queue<BinaryArc> q = new LinkedList<BinaryArc>();
		for (CSPVariable first : arcs.keySet()) {
			for (CSPVariable second : arcs.get(first).keySet()) {
				q.offer(arcs.get(first).get(second));
			}
		}
		return q;
	}

	/**
	 * Gets the outgoing arcs from the provided variable.
	 *
	 * @param var
	 *            the variable to get all the outgoing arcs from
	 * @return the outgoing arcs
	 */
	public Collection<BinaryArc> getOutgoingArcs(CSPVariable var) {

		List<BinaryArc> c = new ArrayList<BinaryArc>();

		if (arcs.containsKey(var)) {
			c.addAll(arcs.get(var).values());
		}
		return c;
	}

	/**
	 * Gets the incoming arcs to the provided variable.
	 *
	 * @param var
	 *            the variable to get all the incoming arcs of
	 * @return the incoming arcs
	 */
	public Collection<BinaryArc> getIncomingArcs(CSPVariable var) {

		List<BinaryArc> c = new ArrayList<BinaryArc>();

		for (CSPVariable first : arcs.keySet()) {
			if (arcs.get(first).containsKey(var)) {
				c.add(arcs.get(first).get(var));
			}
		}
		return c;
	}

	/**
	 * Gets the number of constraints.
	 *
	 * @return the number of constraints
	 */
	public int getNoConstraints() {

		int counter = 0;
		for (CSPVariable first : arcs.keySet()) {
			counter += arcs.get(first).values().size();
		}

		// every constraint consists of 2 arcs
		return counter / 2;
	}

}
