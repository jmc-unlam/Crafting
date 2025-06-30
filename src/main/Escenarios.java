package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
	public static void seleccionarEscenario() {
		System.out.println("1. Escenarios Crear Hacha de Piedra con una receta.");
		System.out.println("2. Escenarios Mesa de fundición.");
		System.out.println("3. Escenarios Puntos 5-6-7 Equipamiento de Arquero:");
		System.out.println("4. Escenarios Prueba de craftear 2 mesas de trabajo diferente:");
		System.out.println("5. Escenarios Mesa de trabajo agregando recetas múltiples:");
		System.out.println("0. Volver al Menú.\n");

		boolean salir = true;
		Scanner scanner = new Scanner(System.in);
		int intescenario;
		HistorialDeCrafteo historial = HistorialDeCrafteo.getInstanciaUnica();

		do {
			// Leer ID del usuario
			System.out.print("Elige el N° de escenario (0=volver al menú): ");
			while (!scanner.hasNextInt()) {
				System.out.println("N° inválido. Por favor, elige un número dentro del listado de escenarios.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige el N° de escenario (0=volver al menú): ");
			}

			intescenario = scanner.nextInt();

			historial.limpiarRegistros(); // reinicia el historial.
			switch (intescenario) {
			case 1:
				Escenarios.escenarioCraftearHachaDePiedraConUnaReceta();
				break;
			case 2:
				Escenarios.escenarioCraftearMesaDeFundicionYSusRecetas();
				break;
			case 3:
				Escenarios.ESCE03EquipamientoDeArquero();
				break;
			case 4:
				Escenarios.ESCE04PruebaMesaDeTrabajo();
				break;
			case 5:
				Escenarios.ESCE05RecetasMultiplesCon();
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

	public static void escenarioCraftearHachaDePiedraConUnaReceta() {
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

		// funcionalidades del sistema
		System.out.println("=== Ingredientes necesarios para Hacha de Piedra ===");
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		System.out.println("=== Ingredientes basicos necesarios para Hacha de Piedra ===");
		ingredientes = sistema.ingredientesBasicosNecesarios(hachaDePiedra);
		ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		System.out.println("\n=== Pruebas de faltantes ===");

		// 1. Prueba ingredientesFaltantesParaCraftear
		System.out.println("\nIngredientes faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(hachaDePiedra);
		if (faltantes.isEmpty())
			System.out.println("No faltan ingredientes directos!");
		else
			faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));

		// 2. Prueba ingredientesBasicosFaltantesParaCraftear
		System.out.println("\nIngredientes básicos faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantesBasicos = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
		if (faltantesBasicos.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos
					.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " unidades básicas de " + obj));

		// 3. Prueba cantidadCrafteable
		System.out.println("\n=== Prueba de cantidad crafteable ===");
		System.out.println("Hachas de piedra crafteables: " + sistema.cantidadCrafteable(hachaDePiedra));
		System.out.println("Óxido crafteable: " + sistema.cantidadCrafteable(oxido));

		// 4. Prueba después de agregar más recursos
		System.out.println("\n[Agregando 10 hierros más al inventario...]");
		inventario.agregarObjeto(hierro, 10);
		System.out.println(inventario);

		System.out.println("\nNuevos valores después de agregar recursos:");
		System.out.println("Hachas de piedra crafteables ahora: " + sistema.cantidadCrafteable(hachaDePiedra));
		System.out.println("Óxido crafteable ahora: " + sistema.cantidadCrafteable(oxido));

		System.out.println("\nNuevos ingredientes básicos faltantes para Hacha de Piedra:");
		Map<Objeto, Integer> faltantesBasicos2 = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
		if (faltantesBasicos2.isEmpty())
			System.out.println("No faltan ingredientes básicos!");
		else
			faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

		try {
			System.out.println("\n=== Intentando craftear 1 unidad de " + hachaDePiedra);
			System.out.println("Tiempo Total: " + sistema.craftearObjeto(hachaDePiedra, 1));

			System.out.println("\n=== Historial de crfateo:");
			sistema.getHistorial().forEach((registro) -> System.out.println(registro));

			System.out.println("\n=== Inventario final:");
			inventario.getObjetos().forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

			new InventarioGSON("res/inventario_salida.json").guardar(inventario.getObjetos());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void escenarioCraftearMesaDeFundicionYSusRecetas() {

		Inventario inventario = new Inventario(new InventarioGSON(Config.INVENTARIO_FUNDIDOR).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.RECETA_FUNDIDOR).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println("====Agrego mas materiales y recetas de fundicion====");
		inventario.agregarObjetos(new InventarioGSON(Config.INVENTARIO_FUNDICION).cargar());
		recetario.agregarRecetas(new RecetaGSON(Config.RECETAS_FUNDICION).cargar());
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println("===========================");
		System.out.println("===Intento craftear recetas que usan el fundidor antes===");
		try {
			sistema.craftearObjeto(recetario.getRecetas().getFirst().getObjetoProducido(), 1);
		} catch (UnsupportedOperationException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(inventario);

		System.out.println("=====Crafteando mesa de fundicion======");
		int tiempoFundidor = sistema.craftearObjeto(new MesaDeFundicion(), 1);
		System.out.println("Se crafteo la mesa de fundicion en: " + tiempoFundidor);
		System.out.println(inventario);
		System.out.println("===========================");
		System.out.println("======Crafteando las recetas de fundicion========");
		System.out.println("===========================");

		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting [" + r.getObjetoProducido() + "] en "
						+ sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println("===========================");
		}
		System.out.println("===========================");
		System.out.println("======Crafteando Segunda Vuelta========");
		System.out.println("===========================");
		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting [" + r.getObjetoProducido() + "] en "
						+ sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println("===========================");
		}
	}

	public static void ESCE03EquipamientoDeArquero() {
		System.out.println("-ESCENARIO 03.");
		System.out.println("-Este escenario emula la creación de un Objeto compuesto de 3 nivels.");
		System.out.println("-Donde estan implicadas 5 recetas, las cuales algunas comparten materiales Básico.");
		System.out.println("\n");
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE03_RUTA_INICIO_INVENTARIO).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE03_RUTA_INICIO_RECETARIO).cargar());

		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println(inventario);

		Objeto equipoDeArquero = recetario.objetoCrafteable("Equipamiento para Arquero");

		// devolver cantidad posible a craftear.
		inventario.cantidadPosibleCraftear(equipoDeArquero, recetario).informarCantidadOpcion5();

		try {
			new Resultado(2, sistema.craftearObjeto(equipoDeArquero, 2), equipoDeArquero)
					.informarTiempoCrafteoOpcion6();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		System.out.println(sistema.getHistorialReal().toString());
		System.out.println(inventario);
		new InventarioGSON(Config.ESCE03_RUTA_FINAL_INVENTARIO).guardar(inventario.getObjetos());
	}

	public static void ESCE04PruebaMesaDeTrabajo() {
		System.out.println("ESCENARIO04:");
		System.out.println("Armar multiples Mesas de trabajo, NO apilables,  una sola opr tipo vez.\n");
		Inventario inventario = new Inventario(new InventarioGSON(Config.ESCE04_RUTA_INICIO_INVENTARIO).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.ESCE04_RUTA_INICIO_RECETARIO).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		System.out.println(inventario);
		System.out.println(recetario);

		Objeto mesa = recetario.objetoCrafteable("Mesa de Generica");
		inventario.cantidadPosibleCraftear(mesa, recetario).informarCantidadOpcion5();

		try {
			new Resultado(1, sistema.craftearObjeto(mesa, 1), mesa).informarTiempoCrafteoOpcion6();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		mesa = recetario.objetoCrafteable("Mesa de Flechas");
		new Resultado(1, sistema.craftearObjeto(mesa, 1), mesa).informarTiempoCrafteoOpcion6();

		inventario.cantidadPosibleCraftear(mesa, recetario).informarCantidadOpcion5();

		Objeto objeIntermedio = recetario.objetoCrafteable("lingote de hierro");
		new Resultado(2, sistema.craftearObjeto(objeIntermedio, 2), objeIntermedio).informarTiempoCrafteoOpcion6();

		objeIntermedio = recetario.objetoCrafteable("Punta de Flecha");
		new Resultado(5, sistema.craftearObjeto(objeIntermedio, 5), objeIntermedio).informarTiempoCrafteoOpcion6();

		recetario.PrologGenerarRecetas();
		inventario.prologGenerarInventario();
		inventario.consultaDeProlog();

		System.out.println(recetario);
		System.out.println(inventario);
		System.out.println(
				"COMENTARIO FINAL: Se crearon las fechas usando la mesa de flechas y el lingote de hierro para las fechas creada con la mesa General.");
		new InventarioGSON(Config.ESCE04_RUTA_FINAL_INVENTARIO).guardar(inventario.getObjetos());

	}

	public static void ESCE05RecetasMultiplesCon() {
		System.out.println("ESCENARIO05:");
		System.out.println("Construir una mesa de flechas, la cual incorpora una receta nueva para la ");
		System.out.println("la creación de las puntas de flecha más, generando mas eficiencia al craftearlas.");
	}

}