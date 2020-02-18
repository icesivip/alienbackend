package icesi.vip.alien.networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public   class AdjListGraph<T extends Integer > implements IGraph<T > {

	/**
	 * This attribute determines whether or not this graph is directed or undirected.
	 */
	private boolean directed;
	
	/**
	 * This attribute determines whether or not this graph is weighter or not.
	 */
	private boolean weighted;
	
	/**
	 * This attribute represents the number of vertices of this graph.
	 */
	private int numberOfVertices;
	
	/**
	 * This attribute represents the number of edges of this graph.
	 */
	private int numberOfEdges;
	
	/**
	 * The list of vertices of the graph.
	 */
	private List<Vertex<T>> vertices;
	
	/**
	 * This map helps to obtain the adjacency vertex of a given one in order to facilitate the information management.
	 */
	private HashMap<T, AdjVertex<T>> map;
	
	
	
	/**
	 * This function initializes a new adjacency list graph.
	 * @param directed This attribute determines whether or not this graph is directed or undirected.
	 * @param weighted This attribute determines whether or not this graph is weighted or not.
	 */
	public AdjListGraph(boolean directed, boolean weighted) {
		this.directed = directed;
		this.weighted = weighted;
		numberOfVertices = 0;
		numberOfEdges = getNumberOfEdges();
		vertices = new LinkedList<Vertex<T>>();
		map = new HashMap<>();
	}
	
	
	
	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#addVertex(java.lang.Object)
	 * This function is responsible of adding a new vertex into the current graph.
	 */
	@Override
	public void addVertex(T value) {
		if (!isInGraph(value)) {
			AdjVertex<T> vertex = new AdjVertex<T>(value);
			map.put(value, vertex);
			vertex.setIndex(numberOfVertices);
			vertices.add(vertex);
			numberOfVertices++;
		}
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#addEdge(java.lang.Object, java.lang.Object)
	 * This function is responsible of adding a new edge in the graph.
	 */
	@Override
	public void addEdge(T x, T y) {
		AdjVertex<T> from = searchVertex(x);
		AdjVertex<T> to = searchVertex(y);
		addEdge(from, to);
	}

	/**
	 * This function is responsible of adding a new edge into the current graph.
	 * @param from The vertex from where the edge comes from.
	 * @param to The vertex to where the edge is directed.
	 */
	public void addEdge(AdjVertex<T> from, AdjVertex<T> to) {
		addEdge(from, to, 1D, 0);
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#addEdge(java.lang.Object, java.lang.Object, double, int)
	 * This function is responsible of adding a new edge into the current graph.
	 */
	@Override
	public void addEdge(T x, T y, double w, int id) {
		if (weighted) {
			AdjVertex<T> from = searchVertex(x);
			AdjVertex<T> to = searchVertex(y);
			addEdge(from, to, w, id);
		}
	}

	/**
	 * This function is responsible of adding a new edge into the current graph, with weight included.
	 * @param from The vertex from where the edge comes from.
	 * @param to The vertex to where the edge is directed.
	 * @param w The weight of the edge to be added.
	 * @param id The id of the edge to be added.
	 */
	public void addEdge(AdjVertex<T> from, AdjVertex<T> to, double w, int id) {
		if (from != null && to != null) {
			Edge<T> edge = new Edge<T>(from, to, w);
			edge.setId(id);
			from.getAdjList().add(edge);
			if (!isDirected()) {
				edge = new Edge<T>(to, from, w);
				edge.setId(id);
				to.getAdjList().add(edge);
			}
			numberOfEdges++;
		}

	}

	/**
	 * This function is responsible of removing an existing vertex from the graph.
	 * @param v The vertex to be added to the graph.
	 */
	public void removeVertex(Vertex<T> v) {
		for (int i = 0; i < vertices.size(); i++) {
			removeEdge(vertices.get(i), v);
			if (isDirected()) {
				removeEdge(v, vertices.get(i));
			}
		}
		vertices.remove(v);
		map.remove(v.getValue());
		numberOfVertices--;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#removeVertex(java.lang.Object)
	 * This function is responsible of removing a vertex from the current graph.
	 */
	public void removeVertex(T v) {
		if (isInGraph(v)) {
			removeVertex(searchVertex(v));
		}
	}

	/**
	 * This function is responsible of removing an existing edge from the current graph.
	 * @param x The vertex from where the edge comes from.
	 * @param y The vertex to where the edge goes to.
	 */
	public void removeEdge(Vertex<T> x, Vertex<T> y) {
		AdjVertex<T> from = (AdjVertex<T>) x;
		AdjVertex<T> to = (AdjVertex<T>) y;
		List<Edge<T>> adjFrom = from.getAdjList();
		Edge<T> e = from.findEdge(to);
		if (e != null)
			adjFrom.remove(e);

		if (!isDirected()) {
			List<Edge<T>> adjTo = to.getAdjList();
			e = to.findEdge(from);
			if (e != null)
				adjTo.remove(e);
		}

		numberOfEdges--;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#removeEdge(java.lang.Object, java.lang.Object)
	 * This function is responsible of removing an existing edge from the graph.
	 */
	public void removeEdge(T x, T y) {
		if (isInGraph(x) && isInGraph(y)) {
			removeEdge(searchVertex(x), searchVertex(y));
		}
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#getNeighbors(icesi.vip.alien.networks.Vertex)
	 * This function is responsible for getting all adjacent vertices of a given vertex.
	 */
	public List<Vertex<T>> getNeighbors(Vertex<T> x) {
		List<Vertex<T>> neigh = new ArrayList<>();
		AdjVertex<T> from = (AdjVertex<T>) x;
		List<Edge<T>> adj = from.getAdjList();
		for (int i = 0; i < adj.size(); i++) {
			neigh.add(adj.get(i).getDestination());
		}
		return neigh;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#areAdjacent(icesi.vip.alien.networks.Vertex, icesi.vip.alien.networks.Vertex)}
	 * This function determines whether or not two given vertices are adjacent.
	 */
	public boolean areAdjacent(Vertex<T> x, Vertex<T> y) {
		return getNeighbors(x).contains(y);
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#isInGraph(java.lang.Object)
	 * This function determines whether or not a given vertex is part of the current graph.
	 */
	public boolean isInGraph(T value) {
		return searchVertex(value) != null;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#getEdgeWeight(icesi.vip.alien.networks.Vertex, icesi.vip.alien.networks.Vertex)
	 * This function obtains the weight of an edge which contains two given vertices.
	 */
	public double getEdgeWeight(Vertex<T> x, Vertex<T> y) {
		double w = 0;
		if (isInGraph(x.getValue()) && isInGraph(y.getValue())) {
			AdjVertex<T> from = (AdjVertex<T>) x;
			AdjVertex<T> to = (AdjVertex<T>) y;
			Edge<T> e = from.findEdge(to);
			if (e != null)
				w = e.getWeight();
		}
		return w;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#setEdgeWeight(icesi.vip.alien.networks.Vertex, icesi.vip.alien.networks.Vertex, double)
	 * This function modifies the edge weight of a given edge.
	 */
	public void setEdgeWeight(Vertex<T> x, Vertex<T> y, double w) {
		if (isInGraph(x.getValue()) && isInGraph(y.getValue()) && weighted) {
			AdjVertex<T> from = (AdjVertex<T>) x;
			AdjVertex<T> to = (AdjVertex<T>) y;
			Edge<T> e = from.findEdge(to);
			if (e != null)
				e.setWeight(w);

			if (!isDirected()) {
				e = to.findEdge(from);
				if (e != null)
					e.setWeight(w);
			}
		}
	}

	/**
	 * This function searches for the adjacency vertex of a given vertex, given only the value of the vertex.
	 * @param value The value of the vertex to be searched.
	 * @return The adjacency vertex that corresponds to the searched value.
	 */
	public AdjVertex<T> searchVertex(T value) {
		return map.get(value);
	}

	/**
	 * This function is responsible of obtaining the index in the list of vertices of a given vertex.
	 * @param v The vertex to be searched.
	 * @return The index of the given vertex in the list of vertices.
	 */
	public int getIndexOf(Vertex<T> v) {
		int index = -1;
		boolean searching = true;
		for (int i = 0; i < vertices.size() && searching; i++) {
			if (vertices.get(i) == v) {
				index = i;
				searching = false;
			}
		}
		return index;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#bfs(icesi.vip.alien.networks.Vertex)
	 * This function does a bfs from a given source vertex.
	 */
	public void bfs(Vertex<T> x) {
		AdjVertex<T> s = (AdjVertex<T>) x;
		for (Vertex<T> u : vertices) {
			u.setColor(Vertex.WHITE);
			u.setD(INF);
			u.setPred(null);
		}
		s.setColor(Vertex.GRAY);
		s.setD(0);
		s.setPred(null);
		Queue<AdjVertex<T>> q = new LinkedList<>();
		q.offer(s);
		while (!q.isEmpty()) {
			AdjVertex<T> u = q.poll();
			for (int i = 0; i < u.getAdjList().size(); i++) {
				AdjVertex<T> v = (AdjVertex<T>) u.getAdjList().get(i).getDestination();
				if (v.getColor() == Vertex.WHITE) {
					v.setColor(Vertex.GRAY);
					v.setD(u.getD() + 1);
					v.setPred(u);
					q.offer(v);
				}
			}
			u.setColor(Vertex.BLACK);
		}
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#dfs()
	 * This function does a bfs from a given source vertex.
	 */
	public int dfs() {
		int components = 0;
		for (Vertex<T> u : vertices) {
			u.setColor(Vertex.WHITE);
			u.setPred(null);
		}
		int time = 0;
		for (Vertex<T> u : vertices) {
			if (u.getColor() == Vertex.WHITE) {
				time = dfsVisit((AdjVertex<T>) u, time);
				components++;
			}
		}
		return components;
	}

	/**
	 * This function is the recursive implementation of the dfs responsible of visiting the vertices in the correct order or a dfs algorithm.
	 * @param u The node that the algorithm is currently standing on.
	 * @param time Thr current accumulated time in the algorithm.
	 * @return
	 */
	private int dfsVisit(AdjVertex<T> u, int time) {
		time++;
		u.setD(time);
		u.setColor(Vertex.GRAY);
		for (int i = 0; i < u.getAdjList().size(); i++) {
			AdjVertex<T> v = (AdjVertex<T>) u.getAdjList().get(i).getDestination();
			if (v.getColor() == Vertex.WHITE) {
				v.setPred(u);
				time = dfsVisit(v, time);
			}
		}
		u.setColor(Vertex.BLACK);
		time++;
		u.setF(time);
		return time;
	}

	 
		

	/**
	 * This function initializes everything that is required for the single source shortest paths solution.
	 * @param s The given source vertex.
	 */
	private void initSingleSource(AdjVertex<T> s) {
		for (Vertex<T> u : vertices) {
			u.setD(INF);
			u.setPred(null);
		}
		s.setD(0);
	}

	/**
	 * This function executes Dijkstra's algorithm.
	 * @param x The source vertex.
	 * @return A string representing the steps taken to solve the problem.
	 */
	public String dijkstra(Vertex<T> x) {
		String graph = "";
		AdjVertex<T> s = (AdjVertex<T>) x;
		initSingleSource(s);
		PriorityQueue<AdjVertex<T>> queue = new PriorityQueue<>();
		queue.add(s);
		while (!queue.isEmpty()) {
			AdjVertex<T> u = queue.poll();
			for (Edge<T> e : u.getAdjList()) {

				AdjVertex<T> v = (AdjVertex<T>) e.getDestination();
				double weight = e.getWeight();
				graph += u.getValue()+","+e.getId()+","+v.getValue()+",";
				// relax(u,v,weight)
				double distanceFromU = u.getD() + weight;
				if (distanceFromU < v.getD()) {
					queue.remove(v);
					v.setD(distanceFromU);
					v.setPred(u);
					queue.add(v);
					graph+= "1-";
				}else
					graph+= "0-";
			}
		}
		return graph;
	}

	/**
	 * This function executes Floyd Warshall's algorithm.
	 * @return A matrix of doubles representing the distances between every pair of nodes.
	 */
	public double[][] floydwarshall() {
		double[][] weights = getWeightsMatrix();
		for (int k = 0; k < vertices.size(); k++) {
			for (int i = 0; i < vertices.size(); i++) {
				for (int j = 0; j < vertices.size(); j++) {
					weights[i][j] = Math.min(weights[i][j], weights[i][k] + weights[k][j]);
				}
			}
		}
		return weights;
	}

	/**
	 * This function builds a weights matrix from the adjacency list graph.
	 * @return The weigths matrix.
	 */
	private double[][] getWeightsMatrix() {
		double[][] weights = new double[vertices.size()][vertices.size()];
		for (int i = 0; i < weights.length; i++) {
			Arrays.fill(weights[i], INF);
		}
		for (int i = 0; i < vertices.size(); i++) {
			weights[i][i] = 0;
			AdjVertex<T> u = (AdjVertex<T>) vertices.get(i);
			for (Edge<T> e : u.getAdjList()) {
				AdjVertex<T> v = (AdjVertex<T>) e.getDestination();
				double weight = e.getWeight();
				weights[i][getIndexOf(v)] = weight;
			}
		}
		return weights;
	}

	/**
	 * This function executes prim's algorithm.
	 * @param s The source vertex.
	 */
	public void prim(Vertex<T> s) {
		AdjVertex<T> r = (AdjVertex<T>) s;
		for (Vertex<T> u : vertices) {
			u.setD(INF);
			u.setColor(Vertex.WHITE);
		}
		r.setD(0);
		r.setPred(null);
		PriorityQueue<AdjVertex<T>> queue = new PriorityQueue<>();
		for (Vertex<T> u : vertices) {
			queue.add((AdjVertex<T>) u);
		}
		while (!queue.isEmpty()) {
			AdjVertex<T> u = queue.poll();
			for (Edge<T> e : u.getAdjList()) {
				AdjVertex<T> v = (AdjVertex<T>) e.getDestination();
				if (v.getColor() == Vertex.WHITE && e.getWeight() < v.getD()) {
					queue.remove(v);
					v.setD(e.getWeight());
					queue.add(v);
					v.setPred(u);
				}
			}
			u.setColor(Vertex.BLACK);
		}
	}

	/**
	 * This function executes Kruskal's algorithm.
	 * @return A string representing the steps taken to solve the minimum spanning tree problem.
	 */
	public String kruskal() { // Adapted from
		String ans = "";					// www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
		ArrayList<Edge<T>> result = new ArrayList<>(); // Tnis will store the resultant MST
		int e = 0; // An index variable, used for result[]
		int i = 0; // An index variable, used for sorted edges

		ArrayList<Edge<T>> edges = getEdges();

		// Step 1: Sort all the edges in non-decreasing order of their
		// weight. If we are not allowed to change the given graph, we
		// can create a copy of array of edges
		Collections.sort(edges);

		UnionFind uf = new UnionFind(vertices.size());
		double sum=0;
		i = 0; // Index used to pick next edge

		// Number of edges to be taken is equal to V-1
		while (i < edges.size()) {
			// Step 2: Pick the smallest edge. And increment
			// the index for next iteration
			Edge<T> edge = edges.get(i);
			i++;
			int source = (int) edge.getSource().getValue();
			int destination = (int) edge.getDestination().getValue();
			int x = uf.find(getIndexOf(edge.getSource()));
			int y = uf.find(getIndexOf(edge.getDestination()));
			
			ans+=source+","+edge.getId()+","+destination+"-";
			// If including this edge does't cause cycle,
			// include it in result and increment the index
			// of result for next edge
			if (x != y) {
				result.add(edge);
				e++;
				uf.union(x, y);
				ans+="1-";
				sum+=edge.getWeight();
			}else {
				ans+="0-";
			}
			// Else discard the edge
		}
		ans+=""+sum;
		return ans;
	}

	/**
	 * This function obtains the list of edges of the graph.
	 * @return The list of edges.
	 */
	public ArrayList<Edge<T>> getEdges() {
		ArrayList<Edge<T>> edges = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			AdjVertex<T> v = (AdjVertex<T>) vertices.get(i);
			for (int j = 0; j < v.getAdjList().size(); j++) {
				edges.add(v.getAdjList().get(j));
			}
		}
		return edges;
	}
	
	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#getVertices()
	 * This function obtains the list of vertices of the graph.
	 */
	public List<Vertex<T>> getVertices() {
		return vertices;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#getNumberOfVertices()
	 * This function obtains the number of vertices of the graph.
	 */
	public int getNumberOfVertices() {
		return numberOfVertices;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#getNumberOfEdges()
	 * This function obtains the number of edges of the graph.
	 */
	public int getNumberOfEdges() {
		return numberOfEdges;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#isDirected()
	 * This function return the directed attribute.
	 */
	public boolean isDirected() {
		return directed;
	}

	/* (non-Javadoc)
	 * @see icesi.vip.alien.networks.IGraph#isWeighted()
	 * This function returns the weighted attribute.
	 */
	public boolean isWeighted() {
		return weighted;
	}


}