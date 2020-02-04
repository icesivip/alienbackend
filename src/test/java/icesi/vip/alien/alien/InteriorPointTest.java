package icesi.vip.alien.alien;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.interiorPoint.InteriorPointContainer;
import icesi.vip.alien.modelLP.Model;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InteriorPointTest {

	public Model model;

	public final static double SIGMA_VALUE = 0.01;

	public void stageOne() {
		String type = "MAXIMIZE";
		String vars = "X1:C,X2:C";
		String objectiveFunction = "3,5";
		String constraints = "1,0,<=,4;0,2,<=,12;3,2,<=,18;1,0,>=,0;0,1,>=,0";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	public void stageTwo() {
		String type = "MINIMIZE";
		String vars = "X1:C,X2:C";
		String objectiveFunction = "2,3";
		String constraints = "0.5,0.25,<=,4;1,3,>=,20;1,1,=,10";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	public void stageThree() {
		String type = "MINIMIZE";
		String vars = "X1:C,X2:C";
		String objectiveFunction = "2,3";
		String constraints = "0.5,0.25,<=,4;1,3,>=,36;1,1,=,10";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	public void stageFour() {
		String type = "MAXIMIZE";
		String vars = "X1:C,X2:C,X3:C,X4:C";
		String objectiveFunction = "36,30,-3,-4";
		String constraints = "1,1,-1,0,<=,5;6,5,0,-1,<=,10";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	public void stageFive() {
		String type = "MAXIMIZE";
		String vars = "X1:C,X2:C,X3:C";
		String objectiveFunction = "60,35,20";
		String constraints = "8,6,1,<=,48;4,2,1.5,<=,20;2,1.5,0.5,<=,8;0,5,0,<=,5";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	public void stageSix() {
		String type = "MAXIMIZE";
		String vars = "X1:C,X2:C,X3:C,X4:C";
		String objectiveFunction = "2,-4,5,-6";
		String constraints = "1,4,-2,0,<=,2;-1,2,3,4,<=,1";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model = m;
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void interiorPointMaxinimize() {
		stageOne();
		try {
			InteriorPointContainer jar = new InteriorPointContainer(model);
			assertTrue(jar.getMaxX() == 6);
			assertTrue(jar.getMaxY() == 9);
			int lastSolution = jar.getSolutionPath().length - 1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible == true);
			
			
			double x1= jar.getSolutionPath()[lastSolution].variables.get("X1");
			double result= Math.abs(2.0- x1)/ 2.0;
			assertTrue(result< SIGMA_VALUE);
			double x2= jar.getSolutionPath()[lastSolution].variables.get("X2");
			result= Math.abs(6.0- x2)/ 6.0;
			assertTrue(result< SIGMA_VALUE);
			double z= jar.getSolutionPath()[lastSolution].z;
			result= Math.abs(36.0- z)/ 36.0;
			assertTrue(result< SIGMA_VALUE);
			
//			assertTrue(jar.getSolutionPath()[lastSolution].z> 36 - (36*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].z< 36 + (36*SIGMA_VALUE));
//			assertTrue(jar.getSolutionPath()[lastSolution].variables.get("X1")> 2- ( 2*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].variables.get("X1")< 2+ ( 2*SIGMA_VALUE));
//			assertTrue(jar.getSolutionPath()[lastSolution].variables.get("X2")> 6- ( 6*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].variables.get("X2")< 6+ ( 6*SIGMA_VALUE));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void interiorPointMinimize() {
		stageTwo();
		try {
			InteriorPointContainer jar = new InteriorPointContainer(model);
			assertTrue(jar.getMaxX() == 20);
			assertTrue(jar.getMaxY() == 16);
			int lastSolution = jar.getSolutionPath().length - 1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible == true);

			double x1= jar.getSolutionPath()[lastSolution].variables.get("X1");
			double result= Math.abs(5.0- x1)/ 5.0;
			assertTrue(result< SIGMA_VALUE);
			double x2= jar.getSolutionPath()[lastSolution].variables.get("X2");
			result= Math.abs(5.0- x2)/ 5.0;
			assertTrue(result< SIGMA_VALUE);
			double z= jar.getSolutionPath()[lastSolution].z;
			result= Math.abs(25.0- z)/ 25.0;
			assertTrue(result< SIGMA_VALUE);
			
//			assertTrue(jar.getSolutionPath()[lastSolution].z> 25 - (25*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].z< 25 + (25*SIGMA_VALUE));
//			assertTrue(jar.getSolutionPath()[lastSolution].variables.get("X1")> 5- ( 5*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].variables.get("X1")< 5+ ( 5*SIGMA_VALUE));
//			assertTrue(jar.getSolutionPath()[lastSolution].variables.get("X2")> 5- ( 5*SIGMA_VALUE) && jar.getSolutionPath()[lastSolution].variables.get("X2")< 5+ ( 5*SIGMA_VALUE));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void interiorPointNotFasible() {
		stageThree();
		try {
			InteriorPointContainer jar = new InteriorPointContainer(model);
			assertTrue(jar.getMaxX() == 36);
			assertTrue(jar.getMaxY() == 16);
			int lastSolution = jar.getSolutionPath().length - 1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible == false);
			
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].z));
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X1")));
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X2")));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void interiorPointNotAcotated() {
		stageFour();
		try {
			InteriorPointContainer jar = new InteriorPointContainer(model);
			assertTrue(jar.getMaxX() == 0);
			assertTrue(jar.getMaxY() == 0);
			int lastSolution = jar.getSolutionPath().length - 1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible == false);
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].z));
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X1")));
			assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X2")));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void interiorPointInfiniteSolution() {
		stageFive();
		try {
			InteriorPointContainer jar = new InteriorPointContainer(model);
			assertTrue(jar.getMaxX() == 0);
			assertTrue(jar.getMaxY() == 0);
			int lastSolution = jar.getSolutionPath().length - 1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible == true);
		} catch (Exception e) {
			fail();
		}
	}

}
