package icesi.vip.alien.networks;

import java.util.ArrayList;
import java.util.HashMap;

public class Vertex<T> {
	
	public static final String GRIS = "Gris";
	public static final String BLANCO = "Blanco";
	public static final String NEGRO = "Negro";
	
	private String color;
	private int distancia;
	private int descubierto;
	private int finalizado;
	private Vertex<T> predecesor;
	private T dato;
	private ArrayList<Terna<T>> ternas;
	private HashMap<String, Terna<T>> hashTernas;
	
	private ArrayList<Vertex<T>> vertices;
	
	public Vertex(T dato){
		this.dato = dato;
		ternas = new ArrayList<Terna<T>>();
		hashTernas = new HashMap<String, Terna<T>>();
		vertices = new ArrayList<Vertex<T>>();
	}
	
	public void agregarTerna(String nombre, double peso, Vertex<T> vertice){
		Terna<T> terna = new Terna<T>(nombre, peso, vertice);
//		if(!(hashTernas.containsKey(nombre))){
			ternas.add(terna);
			hashTernas.put(nombre, terna);
			vertices.add(vertice);			
//		}
	}
	
	public ArrayList<Vertex<T>> getVertices(){
		return vertices;
	}
	
	public T getDato(){
		return dato;
	}
	
	public void setDato(T dato){
		this.dato = dato;
	}
	
	public int getDescubierto() {
		return descubierto;
	}

	public void setDescubierto(int descubierto) {
		this.descubierto = descubierto;
	}

	public int getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(int finalizado) {
		this.finalizado = finalizado;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public Vertex<T> getPredecesor() {
		return predecesor;
	}

	public void setPredecesor(Vertex<T> predecesor) {
		this.predecesor = predecesor;
	}

	public ArrayList<Terna<T>> getTernas() {
		return ternas;
	}

	public void setTernas(ArrayList<Terna<T>> ternas) {
		this.ternas = ternas;
	}

	public HashMap<String, Terna<T>> getHashTernas() {
		return hashTernas;
	}

	public void setHashTernas(HashMap<String, Terna<T>> hashTernas) {
		this.hashTernas = hashTernas;
	}
}
