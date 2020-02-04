package icesi.vip.alien.alien.interiorPoint;


import icesi.vip.alien.modelLP.Model;
import icesi.vip.alien.modelLP.Solution;

import java.util.ArrayList;

import Jama.Matrix;

public class BarrierMethod extends InteriorPointMethod {

	public static int MAX_IT = 1000;

	/**
	 * Method that return the solution depends of the model that recieved by
	 * parameter
	 * 
	 * @param model parameter of the model
	 * @return Solution
	 */
	@Override
	public Solution solve(Model model) {
		Model m;
		if (InteriorPointMethod.isModelInRightForm(model)) {
			m = model;
		} else {
			m = InteriorPointMethod.transformModel(model);
		}
		double pom = BarrierMethod.pom(m);
		return solve(BarrierMethod.getInitialX(pom, m), BarrierMethod.getInitialPhi(m),
				BarrierMethod.getInitialSigma(pom, m), BarrierMethod.getC(m), BarrierMethod.getB(m),
				BarrierMethod.getA(m), m, model);
	}

	/**
	 * Method that returns the solutionPath depends by the model
	 * @param model the model
	 * @return ArrayList of solution
	 */
	public ArrayList<Solution> giveSolutionPath(Model model) {

		Model m;
		if (InteriorPointMethod.isModelInRightForm(model)) {
			m = model;
		} else {
			m = InteriorPointMethod.transformModel(model);
		}
		double pom = BarrierMethod.pom(m);
		return giveSolutionPath(BarrierMethod.getInitialX(pom, m), BarrierMethod.getInitialPhi(m),
				BarrierMethod.getInitialSigma(pom, m), BarrierMethod.getC(m), BarrierMethod.getB(m),
				BarrierMethod.getA(m), m, model);
	}
        /**
         * This method returns an array whit te solution path of the model give by parameter
         * @param x the list whit the xs
         * @param pi the list whit the pis
         * @param sigma the list whit the sigmas
         * @param c the list whit the cs
         * @param b the list whit the bs
         * @param A the matrix whit the as 
         * @param transformedModel the transoformed model
         * @param originalModel the original model
         * @return array whit te solution path of the model give by parameter
         */
	private ArrayList<Solution> giveSolutionPath(double[] x, double[] pi, double[] sigma, double[] c, double[] b,
			double[][] A, Model transformedModel, Model originalModel) {
		ArrayList<Solution> sol = new ArrayList<Solution>();
		double[] originalX = new double[originalModel.getVariableCount()];
		for (int i = 0; i < originalX.length; i++) {
			originalX[i] = x[i];
		}
		sol.add(new Solution(originalModel, originalX));
		double max_x_sigma = 100000;
		final double epsilon = 0.000001;
		final double alpha = 0.99995;
		final double beta = 0.1;

		double miu = 0;

		while (max_x_sigma > epsilon) {

			for (int i = 0; i < x.length; i++) {
				miu = miu + x[i] * sigma[i];

			}
			miu = beta * (miu) / x.length;

			Matrix miu_e = new Matrix(x.length, 1, miu);

			// creamos la matriz con la matriz con la diagonal que multiplica x-1 * sigma
			double[][] Z = new double[A[0].length][A[0].length];

			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						Z[i][j] = -sigma[i] / x[i];
					} else {
						Z[i][j] = 0;
					}
				}
			}

			double[][] Z1 = new double[A[0].length][A[0].length];
			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						Z1[i][j] = 1 / x[i];
					} else {
						Z1[i][j] = 0;
					}
				}
			}
			;

			double[][] S = new double[A[0].length][A[0].length];

			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						S[i][j] = sigma[i];
					} else {
						S[i][j] = 0;
					}
				}
			}

			Matrix F = new Matrix(transposeV_M(sigma)).minus(new Matrix(Z1).times(miu_e));

			// F.print(2, 4);
			// armamos la matriz grande para resolver el sistema uniendo las matrices Z y A,
			// también creamos el vector para el sistema
			double[][] bigmatrix = bigmatrix(A, Z);

