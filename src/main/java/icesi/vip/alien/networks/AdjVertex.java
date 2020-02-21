package icesi.vip.alien.networks;

import java.util.LinkedList;
import java.util.List;

public class AdjVertex<Integer> extends Vertex<Integer> {

	private List<Edge <Integer>> adjList;
	
	public AdjVertex(Integer value) {
		super(value);
		adjList= new LinkedList<Edge<Integer>>();
	}
	
	public List<Edge<Integer>> getAdjList(){
		return adjList;
	}
	
	public boolean isAdjacent(AdjVertex<Integer> vertex) {
		for(int i = 0; i<adjList.size(); i++) {
			if (adjList.get(i).getDestination()==vertex)
				return true;
		}
		return false;
	}
	
	public Edge<Integer> findEdge(AdjVertex<Integer> vertex){
		for (int i=0; i<adjList.size(); i++) {
			if (adjList.get(i).getDestination()==vertex)
				return adjList.get(i);
		}
		return null;
	}

}