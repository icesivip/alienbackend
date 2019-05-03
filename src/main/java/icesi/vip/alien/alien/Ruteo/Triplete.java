package tours;

public class Triplete <V, PN, SN>{

	private V ahorro;
	private PN primerNodo;
	private SN segundoNodo;
	
	public Triplete(V ahorro, PN primerNodo, SN segundoNodo) {
		this.ahorro = ahorro;
		this.primerNodo = primerNodo;
		this.segundoNodo = segundoNodo;
	}
	
	public V getAhorro() {
		return ahorro;
	}
	public void setAhorro(V ahorro) {
		this.ahorro = ahorro;
	}
	public PN getPrimerNodo() {
		return primerNodo;
	}
	public void setPrimerNodo(PN primerNodo) {
		this.primerNodo = primerNodo;
	}
	public SN getSegundoNodo() {
		return segundoNodo;
	}
	public void setSegundoNodo(SN segundoNodo) {
		this.segundoNodo = segundoNodo;
	}
	
	
	
	
	
}
