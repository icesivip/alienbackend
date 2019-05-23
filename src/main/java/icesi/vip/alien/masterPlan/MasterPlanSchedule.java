package icesi.vip.alien.masterPlan;

import java.util.ArrayList;

public class MasterPlanSchedule {
	
	public static final String LOTXLOT = "Lot for Lot";
	public static final String ECONOMIC_ORDER_QUANTITY= "Economic Order Quantity";
	public static final String PERIODS_OF_SUPPLY = "Periods Of Suply";
	public static final String PERIOD_ORDER_QUANTITY = "Period Order Quantity";
	public static final String LEAST_UNIT_COST = "Least Unit Cost";
	public static final String LEAST_TOTAL_COST = "Least Total Cost";
	
	public static final String ANNUAL = "Annual"; 
	public static final String MONTHLY = "Monthly";
	public static final String WEEKLY = "Weekly";
	public static final String DAILY = "Daily";
		
	private String lotSizingMethod;
	private int leadTime;
	private int initialStock;
	private int securityStock;
	private String productCode;
	private String productName;
	private double costArticle;
	private double preparationCost;
	private double maintenanceCost;
	private String periodicity;
	private int TPeriodOFSupply;
	
	public static LotSizingMethods lotSizingMethods;

	private ArrayList<Integer> bruteRequirements;
	private ArrayList<Integer> scheduledReceptions;
	private ArrayList<Integer> scheduledAvailableStock;
	private ArrayList<Integer> netRequirements;
	private ArrayList<Integer> planOrders;
	private ArrayList<Integer> planOrdersLxL;
	private ArrayList<Integer> releasedPlanOrders;
	
	public MasterPlanSchedule(String lotSizingMethod, int leadTime, int initialStock, int securityStock,
			String productCode, String productName, double costArticle, double preparationCost, double maintenanceCost,
			String periodicity, int TPeriodOFSupply) {
		this.lotSizingMethod = lotSizingMethod;
		this.leadTime = leadTime;
		this.initialStock = initialStock;
		this.securityStock = securityStock;
		this.productCode = productCode;
		this.setProductName(productName);
		this.costArticle = costArticle;
		this.preparationCost = preparationCost;
		this.maintenanceCost = maintenanceCost;
		this.periodicity = periodicity;
		this.TPeriodOFSupply = TPeriodOFSupply;
		
		lotSizingMethods = new LotSizingMethods();
		
		bruteRequirements = new ArrayList<Integer>();
		scheduledReceptions = new ArrayList<Integer>();
		scheduledAvailableStock = new ArrayList<Integer>();
		netRequirements = new ArrayList<Integer>();
		planOrders = new ArrayList<Integer>();
		planOrdersLxL = new ArrayList<>();
		releasedPlanOrders = new ArrayList<Integer>();
	}
	
	public void reset() {
		scheduledAvailableStock = new ArrayList<Integer>();
		netRequirements = new ArrayList<Integer>();
	}
	
	public void addBruteRequirement(int toBeAdded) {
		bruteRequirements.add(toBeAdded);
	}
	
	public void addScheduleReception(int toBeAdded) {
		scheduledReceptions.add(toBeAdded);
	}
	
	public void calculatePlanOrders() {
		switch(lotSizingMethod){
		case(ECONOMIC_ORDER_QUANTITY):/*No*/
			planOrders = lotSizingMethods.systemEconomicOrderQuantiy(periodicity, bruteRequirements, planOrdersLxL, costArticle, preparationCost, maintenanceCost);
			break;
		case(PERIODS_OF_SUPPLY):/*No*/
			planOrders = lotSizingMethods.systemPeriodsOfSupply(TPeriodOFSupply, bruteRequirements);
			break;
		case(PERIOD_ORDER_QUANTITY):/*Sí*/
			planOrders = lotSizingMethods.systemPeriodOrderQuantity(periodicity, bruteRequirements, planOrdersLxL, costArticle, preparationCost, maintenanceCost);
			break;
		case(LEAST_UNIT_COST):/*Sí*/
			planOrders = lotSizingMethods.systemLeastUnitCost(planOrdersLxL, preparationCost, maintenanceCost);
			break;
		case(LEAST_TOTAL_COST):/*Sí*/
			planOrders = lotSizingMethods.systemLeastTotalCost(planOrdersLxL, preparationCost, maintenanceCost);
			break;
		}
	}
	
	//Ordenar inventario de seguridad si quedó menor
	
	//Quiza cambiar nombre de planOrder por lxl y usar un nuevo plan orders
	public void makeLXLMPS() {
		int actualNetReq = bruteRequirements.get(0) + securityStock - initialStock - scheduledReceptions.get(0);
		int actualStockAvailable = 0;
		if(actualNetReq > 0) {
			netRequirements.add(actualNetReq);
			planOrdersLxL.add(netRequirements.get(0));
		}else {
			planOrdersLxL.add(0);
			netRequirements.add(0);
		}
		actualStockAvailable = planOrdersLxL.get(0) + initialStock + scheduledReceptions.get(0) - bruteRequirements.get(0);
		scheduledAvailableStock.add(actualStockAvailable);
		for(int i = 1; i < bruteRequirements.size(); i++) {
			actualNetReq = bruteRequirements.get(i) + securityStock - scheduledAvailableStock.get(i-1) - scheduledReceptions.get(i);
			if(actualNetReq > 0) {
				netRequirements.add(actualNetReq);
				planOrdersLxL.add(netRequirements.get(i));
			}else {
				planOrdersLxL.add(0);
				netRequirements.add(0);
			}
			actualStockAvailable = planOrdersLxL.get(i) + 
					scheduledAvailableStock.get(i-1) + 
					scheduledReceptions.get(i) - 
					bruteRequirements.get(i);
			scheduledAvailableStock.add(actualStockAvailable);
		}
	}
	
