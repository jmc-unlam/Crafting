package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import main.Config;

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
		if (!objeto.esBasico()) {
			if (!visitados.add(objeto))
				return true;
		}
		Receta receta;
		try {
			receta = this.buscarReceta(objeto);
		} catch (NoSuchElementException e) {
			// No hay receta todavia entonces no hay ciclo
			return false;
		}
		for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
			if (!entry.getKey().esBasico()) {
				if (detectarCiclo(entry.getKey(), visitados))
					return true;
			}
		}
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
		if (recetas == null || !recetas.remove(receta)) {
			throw new IllegalArgumentException("La receta no existe en el recetario");
		}
		// Eliminar la clave si la lista queda vacía
		if (recetas.isEmpty()) {
			recetasPorObjeto.remove(receta.getObjetoProducido());
		}
	}

	/**
     * Devuelve una lista con todas las recetas almacenadas.
     * 
     * @return Lista de recetas.
     */
	public List<Receta> getRecetas() {
		List<Receta> todas = new ArrayList<>();
		for (List<Receta> lista : recetasPorObjeto.values()) {
			todas.addAll(lista);
		}
		return todas; // Devuelve toda las recetas.
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

	/**
	 * Genera el archivo con todas las recetas, indicando Ingredientes , objetos crafteable y objetos
	 * No Apilables como las mesas. Pero es posible extender esta funcionalidad a otros objetos si no son apilables.
	 */
	public void prologGenerarRecetas() {
		// Borrar Archivo con la lista de Recetas para generarlo nuevamente.
		// Ruta relativa al archivo dentro del proyecto
		File archivo = new File(Config.RUTA_PROLOG_RECETAS);

		List<String> mesas = new LinkedList<String>();

		// Verificar si el archivo existe
		if (archivo.exists()) {
			// Intentar eliminarlo
			archivo.delete();
		}

		try {

			archivo.createNewFile();

			// Escribir texto en el archivo
			FileWriter writer = new FileWriter(archivo);
			writer.write("% Recetas\n");
			writer.write("% Lista de Objetos Crafteables \n");

			// Recorrer las recetas y colocar los ingredientes compuestos.
			for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
				Objeto objetosCrafteable = entry.getKey();
				writer.write("ingredientecompuesto('" + objetosCrafteable.getNombre() + "').\n");
			}

			writer.write("\n");
			writer.write("% Ingredientes por objeto\n");

			// Recorrer las recetas y colocar la primera receta para colocar los
			// ingredientes.
			for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
				Objeto objetosCrafteable = entry.getKey();
				List<Receta> listaRecetas = entry.getValue();

				if (!objetosCrafteable.esApilable()) {
					mesas.add(objetosCrafteable.getNombre());
				}

				Receta receta = listaRecetas.getFirst(); // Devolver la primera receta.
				Map<Objeto, Integer> ingredientes = receta.getIngredientes();

				for (Map.Entry<Objeto, Integer> ingrediente : ingredientes.entrySet()) {
					Objeto objIngrediente = ingrediente.getKey();
					Integer cantidad = ingrediente.getValue();
					writer.write("ingrediente('" + objetosCrafteable.getNombre() + "', '" + objIngrediente.getNombre()
							+ "', " + cantidad + ").\n");
				}
			}

			writer.write("\n");
            writer.write("% Mesas\n");
        
            mesas.add("Mesa Default");
            for (String string : mesas) {
				writer.write("no_apilable('" + string + "').\n"); 
			}
			
			writer.close(); // Cerrar el flujo

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Crea un listado con los objetos Crafteables o para comprarlos.
	 * 
	 * Y los muestra en el orden indicando un Nro para poder seleccionarlo en las funcionalidades q lo necesiten.
	 * 
	 * @return Lista Crafteable-Comprable.
	 */
	public List<Objeto> listaCrafteable() {
		// Devuelve el listado de Objetos Farmeables.
		List<Objeto> listaObjetos = new ArrayList<Objeto>();

		for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
			Objeto objetosCrafteable = entry.getKey();
			listaObjetos.add(objetosCrafteable);
		}

		this.mostrarListaObjetos(listaObjetos, "--Lista de Objetos a Craftear.");

		return listaObjetos;
	}

	/**
	 * Centralizado la muestra de las Listas de listado de objetos y las muestra por pantalla
	 * colocando un Nro para su seleccionar en la interfaz
	 * 
	 * @param listaObjetos Listado de Objetos
	 * @param mesanje	Mensaje Inicial indicando la naturaleza de los Objetos.
	 */
	private void mostrarListaObjetos(List<Objeto> listaObjetos, String mesanje) {
		
		if (listaObjetos.size() > 0) {// crea el listado de objetos crafteables con un Nr para identificarlo
			System.out.println(mesanje);
			int id = 1;
			for (Objeto objeto : listaObjetos) {
				System.out.println("NrID°:" + id + "-" + objeto);
				id++;
			}
			System.out.println("\n");
		} else
			System.out.println("No hay objetos en la lista.\n");
	}
	
	/**
     * Devuelve una lista de objetos básicos utilizados como ingredientes en recetas.
     * 
     * @return Lista de objetos básicos (ingredientes).
     */
	public List<Objeto> listaObjetosRecolectables() {
		// Devuelve la lista de Objetos Basicos.
		Set<Objeto> listaObjetosOri = new LinkedHashSet<Objeto>();

		for (Map.Entry<Objeto, List<Receta>> listaRecetas : recetasPorObjeto.entrySet()) {
			List<Receta> recetasObjeto = listaRecetas.getValue();

			for (Receta receta : recetasObjeto) {
				Map<Objeto, Integer> ingredientes = receta.getIngredientes();
				for (Map.Entry<Objeto, Integer> objBuscandoBasicos : ingredientes.entrySet()) {
					Objeto key = objBuscandoBasicos.getKey();

					if (key.esBasico())
						listaObjetosOri.add(key);
				}
			}

		}

		List<Objeto> listaObjetos = new ArrayList<>(listaObjetosOri);
		
		this.mostrarListaObjetos(listaObjetos, "--Lista de Objetos a Farmeables.");

		return listaObjetos;
	}

	public Objeto objetoCrafteable(String nombre) {
		for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
			Objeto objBuscado = entry.getKey();
			if (objBuscado.getNombre().equals(nombre))
				return objBuscado;
		}
		System.out.println("no encontro un *" + nombre + "* con ese nombre.");
		return null;
	}

}
