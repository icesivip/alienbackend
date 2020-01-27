package icesi.vip.alien.alien.craftMethod.auxiliary;

public class Point {
	public double i;
	public double j;
	
	public Point(double i, double j) {
		this.i=i;
		this.j=j;
	}
	
	
	public static double EuclidianDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.i-p2.i, 2)+Math.pow(p1.j-p2.j, 2));
	}
	
	
	public static double RectilinearDistance(Point p1, Point p2) {
		return Math.abs(p1.i-p2.i)+Math.abs(p1.j-p2.j);
	}
	
	
}
