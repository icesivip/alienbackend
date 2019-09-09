package icesi.vip.alien.alien.simplexMethod;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Jama.Matrix;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Model;
import model.Solution;
import model.Solver;
import model.Variable;

/**
 *
 * @author Luis
 */
public class SensivilityAnalysis {

    private ArrayList<Integer> posSlacks;
    int[] base;
    Solution solution;
    Model modeloS;
    Matrix finalM;
    Matrix shadowPrice;
    private String equationsFO;
	private String equationsConstraints;
	double[][] intervalsFO;
	double[][] intervalsConstraints;

	public SensivilityAnalysis(int[] base, String[] varN, Model modelo, Solution solution, Matrix shadowPrice,
			Matrix equalities, Matrix finalM) {
//        simplexMethod = simplex;
        this.base = base;
        modeloS = modelo;
        this.finalM = finalM;
        this.solution = solution;
        this.shadowPrice = shadowPrice;
        String [] varNames = varN;
        System.out.print("shadow price");
        shadowPrice.print(2, 2);
        posSlacks = new ArrayList();
        for (int i = 0; i < varNames.length; i++) {
            if(varNames[i].startsWith("S") || varNames[i].startsWith("A"))
                posSlacks.add(i);
        }
    }
    
    public String getEquationsFO() {
		return equationsFO;
	}

    public double[][] getShadowPrice(){
    	return Simplex.roundMatrix(shadowPrice.getArray());
    }
    
	public String getEquationsConstraints() {
		return equationsConstraints;
	}
	
    String buildEquationsConstraints() {
        StringBuilder equations = new StringBuilder("<html><body>");
             try {
//                  equations.append("Z = ");
//            equations.append(solution.getObjectiveFunctionValue() + " ");
//            for (int j = 0; j < posSlacks.size(); j++) {
//                if(finalM.getArray()[0][posSlacks.get(j)]<0)
//                equations.append(Simplex.roundDouble(finalM.getArray()[0][posSlacks.get(j)]) + " D" + (j+1) + " ");
//                else equations.append("+"+Simplex.roundDouble(finalM.getArray()[0][posSlacks.get(j)]) + " D" + (j+1) + " ");
//            }
//            equations.append("<br>");
             int x = 0;
            for (int i = 1; i < finalM.getRowDimension(); i++) {
                if(i>1)
                equations.append("<br>");
               
//               while(solution.getVariableValue(modeloS.getVariableAt(x)) == 0)
//                   x++;
//                for (int y = 0; y < base.length; y++) {
				x = base[i - 1];
				Variable var = modeloS.getVariableAt(x);
				equations.append(var.getName() + " = ");
				equations.append(Simplex.roundDouble(solution.getVariableValue(var)) + " ");
				for (int j = 0; j < posSlacks.size(); j++) {
					if (finalM.getArray()[i][posSlacks.get(j)] < 0)
						equations.append(
								Simplex.roundDouble(finalM.getArray()[i][posSlacks.get(j)]) + " D" + (j + 1) + " ");
					else
						equations.append("+" + Simplex.roundDouble(finalM.getArray()[i][posSlacks.get(j)]) + " D"
								+ (j + 1) + " ");
				}
				x++;
			}
//                }
            equations.append("</body></html>");
            } catch (Exception ex) {
                    Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
                }  
             equationsConstraints = equations.toString();
        return equations.toString();
}
    public double[][] getIntervalsDConstraints() {
        String toSolve = buildEquationsConstraints();
        System.out.println(toSolve);
        
        String [] lines = toSolve.split("<br>");
        String[] line = lines[0].split(" ");
        double[][] doubMatrix = new double[lines.length][(line.length-3)/2];
        double[] constants = new double[lines.length];
        double [][] intervals = initializeIntervals(new double[constants.length][2]);
        //toma doubles necesarios
        for (int i = 0; i < lines.length ; i++) {
            line = lines[i].split(" ");
            constants[i] = Double.parseDouble(line[2]);
            for (int j = 3; j < line.length; j+=2) {
                if(!line[j].startsWith("<"))
                doubMatrix[i][(j-3)/2] = Double.parseDouble(line[j]);
            }
        }
        Matrix aimp = new Matrix(doubMatrix);
        System.out.println("antes de ingresar a intervalos");
        aimp.print(2, 2);
        for (int i = 0; i < doubMatrix.length; i++) {
            for (int j = 0; j < doubMatrix[0].length; j++) {
                double aIngresar = constants[i]/doubMatrix[i][j]*-1;
                if(aIngresar > 0){
                    if(intervals[j][1] > aIngresar)
                    intervals[j][1] = Simplex.roundDouble(aIngresar);
                }
                else {
                    if(intervals[j][0] < aIngresar)
                    intervals[j][0] = Simplex.roundDouble(aIngresar);
                }
            }
        }
        Matrix alv = new Matrix(intervals);
        intervalsConstraints = Simplex.roundMatrix(intervals);
        alv.print(2, 2);
        return intervals;
    }

