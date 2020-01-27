package icesi.vip.alien.alien.interiorPoint;

import java.util.ArrayList;

import icesi.vip.alien.alien.graphicalMethod.GraphicalMethod;
import icesi.vip.alien.alien.graphicalMethod.SimpleConstraint;
import icesi.vip.alien.modelLP.*;

public class InteriorPointContainer {

	private Solution.ExportSolution[] solutionPath;
	private double maxX;
	private double maxY;
	private SimpleConstraint[]constraints;
	
	
	
	public InteriorPointContainer(Model m) throws Exception {
		maxX=0;
		maxY=0;
		BarrierMethod bm=new BarrierMethod();
		ArrayList<Solution>solPath=bm.giveSolutionPath(m);
		solutionPath=new Solution.ExportSolution[solPath.size()];
		for (int i = 0; i < solPath.size(); i++) {
			solutionPath[i]=solPath.get(i).exportFormat();
		}
		if(m.getVariableCount()==2) {
			GraphicalMethod gm=new GraphicalMethod();
			ArrayList<Solution>sol=gm.listAllPosibleSolutions(m);
			System.out.println(sol);
			for (int i = 0; i < sol.size(); i++) {
				if(sol.get(i).getVariableValue(m.getVariableAt(0))<0 || sol.get(i).getVariableValue(m.getVariableAt(1))<0) {
					System.out.println("Removing solution:"+sol.get(i));
					sol.remove(i);
					i--;
				}
			}
			System.out.println(sol);
			for (int i = 0; i < sol.size(); i++) {
				System.out.println(i);
				if(sol.get(i).getVariableValue(m.getVariableAt(0))>maxX ) {
					System.out.println(maxX+"<"+sol.get(i).getVariableValue(m.getVariableAt(0)));
					maxX=sol.get(i).getVariableValue(m.getVariableAt(0));
				}
				if(sol.get(i).getVariableValue(m.getVariableAt(1))>maxY ) {
					System.out.println(maxY+"<"+sol.get(i).getVariableValue(m.getVariableAt(1)));
					maxY=sol.get(i).getVariableValue(m.getVariableAt(1));
				}
			}

			constraints=new SimpleConstraint[m.getConstraintCount()];
			for (int i = 0; i < m.getConstraintCount(); i++) {
				constraints[i]=new SimpleConstraint(m.getConstraintAt(i));
			}
		}
		
		
	}



	public Solution.ExportSolution[] getSolutionPath() {
		return solutionPath;
	}



	public double getMaxX() {
		return maxX;
	}



	public double getMaxY() {
		return maxY;
	}



	public SimpleConstraint[] getConstraints() {
		return constraints;
	}
	
	
	
	
	
	
}
