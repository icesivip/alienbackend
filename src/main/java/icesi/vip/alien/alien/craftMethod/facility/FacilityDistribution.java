package icesi.vip.alien.alien.craftMethod.facility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import icesi.vip.alien.alien.craftMethod.auxiliary.Point;
import icesi.vip.alien.alien.craftMethod.labelmap.LabelMap;
import icesi.vip.alien.alien.craftMethod.labelmap.LabelMapNode;
import icesi.vip.alien.alien.craftMethod.relocation.Relocation;

public class FacilityDistribution {

	private LabelMap distribution;
	private Set<Integer>existingAreas;
	private Set<Integer>nonSwappableAreas;
	private double areaSize=1;
	private double unitCost=1;
	private Map<Integer,Map<Integer,Integer>> fromTo;
	

	
	public FacilityDistribution(int[][]distribution) {
		this.distribution=new LabelMap(distribution);
		if(!this.distribution.checkValidity()) {
			throw new IllegalArgumentException("Given distribution is not valid.");
		}
		nonSwappableAreas=new HashSet<Integer>();
		createExistingAreas();
		createFromTo();
	}
	
	
	public static FacilityDistribution getFromFile(String filePath) throws Exception {
	
			int[][] mat=getMatrixFromFile(filePath);
			return new FacilityDistribution(mat);

		
		
	}
	
	
	
	private static  int[][] getMatrixFromFile(String filePath) throws Exception{
		System.out.println(filePath);
		BufferedReader buf=new BufferedReader(new FileReader(filePath));
		ArrayList<int[]> lines = new ArrayList<>();
		String line = buf.readLine();
		while(line != null) {
			String[]nums = line.split(" ");
			int[] intarray = Arrays.stream(nums).mapToInt(Integer::parseInt).toArray();
		    lines.add(intarray);
		    line=buf.readLine();
		}
		buf.close();
		int[][]ret = new int[lines.size()][];
		for (int i = 0; i < ret.length; i++) {
			ret[i]=lines.get(i);
		}
		
		
		return ret;
	}
	
	public void addNonSwappableArea(int area) {
		if(!this.existingAreas.contains(area)) {
			throw new IllegalArgumentException("The area "+area+" does not exist.");
		}else {
			this.nonSwappableAreas.add(area);
		}
	}
	
	public void setAreaSize(double areaSize) {
		this.areaSize=areaSize;
	}
	
	
	public Map<Integer,Point> calculateCentroids(){
		HashMap<Integer,Point>map=new HashMap<Integer, Point>();
		for(int area : this.existingAreas) {
			double x=0;
			double y=0;
			double count=0;
			for (int i = 0; i < this.distribution.map.length; i++) {
				for (int j = 0; j < this.distribution.map[0].length; j++) {
					if(this.distribution.map[i][j].value==area) {
						count++;
						double i1=i*this.areaSize;
						double j1=j*this.areaSize;
						double i2=i1+this.areaSize;
						double j2=j1+this.areaSize;
						x+=(i1+i2)/2.0;
						y+=(j1+j2)/2.0;
						
					}
				}
			}
			map.put(area, new Point(x/count, y/count));
		}
		

//		for(int area: map.keySet()) {
//			System.out.println(area+" => { "+map.get(area).i+" , "+map.get(area).j+" }");
//		}
		return map;
	}
	
	
	
	
	public double evaluateDistribution() {
		Map<Integer,Point> centroids=this.calculateCentroids();
		double evaluation=0;
		for(int area1 : this.existingAreas) {
			for(int area2 : this.existingAreas) {
				if(area1 != area2) {
	
						evaluation+=this.fromTo.get(area1).get(area2)*Point.RectilinearDistance(centroids.get(area1), centroids.get(area2))*this.unitCost;

					
				}
			}
		}  
		return evaluation;
	}
	
	
	public void setFromToMapping(int from , int to , int quantity) {
		
		if(this.fromTo.containsKey(from)) {
			if(this.fromTo.get(from).containsKey(to)) {
				this.fromTo.get(from).replace(to, quantity);
				
			}else {
				throw new IllegalArgumentException("Area "+to+" does not exist.");
			}
		}else {
			throw new IllegalArgumentException("Area "+from+" does not exist.");
		}
	}
	
	
	
