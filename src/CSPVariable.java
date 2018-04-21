
/*
 * @author 170008773
 */
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class CSPVariable.
 */
public class CSPVariable {

	/** The name of the variable. */
	private final String name;

	/** The domain of the variable. */
	private Set<Integer> domain;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CSPVariable other = (CSPVariable) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/** Is the variable assigned? */
	private boolean assigned;

	/** The value. Can only be accessed if the variable is assigned */
	private int value;

	/** The order in the static heuristic being used. */
	private final int order;

	/**
	 * Instantiates a new CSP variable, with all integers between the bounds in it's
	 * domain.
	 *
	 * @param name
	 *            the name of the variable
	 * @param lowerBound
	 *            the lower bound of the domain
	 * @param upperBound
	 *            the upper bound of the domain
	 */
	public CSPVariable(String name, int lowerBound, int upperBound) {

		domain = new HashSet<Integer>();
		this.order = -1;
		this.name = name;
		for (int i = lowerBound; i <= upperBound; i++) {
			domain.add(i);
		}
		setAssigned(false);
	}

	/**
	 * Instantiates a new CSP variable.
	 *
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 */
	public CSPVariable(String name, Collection<Integer> domain) {

		this.name = name;
		this.order = -1;
		this.domain.addAll(domain);
		setAssigned(false);
	}

	/**
	 * Instantiates a new CSP variable, with all integers between the bounds in it's
	 * domain.
	 *
	 * @param name
	 *            the name of the variable
	 * @param order
	 *            the order in the static heuristic
	 * @param lowerBound
	 *            the lower bound of the domain
	 * @param upperBound
	 *            the upper bound of the domain
	 */
	public CSPVariable(String name, int order, int lowerBound, int upperBound) {

		domain = new HashSet<Integer>();
		this.order = order;
		this.name = name;
		for (int i = lowerBound; i <= upperBound; i++) {
			domain.add(i);
		}
		setAssigned(false);
	}

	/**
	 * Instantiates a new CSP variable.
	 *
	 * @param name
	 *            the name of the variable
	 * @param domain
	 *            the domain of the variable
	 */
	public CSPVariable(String name, int order, Collection<Integer> domain) {

		this.name = name;
		this.order = order;
		this.domain.addAll(domain);
		setAssigned(false);
	}

	/**
	 * Gets the name of the variable.
	 *
	 * @return the name of the variable
	 */
	public String getName() {

		return name;
	}

	/**
	 * Checks if the variable is assigned.
	 *
	 * @return true, if is assigned
	 */
	public boolean isAssigned() {

		return assigned;
	}

	/**
	 * Assign the varable the given value.
	 *
	 * @param value
	 *            the value to assign the variable
	 */
	public void assign(int value) {

		assert !assigned;
		assert domain.contains(value);
		this.value = value;
		assigned = true;
	}

	/**
	 * Unassign the variable.
	 */
	public void unassign() {

		assigned = false;
	}

	/**
	 * Adds the value to the domain.
	 *
	 * @param value
	 *            the value to be added to the domain
	 */
	public void addToDomain(int value) {

		domain.add(value);
	}

	/**
	 * Removes a set of values from the domain.
	 *
	 * @param toDelete
	 *            the set of to delete values
	 */
	public void removeFromDomain(Set<Integer> toDelete) {

		domain.removeAll(toDelete);

	}

	/**
	 * Gets the domain of the variable.
	 *
	 * @return the domain the variable
	 */
	public Set<Integer> getDomain() {

		return domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		if (isAssigned()) {
			return "Var " + name + ": " + Integer.toString(getValue());
		} else {
			String ans = "Var " + name + ": ";
			for (int val : domain) {
				ans += Integer.toString(val) + ", ";
			}
			return ans;
		}

	}

	/**
	 * Gets the value of the variable.
	 *
	 * @return the value of the variable
	 */
	public int getValue() {

		assert isAssigned();
		return value;
	}

	/**
	 * Sets the value the variable.
	 *
	 * @param value
	 *            the new value the variable
	 */
	public void setValue(int value) {

		this.value = value;
	}

	/**
	 * Sets the assigned.
	 *
	 * @param assigned
	 *            the new assigned
	 */
	public void setAssigned(boolean assigned) {

		this.assigned = assigned;
	}

	/**
	 * Removes a value from the domain.
	 *
	 * @param val
	 *            the value to remove
	 */
	public void removeFromDomain(int val) {

		domain.remove(val);

	}

	/**
	 * Gets the order of the variable.
	 *
	 * @return the order of the variable
	 */
	public int getOrder() {

		return order;
	}

}
