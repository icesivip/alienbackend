package icesi.vip.alien.networks;

public class Edge<T extends Integer> implements Comparable<Edge<T >>{

	private double weight;
	private int id;
	private Edge residual;
    private long flow, cost;
    private long capacity, originalCost;
	private Vertex<T> source;
	private Vertex<T> destination;

	public Edge(Vertex<T> source, Vertex<T> destination) {
		this(source, destination, 1D);
	}
	
	public Edge(Vertex<T> source, Vertex<T> destination, double weight) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public Edge(Vertex<T> source, Vertex<T> destination, long capacity) {
		this.source = source;
		this.destination = destination;
		this.capacity = capacity;
	}

	public Edge(Vertex<T> source, Vertex<T> destination, long capacity, long cost) {
		this.source = source;
		this.destination = destination;
		this.capacity = capacity;
		this.originalCost = this.cost = cost;
	}

	public boolean isResidual() {
		return capacity == 0;
	}

	public long remainingCapacity() {
		return capacity - flow;
	}

	public void augment(long bottleNeck) {
		flow += bottleNeck;
		residual.flow -= bottleNeck;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Vertex<T> getSource() {
		return source;
	}

	public Vertex<T> getDestination() {
		return destination;
	}

	@Override
	public int compareTo(Edge<T> o) {
		return Double.compare(weight, o.weight);
	}

	public int getId() {
		return id;
	}

}	