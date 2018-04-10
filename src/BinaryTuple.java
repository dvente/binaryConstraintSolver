/**
 * Assumes tuple values are integers
 */
public final class BinaryTuple {
  public int getVal1() {
		return val1;
	}

	public int getVal2() {
		return val2;
	}

private final int val1, val2 ;
  
  public BinaryTuple(int v1, int v2) {
    val1 = v1 ;
    val2 = v2 ;
  }
  
  public String toString() {
    return "<"+val1+", "+val2+">" ;
  }
  
  @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + val1;
	result = prime * result + val2;
	return result;
}

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

public boolean matches(int v1, int v2) {
	    return (val1 == v1) && (val2 == v2) ;
	  }
}
