package icesi.vip.alien.modelLP;


import java.util.*;

public class Model {
	/**
	 * this constant indicates the type of process that the model will perform, in
	 * this case this constant indicates the process of maximize
	 */
	public static final String MAXIMIZE = "MAXIMIZE";
	/**
	 * this constant indicates the type of process that the model will perform, in
	 * this case this constant indicates the process of minimize
	 */
	public static final String MINIMIZE = "MINIMIZE";
	/**
	 * This container of type arraylist contains the variables of variable type
	 * restriction
	 */
	private ArrayList<Variable> variables;
	/**
	 * HashMap referring to the weights, using objects of the Variable type so that
	 * this is the key to obtain a double value.
	 */
	private HashMap<Variable, Double> objectiveFunctionWeights;
	/**
	 * this variable of type String indicates the type that handles the restriction
	 */
	private String type;
	/**
	 * This container type varibale contains elements of type object constraints
	 */
	private ArrayList<Constraint> constraints;

	/**
	 * This is the method that return a copu of the complete model.
	 * 
	 * @return Actually model
	 * @throws Exception the Exception if does not have the rigth way
	 */
	public Model copyModel() throws Exception {

		Model m = new Model(this.getType());
		for (int i = 0; i < this.getVariableCount(); i++) {
			m.addVariable(this.getVariableAt(i).getName(), this.getVariableAt(i).getType(), this.getVariableWeight(i));
		}
		for (int i = 0; i < this.getConstraintCount(); i++) {
			double[] weights = new double[this.getVariableCount()];
			for (int j = 0; j < this.getConstraintAt(i).getVariableCount(); j++) {
				weights[j] = this.getConstraintAt(i).getVariablePonderation(j);
			}
			m.addConstraint(weights, this.getConstraintAt(i).getType(), this.getConstraintAt(i).getRightSideValue(),
					this.getConstraintAt(i).getName(), this.getConstraintAt(i).getTolerance());
		}

		return m;

	}

	/**
	 * This is the constructor of the Model
	 * 
	 * @param type it is the type that initialize the model, has been two options,
	 *             MAXIMIZE OR MINIMIZE
	 */
	public Model(String type) {
		if (!Model.isValidType(type)) {
			throw new IllegalArgumentException("Model type must be either MAXIMIZE or MINIMIZE");
		}
		this.type = type;
		this.constraints = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.objectiveFunctionWeights = new HashMap<>();
	}

	/**
	 * is the constructor of the model of overload 1, taking into account the
	 * variables with their respective weight
	 * 
	 * @param variables Indicates the number of variables per parameter contained in
	 *                  an array, number of variables and ponderations must be the
	 *                  same
	 * @param weights   Indicates the weight of the variables of the previous
	 *                  parameter, it must have the same number of weights as of
	 *                  variables, that saved in a double type array
	 * @param type      it is the type that initialize the model, has been two
	 *                  options, MAXIMIZE OR MINIMIZE
	 */
	public Model(Variable[] variables, double[] weights, String type) {
		if (variables.length != weights.length) {
			throw new IllegalArgumentException("Variable and weight number must be the same.");
		}
		if (!Model.isValidType(type)) {
			throw new IllegalArgumentException("Model type must be either MAXIMIZE or MINIMIZE");
		}
		this.type = type;
		this.constraints = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.objectiveFunctionWeights = new HashMap<>();
		for (int i = 0; i < variables.length; i++) {
			this.variables.add(variables[i]);
			this.objectiveFunctionWeights.put(variables[i], weights[i]);
		}
	}

	/**
	 * it is the get of the variable type
	 * 
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * This method returns the size of the variables that it is in the array
	 * @return number of variables
	 */
	public int getVariableCount() {
		return this.variables.size();
	}

	/**
	 * this method returns a varible that it is especificate by name
	 * 
	 * @param index the index where it is contains the variable that we need
	 * @return the searched variable
	 */
	public Variable getVariableAt(int index) {
		return this.variables.get(index);
	}

	/**
	 * indicates the weight of the variable that you want to search
	 * 
	 * @param index the index of the position of the variable to which you want to
	 *              obtain the weight
	 * @return the weight of the variable to look for
	 */
	public double getVariableWeight(int index) {
		return this.objectiveFunctionWeights.get(this.variables.get(index));
	}

	/**
	 * Method that validates if the type is between any of the two specified
	 * statistic variables
	 * 
	 * @param type the type that needs to be validated
	 * @return a boolean with the return of validation
	 */
	public static boolean isValidType(String type) {
		return type.equals(Model.MAXIMIZE) || type.equals(Model.MINIMIZE);
	}

	/**
	 * Method to add a variable to the list
	 * 
	 * @param name   the name of the variable to add
	 * @param type   the type of the variable to add
	 * @param weight the weight of the variable to add
	 * @throws Exception if there are Constraints in the model
	 */
	public void addVariable(String name, String type, double weight) throws Exception {
		if (constraints.isEmpty()) {
			Variable v = new Variable(name, type);
			this.variables.add(v);
			this.objectiveFunctionWeights.put(v, weight);
		} else {
			throw new Exception("New Variables can not be created if there are Constraints in the model.");
		}

	}

