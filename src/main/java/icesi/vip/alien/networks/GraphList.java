/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$
 * Icesi University (Cali - Colombia)
 * VIP ALLIEN 
 * @Author: Christian Flor christian.flor1@correo.icesi.edu.co>
 * @Author: Carlos Restrepo carlos.restrepo5@correo.icesi.edu.co>
 * @Author: Cesar Canales cesarcanales80@gmail.com
 * @Date: 23 September 2019
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package icesi.vip.alien.networks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
/**
 *
 * @param <T>
 */
public class GraphList<T> implements GraphListInterface<T> {
	
	// -----------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------
	/**
	 * This attribute tells whether or not the graph is directed.
	 */
	private boolean isDirected;
	/**
	 * This attribute tells whether or not the graph is weighted.
	 */
	private boolean isWeighted;
	/**
	 * The adjacency list of the graph.
	 */
	private ArrayList<Vertex<T>> vertices;
	/**
	 * The hash map of vertices which key is the element and the value is the vertex itself.
	 */
	private HashMap<T, Vertex<T>> hashVertex;
	/**
	 * This String shows the paths taken by the dijkstra's algorithm when it is executed.
	 */
	private String paths;
	/**
	 * 
	 */
	private int numEdges;
	/**
	 * 
	 */
	private int time;
	
	// -----------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------
	/**
	 * 
	 * @param isDirected
	 * @param isWeighted
	 */
	public GraphList(boolean isDirected, boolean isWeighted){
		this.isDirected = isDirected;
		this.isWeighted = isWeighted;
		vertices = new ArrayList<Vertex<T>>();
		hashVertex = new HashMap<T, Vertex<T>>();
	}
	public GraphList(String path) throws IOException {
		File file = new File(path);
		FileReader reader = new FileReader(file);
		BufferedReader in = new BufferedReader(reader);
		
		String line = in.readLine();
		int node = 0;
		while(line!=null) {
			Vertex<Integer> nodeA = new Vertex<>(node);
			vertices.add((Vertex<T>) nodeA);
			
			String[] parts = line.split(";");
			
			for (int i = 0; i < parts.length; i++) {
				int weight = Integer.parseInt(parts[i]);
				if(weight!=-1) {
					Vertex<Integer> nodeB = new Vertex<>(node);
					nodeA.addTriple(weight, nodeB);
				}
			}
			
			line = in.readLine();
			
			node++;
		}
	}
	// -----------------------------------------------------------------
    // Methods for add Vertex
    // -----------------------------------------------------------------
	@Override
	/**
	 * 
	 * @param data
	 */
	public void addVertex(T data) {
		if(!hashVertex.containsKey(data)){
			Vertex<T> add = new Vertex<T>(data);
			vertices.add(add);
			hashVertex.put(data, add);
		}
	}
	
	// -----------------------------------------------------------------
    // Methods for add Edge
    // -----------------------------------------------------------------
	/**
	 *  añadir sin peso
	 * @param dataV1
	 * @param dataV2
	 */
	public void addEdge(T dataV1, T dataV2) {
		if(!hashVertex.containsKey(dataV1)){
			addVertex(dataV1);
		}
		if(!hashVertex.containsKey(dataV2)){
			addVertex(dataV2);
		}
		if(isDirected){
			hashVertex.get(dataV1).addTriple(-1, hashVertex.get(dataV2));
			numEdges++;
		}else{
			hashVertex.get(dataV1).addTriple( -1, hashVertex.get(dataV2));
			numEdges++;
			hashVertex.get(dataV2).addTriple( -1, hashVertex.get(dataV1));
			numEdges++;
		}
	}
	
	@Override
	/**
	 * añadir con peso
	 */
	public void addEdge(T dataV1, T dataV2, int weight) {
		if(!hashVertex.containsKey(dataV1)){
			addVertex(dataV1);
		}
		if(!hashVertex.containsKey(dataV2)){
			addVertex(dataV2);
		}
		if(isDirected){
			hashVertex.get(dataV1).addTriple(weight, hashVertex.get(dataV2));
		}else{
			hashVertex.get(dataV1).addTriple(weight, hashVertex.get(dataV2));
			hashVertex.get(dataV2).addTriple(weight, hashVertex.get(dataV1));
		}
	}

	
	@Override
	/**
	 * 
	 */
	public Vertex<T> searchVertex(T data)  {
		Vertex<T> vertex = hashVertex.get(data);
		if(vertex == null){
			//throw new (data);
			return null;
		}else {
			return vertex;
		}
	}
	
	public void BFS(T data) {
		Vertex<T> s = searchVertex(data);
		BFS(s);
	}
	
