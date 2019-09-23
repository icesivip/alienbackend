package icesi.vip.alien.networks;

import java.util.ArrayList;

public interface GraphListInterface<T> {
	
	public void addVertex(T data);
	
	public void removeVertex(T data);
	
	public void addEdge(T dataV1, T dataV2, double weight, String name);
	
	public Vertex<T> searchVertex(T data) ;
	
	public void BFS(T data) ;
	
	public void DFS(T data) ;
	
	public void pathByDijkstra (T start, T end) ;
	
	public void pathByFloyd (T start, T end) ;
	
	public void removeEdge(T dataV1, T dataV2) ;
	
	public ArrayList<Vertex<T>> getVertices();

}
