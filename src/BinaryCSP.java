import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public final class BinaryCSP {

    private List<CSPVariable> varList;
    private Map<CSPVariable, Map<CSPVariable, BinaryArc>> arcs;

    public BinaryCSP(CSPVariable[] domainBounds, Map<CSPVariable, Map<CSPVariable, BinaryArc>> c) {

        varList = new ArrayList<CSPVariable>(Arrays.asList(domainBounds));
        arcs = c;
    }

    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        result.append("CSP:\n");
        for (int i = 0; i < varList.size(); i++) {
            result.append(varList.get(i).toString() + "\n");
        }
        for (Map<CSPVariable, BinaryArc> baMap : arcs.values()) {
            for (BinaryArc ba : baMap.values()) {
                result.append(ba + "\n");
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

        for (Map<CSPVariable, BinaryArc> baMap : arcs.values()) {
            for (BinaryArc ba : baMap.values()) {
                if (!ba.isConsistent()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isArcConsistent(CSPVariable past, CSPVariable current) {

        if (!arcs.containsKey(past) || !arcs.get(past).containsKey(current)) {
            return true;

        }

        return arcs.get(past).get(current).isConsistent();

    }

    public boolean isConsistent() {

        boolean consistent = true;
        for (CSPVariable first : arcs.keySet()) {
            for (CSPVariable second : arcs.get(first).keySet()) {
                BinaryArc ba = arcs.get(first).get(second);
                consistent = consistent && ba.isConsistent();

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

    public Map<CSPVariable, Map<CSPVariable, BinaryArc>> getArcs() {

        return arcs;
    }

    public Queue<BinaryArc> getArcQueue() {

        Queue<BinaryArc> q = new LinkedList<BinaryArc>();
        for (CSPVariable first : arcs.keySet()) {
            for (CSPVariable second : arcs.get(first).keySet()) {
                q.offer(arcs.get(first).get(second));
            }
        }
        return q;
    }

    public Collection<BinaryArc> getOutgoingArcs(CSPVariable currentVar) {

        List<BinaryArc> c = new ArrayList<BinaryArc>();

        if (arcs.containsKey(currentVar)) {
            c.addAll(arcs.get(currentVar).values());
        }
        return c;
    }

    public Collection<BinaryArc> getIncomingArcs(CSPVariable currentVar) {

        List<BinaryArc> c = new ArrayList<BinaryArc>();

        for (CSPVariable first : arcs.keySet()) {
            if (arcs.get(first).containsKey(currentVar)) {
                c.add(arcs.get(first).get(currentVar));
            }
        }
        return c;
    }

    public int getNoConstraints() {

        int counter = 0;
        for (CSPVariable first : arcs.keySet()) {
            counter += arcs.get(first).values().size();
        }
        //every constraint consists of 2 arcs
        return counter / 2;
    }

}
