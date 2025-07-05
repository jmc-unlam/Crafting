package modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representa una receta en el sistema de crafteo, actuando como un nodo compuesto en el patrón Composite.
 * Define los ingredientes necesarios para producir un objeto y gestiona operaciones recursivas 
 * sobre ellos, como el cálculo de tiempos totales o la descomposición en ingredientes básicos.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class Receta implements Comparable<Receta> {
	private Objeto objetoProducido;
	private Map<Objeto, Integer> ingredientes;
	private int cantidadProducida;
	private int tiempoBase;
	private MesaDeTrabajo mesaRequerida;

	/**
     * Constructor para recetas sin mesa requerida.
     * 
     * @param objetoProducido Objeto resultante de la receta.
     * @param ingredientes    Mapa de ingredientes necesarios (Objeto -> Cantidad).
     * @param cantidadProducida Cantidad de objetos producidos por esta receta.
     * @param tiempoBase      Tiempo base para el crafteo.
     */
	public Receta(Objeto objetoProducido, Map<Objeto, Integer> ingredientes, int cantidadProducida, int tiempoBase) {
		this.objetoProducido = objetoProducido;
		this.ingredientes = ingredientes;
		this.cantidadProducida = cantidadProducida;
		this.tiempoBase = tiempoBase;
		this.mesaRequerida = null;
	}

	/**
     * Constructor para recetas con mesa requerida.
     * 
     * @param objetoProducido Objeto resultante de la receta.
     * @param ingredientes    Mapa de ingredientes necesarios (Objeto -> Cantidad).
     * @param cantidadProducida Cantidad de objetos producidos por esta receta.
     * @param tiempoBase      Tiempo base para el crafteo.
     * @param mesa            Mesa de trabajo requerida para esta receta.
     */
	public Receta(Objeto objetoProducido, Map<Objeto, Integer> ingredientes, int cantidadProducida, int tiempoBase,
			MesaDeTrabajo mesa) {
		this.objetoProducido = objetoProducido;
		this.ingredientes = ingredientes;
		this.cantidadProducida = cantidadProducida;
		this.tiempoBase = tiempoBase;
		this.mesaRequerida = mesa;
	}

	/**
     * Devuelve la cantidad de objetos producidos por esta receta.
     * 
     * @return Cantidad producida.
     */
	public int getCantidadProducida() {
		return cantidadProducida;
	}

	/**
     * Devuelve el tiempo base para el crafteo de esta receta.
     * 
     * @return Tiempo base.
     */
	public int getTiempoBase() {
		return tiempoBase;
	}

	/**
     * Devuelve el objeto producido por esta receta.
     * 
     * @return Objeto resultante.
     */
	public Objeto getObjetoProducido() {
		return objetoProducido;
	}

	/**
     * Devuelve la mesa de trabajo requerida para esta receta, si aplica.
     * 
     * @return Mesa de trabajo requerida, o null si no es necesaria.
     */
	public MesaDeTrabajo getMesaRequerida() {
		return mesaRequerida;
	}

	/**
     * Calcula el tiempo total necesario para producir los ingredientes de esta receta.
     * Si un ingrediente es compuesto, delega el cálculo a su propia receta.
     * 
     * @param recetario Recetario que proporciona acceso a las recetas de los ingredientes.
     * @return Tiempo total acumulado.
     */
	public int calcularTiempoTotal(Recetario recetario, int cantidadNecesaria) {
		int tiempoTotal = tiempoBase;
		int vecesReceta = Math.ceilDiv(cantidadNecesaria, this.getCantidadProducida());
		
		tiempoTotal *= vecesReceta;
		for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
			Objeto ingrediente = entry.getKey();
			int cantidadIngrediente = entry.getValue();
			if (!ingrediente.esBasico()) {
				Receta recetaIngrediente = recetario.buscarReceta(ingrediente);

				tiempoTotal += recetaIngrediente.calcularTiempoTotal(recetario,cantidadIngrediente);
			}
		}
		return tiempoTotal;
	}

	/**
     * Devuelve una copia de los ingredientes en la receta con sus cantidades.
     * 
     * @return Mapa de objetos y sus cantidades.
     */
	public Map<Objeto, Integer> getIngredientes() {
		return new HashMap<>(ingredientes);
	}

	/**
     * Obtiene una lista de ingredientes básicos necesarios para esta receta, 
     * descomponiendo recursivamente los ingredientes compuestos.
     * 
     * @param recetario Recetario que proporciona acceso a las recetas de los ingredientes.
     * @return Mapa de ingredientes básicos (Objeto -> Cantidad).
     */
	public Map<Objeto, Integer> getIngredientesBasicos(Recetario recetario, int cantidadAnterior) {
		Map<Objeto, Integer> basicos = new HashMap<>();
		int vecesReceta = Math.ceilDiv(cantidadAnterior,this.getCantidadProducida());

		for (Map.Entry<Objeto, Integer> elemento : this.ingredientes.entrySet()) {
			Objeto ingrediente = elemento.getKey();
			int cantidadRequerida = elemento.getValue() * vecesReceta;;

			if (ingrediente.esBasico()) {
				// Si es básico, lo agregamos directamente
				basicos.merge(ingrediente, cantidadRequerida, Integer::sum);
			} else {
				// Si es intermedio, buscamos su receta y descomponemos recursivamente
				Receta subReceta = recetario.buscarReceta(ingrediente);
				Map<Objeto, Integer> subIngredientesBasicos = subReceta.getIngredientesBasicos(recetario,cantidadRequerida);

				// Multiplicamos por la cantidad requerida y fusionamos
				for (Map.Entry<Objeto, Integer> subElemento : subIngredientesBasicos.entrySet()) {
					basicos.merge(subElemento.getKey(), subElemento.getValue(), Integer::sum);
				}
			}
		}
		return basicos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Objeto producido: ").append(objetoProducido).append("\n");
		sb.append("Cantidad producida: ").append(cantidadProducida).append("\n");
		sb.append("Tiempo de crafteo: ").append(tiempoBase).append("\n");
		if (mesaRequerida != null)
			sb.append("Mesa Requerida: ").append(mesaRequerida).append("\n");
		sb.append("Ingredientes:\n");

		for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
			Objeto obj = entry.getKey();
			int cantidad = entry.getValue();
			sb.append("    - ").append(obj).append(" x ").append(cantidad).append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidadProducida, ingredientes, objetoProducido, tiempoBase);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Receta)) {
			return false;
		}
		Receta other = (Receta) obj;
		return cantidadProducida == other.cantidadProducida && Objects.equals(ingredientes, other.ingredientes)
				&& Objects.equals(objetoProducido, other.objetoProducido) && tiempoBase == other.tiempoBase;
	}

	/**
     * Compara esta receta con otra para determinar orden (prioriza cantidad producida y tiempo).
     * 
     * @param o Receta a comparar.
     * @return Valor negativo, cero o positivo según el orden.
     */
	@Override
	public int compareTo(Receta o) {

		int comparacio = Integer.compare(o.cantidadProducida, this.cantidadProducida); // Orden descendente

		if (comparacio != 0)
			return comparacio;
		else
			return Integer.compare(this.tiempoBase, o.tiempoBase);
	}
}
