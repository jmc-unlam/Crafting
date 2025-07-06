package modelo;

import java.text.Normalizer;
import java.util.Objects;

/**
 * Clase base abstracta que representa un objeto en el sistema de crafteo.
 * Actúa como el componente en el patrón Composite, permitiendo tratar objetos individuales
 * y compuestos de manera uniforme.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public abstract class Objeto implements Comparable<Objeto> {
	
	private String nombre;

	/**
     * Devuelve el nombre normalizado del objeto.
     * 
     * @return Nombre del objeto.
     * @throws IllegalArgumentException Si el nombre esta vacio. 
     */
	private String normalizar(String texto) {
		if (texto == null || texto.isEmpty()) {
			throw new IllegalArgumentException("El nombre esta vacio");
		}

		String normalized = texto.toLowerCase();
		// quita todas letras que no son a-z, 0-9, espacios y la ñ
		normalized = normalized.replaceAll("[^a-zñ0-9 ]", "");

		// quita los espacios repetidos
		normalized = normalized.replaceAll("\\s+", " ");
		// descomposicion canonica
		normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
		// recompongo solo la ñ
		normalized = normalized.replaceAll("n\\u0303", "ñ");
		// quita los acentos y simbolos raros
		normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

		// quita los espacios entre palabras de longitud 1
		normalized = normalized.replaceAll("(?<=\\b\\w)\\s(?=\\w\\b)", "");
		// quita los espacios al principio y al final
		normalized = normalized.trim();

		return normalized;
	}

	/**
     * Constructor que normaliza el nombre del objeto.
     * 
     * @param nombre Nombre original del objeto (puede contener caracteres especiales).
     * @throws IllegalArgumentException Si el nombre está vacío.
     */
	public Objeto(String nombre) {
		this.nombre = normalizar(nombre);
	}

	/**
     * Devuelve el nombre normalizado del objeto.
     * 
     * @return Nombre del objeto.
     */
	public String getNombre() {
		return nombre;
	}

	/**
     * Determina si el objeto es básico (no crafteable).
     * 
     * @return true si es básico, false si es compuesto o una mesa de trabajo.
     */
	public abstract boolean esBasico();

	/**
     * Determina si el objeto es apilable (puede tener múltiples unidades en el inventario).
     * 
     * @return true si es apilable, false si no lo es (ej.: mesas de trabajo).
     */
	public abstract boolean esApilable();

	/**
     * Agrega recetas asociadas a este objeto al recetario (por defecto no hace nada).
     * Se reescribe en clases concretas como {@link MesaDeTrabajo}.
     * 
     * @param r Recetario donde se agregarán las recetas.
     */
	public void listaDeRecetasPropias(Recetario r) {
	}
	
	/**
     * Remueve recetas asociadas a este objeto del recetario (por defecto no hace nada).
     * Se reescribe en clases concretas como {@link MesaDeTrabajo}.
     * 
     * @param r Recetario donde se removerán las recetas.
     */
	public void removerRecetasPropias(Recetario r) {
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Objeto)) {
			return false;
		}
		Objeto other = (Objeto) obj;
		return Objects.equals(nombre, other.nombre);
	}
	
	@Override
	public int compareTo(Objeto o) {

		return this.nombre.compareTo(o.nombre);
	}
}