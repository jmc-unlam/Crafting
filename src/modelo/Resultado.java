package modelo;

public class Resultado {
	private int cantidadCrafteable=0;
	private int tiempo=0;
	private Objeto objetoCrafteableUnico;
	
	public Resultado(int cantidadCrafteable, int tiempo) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
	}
	
	public Resultado(int cantidadCrafteable, int tiempo, Objeto objetoCrafteableUnico) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
		this.objetoCrafteableUnico = objetoCrafteableUnico;
	}
	
	public int getCantidadCrafteable() {
		return cantidadCrafteable;
	}

	public int getTiempo() {
		return tiempo;
	}
	
	public void informarCantidadOpcion5() {
		System.out.println("Cantidad de (" + objetoCrafteableUnico.getNombre() + ") crafteables ahora: "
				+ cantidadCrafteable + " en un tiempo de " + tiempo + "(min).");
	}
	
	public void informarTiempoCrafteoOpcion6() {
		System.out.println("\n=== Intentando craftear " + cantidadCrafteable + " unidad de " + objetoCrafteableUnico);
		System.out.println("Tiempo Total (min): " + tiempo);
		System.out.println(objetoCrafteableUnico.getNombre() + " creado Existosamente.");
	}
	
}
