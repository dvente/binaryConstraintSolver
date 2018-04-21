
/*
 * @author 170008773
 */
import java.util.HashSet;
import java.util.Set;

/**
 * The Class BinaryArc.
 */
public final class BinaryArc {

	/** The destination variable. */
	private final CSPVariable origin, destination;

	/** The table of allowed tuples. */
	private Set<BinaryTuple> tuples;

	/**
	 * Instantiates a new binary arc.
	 *
	 * @param firstVar
	 *            the origin variable
	 * @param secondVar
	 *            the destination variable
	 * @param t
	 *            the set of allowed tuples
	 */
	public BinaryArc(CSPVariable firstVar, CSPVariable secondVar, Set<BinaryTuple> t) {

		origin = firstVar;
		destination = secondVar;
		tuples = t;
	}

	/**
	 * Reverse the arc i.e. c(x,y) => c(y,x).
	 *
	 * @return the binary arc reversed
	 */
	public BinaryArc reverse() {
		Set<BinaryTuple> tupsReversed = new HashSet<BinaryTuple>();
		for (BinaryTuple tuple : tuples) {
			tupsReversed.add(tuple.reverse());
		}
		return new BinaryArc(destination, origin, tupsReversed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append(origin.getName() + "->" + destination.getName());
		return result.toString();
	}

	/**
	 * Checks if the value has support in the destination domain.
	 *
	 * @param destVal
	 *            the value to be tested
	 * @return true, if the value has support in the destination domain
	 */
	public boolean isSupported(int destVal) {
		if (origin.isAssigned()) {
			return tuples.contains(new BinaryTuple(origin.getValue(), destVal));
		} else {
			// origin isn't assigned so we'll have to check the whole domain.
			for (int orgVal : origin.getDomain()) {
				if (tuples.contains(new BinaryTuple(orgVal, destVal))) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Gets the origin variable.
	 *
	 * @return the origin variable
	 */
	public CSPVariable getOrigin() {
		return origin;
	}

	/**
	 * Gets the destination variable.
	 *
	 * @return the destination variable
	 */
	public CSPVariable getDestination() {
		return destination;
	}

	/**
	 * Checks if is consistent, i.e. there exists a consistent assignement.
	 *
	 * @return true, if is consistent
	 */
	public boolean isConsistent() {

		// all variables are assigned
		if (origin.isAssigned() && destination.isAssigned()) {
			return tuples.contains(new BinaryTuple(origin.getValue(), destination.getValue()));

			// only destination is assigned
		} else if (origin.isAssigned() && !destination.isAssigned()) {
			for (int destVal : destination.getDomain()) {
				if (tuples.contains(new BinaryTuple(origin.getValue(), destVal))) {
					return true;
				}
			}
			return false;

			// only origin is assigned
		} else if (!origin.isAssigned() && destination.isAssigned()) {
			for (BinaryTuple tup : tuples) {
				if (origin.getDomain().contains(tup.getVal1()) && tup.getVal2() == destination.getValue()) {
					return true;
				}
			}
			return false;
		} else {
			// none are assigned
			for (BinaryTuple tup : tuples) {
				if (origin.getDomain().contains(tup.getVal1()) && destination.getDomain().contains(tup.getVal2())) {
					return true;
				}
			}
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryArc other = (BinaryArc) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

}
