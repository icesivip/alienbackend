/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icesi.vip.alien.alien.simplexMethod;

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
    Simplex simplexMethod;
    Solution solution;
    Model modeloS;
    Matrix finalM;
    
    public SensivilityAnalysis(Simplex simplex, Model modelo, Solution solution, Matrix shadowPrice, Matrix equalities, Matrix finalM) {
        simplexMethod = simplex;
        modeloS = modelo;
        this.finalM = finalM;
        this.solution = solution;
       String [] varNames = simplex.getEveryVariableName();
        posSlacks = new ArrayList();
        for (int i = 0; i < varNames.length; i++) {
            if(varNames[i].startsWith("S"))
                posSlacks.add(i);
        }
    }

    String getEquations() {
        StringBuilder equations = new StringBuilder("<html><body>");
             try {
                  equations.append("Z = ");
            equations.append(solution.getObjectiveFunctionValue() + " ");
            for (int j = 0; j < posSlacks.size(); j++) {
                equations.append(finalM.getArray()[0][posSlacks.get(j)] + " D" + (j+1) + " ");
            }
            equations.append("<br>");
             int x = 0;
            for (int i = 1; i < finalM.getRowDimension(); i++) {
               while(solution.getVariableValue(modeloS.getVariableAt(x)) == 0)
                   x++;
               Variable var = modeloS.getVariableAt(x);
                    equations.append(var.getName()+" = ");
                    equations.append(solution.getVariableValue(var) + " ");
                    for (int j = 0; j < posSlacks.size(); j++) {
                equations.append(finalM.getArray()[i][posSlacks.get(j)] + " D" + (j+1) + " ");
            }
                    x++;
                    equations.append("<br>");    
            }
            equations.append("</body></html>");
            } catch (Exception ex) {
                    Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
                }  
        return equations.toString();
}
    public double[][] getIntervalsD() {
        String toSolve = getEquations();
        System.out.println(toSolve);
        String [] lines = toSolve.split("<br>");
        String[] line = lines[0].split(" ");
        double[][] doubMatrix = new double[lines.length-1][line.length];
        double[] constants = new double[lines.length-1];
        double [][] intervals = initializeIntervals(new double[constants.length][2]);
        for (int i = 1; i < lines.length-1 ; i++) {
            line = lines[i].split(" ");
            constants[i-1] = Double.parseDouble(line[2]);
            for (int j = 3; j < line.length; j+=2) {
                doubMatrix[i-1][j] = Double.parseDouble(line[j]);
            }
        }
        Matrix aimp = new Matrix(doubMatrix);
        aimp.print(2, 2);
        for (int i = 1; i < doubMatrix.length; i++) {
            for (int j = 0; j < doubMatrix[0].length; j++) {
                double aIngresar = constants[i]/doubMatrix[i][j]*-1;
                if(doubMatrix[i][j]<0){
                    if(intervals[i-1][1] > aIngresar)
                    intervals[i-1][1] = aIngresar;
                }
                else {
                    if(intervals[i-1][0] < aIngresar)
                    intervals[i-1][0] = aIngresar;
                }
            }
        }
        Matrix alv = new Matrix(intervals);
        alv.print(2, 2);
        return intervals;
    }

    private double[][] initializeIntervals(double[][] intervals) {
        for (int x=0; x < intervals.length; x++) {
            for (int y=0; y < intervals[x].length; y++) {
                if(y == 0)
                intervals[x][y] = -Double.MAX_VALUE;
                else
                intervals[x][y] = Double.MAX_VALUE;
            }
        }
        return intervals;
    }
}
