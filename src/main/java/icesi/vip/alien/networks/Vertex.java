package icesi.vip.alien.networks;

import java.util.ArrayList;
import java.util.HashMap;

public class Vertex<T> {
	
	public static final String GREY = "Grey";
	public static final String WHITE = "White";
	public static final String BLACK = "Black";
	
	private String color;
	private int distance;
	private int discovered;
	private int ended;
	private Vertex<T> predecessor;
	private T data;
	private ArrayList<Triple<T>> triples;
	private HashMap<String, Triple<T>> hashTriples;
	
	private ArrayList<Vertex<T>> vertices;
	
	public Vertex(T data){
		this.data = data;
		triples = new ArrayList<Triple<T>>();
		hashTriples = new HashMap<String, Triple<T>>();
		vertices = new ArrayList<Vertex<T>>();
	}
	
	public void addTriple(String name, double weight, Vertex<T> vertex){
		Triple<T> triple = new Triple<T>(name, weight, vertex);
//		if(!(hashTriples.containsKey(nombre))){
			triples.add(triple);
			hashTriples.put(name, triple);
			vertices.add(vertex);			
//		}
	}
	
	public ArrayList<Vertex<T>> getVertices(){
		return vertices;
	}
	
	public T getdata(){
		return data;
	}
	
	public void setdata(T data){
		this.data = data;
	}
	
	public int getDiscovered() {
		return discovered;
	}

	public void setDiscovered(int discovered) {
		this.discovered = discovered;
	}

	public int getEnded() {
		return ended;
	}

	public void setEnded(int ended) {
		this.ended = ended;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Vertex<T> getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Vertex<T> predecessor) {
		this.predecessor = predecessor;
	}

	public ArrayList<Triple<T>> getTriples() {
		return triples;
	}

	public void setTriples(ArrayList<Triple<T>> triples) {
		this.triples = triples;
	}

	public HashMap<String, Triple<T>> getHashTriples() {
		return hashTriples;
	}

	public void setHashTriples(HashMap<String, Triple<T>> hashTriples) {
		this.hashTriples = hashTriples;
	}
}
