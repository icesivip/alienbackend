package icesi.vip.alien.networks;


import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

class AdjListGraphTest {
	/**
	 * graph of integers implemented as adjacency list
	 */
	private AdjListGraph<Integer> graphOne;
	/**
	 * graph of string implemented as adjacency list
	 */
	private AdjListGraph<String> graphTwo;
	/**
	 * List of graphs that will be used to test the algorithms
	 */
	private List<AdjListGraph<Integer>> graphsList;
	
	/**
	 * This scenario is responsible for creating a list of graphs, which are read from a text file.
	 * This is done with the intention of having many graphs as test cases for the algorithms.
	 * @throws IOException
	 */
	private void stage1() throws IOException {
		graphsList = new ArrayList<>();
		File file = new File("src\\test\\java\\icesi\\vip\\alien\\networks\\cases\\case1.txt");
		FileReader reader = new FileReader(file);
		Scanner in = new Scanner(reader);
		int cases = in.nextInt();
		while(cases-->0) {
			int vertex = in.nextInt();
			int edges = in.nextInt();

			
			AdjListGraph<Integer> graph = new AdjListGraph(false,true);
			for (int i = 0; i < vertex; i++) {
				graph.addVertex(i);
			}
			for(int i=0; i<edges; i++) {
				int inicio = in.nextInt();
				int fin = in.nextInt();
				int weight = in.nextInt();

				graph.addEdge(inicio, fin, weight,i);
			}
			graphsList.add(graph);
			
		}	
	}
	/**
	 * This scenery is responsible of initializing a graph which vertices are strings that represent cities
	 * The edges, in the other hand, represent the cost of travelling from one city to another.
	 */
	public void stage2() {
		//vertices
		graphTwo = new AdjListGraph<>(false, true);
		graphTwo.addVertex("Boston");
		graphTwo.addVertex("Nueva York");
		graphTwo.addVertex("Chicago");
		graphTwo.addVertex("Dallas");
		graphTwo.addVertex("Denver");
		graphTwo.addVertex("San Francisco");
		graphTwo.addVertex("Los Angeles");
		// Edges
		graphTwo.addEdge("San Francisco", "Los Angeles", 400,0);
		graphTwo.addEdge("San Francisco", "Denver", 1000,1);
		graphTwo.addEdge("San Francisco", "Chicago", 1500,2);
		graphTwo.addEdge("Los Angeles", "Chicago", 1400,3);
		graphTwo.addEdge("Los Angeles", "Dallas", 1100,4);
		graphTwo.addEdge("Denver", "Chicago", 500,5);
		graphTwo.addEdge("Denver", "Dallas", 600,6);
		graphTwo.addEdge("Dallas", "Chicago", 800,7);
		graphTwo.addEdge("Dallas", "Nueva York", 1200,8);
		graphTwo.addEdge("Chicago", "Nueva York", 700,9);
		graphTwo.addEdge("Boston", "Nueva York", 300,10);
		graphTwo.addEdge("Boston", "Chicago", 900,11);
	}
	/**
	 * This scenery is responsible of initializing an undirected graph.
	 */
	public void stage3() {
		graphOne = new AdjListGraph<Integer>(false, true);
		graphOne.addVertex(1);
		graphOne.addVertex(2);
		graphOne.addVertex(4);
		graphOne.addVertex(5);
		graphOne.addVertex(7);
	}
	/**
	 * This scenery is responsible of initializing a directed graph.
	 */
	public void stage4() {
		graphOne = new AdjListGraph<Integer>(true, true);
		graphOne.addVertex(1);
		graphOne.addVertex(2);
		graphOne.addVertex(4);
		graphOne.addVertex(5);
		graphOne.addVertex(7);
		
	}
	/**
	 * This function is responsible of testing the add vertex functionality of the graph.
	 */