	private void createExistingAreas(){
		Set<Integer>set=new HashSet<>();
		for (int i = 0; i < this.distribution.map.length; i++) {
			for (int j = 0; j <  this.distribution.map[0].length; j++) {
				if(!set.contains(this.distribution.map[i][j].value)) {
					set.add(this.distribution.map[i][j].value);
				}
			}
		}
		this.existingAreas= set;
	}
	
	
	private void createFromTo() {
		HashMap<Integer,Map<Integer,Integer>> map = new HashMap<>();
		for(int i : this.existingAreas) {
			map.put(i, new HashMap<>());
			for(int j : this.existingAreas) {
				if(i!=j) {
					map.get(i).put(j, 0);
				}
			}
		}
		this.fromTo=map;
	}
	
	
	public void swapArea(int area1, int area2,int maxSwapTries) throws Exception {
		int[][]dist=this.distribution.toDistribution();
		this.distribution=new LabelMap(Relocation.swap(dist, area1, area2,maxSwapTries));
	}
	
	
	public FacilityDistribution CraftAlgorithm(int maxSwapTries , boolean verbose) throws Exception {
		double currentCost=this.evaluateDistribution();
		FacilityDistribution currentDist=this.clone();
		boolean changed=true;
		System.out.println("Initial Dist : "+currentCost);
		Relocation.printArray(currentDist.outputDistribution());
		System.out.println();
		while(changed) {
			changed=false;
			if(verbose) {
				System.out.println("Cost : "+currentCost);
				Relocation.printArray(currentDist.outputDistribution());
				System.out.println();
			}
			//System.out.println("Calculando areas cambiables...");
			Set<String>swaps=currentDist.swappableAreas();
			HashMap<String,FacilityDistribution> swapsRecord= new HashMap<String, FacilityDistribution>();
			//System.out.println("Cambiando...");
			for(String swap: swaps) {
				//System.out.println(swap);
				int area1 = Integer.parseInt(swap.split(" ")[0]);
				int area2 = Integer.parseInt(swap.split(" ")[1]);
				FacilityDistribution clone=currentDist.clone();
				clone.swapArea(area1, area2,maxSwapTries);
				swapsRecord.put(swap, clone);
				
			}
			for(String swap : swapsRecord.keySet()) {
				double cost=swapsRecord.get(swap).evaluateDistribution();
				if(verbose) {
					System.out.println("Swap "+swap+" : "+cost);
					Relocation.printArray(swapsRecord.get(swap).outputDistribution());
					System.out.println();
				}

				if(cost<currentCost) {
					
					currentCost=cost;
					currentDist=swapsRecord.get(swap);
					changed=true;
				}
			}
		}
		return currentDist;
	}
	
	
	
	public Map<FacilityDistribution,Double> CraftAlgorithmFull(int maxSwapTries , boolean verbose) throws Exception {
		HashMap<FacilityDistribution,Double> map=new HashMap<>();
		double currentCost=this.evaluateDistribution();
		FacilityDistribution currentDist=this.clone();
		boolean changed=true;
		System.out.println("Initial Dist : "+currentCost);
		Relocation.printArray(currentDist.outputDistribution());
		System.out.println();
		while(changed) {
			changed=false;
			if(verbose) {
				System.out.println("Cost : "+currentCost);
				Relocation.printArray(currentDist.outputDistribution());
				System.out.println();
			}
			//System.out.println("Calculando areas cambiables...");
			Set<String>swaps=currentDist.swappableAreas();
			HashMap<String,FacilityDistribution> swapsRecord= new HashMap<String, FacilityDistribution>();
			//System.out.println("Cambiando...");
			for(String swap: swaps) {
				//System.out.println(swap);
				int area1 = Integer.parseInt(swap.split(" ")[0]);
				int area2 = Integer.parseInt(swap.split(" ")[1]);
				FacilityDistribution clone=currentDist.clone();
				clone.swapArea(area1, area2,maxSwapTries);
				swapsRecord.put(swap, clone);
				
			}
			for(String swap : swapsRecord.keySet()) {
				double cost=swapsRecord.get(swap).evaluateDistribution();
				map.put(swapsRecord.get(swap), cost);
				if(verbose) {
					System.out.println("Swap "+swap+" : "+cost);
					Relocation.printArray(swapsRecord.get(swap).outputDistribution());
					System.out.println();
				}

				if(cost<currentCost) {
					
					currentCost=cost;
					currentDist=swapsRecord.get(swap);
					changed=true;
				}
			}
		}
		return map;
	}
	
