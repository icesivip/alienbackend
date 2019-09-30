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
/**
 *
 * @param <T>
 */
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
