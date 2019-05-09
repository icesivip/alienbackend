package icesi.vip.alien.alien.ruteo;

import java.util.ArrayList;
import java.util.Stack;

public class Controladora {
	
	ArrayList<Nodo> nodos;
	ArrayList<Tour> tours;
	Nodo origen;
	
	public Controladora() {
		nodos=new ArrayList<>();
		tours=new ArrayList<>();
		origen=null;
	}
	
	public void createTours() {
		for(int i=1;i<nodos.size();i++) {
			Tour nuevo=new Tour();
			ArrayList<Nodo> arr=new ArrayList<>();
			arr.add(nodos.get(i));
			nuevo.setNodos(arr);
			tours.add(nuevo);
		}
		joinTours();
	}
	
	public ArrayList<Nodo> reverseArrayList(ArrayList<Nodo> a){
		Stack<Nodo> b=new Stack<>();
		for(int i=0;i<a.size();i++) {
			b.push(a.get(i));
		}
		a.clear();
		for(int i=0;!b.isEmpty();i++) {
			a.add(i, b.pop());
		}
		return a;
	}
	
	public void joinTours() {
		Trifuerza<Double, Integer, Integer> s1=saveMethodFF(getTours());
		Trifuerza<Double, Integer, Integer> s2=saveMethodFL(getTours());
		Trifuerza<Double, Integer, Integer> s3=saveMethodLF(getTours());
		Trifuerza<Double, Integer, Integer> s4=saveMethodLL(getTours());
		double gS=greaterSave(s1.getAhorro(), s2.getAhorro(), s3.getAhorro(), s4.getAhorro());
		if(gS==s1.getAhorro()) {
			joinToursFF(s1.getPrimerNodo(), s1.getSegundoNodo());
		}else if(gS==s2.getAhorro()) {
			joinToursFF(s2.getPrimerNodo(), s2.getSegundoNodo());
		}else if(gS==s3.getAhorro()) {
			joinToursFF(s3.getPrimerNodo(), s3.getSegundoNodo());
		}else if(gS==s4.getAhorro()) {
			joinToursFF(s4.getPrimerNodo(), s4.getSegundoNodo());
		}
		if(getTours().size()>1) {
			joinTours();
		}else {
			endGame();
		}
	}
	
	public void endGame() {
		ArrayList<Nodo> finalTour=new ArrayList<>();
		finalTour.add(origen);
		for(int i=0;i<getTours().get(0).getNodos().size();i++) {
			finalTour.add(getTours().get(0).getNodos().get(i));
		}
		finalTour.add(origen);
		getTours().get(0).setNodos(finalTour);
		String aja="";
		for(int i=0;i<getTours().get(0).getNodos().size();i++) {
			aja+="("+getTours().get(0).getNodos().get(i).toString()+") ";
		}
		System.out.println(aja);
	}
	
	public void cleanTaken(ArrayList<Tour> a) {
		for(int i=0;i<a.size();i++) {
			a.get(i).setTaken(false);
		}
	}
	
	public void joinToursFF(int x, int y) {
		ArrayList<Tour> nuevosTours=new ArrayList<>();
		ArrayList<Nodo> tourUnido=new ArrayList<>();
		getTours().get(x).setNodos((reverseArrayList(getTours().get(x).getNodos())));
		for(int i=0;i<getTours().get(x).getNodos().size();i++) {
			tourUnido.add(getTours().get(x).getNodos().get(i));
		}
		int c=0;
		for(int i=0;i<getTours().get(y).getNodos().size();i++) {
			tourUnido.add(getTours().get(y).getNodos().get(c));
			c++;
		}
		String aja="";
		for(int i=0;i<tourUnido.size();i++) {
			aja+=tourUnido.get(i).toString()+" ";
		}
		System.out.println(aja);
		Tour tU=new Tour();
		tU.setNodos(tourUnido);
		for(int i=0;i<getTours().size();i++) {
			if(!getTours().get(i).isTaken()) {
				if(i==x) {
					nuevosTours.add(tU);
					getTours().get(x).setTaken(true);
					getTours().get(y).setTaken(true);
				}else {
					nuevosTours.add(getTours().get(i));
					getTours().get(i).setTaken(true);
				}
			}
		}
		cleanTaken(nuevosTours);
		setTours(nuevosTours);
	}
	