	public int[][] outputDistribution(){
		return this.distribution.toDistribution();
	}
	
	
	public FacilityDistribution clone() {
		FacilityDistribution clone = new FacilityDistribution(this.distribution.toDistribution());
		clone.areaSize=this.areaSize;
		clone.unitCost=this.unitCost;
		clone.fromTo=this.fromTo;
		clone.nonSwappableAreas=this.nonSwappableAreas;
		return clone;
	}
	
	
	
	private Set<String> swappableAreas() {
		Set<String> added = new HashSet<String>();
		Queue<LabelMapNode> toVisit = new LinkedList<LabelMapNode>();
		toVisit.add(this.distribution.map[0][0]);
		Set<String>visited=new HashSet<>();
		while (!toVisit.isEmpty()) {
			//System.out.println(toVisit.size());
			LabelMapNode node = toVisit.poll();
			visited.add(node.iIndex+" "+node.jIndex);
			if (node.up != null) {
				if(!visited.contains(node.up.iIndex+" "+node.up.jIndex)  && !toVisit.contains(node.up))
				toVisit.add(node.up);
				if(node.up.value != node.value && !added.contains(node.value + " " + node.up.value) && !added.contains(node.up.value + " " + node.value)) {
					added.add(node.value + " " + node.up.value);
				}
			}
			if (node.right != null) {
				if(!visited.contains(node.right.iIndex+" "+node.right.jIndex)  && !toVisit.contains(node.right))
				toVisit.add(node.right);
				if(node.right.value != node.value
						&& !added.contains(node.value + " " + node.right.value)
						&& !added.contains(node.right.value + " " + node.value)) {
					added.add(node.value + " " + node.right.value);
					
				}
			}
			if (node.down != null) {
				if(!visited.contains(node.down.iIndex+" "+node.down.jIndex)  && !toVisit.contains(node.down))
				toVisit.add(node.down);
				if(node.down.value != node.value
						&& !added.contains(node.value + " " + node.down.value)
						&& !added.contains(node.down.value + " " + node.value)) {
					added.add(node.value + " " + node.down.value);
					
				}
			}
			if (node.left != null) {
				if(!visited.contains(node.left.iIndex+" "+node.left.jIndex)  && !toVisit.contains(node.left))
				toVisit.add(node.left);
				if(node.left.value != node.value
						&& !added.contains(node.value + " " + node.left.value)
						&& !added.contains(node.left.value + " " + node.value)) {
					added.add(node.value + " " + node.left.value);
					
				}
			}
		}
		
		HashMap<Integer,Integer>map=new HashMap<Integer, Integer>();
		for(int area:this.existingAreas) {
			int count=0;
			for (int i = 0; i < this.distribution.map.length; i++) {
				for (int j = 0; j < this.distribution.map[0].length; j++) {
					if(this.distribution.map[i][j].value==area) {
						count++;
					}
				}
			}
			map.put(area, count);
		}
		
		
		for(int area:map.keySet()) {
			for(int area2:map.keySet()) {
				if(area!=area2 && map.get(area)==map.get(area2) && !added.contains(area+" "+area2) && !added.contains(area2+" "+area)) {
					added.add(area+" "+area2);
				}
			}
		}
		
		Set<String>toRemove=new HashSet<String>();
		for(String str : added) {
			String[]split=str.split(" ");
			int a1=Integer.parseInt(split[0]);
			int a2=Integer.parseInt(split[1]);
			if(this.nonSwappableAreas.contains(a1) || this.nonSwappableAreas.contains(a2)) {
				toRemove.add(str);
			}
		}
		
		for(String str:toRemove) {
			//System.out.println("Removing "+str+" from swappable areas");
			added.remove(str);
		}
		
		
		return added;
	}
	
	
	
}
