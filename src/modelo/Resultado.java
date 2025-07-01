package modelo;

import java.util.Map;

public class Resultado {
	private int cantidadCrafteable = 0;
	private int tiempo = 0;
	private Objeto objetoCrafteableUnico;
	private Map<Objeto, Integer> ingredientes;

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

	public Resultado(int cantidadCrafteable, int tiempo, Objeto objetoCrafteableUnico,
			Map<Objeto, Integer> ingredientes) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
		this.objetoCrafteableUnico = objetoCrafteableUnico;
		this.ingredientes = ingredientes;
	}

	public int getCantidadCrafteable() {
		return cantidadCrafteable;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void informarCantidadOpcion1() {
		System.out.println("=== Ingredientes necesarios para " + objetoCrafteableUnico.getNombre() + " ===");
		System.out.println("Tiempo en Crafteo (min):" + this.tiempo);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
	}

	public void informarCantidadOpcion2() {
		System.out
				.println("=== Ingredientes basicos necesarios para " + this.objetoCrafteableUnico.getNombre() + " ===");

		System.out.println("Tiempo de Crafteo Total (min):" + this.tiempo);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

	}

	public void informarCantidadOpcion3() {
		System.out.println("\nIngredientes faltantes para " + objetoCrafteableUnico.getNombre() + ":");

		Map<Objeto, Integer> faltantes = ingredientes;

		System.out.println("Tiempo de Crafteo (min):" + this.tiempo);
		if (faltantes.isEmpty())
			System.out.println("No faltan ingredientes directos!");
		else
			faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));
	}

	public void informarCantidadOpcion4() {
		System.out.println("\nNuevos ingredientes básicos faltantes para " + objetoCrafteableUnico.getNombre() + ":");
		Map<Objeto, Integer> faltantesBasicos2 = ingredientes;

		System.out.println("Tiempo de Crafteo Total (min):" + this.tiempo);
		if (faltantesBasicos2.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
	}

	public void informarCantidadOpcion5() {
		System.out.println("Cantidad de (" + objetoCrafteableUnico.getNombre() + ") crafteables ahora: "
				+ cantidadCrafteable + " en un tiempo de " + tiempo + "(min).");
	}

	public void informarTiempoCrafteoOpcion6() {
		System.out.println("\n=== Intentando craftear " + cantidadCrafteable + " unidad de " + objetoCrafteableUnico);
		System.out.println("Tiempo Total (min): " + tiempo);
		System.out.println(objetoCrafteableUnico.getNombre() + " creado Existosamente.\n");
	}

}
