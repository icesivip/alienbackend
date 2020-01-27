package icesi.vip.alien.alien.branchAndBound;

import java.util.logging.Level;
import java.util.logging.Logger;

import icesi.vip.alien.modelLP.*;

public class BranchAndBound implements IntegerSolver {

	public class ModelNode {
            /**
             * This variable indicates the model that use the BranchAndBound method 
             */
		public Model model;
               /**
                * The BranchAndBound indicates the parent that use the code 
                */
		public BranchAndBound parent;
                /**
                 * This variable indicates the upper node 
                 */
		public ModelNode upper;
                /**
                 * This variable indicates the right node 
                 */
		public ModelNode right;
                /**
                 * This variable indicates de left node
                 */
		public ModelNode left;
                /**
                 * This variable of type integer integer the level of the branchAndBound
                 */
		public int level;
                /**
                 * This variable indicates de variable of the solution 
                 */
		public Solution solution;
                
                /**
                 * This builder of the modelNode, this receives the model, the parent branchAndBound, the upper node, and the level to work   
                 * @param model this indicate the model
                 * @param parent this indicates the parent that we need for the modelnode 
                 * @param upper this indicates the upper node 
                 * @param level this inidicates the level of the the model node
                 */
		public ModelNode(Model model, BranchAndBound parent, ModelNode upper, int level) {
			this.level = level;
			this.model = model;
			this.parent = parent;
			this.upper = upper;

			System.out.println("------------------------------------------------");
			System.out.println("B&B Node on Level " + level + "\n");
			System.out.println(model + "\n");

			Solution s = this.parent.solver.solve(this.model);
			this.solution = s;

			System.out.println(s + "\n" + "------------------------------------------------");
			if (s != null) {
				try {

					if (!this.isIntegerSolution(s, model)) {
						if (model.getType().equals(Model.MAXIMIZE)) {
							if (s.getZ() > parent.currentZ) {
								int var = this.chooseBranchingVariable(s);
								branchOn(var, s);
							}
						} else {
							if (s.getZ() < parent.currentZ) {
								int var = this.chooseBranchingVariable(s);
								branchOn(var, s);
							}
						}

					} else {
						if (model.getType().equals(Model.MAXIMIZE)) {
							if (s.getZ() > parent.currentZ) {
								parent.boundZ = s;
								currentZ = s.getZ();
							}
						} else {
							if (s.getZ() < parent.currentZ) {
								parent.boundZ = s;
								currentZ = s.getZ();
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Unfeasible branch.");
			}

		}
                /**
                 * This method returns if a solution is an integer solution of the brandAndBound model
                 * @param s this indicates the solution 
                 * @param m this indicates the model of the solutions
                 * @return the boolean indicates if is an integer solution  
                 */
		public boolean isIntegerSolution(Solution s, Model m) {
			boolean integer = true;
			for (int i = 0; i < m.getVariableCount() && integer; i++) {
				if (m.getVariableAt(i).getType().equals(Variable.INTEGER)) {
					try {
						integer = this.isIntegerValue(s.getVariableValue(m.getVariableAt(i)));
					} catch (Exception ex) {
						Logger.getLogger(BranchAndBound.class.getName()).log(Level.SEVERE, null, ex);
					}
				} else if (m.getVariableAt(i).getType().equals(Variable.BINARY)) {
					try {
						integer = this.isBinary(s.getVariableValue(m.getVariableAt(i)));
					} catch (Exception ex) {
						Logger.getLogger(BranchAndBound.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
			return integer;
		}
                /**
                 * This method do the branch for a variable 
                 * @param var the variable that need to branch 
                 * @param s the solution 
                 * @throws Exception throws an exception 
                 */
		public void branchOn(int var, Solution s) throws Exception {
			if (model.getVariableAt(var).getType().equals(Variable.INTEGER)) {
				double floor = Math.floor(s.getVariableValue(model.getVariableAt(var)));
				double ceil = Math.ceil(s.getVariableValue(model.getVariableAt(var)));
				Model m1 = model.copyModel();
				double[] cons1 = new double[model.getVariableCount()];
				cons1[var] = 1;
				m1.addConstraint(cons1, Constraint.LESS_OR_EQUAL, floor, "B&B Constraint");

				left = new ModelNode(m1, parent, this, level + 1);
				Model m2 = model.copyModel();
				m2.addConstraint(cons1, Constraint.GREATER_OR_EQUAL, ceil, "B&B Constraint");

				right = new ModelNode(m2, parent, this, level + 1);
			} else if (model.getVariableAt(var).getType().equals(Variable.BINARY)) {
				Model m1 = model.copyModel();
				double[] cons1 = new double[model.getVariableCount()];
				cons1[var] = 1;
				m1.addConstraint(cons1, Constraint.EQUAL, 0, "B&B Constraint");

				left = new ModelNode(m1, parent, this, level + 1);
				Model m2 = model.copyModel();
				m2.addConstraint(cons1, Constraint.EQUAL, 1, "B&B Constraint");

				right = new ModelNode(m2, parent, this, level + 1);
			}

		}
                /**
                 * Method to indicate is a integervalue
                 * @param value the value to try out
                 * @return if the value is an integer value
                 */
		public boolean isIntegerValue(double value) {
			double aproxInt = Math.rint(value);

			double lowerBound = aproxInt - parent.integerPrecision;
			double upperBound = aproxInt + parent.integerPrecision;
			return (value >= lowerBound && value <= upperBound);
		}
                /**
                 * Method to indicate is a binary value
                 * @param n value the value to try out
                 * @return if the value is an integer value
                 */
		public boolean isBinary(double n) {
			double lowerBound1 = 0 - parent.integerPrecision;
			double upperBound1 = 0 + parent.integerPrecision;
			double lowerBound2 = 1 - parent.integerPrecision;
			double upperBound2 = 1 + parent.integerPrecision;
			return (n <= upperBound1 && n >= lowerBound1) || (n <= upperBound2 && n >= lowerBound2);
		}
                /**
                 * This methos by the solution do the branching 
                 * @param s the solution of the model
                 * @return returns the branching variable
                 * @throws Exception if does not possible to chosse the branch variable 
                 */
		public int chooseBranchingVariable(Solution s) throws Exception {
			int var = -1;
			double dif = 0;
			
			for (int i = 0; i < model.getVariableCount(); i++) {
				if (model.getVariableAt(i).getType().equals(Variable.BINARY)
						&& !isBinary(s.getVariableValue(model.getVariableAt(i)))) {
					double doubleDif1 = Math.abs(s.getVariableValue(model.getVariableAt(i)) - 0);
					double doubleDif2 = Math.abs(s.getVariableValue(model.getVariableAt(i)) - 1);
					double maxDif = Math.max(doubleDif1, doubleDif2);
					if (maxDif > dif) {
						dif = maxDif;
						var = i;
					}
				}
			}
			if(var==-1) {
				for (int i = 0; i < model.getVariableCount(); i++) {

					if (model.getVariableAt(i).getType().equals(Variable.INTEGER)
							&& !isIntegerValue(s.getVariableValue(model.getVariableAt(i)))) {

						double aprox = Math.floor(s.getVariableValue(model.getVariableAt(i)));
						double candidateDif = s.getVariableValue(model.getVariableAt(i)) - aprox;
						if (candidateDif > dif) {
							dif = candidateDif;
							var = i;
						}

					}
				}
			}
			
			return var;
		}
	}
        /**
         * This is the precision of the base of the integer
         */
	public static double BASE_INTEGER_PRECISION = 0.0001;
        /**
         * this double indicartes de integer precision
         */
	protected double integerPrecision;
        /**
         * This indicates de solver 
         */
	protected Solver solver;
        /**
         * This indicates the solutions but this is the  relaxed z 
         */
	protected Solution relaxedZ;
        /**
         * This indicates the solutions of the bound z
         */
	protected Solution boundZ;
        /**
         * This indicates the double that is the current z in the branch and bound 
         */
	protected double currentZ;
        /**
         * This indicates the original model of the branch
         */
	protected Model originalModel;
        /**
         * this indicates the model node, that is the solution tree
         */
	protected ModelNode solutionTree;

        /**
         * Constructor of the branch and bound
         * @param integerPrecision this indicates the integer precision to consider
         * @param solver the solver
         */
	public BranchAndBound(double integerPrecision, Solver solver) {
		if (integerPrecision < BranchAndBound.BASE_INTEGER_PRECISION) {
			throw new IllegalArgumentException("Integer precision must be greater than 0.0001");
		}
		if (solver instanceof IntegerSolver) {
			throw new IllegalArgumentException("The specified Solver must not be instance of IntegerSolver");
		}
		this.integerPrecision = integerPrecision;
		this.solver = solver;

	}
        /**
         * This indicates the branch and bound of the solver 
         * @param solver the solver of the model
         */
	public BranchAndBound(Solver solver) {

		if (solver instanceof IntegerSolver) {
			throw new IllegalArgumentException("The specified Solver must not be instance of IntegerSolver");
		}
		this.integerPrecision = BranchAndBound.BASE_INTEGER_PRECISION;
		this.solver = solver;

	}
        /**
         * this method returns the solution tree 
         * @return solutionTree
         */
	public ModelNode giveSolutionTree() {
		return this.solutionTree;
	}
        /**
         * This method is the solve whit the method branch and bound 
         * @param model the model to solve    
         * @return the solution
         */
	@Override
	public Solution solve(Model model) {
		relaxedZ = solver.solve(model);
		this.originalModel = model;
		if (model.getType().equals(Model.MAXIMIZE)) {
			currentZ = Double.NEGATIVE_INFINITY;
		} else {
			currentZ = Double.POSITIVE_INFINITY;
		}
		solutionTree = new ModelNode(this.originalModel, this, null, 0);
		return boundZ;
	}

}
