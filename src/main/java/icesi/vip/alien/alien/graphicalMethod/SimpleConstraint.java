package icesi.vip.alien.alien.graphicalMethod;

import model.Constraint;

public class SimpleConstraint {
	 private double xValue;
	   private double yValue;
	   private String type;
	   private double rightValue;
	   private String name;
	    
	    public SimpleConstraint(Constraint c) throws Exception{
	        if(c.getVariableCount()>2){
	            throw new Exception("A SimpleContraint instance can only support two variable Constraints.");
	        }else{
	            xValue=c.getVariablePonderation(0);
	            yValue=c.getVariablePonderation(1);
	            type=c.getType();
	            name=c.getName();
	            rightValue=c.getRightSideValue();
	        }
	    }

	    public double getxValue() {
	        return xValue;
	    }

	    public double getyValue() {
	        return yValue;
	    }

	    public String getType() {
	        return type;
	    }

	    public double getRightValue() {
	        return rightValue;
	    }
	    
	    public String getName() {
	    	return this.name;
	    }

	    
}
