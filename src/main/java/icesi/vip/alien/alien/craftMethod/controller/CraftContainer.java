package icesi.vip.alien.alien.craftMethod.controller;

import java.util.HashMap;
import java.util.Map;

import icesi.vip.alien.alien.craftMethod.facility.FacilityDistribution;

public class CraftContainer {

	public int[][] distribution;
	
	public double areaSize;
	
	public Map<String,Integer> areaMappings;
	
	
	public int[] nonSwappableAreas;
	
	public int maxSwapAttempts;

	public CraftContainer(int[][] distribution, double areaSize, Map<String, Integer> areaMappings,
			int[] nonSwappableAreas, int maxSwapAttempts) {
		super();
		this.distribution = distribution;
		this.areaSize = areaSize;
		this.areaMappings = areaMappings;
		this.nonSwappableAreas = nonSwappableAreas;
		this.maxSwapAttempts = maxSwapAttempts;
	}
	
	
	
	public CraftResponse compute() throws Exception {
		FacilityDistribution f=new FacilityDistribution(this.distribution);
		for(String s:this.areaMappings.keySet()) {
			String[] areas=s.split("-");
			int a1=Integer.parseInt(areas[0]);
			int a2=Integer.parseInt(areas[1]);
			f.setFromToMapping(a1, a2, this.areaMappings.get(s));
		}
		
		f.setAreaSize(this.areaSize);
		
		for(int a : this.nonSwappableAreas) {
			f.addNonSwappableArea(a);
		}
		
		Map<FacilityDistribution,Double>result= f.CraftAlgorithmFull(this.maxSwapAttempts, false);
		

		
		FacilityDistribution minD=null;
		double minC=Double.POSITIVE_INFINITY;
		for(FacilityDistribution d : result.keySet()) {
			if( result.get(d)<=minC) {
				minD=d;
				minC=result.get(d);
			}
		}
		
		CraftResponse cr=new CraftResponse(result, minC, minD);
		return cr;
		
		
		
		
	}
	
	
	
	
	
	
}
