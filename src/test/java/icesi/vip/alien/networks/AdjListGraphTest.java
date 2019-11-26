package icesi.vip.alien.networks;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import icesi.vip.alien.networks.AdjListGraph;
import icesi.vip.alien.networks.Edge;

class AdjListGraphTest {

	private AdjListGraph<Integer> graph;
	private List<AdjListGraph<Integer>> graphsList;
	
	// graph undirected and weighted
	private void stage1() throws IOException {
		
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

				graph.addEdge(inicio, fin, weight);
			}
			graphsList.add(graph);
			
		}	
	}
	
	@Test
	private void testKruskal() throws IOException {
		stage1();
		File file = new File("src\\test\\java\\icesi\\vip\\alien\\networks\\cases\\kruskal_output.txt");
		FileReader reader = new FileReader(file);
		Scanner in = new Scanner(reader);
		boolean valid = true; 
		for (int i = 0; i < graphsList.size(); i++) {
			ArrayList<Edge<Integer>> edgesList = graphsList.get(i).kruskal();
			int sum = 0;
			for (int j = 0; j < edgesList.size(); j++) {
				sum+= (int) edgesList.get(i).getWeight();
			}
			int correct = in.nextInt();
			if(correct!=sum) {
				valid = false;
			}
		}
		
		assertTrue("kruskal algorithm doesn't work", valid);
	}
	
	private void testDFS() throws IOException {
		stage1();
		
		boolean valid = true; 
		for (int i = 0; i < graphsList.size(); i++) {
			ArrayList<Edge<Integer>> edgesList = graphsList.get(i).kruskal();
			int sum = 0;
			for (int j = 0; j < edgesList.size(); j++) {
				sum+= (int) edgesList.get(i).getWeight();
			}
		}
	}
	

}