//			double[][] bigvector = bigvector(transposeM_V(F.getArray()), A);
			double[][] bigvector = this.bigvector(c, A, pi, miu, x, b);

			// resolvemos el sistema
			Matrix BigM = new Matrix(bigmatrix);
			Matrix Resx = null;
			try {
				Resx = BigM.solve(new Matrix(bigvector));

			} catch (Exception e) {
				return null;
			}

			double[][] Res1x = Resx.getArray();
			double[][] delta_x = new double[A[0].length][1];
			double[][] delta_pi = new double[Res1x.length - A[0].length][1];

			// separamos el vector respuesta en delta_x y delta_pi
			for (int i = 0; i < Res1x.length; i++) {
				if (i < A[0].length) {
					delta_x[i][0] = Res1x[i][0];
				} else {
					delta_pi[i - A[0].length][0] = Res1x[i][0];
				}
			}

			Matrix delta_sigma = (new Matrix(transposeV_M(sigma)).uminus())
					.minus(new Matrix(Z1).times(new Matrix(S).times(new Matrix(delta_x)).minus(miu_e)));

			// calculamos los valores para el step con respecto a x
			double[] theta_x = new double[delta_x.length];
			for (int i = 0; i < delta_x.length; i++) {
				if (delta_x[i][0] < 0) {
					theta_x[i] = x[i] / -delta_x[i][0];
				} else {
					theta_x[i] = 1000;
				}
			}

			// calculamos los valores para el step con respecto a sigma
			double[] theta_sigma = new double[delta_sigma.getArray().length];
			for (int i = 0; i < delta_sigma.getArray().length; i++) {
				if (delta_sigma.getArray()[i][0] < 0) {
					theta_sigma[i] = sigma[i] / -delta_sigma.getArray()[i][0];
				} else {
					theta_sigma[i] = 1000;
				}
			}

			// obtenemos el menor valor considerando x y sigma
			double minTheta_x = getMinValue(theta_x);
			double minSigma_x = getMinValue(theta_sigma);
			double theta = getMinValue(new double[] { 1, alpha * minTheta_x, alpha * minSigma_x });

			// obtenemos los nuevos valored de x, pi y sigma teniendo en cuenta el step
			Matrix x_f = new Matrix(transposeV_M(x)).plus(new Matrix(delta_x).times(theta));
			Matrix pi_f = new Matrix(transposeV_M(pi)).plus(new Matrix(delta_pi).times(theta));
			Matrix sigma_f = new Matrix(transposeV_M(sigma)).plus((delta_sigma).times(theta));

			// x_f.print(2,5);
			// sigma_f.print(2,5);
			// calculamos los valores de x*sigma (holguras complementarias) y obtenemos el
			// máximo
			Matrix x_sigma = x_f.arrayTimes(sigma_f);
			max_x_sigma = getMaxValue(x_sigma.getArray());

			// convertimos las matrices en vectores
			x = transposeM_V(x_f.getArray());

			pi = transposeM_V(pi_f.getArray());
			sigma = transposeM_V(sigma_f.getArray());

//            System.out.println(x[0] + "		" + x[1] + "		" + theta + "			" + miu + "			" + max_x_sigma);
			miu = 0;
			originalX = new double[originalModel.getVariableCount()];
			for (int i = 0; i < originalX.length; i++) {
				originalX[i] = x[i];
			}
			sol.add(new Solution(originalModel, originalX));
		}

		return sol;

		// delta_sigma.print(1,4);
		// Matrix D =
		// System.out.println();
	}
        /**
         *  The solution of the model give by parameter
         * @param x list whit the xs
         * @param pi list whit the pis
         * @param sigma list whit the sigmas
         * @param c list whit the cs
         * @param b list whit the bs
         * @param A list whit the as
         * @param transformedModel the transformed model 
         * @param originalModel the original model
         * @return  the solution path 
         */
	protected Solution solve(double[] x, double[] pi, double[] sigma, double[] c, double[] b, double[][] A,
			Model transformedModel, Model originalModel) {

		double max_x_sigma = 100000;
		final double epsilon = 0.000001;
		final double alpha = 0.99995;
		final double beta = 0.1;
		int it = 0;
		double miu = 0;

		while (max_x_sigma > epsilon && it < MAX_IT) {
			it++;
			// System.out.println(it);
			for (int i = 0; i < x.length; i++) {
				miu = miu + x[i] * sigma[i];

			}
			miu = beta * (miu) / x.length;

			Matrix miu_e = new Matrix(x.length, 1, miu);

			// creamos la matriz con la matriz con la diagonal que multiplica x-1 * sigma
			double[][] Z = new double[A[0].length][A[0].length];

			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						Z[i][j] = -sigma[i] / x[i];
					} else {
						Z[i][j] = 0;
					}
				}
			}

			double[][] Z1 = new double[A[0].length][A[0].length];
			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						Z1[i][j] = 1 / x[i];
					} else {
						Z1[i][j] = 0;
					}
				}
			}
			;

			double[][] S = new double[A[0].length][A[0].length];

			for (int i = 0; i < A[0].length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					if (i == j) {
						S[i][j] = sigma[i];
					} else {
						S[i][j] = 0;
					}
				}
			}

			Matrix F = new Matrix(transposeV_M(sigma)).minus(new Matrix(Z1).times(miu_e));

			// F.print(2, 4);
			// armamos la matriz grande para resolver el sistema uniendo las matrices Z y A,
			// también creamos el vector para el sistema
			double[][] bigmatrix = bigmatrix(A, Z);

