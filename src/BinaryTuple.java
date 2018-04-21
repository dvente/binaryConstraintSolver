// TODO: Auto-generated Javadoc
/**
 * Assumes tuple values are integers.
 */
public final class BinaryTuple {

	/**
	 * Gets the first value.
	 *
	 * @return val 1
	 */
	public int getVal1() {
		return val1;
	}

	/**
	 * Gets second value.
	 *
	 * @return the val 2
	 */
	public int getVal2() {
		return val2;
	}

	/** The values. */
	private final int val1, val2;

	/**
	 * Instantiates a new binary tuple.
	 *
	 * @param v1
	 *            the v 1
	 * @param v2
	 *            the v 2
	 */
	public BinaryTuple(int v1, int v2) {
		val1 = v1;
		val2 = v2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "<" + val1 + ", " + val2 + ">";
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
		result = prime * result + val1;
		result = prime * result + val2;
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
		BinaryTuple other = (BinaryTuple) obj;
		if (val1 != other.val1)
			return false;
		if (val2 != other.val2)
			return false;
		return true;
	}

	/**
	 * Matches.
	 *
	 * @param v1
	 *            the v 1
	 * @param v2
	 *            the v 2
	 * @return true, if the tuples match
	 */
	public boolean matches(int v1, int v2) {
		return (val1 == v1) && (val2 == v2);
	}

	/**
	 * Reverse the tuple.
	 *
	 * @return the binary tuple reversed
	 */
	public BinaryTuple reverse() {
		return new BinaryTuple(val2, val1);
	}
}
