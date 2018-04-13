
public class HeuristicGenerator {

	public static void main(String[] args) {
		String CSPLocation = args[0];
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(CSPLocation);
        FCSolver solver = new FCSolver(reader.readBinaryCSP(CSPLocation));

	}

}
