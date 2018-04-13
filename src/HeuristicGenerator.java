import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeuristicGenerator {

    String CSPlocation;
    String fileName;
    BinaryCSP problem;

    public HeuristicGenerator(String CSPLocation) {

        this.CSPlocation = CSPLocation;
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(CSPLocation);
        problem = reader.readBinaryCSP(CSPLocation);
        fileName = CSPlocation.substring(0, CSPlocation.lastIndexOf('.'));
    }

    public static void main(String[] args) throws IOException {

        String CSPLocation = args[0];
        HeuristicGenerator generator = new HeuristicGenerator(CSPLocation);
        Map<String, Integer> h = generator.generateRandomHeuristic();
        generator.writeHeuristicToFile(h);
    }

    public void writeHeuristicToFile(Map<String, Integer> h) throws IOException {

        File heuristicFile = new File(fileName + ".csph");
        try (FileWriter writer = new FileWriter(heuristicFile)) {
            for (String name : h.keySet()) {
                writer.write(name + "," + h.get(name) + "\n");
            }

        }

    }

    public Map<String, Integer> generateMaxDegreeHeuristic() {

        Map<String, Integer> h = new HashMap<String, Integer>();

        return h;
    }

    public Map<String, Integer> generateRandomHeuristic() {

        Map<String, Integer> h = new HashMap<String, Integer>();
        int i = 0;
        for (CSPVariable var : problem.getVars()) {
            h.put(var.getName(), i++);
        }
        return h;
    }

    public Map<String, Integer> generateNameHeuristic() {

        Map<String, Integer> h = new HashMap<String, Integer>();

        return h;
    }

}
