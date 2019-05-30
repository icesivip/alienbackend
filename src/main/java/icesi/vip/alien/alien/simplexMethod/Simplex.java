package icesi.vip.alien.alien.simplexMethod;
import Jama.Matrix;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

public class Simplex implements Solver{

    public static final int BIG_M = 1000000;
    
	private Model model;
        private SensivilityAnalysis analysis;
        /**
         * matriz interna, solo variables y sin la columna y fila del Z, tampoco tiene la fila de las igualdades
         */
        private Matrix ConsLeft ;
        /**
         * matriz identidad antes de convertirse
         */
        private Matrix MId;
        /**
         * matriz con la función objetivo dada en la primera columna
         */
        private Matrix FObj;
        /**
         * matriz que representa las variables de holgura que acompañan a la función objetivo (primera columna)
         */
        private Matrix SlackOF;
        /**
         * matriz de igualdades en la primera columna
         */
        private Matrix equalities;
        /**
         * indices que forman la matriz identidad
         */
        private int [] Base;
        private String [] varsBase;
        

		/**
         * matriz que representa la matriz final de una iteración (sin la columna del Z)
         */
        private Matrix Final;
        
        private int nVarDecision;
        private Solution solution;
        private String messageSolution;
        private int iterationID;
        private String[] initialM;
        private String operationsDone;
        private double [] theta;
        
        public Simplex(String opti, String[] equations) throws Exception {
            initialM = equations;
            iterationID = 0;
        String [] caracteres = equations[0].split(" ");
//      -2 del 1 z y -2 del = C. No se divide entre 2 por las variables de holgura
        nVarDecision = caracteres.length/2-2;
            try {
                generateEquaAndFOMatries(equations, opti);
            
            generateConstraintsLeftMatrix(equations, model.getType().equals(Model.MAXIMIZE));
            solve(model);
            } catch (Exception e) {
//            throw new Exception("Characters not allowed");
            	 e.printStackTrace();
            }
//            System.out.print(isMaximization);
        }
        
        public String[] getVarsBase() {
        	return varsBase;
        }
            
        /**
         * Cambia a la siguiente matriz de la iteración correspondiente (no contiene la columna del Z)
         * @return Una matriz completa que corresponde al resultado de la iteración
         */
        public double[][] nextIteration () {
            operationsDone = "";
            if(quotientTest()){
             internalteration(model.getType().equals(Model.MAXIMIZE));
             iterationID++;
//             Final.print(2,2);
            }
            else {
           double[][] array = Final.getMatrix(0, Final.getRowDimension()-1, 0, Final.getColumnDimension()-2).getArray();
           double[] valuesSolution = new double[array[0].length];
           for (int i = 1; i < array.length; i++) {
               for (int j = 0; j < array[0].length; j++) {
                  if(Base[i-1] == j)
                           valuesSolution[j] = Final.getArray()[i][Final.getColumnDimension()-1];
               }
           }
           System.out.print("solución valores ");
           for (int i = 0; i < valuesSolution.length; i++) {
                System.out.print(valuesSolution[i] + " ");
            }
                solution = new Solution(model, valuesSolution);
                try {
            messageSolution = findKindOfSolution();
//            System.out.println(messageSolution);
        } catch (Exception ex) {
            Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
            Final.print(2, 2);
             return Final.getArray();
        }
        
        public static double[][] roundMatrix(double[][] array) {
        	double[][] toConvert = new double[array.length][array[0].length];
        	for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[0].length; j++) {
					toConvert[i][j] = roundDouble(array[i][j]);
				}
			}
			return toConvert;
		}

		private static double roundDouble(double d) {
			DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
        	separadoresPersonalizados.setDecimalSeparator('.');
        	DecimalFormat df = new DecimalFormat("#.##", separadoresPersonalizados);
			return Double.parseDouble(df.format(d));
		}

