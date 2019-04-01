package interfaces;

import java.util.ArrayList;

import VIPModel.N_Ary_Tree;
import VIPModel.Product;

public interface IMRP {
	
	public void insertProduct(String fatherName, String id, String name, int leadTime, String timeUnit, int amount);
	public void inserProductMRP(String fatherName, String id, String name, int leadTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery);
	
	public ArrayList<Product> generateProductTree();
	
	public N_Ary_Tree getN_Ary_Tree();
	
	public ArrayList<Integer[][]> allProductsMRP(N_Ary_Tree tree, ArrayList<Integer> rqB, ArrayList<String> rqBdate);
	
	
}
