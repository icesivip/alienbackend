package icesi.vip.alien.alien;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Constraint;
import model.Model;
import model.Solution;
import model.Variable;
import solver.interior_point.BarrierMethod;

@RestController
public class AlienController {

	private static final String template = "Hello, %s! Welcome to ICESI's VIP program app.";
	private final AtomicLong counter = new AtomicLong();

	@CrossOrigin
	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	@CrossOrigin
	@RequestMapping("/test")
	public Solution.ExportSolution test(@RequestParam(value="type",required=true)String type,@RequestParam(value="vars",required=true) String vars, @RequestParam(value="objectiveFunction",required=true)String objectiveFunction,@RequestParam(value="constraints",defaultValue="")String constraints) throws Exception{
		  
		
		Model m=new Model(type);
		String[]varsS=vars.split(",");
		String[]coef=objectiveFunction.split(",");
		String[]cons=constraints.split(";");
		for (int i = 0; i < varsS.length; i++) {
			m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
		}
		int varcount=varsS.length;
		for (int i = 0; i < cons.length; i++) {
			double[]c=new double[varcount];
			String[]cons2=cons[i].split(",");
			for (int j = 0; j < c.length; j++) {
				c[j]=Double.parseDouble(cons[j]);
			}
			m.addConstraint(c, cons2[cons2.length-2], Double.parseDouble(cons2[cons2.length-1]), "C"+i);
		}
//	        m.addVariable("X1", Variable.CONTINUOUS, 2);
//	        m.addVariable("X2", Variable.CONTINUOUS, -1);
//	        m.addVariable("X3", Variable.CONTINUOUS, 2);
//	        m.addConstraint(new double[]{2,1,0}, Constraint.LESS_OR_EQUAL, 10, "");
//	        m.addConstraint(new double[]{1,2,-2}, Constraint.LESS_OR_EQUAL, 20, "");
//	        m.addConstraint(new double[]{0,1,2}, Constraint.LESS_OR_EQUAL, 5, "");
	        BarrierMethod bm=new BarrierMethod();
	        return bm.solve(m).exportFormat();
		
	}

}
