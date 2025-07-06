package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import datos.json.InventarioGSON;
import datos.json.RecetaGSON;
import modelo.HistorialDeCrafteo;
import modelo.Inventario;
import modelo.MesaDeFundicion;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.Recetario;
import modelo.Resultado;
import modelo.SistemaDeCrafteo;

public class Escenarios {

	private Escenarios() {
		throw new IllegalStateException("Utility class");
	}

	private static void interrupcion(Scanner scanner) {
		System.out.println("\nPresione Doble Enter para continuar...");
		scanner.nextLine(); // Espera a que el usuario presione Enter
		scanner.nextLine();
	}
	
	public static void interrupcionAnimadaConBarras(Scanner scanner) {
		AtomicBoolean seguir = new AtomicBoolean(true);
		Thread hiloDeEscucha = new Thread(() -> {
			scanner.nextLine(); //se bloquea el hilo esperando Enter
			seguir.set(false);  //el hilo termina
        });
		hiloDeEscucha.setDaemon(true);//el hilo termina con main 
        hiloDeEscucha.start();
        //el flujo principal no se bloquea
        System.out.println("Presione Enter para seguir...");
        int barras = 0;
        while (seguir.get()) {
        	System.out.print("█");
        	barras++;
        	if (barras >= 30) {
        		System.out.print("\r                              ");
        		System.out.print("\r");
        		barras = 0;
        	}
        	try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
        }
        System.out.print("\r██████████████████████████████\n");
    }

	private static final String MENSAJE_ERRROR = "Error: ";
	private static final String ESCENARIOS_SEPARADOR = "===========================";

	public static void seleccionarEscenario(Scanner scanner) {
		System.out.println("1. Escenarios Crear Hacha de Piedra con una receta.");
		System.out.println("2. Escenarios Mesa de fundición.");
		System.out.println("3. Escenarios Puntos 5-6-7 Equipamiento de Arquero:");
		System.out.println("4. Escenarios Prueba de craftear 2 mesas de trabajo diferente:");
		System.out.println("5. Escenarios Mesa de trabajo agregando recetas múltiples:");
		System.out.println("6. ");
		System.out.println("7. Escenarios Mesa de trabajo agregando recetas múltiples:");
		System.out.println("8. Cantidades necesarias de un objeto completo con dif. cantidades:");
		System.out.println("0. Volver al Menú.\n");

		boolean salir = true;

		int intescenario;
		HistorialDeCrafteo historial = HistorialDeCrafteo.getInstanciaUnica();

		do {
			// Leer ID del usuario
			System.out.print("Elige el N° de escenario (0=volver al menú): \n");
			while (!scanner.hasNextInt()) {
				System.out.println("N° inválido. Por favor, elige un número dentro del listado de escenarios.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige el N° de escenario (0=volver al menú): \n");
			}

			intescenario = scanner.nextInt();

			historial.limpiarRegistros(); // reinicia el historial.
			switch (intescenario) {
			case 1:
				Escenarios.escenarioCraftearHachaDePiedraConUnaReceta(scanner);
				break;
			case 2:
				Escenarios.escenarioCraftearMesaDeFundicionYSusRecetas(scanner);
				break;
			case 3:
				Escenarios.esce03EquipamientoDeArquero(scanner);
				break;
			case 4:
				Escenarios.esce04PruebaMesaDeTrabajo(scanner);
				break;
			case 5:
				Escenarios.esce05RecetasMultiplesCon(scanner);
				break;
			case 6:
				break;
			case 7:
				Escenarios.esce07RecetaBUCLE(scanner);
				break;
			case 8:
				Escenarios.esce08EquipoCompleto(scanner);
				break;
			case 0:
				salir = false;
				break;
			default:
				System.out.println("N° inválido. Por favor, elige un número dentro del listado de escenarios.");
				break;
			}

		} while (salir);

	}

