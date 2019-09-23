package icesi.vip.alien.networks;

public class Terna<T> {
	
	private double peso;
	private String nombre;
	private Vertex<T> vertex;
	
	public Terna(String nombre, double peso, Vertex<T> vertice){
		this.nombre = nombre;
		this.peso = peso;
		this.vertex = vertice;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Vertex<T> getVertex() {
		return vertex;
	}

	public void setVertex(Vertex<T> vertice) {
		this.vertex = vertice;
	}
}