		/**
         * Calcula la base inicial del problema
         */
       private void calculateInitialBase () {
           double[][] array = ConsLeft.getArray();
           Base = new int[equalities.getArray().length];
           varsBase = new String[Base.length];
           for (int i = 0; i < array.length; i++) {
               for (int j = nVarDecision; j < array[0].length; j++) {
                   if(array[i][j] == 1) {
                       boolean base = true;
                       for (int k = 0; k < array.length; k++) {
                           if(array[k][j] != 0 && k != i){
                               base = false;
                               break;
                           }
                       }
                       if(base) {
                           Base[i] = j;
                           varsBase[i] = model.getVariableAt(j).getName();
                       }
                   }
               }
           }
//           for (int i = 0; i < Base.length; i++) {
//                System.out.println(Base[i]);
//            }
       }
       
       /**
        * Crea una matriz que compone todas las matrices de entrada
        * @param TAB lado izquierdo de la igualdad
        * @param X_B don know
        * @param Fila_z ecuación objetivo
        * @param z_v don know
        * @return matriz completa dados los parámetros
        */
       public static Matrix CrearTabla (Matrix TAB, Matrix X_B, Matrix Fila_z, Matrix z_v){
 
             double[][] Tabla = new double [TAB.getRowDimension()+1][TAB.getColumnDimension()+1];
 
             for (int j = 0; j<TAB.getColumnDimension(); j++){
                    Tabla[0][j] = Math.round(Fila_z.getArray()[0][j] * 100d) / 100d;                             
             }
             Tabla[0][TAB.getColumnDimension()] = z_v.getArray()[0][0];
 
             for (int i = 0; i<TAB.getRowDimension(); i++){
                    for (int j = 0; j<TAB.getColumnDimension(); j++){
                           Tabla[i+1][j] = Math.round(TAB.getArray()[i][j]* 100d) / 100d;                              
                    }
             }
 
             for (int i = 0; i<TAB.getRowDimension(); i++){
                    Tabla[i+1][TAB.getColumnDimension()] = Math.round(X_B.getArray()[i][0]* 100d) / 100d;                                         
             }
             Matrix Tabla1 = new Matrix(Tabla);
             return Tabla1;
       }
 
       /**
        * Crea la matriz don know
        * @param A
        * @param Base
        * @return 
        */
       public static Matrix CreaB(Matrix A, int [] Base){
 
             double [][] B1 = new double [Base.length][Base.length];
 
             for (int i =0; i<Base.length; i++){
                    for (int j =0; j<Base.length; j++){
                           B1[i][j] = A.getArray()[i][Base[j]];                                                   
                    }
             }
 
             Matrix B = new Matrix(B1);
//             System.out.println("B: ");
//             B.print(2, 2);
             return B;
       }
       private Matrix takeSlackOF(Matrix ObjF, int [] Base){
             double [][] C_B1 = new double [Base.length][1];
 
             for (int j = 0; j<Base.length; j++){                  
                    C_B1[j][0] = ObjF.getArray()[Base[j]][0];                                                  
             }
 
             Matrix C_B = new Matrix(C_B1);
//             System.out.println("C_B:");
//             C_B.print(2, 2);
             return C_B;
       }

