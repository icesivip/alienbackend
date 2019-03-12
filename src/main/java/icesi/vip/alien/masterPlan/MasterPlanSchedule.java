package icesi.vip.alien.masterPlan;

import java.util.ArrayList;

public class MasterPlanSchedule {
	
	public static final String LOTXLOT = "Lot for Lot";
	public static final String ECONOMIC_ORDER_QUANTITY= "Economic Order Quantity";
	public static final String PERIODS_OF_SUPPLY = "Periods Of Suply";
	public static final String PERIOD_ORDER_QUANTITY = "Period Order Quantity";
	public static final String LEAST_UNIT_COST = "Least Unit Cost";
	public static final String LEAST_TOTAL_COST = "Least Total Cost";
	
	public static final String ANNUAL = "Anual"; 
	public static final String SOMETHING = "";
	public static final String MONTHLY = "Mensual";
	public static final String WEEKLY = "Semanal";
	public static final String DAILY = "Diario";
	
	public static final String UNKNOWN = "Unknown";
	
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
	
	public static LotSizingMethods lotSizingMethods;

	private ArrayList<Integer> bruteRequirements;
	private ArrayList<Integer> scheduledReceptions;
	private ArrayList<Integer> scheduledAvailableStock;
	private ArrayList<Integer> netRequirements;
	private ArrayList<Integer> planOrders;
	private ArrayList<Integer> releasedPlanOrders;
	
	public MasterPlanSchedule(String lotSizingMethod, int leadTime, int initialStock, int securityStock,
			String productCode, String productName, double costArticle, double preparationCost, double maintenanceCost,
			String periodicity) {
		this.lotSizingMethod = lotSizingMethod;
		this.leadTime = leadTime;
		this.initialStock = initialStock;
		this.securityStock = securityStock;
		this.productCode = productCode;
		this.productName = productName;
		this.costArticle = costArticle;
		this.preparationCost = preparationCost;
		this.maintenanceCost = maintenanceCost;
		this.periodicity = periodicity;
		
		lotSizingMethods = new LotSizingMethods();
		
		bruteRequirements = new ArrayList<Integer>();
		scheduledReceptions = new ArrayList<Integer>();
		scheduledAvailableStock = new ArrayList<Integer>();
		netRequirements = new ArrayList<Integer>();
		planOrders = new ArrayList<Integer>();
		releasedPlanOrders = new ArrayList<Integer>();
	}
	
	public void addBruteRequirement(int toBeAdded) {
		bruteRequirements.add(toBeAdded);
	}
	
	public void addScheduleReception(int toBeAdded) {
		scheduledReceptions.add(toBeAdded);
	}
	
	public void calculatePlanOrders() {
		switch(lotSizingMethod){
		case(ECONOMIC_ORDER_QUANTITY):
			planOrders = lotSizingMethods.systemEconomicOrderQuantiy(bruteRequirements, costArticle, preparationCost, maintenanceCost);
			break;
		case(PERIODS_OF_SUPPLY):
			planOrders = lotSizingMethods.systemPeriodsOfSupply(1, bruteRequirements);
			break;
		case(PERIOD_ORDER_QUANTITY):
			planOrders = lotSizingMethods.systemPeriodOrderQuantity(bruteRequirements, costArticle, preparationCost, maintenanceCost);
			break;
		case(LEAST_UNIT_COST):
			planOrders = lotSizingMethods.systemLeastUnitCost(bruteRequirements, preparationCost, maintenanceCost);
			break;
		case(LEAST_TOTAL_COST):
			planOrders = lotSizingMethods.systemLeastTotalCost(bruteRequirements, preparationCost, maintenanceCost);
			break;
		}
	}
	
	public void hopeThisWorks() {
		boolean lotxlot = false;
		if(!lotSizingMethod.equals(LOTXLOT)) {
			calculatePlanOrders();
		}else {
			lotxlot = true;
		}
		int actualNetReq = bruteRequirements.get(0) + securityStock - initialStock - scheduledReceptions.get(0);
		int actualStockAvailable = 0;
		if(actualNetReq > 0) {
			netRequirements.add(actualNetReq);
			if(lotxlot) {
				planOrders.add(netRequirements.get(0));
			}
		}else {
			if(lotxlot) {
				planOrders.add(0);
			}
			netRequirements.add(0);
		}
		actualStockAvailable = planOrders.get(0) + initialStock + scheduledReceptions.get(0) - bruteRequirements.get(0);
		scheduledAvailableStock.add(actualStockAvailable);
		for(int i = 1; i < bruteRequirements.size(); i++) {
			actualNetReq = bruteRequirements.get(i) + securityStock - scheduledAvailableStock.get(i-1) - scheduledReceptions.get(i);
			if(actualNetReq > 0) {
				netRequirements.add(actualNetReq);
				if(lotxlot) {
					planOrders.add(netRequirements.get(i));
				}
			}else {
				if(lotxlot) {
					planOrders.add(0);
				}
				netRequirements.add(0);
			}
			actualStockAvailable = planOrders.get(i) + scheduledAvailableStock.get(i-1) + scheduledReceptions.get(i) - bruteRequirements.get(i);
			scheduledAvailableStock.add(actualStockAvailable);
		}
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
	
	
}
