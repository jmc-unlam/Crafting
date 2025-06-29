package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import datos.json.InventarioGSON;
import datos.json.RecetaGSON;
import modelo.Inventario;
import modelo.MesaDeFundicion;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.Recetario;
import modelo.SistemaDeCrafteo;

public class Main {

	private static Inventario inventario;
	private static Recetario recetario;
	private static SistemaDeCrafteo sistema;

	public static void main(String[] args) {

		inicio();

		Scanner scanner = new Scanner(System.in);
		int opcion;
		Objeto objePregunta;

		do {
			// Mostrar menú
			mostrarMenu();

			// Leer opción del usuario
			System.out.print("Elige una opción: ");
			while (!scanner.hasNextInt()) {
				System.out.println("Entrada inválida. Por favor, elige un número.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige una opción: ");
			}
			opcion = scanner.nextInt();

			// Ejecutar acción según la opción
			switch (opcion) {
			case 1:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("=== Ingredientes necesarios para " + objePregunta.getNombre() + " ===");

				Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(objePregunta);
				ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

				break;
			case 2:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("=== Ingredientes basicos necesarios para " + objePregunta.getNombre() + " ===");
				ingredientes = sistema.ingredientesBasicosNecesarios(objePregunta);
				ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

				break;
			case 3:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("\nIngredientes faltantes para " + objePregunta.getNombre() + ":");

				Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(objePregunta);
				if (faltantes.isEmpty())
					System.out.println("No faltan ingredientes directos!");
				else
					faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));
				break;
			case 4:

				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("\nNuevos ingredientes básicos faltantes para " + objePregunta.getNombre() + ":");
				Map<Objeto, Integer> faltantesBasicos2 = sistema.ingredientesBasicosFaltantesParaCraftear(objePregunta);
				if (faltantesBasicos2.isEmpty())
					System.out.println("No faltan ingredientes básicos!");
				else
					faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));

				break;
			case 5:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("Cantidad de " + objePregunta.getNombre() + " crafteables ahora: "
						+ sistema.cantidadCrafteable(objePregunta));
				break;
			case 6:

				try {
					objePregunta = seleccionarObjetoCrafteable();
					System.out.println("\n=== Intentando craftear 1 unidad de " + objePregunta);
					System.out.println("Tiempo Total (seg): " + sistema.craftearObjeto(objePregunta, 1));
					System.out.println(objePregunta.getNombre()+" creado Existosamente.");
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
				break;
			case 7:
				System.out.println("\n=== Historial de crafteo:");
				sistema.getHistorial().forEach((registro) -> System.out.println(registro));
				break;
			case 96:
				objePregunta = seleccionarObjetoFarmeable();
				int cantidadFarmeada;
				boolean salir = true;
				
				do {
					System.out.print("Ingrese la cantidad farmeada entre 1 a 20: ");
					while (!scanner.hasNextInt()) {
						System.out.println("Entrada inválida. Por favor, elige un número.");
						scanner.next(); // Limpiar entrada incorrecta
						System.out.print("Ingrese la cantidad farmeada entre 1 a 20: ");
					}
					cantidadFarmeada = scanner.nextInt();

	
					if (cantidadFarmeada > 0 && cantidadFarmeada < 21)
						salir = false;
					else
						System.out.println("La cantidad debe ser entre 1 y 20, vuelva a intentar.");
	
				} while (salir);
				
				inventario.agregarObjeto(objePregunta, cantidadFarmeada);
				System.out.println(objePregunta+"- Cantidad:" +cantidadFarmeada +", Agregado al inventario.");
				break;
			case 97:
				System.out.println(recetario);
				break;
			case 98:
				System.out.println(inventario);
				break;
			case 99:
				System.out.println("\nProlog:");
				recetario.PrologGenerarRecetas();
				inventario.prologGenerarInventario();
				inventario.consultaDeProlog();
				break;
			case 100:
				seleccionarEscenario();
				break;
			case 0:
				System.out.println("Saliendo del programa. ¡Hasta pronto!");
				System.out.println("Guardando Inventario...");
				new InventarioGSON(Config.RUTA_FIN_INVENTARIO).guardar(inventario.getObjetos());

				break;
			default:
				System.out.println("Opción inválida. Por favor, elige entre las opciones disponibles.");
			}

			System.out.println("");

			System.out.println("\nPresione Enter para continuar...");
			scanner.nextLine(); // Espera a que el usuario presione Enter
			scanner.nextLine();
			// Simular limpiar consola (en Eclipse, imprime saltos de línea)

			// System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.flush();

		} while (opcion != 0);

		scanner.close();

		// escenarioCraftearHachaDePiedraConUnaReceta();
		// escenarioCraftearMesaDeFundicionYSusRecetas();
	}

	private static Objeto seleccionarObjetoFarmeable() {

		List<Objeto> listaCrafteable = recetario.listaObjetosRecolectables();
		int idObjeto;
		boolean salir = true;
		Scanner scanner = new Scanner(System.in);

		do {
			// Leer ID del usuario
			System.out.print("Elige el ID del objeto: ");
			while (!scanner.hasNextInt()) {
				System.out.println("ID inválido. Por favor, elige un número dentro del listado");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige el ID del objeto: ");
			}

			idObjeto = scanner.nextInt();

			if (idObjeto > 0 && idObjeto < (listaCrafteable.size() + 1))
				salir = false;
			else
				System.out.println("ID inválido. Por favor, elige un número dentro del listado.");

		} while (salir);

		return listaCrafteable.get(idObjeto - 1);
	}

	private static Objeto seleccionarObjetoCrafteable() {

		List<Objeto> listaCrafteable = recetario.listaCrafteable();
		int idObjeto;
		boolean salir = true;
		Scanner scanner = new Scanner(System.in);

		do {
			// Leer ID del usuario
			System.out.print("Elige el ID del objeto: ");
			while (!scanner.hasNextInt()) {
				System.out.println("ID inválido. Por favor, elige un número dentro del listado");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige el ID del objeto: ");
			}

			idObjeto = scanner.nextInt();

			if (idObjeto > 0 && idObjeto < (listaCrafteable.size() + 1))
				salir = false;
			else
				System.out.println("ID inválido. Por favor, elige un número dentro del listado.");

		} while (salir);

		return listaCrafteable.get(idObjeto - 1);
	}

	// Método para mostrar el menú
	private static void mostrarMenu() {
		System.out.println("===== MENÚ PRINCIPAL =====");
		System.out.println("1. ¿Qué necesito para craftear un objeto? - Solo primer nivel.");
		System.out.println("2. ¿Qué necesito para craftear un objeto desde cero?");
		System.out.println("3. ¿Qué me falta para craftear un objeto? - Solo primer nivel.");
		System.out.println("4. ¿Qué me falta para craftear un objeto desde cero?");
		System.out.println("5. ¿Cuántos puedo craftear? ##FALTA EL TIEMPO##");
		System.out.println("6. Realizar el crafteo indicado");
		System.out.println("7. Historial de crafteos");
		System.out.println("96. Recolectar Objetos Básicos.");
		System.out.println("97. Mostrar Recetario.");
		System.out.println("98. Mostrar inventario.");
		System.out.println(
				"99. Consulta Prolog -¿Cuáles son todos los productos que podría generar con el inventario actual?");
		System.out.println("100. Escenarios pre-definidos.");
		System.out.println("0. Salir");
		System.out.println("=========================");
	}

	private static void seleccionarEscenario() {
		System.out.println("1. Escenarios Crear Hacha de Piedra con una receta.");
		System.out.println("2. Escenarios Mesa de fundición.");
		
		boolean salir = true;
		Scanner scanner = new Scanner(System.in);
		int intescenario;
		
		do {
			// Leer ID del usuario
			System.out.print("Elige el N° de escenario: ");
			while (!scanner.hasNextInt()) {
				System.out.println("N° inválido. Por favor, elige un número dentro del listado de escenarios.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige el ID del objeto: ");
			}

			intescenario = scanner.nextInt();

			if (intescenario > 0 && intescenario < 3)
				salir = false;
			else
				System.out.println("N° inválido. Por favor, elige un número dentro del listado de escenarios.");

		} while (salir);
		
		switch (intescenario) {
		case 1:
			escenarioCraftearHachaDePiedraConUnaReceta();
			break;
		case 2:
			escenarioCraftearMesaDeFundicionYSusRecetas();
			break;
		}
		
		
	}
	
	
	public static void inicio() {
		System.out.println("** Bienvenido al MedievalCraft **");
		System.out.println("-- Cargando Archivos de inicio.");

		inventario = new Inventario(new InventarioGSON(Config.RUTA_INICIO_INVENTARIO).cargar());
		recetario = new Recetario(new RecetaGSON(Config.RUTA_INICIO_RECETARIO).cargar());
		sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println("\n");
	}

	private static void escenarioCraftearHachaDePiedraConUnaReceta() {
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

	private static void escenarioCraftearMesaDeFundicionYSusRecetas() {
		final String RECETA_FUNDIDOR = "res/fundicion/recetaFundidor.json";
		final String INVENTARIO_FUNDIDOR = "res/fundicion/inventarioFundidor.json";
		final String INVENTARIO_FUNDICION = "res/fundicion/inventarioFundicion.json";
		final String RECETAS_FUNDICION = "res/fundicion/recetasFundicion.json";

		Inventario inventario = new Inventario(new InventarioGSON(INVENTARIO_FUNDIDOR).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(RECETA_FUNDIDOR).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println("====Agrego mas materiales y recetas de fundicion====");
		inventario.agregarObjetos(new InventarioGSON(INVENTARIO_FUNDICION).cargar());
		recetario.agregarRecetas(new RecetaGSON(RECETAS_FUNDICION).cargar());
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
		System.out.println("======Crafteando las recetas de fundicion========");
		try {
			for (Receta r : recetario.getRecetas()) {
				System.out.println("Crafting [" + r.getObjetoProducido() + "] en "
						+ sistema.craftearObjeto(r.getObjetoProducido(), 1));
				System.out.println(inventario);
				System.out.println("===========================");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}
