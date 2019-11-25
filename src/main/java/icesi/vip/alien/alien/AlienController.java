package icesi.vip.alien.alien;

import java.util.concurrent.atomic.AtomicLong;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import icesi.vip.alien.alien.branchAndBound.BranchAndBoundContainer;
import icesi.vip.alien.alien.craftMethod.controller.CraftContainer;
import icesi.vip.alien.alien.craftMethod.controller.CraftResponse;
import icesi.vip.alien.alien.graphicalMethod.GraphicalMethodContainer;
import icesi.vip.alien.alien.interiorPoint.InteriorPointContainer;
import icesi.vip.alien.alien.neosServer.FileUtils;
import icesi.vip.alien.alien.neosServer.NeosClient;
import icesi.vip.alien.alien.neosServer.NeosJob;
import icesi.vip.alien.alien.neosServer.NeosJobXml;
import icesi.vip.alien.alien.simplexMethod.Simplex;
import icesi.vip.alien.masterPlan.MasterPlanSchedule;
import icesi.vip.alien.materialRequirementPlanning.MaterialRequirementsPlanning;
import icesi.vip.alien.modelLP.Model;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class AlienController {

	private static final String template = "Hello, %s! Welcome to ICESI's VIP program app.";
	private final AtomicLong counter = new AtomicLong();

	@CrossOrigin
	@RequestMapping(value="/simplexMethod",method=RequestMethod.GET)
	public Simplex simplexMethod(@RequestParam(value = "type", required = true) String opti,
			@RequestParam(value = "iteration", defaultValue = "F") String iteration,
			@RequestParam(value = "equations", required = true) String equations) throws Exception {
		equations.replaceAll("%20", " ");
		String[] equas = equations.split("n");
		Simplex simplex = new Simplex(opti, equas);
		if (iteration.equals("F")) {
			simplex.solve(null);
		} else {
			for (int i = 0; i < Integer.parseInt(iteration); i++) {
				simplex.nextIteration();
			}
		}
		if (simplex.getMessageSol() != null) {
			simplex.buildAnalysis();
			simplex.getIntervals();
		}
		return simplex;
	}

	@CrossOrigin
	@RequestMapping(value="/greeting",method=RequestMethod.GET)
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}



