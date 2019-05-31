package icesi.vip.alien.alien;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import icesi.vip.alien.alien.branchAndBound.BranchAndBoundContainer;
import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import icesi.vip.alien.alien.interiorPoint.InteriorPointContainer;
import icesi.vip.alien.alien.simplexMethod.Simplex;
import icesi.vip.alien.masterPlan.MasterPlanSchedule;
import icesi.vip.alien.materialRequirementPlanning.MRP;
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
	@RequestMapping("/simplexMethod")
	public Simplex simplexMethod(
			@RequestParam(value = "type", required = true)String opti,
			@RequestParam(value = "iteration", defaultValue = "F")String iteration,
			@RequestParam(value = "equations", required = true)String equations) throws Exception {
		equations.replaceAll("%20", " ");
		String[] equas = equations.split("n");
		Simplex simplex = new Simplex(opti, equas);
        if(iteration.equals("F")) {
        	 double[][] finalFinal = null;
             double[][] sig = simplex.nextIteration();
             while(!sig.equals(finalFinal)){
                 finalFinal = sig;
                 sig = simplex.nextIteration();
             }
        }else {
        	for (int i = 0; i < Integer.parseInt(iteration); i++) {
                simplex.nextIteration();
            }
        }
        if(simplex.getMessageSol() != null) {
        simplex.buildAnalysis();
        simplex.getIntervals();
        }
		return simplex;
	}

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
			@RequestParam(value = "securityStock", defaultValue = "1") String securityStock,
			@RequestParam(value = "costArticle", defaultValue = "1") String articleCost,
			@RequestParam(value = "maintenanceCost", defaultValue = "1") String maintenanceCost,
			@RequestParam(value = "orderingCost", defaultValue = "1") String orderingCost,
			@RequestParam(value = "lotSizingRule", defaultValue = "1") String lotSizingRule,
			@RequestParam(value = "periodicity", defaultValue = "1") String periodicity,
			@RequestParam(value = "TPeriodOFSupply", defaultValue = "1") String TPeriodOFSupply) throws Exception {

		
		try {

			MasterPlanSchedule MPS = new MasterPlanSchedule(lotSizingRule, Integer.parseInt(leadTime), Integer.parseInt(initialInventory), Integer.parseInt(securityStock), levelCode, name, 
					Double.parseDouble(articleCost), Double.parseDouble(orderingCost), Double.parseDouble(maintenanceCost), periodicity, Integer.parseInt(TPeriodOFSupply));

			scheduledReceptions = scheduledReceptions.substring(0, scheduledReceptions.length() - 1);
			grossRequeriment = grossRequeriment.substring(0, grossRequeriment.length() - 1);

			String[] gross = grossRequeriment.split("-");
			String[] schedules = scheduledReceptions.split("-");
			for (int i = 0; i < gross.length; i++) {
				MPS.addBruteRequirement(Integer.parseInt(gross[i]));
				MPS.addScheduleReception(Integer.parseInt(schedules[i]));
			}

			MPS.createMPS();
			return MPS;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}
	
	
	@JsonIgnore()
	@CrossOrigin()
	@RequestMapping("/pruebaMRP")
	public MRP showMRP(@RequestParam(value = "fatherNames") String paramFatherName, @RequestParam(value = "id") String paramId,
			@RequestParam(value = "name") String paramName, @RequestParam(value = "leadTime") String paramLeadTime,
			@RequestParam(value = "amount") String paramAmount,
			@RequestParam(value = "initialInv") String paramInitialInv, @RequestParam(value = "securityInv") String paramSecuriInv,
			@RequestParam(value = "date") String paramDate,
			@RequestParam(value = "programDelivery") String paramProgramDelivery, @RequestParam(value = "rqB") String paramRqB, 
			@RequestParam(value = "rqBDates") String paramRqBDates) {

		MRP m = new MRP();
		
		String [] id = paramId.split(";");
		
		String [] fatherName = paramFatherName.split(";");

		String [] name = paramName.split(";");

		String [] leadTime = paramLeadTime.split(";");

		String [] amount = paramAmount.split(";");

		String [] initialInv = paramInitialInv.split(";");

		String [] securiInv = paramSecuriInv.split(";");

		String [] dates = new String [Integer.parseInt(paramDate)];

		for (int i = 0; i < dates.length; i++) {
			dates[i] = (i+1+"");
		}
		
		System.out.println(paramProgramDelivery);
		System.out.println(paramRqB);
		System.out.println(paramRqBDates);
		
		String [] programDelivery = paramProgramDelivery.split("-");
		String [] reqBrutos = paramRqB.split(";");
		String [] reqBrutosDates = paramRqBDates.split("-");

		ArrayList<String> datesAr = new ArrayList<String>();
		
		ArrayList<ArrayList<Integer>> maestro = new ArrayList<>(); 
		
		ArrayList<Integer> programDeliveryAr = new ArrayList<Integer>();
		
		ArrayList<Integer> rqBAr = new ArrayList<Integer>();
		ArrayList<String> rqBDatesAr = new ArrayList<String>();
		
		for (int j = 0; j < dates.length; j++) {
			datesAr.add(dates[j]);		
		}
		
		for (int i = 1; i < programDelivery.length; i++) {	
			
			String [] tempDelivery = programDelivery[i].split(";");	
			programDeliveryAr = new ArrayList<>();
			for (int j = 0; j < tempDelivery.length; j++) {
				if (!tempDelivery[i].equals("")) {
					int temp = Integer.parseInt(tempDelivery[j]);
					programDeliveryAr.add(temp);
				}
			}
			maestro.add(programDeliveryAr);
		}
		
		for (int j = 0; j < 10; j++) {
			rqBDatesAr.add((j+1)+"");
		}

		for (int j = 0; j < reqBrutos.length; j++) {				
			if (!reqBrutos[j].equals("")) {
				int temp = Integer.parseInt(reqBrutos[j]);
				rqBAr.add(temp);					
			}
		}

		int [] leadTimeInteger = new int [fatherName.length];
		int [] amountInteger = new int [fatherName.length];
		int [] initialInvInteger = new int [fatherName.length];
		int [] securyInvInteger = new int [fatherName.length];
		
		for (int i = 0; i < securiInv.length; i++) {
			if (!amount[i].equals("")) {				
				amountInteger[i] = Integer.parseInt(amount[i]);
			}
			
			initialInvInteger[i] = Integer.parseInt(initialInv[i]);
			leadTimeInteger[i] = Integer.parseInt(leadTime[i]);
			securyInvInteger[i] = Integer.parseInt(securiInv[i]);
		}
				
		for (int i = 0; i < fatherName.length; i++) {
			System.out.println(maestro.get(i));
			int amount_1 = !amount[i].equals("") ? Integer.parseInt(amount[i]): 0; 
			m.inserProductMRP(fatherName[i], id[i], name[i], Integer.parseInt(leadTime[i]), amount_1, 
					Integer.parseInt(initialInv[i]),
					Integer.parseInt(securiInv[i]),
					datesAr, maestro.get(i));
		}

		ArrayList<Integer[][]> a = m.allProductsMRP(m.getN_Ary_Tree(),rqBAr,rqBDatesAr);
		
		System.out.println();
		System.out.println("=======Nuevo MRP=======");
		System.out.println();
		for(int i = 0; i< a.size(); i++) {
			for (int j = 0; j < a.get(i).length; j++) {
				for(int k = 0; k < a.get(i)[0].length; k++) {
					System.out.print(a.get(i)[j][k] + " ");
				}
				System.out.println();
			}
		}
		
		return m;

	}


}
