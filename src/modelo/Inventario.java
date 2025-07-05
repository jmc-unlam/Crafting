package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpl7.Query;
import org.jpl7.Term;

import main.Config;

/**
 * Representa el inventario del jugador, gestionando los objetos disponibles y su cantidad.
 * Actúa como un cliente del patrón Composite, realizando operaciones recursivas sobre objetos básicos y compuestos.
 * Simular el consumo de ingredientes necesarios para fabricar un objeto crafteable.
 * Determinar cuántas veces puede repetirse un crafteo dado el inventario actual.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class Inventario {
	private Map<Objeto, Integer> objetos;

	/**
     * Constructor que inicializa un inventario vacío.
     */
	public Inventario() {
		this.objetos = new HashMap<>(); // no garantiza orden. Orden mutable si es HashMap
	}

	/**
     * Constructor que inicializa el inventario con una colección de objetos y sus cantidades.
     * 
     * @param objetosIniciales Mapa de objetos iniciales y sus cantidades.
     */
	public Inventario(Map<Objeto, Integer> objetosIniciales) {
		this.objetos = new HashMap<>();
		for (Map.Entry<Objeto, Integer> entry : objetosIniciales.entrySet()) {
			this.agregarObjeto(entry.getKey(), entry.getValue());
		}
	}

	/**
     * Constructor que inicializa el inventario y carga las recetas asociadas si se agrega una mesa de trabajo.
     * 
     * @param objetosIniciales Mapa de objetos iniciales y sus cantidades.
     * @param recetario Recetario donde se registrarán las recetas asociadas a mesas de trabajo.
     */
	public Inventario(Map<Objeto, Integer> objetosIniciales, Recetario recetario) {
		this.objetos = new HashMap<>();
		for (Map.Entry<Objeto, Integer> entry : objetosIniciales.entrySet()) {
			this.agregarObjeto(entry.getKey(), entry.getValue(),recetario);
		}
	}
	
	/**
     * Agrega un objeto al inventario.
     * Si el objeto no es apilable y ya está presente, lanza una excepción.
     * 
     * @param objeto Objeto a agregar.
     * @param cantidad Cantidad del objeto a agregar.
     * @throws IllegalArgumentException Si la cantidad es negativa o cero.
     * @throws IllegalArgumentException Si ya existe y no es apilable.
     * @throws IllegalArgumentException Si hay mas de uno y no es apilable.  
     */
	public void agregarObjeto(Objeto objeto, int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser positiva");
		}
		if (!objeto.esApilable()) { // Aquí usamos polimorfismo con esApilable()
			if (objetos.containsKey(objeto)) {
				throw new IllegalArgumentException("El objeto  no es apilable:" + objeto);
			}
			if (cantidad != 1)
				throw new IllegalArgumentException(
						"El objeto  no es apilable, solo admite una cantidad de 1:" + objeto);
		}
		objetos.merge(objeto, cantidad, Integer::sum);
	}
	
	/**
     * Agrega un objeto al inventario y activa sus recetas si corresponde (ej.: mesas de trabajo).
     * 
     * @param objeto Objeto a agregar.
     * @param cantidad Cantidad del objeto a agregar.
     * @param recetario Recetario donde se registrarán las recetas asociadas.
     * @throws IllegalArgumentException Si la cantidad es negativa o cero.
     * @throws IllegalArgumentException Si ya existe y no es apilable.
     * @throws IllegalArgumentException Si hay mas de uno y no es apilable. 
     */
	public void agregarObjeto(Objeto objeto, int cantidad, Recetario recetario) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser positiva");
		}
		if (!objeto.esApilable()) { // Aquí usamos polimorfismo con esApilable()
			if (objetos.containsKey(objeto)) {
				throw new IllegalArgumentException("El objeto  no es apilable:" + objeto);
			}
			if (cantidad != 1)
				throw new IllegalArgumentException(
						"El objeto  no es apilable, solo admite una cantidad de 1:" + objeto);
		}
		objetos.merge(objeto, cantidad, Integer::sum);
		objeto.listaDeRecetasPropias(recetario);
	}

	public void agregarObjetos(Map<Objeto, Integer> objetos) {
		for (Map.Entry<Objeto, Integer> obj : objetos.entrySet()) {
			agregarObjeto(obj.getKey(), obj.getValue());
		}
	}
	
	public void agregarObjetos(Map<Objeto, Integer> objetos, Recetario recetario) {
		for (Map.Entry<Objeto, Integer> obj : objetos.entrySet()) {
			agregarObjeto(obj.getKey(), obj.getValue(), recetario);
		}
	}
	
	/**
     * Remueve una cantidad específica de un objeto del inventario.
     * Si el objeto no tiene suficiente cantidad disponible, lanza una excepción.
     * 
     * @param objeto Objeto a remover.
     * @param cantidad Cantidad a remover.
     * @throws IllegalArgumentException Si cantidad a remover es negativa o cero.
     * @throws IllegalArgumentException Si la cantidad a remover es mayor a lo que tienes.
     */
	public void removerObjeto(Objeto objeto, int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
		}

		int cantidadActual = objetos.getOrDefault(objeto, 0);
		if (cantidadActual < cantidad) {
			throw new IllegalArgumentException("No hay suficiente cantidad de [" + objeto + "]en el inventario.");
		}

		int nuevaCantidad = cantidadActual - cantidad;
		if (nuevaCantidad == 0) {
			objetos.remove(objeto); // Eliminar completamente si llega a cero
		} else {
			objetos.put(objeto, nuevaCantidad);
		}
	}

	/**
     * Remueve una cantidad específica de un objeto del inventario y desactiva sus recetas si corresponde.
     * 
     * @param objeto Objeto a remover.
     * @param cantidad Cantidad a remover.
     * @param recetario Recetario donde se eliminarán las recetas asociadas.
     * @throws IllegalArgumentException Si cantidad a remover es negativa o cero.
     * @throws IllegalArgumentException Si la cantidad a remover es mayor a lo que tienes.
     */
	public void removerObjeto(Objeto objeto, int cantidad, Recetario recetario) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
		}

		int cantidadActual = objetos.getOrDefault(objeto, 0);
		if (cantidadActual < cantidad) {
			throw new IllegalArgumentException("No hay suficiente cantidad de [" + objeto + "]en el inventario.");
		}

		int nuevaCantidad = cantidadActual - cantidad;
		if (nuevaCantidad == 0) {
			objetos.remove(objeto); // Eliminar completamente si llega a cero
			objeto.removerRecetasPropias(recetario);
		} else {
			objetos.put(objeto, nuevaCantidad);
		}
	}

	/**
     * Devuelve una copia de los objetos en el inventario con sus cantidades.
     * 
     * @return Mapa de objetos y sus cantidades.
     */
	public Map<Objeto, Integer> getObjetos() {
		return new HashMap<>(objetos); // crea una copia del original.
	}

	/**
     * Calcula qué ingredientes faltan para completar un conjunto de requerimientos.
     * 
     * @param requeridos Mapa de objetos y sus cantidades mínimas requeridas.
     * @return Mapa de objetos faltantes y sus cantidades necesarias.
     */
	public Map<Objeto, Integer> getFaltantes(Map<Objeto, Integer> requeridos) {
		Map<Objeto, Integer> faltantes = new HashMap<>();

		for (Map.Entry<Objeto, Integer> entry : requeridos.entrySet()) {
			Objeto objeto = entry.getKey();
			int cantidadRequerida = entry.getValue();
			int cantidadEnInventario = this.getCantidad(objeto);

			if (cantidadRequerida > cantidadEnInventario) {
				int faltante = cantidadRequerida - cantidadEnInventario;
				faltantes.put(objeto, faltante);
			}
		}
		return faltantes;
	}

	/**
     * Calcula qué ingredientes básicos faltan para completar un conjunto de requerimientos,
     * considerando la posibilidad de craftear los intermedios necesarios.
     * 
     * @param requeridosBasicos Mapa de ingredientes básicos y sus cantidades mínimas requeridas.
     * @param recetario Recetario que provee las recetas para calcular ingredientes compuestos.
     * @return Mapa de ingredientes básicos faltantes y sus cantidades necesarias.
     */
	public Map<Objeto, Integer> getFaltantesBasicos(Map<Objeto, Integer> requeridosBasicos, Recetario recetario) {
		Map<Objeto, Integer> faltantesBasicos = new HashMap<>();

		for (Map.Entry<Objeto, Integer> entry : requeridosBasicos.entrySet()) {
			Objeto ingrediente = entry.getKey();
			int cantidadNecesaria = entry.getValue();

			int disponible = getCantidadBasico(ingrediente, recetario);

			if (disponible < cantidadNecesaria) {
				faltantesBasicos.put(ingrediente, cantidadNecesaria - disponible);
			}
		}
		return faltantesBasicos;
	}

	/**
     * Devuelve la cantidad disponible de un objeto en el inventario.
     * 
     * @param objeto Objeto a consultar.
     * @return Cantidad disponible del objeto.
     */
	public int getCantidad(Objeto objeto) {
		return objetos.getOrDefault(objeto, 0);
	}

	/**
     * Devuelve la cantidad total disponible de un objeto, incluyendo lo que podría craftearse.
     * Si el objeto es básico, devuelve la cantidad directamente.
     * Si es compuesto, consulta su receta y calcula cuánto puede producirse según los ingredientes disponibles.
     * 
     * @param objeto Objeto a evaluar.
     * @param recetario Recetario que provee las recetas para resolver dependencias.
     * @return Cantidad total disponible del objeto.
     */
	public int getCantidadBasico(Objeto objeto, Recetario recetario) {
		// Funcion Recursiva. Devulve la cantidad
		if (objeto.esBasico()) {
			return getCantidad(objeto);
		} else {
			Receta receta = recetario.buscarReceta(objeto);

			int cantidadTotalDisponible = getCantidad(objeto);
			int numLotesCrafteables = Integer.MAX_VALUE;

			for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
				Objeto ingrediente = entry.getKey();
				int cantidadNecesariaDelIngrediente = entry.getValue();
				int cantidadDisponibleDelIngrediente = getCantidadBasico(ingrediente, recetario);

				int lotesPosibles = cantidadDisponibleDelIngrediente / cantidadNecesariaDelIngrediente;

				// Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más
				// escaso
				numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
			}

			cantidadTotalDisponible += numLotesCrafteables * receta.getCantidadProducida();

			return cantidadTotalDisponible;
		}
	}

