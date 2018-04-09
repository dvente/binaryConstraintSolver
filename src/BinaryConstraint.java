import java.util.ArrayList;

public final class BinaryConstraint {

    private CSPVariable firstVar, secondVar;
    private ArrayList<BinaryTuple> tuples;

    public BinaryConstraint(CSPVariable firstVar, CSPVariable secondVar, ArrayList<BinaryTuple> t) {

        this.firstVar = firstVar;
        this.secondVar = secondVar;
        tuples = t;
    }

    public CSPVariable getFirstVar() {

        return firstVar;
    }

    public CSPVariable getSecondVar() {

        return secondVar;
    }

    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        result.append("c(" + firstVar.getName() + ", " + secondVar.getName() + ")\n");
        for (BinaryTuple bt : tuples) {
            result.append(bt + "\n");
        }
        return result.toString();
    }

    // SUGGESTION: You will want to add methods here to reason about the constraint

    public boolean hasSupport(int val) {

        for (BinaryTuple tup : tuples) {
            if (tup.getVal1() == val) {
                return true;
            }

        }
        return false;
    }

    public boolean isConsistent() {

        //all vars are assigned
        if (firstVar.isAssigned() && secondVar.isAssigned()) {
            for (BinaryTuple tup : tuples) {
                if (tup.matches(firstVar.getValue(), secondVar.getValue())) {
                    return true;
                }
            }
            return false;

        } else if (firstVar.isAssigned() && !secondVar.isAssigned()) { //only second is assigned
            for (BinaryTuple tup : tuples) {
                if (tup.getVal1() == firstVar.getValue() && secondVar.getDomain().contains(tup.getVal2())) {
                    return true;
                }
            }
            return false;
        } else if (!firstVar.isAssigned() && secondVar.isAssigned()) { //only first is assigned
            for (BinaryTuple tup : tuples) {
                if (firstVar.getDomain().contains(tup.getVal1()) && tup.getVal2() == secondVar.getValue()) {
                    return true;
                }
            }
            return false;
        } else {//none are assigned
            for (BinaryTuple tup : tuples) {
                if (firstVar.getDomain().contains(tup.getVal1()) && secondVar.getDomain().contains(tup.getVal2())) {
                    return true;
                }
            }
            return false;
        }
    }
}
