package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Clase central que gestiona las recetas en el sistema, actuando como cliente del patrón Composite.
 * Almacena las relaciones entre objetos y sus recetas, y coordina operaciones recursivas 
 * como la validación de ciclos o el cálculo de ingredientes necesarios.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class Recetario {
	private Map<Objeto, List<Receta>> recetasPorObjeto;

	/**
     * Constructor que inicializa un recetario vacío.
     */
	public Recetario() {
		this.recetasPorObjeto = new HashMap<>();
	}

	/**
     * Constructor que inicializa el recetario con una lista de recetas.
     * 
     * @param recetasIniciales Lista de recetas iniciales.
     */
	public Recetario(List<Receta> recetasIniciales) {
		this.recetasPorObjeto = new HashMap<>();
		if (recetasIniciales != null) {
			for (Receta receta : recetasIniciales) {
				this.agregarReceta(receta); 
			}
		}
	}

	/**
     * Agrega una receta al recetario. Si el objeto producido ya tiene recetas,
     * se añade esta como alternativa. 
     * 
     * @param receta Receta a agregar.
     * @throws IllegalArgumentException Si la receta genera un ciclo.
     */
	public void agregarReceta(Receta receta) {
		if (receta == null) {
			throw new IllegalArgumentException("La receta no puede ser nula");
		}
		recetasPorObjeto.putIfAbsent(receta.getObjetoProducido(), new ArrayList<>());
		List<Receta> recetasExistentes = recetasPorObjeto.get(receta.getObjetoProducido());

		if (!recetasExistentes.contains(receta)) {
			recetasExistentes.add(receta);
			Collections.sort(recetasExistentes);
		}
		Set<Objeto> visitados = new HashSet<>();
		if (detectarCiclo(receta.getObjetoProducido(), visitados)) {
			removerReceta(receta);
			System.err.println("La receta que produce -> "+receta.getObjetoProducido()+" no fue agregada porque tiene ciclos");
		}
	}

	/**
     * Detecta ciclos en la estructura de recetas para evitar dependencias infinitas.
     * 
     * @param objeto Objeto a verificar.
     * @param visitados Conjunto de objetos ya revisados.
     * @return true si se detecta un ciclo, false en caso contrario.
     */
	private boolean detectarCiclo(Objeto objeto, Set<Objeto> visitados) {
		if (!visitados.add(objeto)) {
            return true;
        }

        List<Receta> recetasAsociadas;
        try {
            recetasAsociadas = buscarRecetas(objeto); 
        } catch (NoSuchElementException e) {
        	// No hay receta todavia entonces no hay ciclo
            visitados.remove(objeto);
            return false;
        }

        for (Receta recetaActual : recetasAsociadas) {
            for (Map.Entry<Objeto, Integer> entry : recetaActual.getIngredientes().entrySet()) {
                Objeto ingrediente = entry.getKey();
                if (!ingrediente.esBasico()) {
                    if (detectarCiclo(ingrediente, visitados)) {
                        return true;
                    }
                }
            }
        }
        visitados.remove(objeto);
        return false;
	}

	/**
     * Remueve una receta específica del recetario.
     * 
     * @param receta Receta a remover.
     * @throws IllegalArgumentException Si la receta no existe.
     */
	public void removerReceta(Receta receta) {
		if (receta == null) {
			throw new IllegalArgumentException("La receta no puede ser nula");
		}
		List<Receta> recetas = recetasPorObjeto.get(receta.getObjetoProducido());
		if (recetas == null) {
			throw new IllegalArgumentException("La receta no existe en el recetario");
		}
		recetas.remove(receta);
		// Eliminar la clave si la lista queda vacía
		if (recetas.isEmpty()) {
			recetasPorObjeto.remove(receta.getObjetoProducido());
		}
	}

	/**
     * Devuelve una copia con todas las recetas almacenadas.
     * 
     * @return Lista de recetas.
     */
	public List<Receta> getRecetas() {
		List<Receta> todas = new ArrayList<>();
		for (List<Receta> lista : recetasPorObjeto.values()) {
			todas.addAll(lista);
		}
		return new ArrayList<>(todas); // Devuelve toda las recetas.
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("=== RECETARIO ===\n");
		for (Receta receta : this.getRecetas()) {
			sb.append(receta).append("\n");
		}
		return sb.toString();
	}

	/**
     * Busca una receta asociada a un objeto específico.
     * 
     * @param objetoDeseado Objeto del que se busca la receta.
     * @return La primera receta disponible para el objeto.
     * @throws NoSuchElementException Si no hay recetas para el objeto.
     */
	public Receta buscarReceta(Objeto objetoDeseado) {
		List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
		if (recetas == null || recetas.isEmpty()) {
			throw new NoSuchElementException(
					"No se encontró ninguna receta para el objeto: " + objetoDeseado.getNombre());
		}
		return recetas.get(0); // Devolver la primera receta disponible
	}

	/**
     * Busca todas las recetas asociadas a un objeto específico.
     * 
     * @param objetoDeseado Objeto del que se buscan las recetas.
     * @return Lista de recetas disponibles para el objeto.
     * @throws NoSuchElementException Si no hay recetas para el objeto.
     */
	public List<Receta> buscarRecetas(Objeto objetoDeseado) {
		List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
		if (recetas == null || recetas.isEmpty()) {
			throw new NoSuchElementException("No existen recetas asociadas a :" + objetoDeseado);
		}
		return new ArrayList<>(recetas);
	}

	/**
     * Busca los ingredientes de la primer receta asociado al objeto
     * 
     * @param objeto Objeto del que se buscan las recetas.
     * @return Mapa de ingredientes y sus cantidades o Mapa vacio si no tiene recetas asociadas
     */
	public Map<Objeto, Integer> buscarIngredientes(Objeto objeto) {
		List<Receta> recetas = recetasPorObjeto.get(objeto);
		if (recetas == null || recetas.isEmpty()) {
			return Collections.emptyMap();
		}
		return new HashMap<>(recetas.get(0).getIngredientes()); // Solo de la primera receta
	}

	// *****Implementacion Mesas de Trabajo*************
	public void agregarRecetas(List<Receta> recetas) {

		for (Receta receta : recetas) {
			this.agregarReceta(receta);
		}
	}

	public void removerRecetas(List<Receta> recetas) {
		for (Receta receta : recetas) {
			this.removerReceta(receta);
		}
	}

	public Map<Objeto, List<Receta>> getRecetasPorObjeto() {
		return new HashMap<>(this.recetasPorObjeto);
	}
	
	public Set<Objeto> getObetosProducibles() {
		return new HashSet<Objeto> (recetasPorObjeto.keySet()); 
	}
}