	private void BFS(Vertex<T> s){
		for(int i = 0; i < vertices.size(); i++){
			if(!vertices.get(i).equals(s)){
				vertices.get(i).setColor(Vertex.WHITE);
				vertices.get(i).setDistance(Integer.MAX_VALUE);
				vertices.get(i).setPredecessor(null);
			}
		}
		s.setColor(Vertex.GREY);
		s.setDistance(0);
		s.setPredecessor(null);
		Queue<Vertex<T>> queue = new ArrayDeque<Vertex<T>>();
		queue.offer(s);
		while(!queue.isEmpty()){
			Vertex<T> u = queue.poll();
			for(int i = 0; i < u.getTriples().size(); i++){
				Vertex<T> v = u.getTriples().get(i).getVertex();
				if(v.getColor().equals(Vertex.WHITE)){
					v.setColor(Vertex.GREY);
					v.setDistance(u.getTriples().get(i).getVertex().getDistance() + 1);
					v.setPredecessor(u);
					queue.offer(u.getTriples().get(i).getVertex());
				}
			}
			u.setColor(Vertex.BLACK);
			//Solo añadir "u" a arraylist
		}
	}
	
	//--------------------------- DFS con Pila -------------------------------------
	
	public void DFS(T data)  {
		Vertex<T> s = searchVertex(data);
		DFS(s);
	}
	
	private void DFS(Vertex<T> s){
		for(int i = 0; i < vertices.size(); i++){
			if(!vertices.get(i).equals(s)){
				vertices.get(i).setColor(Vertex.WHITE);
				vertices.get(i).setDistance(Integer.MAX_VALUE);
				vertices.get(i).setPredecessor(null);
			}
		}
		s.setColor(Vertex.GREY);
		s.setDistance(0);
		s.setPredecessor(null);
		Stack<Vertex<T>> stack = new Stack<Vertex<T>>();
		stack.push(s);
		while(!stack.isEmpty()){
			Vertex<T> u = stack.pop();
			for(int i = 0; i < u.getTriples().size(); i++){
				Vertex<T> v = u.getTriples().get(i).getVertex();
				if(v.getColor().equals(Vertex.WHITE)){
					v.setColor(Vertex.GREY);
					v.setDistance(u.getTriples().get(i).getVertex().getDistance() + 1);
					v.setPredecessor(u);
					stack.push(u.getTriples().get(i).getVertex());
				}
			}
			u.setColor(Vertex.BLACK);
			//Solo añadir "u" a arraylist
		}
	}
	
	//----------------------- Fin DFS con Pila -------------------------------------
	
	//--------------------------- DFS sin Pila -------------------------------------
	