    public double[][] getIntervalsDFO() {
        String toSolve = buildEquationsFO();
        System.out.println(toSolve);
        String [] lines = toSolve.split("<br>");
        String[] line = lines[0].split(" ");
        double[][] doubMatrix = new double[lines.length][getInitialVars()];
        double[] constants = new double[lines.length];
        
        double [][] intervals = initializeIntervals(new double[getInitialVars()][2]);
        for (int i = 0; i < lines.length; i++) {
            line = lines[i].split(" ");
            constants[i] = Double.parseDouble(line[2]);
            for (int j = 3; j < line.length; j+=2) {
//                evita tomar un codigo html
				if (!line[j].startsWith("<")) {
//                Funciona para no más de 10 restricciones
					int posD = Integer.parseInt(line[j + 1].charAt(1) + "") - 1;
					doubMatrix[i][(posD)] = Double.parseDouble(line[j]);
				}
			}
		}
		Matrix aimp = new Matrix(doubMatrix);
		System.out.println("antes de ingresar a intervalos");
		aimp.print(2, 2);
		for (int i = 0; i < doubMatrix.length; i++) {
			for (int j = 0; j < doubMatrix[0].length; j++) {
				double aIngresar = constants[i] / doubMatrix[i][j] * -1;
				if (aIngresar > 0) {
					if (intervals[j][1] > aIngresar)
						intervals[j][1] = Simplex.roundDouble(aIngresar);
				} else {
					if (intervals[j][0] < aIngresar)
						intervals[j][0] = Simplex.roundDouble(aIngresar);
				}
			}
		}
		Matrix alv = new Matrix(intervals);
		intervalsFO = Simplex.roundMatrix(intervals);
		alv.print(2, 2);
		return intervals;
	}

	private int getInitialVars() {
		int count = 0;
		for (int i = 0; i < modeloS.getVariableCount(); i++) {
			if (modeloS.getVariableAt(i).getName().startsWith("X"))
				count++;
		}
		return count;
	}

	String buildEquationsFO() {
        StringBuilder equations = new StringBuilder("<html><body>");
             try {
             int x = 0;
            for (int i = 1; i < modeloS.getVariableCount(); i++) {
               while( x<modeloS.getVariableCount() && (solution.getVariableValue(modeloS.getVariableAt(x)) != 0 || modeloS.getVariableAt(x).getName().startsWith("A"))){
                   x++;
                   }	
                   if(x==modeloS.getVariableCount())
                   break;
                   
                   if(i>1)
                equations.append("<br>");
               Variable var = modeloS.getVariableAt(x);
                    equations.append(var.getName()+" = ");
                    for (int j = 0; j < finalM.getRowDimension(); j++) {
                        if(j==0){
                            String constant;
//                            if(var.getName().startsWith("A")){
//                                if(modeloS.getType().equals(Model.MINIMIZE))
//                                   constant = (finalM.get(j, x) + Simplex.BIG_M)+ " ";
//                                else
//                                    constant = (finalM.get(j, x) - Simplex.BIG_M)+ " ";
//                            }
//                            else if(var.getName().startsWith("E"))
//                            	continue;
//                            else
						constant = Simplex.roundDouble(finalM.get(j, x)) + " ";
						equations.append(constant);

					} else if (modeloS.getVariableAt(base[j - 1]).getName().startsWith("X")) {
						if (finalM.get(j, x) < 0)
							equations.append(Simplex.roundDouble(finalM.get(j, x)) + " D"
									+ (modeloS.getVariableAt(base[j - 1]).getName().charAt(1)) + " ");
						else
							equations.append("+" + Simplex.roundDouble(finalM.get(j, x)) + " D"
									+ (modeloS.getVariableAt(base[j - 1]).getName().charAt(1)) + " ");
					}
				}
				if (var.getName().startsWith("X"))
					equations.append("-1 D" + (x + 1) + " ");
				x++;
			}
			equations.append("</body></html>");
		} catch (Exception ex) {
			Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
		}
		equationsFO = equations.toString();
		return equations.toString();
	}

	private double[][] initializeIntervals(double[][] intervals) {
		for (int x = 0; x < intervals.length; x++) {
			for (int y = 0; y < intervals[x].length; y++) {
				if (y == 0)
					intervals[x][y] = -Double.MAX_VALUE;
				else
					intervals[x][y] = Double.MAX_VALUE;
			}
		}
		return intervals;
	}

}