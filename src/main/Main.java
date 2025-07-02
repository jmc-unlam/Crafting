package main;

import java.util.List;
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
			mostrarMenu();
			opcion = devolverOpcion(scanner, "Elige una opción: ");

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
				recetario.prologGenerarRecetas();
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

	/**
	 * Ingreso por teclado y control del mismo, si o si debe ser valor int.
	 * 
	 * @param mensajeInicio mensaje de inicio.
	 * @param scanner       Scanner del menú.
	 * @return devuelve un valor int, ingresado por teclado.
	 */
	private static int devolverOpcion(Scanner scanner, String mensajeInicio) {
		System.out.print(mensajeInicio);
		while (!scanner.hasNextInt()) {
			System.out.println("Entrada inválida. Por favor, elige un número.");
			scanner.next(); // Limpiar entrada incorrecta
			System.out.print(mensajeInicio);
		}
		return scanner.nextInt();
	}

	/**
	 * Menú para la Recolección , Venta o Comprar, que cambia la cantidad dentro del inventario.
	 * Este menú brinda las opciones para alterar el inventario desde los objetos y sin necesidad
	 * de craftear, ingresando por teclado el objeto y la cantidad según cada opción.
	 * 
	 * @param scanner del menú principal
	 */
	
	private static void menuRecolectarVenderComprar(Scanner scanner) {
		System.out.println("\n===== MENÚ para la modificación del inventario rapidamente =====");
		int opcion;

		do {
			System.out.println("1. Recolectar Objetos Básicos.");
			System.out.println("2. Comprar Intermedios.");
			System.out.println("3. Vender del inventario.");
			System.out.println("4. Consultar inventario.\n");
			System.out.println("0. Volver.\n");

			opcion = devolverOpcion(scanner, "Elige una opción: ");
			Objeto objePregunta;

			switch (opcion) {
			case 1: // Recolectar objeto basico.
				objePregunta = seleccionarObjetoFarmeable();
				int cantidadFarmeada = ingresarCantidadPara("farmeada", scanner);

				if (cantidadFarmeada > 0) {
					inventario.agregarObjeto(objePregunta, cantidadFarmeada);
					System.out.println(objePregunta + "- Cantidad:" + cantidadFarmeada + ", Agregado al inventario.\n");
				}
				break;
			case 2: // Comprar objeto intermedio.
				objePregunta = seleccionarObjetoCrafteable();
				int cantidadAComprar = ingresarCantidadPara("a comprar", scanner);

				if (cantidadAComprar > 0) {
					try {
						inventario.agregarObjeto(objePregunta, cantidadAComprar);
						recetario.agregarRecetas(objePregunta.listaDeRecetasPropias());
						System.out.println(
								objePregunta + "- Cantidad:" + cantidadAComprar + ", Agregado al inventario.\n");
					} catch (Exception e) {
						System.err.println("Error: " + e.getMessage());
					}
				}
				break;
			case 3: // verder

				int opcionIDObjeto;
				do {
					System.out.println(inventario);
					System.out.println("0= para salir.\n");

					opcionIDObjeto = devolverOpcion(scanner, "Elige una id del objeto en el inventario: ");
					if (opcionIDObjeto != 0) {
						int cantidadAVender = ingresarCantidadPara("a vender", scanner);

						if (opcionIDObjeto != 0) {
							if (inventario.removerCantidadDeUnObjetoSegunNro(opcionIDObjeto, cantidadAVender,
									recetario))
								opcionIDObjeto = 0;
						}
					}

				} while (opcionIDObjeto != 0);

				break;
			case 4:
				System.out.println(inventario);
				interrupcion(scanner);
				break;
			default:
				System.out.println("Entrada inválida. Por favor, elige un número entre 1 a 3.");
			}

		} while (opcion != 0);

	}

	/**Pide por teclado el ingreso de un valor entre 0 y 50, usado para ingresas cantidades de
	 * farmeo, compra y venta. El valor 0 es admitible por q se usa como opción en los menús 
	 * 
	 * @param accionAinformar Texto donde completa la frase *"Ingrese la cantidad " + accionAinformar + " entre 0 a 50:* "
	 * @param scanner del Menú principal.
	 * @return
	 */
	private static int ingresarCantidadPara(String accionAinformar, Scanner scanner) {

		int cantidadFarmeada;
		boolean salir = true;
		int maximaCantidad = 50;
		String mesanje = "Ingrese la cantidad " + accionAinformar + " entre 0 a " + maximaCantidad + ": ";

		do {
			cantidadFarmeada = devolverOpcion(scanner, mesanje);

			if (cantidadFarmeada >= 0 && cantidadFarmeada < (maximaCantidad + 1))
				salir = false;
			else
				System.out.println("La cantidad debe ser entre 0 y " + maximaCantidad + ", vuelva a intentar.");

		} while (salir);

		return cantidadFarmeada;
	}

	/**interrupción creada usando el scanner. Su incorporación evita el despliege del menú luego de realizar una opción. 
	 * 
	 * @param scanner del Menu principal.
	 */
	private static void interrupcion(Scanner scanner) {
		System.out.println("\nPresione Enter para continuar...");
		scanner.nextLine(); // Espera a que el usuario presione Enter
		scanner.nextLine();
	}

	/**Muestra el listado de objetos Recolectables. Del cual se necesta ingresar un nro para seleccionarlo y devolver.
	 * Este metodo usa la info. cargada en el recetario. Filtrando solo los objetos básicos.
	 * 
	 * @return Objeto Seleccionado del listado.
	 */
	private static Objeto seleccionarObjetoFarmeable() {

		List<Objeto> listaCrafteable = recetario.listaObjetosRecolectables();
		int idObjeto;
		boolean salir = true;
		Scanner scanner = new Scanner(System.in);

		do {
			idObjeto = devolverOpcion(scanner, "Ingrese el Nro de Objeto a Recolectar."); // Leer ID del usuario

			if (idObjeto > 0 && idObjeto < (listaCrafteable.size() + 1))
				salir = false;
			else
				System.out.println("ID inválido. Por favor, elige un número dentro del listado.");

		} while (salir);

		return listaCrafteable.get(idObjeto - 1);
	}

	/**Muestra el listado de objetos Intermedios. Del cual se necesta ingresar un nro para seleccionarlo y devolver.
	 * Este metodo usa la info. cargada en el recetario. Filtrando solo los objetos Intermedios.
	 * 
	 * @return Objeto Seleccionado del listado.
	 */
	private static Objeto seleccionarObjetoCrafteable() {

		List<Objeto> listaCrafteable = recetario.listaCrafteable();
		int idObjeto;
		boolean salir = true;
		Scanner scanner = new Scanner(System.in);

		do {
			idObjeto = devolverOpcion(scanner,"Elige el ID del objeto: ");

			if (idObjeto > 0 && idObjeto < (listaCrafteable.size() + 1))
				salir = false;
			else
				System.out.println("ID inválido. Por favor, elige un número dentro del listado.");

		} while (salir);

		return listaCrafteable.get(idObjeto - 1);
	}

	/**
	 * Método para mostrar el menú Principal.
	 *
	 */
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
		System.out.println("13. Extra - Arbol de Crafteo.\n");
		System.out.println("0. Salir");
		System.out.println("=========================");
	}

	/**
	 * Inicia el main, ingresando los archivos iniciales de configuración.
	 */
	public static void inicio() {
		System.out.println("** Bienvenido al MedievalCraft **");
		System.out.println("-- Cargando Archivos de inicio.");

		inventario = new Inventario(new InventarioGSON(Config.RUTA_INICIO_INVENTARIO).cargar()); //inventario = new Inventario(); si se quiere probar el inventario vacio.
		
		recetario = new Recetario(new RecetaGSON(Config.RUTA_INICIO_RECETARIO).cargar());
		sistema = new SistemaDeCrafteo(inventario, recetario);

		System.out.println("\n");
	}

}