//	// *****Implementacion Recetas Alternativas*************
//	public Map<Objeto, Integer> getFaltantesBasicos(Map<Objeto, Integer> requeridosBasicos, Recetario recetario,
//			int indiceReceta) {
//		Map<Objeto, Integer> faltantesBasicos = new HashMap<>();
//
//		for (Map.Entry<Objeto, Integer> entry : requeridosBasicos.entrySet()) {
//			Objeto ingrediente = entry.getKey();
//			int cantidadNecesaria = entry.getValue();
//
//			int disponible = getCantidadBasico(ingrediente, recetario, indiceReceta);
//
//			if (disponible < cantidadNecesaria) {
//				faltantesBasicos.put(ingrediente, cantidadNecesaria - disponible);
//			}
//		}
//		return faltantesBasicos;
//	}
//
//	public int getCantidadBasico(Objeto objeto, Recetario recetario, int indiceReceta) {
//		if (objeto.esBasico()) {
//			return getCantidad(objeto);
//		} else {
//			List<Receta> recetas = recetario.buscarRecetas(objeto);
//
//			int cantidadTotalDisponible = getCantidad(objeto);
//			int numLotesCrafteables = Integer.MAX_VALUE;
//
//			for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
//				Objeto ingrediente = entry.getKey();
//				int cantidadNecesariaDelIngrediente = entry.getValue();
//				int cantidadDisponibleDelIngrediente = getCantidadBasico(ingrediente, recetario);
//
//				int lotesPosibles = cantidadDisponibleDelIngrediente / cantidadNecesariaDelIngrediente;
//
//				// Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más
//				// escaso
//				numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
//			}
//
//			cantidadTotalDisponible += numLotesCrafteables * recetas.get(indiceReceta).getCantidadProducida();
//
//			return cantidadTotalDisponible;
//		}
//	}

	// *****Implementacion Mesas de Trabajo*************

	/**
     * Determina si el inventario contiene una mesa de trabajo específica.
     * Las mesas son únicas y no apilables, por lo tanto solo pueden estar presentes o ausentes.
     * 
     * @param mesa Mesa de trabajo a verificar.
     * @return true si posee la mesa, false en caso contrario.
     */
	public boolean tieneMesa(MesaDeTrabajo mesa) {
		return (mesa == null) ? true : objetos.containsKey(mesa);
	}

	/**
     * Devuelve una representación en texto del contenido actual del inventario.
     * 
     * @return Cadena descriptiva del inventario.
     */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("=== Inventario ===\n");
		int nroId = 1;
		for (Map.Entry<Objeto, Integer> entry : objetos.entrySet()) {
			sb.append("Nro-" + nroId + "-").append(entry).append("\n");
			nroId++;
		}
		return sb.toString();
	}

	// *****Implementacion Prolog*************

	/**
	 * Genera el archivo de formato pl en la carpeta de prolog con todos los objetos y sus cantidades en el inventario.
	 */
	public void prologGenerarInventario() {
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
			for (Map.Entry<Objeto, Integer> item : objetos.entrySet()) {
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
	public void consultaDeProlog() {

		Query queryConfig = new Query("consult('" + Config.RUTA_PROLOG_CONFIG + "').");
		queryConfig.hasSolution(); // Cargar el archivo Prolog de Configuracion

		Query queryRecetas = new Query("consult('" + Config.RUTA_PROLOG_RECETAS + "').");
		queryRecetas.hasSolution(); // Cargar el archivo Prolog Recetas

		File archivo = new File(Config.RUTA_PROLOG_INVENTARIO);

		if (!archivo.exists()) {
			this.prologGenerarInventario();
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
	 *Calcula cuántas veces se puede realizar un crafteo completo de un objeto compuesto,
     *basándose en los ingredientes disponibles y las recetas definidas.
     *
     *Este método simula el consumo total necesario, incluyendo ingredientes anidados,
     *y devuelve un resultado que incluye la cantidad posible y el tiempo total estimado.
     * 
	 *Tecnico: Este metodo usa la funciona recursiva cantidadRecursivaObjeto
	 *Pero antes de invocarla, hace una copia del inventario actual y va descontando los materiales a utilizar
	 *de este inventario aux. Quitando primero los Objetos Intermedios y luego si no existen los objetos que lo componen,
	 *esto permite detectar la cantidad emulando los crafteaos usando diferentes formas.
	 *
	 *
	 * @param objCrafteable Objeto a craftear.
	 * @param recetario Recetario que proporciona acceso a las recetas necesarias.
	 * @throws UnsupportedOperationException Si es un objeto basico.
	 * @throws IllegalArgumentException Si no tiene receta asociada.
	 * @throws UnsupportedOperationException Si no tiene la mesa necesaria / El objeto es nulo.
	 * @return Resultado Clase Resultado con la información de (Cantidad producida, objeto y Tiempo)
	 */
	public Resultado cantidadPosibleCraftear(Objeto objCrafteable, Recetario recetario) {
		// El metodo en inventario que devuelve en una Clase Resultado (Cantidad a
				// producir + TiempoTotal)
		
		if(objCrafteable == null) {
			throw new UnsupportedOperationException("El objeto es nulo. Comprueba si el nombre es válido.");
		}
		
		if (objCrafteable.esBasico()) {
			throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objCrafteable);
		}

		Receta receta = recetario.buscarReceta(objCrafteable);
		if (receta == null) {
			throw new IllegalStateException("No existe receta para craftear " + objCrafteable);
		}
		// verificar si la receta tiene mesa
		if (!this.tieneMesa(receta.getMesaRequerida())) {
			throw new UnsupportedOperationException(
					"No tienes [" + receta.getMesaRequerida() + "] para craftear->" + objCrafteable);
		}
		// verificar si se puede apilar
		if (this.getCantidad(objCrafteable) >= 1 && !objCrafteable.esApilable()) {
			// throw new UnsupportedOperationException("No se puede crafear porque ya lo
			// tienes, no es apilable: " + ObjCrafteable);
			return new Resultado(0, 0, objCrafteable);
		}

		// Caso un. Un objeto simple , con materiales basico.
		// La division puede ser viable, sin embargo se piede información con los
		// Objetos Intermedios.
		// La solucion de francisco me parece correcta. ir con un contador multiplicando
		// las veces que se craftea de a 1.
		// pero llevarlo a la practica eso es otra cosa.

		// Crear CLONE del inventario. para ir restado al Aux y ver q me queda.
		Inventario inventarioAux = new Inventario(objetos);

		int cantEjecuciones = 0;
		int tiempoAcumulado = 0;
		int totaltiempo = 0;

		boolean senCorte = true;
		// Descompongo el objeto sus ingredientes y miro si los tengo en el inventario.
		// Si tengo todos los ingredientes. Voy restando al inventario y incremento el
		// contador de Lotes.
		// Si no tengo todos los ingredientes basicos o me falta alguno para poder hacer
		// el crafteo .

		do {
			// si tengo todos los materiales para cratear. cantEjecuciones++ y salgo
			// Si encuentro un material Basico que me falta en cantidad. salgo
			for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
				Objeto ingrediente = entry.getKey();
				int cantidadNecesaria = entry.getValue();

				Resultado resIngrediente = cantidadRecursivaObjeto(inventarioAux, ingrediente, cantidadNecesaria,
						recetario);

				if (resIngrediente.getCantidadCrafteable() == 0) {
					// si la cantidad crafteada del resulta es cero, CORTA. por que signifca q ese
					// ingrediente
					// no se pudo crear en la cantidad necesaria.
					senCorte = false;
					break; // salgo del for - un ingrediente Intermedio no es posible de craftear
				} else {
					tiempoAcumulado += resIngrediente.getTiempo(); // acumula los tiempos de los crafteos intermedios
				}
			}

			if (senCorte) { // si es true, significa q se salio exitosamente del for.
				cantEjecuciones++;
				totaltiempo += tiempoAcumulado; // acumula el tiempo que tardo en craftear esta ejecucio.
				tiempoAcumulado = 0;
				if (!objCrafteable.esApilable() && cantEjecuciones == 1)
					senCorte = false; // si no es apilable, cuando es uno sale.
			}
		} while (senCorte);

		return new Resultado(cantEjecuciones * receta.getCantidadProducida(),
				totaltiempo + receta.getTiempoBase() * cantEjecuciones, objCrafteable);
	}
	
	public Resultado cantidadPosibleCraftear(Objeto objCrafteable, Recetario recetario, int indiceReceta) {
		
		// El metodo en inventario que devuelve en una Clase Resultado (Cantidad a
		// producir + TiempoTotal)
		if (objCrafteable.esBasico()) {
			throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objCrafteable);
		}

		List<Receta> recetas = recetario.buscarRecetas(objCrafteable);
		if (recetas == null) {
			throw new IllegalStateException("No existe receta para craftear " + objCrafteable);
		}
		// verificar si la receta tiene mesa
		if (!this.tieneMesa(recetas.get(indiceReceta).getMesaRequerida())) {
			throw new UnsupportedOperationException(
					"No tienes [" + recetas.get(indiceReceta).getMesaRequerida() + "] para craftear->" + objCrafteable);
		}
		// verificar si se puede apilar
		if (this.getCantidad(objCrafteable) >= 1 && !objCrafteable.esApilable()) {
			// throw new UnsupportedOperationException("No se puede crafear porque ya lo
			// tienes, no es apilable: " + ObjCrafteable);
			return new Resultado(0, 0, objCrafteable);
		}

		// Caso un. Un objeto simple , con materiales basico.
		// La division puede ser viable, sin embargo se piede información con los
		// Objetos Intermedios.
		// La solucion de francisco me parece correcta. ir con un contador multiplicando
		// las veces que se craftea de a 1.
		// pero llevarlo a la practica eso es otra cosa.

		// Crear CLONE del inventario. para ir restado al Aux y ver q me queda.
		Inventario inventarioAux = new Inventario(objetos);

		int cantEjecuciones = 0;
		int tiempoAcumulado = 0;
		int totaltiempo = 0;

		boolean senCorte = true;
		// Descompongo el objeto sus ingredientes y miro si los tengo en el inventario.
		// Si tengo todos los ingredientes. Voy restando al inventario y incremento el
		// contador de Lotes.
		// Si no tengo todos los ingredientes basicos o me falta alguno para poder hacer
		// el crafteo .

		do {
			// si tengo todos los materiales para cratear. cantEjecuciones++ y salgo
			// Si encuentro un material Basico que me falta en cantidad. salgo
			for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
				Objeto ingrediente = entry.getKey();
				int cantidadNecesaria = entry.getValue();

				Resultado resIngrediente = cantidadRecursivaObjeto(inventarioAux, ingrediente, cantidadNecesaria,
						recetario);

				if (resIngrediente.getCantidadCrafteable() == 0) {
					// si la cantidad crafteada del resulta es cero, CORTA. por que signifca q ese
					// ingrediente
					// no se pudo crear en la cantidad necesaria.
					senCorte = false;
					break; // salgo del for - un ingrediente Intermedio no es posible de craftear
				} else {
					tiempoAcumulado += resIngrediente.getTiempo(); // acumula los tiempos de los crafteos intermedios
				}
			}

			if (senCorte) { // si es true, significa q se salio exitosamente del for.
				cantEjecuciones++;
				totaltiempo += tiempoAcumulado; // acumula el tiempo que tardo en craftear esta ejecucio.
				tiempoAcumulado = 0;
				if (!objCrafteable.esApilable() && cantEjecuciones == 1)
					senCorte = false; // si no es apilable, cuando es uno sale.
			}
		} while (senCorte);

		return new Resultado(cantEjecuciones * recetas.get(indiceReceta).getCantidadProducida(),
				totaltiempo + recetas.get(indiceReceta).getTiempoBase() * cantEjecuciones, objCrafteable);
	}

	/**
     *Método auxiliar recursivo que ayuda a calcular cuánto de un ingrediente es realmente utilizable.
     *Si el ingrediente es compuesto, consulta su receta y simula cuánto puede producirse.
     * 
     * @param invAux Inventario auxiliar usado durante la simulación.
     * @param objetoConsultar Objeto del cual se quiere conocer disponibilidad real.
     * @param cantidadNecesaria Cantidad mínima deseada del objeto.
     * @param recetario Recetario que provee las recetas necesarias.
     * @return Resultado que indica cuánto se pudo obtener y el tiempo total invertido.
     */
	private Resultado cantidadRecursivaObjeto(Inventario invAux, Objeto objetoConsultar, int cantidadNecesariaDelObjeto,
			Recetario recetario) {
		int tiempoAcumulado = 0;
		int cantidadCrafteadaTotal = 0;

		if (objetoConsultar.esBasico()) {// condicion de corte si es basico.
			if (cantidadNecesariaDelObjeto <= invAux.getCantidad(objetoConsultar)) {
				invAux.removerObjeto(objetoConsultar, cantidadNecesariaDelObjeto);
				return new Resultado(-1, 0); // devuevel -1,0. por q el objeto es basico y habia esa cantidad en el
												// invAUX
			} else {
				return new Resultado(0, 0); // Devuelve 0,0 por q la cantidad no hay en el invAUX
			}
		} else {
			if (cantidadNecesariaDelObjeto <= invAux.getCantidad(objetoConsultar)) {
				// Quita las cantidades del objeto intermedio del inventarioAux.
				invAux.removerObjeto(objetoConsultar, cantidadNecesariaDelObjeto);
				return new Resultado(-1, 0); // devuevel -1,0. por q el objeto es intermedio y habia esa cantidad en el
												// invAUX - no se crafteo nada.
			} else {
				// No hay o es menor a la necesitada. //calcula faltante para crear lo q
				// necesita con el inventario. Actual.
				int cantidadFaltante = cantidadNecesariaDelObjeto - invAux.getCantidad(objetoConsultar);
				// no tengo la cantidad del objeto.
				// busco la receta para corroborar si tengo tengo los basicos o necesito seguir
				// abriendo.
				Receta receta = recetario.buscarReceta(objetoConsultar);

				// Cantidad de veces q se debe ejecutar el crafteo para lograr la cantidad
				// necesaria.
				int vecesReceta = Math.ceilDiv(cantidadFaltante, receta.getCantidadProducida());
				Objeto ingrediente = null;

				for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
					ingrediente = entry.getKey();
					int cantidadNecesaria = entry.getValue() * vecesReceta; // cantidad de materiales total necesario.

					Resultado resCrafteo = cantidadRecursivaObjeto(invAux, ingrediente, cantidadNecesaria, recetario);

					if (resCrafteo.getCantidadCrafteable() == 0) {
						// Una receta no puede hacerce o un componente base no tiene la cantidad.
						return new Resultado(0, 0); // devuelve cantidad y tiempo cero.
					} else {
						// devuelve cantidad diferente a 0 //algo se crafteo o es basico.
						// si es basico, no tiene tiempo.
						// si es intermedio q esta en el inventario , no tiene tiempo
						tiempoAcumulado += resCrafteo.getTiempo();
					}
				}
				// si sale del for, es q todos los ingredientes hay en el inventerio en forma de
				// mat. bases.
				tiempoAcumulado += receta.getTiempoBase() * vecesReceta; // se le agrega el tiempo de la receta.
				cantidadCrafteadaTotal += receta.getCantidadProducida() * vecesReceta;
				// Agrega al inventario Aux, los objetos crafteado y resta lo utilizado.
				if ((cantidadCrafteadaTotal - cantidadFaltante) > 0)
					// Agrega solo cuando la diferencia es positiva. No agrega CERO.
					invAux.agregarObjeto(objetoConsultar, cantidadCrafteadaTotal - cantidadFaltante);
			}
		}

		// devuelve cantidad y tiempo.
		return new Resultado(cantidadCrafteadaTotal, tiempoAcumulado);
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
	public boolean removerCantidadDeUnObjetoSegunNro(int opcionIDObjeto, int cantidadAVender, Recetario recetario) {

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
						this.removerObjeto(objetoEnInventario, cantidadAVender, recetario);
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
}
