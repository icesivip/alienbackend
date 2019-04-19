package icesi.vip.alien.alien.branchAndBound;

import model.Model;
import model.Solution;
import solver.integer.branch_and_bound.BranchAndBound.ModelNode;

public class Node {
	
	
	public Node[] children;
	public NodeText text;
	
	
	
	


	
	
	
	public Node[] getChildren() {
		return children;
	}









	public NodeText getText() {
		return text;
	}









	public Node(ModelNode copy) {
		try {
			
			children=new Node[2];
			
			text=new NodeText(copy.model.toString(), copy.solution.exportFormat().toString());


			
			
			
			if(copy.right==null) {
				children[1]=null;
			}else {
				children[1]=new Node(copy.right);
			}
			
			if(copy.left==null) {
				children[0]=null;
			}else {
				children[0]=new Node(copy.left);
			}
			
			
			
			
			
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}






	
	
	

}
