package icesi.vip.alien.modelLP;


public class Variable {
	/**
	 * Parameter of the class that represents the name of the Variable
	 */
	private String name;
	/**
	 * Parameter of the class that represents the type of the Variable
	 */
	private String type;
	/**
	 * Static variable that indicates the type of variable to which it belongs in
	 * this moments represents a variable continuous
	 */
	public static final String CONTINUOUS = "C";
	/**
	 * Static variable that indicates the type of variable to which it belongs in
	 * this moments represents a variable integer
	 */
	public static final String INTEGER = "I";
	/**
	 * Static variable that indicates the type of variable to which it belongs in
	 * this moments represents a variable binary
	 */
	public static final String BINARY = "B";

	/**
	 * Variable constructor
	 * 
	 * @param name the name of the Variable
	 * @param type the type of the Variable
	 */
	public Variable(String name, String type) {
		if (type.equals(Variable.CONTINUOUS) || type.equals(Variable.INTEGER) || type.equals(Variable.BINARY)) {
			this.name = name;
			this.type = type;
		} else {
			throw new IllegalArgumentException("Variable type not recognized.");
		}
	}

	/**
	 * Get method of the variable name
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get method of the variable type
	 * 
	 * @return type
	 */
	public String getType() {
		return this.type;
	}
}