	public void DFS(){
		for(int i = 0; i < vertices.size(); i++){
			vertices.get(i).setColor(Vertex.WHITE);
			vertices.get(i).setPredecessor(null);
		}
		time = 0;
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).getColor().equals(Vertex.WHITE)){
				visitDFS(vertices.get(i));
			}
		}
	}
	
	public void visitDFS(Vertex<T> u){
		time = time + 1;
		u.setDiscovered(time);
		u.setColor(Vertex.GREY);
		for(int i = 0; i < u.getTriples().size(); i++){
			if(u.getTriples().get(i).getVertex().getColor().equals(Vertex.WHITE)){
				u.getTriples().get(i).getVertex().setPredecessor(u);
				visitDFS(u.getTriples().get(i).getVertex());
			}
		}
		u.setColor(Vertex.BLACK);
		time = time + 1;
		u.setEnded(time);
	}
	
	//----------------------- Fin DFS sin Pila -------------------------------------
	
	/*public ArrayList<Vertex<T>> dijkstra(Vertex<T> ini){
		double[] L = new double[Vertexs.size()];
		for(int i = 0; i < L.length; i++){
			L[i] = Double.MAX_VALUE;
		}
		L[Vertexs.indexOf(ini)] = 0;
		ArrayList<Vertex<T>> S = new ArrayList<Vertex<T>>();
		while(!S.contains(Vertexs.get(Vertexs.size()-1))){
			int indice = 0;
			double valor = Double.MAX_VALUE;
			for(int i = 0; i < L.length; i++){
				if(L[i] < valor && !S.contains(Vertexs.get(i))){
					indice = i;
					valor = L[i];
				}
			}
			Vertex<T> u = Vertexs.get(indice);
			S.add(u);
			for(int i = 0; i < Vertexs.size(); i++){
				if(!S.contains(Vertexs.get(i))){
					if(u.getVertexs().contains(Vertexs.get(i))){
						int j = u.getVertexs().indexOf(Vertexs.get(i));
						double weight = u.getTriples().get(j).getweight();
						if(L[Vertexs.indexOf(u)] + weight < L[Vertexs.indexOf(Vertexs.get(i))]){
							L[Vertexs.indexOf(Vertexs.get(i))] = L[Vertexs.indexOf(u)] + weight;
						}
					}
				}
			}
		}
		return S;
	}*/
	
	//----------------------------------------------------------------------

	/**
	 * This function utilizes dijkstra's algorithm in order to find the shortest route from a source vertex to all other vertices.
	 * <b>Pre:</b> The graph must not have negative cycles.
	 * @param a The source vertex.
	 * @return An array of integers that represents the distances from the source vertex to every other one.
	 */
	@SuppressWarnings("unchecked")
	public int[] dijkstra(Vertex<T> a) {

		boolean[] visited = new boolean[vertices.size()];
		int[] distance = new int[vertices.size()];
		Vertex<T>[] parents = new Vertex[vertices.size()];
		
		PriorityQueue<Vertex<T>> queue = new PriorityQueue<>();
		
		for (int i = 0; i < vertices.size(); i++) {
			queue.add(vertices.get(i));
			distance[i] = Integer.MAX_VALUE;
			parents[i] = null;
		}
		
		int j = vertices.indexOf(a);
		distance[j] = 0;
		queue.add(a);

		while (!queue.isEmpty()) {
			Vertex<T> u = queue.poll();
			int k = vertices.indexOf(u);

			if (!visited[k]) {
				visited[k] = true;
				for (int i = 0; i < u.getTriples().size(); i++) {
					Vertex<T> adj = u.getTriples().get(i).getVertex();
						int weight = (int) u.getTriples().get(i).getWeight();
						if (!visited[i]) {
							relax(k, i, weight, distance, parents);
						}
					}
				
			}
		}
		return distance;
	}
	/**
	 * 
	 * @param current
	 * @param adjacent
	 * @param weight
	 * @param distance
	 * @param queue
	 * @param parents
	 */
	private void relax(int current, int adjacent, int weight, int []distance, Vertex<T>[] parents) {
		
		if (distance[current] + weight < distance[adjacent]) {
			distance[adjacent] = distance[current] +weight;
			parents[adjacent] = vertices.get(current);
			vertices.get(adjacent).setDistance(distance[adjacent]);
		}
		
	}
	/**
	 * 
	 */
	public void pathByDijkstra (T start, T end)  {
		/*int goal = vertices.indexOf(searchVertex(end));
		
		paths = "";
		Vertex<T> Vertexstart = searchVertex(start);
		
		Vertex<T>[] parents = dijkstra(Vertexstart);
		
		if (parents[vertices.indexOf(vertices.get((int) end))] != null) {
			pathByDijkstra(start, parents[vertices.indexOf(vertices.get((int) end))].getdata());
		}
		paths += vertices.get(goal).getdata()+",";*/
	}
	//----------------------------------------------------------------------
	/**
	 * 
	 * @return
	 *//*
	public String[][] floydWarshall(){
		
		double[][] dist = new double[vertices.size()][vertices.size()];
		String[][] pathBack = new String[vertices.size()][vertices.size()];
				
		for(int i = 0; i < dist.length; i++){
			for(int j = 0; j < dist[0].length; j++){
				if(j == i){
					dist[i][j] = 0;
					pathBack[i][j] = vertices.get(i).getdata() + "";
				}else if(vertices.get(i).getVertices().contains(vertices.get(j))){
					dist[i][j] = vertices.get(i).getTriples().get(vertices.get(i).getVertices().indexOf(vertices.get(j))).getWeight();
					pathBack[i][j] = vertices.get(i).getdata() + "," + vertices.get(j).getdata();
				}
				else{
					dist[i][j] = Double.MAX_VALUE;
					pathBack[i][j] = "";
				}
			}
		}
		for(int k = 0; k < dist.length; k++){
			for(int i = 0; i < dist[0].length; i++){
				for(int j = 0; j < dist[0].length; j++){
					if(dist[i][j] > dist[i][k] + dist[k][j]){
						dist[i][j] = dist[i][k] + dist[k][j];
						pathBack[i][j] = pathBack[i][k].substring(0, pathBack[i][k].length()) + "," + pathBack[k][j];
					}					
				}
			}
		}
		return pathBack;
	}*/
	/**
	 * 
	 * @param arre
	 * @return
	 */
	private String noRepetitions(String arre){

		String[] arr = arre.split(",");

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				if (i != j) {
					if (arr[i] != null && arr[j] != null) {
						if (arr[i].equals(arr[j])) {
							arr[j] = null;
						}
					}
				}
			}
		}

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				paths += arr[i] + ",";
			}
		}
		return paths;
	}
	/**
	 * 
	 */
	public void pathByFloyd(T start, T end) {
		int i = vertices.indexOf(searchVertex(start));
		int j = vertices.indexOf(searchVertex(end));;
		paths = "";
		//paths = noRepetitions(floydWarshall()[i][j]);
	}

	/**
	 * 
	 * @param s
	 * @param v
	 */
	public void printPath(Vertex<T> s, Vertex<T> v){
		if(s == v){
			System.out.println(s.getdata());
		}else if(v.getPredecessor() == null){
			System.out.println("No hay camino de " + s.getdata() + " hasta " + v.getdata());
		}else{
			printPath(s, v.getPredecessor());
			System.out.println(v.getdata());
		}
	}
	
	// -----------------------------------------------------------------
    // Methods Atributes
    // -----------------------------------------------------------------
	/**
	 * 
	 */
	public ArrayList<Vertex<T>> getVertices(){
		return vertices;
	}

	/**
	 * 
	 * @return
	 */
	public String getPaths() {
		return paths;
	}
	/**
	 * 
	 * @param paths
	 */
	public void setPaths(String paths) {
		this.paths = paths;
	}
	
	
}