	@Test
	public void testAddVertex() {
		// Test 1
		graphOne = new AdjListGraph<Integer>(false, true);
		graphOne.addVertex(1);
		assertTrue(graphOne.isInGraph(1));
		assertTrue(graphOne.getVertices().size() == 1);

		// Test 2
		stage3();
		graphOne.addVertex(3);
		assertTrue(graphOne.isInGraph(3));
		assertTrue(graphOne.getVertices().size() == 6);

		// Test 3
		stage3();
		graphOne.addVertex(4);
		assertTrue(graphOne.isInGraph(1));
		assertTrue(graphOne.isInGraph(2));
		assertTrue(graphOne.isInGraph(4));
		assertTrue(graphOne.getVertices().size() == 5);
	}
	/**
	 * 	This function tests the add edge functionality of the graph.
	 */
	@Test
	public void testAddEdge() {
		// Test 1
		stage4();
		graphOne.addEdge(5, 7, 3,0);
		assertTrue(graphOne.searchVertex(5).isAdjacent(graphOne.searchVertex(7)));
		assertTrue(graphOne.searchVertex(5).findEdge(graphOne.searchVertex(7)).getWeight() == 3);
		assertTrue(graphOne.searchVertex(7).findEdge(graphOne.searchVertex(5)) == null);

		// Test 2
		stage3();
		graphOne.addEdge(5, 7, 3,0);
		assertTrue(graphOne.searchVertex(5).isAdjacent(graphOne.searchVertex(7)));
		assertTrue(graphOne.searchVertex(5).findEdge(graphOne.searchVertex(7)).getWeight() == 3);
		assertTrue(graphOne.searchVertex(7).findEdge(graphOne.searchVertex(5)) != null);

		// Test 3
		stage2();
		graphTwo.addVertex("Medellin");
		graphTwo.addVertex("Cali");
		graphTwo.addEdge("Medellin", "Cali", 1500,0);
		assertTrue(graphTwo.searchVertex("Medellin").isAdjacent(graphTwo.searchVertex("Cali")));
		assertTrue(graphTwo.searchVertex("Medellin").findEdge(graphTwo.searchVertex("Cali")).getWeight() == 1500);
		assertTrue(graphTwo.searchVertex("Cali").findEdge(graphTwo.searchVertex("Medellin")) != null);

	}
	
	/**
	 * This function is responsible for testing the kruskal algorithm implemented with an adjacency list graph.
	 * The test is done by running the algorithm on many graphs that are present in the list of graphs initialized in stage 1.
	 * The correct solution that the algorithm should return is in a text file called kruskal_output
	 * @throws IOException
	 */
	@Test
	void testKruskal() throws IOException {
		stage1();
		File file = new File("src\\test\\java\\icesi\\vip\\alien\\networks\\cases\\kruskal_output.txt");
		FileReader reader = new FileReader(file);
		Scanner in = new Scanner(reader);
		boolean valid = true; 
		for (int i = 0; i < graphsList.size(); i++) {
			String[] krus = graphsList.get(i).kruskal().split("-");
			int sum = (int) Double.parseDouble(krus[krus.length-1]);
			int correct = in.nextInt();
			if(correct!=sum) {
				valid = false;
			}
		}
		
		assertTrue("kruskal algorithm doesn't work", valid);
	}
	
	/**
	 * This function is responsible for testing the dfs algorithm implemented with an adjacency list graph.
	 * The test is done by running the algorithm on many graphs that are present in the list of graphs initialized in stage 1.
	 * The correct solution that the algorithm should return is in a text file called DFS_output
	 * @throws IOException
	 */
	@Test
	void testDFS() throws IOException {
		stage1();
		File file = new File("src\\test\\java\\icesi\\vip\\alien\\networks\\cases\\DFS_output.txt");
		FileReader reader = new FileReader(file);
		Scanner in = new Scanner(reader);
		boolean valid = true; 
		for (int i = 0; i < graphsList.size(); i++) {
			int components = graphsList.get(i).dfs();
			int correct= in.nextInt();
			if(correct!=components) {
				valid = false;
			}

		}
		assertTrue("DFS algorithm doesn't work", valid);
	}
	/**
	 * This function is responsible of testing Dijkstra's algorithm.
	 * The results that the algorithm must return are known previously.
	 */
	@Test
	public void testDijkstra() {
		stage2();
		// Test 1
		graphTwo.dijkstra(graphTwo.searchVertex("Dallas"));
		assertTrue(graphTwo.searchVertex("Boston").getD() == 1500);

		// Test 2

		graphTwo.dijkstra(graphTwo.searchVertex("San Francisco"));
		assertTrue(graphTwo.searchVertex("Dallas").getD() == 1500);

		// Test 3
		graphTwo.dijkstra(graphTwo.searchVertex("Chicago"));
		assertTrue(graphTwo.searchVertex("Los Angeles").getD() == 1400);

		
	}
}

