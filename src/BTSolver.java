
public class BTSolver {
	
	BinaryCSP problem;
	int nodesExplored;

	public BTSolver(BinaryCSP problem) {
		super();
		this.problem = problem;
	}

	public static void main(String[] args){
		
		String CSPLocation = args[0];
		BinaryCSPReader reader = new BinaryCSPReader() ;
		System.out.println(CSPLocation);
		BTSolver solver = new BTSolver(reader.readBinaryCSP(CSPLocation));
		solver.solveCurrentProblem();
	}
	
	public void solveCurrentProblem(){
		backtrack(0);
//		System.out.println(problem.toString());
	}
	
	// adapted from lecture slides. simple backtracking
	public boolean backtrack(int depth) { 
		CSPVariable var = problem.getVar(depth);
		for(int value = var.getLowerBound(); value < var.getUpperBound(); value++) {
			var.assign(value);
			boolean consistent = true;
			for(BinaryConstraint constr : problem.getConstraints()) {
				if(!constr.isConsistent()) {
					continue;
				}
			}
			if(depth == problem.getNoVariables()-1) {
				System.out.println("Solution: ");
				System.out.println(problem.toString());
				return true;
			} else {
				return backtrack(depth+1);
			}

		}
		return false;
	}

}
