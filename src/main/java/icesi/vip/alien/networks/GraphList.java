package icesi.vip.alien.networks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

public class GraphList<T> implements GraphListInterface<T> {
	
	private boolean esDirigido;
	private boolean esPonderado;
	private ArrayList<Vertex<T>> vertices;
	private HashMap<T, Vertex<T>> hashVertexs;
	private String caminos;
	
	private int contador;
	
	private int time;
	
	public GraphList(boolean esDirigido, boolean esPonderado){
		this.esDirigido = esDirigido;
		this.esPonderado = esPonderado;
		vertices = new ArrayList<Vertex<T>>();
		hashVertexs = new HashMap<T, Vertex<T>>();
	}
	
	public ArrayList<Vertex<T>> getVertices(){
		return vertices;
	}

	public String getCaminos() {
		return caminos;
	}

	public void setCaminos(String caminos) {
		this.caminos = caminos;
	}

	@Override
	public void agregarVertex(T dato) {
		if(!hashVertexs.containsKey(dato)){
			Vertex<T> agregar = new Vertex<T>(dato);
			vertices.add(agregar);
			hashVertexs.put(dato, agregar);
		}
	}

	public void agregarArista(T datoV1, T datoV2) {
		if(!hashVertexs.containsKey(datoV1)){
			agregarVertex(datoV1);
		}
		if(!hashVertexs.containsKey(datoV2)){
			agregarVertex(datoV2);
		}
		if(esDirigido){
			hashVertexs.get(datoV1).agregarTerna(""+contador, -1, hashVertexs.get(datoV2));
			contador++;
		}else{
			hashVertexs.get(datoV1).agregarTerna(""+contador, -1, hashVertexs.get(datoV2));
			contador++;
			hashVertexs.get(datoV2).agregarTerna(""+contador, -1, hashVertexs.get(datoV1));
			contador++;
		}
	}

	@Override
	public void agregarArista(T datoV1, T datoV2, double peso, String nombre) {
		if(!hashVertexs.containsKey(datoV1)){
			agregarVertex(datoV1);
		}
		if(!hashVertexs.containsKey(datoV2)){
			agregarVertex(datoV2);
		}
		if(esDirigido){
			hashVertexs.get(datoV1).agregarTerna(nombre, peso, hashVertexs.get(datoV2));
		}else{
			hashVertexs.get(datoV1).agregarTerna(nombre, peso, hashVertexs.get(datoV2));
			hashVertexs.get(datoV2).agregarTerna(nombre, peso, hashVertexs.get(datoV1));
		}
	}

	@Override
	public void eliminarVertex(T dato)  {
		Vertex<T> Vertex = hashVertexs.get(dato);
		if(Vertex == null){
			// lanzar excepción
		}else {
			for(int i = 0; i < vertices.size(); i++){
	//			if(Vertexs.get(i).getHashTernas().containsKey(dato)) {
	//				Vertexs.get(i).getTernas().remove(Vertexs.get(i).getTernas().indexOf(Vertexs.get(i).getHashTernas().get(dato)));
	//				Vertexs.get(i).getVertexs().remove(Vertexs.get(i).getVertexs().indexOf(dato));
	//				Vertexs.get(i).getHashTernas().remove(dato);
	//			}
				for(int j = 0; j < vertices.get(i).getTernas().size(); j++){
					if(vertices.get(i).getTernas().get(j).getVertex().equals(Vertex)){
						vertices.get(i).getTernas().remove(j);
						vertices.get(i).getHashTernas().remove(vertices.get(i).getTernas().get(j).getNombre());
						vertices.get(i).getVertices().remove(j);
						j--;
					}
				}
			}
			hashVertexs.remove(dato);
			vertices.remove(Vertex);
		}
	}
	
	public void eliminarArista(T datoV1, T datoV2)  {
		Vertex<T> VertexV1 = buscarVertex(datoV1);
		for(int i = 0; i < VertexV1.getTernas().size(); i++){
			if(VertexV1.getTernas().get(i).getVertex().getDato().equals(datoV2)){
				VertexV1.getTernas().remove(i);
				VertexV1.getVertices().remove(i);
				i--;
			}
		}
		if(!esDirigido){
			Vertex<T> VertexV2 = buscarVertex(datoV2);
			for(int i = 0; i < VertexV2.getTernas().size(); i++){
				if(VertexV2.getTernas().get(i).getVertex().getDato().equals(datoV1)){
					VertexV2.getTernas().remove(i);
					VertexV2.getVertices().remove(i);
					i--;
				}
			}
		}
	}

