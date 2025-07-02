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

			// Ejecutar acción según la opción
			switch (opcion) {
			case 1: // 1. ¿Qué necesito para craftear un objeto? - Solo primer nivel.
				sistema.ingredientesNecesariosConCantidad(seleccionarObjetoCrafteable()).informarCantidadOpcion1();
				interrupcion(scanner);
				break;
			case 2: // 2. ¿Qué necesito para craftear un objeto desde cero?
				sistema.ingredientesBasicosNecesariosConTiempo(seleccionarObjetoCrafteable()).informarCantidadOpcion2();
				interrupcion(scanner);
				break;
			case 3: // 3. ¿Qué me falta para craftear un objeto? - Solo primer nivel.
				sistema.ingredientesFaltantesParaCraftearConTiempo(seleccionarObjetoCrafteable())
						.informarCantidadOpcion3();
				interrupcion(scanner);
				break;
			case 4: // 4. ¿Qué me falta para craftear un objeto desde cero?
				sistema.ingredientesBasicosFaltantesParaCraftearConTiempo(seleccionarObjetoCrafteable())
						.informarCantidadOpcion4();
				interrupcion(scanner);
				break;
			case 5: // 5. ¿Cuántos puedo craftear?
				// Calcula cuantos objetos se pueden craftear con el inventario actual.
				inventario.cantidadPosibleCraftear(seleccionarObjetoCrafteable(), recetario).informarCantidadOpcion5();
				interrupcion(scanner);
				break;
			case 6: // 6. Realizar el crafteo indicado
				// Craftear una unidad de un objeto especifico.
				try {
					objePregunta = seleccionarObjetoCrafteable();
					new Resultado(1, sistema.craftearObjeto(objePregunta, 1), objePregunta)
							.informarTiempoCrafteoOpcion6();
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
				interrupcion(scanner);
				break;
			case 7: // 7. Historial de crafteos
				System.out.println(sistema.getHistorialReal().toString());
				interrupcion(scanner);
				break;
			case 8: // 8. Recolectar Objetos Básicos / Comprar Intermedios o Vender del inventario.
				menuRecolectarVenderComprar(scanner);
				break;
			case 9: // 9. Mostrar Recetario."
				System.out.println(recetario);
				interrupcion(scanner);
				break;
			case 10: // 10. Mostrar inventario.
				System.out.println(inventario);
				interrupcion(scanner);
				break;
			case 11: // 11. Consulta PROLOG.
				System.out.println("\nProlog:");
				recetario.PrologGenerarRecetas();
				inventario.prologGenerarInventario();
				inventario.consultaDeProlog();
				interrupcion(scanner);
				break;
			case 12: // 12. Listar Escenarios
				Escenarios.seleccionarEscenario();
				interrupcion(scanner);
				break;
			case 13: // 13 Extra - Arbol.
				sistema.mostrarArbolCrafteo(seleccionarObjetoCrafteable());
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

			System.out.flush();

		} while (opcion != 0);

		scanner.close();

	}
	private static void menuRecolectarVenderComprar(Scanner scanner) {
		System.out.println("\n===== MENÚ para la modificación del inventario rapidamente =====");
		int opcion;
		
		do {
			System.out.println("1. Recolectar Objetos Básicos.");
			System.out.println("2. Comprar Intermedios.");
			System.out.println("3. Vender del inventario.");
			System.out.println("0. Volver.\n");
		
			// Leer opción del usuario
			System.out.print("Elige una opción: ");
			while (!scanner.hasNextInt()) {
				System.out.println("Entrada inválida. Por favor, elige un número entre 1 a 3.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print("Elige una opción: ");
			}
			opcion = scanner.nextInt();
			Objeto objePregunta;
			
			switch(opcion) {
			case 1: // Recolectar objeto basico.
				objePregunta = seleccionarObjetoFarmeable();
				int cantidadFarmeada= ingresarCantidadPara("farmeada",scanner);
				
				if(cantidadFarmeada>0) {
					inventario.agregarObjeto(objePregunta, cantidadFarmeada);
					System.out.println(objePregunta + "- Cantidad:" + cantidadFarmeada + ", Agregado al inventario.\n");
				}
				break;
			case 2: // Comprar objeto intermedio.
				objePregunta = seleccionarObjetoCrafteable();
				int cantidadAComprar= ingresarCantidadPara("a comprar",scanner);
				
				if(cantidadAComprar>0) {
					inventario.agregarObjeto(objePregunta, cantidadAComprar);
					System.out.println(objePregunta + "- Cantidad:" + cantidadAComprar + ", Agregado al inventario.\n");
				}
				
				break;
			case 3: // vender del inventario
				// la idea es desde un listado de todo lo del inventario, seleccionar uno y cargar la cantidad a vender.
				// esto ayuda a testear principalmente agregar y quitar la mesa.
				System.out.println("###########FALTA DESARROLLAR############.\n");
				break;
			default:
			
			}
			
		}while(opcion != 0);
		
	}
	
	private static int ingresarCantidadPara(String accionAinformar, Scanner scanner) {
		
		int cantidadFarmeada;
		boolean salir = true;
		String mesanje = "Ingrese la cantidad " + accionAinformar + " entre 0 a 20: ";
		
		do {
			System.out.print(mesanje);
			while (!scanner.hasNextInt()) {
				System.out.println("Entrada inválida. Por favor, elige un número.");
				scanner.next(); // Limpiar entrada incorrecta
				System.out.print(mesanje);
			}
			cantidadFarmeada = scanner.nextInt();

			if (cantidadFarmeada >= 0 && cantidadFarmeada < 21)
				salir = false;
			else
				System.out.println("La cantidad debe ser entre 0 y 20, vuelva a intentar.");

		} while (salir);
		
		return cantidadFarmeada;
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
		System.out.println("8. Recolectar Objetos Básicos / Comprar Intermedios / Vender del inventario.");
		System.out.println("9. Mostrar Recetario.");
		System.out.println("10. Mostrar inventario.");
		System.out.println(
				"11. Consulta Prolog -¿Cuáles son todos los productos que podría generar con el inventario actual? - Primer nivel.");
		System.out.println("12. Escenarios pre-definidos.");
		System.out.println("13. Extra - Arbol de Crafteo.");
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
