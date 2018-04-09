import java.util.ArrayList;
import java.util.Arrays;

public final class BinaryCSP {

    private ArrayList<CSPVariable> varList;
    private ArrayList<BinaryConstraint> constraints;

    public BinaryCSP(CSPVariable[] domainBounds, ArrayList<BinaryConstraint> c) {

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
        for (BinaryConstraint bc : constraints) {
            result.append(bc + "\n");
        }
        return result.toString();
    }

    public int getNoVariables() {

        return varList.size();
    }

    public ArrayList<BinaryConstraint> getConstraints() {

        return constraints;
    }

    public CSPVariable getVar(int i) {

        return varList.get(i);
    }

    public ArrayList<CSPVariable> getVars() {

        return varList;
    }

    public boolean isArcConsistent() {

        for (BinaryConstraint constr : getConstraints()) {
            if (!constr.isConsistent()) {
                return false;
            }
        }
        return true;
    }

    public boolean isArcConsistent(int i, int depth) {

        for (BinaryConstraint constr : getConstraints()) {
            if (constr.getFirstVar() == varList.get(i) && constr.getSecondVar() == varList.get(depth)
                    && !constr.isConsistent()) {
                return false;
            }
        }
        return true;
    }

}
