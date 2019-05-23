package icesi.vip.alien.alien;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import icesi.vip.alien.alien.branchAndBound.BranchAndBoundContainer;
import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import icesi.vip.alien.alien.interiorPoint.InteriorPointContainer;
import icesi.vip.alien.masterPlan.MasterPlanSchedule;
import lombok.extern.log4j.Log4j2;
import model.Constraint;
import model.Model;
import model.Solution;
import model.Variable;
import solver.interior_point.BarrierMethod;

@RestController
@Log4j2
public class AlienController {

	private static final String template = "Hello, %s! Welcome to ICESI's VIP program app.";
	private final AtomicLong counter = new AtomicLong();

	@CrossOrigin
	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@CrossOrigin
	@RequestMapping("/tutorial")
	public Tutorial tutorial(@RequestParam(value = "nombre", defaultValue = "Tutorial") String nombre,
			@RequestParam(value = "fecha", defaultValue = "18/03/2019") String fecha) {
		return new Tutorial(fecha, nombre);
	}

	@CrossOrigin
	@RequestMapping("/test")
	public Solution.ExportSolution test(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "vars", required = true) String vars,
			@RequestParam(value = "objectiveFunction", required = true) String objectiveFunction,
			@RequestParam(value = "constraints", defaultValue = "") String constraints) throws Exception {

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
//	        m.addVariable("X1", Variable.CONTINUOUS, 2);
//	        m.addVariable("X2", Variable.CONTINUOUS, -1);
//	        m.addVariable("X3", Variable.CONTINUOUS, 2);
//	        m.addConstraint(new double[]{2,1,0}, Constraint.LESS_OR_EQUAL, 10, "");
//	        m.addConstraint(new double[]{1,2,-2}, Constraint.LESS_OR_EQUAL, 20, "");
//	        m.addConstraint(new double[]{0,1,2}, Constraint.LESS_OR_EQUAL, 5, "");
		BarrierMethod bm = new BarrierMethod();
		return bm.solve(m).exportFormat();

	}

	@CrossOrigin
	@RequestMapping("/graphicalMethod")
	public GraphicalMethodContainer graphicalMethod(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "vars", required = true) String vars,
			@RequestParam(value = "objectiveFunction", required = true) String objectiveFunction,
			@RequestParam(value = "constraints", defaultValue = "") String constraints) throws Exception {
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

		return new GraphicalMethodContainer(m);

	}
	
	
	@CrossOrigin
	@RequestMapping("/interiorPoint")
	public InteriorPointContainer interiorPoint(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "vars", required = true) String vars,
			@RequestParam(value = "objectiveFunction", required = true) String objectiveFunction,
			@RequestParam(value = "constraints", defaultValue = "") String constraints) throws Exception {
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

		return new InteriorPointContainer(m);

	}
	
	@CrossOrigin
	@RequestMapping("/branchAndBound")
	public BranchAndBoundContainer branchAndBound(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "vars", required = true) String vars,
			@RequestParam(value = "objectiveFunction", required = true) String objectiveFunction,
			@RequestParam(value = "constraints", defaultValue = "") String constraints) throws Exception {
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

		return new BranchAndBoundContainer(m);

	}
	

	@CrossOrigin
	@RequestMapping("/master")
	public MasterPlanSchedule solucion(

			@RequestParam(value = "scheduledReceptions", defaultValue = "1") String scheduledReceptions,
			@RequestParam(value = "grossRequeriment", defaultValue = "1") String grossRequeriment,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "levelCode", required = true) String levelCode,
			@RequestParam(value = "initialInventory", required = true) String initialInventory,
			@RequestParam(value = "leadTime", defaultValue = "1") String leadTime,
			@RequestParam(value = "maintenanceCost", defaultValue = "1") String maintenanceCost,
			@RequestParam(value = "orderingCost", defaultValue = "1") String orderingCost,
			@RequestParam(value = "lotSizingRule", defaultValue = "1") String lotSizingRule) throws Exception {

		// log.info("funciona");

		try {
			switch (lotSizingRule) {
			case ("1"):
				lotSizingRule = MasterPlanSchedule.LOTXLOT;
				break;
			}

			MasterPlanSchedule m = new MasterPlanSchedule(lotSizingRule, Integer.parseInt(leadTime),
					Integer.parseInt(initialInventory), 2, levelCode, name, 1.0, Double.parseDouble(orderingCost),
					Double.parseDouble(maintenanceCost), "" + 1);

			scheduledReceptions = scheduledReceptions.substring(0, scheduledReceptions.length() - 1);
			grossRequeriment = grossRequeriment.substring(0, grossRequeriment.length() - 1);

			String[] gross = grossRequeriment.split("-");
			String[] schedules = scheduledReceptions.split("-");
			for (int i = 0; i < gross.length; i++) {
				m.addBruteRequirement(Integer.parseInt(gross[i]));
				m.addScheduleReception(Integer.parseInt(schedules[i]));
			}
			// m.calculatePlanOrders();
			m.hopeThisWorks();
			return m;
		} catch (Exception e) {
			throw new Exception(e.getStackTrace().toString());
		}

	}

}