	public void joinToursFL(int x, int y) {
		ArrayList<Tour> nuevosTours=new ArrayList<>();
		ArrayList<Nodo> tourUnido=new ArrayList<>();
		getTours().get(x).setNodos((reverseArrayList(getTours().get(x).getNodos())));
		getTours().get(y).setNodos((reverseArrayList(getTours().get(y).getNodos())));
		for(int i=0;i<getTours().get(x).getNodos().size();i++) {
			tourUnido.add(getTours().get(x).getNodos().get(i));
		}
		int c=0;
		for(int i=0;i<getTours().get(y).getNodos().size();i++) {
			tourUnido.add(getTours().get(y).getNodos().get(c));
			c++;
		}
		Tour tU=new Tour();
		tU.setNodos(tourUnido);
		for(int i=0;i<getTours().size();i++) {
			if(!getTours().get(i).isTaken()) {
				if(i==x) {
					nuevosTours.add(tU);
					getTours().get(x).setTaken(true);
					getTours().get(y).setTaken(true);
				}else {
					nuevosTours.add(getTours().get(i));
					getTours().get(i).setTaken(true);
				}
			}
		}
		cleanTaken(nuevosTours);
		setTours(nuevosTours);
	}

	public void joinToursLL(int x, int y) {
		ArrayList<Tour> nuevosTours=new ArrayList<>();
		ArrayList<Nodo> tourUnido=new ArrayList<>();
		getTours().get(y).setNodos((reverseArrayList(getTours().get(y).getNodos())));
		for(int i=0;i<getTours().get(x).getNodos().size();i++) {
			tourUnido.add(getTours().get(x).getNodos().get(i));
		}
		int c=0;
		for(int i=0;i<getTours().get(y).getNodos().size();i++) {
			tourUnido.add(getTours().get(y).getNodos().get(c));
			c++;
		}
		Tour tU=new Tour();
		tU.setNodos(tourUnido);
		for(int i=0;i<getTours().size();i++) {
			if(!getTours().get(i).isTaken()) {
				if(i==x) {
					nuevosTours.add(tU);
					getTours().get(x).setTaken(true);
					getTours().get(y).setTaken(true);
				}else {
					nuevosTours.add(getTours().get(i));
					getTours().get(i).setTaken(true);
				}
			}
		}
		cleanTaken(nuevosTours);
		setTours(nuevosTours);
	}

	public void joinToursLF(int x, int y) {
		ArrayList<Tour> nuevosTours=new ArrayList<>();
		ArrayList<Nodo> tourUnido=new ArrayList<>();
		for(int i=0;i<getTours().get(x).getNodos().size();i++) {
			tourUnido.add(getTours().get(x).getNodos().get(i));
		}
		int c=0;
		for(int i=0;i<getTours().get(y).getNodos().size();i++) {
			tourUnido.add(getTours().get(y).getNodos().get(c));
			c++;
		}
		Tour tU=new Tour();
		tU.setNodos(tourUnido);
		for(int i=0;i<getTours().size();i++) {
			if(!getTours().get(i).isTaken()) {
				if(i==x) {
					nuevosTours.add(tU);
					getTours().get(x).setTaken(true);
					getTours().get(y).setTaken(true);
				}else {
					nuevosTours.add(getTours().get(i));
					getTours().get(i).setTaken(true);
				}
			}
		}
		cleanTaken(nuevosTours);
		setTours(nuevosTours);
	}
	
	public double greaterSave(double a, double b, double c, double d) {
		double[] saves= {a, b, c, d};
		double mayor=0;
		for(int i=0;i<saves.length;i++) {
			if(saves[i]>mayor) {
				mayor=saves[i];
			}
		}
		return mayor;
	}
	
