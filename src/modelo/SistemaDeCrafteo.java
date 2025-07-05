package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Clase central que orquesta el sistema de crafteo, coordinando el inventario y el recetario.
 * Proporciona funcionalidades clave como calcular ingredientes necesarios, validar faltantes,
 * determinar cuántos objetos pueden fabricarse y ejecutar crafteos con registro histórico.
 *  
 * Funcionalidades principales:
 * 
 * Determinar ingredientes necesarios para un objeto (primer nivel).
 * Calcular ingredientes básicos necesarios desde cero.
 * Identificar faltantes en el inventario (tanto de primer nivel como básicos).
 * Calcular cuántas unidades pueden craftearse dado el inventario actual.
 * Realizar crafteos y actualizar el inventario.
 * Registrar tiempos totales y mantener un historial de acciones.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class SistemaDeCrafteo {
	private Inventario inventario;
	private Recetario recetario;
	private HistorialDeCrafteo historial;

	/**
     * Constructor que inicializa el sistema con un inventario y recetario específicos.
     * Usa el historial de crafteos singleton para registrar acciones realizadas.
     * 
     * @param inventario Inventario del jugador donde se almacenarán los objetos.
     * @param recetario Recetario que contiene todas las recetas disponibles.
     */
	public SistemaDeCrafteo(Inventario inventario, Recetario recetario) {
		if (inventario == null) {
			throw new IllegalArgumentException("No existe Inventario");
		}
		if (recetario == null) {
			throw new IllegalArgumentException("No existe Recetaraio");
		}
		this.inventario = inventario;
		this.recetario = recetario;
		this.historial = HistorialDeCrafteo.getInstanciaUnica();
	}

	/**
     * Devuelve el historial de crafteos realizado.
     * 
     * @return Devuelve la instancia única del historial de crafteo.
     */
	public HistorialDeCrafteo getHistorial() {
		return historial;
	};

	/**
     * Devuelve los ingredientes directos necesarios para craftear un objeto específico.
     * Segun la primer receta
     * Este método solo considera el primer nivel de la receta, sin descomponer ingredientes intermedios.
     * 
     * @param objeto Objeto crafteable del que se requieren los ingredientes.
	 * @return Mapa de ingredientes y sus cantidades necesarias.
	 * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
	 */
	
	public Resultado ingredientesNecesariosConCantidad(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		try {
			Receta receta = recetario.buscarReceta(objeto);

			return new Resultado(1, receta.getTiempoBase(), objeto, receta.getIngredientes());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:" + objeto);
		}
	}


	/**
     * Devuelve los ingredientes básicos necesarios para craftear un objeto desde cero.
     * Segun la primer receta.
     * Este método descompone recursivamente todos los ingredientes intermedios hasta llegar a objetos básicos.
     * 
     * @param objeto Objeto crafteable del que se requieren los ingredientes básicos.
     * @return Mapa de ingredientes básicos y sus cantidades totales necesarias con Tiempo.
     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
     */
	public Resultado ingredientesBasicosNecesariosConTiempo(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		try {
			Receta receta = recetario.buscarReceta(objeto);

			return new Resultado(1, receta.calcularTiempoTotal(recetario,receta.getCantidadProducida()), objeto,
					receta.getIngredientesBasicos(recetario,receta.getCantidadProducida()));
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:" + objeto);
		}
	}

	/**
     * Devuelve los ingredientes que faltan para craftear un objeto (solo primer nivel).
     * Segun la primer receta.
     * Verifica si el inventario tiene suficientes ingredientes directos para fabricar el objeto.
     * 
     * @param objeto Objeto crafteable a evaluar.
     * @return Mapa de ingredientes faltantes y sus cantidades necesarias y el tiempo.
     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
     */
	public Resultado ingredientesFaltantesParaCraftearConTiempo(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		return new Resultado(1, recetario.buscarReceta(objeto).getTiempoBase(), objeto,
				inventario.getFaltantes(ingredientesNecesariosConCantidad(objeto).getIngredientes()));
	}

	/**
     * Devuelve los ingredientes básicos que faltan para craftear un objeto desde cero.
     * Segun la primer receta.
     * Evalúa todos los niveles de la cadena de crafteo hasta los ingredientes básicos.
     * 
     * @param objeto Objeto crafteable a evaluar.
     * @return Mapa de ingredientes básicos faltantes y sus cantidades necesarias.
     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
     */
	public Resultado ingredientesBasicosFaltantesParaCraftearConTiempo(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		Receta receta = recetario.buscarReceta(objeto);

		return new Resultado(1, receta.calcularTiempoTotal(recetario,receta.getCantidadProducida()), objeto,
				inventario.getFaltantesBasicos(ingredientesBasicosNecesariosConTiempo(objeto).getIngredientes(), recetario));
	}
	
	/**
     * Calcula cuántas unidades de un objeto pueden craftearse con el inventario actual.
     * Segun la primer receta.
     * Considera tanto ingredientes directos como la posibilidad de fabricar ingredientes intermedios.
     * 
     * @param objeto Objeto crafteable a evaluar.
     * @return Cantidad máxima de objetos que pueden fabricarse.
     * @throws IllegalArgumentException Si el objeto es básico o no tiene receta.
     * @throws UnsupportedOperationException Si no tiene la mesa necesaria.
     */
	public Resultado cantidadCrafteableConTiempo(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		Resultado res;
		try {
			res = inventario.cantidadPosibleCraftear(objeto, recetario);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
		catch (IllegalStateException e) {
			throw e;
		}
		return res;
	}

	/**
     * Realiza el crafteo de un objeto y actualiza el inventario.
     * Segun la primer receta.
     * Modifica el inventario al consumir ingredientes y añadir el objeto creado.
     * Registra el crafteo en el historial con detalles de ingredientes y tiempo.
     * 
     * @param objeto Objeto crafteable a fabricar.
     * @param cantACraftear Cantidad deseada del objeto.
     * @return Tiempo total invertido en el crafteo.
     * @throws IllegalStateException Si no hay suficientes ingredientes o no existe receta.
     * @throws UnsupportedOperationException Si el objeto es básico o falta la mesa requerida.
     */
	public int craftearObjeto(Objeto objeto, int cantACraftear) {
		if (cantACraftear <= 0) {
			throw new IllegalArgumentException("La cantidad a craftear debe ser positiva");
		}
		if (!objeto.esApilable() && cantACraftear > 1) {
			throw new UnsupportedOperationException("No se puede crafear la cantidad de " + cantACraftear
					+ " , no es apilable solo admite 1 unidad: " + objeto);
		}
		// Verificar si es posible craftear la cantidad solicitada
		int maxCrafteable = inventario.cantidadPosibleCraftear(objeto, recetario).getCantidadCrafteable();
		if (maxCrafteable < cantACraftear) {
			throw new IllegalStateException(
					"No hay suficientes materiales para craftear " + cantACraftear + " " + objeto);
		}
		if (objeto.esBasico()) {
			throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
		}
		Receta receta = recetario.buscarReceta(objeto);
		if (receta == null) {
			throw new IllegalStateException("No existe receta para craftear " + objeto);
		}
		// verificar si la receta tiene mesa
		if (!inventario.tieneMesa(receta.getMesaRequerida())) {
			throw new UnsupportedOperationException(
					"No tienes [" + receta.getMesaRequerida() + "] para craftear->" + objeto);
		}
		// verificar si se puede apilar y q este en el inventario.
		if (inventario.getCantidad(objeto) >= 1 && !objeto.esApilable()) {
			throw new UnsupportedOperationException(
					"No se puede crafear porque ya lo tienes, no es apilable: " + objeto);
		}

		// cuantos lotes de la receta necesitamos ejecutar.
		int vecesReceta = Math.ceilDiv(cantACraftear, receta.getCantidadProducida());

		int cantidadProducida = vecesReceta * receta.getCantidadProducida();

		int tiempoTotal = receta.getTiempoBase() * vecesReceta;
		Map<Objeto, Integer> ingredientesUsados = new HashMap<Objeto, Integer>();

		// Procesar y craftear sub-ingredientes
		for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
			Objeto ingrediente = entry.getKey();
			int cantidadNecesaria = entry.getValue() * vecesReceta;

			if (!ingrediente.esBasico()) {
				int cantidadExistente = inventario.getCantidad(ingrediente);
				int cantidadFaltante = cantidadNecesaria - cantidadExistente;

				if (cantidadFaltante > 0) {
					tiempoTotal += craftearObjeto(ingrediente, cantidadFaltante);
				}
			}
			inventario.removerObjeto(ingrediente, cantidadNecesaria,recetario);
			ingredientesUsados.put(ingrediente, cantidadNecesaria);
		}

		// Agregar el objeto crafteado al inventario.
		inventario.agregarObjeto(objeto, cantidadProducida,recetario);
		//recetario.agregarRecetas(objeto.listaDeRecetasPropias()); no es necesario inventario va agregar las mesas 

		// Registrar el crafteo en el historial.
		historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal, ingredientesUsados);

		return tiempoTotal;
	}
	
	// *****Implementacion Arbol de Crafteo*************
	private int mostrarArbolRecursivo(Receta receta, Recetario recetario, int nivel, 
			int cantidadNecesaria, int cantidadProducida) {

		StringBuilder espacio = new StringBuilder();
		for (int i = 0; i < nivel; i++) {
			espacio.append("  ");
		}
		int vecesReceta = Math.ceilDiv(cantidadNecesaria, receta.getCantidadProducida());
		Objeto objeto = receta.getObjetoProducido();
		cantidadProducida = receta.getCantidadProducida();
		int tiempoBase = receta.getTiempoBase();
		
		System.out.println(
				espacio + "└─ "+cantidadNecesaria+"x[" + objeto.getNombre() + " x"+cantidadProducida+" t="+tiempoBase+"x"+vecesReceta+"veces]");
		
		tiempoBase = receta.getTiempoBase()*vecesReceta;

		for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
			Objeto ingrediente = entry.getKey();
			int cantidadIngrediente = entry.getValue();

			if (!ingrediente.esBasico()) {
				Receta subReceta = recetario.buscarReceta(ingrediente);
				tiempoBase += mostrarArbolRecursivo(subReceta, recetario, nivel + 1,cantidadIngrediente,cantidadProducida);
			} else {
				System.out.println(espacio + "  └─ "+cantidadIngrediente+"x(" + ingrediente.getNombre()+")");
			}
		}
		
		return tiempoBase;
	}

	/**
     * Muestra el árbol de crafteo para un objeto específico.
     * 
     * Representa visualmente la cadena de recetas necesarias para fabricar el objeto,
     * incluyendo ingredientes intermedios y básicos.
     * 
     * @param objeto Objeto crafteable a mostrar.
     * @throws IllegalArgumentException Si el objeto es básico o no tiene receta.
     */
	public void mostrarArbolCrafteo(Objeto objeto) {
		if (objeto == null) {
			throw new IllegalArgumentException("No existe objeto:" + objeto);
		}
		try {
			Receta receta = recetario.buscarReceta(objeto);
			System.out.println("===========================");
			System.out.println("===Arbol de crafteo -> " + objeto + "===");
			System.out.println(" [] = Intermedios / () Basicos / #<- Necesario / #-> producido.");
			System.out.println("===========================");
			

			int cantidad = receta.getCantidadProducida();
			int tiempoTotal = mostrarArbolRecursivo(receta, recetario, 0, cantidad,cantidad);
			
			System.out.println("===Arbol de crafteo Tardo: (" + tiempoTotal + ") min");
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:" + objeto);
		}
	}

	// *****Implementacion Recetas Alternativas*************

	/**
     * Realiza el crafteo de un objeto y actualiza el inventario.
     * Segun la receta que se siga
     * Modifica el inventario al consumir ingredientes y añadir el objeto creado.
     * Registra el crafteo en el historial con detalles de ingredientes y tiempo.
     * 
     * @param objeto Objeto crafteable a fabricar.
     * @param cantACraftear Cantidad deseada del objeto.
     * @param indiceReceta El indice de la receta asociada al objeto
     * @return Tiempo total invertido en el crafteo.
     * @throws IllegalStateException Si no hay suficientes ingredientes o no existe receta.
     * @throws UnsupportedOperationException Si el objeto es básico o falta la mesa requerida.
     */
	public int craftearObjetoConReceta(Objeto objeto, int cantACraftear, int indiceReceta) {
		if (cantACraftear <= 0) {
			throw new IllegalArgumentException("La cantidad a craftear debe ser positiva");
		}
		if (indiceReceta < 0) {
			throw new IllegalArgumentException("El indice de receta no debe ser negativo");
		}
		// Verificar si es posible craftear la cantidad solicitada
		int maxCrafteable = inventario.cantidadPosibleCraftear(objeto, recetario,indiceReceta).getCantidadCrafteable();
		if (maxCrafteable < cantACraftear) {
			throw new IllegalStateException(
					"No hay suficientes materiales para craftear " + cantACraftear + " " + objeto);
		}
		if (objeto.esBasico()) {
			throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
		}
		List<Receta> recetas = recetario.buscarRecetas(objeto);
		if (recetas == null) {
			throw new IllegalStateException("No existe receta para craftear " + objeto);
		}
		// verificar si la receta tiene mesa
		if (!inventario.tieneMesa(recetas.get(indiceReceta).getMesaRequerida())) {
			throw new UnsupportedOperationException(
					"No tienes [" + recetas.get(indiceReceta).getMesaRequerida() + "] para craftear->" + objeto);
		}
		// verificar si se puede apilar
		if (inventario.getCantidad(objeto) >= 1 && !objeto.esApilable()) {
			throw new UnsupportedOperationException(
					"No se puede crafear porque ya lo tienes, no es apilable: " + objeto);
		}

		// cuantos lotes de la receta necesitamos ejecutar.
		int vecesReceta = Math.ceilDiv(cantACraftear, recetas.get(indiceReceta).getCantidadProducida());

		int cantidadProducida = vecesReceta * recetas.get(indiceReceta).getCantidadProducida();

		int tiempoTotal = recetas.get(indiceReceta).getTiempoBase() * vecesReceta;

		// Procesar ingredientes recursivamente
		for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
			Objeto ingrediente = entry.getKey();
			int cantidadNecesaria = entry.getValue() * vecesReceta;

			if (!ingrediente.esBasico()) {
				int cantidadExistente = inventario.getCantidad(ingrediente);
				int cantidadFaltante = cantidadNecesaria - cantidadExistente;

				if (cantidadFaltante > 0) {
					tiempoTotal += craftearObjeto(ingrediente, cantidadFaltante);
				}
			}
			inventario.removerObjeto(ingrediente, cantidadNecesaria, recetario);
		}

		// Agregar al inventario y registrar en historial
		inventario.agregarObjeto(objeto, cantidadProducida, recetario);

		// Registrar en el historial
		historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal);

		return tiempoTotal;
	}
	
