import java.util.HashSet;
import java.util.Set;

public final class BinaryArc {

    private final CSPVariable origin, destination;
    private Set<BinaryTuple> tuples;
    

    public BinaryArc(CSPVariable firstVar, CSPVariable secondVar, Set<BinaryTuple> t) {

        origin = firstVar;
        destination = secondVar;
        tuples = t;
    }
    
    public BinaryArc reverse() {
    	Set<BinaryTuple> tupsReversed = new HashSet<BinaryTuple>();
    	for(BinaryTuple tuple: tuples) {
    		tupsReversed.add(tuple.reverse());
    	}
    	return new BinaryArc(destination,origin,tupsReversed);
    }


    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        result.append(origin.getName() + "->" + destination.getName());
        return result.toString();
    }



    public boolean isSupported(int destVal) {
    	if(origin.isAssigned()) {
    		return tuples.contains(new BinaryTuple(origin.getValue(),destVal));
    	} else {
    		for (int orgVal:origin.getDomain()) {
    			if(tuples.contains(new BinaryTuple(orgVal,destVal))) {
    				return true;
    			}
    		}
    		return false;
    	}
    }

    public CSPVariable getOrigin() {
		return origin;
	}

	public CSPVariable getDestination() {
		return destination;
	}

	public boolean isConsistent() {

        //all vars are assigned
        if (origin.isAssigned() && destination.isAssigned()) {
            for (BinaryTuple tup : tuples) {
                if (tup.matches(origin.getValue(), destination.getValue())) {
                    return true;
                }
            }
            return false;

        } else if (origin.isAssigned() && !destination.isAssigned()) { //only second is assigned
        	for(int destVal: destination.getDomain()) {
        		if(tuples.contains(new BinaryTuple(origin.getValue(),destVal))) {
        			return true;
        		}
        	}
        	return false;
        } else if (!origin.isAssigned() && destination.isAssigned()) { //only first is assigned
            for (BinaryTuple tup : tuples) {
                if (origin.getDomain().contains(tup.getVal1()) && tup.getVal2() == destination.getValue()) {
                    return true;
                }
            }
            return false;
        } else {//none are assigned
            for (BinaryTuple tup : tuples) {
                if (origin.getDomain().contains(tup.getVal1()) && destination.getDomain().contains(tup.getVal2())) {
                    return true;
                }
            }
            return false;
        }
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
