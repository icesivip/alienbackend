package icesi.vip.alien.alien.interiorPoint;

import java.util.ArrayList;
import java.util.Iterator;

import icesi.vip.alien.alien.graphicalMethod.SimpleConstraint;
import model.*;
import model.Solution;
import solver.graphical_method.GraphicalMethod;
import solver.interior_point.BarrierMethod;

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
			ArrayList<Solution>sol=gm.listAllCandidateSolutions(m);
			for (int i = 0; i < sol.size(); i++) {
				if(sol.get(i).getVariableValue(m.getVariableAt(0))<0 || sol.get(i).getVariableValue(m.getVariableAt(1))<0) {
					sol.remove(i);
					
				}
			}
			
			for (int i = 0; i < sol.size(); i++) {
				if(sol.get(i).getVariableValue(m.getVariableAt(0))>maxX ) {
					maxX=sol.get(i).getVariableValue(m.getVariableAt(0));
				}
				if(sol.get(i).getVariableValue(m.getVariableAt(1))>maxY ) {
					maxY=sol.get(i).getVariableValue(m.getVariableAt(1));
				}
			}

			constraints=new SimpleConstraint[m.getConstraintCount()];
			for (int i = 0; i < m.getConstraintCount(); i++) {
				constraints[i]=new SimpleConstraint(m.getConstraintAt(i));
			}
		}
		
		
	}
	
	
	
	
	
	
}
