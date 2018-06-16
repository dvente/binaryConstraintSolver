
/*
 * @author Ian Miguel ijm@st-andrews.ac.uk
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A reader tailored for binary extensional CSPs. It is created from a
 * FileReader and a StreamTokenizer. Only slightly addapted
 */
public final class BinaryCSPReader {

    private FileReader inFR;
    private StreamTokenizer in;
    private Map<String, Integer> heuristicOrdering;

    /**
     * Main (for testing)
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java BinaryCSPReader <file.csp>");
            return;
        }
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(reader.readBinaryCSP(args[0], args[1]));
    }

    /**
     * File format: <no. vars> NB vars indexed from 0 We assume that the domain
     * of all vars is specified in terms of bounds <lb>, <ub> (one per var) Then
     * the list of constraints c(<varno>, <varno>) binary tuples <domain val>,
     * <domain val>
     */
    public BinaryCSP readBinaryCSP(String fn, String heuristicLocation) {

        try {
            heuristicOrdering = readHeuristic(heuristicLocation);
            inFR = new FileReader(fn);
            in = new StreamTokenizer(inFR);
            in.ordinaryChar('(');
            in.ordinaryChar(')');
            in.nextToken(); // n
            int n = (int) in.nval;
            CSPVariable[] vars = new CSPVariable[n];
            for (int i = 0; i < n; i++) {
                in.nextToken(); // ith ub
                int lower = (int) in.nval;
                in.nextToken(); // ','
                in.nextToken();
                int upper = (int) in.nval;
                vars[i] = new CSPVariable(Integer.toString(i), heuristicOrdering.get(Integer.toString(i)), lower,
                        upper);
            }
            Map<CSPVariable, Map<CSPVariable, BinaryArc>> constraints = readBinaryConstraints(vars);
            BinaryCSP csp = new BinaryCSP(vars, constraints);
            // TESTING:
            // System.out.println(csp) ;
            inFR.close();
            return csp;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public BinaryCSP readBinaryCSP(String fn) {

        try {
            inFR = new FileReader(fn);
            in = new StreamTokenizer(inFR);
            in.ordinaryChar('(');
            in.ordinaryChar(')');
            in.nextToken(); // n
            int n = (int) in.nval;
            CSPVariable[] vars = new CSPVariable[n];
            for (int i = 0; i < n; i++) {
                in.nextToken(); // ith ub
                int lower = (int) in.nval;
                in.nextToken(); // ','
                in.nextToken();
                int upper = (int) in.nval;
                vars[i] = new CSPVariable(Integer.toString(i), lower, upper);
            }
            Map<CSPVariable, Map<CSPVariable, BinaryArc>> constraints = readBinaryConstraints(vars);
            BinaryCSP csp = new BinaryCSP(vars, constraints);
            // TESTING:
            // System.out.println(csp) ;
            inFR.close();
            return csp;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    private Map<String, Integer> readHeuristic(String heuristicLocation) throws FileNotFoundException, IOException {

        Map<String, Integer> h = new HashMap<String, Integer>();
        File heuristicFile = new File(heuristicLocation);
        try (FileReader reader = new FileReader(heuristicFile);
                BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String name = line.substring(0, line.lastIndexOf(","));
                int order = Integer.parseInt(line.substring(line.lastIndexOf(",") + 1, line.length()));
                h.put(name, order);

            }

        }
        return h;
    }

    /**
     * @param vars
     */
    private Map<CSPVariable, Map<CSPVariable, BinaryArc>> readBinaryConstraints(CSPVariable[] vars) {

        Map<CSPVariable, Map<CSPVariable, BinaryArc>> constraints = new HashMap<CSPVariable, Map<CSPVariable, BinaryArc>>();

        try {
            in.nextToken(); // 'c' or EOF
            while (in.ttype != StreamTokenizer.TT_EOF) {
                // scope
                in.nextToken(); // '('
                in.nextToken(); // var
                CSPVariable var1 = vars[(int) in.nval];
                in.nextToken(); // ','
                in.nextToken(); // var
                CSPVariable var2 = vars[(int) in.nval];
                in.nextToken(); // ')'

                // tuples
                Set<BinaryTuple> tuples = new HashSet<BinaryTuple>();
                in.nextToken(); // 1st allowed val of 1st tuple
                while (!"c".equals(in.sval) && (in.ttype != StreamTokenizer.TT_EOF)) {
                    int val1 = (int) in.nval;
                    in.nextToken(); // ','
                    in.nextToken(); // 2nd val
                    int val2 = (int) in.nval;
                    tuples.add(new BinaryTuple(val1, val2));
                    in.nextToken(); // 1stallowed val of next tuple/c/EOF
                }
                BinaryArc c = new BinaryArc(var1, var2, tuples);
                if (!constraints.containsKey(var1)) {
                    constraints.put(var1, new HashMap<CSPVariable, BinaryArc>());
                }
                constraints.get(var1).put(var2, c);

                c = c.reverse();
                if (!constraints.containsKey(var2)) {
                    constraints.put(var2, new HashMap<CSPVariable, BinaryArc>());
                }
                constraints.get(var2).put(var1, c);

            }

            return constraints;
        } catch (

        IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
