package icesi.vip.alien.alien.craftMethod.controller;

import java.util.Map;

public class CraftResponse {
	
	public Map<int[][],Double> distributions;
	public double optimalCost;
	public int[][]optimalDistribution;
	
	
	
	public CraftResponse(Map<int[][], Double> distributions, double optimalCost, int[][] optimalDistribution) {
		super();
		this.distributions = distributions;
		this.optimalCost = optimalCost;
		this.optimalDistribution = optimalDistribution;
	}
	
	
	
	
	
	
	
	

}
