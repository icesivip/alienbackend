package icesi.vip.alien.materialRequirementPlanning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MaterialRequirementsPlanning {
	
	private HashMap<String, Product> roots;
	
	public MaterialRequirementsPlanning() {
		
		roots = new HashMap<String, Product>();
		
	}
	
	public Product search(String id) {
		for(Product product: roots.values()) {
			if(product.search(id) != null) {
				return product;
			}
		}
		return null;
	}
	
	public void addProduct(String id, String name, String fatherId, int amount, String lotSizingMethod, int leadTime, int initialStock, int securityStock, 
			double costArticle, double preparationCost, double maintenanceCost, String periodicity, int TPeriodOFSupply, 
			ArrayList<Integer> bruteRequirements, ArrayList<Integer> scheduledReceptions) {
		if(fatherId == null) {
			Product product = new Product(id, name, lotSizingMethod, leadTime, initialStock, securityStock, costArticle, 
					preparationCost, maintenanceCost, periodicity, TPeriodOFSupply, bruteRequirements, scheduledReceptions);
			roots.put(id, product);
		}else {
			Product father = search(fatherId);
			Product product = new Product(id, name, father, amount, lotSizingMethod, leadTime, initialStock, securityStock, costArticle, 
					preparationCost, maintenanceCost, periodicity, TPeriodOFSupply, scheduledReceptions);
			father.insertProduct(product);
			
		}
		
	}
	
	public Collection<Product> getRoots(){
		return roots.values();
	}
	
	
}
