package interfaces;

import java.util.ArrayList;

import VIPModel.Product;

public interface IN_Ary_Tree {

	public int getLevels();
	public String getName();
	public void setLevels(int levels);
	public void setName(String name);
	public Product getRootProduct();
	public void setRootProduct(Product rootProduct);
	public int getFullSizeTree();
	public void setFullSizeTree(int fullSizeTree);
	public void insertProduct(String id, String name, int productionTime, String timeUnit);
	public void insertProductMRP (Product father, String id, String name, int productionTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery);
	public void insertSubProduct(String fatherName, String id, String name, int productionTime, int amount, String timeUnit);
	public void insertSubProductMRP(String fatherName, String id, String name, int productionTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery);
	public Product search (String id);
	public ArrayList<Product> inorder();
	public ArrayList<Product> getPreorder();
	public ArrayList<Product> getPostorder();
	public ArrayList<Product> getByLevels();
	
}
