package icesi.vip.alien.networks;

import java.util.ArrayList;

public interface GraphListInterface<T> {
	
	public void agregarVertex(T dato);
	
	public void eliminarVertex(T dato);
	
	public void agregarArista(T datoV1, T datoV2, double peso, String nombre);
	
	public Vertex<T> buscarVertex(T dato) ;
	
	public void BFS(T dato) ;
	
	public void DFS(T dato) ;
	
	public void caminoPorDijkstra (T inicio, T destino) ;
	
	public void caminoPorFloyd (T inicio, T destino) ;
	
	public void eliminarArista(T datoV1, T datoV2) ;
	
	public ArrayList<Vertex<T>> getVertices();

}
