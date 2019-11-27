package icesi.vip.alien.networks;


import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.*;


class AdjListGraphTest {
	/**
	 * graph implemented as adjacency list
	 */
	private AdjListGraph<Integer> graph;
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

			
			graph = new AdjListGraph(false,true);
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
	

}