//	@CrossOrigin
//	@RequestMapping("/test")
//	public Solution.ExportSolution test(@RequestParam(value = "type", required = true) String type,
//			@RequestParam(value = "vars", required = true) String vars,
//			@RequestParam(value = "objectiveFunction", required = true) String objectiveFunction,
//			@RequestParam(value = "constraints", defaultValue = "") String constraints) throws Exception {
//
//		Model m = new Model(type);
//		String[] varsS = vars.split(",");
//		String[] coef = objectiveFunction.split(",");
//		String[] cons = constraints.split(";");
//		for (int i = 0; i < varsS.length; i++) {
//			m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
//		}
//		int varcount = varsS.length;
//		for (int i = 0; i < cons.length; i++) {
//			double[] c = new double[varcount];
//			String[] cons2 = cons[i].split(",");
//			for (int j = 0; j < c.length; j++) {
//				c[j] = Double.parseDouble(cons2[j]);
//			}
//			m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
//		}
////	        m.addVariable("X1", Variable.CONTINUOUS, 2);
////	        m.addVariable("X2", Variable.CONTINUOUS, -1);
////	        m.addVariable("X3", Variable.CONTINUOUS, 2);
////	        m.addConstraint(new double[]{2,1,0}, Constraint.LESS_OR_EQUAL, 10, "");
////	        m.addConstraint(new double[]{1,2,-2}, Constraint.LESS_OR_EQUAL, 20, "");
////	        m.addConstraint(new double[]{0,1,2}, Constraint.LESS_OR_EQUAL, 5, "");
//		BarrierMethod bm = new BarrierMethod();
//		return bm.solve(m).exportFormat();
//
//	}

	@CrossOrigin
	@RequestMapping(value="/graphicalMethod",method=RequestMethod.GET)
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
	@RequestMapping(value="/craft", method=RequestMethod.POST)
	public CraftResponse craftMethod(@RequestBody CraftContainer container) throws Exception {
		return container.compute();
	}
	
	
	
	@CrossOrigin
	@RequestMapping(value="/neosServer" ,method=RequestMethod.POST)
	public String neosServerRemoteJob(@RequestParam(value = "model", required = true) String model,
			@RequestParam(value = "data", required = true) String data,
			@RequestParam(value = "commands", required = true) String commands,
			@RequestParam(value = "email", defaultValue = "") String email) throws Exception {
	      
		
		
		 String HOST="neos-server.org";
		   String PORT="3333";
		NeosClient client = new NeosClient(HOST, PORT);
			
	      
	      NeosJobXml exJob = new NeosJobXml("milp", "CPLEX", "AMPL"); 


			String example = model;

			/* add contents of string example to model field of job XML */

			exJob.addParam("model", example);

			example = data;

			/* add contents of string example to data field of job XML */

			exJob.addParam("data", example);

			example = commands;

			/* add contents of string example to commands field of job XML */

			exJob.addParam("commands", example); 

			/* convert job XML to string and add it to the parameter vector */

			example = email;
			/* add contents of string example to email field of job XML */
			exJob.addParam("email", example);


	      /* call submitJob() method with string representation of job XML */
	      NeosJob job = client.submitJob(exJob.toXMLString());
	      /* print results to standard output */
	      return job.getResult();

	}
	
	
	
	

	@CrossOrigin
	@RequestMapping(value="/interiorPoint",method=RequestMethod.GET)
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
	@RequestMapping(value="/branchAndBound",method=RequestMethod.GET)
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
	@RequestMapping(value="/master",method=RequestMethod.GET)
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
			switch (lotSizingRule) {
			case ("1"):
				lotSizingRule = MasterPlanSchedule.LOTXLOT;
				break;
			}

			MasterPlanSchedule MPS = new MasterPlanSchedule(lotSizingRule, Integer.parseInt(leadTime),
					Integer.parseInt(initialInventory), Integer.parseInt(securityStock), levelCode, name,
					Double.parseDouble(articleCost), Double.parseDouble(orderingCost),
					Double.parseDouble(maintenanceCost), periodicity, Integer.parseInt(TPeriodOFSupply));

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
			throw new Exception(e.getStackTrace().toString());
		}

	}

	@JsonIgnore()
	@CrossOrigin()
	@RequestMapping(value="/pruebaMRP",method=RequestMethod.GET)
	public MaterialRequirementsPlanning showMRP(@RequestParam(value = "fatherIds") String paramFatherId,
			@RequestParam(value = "id") String paramId, @RequestParam(value = "name") String paramName,
			@RequestParam(value = "leadTime") String paramLeadTime, @RequestParam(value = "amount") String paramAmount,
			@RequestParam(value = "initialInv") String paramInitialInv,
			@RequestParam(value = "securityInv") String paramSecuriInv, @RequestParam(value = "date") String paramDate,
			@RequestParam(value = "articleCost") String paramArticleCost, @RequestParam(value = "maintenanceCost") String paramMaintenanceCost,
			@RequestParam(value = "orderingCost") String paramOrderingCost, @RequestParam(value = "lotSizingRule") String paramLotSizingRule,
			@RequestParam(value = "programDelivery") String paramProgramDelivery,
			@RequestParam(value = "rqB") String paramRqB, @RequestParam(value = "rqBDates") String paramRqBDates, 
			@RequestParam(value = "periodicity") String paramPeriodicity) {

		MaterialRequirementsPlanning mrp = new MaterialRequirementsPlanning();

		String[] id = paramId.split(";");

		String[] fatherId = paramFatherId.split(";");

		String[] name = paramName.split(";");

		String[] leadTime = paramLeadTime.split(";");

		String[] amount = paramAmount.split(";");

		String[] initialInv = paramInitialInv.split(";");

		String[] securiInv = paramSecuriInv.split(";");

		String[] dates = new String[Integer.parseInt(paramDate)];
		
		String[] articleCost = paramArticleCost.split(";");
		
		String[] maintenanceCost = paramMaintenanceCost.split(";");
		
		String[] orderingCost = paramOrderingCost.split(";");
		
		String[] lotSizingRule = paramLotSizingRule.split(";");

		for (int i = 0; i < dates.length; i++) {
			dates[i] = (i + 1 + "");
		}
		
		for(int i = 0; i < fatherId.length; i++) {
			if(fatherId[i].equals("none")) {
				fatherId[i] = null;
			}
		}

		System.out.println(paramProgramDelivery);
		System.out.println(paramRqB);
		System.out.println(paramRqBDates);

		String[] programDelivery = paramProgramDelivery.split("-");
		String[] reqBrutos = paramRqB.split(";");
//		String[] reqBrutosDates = paramRqBDates.split("-");

		ArrayList<String> datesAr = new ArrayList<String>();

		ArrayList<ArrayList<Integer>> maestro = new ArrayList<>();

		ArrayList<Integer> programDeliveryAr = new ArrayList<Integer>();

		ArrayList<Integer> rqBAr = new ArrayList<Integer>();
		ArrayList<String> rqBDatesAr = new ArrayList<String>();

		for (int j = 0; j < dates.length; j++) {
			datesAr.add(dates[j]);
		}

		for (int i = 1; i < programDelivery.length; i++) {

			String[] tempDelivery = programDelivery[i].split(";");
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
			rqBDatesAr.add((j + 1) + "");
		}

		for (int j = 0; j < reqBrutos.length; j++) {
			if (!reqBrutos[j].equals("")) {
				int temp = Integer.parseInt(reqBrutos[j]);
				rqBAr.add(temp);
			}
		}

		int[] leadTimeInteger = new int[fatherId.length];
		int[] amountInteger = new int[fatherId.length];
		int[] initialInvInteger = new int[fatherId.length];
		int[] securyInvInteger = new int[fatherId.length];

		for (int i = 0; i < securiInv.length; i++) {
			if (!amount[i].equals("")) {
				amountInteger[i] = Integer.parseInt(amount[i]);
			}

			initialInvInteger[i] = Integer.parseInt(initialInv[i]);
			leadTimeInteger[i] = Integer.parseInt(leadTime[i]);
			securyInvInteger[i] = Integer.parseInt(securiInv[i]);
		}

		for (int i = 0; i < fatherId.length; i++) {
			System.out.println(maestro.get(i));
			int amount_1 = !amount[i].equals("") ? Integer.parseInt(amount[i]) : 0;
			mrp.addProduct(id[i], name[i], fatherId[i], amount_1, lotSizingRule[i], Integer.parseInt(leadTime[i]), Integer.parseInt(initialInv[i]), 
					Integer.parseInt(securiInv[i]), Double.parseDouble(articleCost[i]), Double.parseDouble(orderingCost[i]), Double.parseDouble(maintenanceCost[i]), 
					paramPeriodicity, 1, rqBAr, maestro.get(i));
//			mrp.inserProductMRP(fatherId[i], id[i], name[i], Integer.parseInt(leadTime[i]), amount_1,
//					Integer.parseInt(initialInv[i]), Integer.parseInt(securiInv[i]), datesAr, maestro.get(i));
		}

//		ArrayList<Integer[][]> a = m.allProductsMRP(m.getN_Ary_Tree(), rqBAr, rqBDatesAr);

//		System.out.println();
//		System.out.println("=======Nuevo MRP=======");
//		System.out.println();
//		for (int i = 0; i < a.size(); i++) {
//			for (int j = 0; j < a.get(i).length; j++) {
//				for (int k = 0; k < a.get(i)[0].length; k++) {
//					System.out.print(a.get(i)[j][k] + " ");
//				}
//				System.out.println();
//			}
//		}

		return mrp;

	}

}