	public static void escenarioCraftearHachaDePiedraConUnaReceta(Scanner scanner) {
		scanner.nextLine(); 
		// Crear objetos
		ObjetoBasico madera = new ObjetoBasico("Madera");
		ObjetoBasico piedra = new ObjetoBasico("Piedra");
		ObjetoBasico hierro = new ObjetoBasico("Hierro");
		ObjetoIntermedio oxido = new ObjetoIntermedio("Oxido");
		ObjetoIntermedio hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");

		// Crear ingredientes de los objetos
		Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
		ingredientesHachaDePiedra.put(piedra, 3);
		ingredientesHachaDePiedra.put(madera, 2);
		ingredientesHachaDePiedra.put(oxido, 2);

		Map<Objeto, Integer> ingredientesOxido = new HashMap<>();
		ingredientesOxido.put(hierro, 7);

		// Crear recetas

		Receta recetaHachaDePiedra = new Receta(hachaDePiedra, ingredientesHachaDePiedra, 1, 5); // produce 1 hacha,
																									// tiempo 5
		Receta recetaOxido = new Receta(oxido, ingredientesOxido, 1, 2);

		// Inicializar inventario y agregar algunos recursos
		Inventario inventario = new Inventario();
		inventario.agregarObjeto(madera, 10);
		inventario.agregarObjeto(piedra, 5);
		inventario.agregarObjeto(hierro, 4);

		// Inicializar recetario y sistema de crafteo
		Recetario recetario = new Recetario();
		recetario.agregarReceta(recetaHachaDePiedra);
		recetario.agregarReceta(recetaOxido);

		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		System.out.println("=== Inventario Inicial ===");
		System.out.println(inventario);
		System.out.println("=== Recetario Inicial ===");
		System.out.println(recetario);
	    
		interrupcionAnimadaConBarras(scanner);
	    
		// funcionalidades del sistema
		System.out.println("=== Ingredientes necesarios para Hacha de Piedra ===");
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesariosConCantidad(hachaDePiedra).getIngredientes();
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		interrupcionAnimadaConBarras(scanner);
		
		System.out.println("=== Ingredientes basicos necesarios para Hacha de Piedra ===");
		ingredientes = sistema.ingredientesBasicosNecesariosConTiempo(hachaDePiedra).getIngredientes();
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		interrupcionAnimadaConBarras(scanner);
		
		System.out.println("\n=== Pruebas de faltantes ===");

		// 1. Prueba ingredientesFaltantesParaCraftear
		System.out.println("\nIngredientes faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftearConTiempo(hachaDePiedra)
				.getIngredientes();
		if (faltantes.isEmpty())
			System.out.println("No faltan ingredientes directos!");
		else
			faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));
		
		interrupcionAnimadaConBarras(scanner);

		// 2. Prueba ingredientesBasicosFaltantesParaCraftear
		System.out.println("\nIngredientes básicos faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantesBasicos = sistema.ingredientesBasicosFaltantesParaCraftearConTiempo(hachaDePiedra)
				.getIngredientes();
		if (faltantesBasicos.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos
					.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " unidades básicas de " + obj));
		
		interrupcionAnimadaConBarras(scanner);

		// 3. Prueba cantidadCrafteable
		System.out.println("\n=== Prueba de cantidad crafteable ===");
		System.out.println("Hachas de piedra crafteables: "
				+ sistema.cantidadCrafteableConTiempo(hachaDePiedra).getCantidadCrafteable());
		System.out.println("Óxido crafteable: " + sistema.cantidadCrafteableConTiempo(oxido).getCantidadCrafteable());
		
		interrupcionAnimadaConBarras(scanner);

		// 4. Prueba después de agregar más recursos
		System.out.println("\n[Agregando 10 hierros más al inventario...]");
		inventario.agregarObjeto(hierro, 10);
		System.out.println(inventario);

		interrupcionAnimadaConBarras(scanner);
		
		System.out.println("\nNuevos valores después de agregar recursos:");
		System.out.println("Hachas de piedra crafteables ahora: "
				+ sistema.cantidadCrafteableConTiempo(hachaDePiedra).getCantidadCrafteable());
		System.out.println(
				"Óxido crafteable ahora: " + sistema.cantidadCrafteableConTiempo(oxido).getCantidadCrafteable());
		
		interrupcionAnimadaConBarras(scanner);

