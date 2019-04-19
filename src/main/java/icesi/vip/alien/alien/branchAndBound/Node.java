package icesi.vip.alien.alien.branchAndBound;

import java.util.ArrayList;

import model.Model;
import model.Solution;
import solver.integer.branch_and_bound.BranchAndBound.ModelNode;

public class Node {
	
	
	public ArrayList<Node> children;
	public NodeText text;
	
	
	
	


	
	
	
	public ArrayList<Node> getChildren() {
		return children;
	}









	public NodeText getText() {
		return text;
	}









	public Node(ModelNode copy) {
		try {
			
			children=new ArrayList<Node>();
			
			text=new NodeText("Solution at Level "+copy.level, copy.model.toString(),copy.solution.toString());


			
			if(copy.left!=null) {
				children.add(new Node(copy.left));
				
			}
			
			if(copy.right!=null) {
				children.add(new Node(copy.right));
			}
			

			
			
			
			
			
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}






	
	
	

}
