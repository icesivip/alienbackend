package icesi.vip.alien.alien;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.branchAndBound.BranchAndBound;
import icesi.vip.alien.alien.interiorPoint.BarrierMethod;
import icesi.vip.alien.modelLP.Constraint;
import icesi.vip.alien.modelLP.Model;
import icesi.vip.alien.modelLP.Variable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BarrierMethodTest {

	BranchAndBound branch;

	Model model;
	
	
	public final static double SIGMA_VALUE= 0.01;


	public BarrierMethodTest() {
	}

	public void ModelOne() {
		try {
			Model m = new Model(Model.MAXIMIZE);
			m.addVariable("X1", Variable.CONTINUOUS, 0);
			m.addVariable("X2", Variable.CONTINUOUS, 0);
			m.addVariable("X3", Variable.CONTINUOUS, 0);
			m.addVariable("Y1", Variable.CONTINUOUS, 0);
			m.addVariable("Y2", Variable.CONTINUOUS, 0);
			m.addVariable("Y3", Variable.CONTINUOUS, 0);
			m.addVariable("V1", Variable.CONTINUOUS, 40);
			m.addVariable("V2", Variable.CONTINUOUS, 60);
			m.addVariable("V3", Variable.CONTINUOUS, 55);
			m.addVariable("W1", Variable.CONTINUOUS, 35);
			m.addVariable("W2", Variable.CONTINUOUS, 40);
			m.addVariable("W3", Variable.CONTINUOUS, 44);
			m.addVariable("IP1", Variable.CONTINUOUS, -10);
			m.addVariable("IP2", Variable.CONTINUOUS, -10);
			m.addVariable("IP3", Variable.CONTINUOUS, 0);
			m.addVariable("IQ1", Variable.CONTINUOUS, -10);
			m.addVariable("IQ2", Variable.CONTINUOUS, -10);
			m.addVariable("IQ3", Variable.CONTINUOUS, 0);
			m.addVariable("Z", Variable.CONTINUOUS, -3);
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 50, "DEMPSMES1");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 45, "DEMPSMES2");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 50, "DEMPSMES3");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 43, "DEMQTMES1");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 50, "DEMQTMES2");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 40, "DEMQTMES3");
			m.addConstraint(new double[] { 3, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 1200, "HLMES1");
			m.addConstraint(new double[] { 0, 3, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 160, "HLMES2");
			m.addConstraint(new double[] { 0, 0, 3, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 190, "HLMES3");
			m.addConstraint(new double[] { 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 2140, "HLES4");
			m.addConstraint(new double[] { 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 150, "HLMES5");
			m.addConstraint(new double[] { 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					Constraint.LESS_OR_EQUAL, 110, "HLMES6");
			m.addConstraint(new double[] { 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0 },
					Constraint.EQUAL, -10, "BALPSMES1");
			m.addConstraint(new double[] { 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0 },
					Constraint.EQUAL, 0, "BALPSMES2");
			m.addConstraint(new double[] { 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0 },
					Constraint.EQUAL, 0, "BALPSMES3");
			m.addConstraint(new double[] { 0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0 },
					Constraint.EQUAL, -5, "BALQTMES1");
			m.addConstraint(new double[] { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, -1, 0, 0 },
					Constraint.EQUAL, 0, "BALQTMES2");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, -1, 0 },
					Constraint.EQUAL, 0, "BALQTMES3");
			m.addConstraint(new double[] { 4, 4, 4, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
					Constraint.LESS_OR_EQUAL, 0, "MATPRIMA");
			m.addConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
					Constraint.LESS_OR_EQUAL, 710, "MAXMATPRIMA");
			model = m;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testModel() {
		ModelOne();
		BarrierMethod bm = new BarrierMethod();
		branch = new BranchAndBound(bm);
		System.out.println(model);
		System.out.println(branch.solve(model));
		System.out.println(branch.solve(model).getZ());
		
		double z= branch.solve(model).getZ();
		double result= Math.abs(7705- z)/ 7705;
		assertTrue(result< SIGMA_VALUE);
		
//		assertTrue(branch.solve(model).getZ() > 7705 - Constraint.BASE_TOLERANCE
//				&& branch.solve(model).getZ() < 7705 + Constraint.BASE_TOLERANCE);
	}
}
