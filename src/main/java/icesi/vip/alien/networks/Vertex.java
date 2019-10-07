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

import java.util.ArrayList;
import java.util.HashMap;
/**
 *  This class represents the vertex of the graph
 * @param <T>
 */
public class Vertex<T> implements Comparable<Vertex<T>> {
	// -----------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------
	/**
	 * 
	 */
	public static final String GREY = "Grey";
	/**
	 * 
	 */
	public static final String WHITE = "White";
	/**
	 * 
	 */
	public static final String BLACK = "Black";
	// -----------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------
	private String color;
	/**
	 * 
	 */
	private int distance;
	/**
	 * 
	 */
	private int discovered;
	/**
	 * 
	 */
	private int ended;
	/**
	 * 
	 */
	private Vertex<T> predecessor;
	/**
	 * 
	 */
	private T data;
	/**
	 * 
	 */
	private ArrayList<Triple<T>> triples;
	/**
	 * 
	 */
	
	
	// -----------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------
	/**
	 * 
	 * @param data
	 */
	public Vertex(T data){
		this.data = data;
		triples = new ArrayList<Triple<T>>();
		distance = Integer.MAX_VALUE;
	}
	// -----------------------------------------------------------------
    // Methods for add ...
    // -----------------------------------------------------------------
	/**
	 * 
	 * @param name
	 * @param weight
	 * @param vertex
	 */
	public void addTriple(int weight, Vertex<T> vertex){
		Triple<T> triple = new Triple<T>(weight, vertex);
//		if(!(hashTriples.containsKey(nombre))){
			triples.add(triple);
				
//		}
	}
	
	// -----------------------------------------------------------------
    // Methods Atributes
    // -----------------------------------------------------------------
	
	
	@Override
	public int compareTo(Vertex<T> other) {
		
		return this.distance-other.distance;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getdata(){
		return data;
	}
	/**
	 * 
	 * @param data
	 */
	public void setdata(T data){
		this.data = data;
	}
	/**
	 * 
	 * @return
	 */
	public int getDiscovered() {
		return discovered;
	}
	/**
	 * 
	 * @param discovered
	 */
	public void setDiscovered(int discovered) {
		this.discovered = discovered;
	}
	/**
	 * 
	 * @return
	 */
	public int getEnded() {
		return ended;
	}
	/**
	 * 
	 * @param ended
	 */
	public void setEnded(int ended) {
		this.ended = ended;
	}
	/**
	 * 
	 * @return
	 */
	public String getColor() {
		return color;
	}
	/**
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * 
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * 
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	/**
	 * 
	 * @return
	 */
	public Vertex<T> getPredecessor() {
		return predecessor;
	}
	/**
	 * 
	 * @param predecessor
	 */
	public void setPredecessor(Vertex<T> predecessor) {
		this.predecessor = predecessor;
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Triple<T>> getTriples() {
		return triples;
	}
	/**
	 * 
	 * @param triples
	 */
	public void setTriples(ArrayList<Triple<T>> triples) {
		this.triples = triples;
	}
	
	
}