		System.out.println("\nNuevos ingredientes básicos faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantesBasicos2 = sistema
				.ingredientesBasicosFaltantesParaCraftearConTiempo(hachaDePiedra).getIngredientes();
		if (faltantesBasicos2.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		interrupcionAnimadaConBarras(scanner);
		
		try {
			System.out.println("\n=== Intentando craftear 1 unidad de " + hachaDePiedra);
			System.out.println("Tiempo Total: " + sistema.craftearObjeto(hachaDePiedra, 1));

			System.out.println("\n=== Historial de crfateo:");
			sistema.getHistorial().getRegistros().forEach((registro) -> System.out.println(registro));

			System.out.println("\n=== Inventario final:");
			inventario.getObjetos().forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

			new InventarioGSON("res/inventario_salida.json").guardar(inventario.getObjetos());
		} catch (Exception e) {
			System.err.println(MENSAJE_ERRROR + e.getMessage());
		}
		
		interrupcionAnimadaConBarras(scanner);

		System.out.println(ESCENARIOS_SEPARADOR);
		System.out.println("===Mostrando arbol de hacha de piedra===");
		try {
			sistema.mostrarArbolCrafteo(hachaDePiedra);
		} catch (IllegalArgumentException e) {
			System.err.println(MENSAJE_ERRROR + e.getMessage());
		}
	}

	public static void escenarioCraftearMesaDeFundicionYSusRecetas(Scanner scanner) {
		scanner.nextLine(); 
		Inventario inventario = new Inventario(new InventarioGSON(Config.INVENTARIO_FUNDIDOR).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.RECETA_FUNDIDOR).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		System.out.println(inventario);
		System.out.println(recetario);
		interrupcionAnimadaConBarras(scanner);
		System.out.println("====Agrego mas materiales y recetas de fundicion====");
		inventario.agregarObjetos(new InventarioGSON(Config.INVENTARIO_FUNDICION).cargar());
		recetario.agregarRecetas(new RecetaGSON(Config.RECETAS_FUNDICION).cargar());
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println(ESCENARIOS_SEPARADOR);
		interrupcionAnimadaConBarras(scanner);
		System.out.println("===Intento craftear recetas que usan el fundidor antes===");
		try {
			sistema.craftearObjeto(recetario.getRecetas().getFirst().getObjetoProducido(), 1);
		} catch (UnsupportedOperationException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(inventario);
		interrupcionAnimadaConBarras(scanner);
		System.out.println("=====Crafteando mesa de fundicion======");
		int tiempoFundidor = sistema.craftearObjeto(new MesaDeFundicion(), 1);
		System.out.println("Se crafteo la mesa de fundicion en: " + tiempoFundidor);
		System.out.println(inventario);
		interrupcionAnimadaConBarras(scanner);
		System.out.println(ESCENARIOS_SEPARADOR);
		System.out.println("======Crafteando las recetas de fundicion========");
		System.out.println(ESCENARIOS_SEPARADOR);

		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting [" + r.getObjetoProducido() + "] en "
						+ sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println(ESCENARIOS_SEPARADOR);
		}
		interrupcionAnimadaConBarras(scanner);
		System.out.println(ESCENARIOS_SEPARADOR);
		System.out.println("======Crafteando Segunda Vuelta========");
		System.out.println(ESCENARIOS_SEPARADOR);
		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting [" + r.getObjetoProducido() + "] en "
						+ sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println(ESCENARIOS_SEPARADOR);
		}
	}

