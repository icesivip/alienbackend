package icesi.vip.alien.alien.branchAndBound;

import model.Model;
import model.Solution;
import solver.integer.branch_and_bound.BranchAndBound;
import solver.integer.branch_and_bound.BranchAndBound.ModelNode;
import solver.interior_point.BarrierMethod;

public class BranchAndBoundContainer {
	
	private Node solutionTree;
	private Solution.ExportSolution optimalSolution;
	
	public BranchAndBoundContainer(Model m) {

		try {
			BarrierMethod bm=new BarrierMethod();
			BranchAndBound bb=new BranchAndBound(bm);
			this.optimalSolution=bb.solve(m).exportFormat();
			ModelNode st=bb.giveSolutionTree();
			
			System.out.println(st.left);
			System.out.println(st.right);
			solutionTree=new Node(st);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}

	public Node getSolutionTree() {
		return solutionTree;
	}

	public Solution.ExportSolution getOptimalSolution() {
		return optimalSolution;
	}
	
	
	
	
	

}
