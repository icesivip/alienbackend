package icesi.vip.alien.alien.craftMethod.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import icesi.vip.alien.alien.craftMethod.auxiliary.Point;
import icesi.vip.alien.alien.craftMethod.facility.FacilityDistribution;
import icesi.vip.alien.alien.craftMethod.relocation.Relocation;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		FacilityDistribution dist= FacilityDistribution.getFromFile("craft.txt");
		dist.setAreaSize(0.1);
		dist.setFromToMapping(1, 2, 10000);
		dist.setFromToMapping(1, 3, 10000);
		dist.setFromToMapping(1, 4, 0);
		dist.setFromToMapping(1, 5, 10000);
		dist.setFromToMapping(1, 6, 10000);
		dist.setFromToMapping(2, 1, 100);
		dist.setFromToMapping(2, 3, 1000);
		dist.setFromToMapping(2, 4, 100);
		dist.setFromToMapping(2, 5, 1000);
		dist.setFromToMapping(2, 6, 100);
		dist.setFromToMapping(2, 10, -10000);	
		dist.setFromToMapping(3, 1, 0);
		dist.setFromToMapping(3, 2, 0);
		dist.setFromToMapping(3, 4, 10000);
		dist.setFromToMapping(3, 5, 1000);
		dist.setFromToMapping(3, 6, 0);
		dist.setFromToMapping(4, 1, 0);	
		dist.setFromToMapping(4, 2, 100);
		dist.setFromToMapping(4, 3, 0);
		dist.setFromToMapping(4, 5, 1000);
		dist.setFromToMapping(4, 6, 0);
		
		dist.setFromToMapping(4, 10, 10000);
				
		dist.setFromToMapping(5, 1, 0);
		dist.setFromToMapping(5, 2, 1000);
		dist.setFromToMapping(5, 3, 0);
		dist.setFromToMapping(5, 4, 1000);
		dist.setFromToMapping(5, 6, 1000);
		dist.setFromToMapping(6, 1, 10000);
		dist.setFromToMapping(6, 2, 100);
		dist.setFromToMapping(6, 3, 0);
		dist.setFromToMapping(6, 4, 0);
		dist.setFromToMapping(6, 5, 0);	
		
		dist.setFromToMapping(6, 11, 10000);
		
		dist.addNonSwappableArea(8);
		dist.addNonSwappableArea(9);
		dist.addNonSwappableArea(7);
		dist.addNonSwappableArea(10);
		dist.addNonSwappableArea(11);
		

		
		
//		FacilityDistribution min=dist;
//
//		for (int i = 0; i < 100; i++) {
//			min=min.CraftAlgorithm(100, false);
//		}
		ArrayList<FacilityDistribution>dists=new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			dists.add(dist.CraftAlgorithm(100,true));
		}
		FacilityDistribution min=null;
		double minCost=Double.MAX_VALUE;
		for(FacilityDistribution dist1 : dists) {
			double cost= dist1.evaluateDistribution();
			if(cost<minCost) {
				minCost=cost;
				min=dist1;
			}
		}
		
		
		System.out.println("Final Result : "+min.evaluateDistribution());
		Relocation.printArray(min.outputDistribution());
		
//		Map<Integer,Point> map = dist.calculateCentroids();
//		for(int area: map.keySet()) {
//			System.out.println(area+" => { "+map.get(area).i+" , "+map.get(area).j+" }");
//		}
//		
//		System.out.println(dist.evaluateDistribution());
	}
	
	
	
	public static int[][] getMatrixFromFile(String filePath) throws Exception{
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
	

}
