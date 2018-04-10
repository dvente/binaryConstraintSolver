import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CSPVariable {

    private String name;
    private Set<Integer> domain;
    private boolean assigned;
    private int value;

    public CSPVariable(String name, int lowerBound, int upperBound) {

        domain = new HashSet<Integer>();
        this.name = name;
        for (int i = lowerBound; i <= upperBound; i++) {
            domain.add(i);
        }
        setAssigned(false);
    }

    public CSPVariable(String name, Collection<Integer> domain) {

        this.name = name;
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

        domain.remove(toDelete);

    }

    public Set<Integer> getDomain() {

        return domain;
    }

    @Override
    public String toString() {

        if (isAssigned()) {
            return "Var " + name + ": " + Integer.toString(getValue()) + "\n";
        } else {
            String ans = "Var " + name + ": ";
            for (int val : domain) {
                ans += Integer.toString(val) + ", ";
            }
            return ans + "\n";
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

}
