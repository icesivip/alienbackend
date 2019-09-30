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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
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
	 * 
	 */
	private boolean isDirected;
	/**
	 * 
	 */
	private boolean isWeighted;
	/**
	 * 
	 */
	private ArrayList<Vertex<T>> vertices;
	/**
	 * 
	 */
	private HashMap<T, Vertex<T>> hashVertex;
	/**
	 * 
	 */
	private String paths;
	/**
	 * 
	 */
	private int counter;
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
	 * 
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
			hashVertex.get(dataV1).addTriple(""+counter, -1, hashVertex.get(dataV2));
			counter++;
		}else{
			hashVertex.get(dataV1).addTriple(""+counter, -1, hashVertex.get(dataV2));
			counter++;
			hashVertex.get(dataV2).addTriple(""+counter, -1, hashVertex.get(dataV1));
			counter++;
		}
	}
	
	@Override
	/**
	 * 
	 */
	public void addEdge(T dataV1, T dataV2, double weight, String name) {
		if(!hashVertex.containsKey(dataV1)){
			addVertex(dataV1);
		}
		if(!hashVertex.containsKey(dataV2)){
			addVertex(dataV2);
		}
		if(isDirected){
			hashVertex.get(dataV1).addTriple(name, weight, hashVertex.get(dataV2));
		}else{
			hashVertex.get(dataV1).addTriple(name, weight, hashVertex.get(dataV2));
			hashVertex.get(dataV2).addTriple(name, weight, hashVertex.get(dataV1));
		}
	}

	@Override
	/**
	 * 
	 */
	public void removeVertex(T data)  {
		Vertex<T> vertex = hashVertex.get(data);
		if(vertex == null){
			// lanzar excepción
		}else {
			for(int i = 0; i < vertices.size(); i++){
	//			if(Vertexs.get(i).getHashTernas().containsKey(data)) {
	//				Vertexs.get(i).getTriples().remove(Vertexs.get(i).getTriples().indexOf(Vertexs.get(i).getHashTernas().get(data)));
	//				Vertexs.get(i).getVertexs().remove(Vertexs.get(i).getVertexs().indexOf(data));
	//				Vertexs.get(i).getHashTernas().remove(data);
	//			}
				for(int j = 0; j < vertices.get(i).getTriples().size(); j++){
					if(vertices.get(i).getTriples().get(j).getVertex().equals(vertex)){
						vertices.get(i).getTriples().remove(j);
						vertices.get(i).getHashTriples().remove(vertices.get(i).getTriples().get(j).getName());
						vertices.get(i).getVertices().remove(j);
						j--;
					}
				}
			}
			hashVertex.remove(data);
			vertices.remove(vertex);
		}
	}
	
	/**
	 * 
	 */
	public void removeEdge(T dataV1, T dataV2)  {
		Vertex<T> VertexV1 = searchVertex(dataV1);
		for(int i = 0; i < VertexV1.getTriples().size(); i++){
			if(VertexV1.getTriples().get(i).getVertex().getdata().equals(dataV2)){
				VertexV1.getTriples().remove(i);
				VertexV1.getVertices().remove(i);
				i--;
			}
		}
		if(!isDirected){
			Vertex<T> VertexV2 = searchVertex(dataV2);
			for(int i = 0; i < VertexV2.getTriples().size(); i++){
				if(VertexV2.getTriples().get(i).getVertex().getdata().equals(dataV1)){
					VertexV2.getTriples().remove(i);
					VertexV2.getVertices().remove(i);
					i--;
				}
			}
		}
	}

	/**
	 * 
	 * @param dataV1
	 * @param dataV2
	 * @param name
	 */
	public void eliminarArista(T dataV1, T dataV2, String name)   {
		Vertex<T> VertexV1 = searchVertex(dataV1);
		VertexV1.getVertices().remove(VertexV1.getHashTriples().get(name).getVertex());
		VertexV1.getTriples().remove(VertexV1.getHashTriples().get(name));
		VertexV1.getHashTriples().remove(name);
		if(!isDirected){
			Vertex<T> VertexV2 = searchVertex(dataV2);
			VertexV2.getVertices().remove(VertexV2.getHashTriples().get(name).getVertex());
			VertexV2.getTriples().remove(VertexV2.getHashTriples().get(name));
			VertexV2.getHashTriples().remove(name);
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

	@SuppressWarnings("unchecked")
	private Vertex<T>[] dijkstra(Vertex<T> a) {

		boolean[] visited = new boolean[vertices.size()];
		int[] distance = new int[vertices.size()];
		Vertex<T>[] parents = new Vertex[vertices.size()];
		
		for (int i = 0; i < vertices.size(); i++) {
			distance[i] = Integer.MAX_VALUE;
			parents[i] = null;
		}
		
		Queue<Vertex<T>> queue = new ArrayDeque<>();

		int j = vertices.indexOf(a);
		distance[j] = 0;
		queue.add(a);

		while (!queue.isEmpty()) {
			Vertex<T> u = queue.poll();
			int k = vertices.indexOf(u);

			if (!visited[k]) {
				visited[k] = true;
				for (int i = 0; i < vertices.size(); i++) {
					if (vertices.get(k).getVertices().contains(vertices.get(i))){
						int w = (int) vertices.get(k).getTriples().get(vertices.get(k).getVertices().indexOf(vertices.get(i))).getWeight();
						if (!visited[i]) {
							relax(k, i, w, distance, queue, parents);
						}
					}
				}
			}
		}
		return parents;
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
	private void relax (int current, int adjacent, int weight, int []distance, Queue<Vertex<T>> queue, Vertex<T>[] parents) {
		
		if (distance[current] + weight < distance[adjacent]) {
			distance[adjacent] = distance[current] +weight;
			parents[adjacent] = vertices.get(current);
			queue.add(vertices.get(adjacent));
		}
		
	}
	/**
	 * 
	 */
	public void pathByDijkstra (T start, T end)  {
		int goal = vertices.indexOf(searchVertex(end));
		
		paths = "";
		Vertex<T> Vertexstart = searchVertex(start);
		
		Vertex<T>[] parents = dijkstra(Vertexstart);
		
		if (parents[vertices.indexOf(vertices.get((int) end))] != null) {
			pathByDijkstra(start, parents[vertices.indexOf(vertices.get((int) end))].getdata());
		}
		paths += vertices.get(goal).getdata()+",";
	}
	//----------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
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
	}
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
		paths = noRepetitions(floydWarshall()[i][j]);
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
