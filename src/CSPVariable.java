import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CSPVariable {

    private final String name;
    private Set<Integer> domain;

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

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

    private boolean assigned;
    private int value;
    private final int order;

    public CSPVariable(String name, int lowerBound, int upperBound) {

        domain = new HashSet<Integer>();
        this.order = -1;
        this.name = name;
        for (int i = lowerBound; i <= upperBound; i++) {
            domain.add(i);
        }
        setAssigned(false);
    }

    public CSPVariable(String name, Collection<Integer> domain) {

        this.name = name;
        this.order = -1;
        this.domain.addAll(domain);
        setAssigned(false);
    }

    public CSPVariable(String name, int order, int lowerBound, int upperBound) {

        domain = new HashSet<Integer>();
        this.order = order;
        this.name = name;
        for (int i = lowerBound; i <= upperBound; i++) {
            domain.add(i);
        }
        setAssigned(false);
    }

    public CSPVariable(String name, int order, Collection<Integer> domain) {

        this.name = name;
        this.order = order;
        this.domain.addAll(domain);
        setAssigned(false);
    }

    public String getName() {

        return name;
    }

    public boolean isAssigned() {

        return assigned;
    }

    public void assign(int value) {

        assert !assigned;
        assert domain.contains(value);
        this.value = value;
        assigned = true;
    }

    public void unassign() {

        assigned = false;
    }

    public void addToDomain(int value) {

        domain.add(value);
    }

    public void removeFromDomain(Set<Integer> toDelete) {

        domain.removeAll(toDelete);

    }

    public Set<Integer> getDomain() {

        return domain;
    }

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

    public int getValue() {

        assert isAssigned();
        return value;
    }

    public void setValue(int value) {

        this.value = value;
    }

    public void setAssigned(boolean assigned) {

        this.assigned = assigned;
    }

    public void removeFromDomain(int val) {

        domain.remove(val);

    }

    public int getOrder() {

        return order;
    }

}
