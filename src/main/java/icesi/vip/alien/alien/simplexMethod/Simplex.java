package icesi.vip.alien.alien.simplexMethod;

import Jama.Matrix;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;

import model.*;

public class Simplex implements Solver {
	/**
	 * The M represents the coeficient of the artifical variables in the objective
	 * function to penalize the solution.
	 */
	public static final int BIG_M = 1000000;
	public static final String NOT_FEASIBLE = "Solution not feasible";
	public static final String INFINITE_SOLUTIONS = "Infinite solutions!";
	public static final String NOT_BOUNDED = "Solution not bounded";
	public static final String SOLVED = "Problem finished";
	/**
	 * Indicates the model that is being used so far
	 */
	private Model model;
	/**
	 * Represents the sensitivity analysis that has been obtained
	 */
	private SensivilityAnalysis analysis;
	/**
	 * Matrix with variables, without the Z row, the basic variables column non thw
	 * equalities row.
	 */
	private Matrix ConsLeft;
	/**
	 * Identity matrix before its conversion
	 */
	private Matrix MId;
	/**
	 * Matrix that contains the objective function in the first column
	 */
	private Matrix FObj;
	/**
	 * Matrix that contains the representation of the slack variables in the column
	 * of Basic Variables
	 */
	private Matrix SlackOF;
	/**
	 * Matrix that represents the right side of the constraints.
	 */
	private Matrix equalities;
	/**
	 * Position of the variables contained in the base.
	 */
	private int[] Base;
	/**
	 * Name of the variables contained in base.
	 */
	private String[] varsBase;
	/**
	 * Matrix that represents the Identity matrix inverted
	 */
	private Matrix B_inv;
	/**
	 * Matrix that represents the result of an iteration (without the z column).
	 */
	private Matrix Final;
	/**
	 * Number of decision variables.
	 */
	private int nVarDecision;
	/**
	 * Represents the value of the solution.
	 */
	private Solution solution;
	/**
	 * Indicates if the problem has a solution or not.
	 */
	private String messageSolution;
	/**
	 * Defines the number of the actual iteration.
	 */
	private int iterationID;
	/**
	 * Represents the initial matrix of the problem.
	 */
	private String[] initialM;
	/**
	 * Defines the operations that have already been done.
	 */
	private String operationsDone;
	/**
	 * Represents the ratio test.
	 */
	private double[] theta;

