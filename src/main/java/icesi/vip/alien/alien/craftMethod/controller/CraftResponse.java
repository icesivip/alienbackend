package icesi.vip.alien.alien.craftMethod.controller;

import java.util.ArrayList;
import java.util.Map;

public class CraftResponse {
	
	public ArrayList<DistributionResult> distributions;
	public double optimalCost;
	public int[][]optimalDistribution;
	
	
	
	public CraftResponse(Map<int[][], Double> distributions, double optimalCost, int[][] optimalDistribution) {
		super();
		this.distributions = new ArrayList<>();
		for(int[][] dist : distributions.keySet()) {
			this.distributions.add(new DistributionResult(dist, distributions.get(dist)));
		}
		
		
		
		this.optimalCost = optimalCost;
		this.optimalDistribution = optimalDistribution;
	}
	
	
	
	
	
	
	
	

}