	/**
	 * Primero, Primero
	 * @param a
	 * @return
	 */
	public Trifuerza<Double, Integer, Integer> saveMethodFF(ArrayList<Tour> a) {
		Nodo f1, f2=null;
		int c1=0;
		int c2=0;
		double maxSave=Double.MAX_VALUE;
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<a.size();j++) {
				if(i!=j) {
					f1=a.get(i).getNodos().get(0);
					f2=a.get(j).getNodos().get(0);
					double distancia1=distances(f1, origen);
					double distancia2=distances(f2, origen);
					double distancia3=distances(f1, f2);
					double actualSave=distancia1+distancia2-distancia3;
					if(actualSave<maxSave) {
						maxSave=actualSave;
						c1=i;
						c2=j;
					}
				}
			}
		}
		return new Trifuerza<>(maxSave, c1, c2);
	}
	
	/**
	 * Primero - Ultimo
	 * First - Last
	 * @param a arreglo de tours a analizar
	 * @return un string con la informacion de los tours a unir
	 */
	public Trifuerza<Double, Integer, Integer> saveMethodFL(ArrayList<Tour> a) {
		Nodo f1, f2=null;
		int c1=0;
		int c2=0;
		double maxSave=Double.MAX_VALUE;
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<a.size();j++) {
				if(i!=j) {
					f1=a.get(i).getNodos().get(0);
					f2=a.get(j).getNodos().get(a.get(j).getNodos().size()-1);
					double d1=distances(f1, origen);
					double d2=distances(f2, origen);
					double d3=distances(f1, f2);
					double actualSave=(d1+d2)-d3;
					if(actualSave<maxSave) {
						maxSave=actualSave;
						c1=i;
						c2=j;
					}
				}
			}
		}
		return new Trifuerza<>(maxSave, c1, c2);
	}
	
	/**
	 * Ultimo Primero
	 * Last First
	 * @param a arreglo de tours a analizar
	 * @return un string con la informacion de los tours a unir
	 */
	public Trifuerza<Double, Integer, Integer> saveMethodLF(ArrayList<Tour> a) {
		Nodo f1, f2=null;
		int c1=0;
		int c2=0;
		double maxSave=Double.MAX_VALUE;
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<a.size();j++) {
				if(i!=j) {
					f1=a.get(i).getNodos().get(a.get(i).getNodos().size()-1);
					f2=a.get(j).getNodos().get(0);
					double distancia1=distances(f1, origen);
					double distancia2=distances(f2, origen);
					double distancia3=distances(f1, f2);
					double actualSave=distancia1+distancia2-distancia3;
					if(actualSave<maxSave) {
						maxSave=actualSave;
						c1=i;
						c2=j;
					}
				}
			}
		}
		return new Trifuerza<>(maxSave, c1, c2);				
	}

	/**
	 * Ultimo - Ultimo
	 * Last - Last
	 * @param a arreglo de tours a analizar
	 * @return un string con la informacion de los tours a unir
	 */
	public Trifuerza<Double, Integer, Integer> saveMethodLL(ArrayList<Tour> a) {
		Nodo f1, f2=null;
		int c1=0;
		int c2=0;
		double maxSave=Double.MAX_VALUE;
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<a.size();j++) {
				if(i!=j) {
					f1=a.get(i).getNodos().get(a.get(i).getNodos().size()-1);
					f2=a.get(j).getNodos().get(a.get(j).getNodos().size()-1);
					double distancia1=distances(f1, origen);
					double distancia2=distances(f2, origen);
					double distancia3=distances(f1, f2);
					double actualSave=distancia1+distancia2-distancia3;
					if(actualSave<maxSave) {
						maxSave=actualSave;
						c1=i;
						c2=j;
					}
				}
			}
		}
		return new Trifuerza<>(maxSave, c1, c2);
	}
	
	public double distances(Nodo n0, Nodo n1) {
		double dX=n1.getPosX()-n0.getPosX();
		double dY=n1.getPosY()-n0.getPosY();;
		return Math.sqrt( (Math.pow(dX,2)) + (Math.pow(dY,2)) );
	}
	
	/**
	 * @return the ns1
	 */
	public ArrayList<Nodo> getNodos() {
		return nodos;
	}

	/**
	 * @param ns1 the ns1 to set
	 */
	public void setNodos(ArrayList<Nodo> nodos) {
		this.nodos=nodos;
		origen=nodos.get(0);
		createTours();
	}

	/**
	 * @return the ts1
	 */
	public ArrayList<Tour> getTours() {
		return tours;
	}

	/**
	 * @param ts1 the ts1 to set
	 */
	public void setTours(ArrayList<Tour> tours) {
		this.tours = tours;
	}

	public Nodo getOrigen() {
		return origen;
	}
	
	/*public static void main(String[] args) {
		Controladora m=new Controladora();
		ArrayList<Nodo> nodos=new ArrayList<>();
		nodos.add(new Nodo(10,2));
		nodos.add(new Nodo(1,5));
		nodos.add(new Nodo(4,6));
		nodos.add(new Nodo(3,9));
		nodos.add(new Nodo(0,7));
		nodos.add(new Nodo(4,4));
		nodos.add(new Nodo(11,3));
		nodos.add(new Nodo(12,6));
		nodos.add(new Nodo(1,9));
		nodos.add(new Nodo(12,15));
		nodos.add(new Nodo(1,21));
		nodos.add(new Nodo(21,13));
		nodos.add(new Nodo(8,2));
		nodos.add(new Nodo(32,4));
		nodos.add(new Nodo(6,18));
		m.setNodos(nodos);
	}*/
	
}