	public void eliminarArista(T datoV1, T datoV2, String nombre)   {
		Vertex<T> VertexV1 = buscarVertex(datoV1);
		VertexV1.getVertices().remove(VertexV1.getHashTernas().get(nombre).getVertex());
		VertexV1.getTernas().remove(VertexV1.getHashTernas().get(nombre));
		VertexV1.getHashTernas().remove(nombre);
		if(!esDirigido){
			Vertex<T> VertexV2 = buscarVertex(datoV2);
			VertexV2.getVertices().remove(VertexV2.getHashTernas().get(nombre).getVertex());
			VertexV2.getTernas().remove(VertexV2.getHashTernas().get(nombre));
			VertexV2.getHashTernas().remove(nombre);
		}
	}

	@Override
	public Vertex<T> buscarVertex(T dato)  {
		Vertex<T> Vertex = hashVertexs.get(dato);
		if(Vertex == null){
			//throw new (dato);
			return null;
		}else {
			return Vertex;
		}
	}
	
	public void BFS(T dato) {
		Vertex<T> s = buscarVertex(dato);
		BFS(s);
	}
	
	private void BFS(Vertex<T> s){
		for(int i = 0; i < vertices.size(); i++){
			if(!vertices.get(i).equals(s)){
				vertices.get(i).setColor(Vertex.BLANCO);
				vertices.get(i).setDistancia(Integer.MAX_VALUE);
				vertices.get(i).setPredecesor(null);
			}
		}
		s.setColor(Vertex.GRIS);
		s.setDistancia(0);
		s.setPredecesor(null);
		Queue<Vertex<T>> cola = new ArrayDeque<Vertex<T>>();
		cola.offer(s);
		while(!cola.isEmpty()){
			Vertex<T> u = cola.poll();
			for(int i = 0; i < u.getTernas().size(); i++){
				Vertex<T> v = u.getTernas().get(i).getVertex();
				if(v.getColor().equals(Vertex.BLANCO)){
					v.setColor(Vertex.GRIS);
					v.setDistancia(u.getTernas().get(i).getVertex().getDistancia() + 1);
					v.setPredecesor(u);
					cola.offer(u.getTernas().get(i).getVertex());
				}
			}
			u.setColor(Vertex.NEGRO);
			//Solo añadir "u" a arraylist
		}
	}
	
	//--------------------------- DFS con Pila -------------------------------------
	
	public void DFS(T dato)  {
		Vertex<T> s = buscarVertex(dato);
		DFS(s);
	}
	
	private void DFS(Vertex<T> s){
		for(int i = 0; i < vertices.size(); i++){
			if(!vertices.get(i).equals(s)){
				vertices.get(i).setColor(Vertex.BLANCO);
				vertices.get(i).setDistancia(Integer.MAX_VALUE);
				vertices.get(i).setPredecesor(null);
			}
		}
		s.setColor(Vertex.GRIS);
		s.setDistancia(0);
		s.setPredecesor(null);
		Stack<Vertex<T>> cola = new Stack<Vertex<T>>();
		cola.push(s);
		while(!cola.isEmpty()){
			Vertex<T> u = cola.pop();
			for(int i = 0; i < u.getTernas().size(); i++){
				Vertex<T> v = u.getTernas().get(i).getVertex();
				if(v.getColor().equals(Vertex.BLANCO)){
					v.setColor(Vertex.GRIS);
					v.setDistancia(u.getTernas().get(i).getVertex().getDistancia() + 1);
					v.setPredecesor(u);
					cola.push(u.getTernas().get(i).getVertex());
				}
			}
			u.setColor(Vertex.NEGRO);
			//Solo añadir "u" a arraylist
		}
	}
	
	//----------------------- Fin DFS con Pila -------------------------------------
	
	//--------------------------- DFS sin Pila -------------------------------------
	