	/**
	 * Creates the constructor of the Simplex class
	 * 
	 * @param opti
	 *            Indica si se quiere maximizar o minimizar el problema
	 * @param equations
	 *            Representa un conjunto de ecuaciones
	 * @throws Exception
	 *             especificar cual método arroja la excepcion "se presenta una
	 *             excepcion cuando o porque"
	 */
	public Simplex(String opti, String[] equations) throws Exception {
		initialM = equations;
		iterationID = 0;
		String[] caracteres = equations[0].split(" ");
		// -2 del 1 z y -2 del = C. No se divide entre 2 por las variables de holgura
		nVarDecision = caracteres.length / 2 - 2;
		try {
			generateEquaAndFOMatries(equations, opti);

			generateConstraintsLeftMatrix(equations, model.getType().equals(Model.MAXIMIZE));

			calculateInitialBase();
			internalteration(model.getType().equals(Model.MAXIMIZE));

			// solve(model);

		} catch (Exception e) {
			// throw new Exception("Characters not allowed");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the variables contained in the base.
	 * 
	 * @return
	 */
	public String[] getVarsBase() {
		return varsBase;
	}

	/**
	 * Changes to the next matrix of the iteration (without the basic variables
	 * column).
	 * 
	 * @return Complete matrix that represents the results of the iteration
	 */
	public double[][] nextIteration() {
		operationsDone = "";
		if (quotientTest()) {
			internalteration(model.getType().equals(Model.MAXIMIZE));
			iterationID++;
			// Final.print(2,2);
		} else {
			double[][] array = Final.getMatrix(0, Final.getRowDimension() - 1, 0, Final.getColumnDimension() - 2)
					.getArray();
			double[] valuesSolution = new double[array[0].length];
			for (int i = 1; i < array.length; i++) {
				for (int j = 0; j < array[0].length; j++) {
					if (Base[i - 1] == j)
						valuesSolution[j] = Final.getArray()[i][Final.getColumnDimension() - 1];
				}
			}
			System.out.print("solución valores ");
			for (int i = 0; i < valuesSolution.length; i++) {
				System.out.print(valuesSolution[i] + " ");
			}
			solution = new Solution(model, valuesSolution);
			try {
				messageSolution = findKindOfSolution();
				// System.out.println(messageSolution);
			} catch (Exception ex) {
				Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Final.print(2, 2);
		return Final.getArray();
	}

	public double[][] getFObj() {
		return FObj.getArray();
	}

	/**
	 * Rounds the values of the entire matrix to numbers of two decimal places
	 * 
	 * @param array
	 *            Represents a set of values
	 * @return A set of values with the desired format
	 */
	public static double[][] roundMatrix(double[][] array) {
		double[][] toConvert = new double[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				toConvert[i][j] = roundDouble(array[i][j]);
			}
		}
		return toConvert;
	}

	/**
	 * Rounds a number to two decimal places
	 * 
	 * @param d
	 *            The number to be rounded
	 * @return Rounded value with the desired format
	 */
	public static double roundDouble(double d) {
		if(Math.abs(d)<0.0001)
			return 0;
		DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
		separadoresPersonalizados.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.###", separadoresPersonalizados);
		return Double.parseDouble(df.format(d));
	}

	/**
	 * Calculate the initial base of the problem
	 */
	private void calculateInitialBase() {
		double[][] array = ConsLeft.getArray();
		Base = new int[equalities.getArray().length];
		varsBase = new String[Base.length];
		for (int i = 0; i < array.length; i++) {
			for (int j = nVarDecision; j < array[0].length; j++) {
				if (array[i][j] == 1) {
					boolean base = true;
					for (int k = 0; k < array.length; k++) {
						if (array[k][j] != 0 && k != i) {
							base = false;
							break;
						}
					}
					if (base) {
						Base[i] = j;
						varsBase[i] = model.getVariableAt(j).getName();
					}
				}
			}
		}
		// for (int i = 0; i < Base.length; i++) {
		// System.out.println(Base[i]);
		// }
	}

	/**
	 * Creates an array that composes all input matrix
	 * 
	 * @param TAB
	 *            Represents the left side of equality
	 * @param X_B
	 *            Represents the matrix where the left side of the equality will be
	 *            calculated in each iteration.
	 * @param Fila_z
	 *            Represents the coeficients of the objective function
	 * @param z_v
	 *            Represents the coeficients of the objective function in each
	 *            interation
	 * @return Full array given the parameters
	 */
	public static Matrix CrearTabla(Matrix TAB, Matrix X_B, Matrix Fila_z, Matrix z_v) {

		double[][] Tabla = new double[TAB.getRowDimension() + 1][TAB.getColumnDimension() + 1];

		for (int j = 0; j < TAB.getColumnDimension(); j++) {
			Tabla[0][j] = Fila_z.getArray()[0][j];
		}
		Tabla[0][TAB.getColumnDimension()] = z_v.getArray()[0][0];

		for (int i = 0; i < TAB.getRowDimension(); i++) {
			for (int j = 0; j < TAB.getColumnDimension(); j++) {
				Tabla[i + 1][j] = TAB.getArray()[i][j];
			}
		}

		for (int i = 0; i < TAB.getRowDimension(); i++) {
			Tabla[i + 1][TAB.getColumnDimension()] = X_B.getArray()[i][0];
		}
		Matrix Tabla1 = new Matrix(Tabla);
		return Tabla1;
	}

	/**
	 * Create the matrix with the coefficients of the basic variables of the initial
	 * matrix
	 * 
	 * @param A
	 *            Current matrix of the actual iteration
	 * @param Base
	 *            Basic values of the initial matrix
	 * @return The set of matrix coefficients
	 */
	public static Matrix CreaB(Matrix A, int[] Base) {

		double[][] B1 = new double[Base.length][Base.length];

		for (int i = 0; i < Base.length; i++) {
			for (int j = 0; j < Base.length; j++) {
				B1[i][j] = A.getArray()[i][Base[j]];
			}
		}

		Matrix B = new Matrix(B1);
		// System.out.println("B: ");
		// B.print(2, 2);
		return B;
	}

	/**
	 * Returns the slack variables of the objective function
	 * 
	 * @param ObjF
	 *            Rpresents the objective function
	 * @param Base
	 *            Set of basic variables
	 * @return Set of slack variables
	 */
	private Matrix takeSlackOF(Matrix ObjF, int[] Base) {
		double[][] C_B1 = new double[Base.length][1];

		for (int j = 0; j < Base.length; j++) {
			C_B1[j][0] = ObjF.getArray()[Base[j]][0];
		}

		Matrix C_B = new Matrix(C_B1);
		// System.out.println("C_B:");
		// C_B.print(2, 2);
		return C_B;
	}

	/**
	 * Generates the constraints of the matrix without their equalities
	 * 
	 * @param equations
	 *            Problem equation set
	 * @param isMax
	 *            Indicates whether this problem is being maximized or not
	 * @return Indication that the constraints were created successfully or not
	 */
	private boolean generateConstraintsLeftMatrix(String[] equations, boolean isMax) {
		double[][] matr = new double[equations.length - 1][];
		int slackPos = 1;
		ArrayList<Integer> ExcessVarsPos = calculatePosExcess(equations);
		ArrayList<Integer> EqualityConstPos = calculatePosEqualities(equations);
		ArrayList<Variable> toEnter = new ArrayList();
		ArrayList<Double> valuesTE = new ArrayList();
		boolean isBigM = false;
		for (int i = 1; i < equations.length; i++) {
			String[] caracteres = equations[i].split(" ");
			double[] equation = new double[nVarDecision + equations.length - 1 + ExcessVarsPos.size()];
			int j;
			for (j = 0; j < nVarDecision; j++) {
				// el +2 omite la Z que le entra por parámetro
				equation[j] = Double.parseDouble(caracteres[j * 2 + 2]);
			}
			try {
				if (i == slackPos)
					equation[i + nVarDecision - 1] = 1;

				if (ExcessVarsPos.contains(i)) {
					equation[equation.length - ExcessVarsPos.size() + ExcessVarsPos.indexOf(i)] = -1;
					FObj.getArray()[i + nVarDecision - 1][0] = BIG_M;
					isBigM = true;
					if (isMax)
						FObj.getArray()[i + nVarDecision - 1][0] *= -1;
					model.addVariable("A" + i, Variable.CONTINUOUS, FObj.getArray()[i + nVarDecision - 1][0]);
					toEnter.add(new Variable("E" + i, Variable.CONTINUOUS));
					valuesTE.add(0.0);
				} else if (EqualityConstPos.contains(i)) {
					FObj.getArray()[i + nVarDecision - 1][0] = BIG_M;
					isBigM = true;
					if (isMax)
						FObj.getArray()[i + nVarDecision - 1][0] *= -1;
					model.addVariable("A" + i, Variable.CONTINUOUS, FObj.getArray()[i + nVarDecision - 1][0]);
				} else {
					model.addVariable("S" + i, Variable.CONTINUOUS, 0);
				}
			} catch (Exception ex) {
				Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
			}
			slackPos++;
			matr[i - 1] = equation;
		}
		for (int i = 0; i < toEnter.size(); i++) {
			try {
				model.addVariable(toEnter.get(i).getName(), toEnter.get(i).getType(), valuesTE.get(i));
			} catch (Exception ex) {
				Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		for (int i = 0; i < matr.length; i++) {
			String[] caracteres = equations[i + 1].split(" ");
			model.addConstraint(matr[i], caracteres[caracteres.length - 2],
					Double.parseDouble(caracteres[caracteres.length - 1]), "name?");
		}
		// System.out.println("cantidad de variables"+toEnter.size() + " <- holgura
		// negativos" + model.getVariableCount());
		ConsLeft = new Matrix(matr);
		if (isBigM) {
			enlargeFO(ExcessVarsPos.size());
			normalizeBigM(ExcessVarsPos.size() + EqualityConstPos.size(), equations);
		}
		System.out.println(model.toString());
		// System.out.println("Constantes izquierda");
		// ConsLeft.print(2,2);
		return isBigM;
	}

	/**
	 * Generates the matrix of the objective function and constraints
	 * 
	 * @param equations
	 *            Represents the set of equations of the problem
	 * @param opti
	 *            Indicates whether this problem is being maximized or not.
	 */
	private void generateEquaAndFOMatries(String[] equations, String opti) {
		String[] objective = equations[0].split(" ");
		double[][] toFO = new double[equations.length - 1 + nVarDecision][1];
		Variable[] vars = new Variable[nVarDecision];
		double[] pesos = new double[nVarDecision];
		String[] caracteres = equations[0].split(" ");

		for (int i = 1; i <= nVarDecision; i++) {
			toFO[i - 1][0] = Double.parseDouble(objective[i * 2]) * (-1);
			vars[i - 1] = new Variable(caracteres[(i * 2) + 1], Variable.CONTINUOUS);
			pesos[i - 1] = toFO[i - 1][0];
		}
		if (Model.MAXIMIZE.equalsIgnoreCase(opti))
			model = new Model(vars, pesos, Model.MAXIMIZE);
		else
			model = new Model(vars, pesos, Model.MINIMIZE);

		FObj = new Matrix(toFO);
		// FObj.print(2, 2);
		double[][] toEqua = new double[equations.length - 1][1];

		for (int i = 1; i < equations.length; i++) {
			caracteres = equations[i].split(" ");
			toEqua[i - 1][0] = Double.parseDouble(caracteres[caracteres.length - 1]);
		}
		equalities = new Matrix(toEqua);
		// System.out.println("igualdades:");
		// equalities.print(2, 2);
	}

	/**
	 * Validates that the ratio rule is being fulfilled
	 * 
	 * @return Indication that the method was performed correctly or not.
	 */
	private boolean quotientTest() {
		Matrix leftC = Final.getMatrix(1, Final.getRowDimension() - 1, 0, Final.getColumnDimension() - 2);
		double[][] matr = leftC.getArray();
		boolean procd = false;
		Matrix equality = Final.getMatrix(1, Final.getRowDimension() - 1, Final.getColumnDimension() - 1,
				Final.getColumnDimension() - 1);
		Matrix eObjective = Final.getMatrix(0, 0, 0, Final.getColumnDimension() - 2).transpose();
		double[][] eObjec = eObjective.getArray();
		double[][] igualdad = equality.getArray();
		double masGrande = 0;
		int posMasG = -1;
		for (int i = 0; i < eObjec.length; i++) {
			if ((roundDouble(eObjec[i][0]) < 0 && model.getType().equals(Model.MAXIMIZE) && eObjec[i][0] < masGrande)
					|| (roundDouble(eObjec[i][0]) >= 0 && !model.getType().equals(Model.MAXIMIZE) && eObjec[i][0] > masGrande)) {
				masGrande = eObjec[i][0];
				posMasG = i;
			}
		}
		if (masGrande != 0) {
			procd = true;
			operationsDone += "La variable " + model.getVariableAt(posMasG).getName() + " entra a base.";
		}
		theta = new double[Base.length];
		double rowLow = Double.MAX_VALUE;
		int posLow = -1;
		if (procd) {
			for (int i = 0; i < Base.length; i++) {
				theta[i] = igualdad[i][0] / matr[i][posMasG];
				if (rowLow > theta[i] && theta[i] > 0) {
					rowLow = theta[i];
					posLow = i;
				}
			}
			if (posLow != -1) {
				Base[posLow] = posMasG;
				varsBase[posLow] = model.getVariableAt(posMasG).getName();
				operationsDone += " La variable " + model.getVariableAt(posLow + nVarDecision).getName()
						+ " sale de base";
			} else
				procd = false;
			for (int i = 0; i < Base.length; i++) {
				System.out.println(Base[i]);
			}
		}
		return procd;
	}

	public int getnVarDecision() {
		return nVarDecision;
	}

	/**
	 * Method in charge of conducting class tests
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Problema normi
		// Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -3 X1 -5 X2 = 0",
		// "0 Z 1 X1 0 X2 <= 4",
		// "0 Z 0 X1 2 X2 <= 12",
		// "0 Z 3 X1 2 X2 <= 18"});
		// Gran M method
		Simplex s = new Simplex("MINIMIZE", new String[] { "1 Z -2 X1 -3 X2 = 0", "0 Z 0.5 X1 0.25 X2 <= 4",
				"0 Z 1 X1 3 X2 >= 20", "0 Z 1 X1 1 X2 = 10" });
		// Solución no factible
//		 Simplex s = new Simplex("MINIMIZE", new String[] {"1 Z -2 X1 -3 X2 = 0",
//		 "0 Z 0.5 X1 0.25 X2 <= 4",
//		 "0 Z 1 X1 3 X2 >= 36",
//		 "0 Z 1 X1 1 X2 = 10"});
		// Problema no acotado
//		 Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -36 X1 -30 X2 3 X3 4
//		 X4 = 0",
//		 "0 Z 1 X1 1 X2 -1 X3 0 X4 <= 5",
//		 "0 Z 6 X1 5 X2 0 X3 -1 X4 <= 10"});
		// Infinitas soluciones
//		 Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -60 X1 -35 X2 -20 X3 =
//		 0",
//		 "0 Z 8 X1 6 X2 1 X3 <= 48",
//		 "0 Z 4 X1 2 X2 1.5 X3 <= 20",
//		 "0 Z 2 X1 1.5 X2 0.5 X3 <= 8",
//		 "0 Z 0 X1 1 X2 0 X3 <= 5"});
		s.buildAnalysis();
		s.getIntervals();
	}

	/**
	 * Calculates the positions of the excess variables
	 * 
	 * @param equations
	 *            Represents the equations of the problem
	 * @return Set of numbers
	 */
	private ArrayList<Integer> calculatePosExcess(String[] equations) {
		ArrayList<Integer> resultado = new ArrayList();
		for (int i = 0; i < equations.length; i++) {
			String[] actual = equations[i].split(" ");
			if (actual[actual.length - 2].equals(">="))
				resultado.add(i);
		}
		return resultado;
	}

	/**
	 * Calculates the positions of the equalities of the restrictions
	 * 
	 * @param equations
	 *            Represents the set of equations of the problem.
	 * @return Set of calculated numbers
	 */
	private ArrayList<Integer> calculatePosEqualities(String[] equations) {
		ArrayList<Integer> resultado = new ArrayList();
		for (int i = 1; i < equations.length; i++) {
			String[] actual = equations[i].split(" ");
			if (actual[actual.length - 2].equals("="))
				resultado.add(i);
		}
		return resultado;
	}

	/**
	 * Method in charge of modifying the matrix with the Big M
	 * 
	 * @param emes
	 *            Indicates the value of the M
	 * @param equations
	 *            Set of equations
	 */
	private void normalizeBigM(int emes, String[] equations) {
		System.out.println("emes " + emes);
		double[][] aNormalizar = ConsLeft.getArray();
		calculateInitialBase();
		internalteration(!model.getType().equals(Model.MAXIMIZE));
		Final.print(2, 2);
	}

	/**
	 * Method responsible of representing the new objective function
	 * 
	 * @param excess
	 *            Indicates the value of the excess variable
	 */
	private void enlargeFO(int excess) {
		double[][] newFO = new double[FObj.getRowDimension() + excess][1];
		double[][] oldFO = FObj.getArray();
		for (int i = 0; i < FObj.getRowDimension(); i++) {
			newFO[i][0] = oldFO[i][0];
		}
		FObj = new Matrix(newFO);
		// System.out.println("FO expandido:");
		// FObj.print(2, 2);
	}

	/**
	 * Method in charge of controlling the internal iterations of the problem.
	 */
	private void internalteration(boolean isMax) {
		MId = CreaB(ConsLeft, Base);
		SlackOF = takeSlackOF(FObj, Base);
		B_inv = MId.inverse();
		Matrix X_B = B_inv.times(equalities);
		Matrix TAB = B_inv.times(ConsLeft);
		Matrix Fila_z = ((SlackOF.transpose()).times(TAB)).minus(FObj.transpose());
		Matrix z_v = (SlackOF.transpose()).times(X_B);
		Final = CrearTabla(TAB, X_B, Fila_z, z_v);
	}

	/**
	 * Method in charge of solving a problem based on the model
	 */
	@Override
	public Solution solve(Model model) {
		double[][] finalFinal = null;
		double[][] sig = nextIteration();
		while (!sig.equals(finalFinal)) {
			finalFinal = sig;
			sig = nextIteration();
		}
		return solution;
	}

	/**
	 * Method in charge of indicating the type of solution of the problem.
	 *
	 * @return A message indicating the type of solution
	 * @throws Exception
	 *             Indicates if a problem was obtained by performing these
	 *             operations
	 */
	private String findKindOfSolution() throws Exception {
		model.isFeasibleSolution(solution);
		for (int i = 0; i < FObj.getArray().length; i++) {
			if (model.getVariableAt(i).getName().startsWith("A"))
				if (solution.getVariableValue(model.getVariableAt(i)) != 0) {
					return NOT_FEASIBLE;
				}
			if (model.getVariableAt(i).getName().startsWith("X"))
				if (solution.getVariableValue(model.getVariableAt(i)) == 0)
					if (roundDouble(Final.getArray()[0][i]) == 0)
						return INFINITE_SOLUTIONS;
			if ((model.getType().equals(Model.MAXIMIZE) && Final.getArray()[0][i] < 0)
					|| (model.getType().equals(Model.MINIMIZE) && Final.getArray()[0][i] > 0))
				return NOT_BOUNDED;
		}
		return SOLVED;
	}

	/**
	 * Returns a message indicating whether the problem has a solution or not.
	 * 
	 * @return
	 */
	public String getMessageSol() {
		return messageSolution;
	}

	/**
	 * Returns the values of the initial M
	 * 
	 * @return
	 */
	public double[] getRHSInitialM() {
		double[] rhs = new double[initialM.length];
		for (int i = 0; i < initialM.length; i++) {
			rhs[i] = Double.parseDouble(initialM[i].split(" ")[nVarDecision * 2 + 3]);
		}
		return rhs;
	}

	public double[] getFinalValuesConstraints() {
		double[] values = new double[initialM.length - 1];
		if (solution != null) {
			for (int i = 1; i < initialM.length; i++) {
				String[] restr = initialM[i].split(" ");
				double val = 0;
				for (int j = 2; j < restr.length - 2; j += 2) {
					try {
						val += Double.parseDouble(restr[j]) * solution.getVariableValue(model.getVariableAt(j / 2 - 1));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				values[i - 1] = roundDouble(val);
			}
		}
		return values;
	}

	/**
	 * Returns the name of the variables
	 * 
	 * @return Set of the variable names
	 */
	public String[] getEveryVariableName() {
		String[] vars = new String[model.getVariableCount()];
		for (int i = 0; i < model.getVariableCount(); i++)
			vars[i] = model.getVariableAt(i).getName();
		return vars;
	}

	/**
	 * Returns the values of the solution
	 * 
	 * @return Set of results
	 */
	public double[] getVarsValuesSolution() {
		double[] sb = new double[getEveryVariableName().length + 1];
		if (solution != null && model != null) {
			try {
				for (int i = 0; i < model.getVariableCount(); i++) {

					sb[i] = roundDouble(solution.getVariableValue(model.getVariableAt(i)));

				}
				sb[model.getVariableCount()] = roundDouble(solution.getObjectiveFunctionValue());
			} catch (Exception ex) {
				Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return sb;

	}

	public double[] getReducedCosts() {
		double[] reducedCosts = new double[nVarDecision];
		if (solution != null) {
			double[][] intervals = analysis.getIntervalsDFO();
			for (int i = 0; i < nVarDecision; i++) {
				 try {
					if(solution.getVariableValue(model.getVariableAt(i)) == 0) {
						 if(model.getType().equals(model.MAXIMIZE))
							 reducedCosts[i] = intervals[i][1];
						 else reducedCosts[i] = -intervals[i][0];
					 } else reducedCosts[i] = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return reducedCosts;
	}

	/**
	 * Indicates the criteria with which the problem is handled
	 * 
	 * @return Message representing the criteria of the model
	 */
	public String getCriterion() {
		return model.getType();
	}

	/**
	 * Indicates the number of the actual iteration
	 * 
	 * @return Number of the actual iteration
	 */
	public int getIterationID() {
		return iterationID;
	}

	/**
	 * Returns the current matrix of the problem
	 * 
	 * @return Current matrix
	 */
	public double[][] getActualMatrix() {
		return roundMatrix(Final.getArray());
	}

	/**
	 * Returns a message indicating whether the iterations were terminated or not.
	 * 
	 * @return Message with the indication
	 */
	public String getOperationsDone() {
		return operationsDone;
	}

	/**
	 * Returns the theta values of the iteration
	 * 
	 * @return
	 */
	public double[] getTheta() {
		if(theta!= null) {
		double[] thetaCopy = new double[theta.length];
		for (int i = 0; i < thetaCopy.length; i++) {
			thetaCopy[i] = roundDouble(theta[i]);
		}
		return thetaCopy;
		} else return null;
	}

	/**
	 * Method responsible of building sensitivity analysis
	 */
	public void buildAnalysis() {
		SlackOF = SlackOF.transpose();
		Matrix shadowPrice = SlackOF.times(B_inv);
		analysis = new SensivilityAnalysis(Base, getEveryVariableName(), model, solution, shadowPrice, equalities,
				Final);
	}

	/**
	 * Method responsible of assigning intervals to sensitivity analysis
	 */
	public void getIntervals() {
		analysis.getIntervalsDConstraints();
		analysis.getIntervalsDFO();
	}

	/**
	 * Method in charge of delivering the sensitivity analysis
	 * 
	 * @return Sensitivity analysis to be obtained
	 */
	public SensivilityAnalysis getAnalysis() {
		return analysis;
	}

	/**
	 * Method in charge of modifying the sensitivity analysis
	 * 
	 * @param analysis
	 *            Mdified sensitivity analysis
	 */
	public void setAnalysis(SensivilityAnalysis analysis) {
		this.analysis = analysis;
	}
}