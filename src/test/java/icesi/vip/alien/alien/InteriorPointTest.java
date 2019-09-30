package icesi.vip.alien.alien;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import icesi.vip.alien.alien.interiorPoint.InteriorPointContainer;
import model.Model;
@RunWith(SpringRunner.class)
@SpringBootTest
class InteriorPointTest {

	
	
	public Model model;
	
	public void StageOne() {
		String type="MAXIMIZE";
		String vars="X1:C,X2:C";
		String objectiveFunction="3,5";
		String constraints="1,0,<=,4;0,2,<=,12;3,2,<=,18;1,0,>=,0;0,1,>=,0";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	public void StageTwo() {
		String type="MINIMIZE";
		String vars="X1:C,X2:C";
		String objectiveFunction="2,3";
		String constraints="0.5,0.25,<=,4;1,3,>=,20;1,1,=,10";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	
	public void StageThree() {
		String type="MINIMIZE";
		String vars="X1:C,X2:C";
		String objectiveFunction="2,3";
		String constraints="0.5,0.25,<=,4;1,3,>=,36;1,1,=,10";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	
	public void StageFour() {
		String type="MAXIMIZE";
		String vars="X1:C,X2:C,X3:C,X4:C";
		String objectiveFunction="36,30,-3,-4";
		String constraints="1,1,-1,0,<=,5;6,5,0,-1,<=,10";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	public void StageFive() {
		String type="MAXIMIZE";
		String vars="X1:C,X2:C,X3:C";
		String objectiveFunction="60,35,20";
		String constraints="8,6,1,<=,48;4,2,1.5,<=,20;2,1.5,0.5,<=,8;0,5,0,<=,5";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	
	public void StageSix() {
		String type="MAXIMIZE";
		String vars="X1:C,X2:C,X3:C,X4:C";
		String objectiveFunction="2,-4,5,-6";
		String constraints="1,4,-2,0,<=,2;-1,2,3,4,<=,1";
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
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	void interiorPointMaxinimize() {
			StageOne();
			try {
			InteriorPointContainer jar= new InteriorPointContainer(model);
			// solution z=36
			// solution x2 =6 
			// solution x1 =2 
			assertTrue(jar.getMaxX()==6);
			assertTrue(jar.getMaxY()==9);
			int lastSolution= jar.getSolutionPath().length -1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible==true);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].z)==36);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].variables.get("X1"))==2);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].variables.get("X2"))==6);
			} catch (Exception e) {
				fail();
			}
	}
	@Test
	void interiorPointMinimize() {
			StageTwo();
			try {
			InteriorPointContainer jar= new InteriorPointContainer(model);
			assertTrue(jar.getMaxX()==20);
			assertTrue(jar.getMaxY()==16);
			int lastSolution= jar.getSolutionPath().length -1;
			assertTrue(jar.getSolutionPath()[lastSolution].feasible==true);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].z)==25);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].variables.get("X1"))==5);
			assertTrue(Math.round(jar.getSolutionPath()[lastSolution].variables.get("X2"))==5);
			} catch (Exception e) {
				fail();
			}
	}
	@Test
	void interiorPointNotFasible() {
		StageThree();
		try {
		InteriorPointContainer jar= new InteriorPointContainer(model);
		assertTrue(jar.getMaxX()==36);
		assertTrue(jar.getMaxY()==16);
		int lastSolution= jar.getSolutionPath().length -1;
		assertTrue(jar.getSolutionPath()[lastSolution].feasible==false);
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].z));
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X1")));
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X2")));
		} catch (Exception e) {
			fail();
		}
	}
	@Test
	void interiorPointNotAcotated() {
		StageFour();
		try {
		InteriorPointContainer jar= new InteriorPointContainer(model);
		assertTrue(jar.getMaxX()==0);
		assertTrue(jar.getMaxY()==0);
		int lastSolution= jar.getSolutionPath().length -1;
		assertTrue(jar.getSolutionPath()[lastSolution].feasible==false);
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].z));
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X1")));
		assertTrue(Double.isNaN(jar.getSolutionPath()[lastSolution].variables.get("X2")));
		} catch (Exception e) {
			fail();
		}
	}
	@Test
	void interiorPointInfiniteSolution() {
		StageFive();
		try {
			// z 31 x1 8 x2 3 x3 3 x4 9
		InteriorPointContainer jar= new InteriorPointContainer(model);
		assertTrue(jar.getMaxX()==0);
		assertTrue(jar.getMaxY()==0);
		int lastSolution= jar.getSolutionPath().length -1;
		assertTrue(jar.getSolutionPath()[lastSolution].feasible==true);
		} catch (Exception e) {
			fail();
		}
	}

}
