package icesi.vip.alien.masterPlan;

import java.util.ArrayList;

public class LotSizingMethods {

	/**
	 * calculate all net requirements to ordering in all t times using Lot x Lot
	 * method.
	 * <p>
	 * 
	 * @param ArrayList<Integer> with the quantities of weekly articles or items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLotXLot(ArrayList<Integer> weeklyArticles) {
		return weeklyArticles;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantiy method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param double: unit cost of articles into the list.
	 * @param double: cost to prepare or order all articles into the list.
	 * @param double: cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemEconomicOrderQuantiy(ArrayList<Integer> weeklyArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		int EOQ = (int) calculateEconomicOrderQuantity(weeklyArticles, itemCost, preparationCost, maintenanceCost);
		ArrayList<Integer> result = new ArrayList<>();
		int quantity = 0;
		result.add(EOQ);
		for (int i = 0; i < weeklyArticles.size(); i++) {
			quantity += weeklyArticles.get(i);
			if (quantity >= EOQ) {
				result.add(EOQ);
				quantity = weeklyArticles.get(i);
			} else if (i != 0) {
				result.add(0);
			}
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantity method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param double: unit cost of articles into the list.
	 * @param double: cost to prepare or order all articles into the list.
	 * @param double: cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public double calculateEconomicOrderQuantity(ArrayList<Integer> weeklyArticles, double itemCost,
			double preparationCost, double maintenanceCost) {

		// we past from week to years
		int totalItems = 0;
		for (int i = 0; i < weeklyArticles.size(); i++) {
			totalItems += weeklyArticles.get(i);
		}
		double D = ((totalItems * 1.0) / weeklyArticles.size()) * (52);

		// found the anuual maintenance cost
		double H = maintenanceCost * itemCost * 52;
		double EOQ = Math.sqrt((2 * D * preparationCost) / H);

		return EOQ;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Periods Of
	 * Supplymethod.
	 * <p>
	 * 
	 * @param int: amount of times to do a order.
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemPeriodsOfSupply(int t, ArrayList<Integer> weeklyArticles) {

		ArrayList<Integer> orders = new ArrayList<Integer>();
		int counter = 0;
		int sumProducts = 0;
		for (int i = 0; i < weeklyArticles.size(); i++) {
			counter++;
			sumProducts = sumProducts + weeklyArticles.get(i);
			if (counter == t) {
				counter = 0;
				orders.add(sumProducts);
				sumProducts = 0;
			} else if (i == weeklyArticles.size() - 1) {
				orders.add(sumProducts);
			}
		}
		return orders;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Period Order
	 * Quantity method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param double: unit cost of articles into the list.
	 * @param double: cost to prepare or order all articles into the list.
	 * @param double: cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemPeriodOrderQuantity(ArrayList<Integer> weeklyArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		ArrayList<Integer> returnReves = new ArrayList<Integer>();
		int demand = 0;
		for (int i = 0; i < weeklyArticles.size(); i++) {
			demand += weeklyArticles.get(i);
		}
		double frequencyRequested = (demand * 1.0)
				/ calculateEconomicOrderQuantity(weeklyArticles, itemCost, preparationCost, maintenanceCost);
		int optimalPeriodRequested = (int) ((weeklyArticles.size() * 1.0) / frequencyRequested);
		int quantity = 0;
		System.out.println(weeklyArticles.size() - 1);
		for (int j = weeklyArticles.size() - 1; j >= 0; j--) {
			quantity += weeklyArticles.get(j);
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
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param double: cost to prepare or order all articles into the list.
	 * @param double: cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLeastUnitCost(ArrayList<Integer> weeklyArticles, double preparationCost,
			double maintenanceCost) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < weeklyArticles.size(); i++) {
			quantity += weeklyArticles.get(i);
			storedUnits += weeklyArticles.get(i) * StoredTimes;
			double UnitCost = (preparationCost + (maintenanceCost * (storedUnits * 1.0))) / (quantity * 1.0);
			if (i != weeklyArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (weeklyArticles.get(i + 1) * (StoredTimes + 1));
				double nextUnitCost = (preparationCost + (maintenanceCost * (nextStoredUnits * 1.0)))
						/ ((quantity + weeklyArticles.get(i + 1)) * 1.0);
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
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param double: cost to prepare or order all articles into the list.
	 * @param double: cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public ArrayList<Integer> systemLeastTotalCost(ArrayList<Integer> weeklyArticles, double preparationCost,
			double maintenanceCost) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < weeklyArticles.size(); i++) {
			quantity += weeklyArticles.get(i);
			storedUnits += weeklyArticles.get(i) * StoredTimes;
			double difference = Math.abs(preparationCost - (maintenanceCost * (storedUnits * 1.0)));
			if (i != weeklyArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (weeklyArticles.get(i + 1) * (StoredTimes + 1));
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
	 * @param ArrayList<Integer>: list with the quantities of weekly articles or
	 *        items.
	 * @param int: cost to prepare or order all articles into the list.
	 * @param int: cost to maintenance all articles into the list.
	 * @param int: cost to maintenance all articles into the list.
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
