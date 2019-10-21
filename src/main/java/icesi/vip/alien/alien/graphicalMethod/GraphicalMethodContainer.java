package icesi.vip.alien.alien.graphicalMethod;

import java.util.ArrayList;

import icesi.vip.alien.modelLP.*;


public class GraphicalMethodContainer {

	public SimpleConstraint[] constraints;
	public Solution.ExportSolution[] solutionList;
	public Solution.ExportSolution optimalSolution;
	
	public GraphicalMethodContainer(Model m) {
		GraphicalMethod solver = new GraphicalMethod();
		ArrayList<Solution> sol = solver.listAllPosibleSolutions(m);
		Solution.ExportSolution[] solEx = new Solution.ExportSolution[sol.size()];
		for (int i = 0; i < solEx.length; i++) {
			try {
				solEx[i] = new Solution.ExportSolution(sol.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		solutionList = solEx;
		SimpleConstraint[] cons = new SimpleConstraint[m.getConstraintCount()];
		for (int i = 0; i < cons.length; i++) {
			try {
				cons[i] = new SimpleConstraint(m.getConstraintAt(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		constraints = cons;
		try {
			if(solver.solve(m)!= null) {
			optimalSolution = solver.solve(m).exportFormat();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SimpleConstraint[] getConstraints() {
		return constraints;
	}

	public Solution.ExportSolution[] getSolutionList() {
		return solutionList;
	}

	public Solution.ExportSolution getOptimalSolution() {
		return optimalSolution;
	}

}
