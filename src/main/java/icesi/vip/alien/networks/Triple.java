package icesi.vip.alien.networks;

public class Triple<T> {
	
	private double weight;
	private String name;
	private Vertex<T> vertex;
	
	public Triple(String name, double weight, Vertex<T> vertex){
		this.name = name;
		this.weight = weight;
		this.vertex = vertex;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vertex<T> getVertex() {
		return vertex;
	}

	public void setVertex(Vertex<T> vertex) {
		this.vertex = vertex;
	}
}
