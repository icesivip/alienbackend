package icesi.vip.alien.alien.graphicalMethod;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import icesi.vip.alien.modelLP.*;

public class GraphicalMethod implements Solver {

    /**
     * This method returns the solutions of the model
     * @param model the model to take the solution
     * @return the solution of the model solved by graphicalmethod
     */
	@Override
	public Solution solve(Model model) {
		if (model.getVariableCount() != 2) {
			throw new IllegalArgumentException("Graphical solving is only posible with 2 Variable Models.");
		} else {
			ArrayList<Solution> candidateSolutions = this.listAllCandidateSolutions(model);
			if (candidateSolutions.isEmpty()) {
				return null;
			} else {
				if (model.getType().equals(Model.MAXIMIZE)) {
					Solution max = null;
					double maxZ = Double.NEGATIVE_INFINITY;
					for (int i = 0; i < candidateSolutions.size(); i++) {
						if (candidateSolutions.get(i).getZ() > maxZ) {
							maxZ = candidateSolutions.get(i).getZ();
							max = candidateSolutions.get(i);
						}
					}
					return max;
				} else {
					Solution min = null;
					double minZ = Double.POSITIVE_INFINITY;
					for (int i = 0; i < candidateSolutions.size(); i++) {
						if (candidateSolutions.get(i).getZ() < minZ) {
							minZ = candidateSolutions.get(i).getZ();
							min = candidateSolutions.get(i);
						}
					}
					return min;
				}
			}

		}

	}
/**
 * This method list all the candidate to be a solutions 
 * @param model the model 
 * @return array of the possible solutions
 */
	public ArrayList<Solution> listAllCandidateSolutions(Model model) {
		if (model.getVariableCount() != 2) {
			throw new IllegalArgumentException("Graphical solving is only posible with 2 Variable Models.");
		} else {
			ArrayList<Solution> candidateSolutions = new ArrayList<>();
			candidateSolutions.add(new Solution(model, new double[] { 0, 0 }));
			for (int i = 0; i < model.getConstraintCount(); i++) {
				candidateSolutions.addAll(this.giveAxisIntersection(model.getConstraintAt(i), model));
			}

			for (int i = 0; i < model.getConstraintCount(); i++) {
				for (int j = i + 1; j < model.getConstraintCount(); j++) {
					Solution s = this.giveConstraintIntersection(model.getConstraintAt(i), model.getConstraintAt(j),
							model);
					// System.out.println(model.getConstraintAt(i).getName()+" X
					// "+model.getConstraintAt(j).getName()+"="+s);
					if (s != null) {
						candidateSolutions.add(s);
					}
				}
			}

			for (int i = 0; i < candidateSolutions.size(); i++) {
				try {
					if (!model.isFeasibleSolution(candidateSolutions.get(i))) {
						candidateSolutions.remove(i);
						i--;
					}
				} catch (Exception ex) {
					Logger.getLogger(GraphicalMethod.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

//             for (int i = 0; i < candidateSolutions.size(); i++) {
//                try {
//                    System.out.println(candidateSolutions.get(i).getVariableValue(model.getVariableAt(0))+" ; "+candidateSolutions.get(i).getVariableValue(model.getVariableAt(1)));
//                } catch (Exception ex) {
//                    Logger.getLogger(GraphicalMethod.class.getName()).log(Level.SEVERE, null, ex);
//                }
//             }
			return candidateSolutions;
		}
	}
	
	/**
         * This method list all the candidate to be a solutions 
         * @param model the model 
         * @return array of the possible solutions
         */
	public ArrayList<Solution> listAllPosibleSolutions(Model model) {
		if (model.getVariableCount() != 2) {
			throw new IllegalArgumentException("Graphical solving is only posible with 2 Variable Models.");
		} else {
			ArrayList<Solution> candidateSolutions = new ArrayList<>();
			candidateSolutions.add(new Solution(model, new double[] { 0, 0 }));
			for (int i = 0; i < model.getConstraintCount(); i++) {
				candidateSolutions.addAll(this.giveAxisIntersection(model.getConstraintAt(i), model));
			}

			for (int i = 0; i < model.getConstraintCount(); i++) {
				for (int j = i + 1; j < model.getConstraintCount(); j++) {
					Solution s = this.giveConstraintIntersection(model.getConstraintAt(i), model.getConstraintAt(j),
							model);
					// System.out.println(model.getConstraintAt(i).getName()+" X
					// "+model.getConstraintAt(j).getName()+"="+s);
					if (s != null) {
						candidateSolutions.add(s);
					}
				}
			}



//             for (int i = 0; i < candidateSolutions.size(); i++) {
//                try {
//                    System.out.println(candidateSolutions.get(i).getVariableValue(model.getVariableAt(0))+" ; "+candidateSolutions.get(i).getVariableValue(model.getVariableAt(1)));
//                } catch (Exception ex) {
//                    Logger.getLogger(GraphicalMethod.class.getName()).log(Level.SEVERE, null, ex);
//                }
//             }
			return candidateSolutions;
		}
	}
        /**
         * This method give the axis intersection,made the array of solutions 
         * @param c the constraint c 
         * @param m the model 
         * @return array of solutions
         */
	private ArrayList<Solution> giveAxisIntersection(Constraint c, Model m) {
		double y = c.getRightSideValue() / c.getVariablePonderation(1);
		double x = c.getRightSideValue() / c.getVariablePonderation(0);
		ArrayList<Solution> cuts = new ArrayList<Solution>();
		if (!(Double.isInfinite(y) || Double.isNaN(y))) {

			cuts.add(new Solution(m, new double[] { 0, y }));
		}

		if (!(Double.isInfinite(x) || Double.isNaN(x))) {

			cuts.add(new Solution(m, new double[] { x, 0 }));
		}
		return cuts;
	}
        /**
         * This method give the constraint intersection,made the array of solutions 
         * @param c1 the constraint one
         * @param c2 the constraint two
         * @param m the model 
         * @return the solution
         */
	private Solution giveConstraintIntersection(Constraint c1, Constraint c2, Model m) {
		// System.out.println(c1.getName()+" X "+c2.getName());
		double x;
		double y;

		x = ((c2.getRightSideValue() * c1.getVariablePonderation(1))
				- (c2.getVariablePonderation(1) * c1.getRightSideValue()))
				/ ((c1.getVariablePonderation(1) * c2.getVariablePonderation(0))
						- (c2.getVariablePonderation(1) * c1.getVariablePonderation(0)));
		y = ((c1.getRightSideValue() * c2.getVariablePonderation(0))
				- (c1.getVariablePonderation(0) * c2.getRightSideValue()))
				/ ((c2.getVariablePonderation(0) * c1.getVariablePonderation(1))
						- (c2.getVariablePonderation(1) * c1.getVariablePonderation(0)));

		// System.out.println("x="+x+" y="+y);
		if (Double.isInfinite(x) || Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(y)) {
			return null;
		} else {

			return new Solution(m, new double[] { x, y });
		}
	}

}
