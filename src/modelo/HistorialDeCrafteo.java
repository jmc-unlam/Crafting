package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gestiona el historial de todas las acciones de crafteo realizadas.
 * Implementa el patrón Singleton para garantizar una única instancia global.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class HistorialDeCrafteo {

	private static HistorialDeCrafteo instanciaUnica;

	private List<RegistroCrafteo> registros;

	private HistorialDeCrafteo() {
		this.registros = new ArrayList<>();
	}

	/**
     * Devuelve la instancia única del historial (Singleton).
     * 
     * @return Instancia única de HistorialDeCrafteo.
     */
	public static HistorialDeCrafteo getInstanciaUnica() {
		if (instanciaUnica == null) {
			instanciaUnica = new HistorialDeCrafteo();
		}
		return instanciaUnica;
	}

	/**
     * Agrega un nuevo registro al historial.
     * 
     * @param objeto Objeto crafteado.
     * @param cantidad Cantidad de objetos creados.
     * @param tiempoTotal Tiempo total invertido en el proceso.
     * @param ingredientesUsados Ingredientes consumidos durante el crafteo.
     */
	public void agregarRegistro(Objeto objeto, int cantidad, int tiempoTotal, Map<Objeto, Integer> ingredientesUsados) {
		if (objeto == null) {
			throw new IllegalArgumentException("El objeto no puede ser nulo");
		}
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser positiva");
		}
		if (tiempoTotal <= 0) {
			throw new IllegalArgumentException("El tiempo no puede ser negativo o nulo");
		}

		registros.add(new RegistroCrafteo(objeto, cantidad, tiempoTotal, ingredientesUsados));
	}

	/**
     * Agrega un nuevo registro al historial sin ingredientes específicos.
     * 
     * @param objeto Objeto crafteado.
     * @param cantidad Cantidad de objetos creados.
     * @param tiempoTotal Tiempo total invertido en el proceso.
     */
	public void agregarRegistro(Objeto objeto, int cantidad, int tiempoTotal) {
		if (objeto == null) {
			throw new IllegalArgumentException("El objeto no puede ser nulo");
		}
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser positiva");
		}
		if (tiempoTotal <= 0) {
			throw new IllegalArgumentException("El tiempo no puede ser negativo o nulo");
		}

		registros.add(new RegistroCrafteo(objeto, cantidad, tiempoTotal));
	}

	/**
     * Limpia todos los registros y reinicia el contador de turnos.
     */
	public void limpiarRegistros() {
		registros.clear();
		RegistroCrafteo.reiniciarContador();
	}

	/**
     * Devuelve una copia inmutable de todos los registros.
     * 
     * @return Lista de registros.
     */
	public List<RegistroCrafteo> getRegistros() {
		return new ArrayList<>(registros); // Devolver una copia
	}

	@Override
	public String toString() {

		if (registros.size() == 0)
			return "=== Historial de Crafteos Vacio ===\n";

		StringBuilder sb = new StringBuilder();
		sb.append("=== Historial de Crafteos ===\n");
		for (RegistroCrafteo registro : registros) {
			sb.append(registro).append("\n");
		}
		sb.append("=============================\n");
		return sb.toString();
	}

	/**
     * Busca registros por nombre del objeto crafteado (búsqueda exacta).
     * 
     * @param objeto Objeto buscado.
     * @return Lista de registros que coinciden con el objeto.
     */
	public List<RegistroCrafteo> buscarCrafteosPorNombre(Object objeto) {
		List<RegistroCrafteo> resultados = new ArrayList<>();
		for (RegistroCrafteo registro : registros) {
			if (registro.getObjetoCrafteado().equals(objeto)) {
				resultados.add(registro);
			}
		}
		return resultados;
	}

	/**
     * Busca el primer crafteo realizado para un objeto (el más antiguo).
     * 
     * @param objeto Objeto buscado.
     * @return Primer registro o null si no se encontró.
     */
	public RegistroCrafteo buscarPrimerCrafteo(Object objeto) {
		// el primero esta al ultimo de la lista
		RegistroCrafteo ultimo = null;
		for (int i = registros.size() - 1; i >= 0; i--) {
			RegistroCrafteo registro = registros.get(i);
			if (registro.getObjetoCrafteado().equals(objeto)) {
				ultimo = registro;
			}
		}
		return ultimo;
	}

	/**
     * Busca el último crafteo realizado para un objeto (el más reciente).
     * 
     * @param objeto Objeto buscado.
     * @return Último registro o null si no se encontró.
     */
	public RegistroCrafteo buscarUltimoCrafteo(Object objeto) {
		// el ultimo crafteo es el primero de la lista
		RegistroCrafteo primero = null;
		for (int i = 0; i < registros.size(); i++) {
			RegistroCrafteo registro = registros.get(i);
			if (registro.getObjetoCrafteado().equals(objeto)) {
				primero = registro;
			}
		}
		return primero;

	}

	/**
     * Calcula la cantidad total crafteada de un objeto en todo el historial.
     * 
     * @param objeto Objeto a evaluar.
     * @return Cantidad total crafteada.
     */
	public int getCantidadTotalCrafteada(Object objeto) {
		int total = 0;
		for (RegistroCrafteo registro : registros) {
			if (registro.getObjetoCrafteado().equals(objeto)) {
				total += registro.getCantidadCrafteada();
			}
		}
		return total;
	}
}
