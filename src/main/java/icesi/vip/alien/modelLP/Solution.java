package icesi.vip.alien.modelLP;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solution {
	/**
	 * Attribute of type HasMap that stores variables of type double from an Object
	 * key Variable
	 */
	private HashMap<Variable, Double> valueMap;
	/**
	 * Atritube that indicates the model
	 */
	private Model model;
	/**
	 * Attribute of type doubel that indicates the variable Z of the solution
	 */
	private double Z;

	public static class ExportSolution {
		public double z;
		public HashMap<String, Double> variables;
		public boolean feasible;

		public ExportSolution(Solution s) throws Exception {
			this.z = s.Z;
			variables = new HashMap<>();
			for (Variable var : s.valueMap.keySet()) {
				variables.put(var.getName(), s.getVariableValue(var));
			}
			feasible = s.model.isFeasibleSolution(s);
		}

	}

	/**
	 * 
	 * @return Method that returns a new solution with the same variables as
	 *         HashMap, taking into account the same variables and names
	 * @throws Exception if is not possible to export the solution
	 */
	public Solution.ExportSolution exportFormat() throws Exception {

		return new Solution.ExportSolution(this);

	}

	/**
	 * Solution builder
	 * 
	 * @param model  indicates the model of the solution
	 * @param values the values for the variables
	 */
	public Solution(Model model, double[] values) {
		if (model.getVariableCount() != values.length) {
			throw new IllegalArgumentException(
					"The Variable count in the specified Model must be equals to values.length");
		}
		this.model = model;
		valueMap = new HashMap<>();
		for (int i = 0; i < model.getVariableCount(); i++) {
			valueMap.put(model.getVariableAt(i), values[i]);
		}
		try {
			Z = this.getObjectiveFunctionValue();
		} catch (Exception ex) {
			Logger.getLogger(Solution.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Method get of the variable z
	 * 
	 * @return z
	 */
	public double getZ() {
		return Z;
	}

	/**
	 * method that returns the value from a key specified by parameter
	 * 
	 * @param v the key of the value that we search
	 * @return the double that indicates de value in the hasmap
	 * @throws Exception Specified Variable is not contained in Solution
	 */
	public double getVariableValue(Variable v) throws Exception {
		if (this.valueMap.containsKey(v)) {
			return this.valueMap.get(v);
		} else {
			throw new Exception("Specified Variable is not contained in Solution.");
		}

	}

	/**
	 * method for exchanging the value of a variable, for one received per
	 * parameter. you need to specify the key or the variable to which the value
	 * will be changed
	 * 
	 * @param v        the value of the key
	 * @param newValue the new value
	 * @throws Exception Specified Variable is not contained in Solution.
	 */
	public void setVariableValue(Variable v, double newValue) throws Exception {
		if (this.valueMap.containsKey(v)) {
			this.valueMap.replace(v, newValue);
			try {
				Z = this.getObjectiveFunctionValue();
			} catch (Exception ex) {
				Logger.getLogger(Solution.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			throw new Exception("Specified Variable is not contained in Solution.");
		}
	}

	/**
	 * method to obtain the value of the objective function of the solution
	 * 
	 * @return value of the objective function of the solution
	 * @throws Exception if is no possible to calculate the objective function value
	 */
	public double getObjectiveFunctionValue() throws Exception {
		return this.model.calculateObjectiveFunctionValue(this);
	}

	/**
	 * Method toString of the class Solution
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		String toString = "";
		try {
			if (model.isFeasibleSolution(this)) {
				toString = "Solution (feasible):\n";
			} else {
				toString = "Solution (unfeasible):\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < model.getVariableCount(); i++) {
				toString += (model.getVariableAt(i).getName() + " = " + this.getVariableValue(model.getVariableAt(i))
						+ "\n");
			}
			toString += "Z = " + this.getObjectiveFunctionValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toString;
	}

}
