package icesi.vip.alien.alien;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
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
	
	
	@Test
	public void GraphicalProblemMaximize() {
		StageOne();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		// solution z=36
		// solution x2 =6 
		// solution x1 =2 
		assertTrue(jar.getOptimalSolution().feasible);
		assertTrue(jar.getOptimalSolution().variables.get("X1")==2.0);
		assertTrue(jar.getOptimalSolution().variables.get("X2")==6.0);
		assertTrue(jar.getOptimalSolution().z== 36.0);

	}
	
	@Test
	public void GraphicalProblemMinimize() {
		StageTwo();
		GraphicalMethodContainer jar= new GraphicalMethodContainer(model);
		assertTrue(jar.getOptimalSolution().feasible);
		assertTrue(jar.getOptimalSolution().variables.get("X1")==5.0);
		assertTrue(jar.getOptimalSolution().variables.get("X2")==5.0);
		assertTrue(jar.getOptimalSolution().z== 25.0);

	}
	@Test
	public void GraphicalNotBounded() {
		
	}
	@Test
	public void GraphicalInfiniteSolutions() {
		
	}


}
