
/*
 * @author 170008773
 */
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class HeuristicGenerator.
 */
public class HeuristicGenerator {

	/** The CSP location. */
	String CSPlocation;

	/** The file name of the CSP problem. */
	String fileName;

	/** The CSP. */
	BinaryCSP problem;

	/**
	 * Instantiates a new heuristic generator.
	 *
	 * @param CSPLocation
	 *            the CSP location
	 */
	public HeuristicGenerator(String CSPLocation) {

		this.CSPlocation = CSPLocation;
		BinaryCSPReader reader = new BinaryCSPReader();
		problem = reader.readBinaryCSP(CSPLocation);
		fileName = CSPlocation.substring(0, CSPlocation.lastIndexOf('.'));
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
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

				generator = new HeuristicGenerator(location.getPath());
				generator.generateMaxDegreeHeuristic();
				generator = new HeuristicGenerator(location.getPath());
				generator.generateNameHeuristic();
			}
		} else {
			HeuristicGenerator generator = new HeuristicGenerator(CSPLocation);
			generator.generateMaxDegreeHeuristic();
			generator.generateNameHeuristic();
		}

	}

	/**
	 * Write heuristic to file.
	 *
	 * @param h
	 *            the heuristic to be written to file
	 * @param hName
	 *            the name of the heuristic
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeHeuristicToFile(Map<String, Integer> h, String hName) throws IOException {

		File heuristicFile = new File(fileName + hName + ".csph");
		try (FileWriter writer = new FileWriter(heuristicFile)) {
			for (String name : h.keySet()) {
				writer.write(name + "," + h.get(name) + "\n");
			}

		}

	}

	/**
	 * Generate max degree heuristic.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void generateMaxDegreeHeuristic() throws IOException {

		Map<String, Integer> h = new HashMap<String, Integer>();
		int i = 0;

		List<CSPVariable> varList = problem.getVars();

		// sort the variable list according to the heuritic
		Collections.sort(varList, new Comparator<CSPVariable>() {

			@Override
			public int compare(CSPVariable o1, CSPVariable o2) {
				// Doesn't matter whether we pick incoming or outgoing arcs
				// since the number will be equal
				return problem.getIncomingArcs(o1).size() - problem.getIncomingArcs(o2).size();
			}

		});

		// record the order
		for (CSPVariable var : varList) {
			h.put(var.getName(), i++);
		}

		writeHeuristicToFile(h, "MaxDegree");
	}

	/**
	 * Generate lexographic heuristic.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void generateNameHeuristic() throws IOException {

		Map<String, Integer> h = new HashMap<String, Integer>();
		int i = 0;
		List<CSPVariable> varList = problem.getVars();
		// sort the variable list according to the heuritic
		varList.sort(new Comparator<CSPVariable>() {

			@Override
			public int compare(CSPVariable o1, CSPVariable o2) {

				return o1.getName().compareTo(o2.getName());
			}

		});

		// record the orders
		for (CSPVariable var : varList) {
			h.put(var.getName(), i++);
		}

		writeHeuristicToFile(h, "Name");
	}

}