//			double[][] bigvector = bigvector(transposeM_V(F.getArray()), A);
			double[][] bigvector = this.bigvector(c, A, pi, miu, x, b);

			// resolvemos el sistema
			Matrix BigM = new Matrix(bigmatrix);
			Matrix Resx = null;
			try {
				Resx = BigM.solve(new Matrix(bigvector));

			} catch (Exception e) {
				return null;
			}

			double[][] Res1x = Resx.getArray();
			double[][] delta_x = new double[A[0].length][1];
			double[][] delta_pi = new double[Res1x.length - A[0].length][1];

			// separamos el vector respuesta en delta_x y delta_pi
			for (int i = 0; i < Res1x.length; i++) {
				if (i < A[0].length) {
					delta_x[i][0] = Res1x[i][0];
				} else {
					delta_pi[i - A[0].length][0] = Res1x[i][0];
				}
			}

			Matrix delta_sigma = (new Matrix(transposeV_M(sigma)).uminus())
					.minus(new Matrix(Z1).times(new Matrix(S).times(new Matrix(delta_x)).minus(miu_e)));

			// calculamos los valores para el step con respecto a x
			double[] theta_x = new double[delta_x.length];
			for (int i = 0; i < delta_x.length; i++) {
				if (delta_x[i][0] < 0) {
					theta_x[i] = x[i] / -delta_x[i][0];
				} else {
					theta_x[i] = 1000;
				}
			}

			// calculamos los valores para el step con respecto a sigma
			double[] theta_sigma = new double[delta_sigma.getArray().length];
			for (int i = 0; i < delta_sigma.getArray().length; i++) {
				if (delta_sigma.getArray()[i][0] < 0) {
					theta_sigma[i] = sigma[i] / -delta_sigma.getArray()[i][0];
				} else {
					theta_sigma[i] = 1000;
				}
			}

			// obtenemos el menor valor considerando x y sigma
			double minTheta_x = getMinValue(theta_x);
			double minSigma_x = getMinValue(theta_sigma);
			double theta = getMinValue(new double[] { 1, alpha * minTheta_x, alpha * minSigma_x });

			// obtenemos los nuevos valored de x, pi y sigma teniendo en cuenta el step
			Matrix x_f = new Matrix(transposeV_M(x)).plus(new Matrix(delta_x).times(theta));
			Matrix pi_f = new Matrix(transposeV_M(pi)).plus(new Matrix(delta_pi).times(theta));
			Matrix sigma_f = new Matrix(transposeV_M(sigma)).plus((delta_sigma).times(theta));

			// x_f.print(2,5);
			// sigma_f.print(2,5);
			// calculamos los valores de x*sigma (holguras complementarias) y obtenemos el
			// máximo
			Matrix x_sigma = x_f.arrayTimes(sigma_f);
			// System.out.println(strung(x_sigma));
			max_x_sigma = getMaxValue(x_sigma.getArray());

			// convertimos las matrices en vectores
			x = transposeM_V(x_f.getArray());

			pi = transposeM_V(pi_f.getArray());
			sigma = transposeM_V(sigma_f.getArray());

//            System.out.println(x[0] + "		" + x[1] + "		" + theta + "			" + miu + "			" + max_x_sigma);
			miu = 0;

		}

		double[] originalX = new double[originalModel.getVariableCount()];
		for (int i = 0; i < originalX.length; i++) {
			originalX[i] = x[i];
		}
		return new Solution(originalModel, originalX);

		// delta_sigma.print(1,4);
		// Matrix D =
		// System.out.println();
	}
        /**
         * This method returns the strung 
         * @param m the matrix to evaluate 
         * @return the strings 
         */
	public static String strung(Matrix m) {
		StringBuffer sb = new StringBuffer();
		for (int r = 0; r < m.getRowDimension(); ++r) {
			for (int c = 0; c < m.getColumnDimension(); ++c)
				sb.append(m.get(r, c)).append("\t");
			sb.append("\n");
		}
		return sb.toString();
	}
        /**
         * This method returns the bigvector form 
         * @param c list whit the cs
         * @param A list whit the as
         * @param pi list whit the pis
         * @param miu list whit the mius
         * @param x list whit the xs
         * @param b list whit the bs
         * @return the bigvector
         */
	protected double[][] bigvector(double[] c, double[][] A, double[] pi, double miu, double[] x, double[] b) {

		Matrix result = new Matrix(c, 1).transpose();

		Matrix mA = new Matrix(A);

		Matrix pi2 = new Matrix(pi, 1);

		double[][] xinverse = new double[x.length][1];
		for (int i = 0; i < xinverse.length; i++) {
			xinverse[i][0] = 1.0 / x[i];
		}
		Matrix Xinv = new Matrix(xinverse);

		Matrix resultado1 = result.minus(mA.transpose().times(pi2.transpose())).minus(Xinv.times(miu));

		Matrix resultado2 = new Matrix(b, 1).transpose().minus(mA.times(new Matrix(x, 1).transpose()));

//            resultado.print(1, 3);
		double[][] arr1 = resultado1.getArray();
		double[][] arr2 = resultado2.getArray();
		double[][] arrFinal = new double[arr1.length + arr2.length][1];
		for (int i = 0; i < arr1.length; i++) {
			arrFinal[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			arrFinal[arr1.length + i] = arr2[i];
		}

		return arrFinal;

	}

}

