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
/**
 *
 * @param <T>
 */
public class Triple<T> {
	// -----------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------
	/**
	 * 
	 */
	private int weight;
	/**
	 * 
	 */
	/**
	 * 
	 */
	private Vertex<T> vertex;
	
	// -----------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------
	/**
	 * 
	 * @param name
	 * @param weight
	 * @param vertex
	 */
	public Triple(int weight, Vertex<T> vertex){

		this.weight = weight;
		this.vertex = vertex;
	}
	// -----------------------------------------------------------------
    // Methods Atributes
    // -----------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * 
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vertex<T> getVertex() {
		return vertex;
	}
	/**
	 * 
	 * @param vertex
	 */
	public void setVertex(Vertex<T> vertex) {
		this.vertex = vertex;
	}
}
