import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public final class BinaryCSP {

    private List<CSPVariable> varList;
    private Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> constraints;

    public BinaryCSP(CSPVariable[] domainBounds, Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> c) {

        varList = new ArrayList<CSPVariable>(Arrays.asList(domainBounds));
        constraints = c;
    }

    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        result.append("CSP:\n");
        for (int i = 0; i < varList.size(); i++) {
            result.append(varList.get(i).toString());
        }
        for (Map<CSPVariable, BinaryConstraint> bcMap : constraints.values()) {
            for (BinaryConstraint bc : bcMap.values()) {
                result.append(bc + "\n");
            }

        }
        return result.toString();
    }

    public int getNoVariables() {

        return varList.size();
    } 

    public CSPVariable getVar(int i) {

        return varList.get(i);
    }

    public List<CSPVariable> getVars() {

        return varList;
    }

    public boolean isArcConsistent() {

        for (Map<CSPVariable, BinaryConstraint> bcMap : constraints.values()) {
            for (BinaryConstraint bc : bcMap.values()) {
                if (!bc.isConsistent()) {
                    return false;
                }
            }

        }
        return true;
    }

    public boolean isArcConsistent(CSPVariable past, CSPVariable current) {

        if (!constraints.containsKey(past) || !constraints.get(past).containsKey(current)) {
            return true;

        }

        return constraints.get(past).get(current).isConsistent();

    }

    public boolean isConsistent() {

        boolean consistent = true;
        for (CSPVariable first : constraints.keySet()) {
            for (CSPVariable second : constraints.get(first).keySet()) {
                BinaryConstraint bc = constraints.get(first).get(second);
                consistent = consistent && bc.isConsistent();

            }
        }

        return consistent;
    }

    public boolean completeAssignment() {

        boolean done = true;
        for (CSPVariable var : varList) {
            done = var.isAssigned() && done;
        }
        return done;
    }

    public Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> getContraints() {
    	
        return constraints;
    }

    public Queue<BinaryConstraint> getContraintQueue() {
    	Queue<BinaryConstraint> q = new LinkedList<BinaryConstraint>();
    	for(CSPVariable first: constraints.keySet()) {
    		for(CSPVariable second: constraints.get(first).keySet()) {
    			q.offer(constraints.get(first).get(second));
    		}
    	}
        return q;
    }
    
    public Collection<BinaryConstraint> getContraints(CSPVariable currentVar) {

        List<BinaryConstraint> c = new ArrayList<BinaryConstraint>();

        if (constraints.containsKey(currentVar)) {
            c.addAll(constraints.get(currentVar).values());
        }
        for (CSPVariable first : constraints.keySet()) {
            if (constraints.get(first).containsKey(currentVar)) {
                c.add(constraints.get(first).get(currentVar));
            }
        }
        return c;
    }

}
