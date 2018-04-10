import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class BinaryCSP {

    private ArrayList<CSPVariable> varList;
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

    //    public ArrayList<BinaryConstraint> getConstraints() {
    //
    //        return constraints;
    //    }

    public CSPVariable getVar(int i) {

        return varList.get(i);
    }

    public ArrayList<CSPVariable> getVars() {

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

        return constraints.get(past).get(current).isConsistent();

        //        for (BinaryConstraint constr : getConstraints()) {
        //            if (constr.getFirstVar() == varList.get(i) && constr.getSecondVar() == varList.get(depth)
        //                    && !constr.isConsistent()) {
        //                return false;
        //            }
        //        }
        //        return true;
    }

    public boolean completeAssignment() {

        boolean done = false;
        for (CSPVariable var : varList) {
            done = var.isAssigned();
        }
        return done;
    }

    public Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> getContraints() {

        return constraints;
    }

    public Collection<BinaryConstraint> getContraints(CSPVariable currentVar) {

        if (constraints.containsKey(currentVar)) {
            return constraints.get(currentVar).values();
        } else {
            List<BinaryConstraint> c = new ArrayList<BinaryConstraint>();
            for (CSPVariable first : constraints.keySet()) {
                if (constraints.get(first).containsKey(currentVar)) {
                    c.add(constraints.get(first).get(currentVar));
                }
            }
            return c;
        }
    }

}
