package icesi.vip.alien.networks;

public class Vertex<Integer> implements Comparable<Vertex<Integer>>{

	public static final int WHITE = 0;
	public static final int GRAY = 1;
	public static final int BLACK = 2;
	
	private Integer value;
	//Distance
	private double d;
	//IDK
	private int f;
	
	private int index;
	
	private int color;
	
	private Vertex<Integer> pred;
	
	public Vertex(Integer value) {
		this.value=value;
		pred=null;
		color=WHITE;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Vertex<Integer> getPred() {
		return pred;
	}

	public void setPred(Vertex<Integer> pred) {
		this.pred = pred;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index=index;
	}

	@Override
	public int compareTo(Vertex<Integer> vertex) {
		return Double.compare(d, vertex.d);
	}
	
}