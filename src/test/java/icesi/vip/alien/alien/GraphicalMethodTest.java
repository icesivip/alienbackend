package icesi.vip.alien.alien;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import model.Model;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphicalMethodTest {

	private Model model;
	
	
	public final static double SIGMA_VALUE= 0.01;

	
	public void stageOne() {
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
	public void stageTwo() {
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
	public void stageTree() {
		String type="MAXIMIZE";
		String vars="X1:C,X2:C";
		String objectiveFunction="4,6";
		String constraints="3,4,<=,12;3,5,>=,30;0,1,=,10";
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
	public void graphicalProblemMaximize() {
		stageOne();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		// solution z=36
		// solution x2 =6 
		// solution x1 =2 
		
		
		assertTrue(jar.getOptimalSolution().feasible);
		
		// To test x1    2- sigma*2<value<2 + sigma*2
		assertTrue(jar.getOptimalSolution().variables.get("X1")>2.0-(2.0*SIGMA_VALUE) && jar.getOptimalSolution().variables.get("X1")<2.0+(2.0*SIGMA_VALUE));
		assertTrue(jar.getOptimalSolution().variables.get("X2")>6.0-(6.0*SIGMA_VALUE) && jar.getOptimalSolution().variables.get("X2")<6.0+(6.0*SIGMA_VALUE));
		assertTrue(jar.getOptimalSolution().z>36.0-(36.0*SIGMA_VALUE) && jar.getOptimalSolution().z<36.0+(36.0*SIGMA_VALUE));

	}
	
	@Test
	public void graphicalProblemMinimize() {
		stageTwo();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		assertTrue(jar.getOptimalSolution().feasible);
		double x1= jar.getOptimalSolution().variables.get("X1");
		assertTrue(x1>5.0 - (5.0*SIGMA_VALUE) && x1< 5.0 +( 5.0 * SIGMA_VALUE));
		double x2= jar.getOptimalSolution().variables.get("X2");
		assertTrue(x2>5.0 - (5.0*SIGMA_VALUE) && x2< 5.0 +( 5.0 * SIGMA_VALUE));
		assertTrue(jar.getOptimalSolution().z>25.0 -( 25.0 * SIGMA_VALUE)&& jar.getOptimalSolution().z<25.0 +( 25.0 * SIGMA_VALUE) );
	}
	
	@Test
	public void graphicalNotBounded() {
		
	}
	@Test
	public void graphicalInfiniteSolutions() {
		stageTree();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		assertNull(jar.getOptimalSolution());
	}


}
