package icesi.vip.alien.networks;

public class Edge<Integer> implements Comparable<Edge<Integer>>{

	private double weight;
	private int id;
	private Edge residual;
    private long flow, cost;
    private long capacity, originalCost;
	private Vertex<Integer> source;
	private Vertex<Integer> destination;

	public Edge(Vertex<Integer> source, Vertex<Integer> destination) {
		this(source, destination, 1D);
	}
	
	public Edge(Vertex<Integer> source, Vertex<Integer> destination, double weight) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public Edge(Vertex<Integer> source, Vertex<Integer> destination, long capacity) {
		this.source = source;
		this.destination = destination;
		this.capacity = capacity;
	}

	public Edge(Vertex<Integer> source, Vertex<Integer> destination, long capacity, long cost) {
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
	
	public Vertex<Integer> getSource() {
		return source;
	}

	public Vertex<Integer> getDestination() {
		return destination;
	}

	@Override
	public int compareTo(Edge<Integer> o) {
		return Double.compare(weight, o.weight);
	}

	public int getId() {
		return id;
	}

}	