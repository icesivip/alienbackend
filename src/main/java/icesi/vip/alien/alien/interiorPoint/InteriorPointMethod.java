package icesi.vip.alien.alien.interiorPoint;


import java.util.HashMap;
import icesi.vip.alien.modelLP.*;

public abstract class InteriorPointMethod implements Solver {
    /**
     * This method represents the method that returns the array os double that represents the B, this model need to stay in the right form
     * @param m the model
     * @return array of numbers of type double
     */
	protected static double[] getB(Model m) {
		if (isModelInRightForm(m)) {
			double[] b = new double[m.getConstraintCount()];
			for (int i = 0; i < m.getConstraintCount(); i++) {
				b[i] = m.getConstraintAt(i).getRightSideValue();
			}
			return b;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}
        /**
         * This method represents the method that returns the array os double that represents the C, this model need to stay in the right form
         * @param m the model
         * @return array of numbers of type double
         */
	protected static double[] getC(Model m) {
		if (isModelInRightForm(m)) {
			double[] c = new double[m.getVariableCount()];
			for (int i = 0; i < m.getVariableCount(); i++) {
				c[i] = m.getVariableWeight(i);
			}
			return c;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}
        /**
         * This method represents the method that returns the array os double that represents the A, this model need to stay in the right form
         * @param m the model
         * @return array of numbers of type double
         */
	protected static double[][] getA(Model m) {
		if (isModelInRightForm(m)) {
			double[][] A = new double[m.getConstraintCount()][m.getVariableCount()];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					A[i][j] = m.getConstraintAt(i).getVariablePonderation(j);
				}
			}
			return A;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}
        /**
         * This method must be used to verificated that model are in the right form
         * @param m the model 
         * @return a boolean if te model are in right form 
         */
	protected static boolean isModelInRightForm(Model m) {
		boolean correct = m.getType().equals(Model.MINIMIZE);
		for (int i = 0; i < m.getConstraintCount() && correct; i++) {
			if (!m.getConstraintAt(i).getType().equals(Constraint.EQUAL)) {
				correct = false;
			}
		}
		return correct;
	}
        /**
         * This method represents the action to transform the model
         * @param m the model to transform 
         * @return the model
         */
	protected static Model transformModel(Model m) {
		Variable[] vars = new Variable[m.getVariableCount()];
		double[] w = new double[m.getVariableCount()];
		for (int i = 0; i < m.getVariableCount(); i++) {
			vars[i] = m.getVariableAt(i);
			if (m.getType().equals(Model.MINIMIZE)) {
				w[i] = m.getVariableWeight(i);
			} else {
				w[i] = -m.getVariableWeight(i);
			}

		}

		HashMap<Integer, Double> constraintsToChange = new HashMap<>();
		Model newModel = new Model(vars, w, Model.MINIMIZE);
		int count = 1;
		for (int i = 0; i < m.getConstraintCount(); i++) {
			if (!m.getConstraintAt(i).getType().equals(Constraint.EQUAL)) {
				try {
					newModel.addVariable("SlackVariable_" + count, Variable.CONTINUOUS, 0);
					count++;
					if (m.getConstraintAt(i).getType().equals(Constraint.GREATER_OR_EQUAL)) {
						constraintsToChange.put(i, -1.0);
					} else {
						constraintsToChange.put(i, 1.0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		int auxCount = 0;
		for (int i = 0; i < m.getConstraintCount(); i++) {
			double[] weights = new double[newModel.getVariableCount()];
			for (int j = 0; j < m.getConstraintAt(i).getVariableCount(); j++) {
				weights[j] = m.getConstraintAt(i).getVariablePonderation(j);
			}

			if (constraintsToChange.containsKey(i)) {
				weights[m.getVariableCount() + auxCount] = constraintsToChange.get(i);
				auxCount++;
			}
			newModel.addConstraint(weights, Constraint.EQUAL, m.getConstraintAt(i).getRightSideValue(),
					m.getConstraintAt(i).getName());
		}
		return newModel;
	}
        /**
         * This method transpose the matrix that we put by parameter 
         * @param M the matrix to transform or transpose
         * @return matrix transpose
         */
	protected double[][] transpose(double[][] M) {
		double[][] M_t = new double[M[0].length][M.length];

		for (int i = 0; i < M[0].length; i++) {
			for (int j = 0; j < M.length; j++) {
				M_t[i][j] = M[j][i];
			}
		}
		return M_t;
	}
        /**
         * This method transpose the matrix only by the first elements for each row
         * @param M the list of numbers to put at first of each row
         * @return the matrix whit the changes 
         */
	protected double[][] transposeV_M(double[] M) {
		double[][] M_t = new double[M.length][1];

		for (int i = 0; i < M.length; i++) {

			M_t[i][0] = M[i];

		}
		return M_t;
	}
        /**
         * this method returns the first variable of the each row of a matrix 
         * @param M the matrix to put away the first terms on a array
         * @return array of the first terms of the matrix M
         */
	protected double[] transposeM_V(double[][] M) {
		double[] M_t = new double[M.length];

		for (int i = 0; i < M.length; i++) {

			M_t[i] = M[i][0];

		}
		return M_t;
	}
        /**
         * This method combine two matrix for a only one 
         * @param A the matrix a
         * @param Z the matrix z
         * @return the combine of the matriz 
         */
	protected double[][] bigmatrix(double[][] A, double[][] Z) {

		double[][] A_t = transpose(A);
		double[][] big_matrix = new double[Z[0].length + A_t[0].length][Z[0].length + A_t[0].length];

		for (int i = 0; i < Z[0].length; i++) {
			for (int j = 0; j < Z[0].length; j++) {
				big_matrix[i][j] = Z[i][j];
			}
		}

		for (int i = Z[0].length; i < Z[0].length + A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				big_matrix[i][j] = A[i - Z[0].length][j];
			}
		}

		for (int i = 0; i < Z[0].length; i++) {
			for (int j = Z[0].length; j < Z[0].length + A.length; j++) {
				big_matrix[i][j] = A_t[i][j - Z[0].length];
			}
		}

		return big_matrix;
	}
        /**
         * this convert to vectors in one called bigvector by convert the firs element for each sigma array into a first element for each row 
         * @param sigma the vector 
         * @param A the matrix thar we need to sum the size for the big vector
         * @return the bigvectors
         */
	protected double[][] bigvector(double[] sigma, double[][] A) {

		double[][] big_vector = new double[sigma.length + A.length][1];
		for (int i = 0; i < sigma.length; i++) {
			big_vector[i][0] = sigma[i];
		}
		return big_vector;
	}
        /**
         * This method we use to take the min value of array
         * @param array the array to analyce 
         * @return the min value
         */
	protected double getMinValue(double[] array) {
		double minValue = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < minValue) {
				minValue = array[i];
			}
		}
		return minValue;
	}

	// getting the maximum value
        /**
         * This method we use to take the max value of array
         * @param array the array to analyce 
         * @return the max value
         */
	protected double getMaxValue(double[][] array) {
		double maxValue = array[0][0];
		for (int i = 1; i < array.length; i++) {
			if (array[i][0] > maxValue) {
				maxValue = array[i][0];
			}
		}
		return maxValue;
	}
        /**
         * this method returns the pom
         * @param m the model
         * @return pom values
         */
	protected static double pom(Model m) {
		if (isModelInRightForm(m)) {
			double pom = 0;
			for (int i = 0; i < m.getConstraintCount(); i++) {

				double sum = 0;
				for (int j = 0; j < m.getConstraintAt(i).getVariableCount(); j++) {
					sum += m.getConstraintAt(i).getVariablePonderation(j);
				}
				sum = Math.abs(sum);
				double b = Math.abs(m.getConstraintAt(i).getRightSideValue());
				double candidate = Math.max(b / sum, 1);

				if (candidate > pom) {
					pom = candidate;
				}
			}
			if (Double.isInfinite(pom)) {
				pom = 1;
			}
			return pom;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}
        /**
         * this method returns an array of numbers of the variables count, by the value of the pom
         * @param pom the pom
         * @param m the model to compare, this contatins the variable count 
         * @return array whit values of pom, the lenght are equal that the variables in the model 
         */
	protected static double[] getInitialX(double pom, Model m) {
		if (isModelInRightForm(m)) {
			double[] x = new double[m.getVariableCount()];
			for (int i = 0; i < x.length; i++) {
				x[i] = pom;
			}
			return x;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}
        /**
         * This method returns the initial sigma 
         * @param pom the pom
         * @param m the model to use the method
         * @return the initialSigma
         */
	protected static double[] getInitialSigma(double pom, Model m) {
		return getInitialX(pom, m);
	}
        /**
         * This method returns the intial phi oh the model 
         * @param m the model
         * @return array that contains the initial phi of the model
         */
	protected static double[] getInitialPhi(Model m) {
		if (isModelInRightForm(m)) {
			double[] phi = new double[m.getConstraintCount()];

			return phi;
		} else {
			throw new IllegalArgumentException(
					"Specified Model is not in the right form for this Solver, use transformModel method.");
		}
	}

}
