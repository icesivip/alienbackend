package icesi.vip.alien.alien;



import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.graphicalMethod.GraphicalMethod;
import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import icesi.vip.alien.modelLP.Constraint;
import icesi.vip.alien.modelLP.Model;
import icesi.vip.alien.modelLP.Variable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphicalMethodTest {

	private Model model;
	private GraphicalMethod graphi;
	
	public final static double SIGMA_VALUE= 0.01;

	
	public void ModelOneTestingModel() {
		try {
			Model m = new Model(Model.MAXIMIZE);
			m.addVariable("X1", Variable.CONTINUOUS, 0);
			m.addVariable("X2", Variable.CONTINUOUS, 225);
			m.addConstraint(new double[] { 1, 0 }, Constraint.LESS_OR_EQUAL, 50, "C1");
			m.addConstraint(new double[] { 18, 8 }, Constraint.LESS_OR_EQUAL, 1800, "C2");
			m.addConstraint(new double[] { 1.6, 1 }, Constraint.LESS_OR_EQUAL, 240, "C3");
			m.addConstraint(new double[] { 2, 0.5 }, Constraint.LESS_OR_EQUAL, 164, "C4");
			model = m;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
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
		assertTrue(jar.getOptimalSolution().feasible);
		double x1= jar.getOptimalSolution().variables.get("X1");
		double result= Math.abs(2.0- x1)/ 2.0;
		assertTrue(result<SIGMA_VALUE );
		double x2= jar.getOptimalSolution().variables.get("X2");
		result= Math.abs(6.0- x2)/ 6.0;
		assertTrue(result< SIGMA_VALUE);
		double z= jar.getOptimalSolution().z;
		result= Math.abs(36.0- z)/ 36.0;
		assertTrue(result< SIGMA_VALUE);
//		assertTrue(jar.getOptimalSolution().variables.get("X1")>2.0-(2.0*SIGMA_VALUE) && jar.getOptimalSolution().variables.get("X1")<2.0+(2.0*SIGMA_VALUE));
//		assertTrue(jar.getOptimalSolution().variables.get("X2")>6.0-(6.0*SIGMA_VALUE) && jar.getOptimalSolution().variables.get("X2")<6.0+(6.0*SIGMA_VALUE));
//		assertTrue(jar.getOptimalSolution().z>36.0-(36.0*SIGMA_VALUE) && jar.getOptimalSolution().z<36.0+(36.0*SIGMA_VALUE));
	}
	
	@Test
	public void graphicalProblemMinimize() {
		stageTwo();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		assertTrue(jar.getOptimalSolution().feasible);
		
//		double x1= jar.getOptimalSolution().variables.get("X1");
//		assertTrue(x1>5.0 - (5.0*SIGMA_VALUE) && x1< 5.0 +( 5.0 * SIGMA_VALUE));
//		double x2= jar.getOptimalSolution().variables.get("X2");
//		assertTrue(x2>5.0 - (5.0*SIGMA_VALUE) && x2< 5.0 +( 5.0 * SIGMA_VALUE));
//		assertTrue(jar.getOptimalSolution().z>25.0 -( 25.0 * SIGMA_VALUE)&& jar.getOptimalSolution().z<25.0 +( 25.0 * SIGMA_VALUE) );
		
		double x1= jar.getOptimalSolution().variables.get("X1");
		double result= Math.abs(5.0- x1)/ 5.0;
		assertTrue(result< SIGMA_VALUE);
		double x2= jar.getOptimalSolution().variables.get("X2");
		result= Math.abs(5.0- x2)/ 5.0;
		assertTrue(result< SIGMA_VALUE);
		double z= jar.getOptimalSolution().z;
		result= Math.abs(25.0- z)/ 25.0;
		assertTrue(result< SIGMA_VALUE);
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





	@Test
	public void GraphicalMethodTestModel() {
		ModelOneTestingModel();
		graphi = new GraphicalMethod();
		
		double z= graphi.solve(model).getZ();
		double result= Math.abs(62307.0- z)/ 62307.0;
		assertTrue(result< SIGMA_VALUE);
		
//		assertTrue(graphi.solve(model).getZ() > 62307.6 - Constraint.BASE_TOLERANCE
//				&& graphi.solve(model).getZ() < 62307.6 + Constraint.BASE_TOLERANCE);
	}
}