	public void DFS(){
		for(int i = 0; i < vertices.size(); i++){
			vertices.get(i).setColor(Vertex.BLANCO);
			vertices.get(i).setPredecesor(null);
		}
		time = 0;
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).getColor().equals(Vertex.BLANCO)){
				visitaDFS(vertices.get(i));
			}
		}
	}
	
	public void visitaDFS(Vertex<T> u){
		time = time + 1;
		u.setDescubierto(time);
		u.setColor(Vertex.GRIS);
		for(int i = 0; i < u.getTernas().size(); i++){
			if(u.getTernas().get(i).getVertex().getColor().equals(Vertex.BLANCO)){
				u.getTernas().get(i).getVertex().setPredecesor(u);
				visitaDFS(u.getTernas().get(i).getVertex());
			}
		}
		u.setColor(Vertex.NEGRO);
		time = time + 1;
		u.setFinalizado(time);
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
						double peso = u.getTernas().get(j).getPeso();
						if(L[Vertexs.indexOf(u)] + peso < L[Vertexs.indexOf(Vertexs.get(i))]){
							L[Vertexs.indexOf(Vertexs.get(i))] = L[Vertexs.indexOf(u)] + peso;
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

		boolean[] visitado = new boolean[vertices.size()];
		int[] distancia = new int[vertices.size()];
		Vertex<T>[] previo = new Vertex[vertices.size()];
		
		for (int i = 0; i < vertices.size(); i++) {
			distancia[i] = Integer.MAX_VALUE;
			previo[i] = null;
		}
		
		Queue<Vertex<T>> cola = new ArrayDeque<>();

		int j = vertices.indexOf(a);
		distancia[j] = 0;
		cola.add(a);

		while (!cola.isEmpty()) {
			Vertex<T> u = cola.poll();
			int k = vertices.indexOf(u);

			if (!visitado[k]) {
				visitado[k] = true;
				for (int i = 0; i < vertices.size(); i++) {
					if (vertices.get(k).getVertices().contains(vertices.get(i))){
						int w = (int) vertices.get(k).getTernas().get(vertices.get(k).getVertices().indexOf(vertices.get(i))).getPeso();
						if (!visitado[i]) {
							relajacion(k, i, w, distancia, cola, previo);
						}
					}
				}
			}
		}
		return previo;
	}

	private void relajacion (int actual, int adyacente, int peso, int []distancia, Queue<Vertex<T>> cola, Vertex<T>[] previo) {
		
		if (distancia[actual] + peso < distancia[adyacente]) {
			distancia[adyacente] = distancia[actual] +peso;
			previo[adyacente] = vertices.get(actual);
			cola.add(vertices.get(adyacente));
		}
		
	}
	
	public void caminoPorDijkstra (T inicio, T destino)  {
		int fin = vertices.indexOf(buscarVertex(destino));
		
		caminos = "";
		Vertex<T> VertexInicio = buscarVertex(inicio);
		
		Vertex<T>[] previo = dijkstra(VertexInicio);
		
		if (previo[vertices.indexOf(vertices.get(fin))] != null) {
			caminoPorDijkstra(inicio, previo[vertices.indexOf(vertices.get(fin))].getDato());
		}
		caminos += vertices.get(fin).getDato()+",";
	}
	//----------------------------------------------------------------------
	
	public String[][] floydWarshall(){
		
		double[][] dist = new double[vertices.size()][vertices.size()];
		String[][] retorno = new String[vertices.size()][vertices.size()];
				
		for(int i = 0; i < dist.length; i++){
			for(int j = 0; j < dist[0].length; j++){
				if(j == i){
					dist[i][j] = 0;
					retorno[i][j] = vertices.get(i).getDato() + "";
				}else if(vertices.get(i).getVertices().contains(vertices.get(j))){
					dist[i][j] = vertices.get(i).getTernas().get(vertices.get(i).getVertices().indexOf(vertices.get(j))).getPeso();
					retorno[i][j] = vertices.get(i).getDato() + "," + vertices.get(j).getDato();
				}
				else{
					dist[i][j] = Double.MAX_VALUE;
					retorno[i][j] = "";
				}
			}
		}
		for(int k = 0; k < dist.length; k++){
			for(int i = 0; i < dist[0].length; i++){
				for(int j = 0; j < dist[0].length; j++){
					if(dist[i][j] > dist[i][k] + dist[k][j]){
						dist[i][j] = dist[i][k] + dist[k][j];
						retorno[i][j] = retorno[i][k].substring(0, retorno[i][k].length()) + "," + retorno[k][j];
					}					
				}
			}
		}
		return retorno;
	}
	
	private String sinRepetir(String arr){

		String[] arreglo = arr.split(",");

		for (int i = 0; i < arreglo.length; i++) {
			for (int j = 0; j < arreglo.length; j++) {
				if (i != j) {
					if (arreglo[i] != null && arreglo[j] != null) {
						if (arreglo[i].equals(arreglo[j])) {
							arreglo[j] = null;
						}
					}
				}
			}
		}

		for (int i = 0; i < arreglo.length; i++) {
			if (arreglo[i] != null) {
				caminos += arreglo[i] + ",";
			}
		}
		return caminos;
	}
	
	public void caminoPorFloyd(T inicio, T destino) {
		int i = vertices.indexOf(buscarVertex(inicio));
		int j = vertices.indexOf(buscarVertex(destino));;
		caminos = "";
		caminos = sinRepetir(floydWarshall()[i][j]);
	}

	public void imprimirCamino(Vertex<T> s, Vertex<T> v){
		if(s == v){
			System.out.println(s.getDato());
		}else if(v.getPredecesor() == null){
			System.out.println("No hay camino de " + s.getDato() + " hasta " + v.getDato());
		}else{
			imprimirCamino(s, v.getPredecesor());
			System.out.println(v.getDato());
		}
	}

	
	
}
