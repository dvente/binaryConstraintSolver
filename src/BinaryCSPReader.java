import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A reader tailored for binary extensional CSPs. It is created from a
 * FileReader and a StreamTokenizer
 */
public final class BinaryCSPReader {

    private FileReader inFR;
    private StreamTokenizer in;

    /**
     * Main (for testing)
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java BinaryCSPReader <file.csp>");
            return;
        }
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(reader.readBinaryCSP(args[0]));
    }

    /**
     * File format: <no. vars> NB vars indexed from 0 We assume that the domain
     * of all vars is specified in terms of bounds <lb>, <ub> (one per var) Then
     * the list of constraints c(<varno>, <varno>) binary tuples <domain val>,
     * <domain val>
     */
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
            Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> constraints = readBinaryConstraints(vars);
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

    /**
     * @param vars
     */
    private Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> readBinaryConstraints(CSPVariable[] vars) {

        Map<CSPVariable, Map<CSPVariable, BinaryConstraint>> constraints = new HashMap<CSPVariable, Map<CSPVariable, BinaryConstraint>>();

        try {
            in.nextToken(); //'c' or EOF
            while (in.ttype != StreamTokenizer.TT_EOF) {
                // scope
                in.nextToken(); //'('
                in.nextToken(); //var
                CSPVariable var1 = vars[(int) in.nval];
                in.nextToken(); //','
                in.nextToken(); //var
                CSPVariable var2 = vars[(int) in.nval];
                in.nextToken(); //')'

                //tuples
                Set<BinaryTuple> tuples = new HashSet<BinaryTuple>();
                in.nextToken(); //1st allowed val of 1st tuple
                while (!"c".equals(in.sval) && (in.ttype != StreamTokenizer.TT_EOF)) {
                    int val1 = (int) in.nval;
                    in.nextToken(); //','
                    in.nextToken(); //2nd val
                    int val2 = (int) in.nval;
                    tuples.add(new BinaryTuple(val1, val2));
                    in.nextToken(); //1stallowed val of next tuple/c/EOF
                }
                BinaryConstraint c = new BinaryConstraint(var1, var2, tuples);
                if (!constraints.keySet().contains(var1)) {
                    constraints.put(var1, new HashMap<CSPVariable, BinaryConstraint>());
                }
                constraints.get(var1).put(var2, c);

            }

            return constraints;
        } catch (

        IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
