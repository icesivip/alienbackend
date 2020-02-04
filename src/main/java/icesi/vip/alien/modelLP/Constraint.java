package icesi.vip.alien.modelLP;
import java.util.*;

public class Constraint {
	/**
	 * This static variable that indicates when it is less or equal
	 */
	public static final String LESS_OR_EQUAL = "<=";
	/**
	 * this static variable that indicates when it is greater or equal
	 */
	public static final String GREATER_OR_EQUAL = ">=";
	/**
	 * this static variable that indicates when it's the same
	 */
	public static final String EQUAL = "=";
	/**
	 * this static variable indicates the tolerance base that the restriction has
	 */
	public static final double BASE_TOLERANCE = 0.00001;
	/**
	 * this variable of type String indicates the type that handles the restriction
	 */
	private String type;
	/**
	 * This variable of type double indicates the value of the right side
	 */
	private double rightSideValue;
	/**
	 * This container of type arraylist contains the variables of variable type
	 * restriction
	 */
	private ArrayList<Variable> variables;
	/**
	 * HashMap referring to the weights, using objects of the Variable type so that
	 * this is the key to obtain a double value.
	 */
	private HashMap<Variable, Double> ponderations;
	/**
	 * refers to the double reference to tolerance
	 */
	private double tolerance;
	/**
	 * variable of type String that indicates the name of the restriction
	 */
	private String name;

	/**
	 * Indicates the constructor of the Constraint class
	 * 
	 * @param variables    Indicates the number of variables per parameter contained
	 *                     in an array of type Arratlist , number of variables and
	 *                     ponderations must be the same
	 * @param ponderations refers to the number of weightings encapsulated in a
	 *                     container of size with double values
	 * @param type         this type String indicates the type that handles the
	 *                     constraint
	 * @param rightSide    indicates the rigt side of the inequality
	 * @param name         indicates de name of the constraint
	 */
	public Constraint(ArrayList<Variable> variables, double[] ponderations, String type, double rightSide,
			String name) {
		if (variables.size() != ponderations.length) {
			throw new IllegalArgumentException("The number of Variables and ponderations must be the same.");
		}

		if (!Constraint.isValidType(type)) {
			throw new IllegalArgumentException("Type specified is not recognized as neither <=, >= or =");
		}

		this.variables = new ArrayList<>();
		this.variables.addAll(variables);
		this.ponderations = new HashMap<>();
		this.type = type;
		this.name = name;
		this.tolerance = Constraint.BASE_TOLERANCE;
		this.rightSideValue = rightSide;
		for (int i = 0; i < variables.size(); i++) {
			this.ponderations.put(variables.get(i), ponderations[i]);
		}

	}

	/**
	 * Get of the parameter name
	 * 
	 * @return name of the constraint
	 */
	public String getName() {
		return name;
	}

	/**
	 * Builder with overload one, unlike the main constructor, this has an indicated
	 * tolerance, instead of the constant indicated in the class.
	 * 
	 * @param variables    Indicates the number of variables per parameter contained
	 *                     in an array of type Arratlist , number of variables and
	 *                     ponderations must be the same
	 * @param ponderations refers to the number of weightings encapsulated in a
	 *                     container of size with double values
	 * @param type         this type String indicates the type that handles the
	 *                     constraint
	 * @param rightSide    indicates the rigt side of the inequality
	 * @param name         indicates de name of the constraint
	 * @param tolerance    indicate the tolerance
	 */
	public Constraint(ArrayList<Variable> variables, double[] ponderations, String type, double rightSide, String name,
			double tolerance) {
		if (variables.size() != ponderations.length) {
			throw new IllegalArgumentException("The number of Variables and ponderations must be the same.");
		}

		if (!Constraint.isValidType(type)) {
			throw new IllegalArgumentException("Type specified is not recognized as neither <=, >= or =");
		}

		this.variables = new ArrayList<>();
		this.variables.addAll(variables);
		this.ponderations = new HashMap<>();
		this.name = name;
		this.type = type;
		this.tolerance = tolerance;
		this.rightSideValue = rightSide;
		for (int i = 0; i < variables.size(); i++) {
			this.ponderations.put(variables.get(i), ponderations[i]);
		}

	}

	/**
	 * this method indicates if the solution passed per parameter satisfies the
	 * stipulated limits
	 * 
	 * @param s the solition that should be satisfied
	 * @return the result of the checks, mus be = inflim,or =suplim or behind this
	 *         limits
	 * @throws Exception The variables must be considered in the specified
	 *                   Solution."
	 */
	public boolean isSatisfiedBy(Solution s) throws Exception {
		double calculatedValue = 0;
		for (int i = 0; i < this.variables.size(); i++) {
			try {
				double val = s.getVariableValue(this.getVariableAt(i));
				calculatedValue += this.getVariablePonderation(i) * val;
			} catch (Exception e) {
				throw new Exception("The variable " + this.getVariableAt(i).getName()
						+ " is not considered in the specified Solution.");
			}

		}

		if (this.type.equals(Constraint.GREATER_OR_EQUAL)) {
			double infLim = this.rightSideValue - this.tolerance;
			return calculatedValue >= infLim;
		} else if (this.type.equals(Constraint.LESS_OR_EQUAL)) {
			double supLim = this.rightSideValue + this.tolerance;
			return calculatedValue <= supLim;
		} else {
			double infLim = this.rightSideValue - (this.tolerance / 2.0);
			double supLim = this.rightSideValue + (this.tolerance / 2.0);
			return calculatedValue >= infLim && calculatedValue <= supLim;
		}

	}

	/**
	 * return the variable depends by the index
	 * 
	 * @param index the parameter of the index that we need
	 * @return a Object of type Variable that it is containt in the list
	 */
	public Variable getVariableAt(int index) {
		return variables.get(index);
	}

	/**
	 * this method return the size of the variables
	 * 
	 * @return size of the variables
	 */
	public int getVariableCount() {
		return variables.size();
	}

	/**
	 * this method set the tolerance by one that we recived by parameter
	 * 
	 * @param tolerance this is the new tolerance by double
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * This method return the type by String
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * This returns the double of the variable of tolerance
	 * 
	 * @return the tolerance
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * This method return the ponderations using the key of one variable that it is
	 * in the position of one index
	 * 
	 * @param index the index that it is contains the key of the value that we
	 *              search
	 * @return the double of the variable poderation
	 */
	public double getVariablePonderation(int index) {
		return this.ponderations.get(variables.get(index));
	}

	/**
	 * this variable returns the right side value
	 * 
	 * @return the rightside value
	 */
	public double getRightSideValue() {
		return this.rightSideValue;
	}

	/**
	 * This method validate that if it is a valid type recived by parameter
	 * 
	 * @param type it is the type that need to validate
	 * @return the boolean that represents the if the value it is equals.
	 */
	public static boolean isValidType(String type) {
		return type.equals(Constraint.EQUAL) || type.equals(Constraint.LESS_OR_EQUAL)
				|| type.equals(Constraint.GREATER_OR_EQUAL);
	}

	/**
	 * ToString of the object Constraint.
	 * 
	 * @return a String that must be represent the constraint the best way that it
	 *         is posible.
	 */
	@Override
	public String toString() {
		String toString = "";
		for (int i = 0; i < this.variables.size(); i++) {
			String signum = "";
			if (this.ponderations.get(this.variables.get(i)) >= 0 && i != 0) {
				signum = " + ";
			}

			toString += signum + this.ponderations.get(this.variables.get(i)) + " " + this.variables.get(i).getName();

		}
		toString += " " + this.type;
		toString += " " + this.rightSideValue;
		return toString;
	}

}
