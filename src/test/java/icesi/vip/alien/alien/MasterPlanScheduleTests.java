package icesi.vip.alien.alien;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.masterPlan.MasterPlanSchedule;


public class MasterPlanScheduleTests {
	
	private MasterPlanSchedule masterPlanSchedule;
	private ArrayList<Integer> bruteRequirements;
	private ArrayList<Integer> scheduledReceptions;
	
	public void setUpScenarioLxL() {
		
		//Data needed
		String lotSizingMethod = MasterPlanSchedule.LOTXLOT;
		int leadTime = 4;
		int initialStock = 120;
		int securityStock = 50;
		String productCode = "12345";
		String productName = "Radio";
		String periodicity = MasterPlanSchedule.WEEKLY;
		
		masterPlanSchedule = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, productCode, productName, 0.0, 0.0, 0.0, periodicity, 1);
		
		bruteRequirements = new ArrayList<Integer>();
		bruteRequirements.add(145);
		bruteRequirements.add(134);
		bruteRequirements.add(128);
		bruteRequirements.add(167);
		bruteRequirements.add(103);
		bruteRequirements.add(110);
		bruteRequirements.add(123);
		bruteRequirements.add(106);
		bruteRequirements.add(139);
		bruteRequirements.add(129);
		bruteRequirements.add(142);
		bruteRequirements.add(132);
		
		masterPlanSchedule.setBruteRequirements(bruteRequirements);
		
