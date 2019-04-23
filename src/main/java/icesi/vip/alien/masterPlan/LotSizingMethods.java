package icesi.vip.alien.masterPlan;

import java.util.ArrayList;

public class LotSizingMethods {
	
	public static final String ANNUAL = "Annual"; 
	public static final String MONTHLY = "Monthly";
	public static final String WEEKLY = "Weekly";
	public static final String DAILY = "Daily";
	
	public static final int MONTHS_IN_YEAR = 12;
	public static final int WEEKS_IN_YEAR = 52;
	public static final int DAYS_IN_YEAR = 365;
	
	/**
	 * calculate all net requirements to ordering in all t times using Lot x Lot
	 * method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>
	 *            with the quantities of weekly articles or items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLotXLot(ArrayList<Integer> requiredArticles) {
		return requiredArticles;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantiy method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemEconomicOrderQuantiy(String periodicty, ArrayList<Integer> requiredArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		double EOQDoub = calculateEconomicOrderQuantity(periodicty, requiredArticles, itemCost, preparationCost, maintenanceCost);
		int EOQ = (int) EOQDoub;
		// Techo si EOQDoub > EOQ ya que son unidades, si s�lo se castea har� piso.
		if (EOQDoub > EOQ) {
			EOQ += 1;
		}
		System.out.println(EOQ);
		ArrayList<Integer> result = new ArrayList<>();
		int quantity = 0;
		result.add(EOQ);
		for (int i = 0; i < requiredArticles.size(); i++) {
			quantity += requiredArticles.get(i);
			if (quantity > EOQ) {
				result.add(EOQ);
				quantity = quantity-EOQ;
			} else if (i != 0) {
				result.add(0);
			}
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantity method. Default: Weeks
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public double calculateEconomicOrderQuantity(String periodicity, ArrayList<Integer> requiredArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		int totalItems;
		double D;
		double H;
		double EOQ;
		switch (periodicity) {
		case MONTHLY:
			totalItems = 0;
			for (int i = 0; i < requiredArticles.size(); i++) {
				totalItems += requiredArticles.get(i);
			}
			D = (totalItems * 1.0);

			// found the anuual maintenance cost
			H = maintenanceCost * itemCost * MONTHS_IN_YEAR;
			EOQ = Math.sqrt((2 * D * preparationCost) / H);

			return EOQ;
		case ANNUAL:
			totalItems = 0;
			for (int i = 0; i < requiredArticles.size(); i++) {
				totalItems += requiredArticles.get(i);
			}
			D = (totalItems * 1.0) / requiredArticles.size();

			// found the anuual maintenance cost
			H = maintenanceCost * itemCost;
			EOQ = Math.sqrt((2 * D * preparationCost) / H);

			return EOQ;
		case DAILY:
			totalItems = 0;
			for (int i = 0; i < requiredArticles.size(); i++) {
				totalItems += requiredArticles.get(i);
			}
			D = ((totalItems * 1.0) / requiredArticles.size()) * (DAYS_IN_YEAR);

			// found the anuual maintenance cost
			H = maintenanceCost * itemCost * DAYS_IN_YEAR;
			EOQ = Math.sqrt((2 * D * preparationCost) / H);

			return EOQ;
			//In this case, default is weekly
		default:
			// we past from week to years
			totalItems = 0;
			for (int i = 0; i < requiredArticles.size(); i++) {
				totalItems += requiredArticles.get(i);
			}
			D = ((totalItems * 1.0) / requiredArticles.size()) * (WEEKS_IN_YEAR);

			// found the anuual maintenance cost
			H = maintenanceCost * itemCost * WEEKS_IN_YEAR;
			EOQ = Math.sqrt((2 * D * preparationCost) / H);
			System.out.println(D + " - " + totalItems + " - " + H);

			return EOQ;
		}
	}

	/**
	 * calculate all net requirements to ordering in all t times using Periods Of
	 * Supply method.
	 * <p>
	 * 
	 * @param int:
	 *            amount of times to do a order.
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemPeriodsOfSupply(int t, ArrayList<Integer> requiredArticles) {

		ArrayList<Integer> orders = new ArrayList<Integer>();
		int counter = 0;
		int sumProducts = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			counter++;
			sumProducts = sumProducts + requiredArticles.get(i);
			if (counter == t) {
				counter = 0;
				orders.add(sumProducts);
				sumProducts = 0;
			} else if (i == requiredArticles.size() - 1) {
				orders.add(sumProducts);
			}
		}
		ArrayList<Integer> ordersWeekly = new ArrayList<Integer>();
		for (int i = 0; i < orders.size(); i++) {
			ordersWeekly.add(orders.get(i));
			for (int j = 0; j < t-1; j++) {
				ordersWeekly.add(0);
			}
		}
		return ordersWeekly;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Period Order
	 * Quantity method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemPeriodOrderQuantity(String periodicity, ArrayList<Integer> requiredArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		ArrayList<Integer> returnReves = new ArrayList<Integer>();
		int demand = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			demand += requiredArticles.get(i);
		}
		double frequencyRequested = (demand * 1.0)
				/ calculateEconomicOrderQuantity(periodicity, requiredArticles, itemCost, preparationCost, maintenanceCost);
		int optimalPeriodRequested = (int)Math.round((requiredArticles.size() * 1.0) / frequencyRequested);
		int quantity = 0;
		for (int j = requiredArticles.size() - 1; j >= 0; j--) {
			quantity += requiredArticles.get(j);
			if (j % optimalPeriodRequested == 0) {
				returnReves.add(quantity);
				quantity = 0;
			} else {
				returnReves.add(0);
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = returnReves.size() - 1; i >= 0; i--) {
			result.add(returnReves.get(i));
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Least Unit
	 * Cost method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLeastUnitCost(ArrayList<Integer> requiredArticles, double preparationCost,
			double maintenanceCost) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			quantity += requiredArticles.get(i);
			storedUnits += requiredArticles.get(i) * StoredTimes;
			double UnitCost = (preparationCost + (maintenanceCost * (storedUnits * 1.0))) / (quantity * 1.0);
			if (i != requiredArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (requiredArticles.get(i + 1) * (StoredTimes + 1));
				double nextUnitCost = (preparationCost + (maintenanceCost * (nextStoredUnits * 1.0)))
						/ ((quantity + requiredArticles.get(i + 1)) * 1.0);
				if (nextUnitCost > UnitCost) {
					updateAuxiliaryList(result, lastTime, i, quantity);
					quantity = 0;
					storedUnits = 0;
					StoredTimes = 0;
					lastTime = i + 1;
				} else {
					StoredTimes++;
				}
			} else {
				updateAuxiliaryList(result, lastTime, i, quantity);
			}
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Least Total
	 * Cost method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLeastTotalCost(ArrayList<Integer> requiredArticles, double preparationCost,
			double maintenanceCost) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			quantity += requiredArticles.get(i);
			storedUnits += requiredArticles.get(i) * StoredTimes;
			double difference = Math.abs(preparationCost - (maintenanceCost * (storedUnits * 1.0)));
			if (i != requiredArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (requiredArticles.get(i + 1) * (StoredTimes + 1));
				double nextDiference = Math.abs(preparationCost - (maintenanceCost * (nextStoredUnits * 1.0)));
				if (nextDiference > difference) {
					updateAuxiliaryList(result, lastTime, i, quantity);
					quantity = 0;
					storedUnits = 0;
					StoredTimes = 0;
					lastTime = i + 1;
				} else {
					StoredTimes++;
				}
			} else {
				updateAuxiliaryList(result, lastTime, i, quantity);
			}
		}
		return result;
	}

	// preguntarle a jeison
	/**
	 * Update the list with all net requeriments adding.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param int:
	 *            cost to prepare or order all articles into the list.
	 * @param int:
	 *            cost to maintenance all articles into the list.
	 * @param int:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public void updateAuxiliaryList(ArrayList<Integer> list, int j, int i, int quantity) {
		list.add(quantity);
		int k = j;
		while (k < i) {
			list.add(0);
			k++;
		}
	}

}
