package icesi.vip.alien.alien.branchAndBound;

import icesi.vip.alien.alien.branchAndBound.BranchAndBound.ModelNode;
import icesi.vip.alien.alien.interiorPoint.BarrierMethod;
import icesi.vip.alien.modelLP.Model;
import icesi.vip.alien.modelLP.Solution;

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
