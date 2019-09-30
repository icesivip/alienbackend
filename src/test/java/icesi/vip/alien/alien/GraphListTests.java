package icesi.vip.alien.alien;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.networks.GraphList;
import icesi.vip.alien.networks.Vertex;

@RunWith(SpringRunner.class)
@SpringBootTest


//mvnw test

class GraphListTests {
	
	private GraphList graphList;
	
	
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void setUpScenary1() throws IOException {
		graphList = new GraphList("test1");
	}
	/**
	 *
	 */
	public void setUpScenary2() {
		
	}
	/**
	 *  
	 */
	public void setUpScenary() {
		
	}
	@Test
	public void dijkstraTest() {
		int[][] correctDistances= {};
		
		boolean correct = true;
		for (int i = 0; i < graphList.getVertices().size() && correct; i++) {
			int[] dijkstraDistances = graphList.dijkstra((Vertex) graphList.getVertices().get(i));
			if(!Arrays.equals(correctDistances[i], dijkstraDistances)) {
				correct = false;
			}
			
		}
		
		assertTrue("Dijkstra's algorithm is not implemented correctly", correct);
	}
	

}