    private boolean generateConstraintsLeftMatrix(String[] equations, boolean isMax){
        double[][] matr = new double[equations.length-1][];
        int slackPos = 1;
        ArrayList<Integer> ExcessVarsPos = calculatePosExcess(equations);
        ArrayList<Integer> EqualityConstPos = calculatePosEqualities(equations);
        ArrayList<Variable> toEnter = new ArrayList();
        ArrayList<Double> valuesTE = new ArrayList();
        boolean isBigM = false;
        for (int i = 1; i < equations.length; i++) {
                String[] caracteres = equations[i].split(" ");
                double[] equation = new double[nVarDecision + equations.length-1 + ExcessVarsPos.size()];
                int j;
                for (j = 0; j < nVarDecision; j++) {
//                  el +2 omite la Z que le entra por parámetro  
                    equation[j] = Double.parseDouble(caracteres[j*2+2]);
                }
                try {
                    if(i == slackPos)
                        equation[i + nVarDecision-1] = 1;
                    
                    if(ExcessVarsPos.contains(i)){
                        equation[equation.length - ExcessVarsPos.size() + ExcessVarsPos.indexOf(i)] = -1;
                        FObj.getArray()[i + nVarDecision-1][0] = BIG_M;
                        isBigM = true;
                        if(isMax)
                            FObj.getArray()[i + nVarDecision-1][0] *= -1;
                        model.addVariable("A" + i, Variable.CONTINUOUS, FObj.getArray()[i + nVarDecision-1][0]);
                        toEnter.add(new Variable("E"+ i, Variable.CONTINUOUS));
                        valuesTE.add(0.0);
                    }
                    else if(EqualityConstPos.contains(i)){
                        FObj.getArray()[i + nVarDecision-1][0] = BIG_M;
                        isBigM = true;
                        if(isMax)
                            FObj.getArray()[i + nVarDecision-1][0] *= -1;
                        model.addVariable("A"+ i, Variable.CONTINUOUS, FObj.getArray()[i + nVarDecision-1][0]);
                    } else {
                        model.addVariable("S"+ i, Variable.CONTINUOUS, 0);
                         }
                        } catch (Exception ex) {
                            Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
                        }
                slackPos++;
                matr[i-1] = equation;
                }
        for (int i = 0; i < toEnter.size(); i++) {
            try {
                model.addVariable(toEnter.get(i).getName(), toEnter.get(i).getType(), valuesTE.get(i));
            } catch (Exception ex) {
                Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int i = 0; i < matr.length; i++) {
            String[] caracteres = equations[i+1].split(" ");
            model.addConstraint(matr[i], caracteres[caracteres.length-2], Double.parseDouble(caracteres[caracteres.length-1]), "name?");
            }
//            System.out.println("cantidad de variables"+toEnter.size() + " <- holgura negativos" + model.getVariableCount());
            ConsLeft = new Matrix(matr);
            if(isBigM){
                enlargeFO(ExcessVarsPos.size());
                normalizeBigM(ExcessVarsPos.size() + EqualityConstPos.size(), equations);
            }
            System.out.println(model.toString());
//            System.out.println("Constantes izquierda");
//            ConsLeft.print(2,2);
            return isBigM;
    }

    private void generateEquaAndFOMatries(String[] equations, String opti) {
        String[] objective = equations[0].split(" ");
        double[][] toFO = new double[equations.length-1 + nVarDecision][1];
        Variable[] vars = new Variable[nVarDecision];
        double [] pesos = new double[nVarDecision];
        String[] caracteres = equations[0].split(" ");
            
        for (int i = 1; i <= nVarDecision; i++) {
            toFO[i-1][0] = Double.parseDouble(objective[i*2]) * (-1);
            vars[i-1] = new Variable(caracteres[(i*2)+1], Variable.CONTINUOUS);
            pesos[i-1] = toFO[i-1][0];
        }
        if(Model.MAXIMIZE.equalsIgnoreCase(opti))
                model = new Model(vars, pesos, Model.MAXIMIZE);
            else model = new Model(vars, pesos, Model.MINIMIZE);
        
        FObj = new Matrix(toFO);
//        FObj.print(2, 2);
        double[][] toEqua = new double[equations.length-1][1];
        
        for (int i = 1; i < equations.length; i++) {
            caracteres = equations[i].split(" ");
            toEqua[i-1][0] = Double.parseDouble(caracteres[caracteres.length-1]);
        }
        equalities = new Matrix(toEqua);
//        System.out.println("igualdades:");
//        equalities.print(2, 2);
    }

        private boolean quotientTest() {
        Matrix leftC = Final.getMatrix(1, Final.getRowDimension()-1, 0, Final.getColumnDimension()-2);
        double[][] matr = leftC.getArray();
        boolean procd = false;
        Matrix equality = Final.getMatrix(1, Final.getRowDimension()-1, Final.getColumnDimension()-1, Final.getColumnDimension()-1);
        Matrix eObjective = Final.getMatrix(0, 0, 0, Final.getColumnDimension()-2).transpose();
        double[][] eObjec = eObjective.getArray();
        double [][] igualdad = equality.getArray();
        double masGrande = 0;
        int posMasG = -1;
        for (int i = 0; i < eObjec.length; i++) {
            if((eObjec[i][0] < 0 && model.getType().equals(Model.MAXIMIZE) && eObjec[i][0] < masGrande) || (eObjec[i][0] > 0 && !model.getType().equals(Model.MAXIMIZE)&& eObjec[i][0] > masGrande)){
                masGrande = eObjec[i][0];
                posMasG = i;
            }
        }
        if(masGrande != 0){
            procd = true;
            operationsDone += "La variable " + model.getVariableAt(posMasG).getName() + " entra a base.";
        }
        theta = new double[Base.length];
        double rowLow = Double.MAX_VALUE;
        int posLow = -1;
        if(procd){
        for (int i = 0; i < Base.length; i++) {
            theta[i] = igualdad[i][0] / matr[i][posMasG];
            if(rowLow>theta[i] && theta[i] > 0){
                rowLow = theta[i];
                posLow = i;
            }
        }
        if(posLow != -1){
        Base[posLow] = posMasG;
        varsBase[posLow] = model.getVariableAt(posMasG).getName();
        operationsDone += " La variable " + model.getVariableAt(posLow + nVarDecision).getName() + " sale de base";
        }
        else procd = false;
        for (int i = 0; i < Base.length; i++) {
                System.out.println(Base[i]);
            }
        }
        return procd;
    }
    public static void main(String[] args) throws Exception {
//        Problema normi
//        Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -3 X1 -5 X2 = 0",
//                                           "0 Z 1 X1 0 X2 <= 4",
//                                           "0 Z 0 X1 2 X2 <= 12",
//                                           "0 Z 3 X1 2 X2 <= 18"});
//           Gran M method
           Simplex s = new Simplex("MINIMIZE", new String[] {"1 Z -2 X1 -3 X2 = 0",
                                           "0 Z 0.5 X1 0.25 X2 <= 4",
                                           "0 Z 1 X1 3 X2 >= 20",
                                           "0 Z 1 X1 1 X2 = 10"});
//          Solución no factible
//          Simplex s = new Simplex("MINIMIZE", new String[] {"1 Z -2 X1 -3 X2 = 0",
//                                           "0 Z 0.5 X1 0.25 X2 <= 4",
//                                           "0 Z 1 X1 3 X2 >= 36",
//                                           "0 Z 1 X1 1 X2 = 10"});
//            Problema no acotado
//              Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -36 X1 -30 X2 3 X3 4 X4 = 0",
//                                           "0 Z 1 X1 1 X2 -1 X3 0 X4 <= 5",
//                                           "0 Z 6 X1 5 X2 0 X3 -1 X4 <= 10"});
//             Infinitas soluciones
//            Simplex s = new Simplex("MAXIMIZE", new String[] {"1 Z -60 X1 -35 X2 -20 X3 = 0",
//                                           "0 Z 8 X1 6 X2 1 X3 <= 48",
//                                           "0 Z 4 X1 2 X2 1.5 X3 <= 20",
//                                           "0 Z 2 X1 1.5 X2 0.5 X3 <= 8",
//                                            "0 Z 0 X1 1 X2 0 X3 <= 5"});
    }

    private ArrayList <Integer> calculatePosExcess(String[] equations) {
        ArrayList <Integer>resultado = new ArrayList();
        for (int i = 0; i < equations.length; i++) {
            String [] actual = equations[i].split(" ");
            if(actual[actual.length-2].equals(">="))
                resultado.add(i);
        }
        return resultado;
    }

    private ArrayList<Integer> calculatePosEqualities(String[] equations) {
        ArrayList <Integer>resultado = new ArrayList();
        for (int i = 1; i < equations.length; i++) {
            String [] actual = equations[i].split(" ");
            if(actual[actual.length-2].equals("="))
                resultado.add(i);
        }
        return resultado;
        }

    private void normalizeBigM(int emes, String []equations) {
        System.out.println("emes " +emes);
        double[][] aNormalizar = ConsLeft.getArray();
            calculateInitialBase();
            internalteration(!model.getType().equals(Model.MAXIMIZE));
             Final.print(2, 2);
    }

    private void enlargeFO(int excess) {
        double[][] newFO = new double[FObj.getRowDimension()+excess][1];
        double [][] oldFO = FObj.getArray();
        for (int i = 0; i < FObj.getRowDimension(); i++) {
            newFO[i][0] = oldFO[i][0];
        }
        FObj = new Matrix(newFO);
//        System.out.println("FO expandido:");
//        FObj.print(2, 2);
        }

    private void internalteration(boolean isMax) {
        MId = CreaB(ConsLeft, Base);
        SlackOF = takeSlackOF(FObj, Base);
        Matrix B_inv = MId.inverse(); 
        Matrix X_B = B_inv.times(equalities);            
        Matrix TAB = B_inv.times(ConsLeft);
        Matrix Fila_z = ((SlackOF.transpose()).times(TAB)).minus(FObj.transpose());
        Matrix z_v = (SlackOF.transpose()).times(X_B);
        Final = CrearTabla(TAB, X_B, Fila_z, z_v);
    }

    @Override
    public Solution solve(Model model) {
        calculateInitialBase();
        internalteration(model.getType().equals(Model.MAXIMIZE));
        return solution;
    }

    private String findKindOfSolution() throws Exception {
        model.isFeasibleSolution(solution);
        for (int i = 0; i < FObj.getArray().length; i++) {
            if(model.getVariableAt(i).getName().startsWith("A"))
                if(solution.getVariableValue(model.getVariableAt(i)) != 0){
                    return "Solution not feasible";
                }
            if(model.getVariableAt(i).getName().startsWith("X"))
                if(solution.getVariableValue(model.getVariableAt(i)) == 0)
                    if(Final.getArray()[0][i] == 0)
                        return "Infinite solutions!";
            if((model.getType().equals(Model.MAXIMIZE) && Final.getArray()[0][i] <0) || (model.getType().equals(Model.MINIMIZE) && Final.getArray()[0][i] > 0))
                return "Solution not bounded";
        }
        return "Problem finished";
    }
    
    public String getMessageSol() {
        return messageSolution;
    }
    
    public String[] getInitialM () {
        return initialM;
    }
    public String[] getEveryVariableName() {
        String[] vars = new String[model.getVariableCount()];
        for (int i = 0; i < model.getVariableCount(); i++) 
            vars[i] = model.getVariableAt(i).getName();
        return vars;
    }

    public double [][] getFinalSolution() {
        double[][] finalFinal = null;
        double[][] sig = nextIteration();
        while(!sig.equals(finalFinal)){
            finalFinal = sig;
            sig = nextIteration();
        }
        return roundMatrix(sig);
    }

   public double[] getVarsValuesSolution() {
        double [] sb = new double[getEveryVariableName().length + 1];
        if(solution!= null && model != null){
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

    public String getCriterion() {
        return model.getType();
    }
    public int getIterationID() {
        return iterationID;
    }

    public double[][] getActualMatrix() {
        return roundMatrix(Final.getArray());
    }

    public String getOperationsDone() {
        return operationsDone;
    }

    public double[] getTheta() {
        return theta;
    }

    public void buildAnalysis() {
    	analysis = new SensivilityAnalysis(Base, getEveryVariableName(), model, solution, SlackOF, equalities, Final);
    }

    public void getIntervals() {
			analysis.getIntervalsDConstraints();
			analysis.getIntervalsDFO();
    }

	public SensivilityAnalysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(SensivilityAnalysis analysis) {
		this.analysis = analysis;
	}
}
