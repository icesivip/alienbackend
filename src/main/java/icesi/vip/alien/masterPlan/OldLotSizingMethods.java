package icesi.vip.alien.masterPlan;

import java.util.ArrayList;

public class OldLotSizingMethods {

	// Quiz� se puedan cambiar a enteros que representen la totalidad de cada
	// periodo.
	public static final String ANUAL = "Anual";
	public static final String MENSUAL = "Mensual";
	public static final String SEMANAL = "Semanal";
	public static final String DIARIO = "Diario";
	
	public static final String UNKNOWN = "Unknown";

	// PENSAR EN MEJORES NOMBRE DESPUES
	// Costos asociados
	private double costoArticulo;
	private double costoPreparacion;
	private double costoMantenimiento;
	private String periodicidad;

	private ArrayList<Integer> articulosSemanales;
	private ArrayList<Integer> planPedidos;

	public OldLotSizingMethods(double pCostosArticulo, double pCostoPreparacion, double pCostosMantenimiento,
			ArrayList<Integer> pArticulosSemanes, String pPeriodicidad) {

		costoArticulo = pCostosArticulo;
		costoPreparacion = pCostoPreparacion;
		costoMantenimiento = pCostosMantenimiento;
		articulosSemanales = pArticulosSemanes;
		planPedidos = new ArrayList<Integer>();
		setPeriodicidad(pPeriodicidad);

	}
	
	public OldLotSizingMethods(double pCostosArticulo, double pCostoPreparacion, double pCostosMantenimiento, String pPeriodicidad) {

		costoArticulo = pCostosArticulo;
		costoPreparacion = pCostoPreparacion;
		costoMantenimiento = pCostosMantenimiento;
		articulosSemanales = new ArrayList<Integer>();
		planPedidos = new ArrayList<Integer>();
		setPeriodicidad(pPeriodicidad);

	}
	
	public void agregarPedido(int toAdd) {
		articulosSemanales.add(toAdd);
	}

	public double getCostoArticulo() {
		return costoArticulo;
	}

	public void setCostoArticulo(double costoArticulo) {
		this.costoArticulo = costoArticulo;
	}

	public double getCostoPreparacion() {
		return costoPreparacion;
	}

	public void setCostoPreparacion(double costoPreparacion) {
		this.costoPreparacion = costoPreparacion;
	}

	public double getCostoMantenimiento() {
		return costoMantenimiento;
	}

	public void setCostoMantenimiento(double costoMantenimiento) {
		this.costoMantenimiento = costoMantenimiento;
	}

	public ArrayList<Integer> getArticulosSemanales() {
		return articulosSemanales;
	}

	public void setArticulosSemanales(ArrayList<Integer> articulosSemanales) {
		this.articulosSemanales = articulosSemanales;
	}

	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public ArrayList<Integer> sistemaLotePorLote() {
		planPedidos = articulosSemanales;
		return articulosSemanales;
	}
	
	public ArrayList<Integer> sistemaCantidadDeOrdenEconomica(){
		int EOQ = (int) calcularCantidadDeOrdenEconomica();
		ArrayList<Integer> retorno = new ArrayList<>();
		int cantidad = 0;
		retorno.add(EOQ);
		for(int i = 0; i < articulosSemanales.size(); i++) {
			cantidad += articulosSemanales.get(i);
			if(cantidad >= EOQ) {
				retorno.add(EOQ);
				cantidad = articulosSemanales.get(i);
			}else if(i != 0){
				retorno.add(0);
			}
		}
		planPedidos = retorno;
		return retorno;
	}

	public double calcularCantidadDeOrdenEconomica() {

		// Pasamos de semanas a a�os
		int totalArticulos = 0;
		for (int i = 0; i < articulosSemanales.size(); i++) {
			totalArticulos += articulosSemanales.get(i);
		}
		double D = ((totalArticulos * 1.0) / articulosSemanales.size()) * (52);

		// encontramos el costo anual de mantemiento
		double H = costoMantenimiento * costoArticulo * 52;
		double EOQ = Math.sqrt((2 * D * costoPreparacion) / H);

		return EOQ;
	}

	public ArrayList<Integer> sistemaPeriodoDeSuministro(int t) {

		ArrayList<Integer> pedidos = new ArrayList<Integer>();
		int contador = 0;
		int sumaProductos = 0;
		for (int i = 0; i < articulosSemanales.size(); i++) {
			contador++;
			sumaProductos = sumaProductos + articulosSemanales.get(i);
			if (contador == t) {
				contador = 0;
				pedidos.add(sumaProductos);
				sumaProductos = 0;
			} else if (i == articulosSemanales.size() - 1) {
				pedidos.add(sumaProductos);
			}
		}
		planPedidos = pedidos;
		return pedidos;
	}

	public ArrayList<Integer> sistemaCantidadDeOrdenDePeriodo() {
		ArrayList<Integer> retornoReves = new ArrayList<Integer>();
		int demanda = 0;
		for (int i = 0; i < articulosSemanales.size(); i++) {
			demanda += articulosSemanales.get(i);
		}
		double frecienciaPedido = (demanda * 1.0) / calcularCantidadDeOrdenEconomica();
		int periodoOptimoPedido = (int) ((articulosSemanales.size() * 1.0) / frecienciaPedido);
		int cantidad = 0;
		System.out.println(articulosSemanales.size() - 1);
		for (int j = articulosSemanales.size() - 1; j >= 0; j--) {
			cantidad += articulosSemanales.get(j);
			if (j % periodoOptimoPedido == 0) {
				retornoReves.add(cantidad);
				cantidad = 0;
			} else {
				retornoReves.add(0);
			}
		}
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		for (int i = retornoReves.size() - 1; i >= 0; i--) {
			retorno.add(retornoReves.get(i));
		}
		planPedidos = retorno;
		return retorno;
	}
	
