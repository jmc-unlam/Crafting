package main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import datos.json.InventarioGSON;
import datos.json.RecetaGSON;
import modelo.Inventario;
import modelo.Objeto;
import modelo.Recetario;
import modelo.Resultado;
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
			Map<Objeto, Integer> ingredientes;
			
			// Ejecutar acción según la opción
			switch (opcion) {
			case 1:
				sistema.ingredientesNecesariosConCantidad(seleccionarObjetoCrafteable()).informarCantidadOpcion1();;
				interrupcion(scanner);
				break;
			case 2:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("=== Ingredientes basicos necesarios para " + objePregunta.getNombre() + " ===");
				ingredientes = sistema.ingredientesBasicosNecesarios(objePregunta);
				ingredientes.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
				interrupcion(scanner);
				break;
			case 3:
				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("\nIngredientes faltantes para " + objePregunta.getNombre() + ":");

				Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(objePregunta);
				if (faltantes.isEmpty())
					System.out.println("No faltan ingredientes directos!");
				else
					faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));
				interrupcion(scanner);
				break;
			case 4:

				objePregunta = seleccionarObjetoCrafteable();

				System.out.println("\nNuevos ingredientes básicos faltantes para " + objePregunta.getNombre() + ":");
				Map<Objeto, Integer> faltantesBasicos2 = sistema.ingredientesBasicosFaltantesParaCraftear(objePregunta);
				if (faltantesBasicos2.isEmpty())
					System.out.println("No faltan ingredientes básicos!");
				else
					faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
				interrupcion(scanner);
				break;
			case 5:
				// Calcula cuantos objetos se pueden craftear con el inventario actual.
				inventario.cantidadPosibleCraftear(seleccionarObjetoCrafteable(), recetario).informarCantidadOpcion5();
				interrupcion(scanner);
				break;
			case 6:
				// Craftear una unidad de un objeto especifico.
				try {
					objePregunta = seleccionarObjetoCrafteable();
					new Resultado(1,sistema.craftearObjeto(objePregunta, 1),objePregunta).informarTiempoCrafteoOpcion6();
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
				interrupcion(scanner);
				break;
			case 7:				
				System.out.println(sistema.getHistorialReal().toString());
				interrupcion(scanner);
				break;
			case 8:
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
				System.out.println(objePregunta + "- Cantidad:" + cantidadFarmeada + ", Agregado al inventario.");
				interrupcion(scanner);
				break;
			case 9:
				System.out.println(recetario);
				interrupcion(scanner);
				break;
			case 10:
				System.out.println(inventario);
				interrupcion(scanner);
				break;
			case 11:
				System.out.println("\nProlog:");
				recetario.PrologGenerarRecetas();
				inventario.prologGenerarInventario();
				inventario.consultaDeProlog();
				interrupcion(scanner);
				break;
			case 12:
				Escenarios.seleccionarEscenario();
				interrupcion(scanner);
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

			// Simular limpiar consola (en Eclipse, imprime saltos de línea)
			System.out.flush();

		} while (opcion != 0);

		scanner.close();

	}

	private static void interrupcion(Scanner scanner) {
		System.out.println("\nPresione Enter para continuar...");
		scanner.nextLine(); // Espera a que el usuario presione Enter
		scanner.nextLine();
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
		System.out.println("5. ¿Cuántos puedo craftear?");
		System.out.println("6. Realizar el crafteo indicado");
		System.out.println("7. Historial de crafteos");
		System.out.println("8. Recolectar Objetos Básicos.");
		System.out.println("9. Mostrar Recetario.");
		System.out.println("10. Mostrar inventario.");
		System.out.println(
				"11. Consulta Prolog -¿Cuáles son todos los productos que podría generar con el inventario actual? - Primer nivel.");
		System.out.println("12. Escenarios pre-definidos.");
		System.out.println("0. Salir");
		System.out.println("=========================");
	}


	public static void inicio() {
		System.out.println("** Bienvenido al MedievalCraft **");
		System.out.println("-- Cargando Archivos de inicio.");

		inventario = new Inventario(new InventarioGSON(Config.RUTA_INICIO_INVENTARIO).cargar());
		recetario = new Recetario(new RecetaGSON(Config.RUTA_INICIO_RECETARIO).cargar());
		sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println("\n");
	}

}
