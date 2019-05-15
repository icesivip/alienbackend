package icesi.vip.alien.materialRequirementPlanning;

import java.util.ArrayList;



public class MRP{
	
	private N_Ary_Tree nAryTree;
	private ArrayList<Integer[][]> mrp;
	
	public MRP() {
		nAryTree = new N_Ary_Tree();
	}
	
	/**
	 * 
	 *  param fatherName
	 *  param id
	 *  param name
	 *  param leadTime
	 *  param timeUnit
	 *  param amount
	 */
	  
	public void insertProduct (String fatherName, String id, String name, int leadTime, String timeUnit, int amount) {
		
		if (nAryTree.getRootProduct() == null) {
			nAryTree.insertProduct(id, name, leadTime, timeUnit);
		} else {
			nAryTree.insertSubProduct(fatherName, id, name, leadTime, amount, timeUnit);
		}
		
	}
	
	/**
	 * 
	 *  param fatherName
	 *  param id
	 *  param name
	 *  param leadTime
	 *  param amount
	 *  param timeUnit
	 *  param initialInv
	 *  param securiInv
	 *  param date
	 *  param programDelivery
	 */
	  
	public void inserProductMRP (String fatherName, String id, String name, int leadTime, int amount, int initialInv, int securiInv, ArrayList<String> date, ArrayList<Integer> programDelivery) {
		
		if (nAryTree.getRootProduct() == null) {
			nAryTree.insertProductMRP(null, fatherName , id, name, leadTime, amount, initialInv, securiInv, date, programDelivery);
		} else {
			nAryTree.insertSubProductMRP(fatherName, id, name, leadTime, amount, initialInv, securiInv, date, programDelivery);
		}
		
	}
	
	/**
	 * 
	 *  return
	 */
	  
	public ArrayList<Product> generateProductTree () {
		return nAryTree.getByLevels();
	}
	
	/**
	 * 
	 *  return
	 */
	  
	public N_Ary_Tree getN_Ary_Tree () {
		return nAryTree;
	}
	
	/**
	 * 
	 *  param tree
	 *  param rqB
	 *  param rqBdate
	 *  return
	 */
	  
	public ArrayList<Integer[][]> allProductsMRP (N_Ary_Tree tree, ArrayList<Integer> rqB, ArrayList<String> rqBdate) {
		
		mrp = new ArrayList<>();
		
		ArrayList<Product> product = tree.getByLevels();
		
		ArrayList<Integer> aux = new ArrayList<>();
		
		int productTime = biggestDateRqb(rqBdate);
		
		for (int i = 0; i < product.size(); i++) {
			
			if (i == 0) {
				for (int K = 0; K < productTime; K++) {
					aux.add(0);
				}
				
				System.out.println(rqBdate.size());
				System.out.println(rqB.size());
				for (int K = 1; K < rqBdate.size(); K++) {
					aux.set(Integer.parseInt(rqBdate.get(K))-1, rqB.get(K));
				}
				
				mrp.add(allMRP(tree, product.get(i).getName(), productTime, aux, rqBdate));
			} else {
				mrp.add(allMRP(tree, product.get(i).getName(), productTime, nAryTree.getRootProduct().search(product.get(i).darFather()).getBuyingOrder(), rqBdate));
			}
			
		}
		
		return mrp;
	}
	
	/**
	 * 
	 *  param rqB
	 *  return
	 */
	private int biggestDateRqb (ArrayList<String> date) {
		
		int a = 0;
		
		for (int i = 0; i < date.size(); i++) {
			
			if (Integer.parseInt(date.get(i)) > a) {
				a = Integer.parseInt(date.get(i));
			}
			
		}
		
		return a;
	}
	
	/**
	 * 
	 *  param t
	 *  param id
	 *  param productTime
	 *  param rqB
	 *  param rqBdate
	 *  return
	 */
	private Integer[][] allMRP (N_Ary_Tree t, String id, int productTime, ArrayList<Integer> rqB, ArrayList<String> rqBdate) {
		
		Product product = t.search(id);
		
		int invI = product.getInitialInv();
		
		int invS = product.getSecurityInv();
		
		ArrayList<Integer> thisBuyingOrder = new ArrayList<>();
				
		Integer[][] mrp = new Integer[6][productTime];
		
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < mrp[0].length; j++) {
				
				if(i == 0) {
					if (product.getAmount() > 1) {
						mrp[i][j] = rqB.get(j)*product.getAmount();						
					} else {
						mrp[i][j] = rqB.get(j);
					}
				}
				
				if (i == 1) {
					
					if (product.getProgramDelivery().isEmpty()) {
						mrp[i][j] = 0;
					} else {
						Integer aux = product.getProgramDelivery().get(j+1+"");
						if (aux == null) {
							mrp[i][j] = 0;
						} else {
							mrp[i][j] = product.getProgramDelivery().get(j+1 + "");
						}
					}
					
				}
				
			}
		}
		
		for (int i = 2; i < mrp.length; i++) {
			for (int j = 0; j < mrp[0].length; j++) {
				
				if (i == 2) {
					
					if (j == 0) {
						int check = checkNetRq(mrp[i-2][j], invI, mrp[i-1][j], invS);
						
						mrp[i+1][j] = check;
						mrp[i+2][j] = check;
						mrp[i][j] = invI + mrp[i-1][j] + mrp[i+2][j] - mrp[i-2][j];
					} else {

						int check = checkNetRq(mrp[i-2][j], mrp[i][j - 1], mrp[i-1][j], invS);

						mrp[i+1][j] = check;
						mrp[i+2][j] = check;
						mrp[i][j] = mrp[i][j - 1] + mrp[i - 1][j] + mrp[i+2][j] - mrp[i-2][j];
					}
					
				} else if (i == 5) {
					if ((j + product.getLeadTime()) < productTime) {
						mrp[i][j] = mrp[i-1][j+product.getLeadTime()];
						thisBuyingOrder.add(j, mrp[i][j]);
					} else {
						mrp[i][j] = 0;
						thisBuyingOrder.add(j, mrp[i][j]);
					}
				}
			}
		}
		
		product.setBuyingOrder(thisBuyingOrder);
		
		return mrp;
		
	}
	

	public ArrayList<Integer[][]> getMrp() {
		return mrp;
	}

	public void setMrp(ArrayList<Integer[][]> mrp) {
		this.mrp = mrp;
	}

	/**
	 * 
	 *  param A
	 *  param B
	 *  param C
	 *  param invS
	 *  return
	 */
	private int checkNetRq (int A, int B, int C, int invS) {
		int check = 0;
		check = Math.max(A+invS-B-C, 0);
		return check;
	}

}