	public ArrayList<Integer> sistemaPorMenorCostoUnitario() {
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		int cantidad = 0;
		int vecesAlmacenada = 0;
		int unidadesAlmacenadas = 0;
		int ultimaVez = 0;
		for(int i = 0; i < articulosSemanales.size(); i++) {
			cantidad += articulosSemanales.get(i);
			unidadesAlmacenadas += articulosSemanales.get(i) * vecesAlmacenada;
			double costoUnitario = (costoPreparacion + (costoMantenimiento * (unidadesAlmacenadas*1.0)))/(cantidad*1.0);
			if(i != articulosSemanales.size()-1) {
				int unidadesAlmacenadasNext = unidadesAlmacenadas + (articulosSemanales.get(i+1)*(vecesAlmacenada+1)); 
				double costoUnitarioNext = (costoPreparacion + (costoMantenimiento * (unidadesAlmacenadasNext*1.0)))/((cantidad+articulosSemanales.get(i+1))*1.0);
				if(costoUnitarioNext > costoUnitario) {
					auxActualizarLista(retorno, ultimaVez, i, cantidad);
					cantidad = 0;
					unidadesAlmacenadas = 0;
					vecesAlmacenada = 0;
					ultimaVez = i+1;
				}else {
					vecesAlmacenada++;
				}
			}else {
				auxActualizarLista(retorno, ultimaVez, i, cantidad);
			}
		}
		planPedidos = retorno;
		return retorno;
	}

	public ArrayList<Integer> sistemaPorCostoTotalMinimo() {
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		int cantidad = 0;
		int vecesAlmacenada = 0;
		int unidadesAlmacenadas = 0;
		int ultimaVez = 0;
		for(int i = 0; i < articulosSemanales.size(); i++) {
			cantidad += articulosSemanales.get(i);
			unidadesAlmacenadas += articulosSemanales.get(i) * vecesAlmacenada;
			double diferencia = Math.abs(costoPreparacion - (costoMantenimiento * (unidadesAlmacenadas*1.0)));
			if(i != articulosSemanales.size()-1) {
				int unidadesAlmacenadasNext = unidadesAlmacenadas + (articulosSemanales.get(i+1)*(vecesAlmacenada+1)); 
				double diferenciaNext = Math.abs(costoPreparacion - (costoMantenimiento * (unidadesAlmacenadasNext*1.0)));
				if(diferenciaNext > diferencia) {
					auxActualizarLista(retorno, ultimaVez, i, cantidad);
					cantidad = 0;
					unidadesAlmacenadas = 0;
					vecesAlmacenada = 0;
					ultimaVez = i+1;
				}else {
					vecesAlmacenada++;
				}
			}else {
				auxActualizarLista(retorno, ultimaVez, i, cantidad);
			}
		}
		planPedidos = retorno;
		return retorno;
	}
	
	public void auxActualizarLista(ArrayList<Integer> lista, int j, int i, int cantidad) {
		lista.add(cantidad);
		int k = j;
		while(k < i) {
			lista.add(0);
			k++;
		}
	}
	
	public String toString() {
		String retorno = "";
		for(int i = 0; i < planPedidos.size(); i++) {
			if(i != planPedidos.size()-1) {
				retorno += planPedidos.get(i) + " - ";
			}else {
				retorno += planPedidos.get(i) + "";
			}
		}
		return retorno;
	}

	public static void main(String[] arg0) {
		// SE DEBE CONSIDERAR FORMATOS
		ArrayList<Integer> pedidos = new ArrayList<>();
//		pedidos.add(50);
//		pedidos.add(60);
//		pedidos.add(70);
//		pedidos.add(60);
//		pedidos.add(95);
//		pedidos.add(75);
//		pedidos.add(60);
//		pedidos.add(55);
//		LotSizingMethods l = new LotSizingMethods(10.0, 47.0, 0.005, pedidos, SEMANAL);
		
		pedidos.add(160);
		pedidos.add(166);
		pedidos.add(220);
		pedidos.add(271);
		pedidos.add(210);
		pedidos.add(188);
		pedidos.add(161);
		pedidos.add(170);
		OldLotSizingMethods l = new OldLotSizingMethods(10.0, 250.0, 0.5, pedidos, SEMANAL);
		
//		pedidos.add(50);
//		pedidos.add(60);
//		pedidos.add(70);
//		pedidos.add(60);
//		pedidos.add(95);
//		pedidos.add(75);
//		pedidos.add(60);
//		pedidos.add(55);
//		SistemaLoteo l = new SistemaLoteo(10.0, 47.0, 0.05, pedidos, SEMANAL);
//		
		System.out.println(l.sistemaPorCostoTotalMinimo());
		// LUC TRAE PROBLEMAS...
		// ArrayList<Integer>POS = l.LUC();
		// for (int i = 0; i < POS.size(); i++) {
		// System.out.println(POS.get(i));
		// }
	}
}