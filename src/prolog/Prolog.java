package prolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jpl7.Query;
import org.jpl7.Term;

import main.Config;
import modelo.Inventario;
import modelo.Objeto;
import modelo.Receta;
import modelo.Recetario;

public class Prolog {

	private Prolog() {
		throw new AssertionError("No se puede instanciar");
	}
	
	/**
	 * Centralizado la muestra de las Listas de listado de objetos y las muestra por pantalla
	 * colocando un Nro para su seleccionar en la interfaz
	 * 
	 * @param listaObjetos Listado de Objetos
	 * @param mesanje	Mensaje Inicial indicando la naturaleza de los Objetos.
	 */
	private static void mostrarListaObjetos(List<Objeto> listaObjetos, String mesanje) {
		
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
	 * Genera el archivo de formato pl en la carpeta de prolog con todos los objetos y sus cantidades en el inventario.
	 */
	public static void generarInventario(Inventario inventario) {
		// Ruta relativa al archivo dentro del proyecto
		File archivo = new File(Config.RUTA_PROLOG_INVENTARIO);

		// Verificar si el archivo existe
		if (archivo.exists()) {
			// Intentar eliminarlo
			archivo.delete();
		}

		try {

			archivo.createNewFile();

			// Escribir texto en el archivo
			FileWriter writer = new FileWriter(archivo);
			writer.write("% Objetos en el inventario\n");

			// Recorrer el inventario y generar el archivo prolog con las cantidades de los
			// objetos.
			for (Map.Entry<Objeto, Integer> item : inventario.getObjetos().entrySet()) {
				Objeto obj = item.getKey();
				Integer cantidad = item.getValue();
				writer.write("inventario('" + obj.getNombre() + "'," + cantidad + ").\n");
			}
			
			//Caso particular para evitar errores en el prolog al no encontrar ningun objeto en el inventario.
			writer.write("inventario('Objeto Default', 0).\n");
			
			writer.close(); // Cerrar el flujo

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método para mostrar la consulta de Prolog.
	 *
	 * Punto del TP2:¿Cuáles son todos los productos que podría generar con el inventario actual?
	 * IMPORTANTE: Esta version NO crea los archivos del inventario y recetario. Esos archivos, se generan
	 * en Metodos aparte.
	 * 
	 * */
	public static void consulta(Inventario inventario) {

		Query queryConfig = new Query("consult('" + Config.RUTA_PROLOG_CONFIG + "').");
		queryConfig.hasSolution(); // Cargar el archivo Prolog de Configuracion

		Query queryRecetas = new Query("consult('" + Config.RUTA_PROLOG_RECETAS + "').");
		queryRecetas.hasSolution(); // Cargar el archivo Prolog Recetas

		File archivo = new File(Config.RUTA_PROLOG_INVENTARIO);

		if (!archivo.exists()) {
			generarInventario(inventario);
		}

		Query queryInventario = new Query("consult('" + Config.RUTA_PROLOG_INVENTARIO + "').");
		queryInventario.hasSolution(); // Cargar el archivo Prolog inventario.

		Query queryLogica = new Query("consult('" + Config.RUTA_PROLOG_LOGICA + "').");
		queryLogica.hasSolution(); // Cargar el archivo Prolog con la logica de programación.

		// Crear una consulta
		Query consulta = new Query("posibleCrafteo(Objeto).");

		System.out.println("\n--PROLOG:");
		System.out.println("--Objetos crafteables con el inventario actual:");
		// Obtener resultados
		while (consulta.hasMoreSolutions()) {
			java.util.Map<String, Term> solucion = consulta.nextSolution();
			System.out.println("Objeto = " + solucion.get("Objeto"));

		}
	}
	
	/**
	 * Método para restar o quitar objetos del inventario según su nro en el inventario y la cantidad.
	 * Anteriormente creado en el main, sin embargo, el main debia calculas cosas q el inventario las hace internamente.
	 * 
	 * @param opcionIDObjeto Nro del objeto a vender.
	 * @param cantidadAVender  Cantidad a vender.
	 * @param recetario  usado para quitar las recetas si el objeto a vender es una mesa.
	 * @return si la operacion fue realizada con exito.
	 */
	public static boolean removerCantidadDeUnObjetoSegunNro(int opcionIDObjeto, int cantidadAVender, Recetario recetario, Inventario inventario) {
		
		Map<Objeto, Integer> objetos = inventario.getObjetos();
		if (opcionIDObjeto > objetos.size() || opcionIDObjeto < 0 || cantidadAVender==0) {
			System.out.println("Entrada inválida. Por favor, elige un número de la list y una cantidad igual o menor que la del inventario.");
			return false;
		} else {

			int nroOrden = 1;
			nroOrden = 1;
			for (Map.Entry<Objeto, Integer> entry : objetos.entrySet()) {
				Objeto objetoEnInventario = entry.getKey();
				Integer cantidadEnInventario = entry.getValue();
				if (nroOrden == opcionIDObjeto) {
					if (cantidadEnInventario >= cantidadAVender) {
						inventario.removerObjeto(objetoEnInventario, cantidadAVender, recetario);
						//recetario.removerRecetas(objetoEnInventario.listaDeRecetasPropias()); no es necesario
						System.out.println(objetoEnInventario + " VENDIDA\n");
						return true;
					} else {
						System.out.println("La cantidad " + cantidadAVender
								+ " a vender es mayor a la del inventario q es . " + cantidadEnInventario + "\n");
					}
					return false;
				}
				nroOrden++;
			}
			return false;
		}
	}
	
	/**
	 * Genera el archivo con todas las recetas, indicando Ingredientes , objetos crafteable y objetos
	 * No Apilables como las mesas. Pero es posible extender esta funcionalidad a otros objetos si no son apilables.
	 */
	public static void generarRecetas(Recetario recetario) {
		Map<Objeto, List<Receta>> recetasPorObjeto = recetario.getRecetasPorObjeto();
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
	public static List<Objeto> listaCrafteable(Recetario recetario) {
		Map<Objeto, List<Receta>> recetasPorObjeto = recetario.getRecetasPorObjeto();
		// Devuelve el listado de Objetos Farmeables.
		List<Objeto> listaObjetos = new ArrayList<Objeto>();

		for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
			Objeto objetosCrafteable = entry.getKey();
			listaObjetos.add(objetosCrafteable);
		}

		Collections.sort(listaObjetos);
		
		mostrarListaObjetos(listaObjetos, "--Lista de Objetos a Craftear.");
		
		return listaObjetos;
	}

	/**
     * Devuelve una lista de objetos básicos utilizados como ingredientes en recetas.
     * 
     * @return Lista de objetos básicos (ingredientes).
     */
	public static List<Objeto> listaObjetosRecolectables(Recetario recetario) {
		Map<Objeto, List<Receta>> recetasPorObjeto = recetario.getRecetasPorObjeto();
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
		
		Collections.sort(listaObjetos);
		
		mostrarListaObjetos(listaObjetos, "--Lista de Objetos a Farmeables.");

		return listaObjetos;
	}
}