	public static void esce03EquipamientoDeArquero(Scanner scanner) {
		System.out.println("-ESCENARIO 03.");
		System.out.println("-Este escenario emula la creación de un Objeto compuesto de 3 nivels.");
		System.out.println("-Donde estan implicadas 5 recetas, las cuales algunas comparten materiales Básico.");
		
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE03_RUTA_INICIO_INVENTARIO).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE03_RUTA_INICIO_RECETARIO).cargar());

		System.out.println("\n\nA continuación se crearan 2 veces el equipamiento, no juntas, si no por separado.\n");
		interrupcion(scanner);

		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println(inventario);

		Objeto equipoDeArquero = recetario.objetoCrafteable("Equipamiento para Arquero");

		// devolver cantidad posible a craftear.
		inventario.cantidadPosibleCraftear(equipoDeArquero, recetario).informarCantidadOpcion5();

		try {
			System.out.println("\n**Primera ejecución.\n");
			new Resultado(1, sistema.craftearObjeto(equipoDeArquero, 1), equipoDeArquero)
					.informarTiempoCrafteoOpcion6();
			System.out.println("\n**Segunda ejecución.\n");
			new Resultado(1, sistema.craftearObjeto(equipoDeArquero, 1), equipoDeArquero)
					.informarTiempoCrafteoOpcion6();
		} catch (Exception e) {
			System.err.println(MENSAJE_ERRROR + e.getMessage());
		}

		System.out.println("\nLuego de la creación exitosa, se muestra el historial con las 2 formas de crafteo. Una directa y otra usando los objetos básicos.\n");
		interrupcion(scanner);
		
		System.out.println(sistema.getHistorial().toString());
		System.out.println(inventario);
		new InventarioGSON(Config.ESCE03_RUTA_FINAL_INVENTARIO).guardar(inventario.getObjetos());
		System.out.println("\nFIN del escensario 03.\n");
	}

	public static void esce04PruebaMesaDeTrabajo(Scanner scanner) {
		System.out.println("ESCENARIO04:");
		System.out.println("Armar multiples Mesas de trabajo, NO apilables,  una sola vez tipo. Comprobandolo también en Prolog.\n");
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE04_RUTA_INICIO_INVENTARIO).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE04_RUTA_INICIO_RECETARIO).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println("Mostrar el inventario y el recetario.");
		interrupcion(scanner);
		System.out.println(inventario);
		System.out.println(recetario);
		recetario.prologGenerarRecetas();
		inventario.prologGenerarInventario();
		inventario.consultaDeProlog();
		
		System.out.println("\n\n**** A continuación se crearán las mesas.");
		interrupcion(scanner);

		Objeto mesa = recetario.objetoCrafteable("Mesa Generica");
		inventario.cantidadPosibleCraftear(mesa, recetario).informarCantidadOpcion5();

		try {
			new Resultado(1, sistema.craftearObjeto(mesa, 1), mesa).informarTiempoCrafteoOpcion6();
		} catch (Exception e) {
			System.err.println(MENSAJE_ERRROR + e.getMessage());
		}

		mesa = recetario.objetoCrafteable("Mesa de Flechas");
		new Resultado(1, sistema.craftearObjeto(mesa, 1), mesa).informarTiempoCrafteoOpcion6();

		inventario.cantidadPosibleCraftear(mesa, recetario).informarCantidadOpcion5();

		System.out.println("\n\n****Crear Lingotes y Puntas de Flechas, usando las recetas de las mesas.");
		interrupcion(scanner);

		Objeto objeIntermedio = recetario.objetoCrafteable("lingote de hierro");
		new Resultado(2, sistema.craftearObjeto(objeIntermedio, 2), objeIntermedio).informarTiempoCrafteoOpcion6();

		objeIntermedio = recetario.objetoCrafteable("Punta de Flecha");
		new Resultado(5, sistema.craftearObjeto(objeIntermedio, 5), objeIntermedio).informarTiempoCrafteoOpcion6();

		System.out.println("\n\n****Mostrar consulta de Prolog sin las mesas.");
		interrupcion(scanner);

		recetario.prologGenerarRecetas();
		inventario.prologGenerarInventario();
		inventario.consultaDeProlog();

		System.out.println("\n\n****Mostrar el recetario con todas las recetas cargadas de las mesas y inventario final.");
		interrupcion(scanner);

		System.out.println(recetario);
		System.out.println(inventario);

		new InventarioGSON(Config.ESCE04_RUTA_FINAL_INVENTARIO).guardar(inventario.getObjetos());
		System.out.println("\n\n****FIN Del escenario 04.");
		interrupcion(scanner);
	}

	/**
	 * Demostrar la incorporación de multiples recetas al crear una mesa y la seleccion automatica de la mas
	 * eficiente.
	 * 
	 * @param scanner para las interrupciones
	 */
	public static void esce05RecetasMultiplesCon(Scanner scanner) {
		System.out.println("ESCENARIO05:");
		System.out.println("Construir una mesa de flechas, la cual incorpora una receta nueva para ");
		System.out.println("la creación de las puntas de flecha, generando mas eficiencia al craftearlas.");
		System.out.println("Quitar la mesa de Flechas, quita las recetas.");
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE05_RUTA_INICIO_INVENTARIO).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE05_RUTA_INICIO_RECETARIO).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println(
				"A continuación se mostrara el Recetario con 3 recesas, 1 para la mesa, otra para las fechas y una para un componente.");
		interrupcion(scanner);

		System.out.println(recetario);

		System.out.println("\n\n A continuación se Creara una Mesa de Flechas , q agrega una receta nueva.");
		interrupcion(scanner);

		Objeto objMesa = recetario.objetoCrafteable("Mesa de Flechas");
		new Resultado(1, sistema.craftearObjeto(objMesa, 1), objMesa).informarTiempoCrafteoOpcion6();

		System.out.println("\n--Cambio de inventario y Recetario al agregar la Mesa de Flechas:\n");
		System.out.println(inventario);
		System.out.println(recetario);

		System.out.println("SE crafteara un lote de Punta de Flecha, para probar cual receta esa usando.");
		interrupcion(scanner);
		System.out.println("\n Crear Punta de Flecha con la Mejor Receta:");
		Objeto objCraftear = recetario.objetoCrafteable("Punta de Flecha");

		sistema.ingredientesNecesariosConCantidad(objCraftear).informarCantidadOpcion1();
		new Resultado(1, sistema.craftearObjeto(objCraftear, 1), objCraftear).informarTiempoCrafteoOpcion6();

		System.out.println(sistema.getHistorial().toString());

		System.out.println("Quitar la mesa del inventario, debe sacar la receta mas eficiente.");
		interrupcion(scanner);

		inventario.removerObjeto(objMesa, 1, recetario);

		System.out.println("Inventario final y Receta.");

		System.out.println(inventario);
		System.out.println(recetario);

		new InventarioGSON(Config.ESCE05_RUTA_FINAL_INVENTARIO).guardar(inventario.getObjetos());

		System.out.println("\n\nFIN del Escenario 05.\n\n");
	}
	
	/**
	 * Escenario para probar las recetas sin fin.
	 * @param scanner
	 */
	public static void esce07RecetaBUCLE(Scanner scanner) {
		System.out.println("ESCENARIO07:");
		System.out.println("Se intenta importar un conjunto de rectas con una ciclo sin fin en una de ellas:");
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE07_RUTA_INICIO_RECETARIO).cargar());
		
		System.out.println(recetario);
		System.out.println("\n\nSe intenta seleccionar de las recetas el objeto con el ciclo.:");
		recetario.objetoCrafteable("Objeto Bucle");
		System.out.println("\n\nFIN 07.:");
		interrupcion(scanner);
	}
	
	/**
	 * Escenario Incremental para mostrar los insumos y tiempos al incrementar
	 *  la cantidad inicial para hacer el equipo completo.
	 * 
	 * @param scanner
	 */
	public static void esce08EquipoCompleto(Scanner scanner) {
		System.out.println("ESCENARIO08:");
		System.out.println("Escenario Incremental para mostrar los insumos y tiempos al incrementar");
		System.out.println(" la cantidad inicial para hacer el equipo completo usando 3 recetas diferentes q emulan ir incrementando su cantidad en +1.");
		
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE08_RUTA_INICIO_RECETARIO).cargar());
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE08_RUTA_INICIO_INVENTARIO).cargar(),recetario);
		
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		
		Objeto objConsultar = recetario.objetoCrafteable("Equipamiento Completo");
		
		
		System.out.println("\n**** Cantidad y Tiempo de un crafteo.\n");
		sistema.ingredientesBasicosNecesariosConTiempo(objConsultar).informarCantidadOpcion2();
		interrupcion(scanner);
		
		Recetario recetario2 = new Recetario(new RecetaGSON(Config.ESCE08_RUTA_INICIO_RECETARIOV2).cargar());
		inventario = new Inventario(new InventarioGSON(Config.ESCE08_RUTA_INICIO_INVENTARIO).cargar(),recetario2);
		sistema = new SistemaDeCrafteo(inventario, recetario2);
		
		System.out.println("\n**** Cantidad y Tiempo de DOS crafteos.\n");
		
		sistema.ingredientesBasicosNecesariosConTiempo(objConsultar).informarCantidadOpcion2();
		interrupcion(scanner);
		
		Recetario recetario3 = new Recetario(new RecetaGSON(Config.ESCE08_RUTA_INICIO_RECETARIOV3).cargar());
		inventario = new Inventario(new InventarioGSON(Config.ESCE08_RUTA_INICIO_INVENTARIO).cargar(),recetario3);
		sistema = new SistemaDeCrafteo(inventario, recetario3);
		
		System.out.println("\n**** Cantidad y Tiempo de TRES crafteos.\n");
		
		sistema.ingredientesBasicosNecesariosConTiempo(objConsultar).informarCantidadOpcion2();
		
		System.out.println("\n**** Crear el Equipamiento Completo.\n");
		interrupcion(scanner);
		
		new Resultado(1, sistema.craftearObjeto(objConsultar, 1), objConsultar)
		.informarTiempoCrafteoOpcion6();
		
		System.out.println("\n**** Mostrar el historial de crafteo.\n");
		interrupcion(scanner);
		
		System.out.println(sistema.getHistorial().toString());
		
		System.out.println("\n**** FIN DEL ESCENARIO 08.\n");
		interrupcion(scanner);
	}

}