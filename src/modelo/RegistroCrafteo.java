package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un registro individual de una acción de crafteo.
 * Almacena información sobre el objeto creado, la cantidad, el tiempo invertido y los ingredientes utilizados.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class RegistroCrafteo {
	// Campo estático que cuenta cuántos registros se han creado
	private static int contadorTurnos = 1;

	private Objeto objetoCrafteado;
	private int cantidadCrafteada;
	private int turno; // Número de turno de crafteo
	private final int tiempoTotal;
	private Map<Objeto, Integer> ingredientesUsados;

	/**
     * Constructor para registros con ingredientes especificados.
     * 
     * @param objetoCrafteado Objeto resultante del crafteo.
     * @param cantidadCrafteada Cantidad de objetos creados.
     * @param tiempoTotal Tiempo total invertido en el proceso.
     * @param ingredientesUsados Ingredientes consumidos (deben ser básicos).
     */
	public RegistroCrafteo(Objeto objetoCrafteado, int cantidadCrafteada, int tiempoTotal,
			Map<Objeto, Integer> ingredientesUsados) {
		if (objetoCrafteado == null) {
			throw new IllegalArgumentException("El objeto crafteado no puede ser nulo");
		}
		if (cantidadCrafteada <= 0) {
			throw new IllegalArgumentException("La cantidad crafteada debe ser positiva");
		}
		if (tiempoTotal < 0) {
			throw new IllegalArgumentException("El tiempo total no puede ser negativo");
		}

		this.turno = contadorTurnos++;
		this.objetoCrafteado = objetoCrafteado;
		this.cantidadCrafteada = cantidadCrafteada;
		this.tiempoTotal = tiempoTotal;
		this.ingredientesUsados = ingredientesUsados;
	}

	/**
     * Constructor para registros sin ingredientes específicos.
     * 
     * @param objetoCrafteado Objeto resultante del crafteo.
     * @param cantidadCrafteada Cantidad de objetos creados.
     * @param tiempoTotal Tiempo total invertido.
     */
	public RegistroCrafteo(Objeto objetoCrafteado, int cantidadCrafteada, int tiempoTotal) {
		if (objetoCrafteado == null) {
			throw new IllegalArgumentException("El objeto crafteado no puede ser nulo");
		}
		if (cantidadCrafteada <= 0) {
			throw new IllegalArgumentException("La cantidad crafteada debe ser positiva");
		}
		if (tiempoTotal < 0) {
			throw new IllegalArgumentException("El tiempo total no puede ser negativo");
		}

		this.turno = contadorTurnos++;
		this.objetoCrafteado = objetoCrafteado;
		this.cantidadCrafteada = cantidadCrafteada;
		this.tiempoTotal = tiempoTotal;

	}

	/**
     * Devuelve el objeto creado durante el crafteo.
     * 
     * @return Objeto crafteado.
     */
	public Objeto getObjetoCrafteado() {
		return objetoCrafteado;
	}

	/**
     * Devuelve la cantidad de objetos creados.
     * 
     * @return Cantidad crafteada.
     */
	public int getCantidadCrafteada() {
		return cantidadCrafteada;
	}

	/**
     * Devuelve el número de turno en que ocurrió el crafteo.
     * 
     * @return Número de turno.
     */
	public int getTurno() {
		return turno;
	}

	/**
     * Devuelve el tiempo total invertido en el crafteo.
     * 
     * @return Tiempo total en segundos o unidades equivalentes.
     */
	public int getTiempoTotal() {
		return tiempoTotal;
	}
	
	/**
     * Devuelve una copia inmutable de los ingredientes utilizados.
     * 
     * @return Mapa de ingredientes y sus cantidades utilizadas.
     */
    public Map<Objeto, Integer> getIngredientesUsados() {
        return new HashMap<>(ingredientesUsados);
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Turno:").append(turno).append("\n");
		sb.append("\t").append("objeto creado->").append(objetoCrafteado).append("\n");
		sb.append("\t").append("cantidad creada: ").append(cantidadCrafteada).append("\n");
		sb.append("\t").append("tiempo total: ").append(tiempoTotal).append("\n");

		if (ingredientesUsados != null) {
			for (Map.Entry<Objeto, Integer> entry : ingredientesUsados.entrySet()) {
				Objeto ingrediente = entry.getKey();
				Integer cantidad = entry.getValue();
				sb.append("\t").append("Ingrediente usado: ").append(ingrediente).append(" - Cantidad: ")
						.append(cantidad).append("\n");
			}
		}
		return sb.toString();
	}

	/**
     * Reinicia el contador de turnos.
     * Útil para reiniciar la historia sin crear una nueva instancia.
     */
	public static void reiniciarContador() {
		contadorTurnos = 1;
	}
}
