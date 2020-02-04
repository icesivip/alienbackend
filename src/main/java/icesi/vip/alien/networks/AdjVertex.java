package icesi.vip.alien.networks;

import java.util.LinkedList;
import java.util.List;

public class AdjVertex<T> extends Vertex<T> {

	private List<Edge <T>> adjList;
	
	public AdjVertex(T value) {
		super(value);
		adjList= new LinkedList<Edge<T>>();
	}
	
	public List<Edge<T>> getAdjList(){
		return adjList;
	}
	
	public boolean isAdjacent(AdjVertex<T> vertex) {
		for(int i = 0; i<adjList.size(); i++) {
			if (adjList.get(i).getDestination()==vertex)
				return true;
		}
		return false;
	}
	
	public Edge<T> findEdge(AdjVertex<T> vertex){
		for (int i=0; i<adjList.size(); i++) {
			if (adjList.get(i).getDestination()==vertex)
				return adjList.get(i);
		}
		return null;
	}

}