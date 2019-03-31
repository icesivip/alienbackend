package VIPModel;

import java.util.ArrayList;

import interfaces.IN_Ary_Tree;

public class N_Ary_Tree implements IN_Ary_Tree{

	/**
	 * The root of the tree
	 */
	private Product rootProduct;

	/**
	 * The numbers of levels that the tree will have
	 */
	private int levels;

	/**
	 * The name of the n-ary tree
	 */
	private String name;

	/**
	 * 
	 */
	private int fullSizeTree;

	/**
	 * Creates a n-ary tree (information container)
	 */
	public N_Ary_Tree() {
		rootProduct = null;
		levels = 0;
		name = "Default Name";
		fullSizeTree = 0;
	}

	/**
	 * Creates a n-ary tree (information container)
	 * 
	 * @param levels
	 *            The number of levels the tree will have
	 * @param name
	 *            The tree's name
	 */
	public N_Ary_Tree(int levels, String name) {
		this.name = name;
		this.levels = levels;
		rootProduct = null;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getLevels() {
		return levels;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param levels
	 */
	@Override
	public void setLevels(int levels) {
		this.levels = levels;
	}

	/**
	 * 
	 * @param name
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Product getRootProduct() {
		return rootProduct;
	}

	/**
	 * 
	 * @param rootProduct
	 */
	@Override
	public void setRootProduct(Product rootProduct) {
		this.rootProduct = rootProduct;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getFullSizeTree() {
		return fullSizeTree;
	}

	/**
	 * 
	 * @param fullSizeTree
	 */
	@Override
	public void setFullSizeTree(int fullSizeTree) {
		this.fullSizeTree = fullSizeTree;
	}

	/**
	 * This method initializes the root.
	 * 
	 * @param name
	 *            Product name father
	 * @param productionTime
	 *            The time to make the product father
	 * @param timeUnit
	 *            The unit time in which the product father is made.
	 */
	@Override
	public void insertProduct(String id, String name, int productionTime, String timeUnit) {
		rootProduct = new Product(null, id, name, productionTime, 1);
		fullSizeTree++;
	}
	
	/**
	 * 
	 * @param father
	 * @param id
	 * @param name
	 * @param productionTime
	 * @param amount
	 * @param timeUnit
	 * @param initialInv
	 * @param securiInv
	 * @param date
	 * @param programDelivery
	 */
	@Override
	public void insertProductMRP (Product father, String id, String name, int productionTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery) {
		
		rootProduct = new Product(father, id, name, productionTime, amount, initialInv, securiInv);
		
		if(programDelivery != null) {
			for (int i = 0; i < programDelivery.size(); i++) {
				rootProduct.insertProgramDelivery(date.get(i), programDelivery.get(i));
			}
		}
		
		fullSizeTree++;
 		
	}

	/**
	 * This method insert subproducts to a product father
	 * 
	 * @param fatherName
	 *            The name of the product to which the new product is inserted
	 * @param name
	 *            The name of the product to be inserted
	 * @param productionTime
	 *            The time necessary to make the product to be inserted
	 * @param amount
	 *            The amount of this product to build a father
	 * @param timeUnit
	 *            The product's time unit
	 */
	@Override
	public void insertSubProduct(String fatherName, String id, String name, int productionTime, int amount, String timeUnit) {

		Product fatherProduct = rootProduct.search(fatherName);

		if (fatherProduct != null) {
			Product subProduct = new Product(fatherProduct, id, name, productionTime, amount);
			fatherProduct.insertProduct(subProduct);
			fullSizeTree++;
		}

	}
	
	/**
	 * 
	 * @param fatherName
	 * @param id
	 * @param name
	 * @param productionTime
	 * @param amount
	 * @param timeUnit
	 * @param initialInv
	 * @param securiInv
	 * @param date
	 * @param programDelivery
	 */
	@Override
	public void insertSubProductMRP(String fatherName, String id, String name, int productionTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery) {

		Product fatherProduct = rootProduct.search(fatherName);

		if (fatherProduct != null) {
			Product subProduct = new Product(fatherProduct, id, name, productionTime, amount, initialInv, securiInv);
			fatherProduct.insertProduct(subProduct);
			
			if (programDelivery != null) {
				for (int i = 0; i < programDelivery.size(); i++) {
					subProduct.insertProgramDelivery(date.get(i), programDelivery.get(i));
				}
			}
			fullSizeTree++;
		}

	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Product search (String id) {
		
		Product product = rootProduct.search(id);

		if (product != null) {
			return product;
		}
		
		return null;
		
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Product> inorder() {

		ArrayList<Product> products = new ArrayList<>();

		if (rootProduct != null) {
			rootProduct.inorder(products);
		}
		return products;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Product> getPreorder() {
		ArrayList<Product> products = new ArrayList<>();

		if (rootProduct != null) {
			rootProduct.preorder(products);
		}

		return products;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Product> getPostorder() {
		ArrayList<Product> products = new ArrayList<>();
		
		if (rootProduct != null) {
			rootProduct.postorder(products);			
		}
		
		return products;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Product> getByLevels() {

		ArrayList<Product> products = new ArrayList<>();

		if (rootProduct != null) {
			rootProduct.byLevels(products);
		}

		return products;

	}

}