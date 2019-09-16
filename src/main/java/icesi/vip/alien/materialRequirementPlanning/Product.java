package icesi.vip.alien.materialRequirementPlanning;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnore;

import icesi.vip.alien.masterPlan.MasterPlanSchedule;

/**
 * Entity that represents a product
 * @author Manuel Alejandro Coral Lozano
 * @version 1.0
 */

public class Product {
	
	// Atributos
	/**
	 * Product's name
	 */
	private String name;
//	
//	/**
//	 * Time to make a product
//	 */
//	private int leadTime;
	
	/**
	 * Amount of products necessary to build one
	 */
	private ArrayList<Integer> amounts;
//
//	/**
//	 * Unit in which the production time is manage
//	 */
//	private String timeUnit;

	/**
	 * Subproducts of this product 
	 */
	private ArrayList<Product> subProducts;
	
	/**
	 * 
	 */
	@JsonIgnore
	private ArrayList<Product> fathers;
	
	/**
	 * 
	 */
	private String id;
	
	private MasterPlanSchedule mps;
	
	public Product (String id, String name, ArrayList<Product> fathers, ArrayList<Integer> amounts, String lotSizingMethod, int leadTime, int initialStock, int securityStock, 
			double costArticle, double preparationCost, double maintenanceCost, String periodicity, int TPeriodOFSupply, ArrayList<Integer> scheduledReceptions) {
		
		this.name = name;
		this.id = id;
		this.amounts = amounts;
		this.fathers = fathers;
		this.mps = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, id, name,
				costArticle, preparationCost, maintenanceCost, periodicity, TPeriodOFSupply);
		this.mps.setBruteRequirements(calculateBruteRequirements());
		this.mps.setScheduledReceptions(scheduledReceptions);
		this.mps.createMPS();
		subProducts = new ArrayList<Product>();
		
	}
	
	public Product (String id, String name, Product father, int amount, String lotSizingMethod, int leadTime, int initialStock, int securityStock, 
			double costArticle, double preparationCost, double maintenanceCost, String periodicity, int TPeriodOFSupply, ArrayList<Integer> scheduledReceptions) {
		
		this.name = name;
		this.id = id;
		this.amounts = new ArrayList<Integer>();
		this.fathers = new ArrayList<Product>();
		addFatherWithAmount(father, amount);
		this.mps = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, id, name,
				costArticle, preparationCost, maintenanceCost, periodicity, TPeriodOFSupply);
		this.mps.setBruteRequirements(calculateBruteRequirements());
		this.mps.setScheduledReceptions(scheduledReceptions);
		this.mps.createMPS();
		subProducts = new ArrayList<Product>();
		
	}
	
	public Product (String id, String name, String lotSizingMethod, int leadTime, int initialStock, int securityStock, 
			double costArticle, double preparationCost, double maintenanceCost, String periodicity, int TPeriodOFSupply, 
			ArrayList<Integer> bruteRequirements, ArrayList<Integer> scheduledReceptions) {
		
		this.name = name;
		this.id = id;
		this.amounts = new ArrayList<Integer>();
		this.amounts.add(1);
		this.mps = new MasterPlanSchedule(lotSizingMethod, leadTime, initialStock, securityStock, id, name,
				costArticle, preparationCost, maintenanceCost, periodicity, TPeriodOFSupply);
		this.mps.setBruteRequirements(bruteRequirements);
		this.mps.setScheduledReceptions(scheduledReceptions);
		this.mps.createMPS();
		subProducts = new ArrayList<Product>();
		
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> calculateBruteRequirements(){
		ArrayList<Integer> bruteRequirements = null;
		for(int k = 0; k < fathers.size(); k++) {
			bruteRequirements = (ArrayList<Integer>) fathers.get(k).mps.getReleasedPlanOrders().clone();
			for(int i = 0; i < bruteRequirements.size(); i++) {
				bruteRequirements.set(i, bruteRequirements.get(i)*amounts.get(k));
			}			
		}
		return bruteRequirements;
	}
	
	public void addFatherWithAmount(Product father, int amount) {
		this.fathers.add(father);
		this.amounts.add(amount);
	}
	
//	/**
//	 * Creates a product
//	 * @param name Is the name's product
//	 * @param productionTime It's the time it takes to produce this product
//	 * @param amount It's the amount of this product that is needed to create the parent product
//	 * @param timeUnit It's the time's unit in which the product is produced
//	 */
//	public Product(Product father, String id, String name, int leadTime, int amount) {
//		
//		this.name = name;
//		this.id = id;
//		this.leadTime = leadTime;
//		this.amount = amount;
//		this.father = father;
//		
//		subProducts = new ArrayList<>();
//		
//	}
//
//	/**
//	 * Creates a product
//	 * @param name Is the name's product
//	 * @param productionTime It's the time it takes to produce this product
//	 * @param amount It's the amount of this product that is needed to create the parent product
//	 * @param timeUnit It's the time's unit in which the product is produced
//	 * @param subProducts They are the products "children" of this product
//	 */
//	public Product(Product father, String id, String name, int leadTime, int amount, ArrayList<Product> subProducts) {
//		
//		this.id = id;
//		this.father = father;
//		this.name = name;
//		this.leadTime = leadTime;
//		this.amount = amount;
//		
//		this.subProducts = subProducts;
//	}
	
	/**
	 * This method is responsible for inserting a subproduct to the container
	 * @param subProduct The subproduct that we want to insert.
	 */
	public void insertProduct(Product subProduct) {
		subProducts.add(subProduct);
	}

	/**
	 * This method is responsible for searching a product, by name, inside the container
	 * @param id Product's id we want to search
	 * @return The Product we were looking for
	 */
	public Product search(String id) {
		
		if (this.id.equals(id)) {
			return this;
		} else {
			for (int i = 0; i < subProducts.size(); i++) {
				Product aux = subProducts.get(i).search(id);
				if (aux != null) {
					return aux;
				}
			}
			return null;
		}
	}
	
	
	/**
	 * This method if a produt is a leaf
	 * @return True if is a leaf, False if is not a leaf
	 */
	public boolean leaf() {
		return subProducts == null || subProducts.isEmpty();
	}

	/**
	 * This method returns the name of the product
	 * @return The name of the product
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method changes the name of one product for another
	 * @param name The new name of one product
	 */
	public void setName(String name) {
		this.name = name;
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public int getLeadTime() {
//		return this.leadTime;
//	}
//
//	/**
//	 * 
//	 * @param productionTime
//	 */
//	public void setLeadTime(int leadTime) {
//		this.leadTime = leadTime;
//	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Integer> getAmount() {
		return this.amounts;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAmount(ArrayList<Integer> amounts) {
		this.amounts = amounts;
	}
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public ArrayList<Product> getFather() {
		return fathers;
	}

	/**
	 * 
	 * @param father
	 */	
	public void setFather(ArrayList<Product> father) {
		this.fathers = father;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Product> getSubProducts() {
		return this.subProducts;
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public ArrayList<Integer> getBuyingOrder() {
//		return buyingOrder;
//	}
//
//	/**
//	 * 
//	 * @param buyingOrder
//	 */
//	public void setBuyingOrder(ArrayList<Integer> buyingOrder) {
//		this.buyingOrder = buyingOrder;
//	}

	/**
	 * This method is responsible for returning the toString of each elementos inside the container
	 * @return The toString of each element
	 */
	public String toStringArrayList () {
		
		String names = "";
		
		for (int i = 0; i < subProducts.size(); i++) {
			
			names += " " + subProducts.get(i).getName() + " ";
			
		}
		
		return names;
		
	}
	
	/**
	 * 
	 * @param subProducts
	 */
	public void setSubProducts(ArrayList<Product> subProducts) {
		this.subProducts = subProducts;
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public String getTimeUnit() {
//		return this.timeUnit;
//	}
//
//	/**
//	 * 
//	 * @param timeUnit
//	 */
//	public void setTimeUnit(String timeUnit) {
//		this.timeUnit = timeUnit;
//	}
//	
//	public String getFatherName() {
//		return fatherName;
//	}
//
//	public void setFatherName(String fatherName) {
//		this.fatherName = fatherName;
//	}

	/**
	 * 
	 * @param products
	 */
	public void inorder(ArrayList<Product> products) {
		
		if (subProducts.size() == 0) {
			
			products.add(this);
			
		} else {
			subProducts.get(0).inorder(products);
			
			products.add(this);
			
			for (int i = 1; i < subProducts.size(); i++) {
				subProducts.get(i).inorder(products);
			}
			
		}
		
	}
	
	/**
	 * This method move in the tree in preorder.
	 * @param products The products to go
	 */
	public void preorder(ArrayList<Product> products) {

		products.add(this);

		for (int i = 0; subProducts != null && i < subProducts.size(); i++) {
			subProducts.get(i).preorder(products);
		}

	}
	
	/**
	 * This method move in the tree in postorder
	 * @param products The products to go
	 */
	public void postorder (ArrayList<Product> products) {
		
		for (int i = 0; subProducts != null && i < subProducts.size(); i++) {
			System.out.println(subProducts.size());
			subProducts.get(i).postorder(products);
		}
		
		products.add(this);
		
	}
	
	/**
	 * 
	 * @param products
	 */
	public void byLevels(ArrayList<Product> products) {

		Queue<Product> queue = new ArrayDeque<>();

		queue.add(this);
		while (!queue.isEmpty()) {
			Product aux = null;
			aux = queue.poll();
			products.add(aux);

			for (int i = 0; aux.getSubProducts() != null && i < aux.getSubProducts().size(); i++) {
				queue.add(aux.getSubProducts().get(i));
			}

		}

	}
	
	public MasterPlanSchedule getMPS() {
		return mps;
	}
	
//	public int getSecurityInv() {
//		return securityInv;
//	}
//
//	public void setSecurityInv(int securityInv) {
//		this.securityInv = securityInv;
//	}
//
//	public int getInitialInv() {
//		return initialInv;
//	}
//
//	public void setInitialInv(int initialInv) {
//		this.initialInv = initialInv;
//	}
//
//	public Hashtable<String, Integer> getProgramDelivery() {
//		return programDelivery;
//	}
//
//	public void setProgramDelivery(Hashtable<String, Integer> programDelivery) {
//		this.programDelivery = programDelivery;
//	}
//
//	public void insertProgramDelivery (String date, int programDelivery) {
//		this.programDelivery.put(date, programDelivery);
//	}

	@Override
	public String toString() {
		return "[ " + name + "," + id + " ]";
	}

}