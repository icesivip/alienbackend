package icesi.vip.alien.alien.craftMethod.controller;

import java.util.ArrayList;
import java.util.Map;

import icesi.vip.alien.alien.craftMethod.facility.FacilityDistribution;
import icesi.vip.alien.alien.craftMethod.relocation.Relocation;

public class CraftResponse {
	
	public ArrayList<DistributionResult> distributions;
	public double optimalCost;
	public int[][] optimalDistribution;
	
	
	
	public CraftResponse(Map<FacilityDistribution, Double> distributions, double optimalCost, FacilityDistribution optimalDistribution) {
		super();
		this.distributions = new ArrayList<>();
		for(FacilityDistribution dist : distributions.keySet()) {
			int[][]out=dist.outputDistribution();
			Relocation.printArray(out);
			System.out.println();
			this.distributions.add(new DistributionResult(out, distributions.get(dist)));
		}
		
		
		
		this.optimalCost = optimalCost;
		this.optimalDistribution = optimalDistribution.outputDistribution();
	}
	
	
	
	
	
	
	
	

}