	/**
	 * Method that returns the constraint number that is currently in the model
	 * 
	 * @return a number that represent the number of constraints that it is in the
	 *         model
	 */
	public int getConstraintCount() {
		return this.constraints.size();
	}

	/**
	 * Method to get a constraint from a specified index
	 * 
	 * @param index specific index
	 * @return a constraint that we searched
	 */
	public Constraint getConstraintAt(int index) {
		return this.constraints.get(index);
	}

	/**
	 * Method to add a new constraint to the model
	 * 
	 * @param ConstraintWeights specifies the weight of the constraint
	 * @param type              specifies the type of the constraint
	 * @param rightSideValue    specifies the rightSideValue of the constraint
	 * @param name              indicates the name of the constraint
	 */
	public void addConstraint(double[] ConstraintWeights, String type, double rightSideValue, String name) {
		Constraint c = new Constraint(variables, ConstraintWeights, type, rightSideValue, name);
		this.constraints.add(c);
	}

	/**
	 * Method to add a new constraint to the model overload that differs whith the
	 * other thaht contains another parameter called tolerance
	 * 
	 * @param ConstraintWeights specifies the weight of the constraint
	 * @param type              specifies the type of the constraint
	 * @param rightSideValue    specifies the rightSideValue of the constraint
	 * @param name              indicates the name of the constraint
	 * @param tolerance         indicates the tolerance that has been the constraint
	 */
	public void addConstraint(double[] ConstraintWeights, String type, double rightSideValue, String name,
			double tolerance) {
		Constraint c = new Constraint(variables, ConstraintWeights, type, rightSideValue, name, tolerance);
		this.constraints.add(c);
	}

	/**
	 * Method that is used to calculate the objective function of a given solution
	 * 
	 * @param s indicates the solution, which contains the variables to which you
	 *          will find the target value
	 * @return a double with the result of the target value
	 * @throws Exception if the variables weight it is not right 
	 */
	public double calculateObjectiveFunctionValue(Solution s) throws Exception {
		double z = 0;
		for (int i = 0; i < this.variables.size(); i++) {
			z += this.getVariableWeight(i) * s.getVariableValue(this.variables.get(i));
		}
		return z;
	}

	/**
	 * This method indicates if the function is feasible according to a solution
	 * received by parameter
	 * 
	 * @param s s with the solution to which the method will be applied
	 * @return a boolean indicating whether the solution is feasible or not
	 * @throws Exception in case the possible answers of the isSatisfiedBy method
	 *                   are not taken into account
	 */
	public boolean isFeasibleSolution(Solution s) throws Exception {
		boolean feasible = true;
		for (int i = 0; i < this.constraints.size() && feasible; i++) {
			if (!this.constraints.get(i).isSatisfiedBy(s)) {
				feasible = false;
			}
		}
		return feasible;
	}

	/**
	 * Method tostring of the class
	 * 
	 * @return String that indicates the class convert in a variable String
	 */
	@Override
	public String toString() {
		String toString = type + " ";
		for (int i = 0; i < this.variables.size(); i++) {
			String signum = "";
			if (this.getVariableWeight(i) >= 0 && i != 0) {
				signum = " + ";
			}
			toString += signum + this.getVariableWeight(i) + " " + this.getVariableAt(i).getName();
		}
		toString += "\nSubject to:";
		for (int i = 0; i < this.constraints.size(); i++) {
			toString += "\n" + this.constraints.get(i).toString();
		}
		for (int i = 0; i < this.variables.size(); i++) {
			if (this.variables.get(i).getType().equals(Variable.BINARY)) {
				toString += "\n" + this.variables.get(i).getName() + " Binary";
			} else if (this.variables.get(i).getType().equals(Variable.INTEGER)) {
				toString += "\n" + this.variables.get(i).getName() + " Integer";
			}
		}
		return toString;
	}

//    public static void main(String[] args) throws Exception {
//        Model m = new Model(new Variable[]{new Variable("X1", Variable.INTEGER), new Variable("X2", Variable.CONTINUOUS)}, new double[]{5, 1}, MAXIMIZE);
//        m.addVariable("X3", Variable.BINARY, 6);
//        m.addConstraint(new double[]{1, 1, 1}, Constraint.LESS_OR_EQUAL, 12, "C1");
//        m.addConstraint(new double[]{1, 0, 0}, Constraint.GREATER_OR_EQUAL, 6, "C2");
//        m.addConstraint(new double[]{0, 1, 0}, Constraint.LESS_OR_EQUAL, 3, "C3");
//        m.addConstraint(new double[]{0, 1, 0}, Constraint.EQUAL, 2, "C4");
//
//        System.out.println(m.toString());
//
//    }

}
