package VIPModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;

/**
 * Entity that represents a product
 * @author Manuel Alejandro Coral Lozano
 * @version 1.0
 */

public class Product {

	// Constantes
	public static String TIME_IN_WEEKS = "Weeks";
	public static String TIME_IN_DAYS = "Days";
	public static String TIME_IN_HOURS = "Hours";
	public static String TIME_IN_MONTHS = "Months";
	public static String TIME_IN_YEARS = "Years";
	
	// Atributos
	/**
	 * Product's name
	 */
	private String name;
	
	/**
	 * Time to make a product
	 */
	private int leadTime;
	
	/**
	 * Amount of products necessary to build one
	 */
	private int amount;

	/**
	 * Unit in which the production time is manage
	 */
	private String timeUnit;

	/**
	 * Subproducts of this product 
	 */
	private ArrayList<Product> subProducts;
	
	/**
	 * 
	 */
	private Product father;
	
	/**
	 * 
	 */
	private String id;
	
	/**
	 * 
	 */
	private int securityInv;
	
	/**
	 * 
	 */
	private int initialInv;
	
	/**
	 * 
	 */
	private ArrayList<Integer> buyingOrder;
	
	/**
	 * 
	 */
	private Hashtable<String, Integer> programDelivery;
	
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
		
		if (this.name.equals(id)) {
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
		return subProducts == null;
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

	/**
	 * 
	 * @return
	 */
	public int getLeadTime() {
		return this.leadTime;
	}

	/**
	 * 
	 * @param productionTime
	 */
	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}

	/**
	 * 
	 * @return
	 */
	public int getAmount() {
		return this.amount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public String darFather() {
		return father.getName();
	}

	/**
	 * 
	 * @param father
	 */
	public void setFather(Product father) {
		this.father = father;
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

	/**
	 * 
	 * @return
	 */
	public ArrayList<Integer> getBuyingOrder() {
		return buyingOrder;
	}

	/**
	 * 
	 * @param buyingOrder
	 */
	public void setBuyingOrder(ArrayList<Integer> buyingOrder) {
		this.buyingOrder = buyingOrder;
	}

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

	/**
	 * 
	 * @return
	 */
	public String getTimeUnit() {
		return this.timeUnit;
	}

	/**
	 * 
	 * @param timeUnit
	 */
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
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
	
	public int getSecurityInv() {
		return securityInv;
	}

	public void setSecurityInv(int securityInv) {
		this.securityInv = securityInv;
	}

	public int getInitialInv() {
		return initialInv;
	}

	public void setInitialInv(int initialInv) {
		this.initialInv = initialInv;
	}

	public Hashtable<String, Integer> getProgramDelivery() {
		return programDelivery;
	}

	public void setProgramDelivery(Hashtable<String, Integer> programDelivery) {
		this.programDelivery = programDelivery;
	}

	
	public Product (Product father, String id, String name, int leadTime, int amount, int initialInv, int securiInv) {
		
		this.name = name;
		this.id = id;
		this.leadTime = leadTime;
		this.amount = amount;
		this.father = father;
		this.initialInv = initialInv;
		this.securityInv = securiInv;
		
		this.programDelivery = new Hashtable<>();
		
		subProducts = new ArrayList<>();
		
	}
	
	public void insertProgramDelivery (String date, int programDelivery) {
		this.programDelivery.put(date, programDelivery);
	}
	
	
	/**
	 * Creates a product
	 * @param name Is the name's product
	 * @param productionTime It's the time it takes to produce this product
	 * @param amount It's the amount of this product that is needed to create the parent product
	 * @param timeUnit It's the time's unit in which the product is produced
	 */
	public Product(Product father, String id, String name, int leadTime, int amount) {
		
		this.name = name;
		this.id = id;
		this.leadTime = leadTime;
		this.amount = amount;
		this.father = father;
		
		subProducts = new ArrayList<>();
		
	}

	/**
	 * Creates a product
	 * @param name Is the name's product
	 * @param productionTime It's the time it takes to produce this product
	 * @param amount It's the amount of this product that is needed to create the parent product
	 * @param timeUnit It's the time's unit in which the product is produced
	 * @param subProducts They are the products "children" of this product
	 */
	public Product(Product father, String id, String name, int leadTime, int amount, ArrayList<Product> subProducts) {
		
		this.id = id;
		this.father = father;
		this.name = name;
		this.leadTime = leadTime;
		this.amount = amount;
		
		this.subProducts = subProducts;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[ " + name + "," + id + " ]";
	}

}