	public void calculateReleasedPlanOrders() {
		for(int i = leadTime; i < planOrders.size(); i++) {
			releasedPlanOrders.add(planOrders.get(i));
		}
		for(int i = 0; i < leadTime; i++) {
			releasedPlanOrders.add(0);
		}
	}
	
	public void createMPS() {
		if(!lotSizingMethod.equals(LOTXLOT)) {
			if(!lotSizingMethod.equals(PERIODS_OF_SUPPLY)) {
				makeLXLMPS();
				reset();
			}
			calculatePlanOrders();
		}else {
			makeLXLMPS();
			planOrders = planOrdersLxL;
			calculateReleasedPlanOrders();
			return;
		}
		int actualNetReq = bruteRequirements.get(0) + securityStock - initialStock - scheduledReceptions.get(0);
		int actualStockAvailable = 0;
		if(actualNetReq > 0) {
			netRequirements.add(actualNetReq);
		}else {
			netRequirements.add(0);
		}
		actualStockAvailable = planOrders.get(0) + initialStock + scheduledReceptions.get(0) - bruteRequirements.get(0);
		scheduledAvailableStock.add(actualStockAvailable);
		for(int i = 1; i < bruteRequirements.size(); i++) {
			actualNetReq = bruteRequirements.get(i) + securityStock - scheduledAvailableStock.get(i-1) - scheduledReceptions.get(i);
			if(actualNetReq > 0) {
				netRequirements.add(actualNetReq);
			}else {
				netRequirements.add(0);
			}
			actualStockAvailable = planOrders.get(i) + 
					scheduledAvailableStock.get(i-1) + 
					scheduledReceptions.get(i) - 
					bruteRequirements.get(i);
			scheduledAvailableStock.add(actualStockAvailable);
		}
		calculateReleasedPlanOrders();
		
	}

	public ArrayList<Integer> getScheduledAvailableStock() {
		return scheduledAvailableStock;
	}

	public void setScheduledAvailableStock(ArrayList<Integer> scheduledAvailableStock) {
		this.scheduledAvailableStock = scheduledAvailableStock;
	}

	public ArrayList<Integer> getNetRequirements() {
		return netRequirements;
	}

	public void setNetRequirements(ArrayList<Integer> netRequirements) {
		this.netRequirements = netRequirements;
	}

	public ArrayList<Integer> getPlanOrders() {
		return planOrders;
	}

	public void setPlanOrders(ArrayList<Integer> planOrders) {
		this.planOrders = planOrders;
	}

	public int getTPeriodOFSupply() {
		return TPeriodOFSupply;
	}

	public void setTPeriodOFSupply(int tPeriodOFSupply) {
		TPeriodOFSupply = tPeriodOFSupply;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLotSizingMethod() {
		return lotSizingMethod;
	}

	public void setLotSizingMethod(String lotSizingMethod) {
		this.lotSizingMethod = lotSizingMethod;
	}

	public int getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}

	public int getInitialStock() {
		return initialStock;
	}

	public void setInitialStock(int initialStock) {
		this.initialStock = initialStock;
	}

	public int getSecurityStock() {
		return securityStock;
	}

	public void setSecurityStock(int securityStock) {
		this.securityStock = securityStock;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public double getCostArticle() {
		return costArticle;
	}

	public void setCostArticle(double costArticle) {
		this.costArticle = costArticle;
	}

	public double getPreparationCost() {
		return preparationCost;
	}

	public void setPreparationCost(double preparationCost) {
		this.preparationCost = preparationCost;
	}

	public double getMaintenanceCost() {
		return maintenanceCost;
	}

	public void setMaintenanceCost(double maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public ArrayList<Integer> getBruteRequirements() {
		return bruteRequirements;
	}

	public void setBruteRequirements(ArrayList<Integer> bruteRequirements) {
		this.bruteRequirements = bruteRequirements;
	}

	public ArrayList<Integer> getScheduledReceptions() {
		return scheduledReceptions;
	}

	public void setScheduledReceptions(ArrayList<Integer> scheduledReceptions) {
		this.scheduledReceptions = scheduledReceptions;
	}

	public ArrayList<Integer> getReleasedPlanOrders() {
		return releasedPlanOrders;
	}

	public void setReleasedPlanOrders(ArrayList<Integer> releasedPlanOrders) {
		this.releasedPlanOrders = releasedPlanOrders;
	}

	public ArrayList<Integer> getPlanOrdersLxL() {
		return planOrdersLxL;
	}

	public void setPlanOrdersLxL(ArrayList<Integer> planOrdersLxL) {
		this.planOrdersLxL = planOrdersLxL;
	}
	
	
}