		scheduledReceptions = new ArrayList<Integer>();
		scheduledReceptions.add(100);
		scheduledReceptions.add(200);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		
		masterPlanSchedule.setScheduledReceptions(scheduledReceptions);
		
		
	}
	
	public void setUpScenarioEOQ() {
		
		String lotSizingMethod = MasterPlanSchedule.ECONOMIC_ORDER_QUANTITY;
		int leadTime = 3;
		int initialStock = 25;
		int securityStock = 10;
		String productCode = "12345";
		String productName = "Radio";
		double costArticle = 10.0;
		double preparationCost = 40;
		double maintenanceCost = 0.003;
		String periodicity = MasterPlanSchedule.WEEKLY;

		masterPlanSchedule = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, productCode, productName, costArticle, preparationCost, maintenanceCost, periodicity, 1);
		
		bruteRequirements = new ArrayList<Integer>();
		bruteRequirements.add(56);
		bruteRequirements.add(45);
		bruteRequirements.add(76);
		bruteRequirements.add(67);
		bruteRequirements.add(45);
		bruteRequirements.add(67);
		bruteRequirements.add(56);
		bruteRequirements.add(87);
		bruteRequirements.add(89);
		bruteRequirements.add(94);
		bruteRequirements.add(86);
		bruteRequirements.add(87);
		bruteRequirements.add(89);
		bruteRequirements.add(78);
		bruteRequirements.add(75);
		bruteRequirements.add(46);
		bruteRequirements.add(53);
		bruteRequirements.add(49);
		
		masterPlanSchedule.setBruteRequirements(bruteRequirements);
		
		scheduledReceptions = new ArrayList<Integer>();
		scheduledReceptions.add(70);
		scheduledReceptions.add(100);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		
		masterPlanSchedule.setScheduledReceptions(scheduledReceptions);
		
	}
	
	public void setUpScenarioPOS() {
		
		String lotSizingMethod = MasterPlanSchedule.PERIODS_OF_SUPPLY;
		int leadTime = 4;
		int initialStock = 120;
		int securityStock = 50;
		String productCode = "12345";
		String productName = "Radio";
		String periodicity = MasterPlanSchedule.WEEKLY;
		int tPeriodOfSupply = 3;

		masterPlanSchedule = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, productCode, productName, 0.0, 0.0, 0.0, periodicity, tPeriodOfSupply);
		
		bruteRequirements = new ArrayList<Integer>();
		bruteRequirements.add(145);
		bruteRequirements.add(134);
		bruteRequirements.add(128);
		bruteRequirements.add(167);
		bruteRequirements.add(103);
		bruteRequirements.add(110);
		bruteRequirements.add(123);
		bruteRequirements.add(106);
		bruteRequirements.add(139);
		bruteRequirements.add(129);
		bruteRequirements.add(142);
		bruteRequirements.add(132);
		
		masterPlanSchedule.setBruteRequirements(bruteRequirements);
		
		scheduledReceptions = new ArrayList<Integer>();
		scheduledReceptions.add(100);
		scheduledReceptions.add(200);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		
		masterPlanSchedule.setScheduledReceptions(scheduledReceptions);
		
	}
	
	public void setUpScenarioPOQ() {
		
		String lotSizingMethod = MasterPlanSchedule.PERIOD_ORDER_QUANTITY;
		int leadTime = 4;
		int initialStock = 120;
		int securityStock = 50;
		String productCode = "12345";
		String productName = "Radio";
		double costArticle = 7.0;
		double preparationCost = 30;
		double maintenanceCost = 0.0008;
		String periodicity = MasterPlanSchedule.WEEKLY;

		masterPlanSchedule = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, productCode, productName, costArticle, preparationCost, maintenanceCost, periodicity, 1);
		
		bruteRequirements = new ArrayList<Integer>();
		bruteRequirements.add(145);
		bruteRequirements.add(134);
		bruteRequirements.add(128);
		bruteRequirements.add(167);
		bruteRequirements.add(103);
		bruteRequirements.add(110);
		bruteRequirements.add(123);
		bruteRequirements.add(106);
		bruteRequirements.add(139);
		bruteRequirements.add(129);
		bruteRequirements.add(142);
		bruteRequirements.add(132);
		
		masterPlanSchedule.setBruteRequirements(bruteRequirements);
		
		scheduledReceptions = new ArrayList<Integer>();
		scheduledReceptions.add(100);
		scheduledReceptions.add(200);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		scheduledReceptions.add(0);
		
		masterPlanSchedule.setScheduledReceptions(scheduledReceptions);
		
	}
	
	
	public void lxlFunctionalTest() {
		
		setUpScenarioLxL();
		
		int[] scheduledAvailableStock = {75, 141, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
		int[] netRequirements = {0, 0, 37, 167, 103, 110, 123, 106, 139, 129, 142, 132};
		int[] planOrders = {0, 0, 37, 167, 103, 110, 123, 106, 139, 129, 142, 132};
		int[] releasedPlanOrders = {103, 110, 123, 106, 139, 129, 142, 132, 0, 0, 0, 0};
		
		masterPlanSchedule.createMPS();
		
		for(int i = 0; i < scheduledAvailableStock.length; i++) {
			assertEquals("Scheduled available stock for the " + i + "-th period is not correct", new Integer(scheduledAvailableStock[i]), masterPlanSchedule.getScheduledAvailableStock().get(i));
			assertEquals("Net requirements for the " + i + "-th period is not correct", new Integer(netRequirements[i]), masterPlanSchedule.getNetRequirements().get(i));
			assertEquals("Plan orders for the " + i + "-th period is not correct", new Integer(planOrders[i]), masterPlanSchedule.getPlanOrders().get(i));
			assertEquals("Release of planned orders for the " + i + "-th period is not correct", new Integer(releasedPlanOrders[i]), masterPlanSchedule.getReleasedPlanOrders().get(i));
		}
		
	}
	
	
	public void EOQFunctionalTest() {
		
		setUpScenarioEOQ();
		
		int[] scheduledAvailableStock = {39, 94, 448, 381, 336, 269, 213, 126, 37, 373, 287, 200, 111, 33, 388, 342, 289, 240};
		int[] netRequirements = {0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0, 0, 52, 0, 0, 0};
		int[] planOrders = {0, 0, 430, 0, 0, 0, 0, 0, 0, 430, 0, 0, 0, 0, 430, 0, 0, 0};
		int[] releasedPlanOrders = {0, 0, 0, 0, 0, 0, 430, 0, 0, 0, 0, 430, 0, 0, 0, 0, 0, 0};
		
		masterPlanSchedule.createMPS();
		
		for(int i = 0; i < scheduledAvailableStock.length; i++) {
			assertEquals("Scheduled available stock for the " + i + "-th period is not correct", new Integer(scheduledAvailableStock[i]), masterPlanSchedule.getScheduledAvailableStock().get(i));
			assertEquals("Net requirements for the " + i + "-th period is not correct", new Integer(netRequirements[i]), masterPlanSchedule.getNetRequirements().get(i));
			assertEquals("Plan orders for the " + i + "-th period is not correct", new Integer(planOrders[i]), masterPlanSchedule.getPlanOrders().get(i));
			assertEquals("Release of planned orders for the " + i + "-th period is not correct", new Integer(releasedPlanOrders[i]), masterPlanSchedule.getReleasedPlanOrders().get(i));
		}
		
	}
	
	
	public void POQFunctionalTest() {
		
		setUpScenarioPOQ();
		
		int[] scheduledAvailableStock = {75, 141, 217, 50, 160, 50, 156, 50, 179, 50, 182, 50};
		int[] netRequirements = {0, 0, 37, 0, 103, 0, 123, 0, 139, 0, 142, 0};
		int[] planOrders = {0, 0, 204, 0, 213, 0, 229, 0, 268, 0, 274, 0};
		int[] releasedPlanOrders = {213, 0, 229, 0, 268, 0, 274, 0, 0, 0, 0, 0};
		
		masterPlanSchedule.createMPS();
		
		for(int i = 0; i < scheduledAvailableStock.length; i++) {
			assertEquals("Scheduled available stock for the " + i + "-th period is not correct", new Integer(scheduledAvailableStock[i]), masterPlanSchedule.getScheduledAvailableStock().get(i));
			assertEquals("Net requirements for the " + i + "-th period is not correct", new Integer(netRequirements[i]), masterPlanSchedule.getNetRequirements().get(i));
			assertEquals("Plan orders for the " + i + "-th period is not correct", new Integer(planOrders[i]), masterPlanSchedule.getPlanOrders().get(i));
			assertEquals("Release of planned orders for the " + i + "-th period is not correct", new Integer(releasedPlanOrders[i]), masterPlanSchedule.getReleasedPlanOrders().get(i));
		}
		
	}
	
	
	public void POSFunctionalTest() {
		
		setUpScenarioPOS();
		
		int[] scheduledAvailableStock = {482, 548, 420, 633, 530, 420, 665, 559, 420, 694, 552, 420};
		int[] netRequirements = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] planOrders = {407, 0, 0, 380, 0, 0, 368, 0, 0, 403, 0, 0};
		int[] releasedPlanOrders = {0, 0, 368, 0, 0, 403, 0, 0, 0, 0, 0, 0};
		
		masterPlanSchedule.createMPS();
		
		for(int i = 0; i < scheduledAvailableStock.length; i++) {
			assertEquals("Scheduled available stock for the " + i + "-th period is not correct", new Integer(scheduledAvailableStock[i]), masterPlanSchedule.getScheduledAvailableStock().get(i));
			assertEquals("Net requirements for the " + i + "-th period is not correct", new Integer(netRequirements[i]), masterPlanSchedule.getNetRequirements().get(i));
			assertEquals("Plan orders for the " + i + "-th period is not correct", new Integer(planOrders[i]), masterPlanSchedule.getPlanOrders().get(i));
			assertEquals("Release of planned orders for the " + i + "-th period is not correct", new Integer(releasedPlanOrders[i]), masterPlanSchedule.getReleasedPlanOrders().get(i));
		}
		
	}
	
}
