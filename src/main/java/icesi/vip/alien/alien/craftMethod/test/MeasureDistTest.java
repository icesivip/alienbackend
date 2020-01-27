package icesi.vip.alien.alien.craftMethod.test;

import icesi.vip.alien.alien.craftMethod.facility.FacilityDistribution;

public class MeasureDistTest {

	
	
	public static void main(String[] args) throws Exception {
		FacilityDistribution dist= FacilityDistribution.getFromFile("craft2.txt");
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
		System.out.println(dist.evaluateDistribution());
	}
}
