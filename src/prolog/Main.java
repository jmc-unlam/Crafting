package prolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;

import modelo.Inventario;
import modelo.Objeto;
import modelo.Receta;
import modelo.Recetario;

public class Main {

	private static final String PROLOG_DIR = "prolog/";
	private static final String PROLOG_INVENTARIO = PROLOG_DIR + "inventario.pl";

	public static void main(String[] args) {
		Query q = new Query("consult('prolog/Ejercicio3-Agencia de Viajes.pl').");
		q.hasSolution(); // Cargar el archivo Prolog
		// Crear una consulta
		Query consulta = new Query("viaje(Ciudad,1,Hospedaje, Precio).");
		// viaje(Ciudad,1,Hospedaje, Precio) //predicado(X,Y).
		// Obtener resultados
		while (consulta.hasMoreSolutions()) {
			java.util.Map<String, Term> solucion = consulta.nextSolution();
			System.out.println("Ciudad = " + solucion.get("Ciudad") + " ,Hospedaje = " + solucion.get("Hospedaje")
					+ ", Precio = " + solucion.get("Precio"));
		}
	}

	public static void PrologGenerarRecetas(Recetario recetas) {
		// Borrar Archivo con la lista de Recetas para generarlo nuevamente.
		// Ruta relativa al archivo dentro del proyecto
		File archivo = new File("prolog/recetas.pl");

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
			for (Map.Entry<Objeto, List<Receta>> entry : recetas.getRecetasPorObjeto().entrySet()) {
				Objeto objetosCrafteable = entry.getKey();
				writer.write("ingredientecompuesto('" + objetosCrafteable.getNombre() + "').\n");
			}

			writer.write("\n");
			writer.write("% Ingredientes por objeto\n");

			// Recorrer las recetas y colocar la primera receta para colocar los
			// ingredientes.
			for (Map.Entry<Objeto, List<Receta>> entry : recetas.getRecetasPorObjeto().entrySet()) {
				Objeto objetosCrafteable = entry.getKey();
				List<Receta> listaRecetas = entry.getValue();

				Receta receta = listaRecetas.getFirst(); // Devolver la primera receta.
				Map<Objeto, Integer> ingredientes = receta.getIngredientes();

				for (Map.Entry<Objeto, Integer> ingrediente : ingredientes.entrySet()) {
					Objeto objIngrediente = ingrediente.getKey();
					Integer cantidad = ingrediente.getValue();
					writer.write("ingrediente('" + objetosCrafteable.getNombre() + "', '" + objIngrediente.getNombre()
							+ "', " + cantidad + ").\n");
				}
			}

			writer.close(); // Cerrar el flujo

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void prologGenerarInventario(Inventario inventario) {
		// Ruta relativa al archivo dentro del proyecto
		File archivo = new File(PROLOG_INVENTARIO);

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

			writer.close(); // Cerrar el flujo

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void consultaDeProlog(Inventario inventario) {

		Query q0 = new Query("consult('" + PROLOG_DIR + "config.pl').");
		q0.hasSolution(); // Cargar el archivo Prolog de Configuracion

		Query q = new Query("consult('" + PROLOG_DIR + "recetas.pl').");
		q.hasSolution(); // Cargar el archivo Prolog Recetas

		File archivo = new File(PROLOG_INVENTARIO);

		if (!archivo.exists()) {
			prologGenerarInventario(inventario);
		}

		Query q2 = new Query("consult('" + PROLOG_INVENTARIO + "').");
		q2.hasSolution(); // Cargar el archivo Prolog inventario.

		Query q3 = new Query("consult('" + PROLOG_DIR + "logica.pl').");
		q3.hasSolution(); // Cargar el archivo Prolog con la logica de programación.

		// Crear una consulta
		Query consulta = new Query("posibleCrafteo(Objeto).");

		System.out.println("--Objetos crafteables con el inventario actual:");
		// Obtener resultados
		while (consulta.hasMoreSolutions()) {
			java.util.Map<String, Term> solucion = consulta.nextSolution();
			System.out.println("Objeto = " + solucion.get("Objeto"));

		}
	}
}