//	/**
//     * Devuelve los ingredientes directos necesarios para craftear un objeto específico.
//     * Segun la receta que se siga
//     * Este método solo considera el primer nivel de la receta, sin descomponer ingredientes intermedios.
//     * 
//     * @param objeto Objeto crafteable del que se requieren los ingredientes.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @return Mapa de ingredientes y sus cantidades necesarias.
//     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
//     */
//	public Map<Objeto, Integer> ingredientesNecesarios(Objeto objeto, int indiceReceta) {
//		if (objeto == null || indiceReceta < 0) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		if (indiceReceta < 0) {
//			throw new IllegalArgumentException("El indice de receta no debe ser negativo");
//		}
//		try {
//			List<Receta> recetas = recetario.buscarRecetas(objeto);
//			return recetas.get(indiceReceta).getIngredientes();
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("El objeto no tiene receta:" + objeto);
//		}
//	}
//
//	/**
//     * Devuelve los ingredientes básicos necesarios para craftear un objeto desde cero.
//     * Segun la receta que se siga
//     * Este método descompone recursivamente todos los ingredientes intermedios hasta llegar a objetos básicos.
//     * 
//     * @param objeto Objeto crafteable del que se requieren los ingredientes básicos.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @return Mapa de ingredientes básicos y sus cantidades totales necesarias.
//     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
//     */
//	public Map<Objeto, Integer> ingredientesBasicosNecesarios(Objeto objeto, int indiceReceta) {
//		if (objeto == null) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		if (indiceReceta < 0) {
//			throw new IllegalArgumentException("El indice de receta no debe ser negativo");
//		}
//		try {
//			List<Receta> recetas = recetario.buscarRecetas(objeto);
//			return recetas.get(indiceReceta).getIngredientesBasicos(recetario);
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("El objeto no tiene recetas:" + objeto);
//		}
//	}
//	
//	/**
//     * Devuelve los ingredientes que faltan para craftear un objeto (solo primer nivel).
//     * Segun la receta que se siga
//     * Verifica si el inventario tiene suficientes ingredientes directos para fabricar el objeto.
//     * 
//     * @param objeto Objeto crafteable a evaluar.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @return Mapa de ingredientes faltantes y sus cantidades necesarias.
//     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
//     */
//	public Map<Objeto, Integer> ingredientesFaltantesParaCraftear(Objeto objeto, int indiceReceta) {
//		if (objeto == null) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		return inventario.getFaltantes(ingredientesNecesarios(objeto,indiceReceta));
//	}
//
//	/**
//     * Devuelve los ingredientes básicos que faltan para craftear un objeto desde cero.
//     * Segun la receta que se siga
//     * Evalúa todos los niveles de la cadena de crafteo hasta los ingredientes básicos.
//     * 
//     * @param objeto Objeto crafteable a evaluar.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @return Mapa de ingredientes básicos faltantes y sus cantidades necesarias.
//     * @throws IllegalArgumentException Si el objeto es nulo o no tiene receta.
//     */
//	public Map<Objeto, Integer> ingredientesBasicosFaltantesParaCraftear(Objeto objeto, int indiceReceta) {
//		if (objeto == null) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		if (indiceReceta < 0) {
//			throw new IllegalArgumentException("El indice de receta no debe ser negativo");
//		}
//		return inventario.getFaltantesBasicos(ingredientesBasicosNecesarios(objeto, indiceReceta), recetario,
//				indiceReceta);
//
//	}
//
//	/**
//     * Calcula cuántas unidades de un objeto pueden craftearse con el inventario actual.
//     * Segun la receta que se siga
//     * Considera tanto ingredientes directos como la posibilidad de fabricar ingredientes intermedios.
//     * 
//     * @param objeto Objeto crafteable a evaluar.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @return Cantidad máxima de objetos que pueden fabricarse.
//     * @throws IllegalArgumentException Si el objeto es básico o no tiene receta.
//     * @throws UnsupportedOperationException Si no tiene la mesa necesaria.
//     */
//	public int cantidadCrafteable(Objeto objeto, int indiceReceta) {
//		if (objeto == null) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		if (objeto.esBasico()) {
//			throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
//		}
//		List<Receta> recetas = null;
//		try {
//			recetas = recetario.buscarRecetas(objeto);
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("El objeto no tiene recetas:" + objeto);
//		}
//		if (recetas == null) {
//			throw new IllegalStateException("No existe receta para craftear " + objeto);
//		}
//
//		if (!inventario.tieneMesa(recetas.get(indiceReceta).getMesaRequerida())) {
//			throw new UnsupportedOperationException(
//					"No tienes [" + recetas.get(indiceReceta).getMesaRequerida() + "] para craftear->" + objeto);
//		}
//
//		int cantidadTotalDisponible = 0;
//		int numLotesCrafteables = Integer.MAX_VALUE;
//
//		for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
//			Objeto ingrediente = entry.getKey();
//			int cantidadNecesariaDelIngrediente = entry.getValue();
//			int cantidadDisponibleDelIngrediente = inventario.getCantidadBasico(ingrediente, recetario);
//
//			int lotesPosibles = Math.floorDiv(cantidadDisponibleDelIngrediente, cantidadNecesariaDelIngrediente);
//
//			// Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más
//			// escaso
//			numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
//		}
//
//		cantidadTotalDisponible += numLotesCrafteables * recetas.get(indiceReceta).getCantidadProducida();
//
//		int vecesReceta = (numLotesCrafteables == 0) ? 1 : numLotesCrafteables;
//		System.out.println("La cantidad crafteable se ejecuto en: "
//				+ recetas.get(indiceReceta).calcularTiempoTotal(recetario) * vecesReceta);
//		return cantidadTotalDisponible;
//
//	}
//	
//	/**
//     * Muestra el árbol de crafteo para un objeto específico.
//     * Segun la receta que se siga
//     * Representa visualmente la cadena de recetas necesarias para fabricar el objeto,
//     * incluyendo ingredientes intermedios y básicos.
//     * 
//     * @param objeto Objeto crafteable a mostrar.
//     * @param indiceReceta El indice de la receta asociada al objeto
//     * @throws IllegalArgumentException Si el objeto es básico o no tiene receta.
//     */
//	public void mostrarArbolCrafteo(Objeto objeto, int indiceReceta) {
//		if (objeto == null) {
//			throw new IllegalArgumentException("No existe objeto:" + objeto);
//		}
//		try {
//			List<Receta> recetas = recetario.buscarRecetas(objeto);
//			System.out.println("===========================");
//			System.out.println("===Arbol de crafteo -> " + objeto + "===");
//			System.out.println(" [] = Intermedios / () Basicos / #<- Necesario / #-> producido.");
//			System.out.println("===========================");
//			
//
//			int cantidad = recetas.get(indiceReceta).getCantidadProducida();
//			mostrarArbolRecursivo(recetas.get(indiceReceta), recetario, 0, cantidad,cantidad);
//		} catch (NoSuchElementException e) {
//			throw new IllegalArgumentException("El objeto no tiene receta:" + objeto);
//		}
//	}
}
