import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CSPVariable {
	private String name;
	private Set<Integer> domain;
	private boolean assigned;
	private int value;
	private int upperBound, lowerBound;
	
	public CSPVariable(String name, int lowerBound, int upperBound) {
		domain = new HashSet<Integer>();
		this.name = name;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		for(int i = lowerBound; i <= upperBound; i++) {
			domain.add(i);
		}
		assigned = false;
	}
	
	public CSPVariable(String name, Collection<Integer> domain) {
		this.name = name;
		this.domain.addAll(domain);
		assigned = false;
	}

	public String getName() {
		return name;
	}

	public boolean isAssigned() {
		return assigned;
	}
	
	private void setUpperBound() {
		int upper = Integer.MIN_VALUE;
		for (int val: domain) {
			if(val > upper) {
				upper = val;
			}
		}
		upperBound = upper;
	}
	
	private void setLowerBound() {
		int lower = Integer.MAX_VALUE;
		for (int val: domain) {
			if(val < lower) {
				lower = val;
			}
		}
		lowerBound = lower;
	}

	public void assign(int value){
		assert !assigned;
		assert domain.contains(value);
		this.value = value;
		assigned = true;
	}
	
	public void addToDomain(int value) {
		domain.add(value);
		setLowerBound();
		setUpperBound();
	}
	
	public void removeFromDomain(int value) throws DomainWhipeoutException {
		domain.remove(value);
		if(domain.isEmpty()) {
			throw new DomainWhipeoutException();
		}
		setLowerBound();
		setUpperBound();
	}
	
	
	public void unassign() {
		assigned = false; 
	}

	public Set<Integer> getDomain() {
		return domain;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	@Override
	public String toString() {
		if(assigned) {
			return "Var "+ name +": " + Integer.toString(value) +"\n"; 
		}
		else {
			return "Var "+name+": "+ lowerBound+" .. "+upperBound+"\n";
		}
		
	}

	public int getUpperBound() {
		return upperBound;
	}
	
	public int getValue() {
		assert isAssigned();
		return value;
	}
	
}
