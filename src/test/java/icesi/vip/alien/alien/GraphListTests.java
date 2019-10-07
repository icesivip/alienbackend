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

public class GraphListTests {
	
	private GraphList graphList;
	
	
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void setUpScenary1() throws IOException {
		graphList = new GraphList("src/main/java/icesi/vip/alien/networks/test.cases/case1.txt");
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
	public void dijkstraTest() throws IOException {
		setUpScenary1();
		int[] correctDistances= {0,4,3,0,0,0,0};
		boolean correct = true;
		/*for (int i = 0; i < graphList.getVertices().size() && correct; i++) {
			int[] dijkstraDistances = graphList.dijkstra((Vertex) graphList.getVertices().get(i));
			if(!Arrays.equals(correctDistances[i], dijkstraDistances)) {
				correct = false;
			}
			
		}*/
		
		int[] dijkstraDistances = graphList.dijkstra((Vertex) graphList.getVertices().get(0));
		for (int i = 0; i < dijkstraDistances.length; i++) {
			System.out.print(dijkstraDistances[i]);
		}
		System.out.println();
		if(!Arrays.equals(correctDistances, dijkstraDistances))
			correct = false;
		
		assertTrue("Dijkstra's algorithm is not implemented correctly", correct);
	}
	

}
