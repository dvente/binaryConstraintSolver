import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeuristicGenerator {

    String CSPlocation;
    String fileName;
    BinaryCSP problem;

    public HeuristicGenerator(String CSPLocation) {

        this.CSPlocation = CSPLocation;
        BinaryCSPReader reader = new BinaryCSPReader();
        problem = reader.readBinaryCSP(CSPLocation);
        fileName = CSPlocation.substring(0, CSPlocation.lastIndexOf('.'));
    }

    public static void main(String[] args) throws IOException {

        String CSPLocation = args[0];
        File input = new File(CSPLocation);
        if (input.isDirectory()) {
            File[] fileList = input.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    return name.endsWith(".csp");
                }

            });
            for (File location : fileList) {

                HeuristicGenerator generator = new HeuristicGenerator(location.getPath());
                generator.generateRandomHeuristic();

                generator = new HeuristicGenerator(location.getPath());
                generator.generateMaxDegreeHeuristic();
                generator = new HeuristicGenerator(location.getPath());
                generator.generateNameHeuristic();
            }
        } else {
            HeuristicGenerator generator = new HeuristicGenerator(CSPLocation);
            generator.generateRandomHeuristic();
            generator.generateMaxDegreeHeuristic();
            generator.generateNameHeuristic();
        }

    }

    public void writeHeuristicToFile(Map<String, Integer> h, String hName) throws IOException {

        File heuristicFile = new File(fileName + hName + ".csph");
        try (FileWriter writer = new FileWriter(heuristicFile)) {
            for (String name : h.keySet()) {
                writer.write(name + "," + h.get(name) + "\n");
            }

        }

    }

    public void generateMaxDegreeHeuristic() throws IOException {

        Map<String, Integer> h = new HashMap<String, Integer>();
        int i = 0;

        List<CSPVariable> varList = problem.getVars();
        Collections.sort(varList, new Comparator<CSPVariable>() {

            @Override
            public int compare(CSPVariable o1, CSPVariable o2) {

                return -1 * (problem.getIncomingArcs(o1).size() - problem.getIncomingArcs(o2).size());
            }

        });
        for (CSPVariable var : varList) {
            h.put(var.getName(), i++);
        }
        writeHeuristicToFile(h, "MaxDegree");
    }

    public void generateRandomHeuristic() throws IOException {

        Map<String, Integer> h = new HashMap<String, Integer>();
        int i = 0;
        List<CSPVariable> varList = problem.getVars();
        Collections.shuffle(varList);
        for (CSPVariable var : varList) {
            h.put(var.getName(), i++);
        }
        writeHeuristicToFile(h, "Random");
    }

    public void generateNameHeuristic() throws IOException {

        Map<String, Integer> h = new HashMap<String, Integer>();
        int i = 0;
        List<CSPVariable> varList = problem.getVars();
        varList.sort(new Comparator<CSPVariable>() {

            @Override
            public int compare(CSPVariable o1, CSPVariable o2) {

                return o1.getName().compareTo(o2.getName());
            }

        });
        for (CSPVariable var : varList) {
            h.put(var.getName(), i++);
        }
        writeHeuristicToFile(h, "Name");
    }

}
