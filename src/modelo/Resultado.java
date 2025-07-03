package modelo;

import java.util.Map;

	
	/**
	 * Resultado es una clase donde se guardan los Resultados de las consultas usando sus multiples Constructores
	 *  y a su ves sus métodos informan los datos guardados con los diferentes formatos dependiendo 
	 *  de cual punto del informe se quiere representar. 
	 * 
	 * Solo Guarda y Muestra. No procesa
	 * 
	 */
public class Resultado {

	private int cantidadCrafteable = 0;
	private int tiempo = 0;
	private Objeto objetoCrafteableUnico;
	private Map<Objeto, Integer> ingredientes;

	/**
	 * Constructor /2. Se ingresa la cantidad crafteada y el tiempo que tarda ese crafteo
	 * 
	 * @param cantidadCrafteable
	 * @param tiempo
	 */
	public Resultado(int cantidadCrafteable, int tiempo) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
	}
	
	
	/**
	 *  Constructor /3. Se ingresa la cantidad crafteada, el tiempo que tarda ese crafteo y el objeto crafteado.
	 * 
	 *
	 * 5. ¿Cuántos puedo craftear?
	 * 
	 * @param cantidadCrafteable
	 * @param tiempo
	 * @param objetoCrafteableUnico
	 */
	public Resultado(int cantidadCrafteable, int tiempo, Objeto objetoCrafteableUnico) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
		this.objetoCrafteableUnico = objetoCrafteableUnico;
	}

	/**
	 * Constructor /4. Se ingresa la cantidad crafteada, el tiempo que tarda ese crafteo , el objeto crafteado y los ingredientes.
	 * 
	 *  1. ¿Qué necesito para craftear un objeto?
	 *  2. ¿Qué necesito para craftear un objeto desde cero?
	 *  3. ¿Qué me falta para craftear un objeto?
	 *  4. ¿Qué me falta para craftear un objeto desde cero?
	 *  
	 * @param cantidadCrafteable
	 * @param tiempo
	 * @param objetoCrafteableUnico
	 * @param ingredientes
	 */
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

	/**
	 * 1. ¿Qué necesito para craftear un objeto?
	 * 
	 * Informa el objeto a craftear, el tiempo de crafteo y los ingredientes necesarios.
	 */
	public void informarCantidadOpcion1() {
		System.out.println("=== Ingredientes necesarios para " + objetoCrafteableUnico.getNombre() + " ===");
		System.out.println("Tiempo en Crafteo (min):" + this.tiempo);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
	}

	/**
	 * 2. ¿Qué necesito para craftear un objeto desde cero?
	 * 
	 * Informa el objeto a craftear, el tiempo de crafteo y los ingredientes necesarios.
	 */
	public void informarCantidadOpcion2() {
		System.out
				.println("=== Ingredientes basicos necesarios para " + this.objetoCrafteableUnico.getNombre() + " ===");

		System.out.println("Tiempo de Crafteo Total (min):" + this.tiempo);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

	}

	/**
	 * 3. ¿Qué me falta para craftear un objeto?
	 * 
	 * Informa Objeto a craftear, tiempo y ingredientes necesarios, si no muestra q No faltan.
	 */
	public void informarCantidadOpcion3() {
		System.out.println("\nIngredientes faltantes para " + objetoCrafteableUnico.getNombre() + ":");

		Map<Objeto, Integer> faltantes = ingredientes;

		System.out.println("Tiempo de Crafteo (min):" + this.tiempo);
		if (faltantes.isEmpty())
			System.out.println("No faltan ingredientes directos!");
		else
			faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));
	}

	/**
	 * 4. ¿Qué me falta para craftear un objeto desde cero?
	 * 
	 * Informa Objeto a craftear, tiempo y ingredientes necesarios, si no muestra q No faltan.
	 */
	public void informarCantidadOpcion4() {
		//
		System.out.println("\nNuevos ingredientes básicos faltantes para " + objetoCrafteableUnico.getNombre() + ":");
		Map<Objeto, Integer> faltantesBasicos2 = ingredientes;

		System.out.println("Tiempo de Crafteo Total (min):" + this.tiempo);
		if (faltantesBasicos2.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
	}

	/**
	 * 5. ¿Cuántos puedo craftear?
	 * 
	 * Informa la Cantidad, el objeto y el tiempo del intento de crafteo.
	 */
	public void informarCantidadOpcion5() {
		//
		System.out.println("Cantidad de (" + objetoCrafteableUnico.getNombre() + ") crafteables ahora: "
				+ cantidadCrafteable + " en un tiempo de " + tiempo + "(min).");
	}

	/**
	 * 6. Realizar el crafteo indicado 
	 * 
	 * Informa objeto, cantidad y tiempo del crafteo generado.
	 */
	public void informarTiempoCrafteoOpcion6() {
		//6. Realizar el crafteo indicado 
		System.out.println("\n=== Intentando craftear " + cantidadCrafteable + " unidad de " + objetoCrafteableUnico);
		System.out.println("Tiempo Total (min): " + tiempo);
		System.out.println(objetoCrafteableUnico.getNombre() + " creado Existosamente.\n");
	}

}
