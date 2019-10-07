package icesi.vip.alien.alien;

import java.io.IOException;

import icesi.vip.alien.networks.GraphList;
import icesi.vip.alien.networks.Vertex;

public class Main {

	public static void main(String[] args) throws IOException {
		GraphList gr = new GraphList("src/main/java/icesi/vip/alien/networks/test.cases/case1.txt");
		
		int[] arr = gr.dijkstra((Vertex) gr.getVertices().get(0));
		
		for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i]+" ");
		}
		System.out.println();
	}

}
