package icesi.vip.alien.networks;

import java.util.List;

public interface IGraph<Integer> {
	
	public double INF = Double.POSITIVE_INFINITY;

	public List<Vertex<Integer>> getVertices();
	
	public boolean isDirected();
	
	public boolean isWeighted();

	
	public void addVertex(Integer value);

	public void addEdge(Integer x, Integer y);
	
	public void addEdge(Integer x, Integer y, double w, int id);


	public void removeVertex(Integer v);

	public void removeEdge(Integer x, Integer y);

	public List<Vertex<Integer>> getNeighbors(Vertex<Integer> x);

	public int getNumberOfVertices();

	public int getNumberOfEdges();

	public boolean areAdjacent(Vertex<Integer> x, Vertex<Integer> y);

	public boolean isInGraph(Integer value);

	public double getEdgeWeight(Vertex<Integer> x, Vertex<Integer> y);

	public void setEdgeWeight(Vertex<Integer> x, Vertex<Integer> y, double w);

	public void bfs(Vertex<Integer> s);

	public int dfs();

}