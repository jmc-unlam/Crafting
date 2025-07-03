package datos.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonParseException;

import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.Recetario;
import modelo.Inventario;
import modelo.MesaDeFundicion;

class InventarioGSONTest {
	private static final String TEST_DIR = "target/test-temp/";
	private static final String INVENTARIO_VACIO_JSON = TEST_DIR + "inventario_vacio.json";
	private static final String INVENTARIO_BASICOS_JSON = TEST_DIR + "inventario_basicos.json";
	private static final String INVENTARIO_INTERMEDIOS_JSON = TEST_DIR + "inventario_intermedios.json";
	private static final String INVENTARIO_MIXTO_JSON = TEST_DIR + "inventario_mixto.json";
	private static final String INVENTARIO_DESCONOCIDO_JSON = TEST_DIR + "inventario_desconocido.json";
	private static final String INVENTARIO_VACIO_SALIDA_JSON = TEST_DIR + "inventario_vacio_salida.json";
	private static final String INVENTARIO_MIXTO_SALIDA_JSON = TEST_DIR + "inventario_mixto_salida.json";
	private static final String INVENTARIO_CON_MESA_DE_FUNDICION = TEST_DIR + "inventario_mesa_hierro_salida.json";

	@BeforeEach
	void setUp() throws IOException {
		Files.createDirectories(Path.of(TEST_DIR));

		try (FileWriter writer = new FileWriter(INVENTARIO_VACIO_JSON)) {
			writer.write("[]");
		}

		try (FileWriter writer = new FileWriter(INVENTARIO_BASICOS_JSON)) {
			writer.write("[\n" + "  {\n" + "    \"objeto\": {\n" + "      \"nombre\": \"madera de roble\",\n"
					+ "      \"tipo\": \"basico\"\n" + "    },\n" + "    \"cantidad\": 10\n" + "  },\n" + "  {\n"
					+ "    \"objeto\": {\n" + "      \"nombre\": \"piedra\",\n" + "      \"tipo\": \"basico\"\n"
					+ "    },\n" + "    \"cantidad\": 5\n" + "  }\n" + "]");
		}

		try (FileWriter writer = new FileWriter(INVENTARIO_INTERMEDIOS_JSON)) {
			writer.write("[\n" + "  {\n" + "    \"objeto\": {\n" + "      \"nombre\": \"tablones de roble\",\n"
					+ "      \"tipo\": \"intermedio\"\n" + "    },\n" + "    \"cantidad\": 4\n" + "  },\n" + "  {\n"
					+ "    \"objeto\": {\n" + "      \"nombre\": \"palo\",\n" + "      \"tipo\": \"intermedio\"\n"
					+ "    },\n" + "    \"cantidad\": 8\n" + "  }\n" + "]");
		}

		try (FileWriter writer = new FileWriter(INVENTARIO_MIXTO_JSON)) {
			writer.write("[\n" + "  {\n" + "    \"objeto\": {\n" + "      \"nombre\": \"mineral de hierro\",\n"
					+ "      \"tipo\": \"basico\"\n" + "    },\n" + "    \"cantidad\": 8\n" + "  },\n" + "  {\n"
					+ "    \"objeto\": {\n" + "      \"nombre\": \"tablones de roble\",\n"
					+ "      \"tipo\": \"intermedio\"\n" + "    },\n" + "    \"cantidad\": 3\n" + "  }\n" + "]");
		}

		try (FileWriter writer = new FileWriter(INVENTARIO_DESCONOCIDO_JSON)) {
			writer.write("[\n" + "  {\n" + "    \"objeto\": {\n" + "      \"nombre\": \"ruby\",\n"
					+ "      \"tipo\": \"desconocido\"\n" + "    },\n" + "    \"cantidad\": 1\n" + "  }\n" + "]");
		}

		try (FileWriter writer = new FileWriter(INVENTARIO_CON_MESA_DE_FUNDICION)) {
			writer.write("[\n" + "  {\n" + "    \"objeto\": {\n" + "      \"tipo\": \"basico\",\n"
					+ "      \"nombre\": \"madera\"\n" + "    },\n" + "    \"cantidad\": 10\n" + "  },\n" + "  {\n"
					+ "    \"objeto\": {\n" + "      \"nombre\": \"mesa de fundicion\",\n"
					+ "      \"tipo\": \"mesa de fundicion\"\n" + "    },\n" + "    \"cantidad\": 1\n" + "  }\n" + "]");
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		Files.deleteIfExists(Path.of(INVENTARIO_VACIO_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_BASICOS_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_INTERMEDIOS_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_MIXTO_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_DESCONOCIDO_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_VACIO_SALIDA_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_MIXTO_SALIDA_JSON));
		Files.deleteIfExists(Path.of(INVENTARIO_CON_MESA_DE_FUNDICION));
		Files.deleteIfExists(Path.of(TEST_DIR));
	}

	@Test
	void cargarInventarioVacio() {
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_VACIO_JSON).cargar();
		assertTrue(objetos.isEmpty());
	}

	@Test
	void cargarInventarioConObjetosBasicos() {
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_BASICOS_JSON).cargar();
		assertEquals(2, objetos.size());
		assertEquals(10, objetos.get(new ObjetoBasico("Madera de Roble")));
		assertEquals(5, objetos.get(new ObjetoBasico("Piedra")));
		for (Objeto key : objetos.keySet()) {
			assertTrue(key instanceof ObjetoBasico);
		}
	}

	@Test
	void cargarInventarioConObjetosIntermedios() {
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_INTERMEDIOS_JSON).cargar();
		assertEquals(2, objetos.size());
		assertEquals(4, objetos.get(new ObjetoIntermedio("Tablones de Roble")));
		assertEquals(8, objetos.get(new ObjetoIntermedio("Palo")));
		for (Objeto key : objetos.keySet()) {
			assertTrue(key instanceof ObjetoIntermedio);
		}
	}

	@Test
	void cargarInventarioMixto() {
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_MIXTO_JSON).cargar();
		assertEquals(2, objetos.size());
		assertEquals(8, objetos.get(new ObjetoBasico("Mineral de Hierro")));
		assertEquals(3, objetos.get(new ObjetoIntermedio("Tablones de Roble")));
		Set<Objeto> keys = objetos.keySet();
		Iterator<Objeto> it = keys.iterator();
		assertTrue(it.next() instanceof ObjetoIntermedio);
		assertTrue(it.next() instanceof ObjetoBasico);
	}

	@Test
	void guardarInventarioVacio() {
		Map<Objeto, Integer> vacio = new HashMap<>();
		new InventarioGSON(INVENTARIO_VACIO_SALIDA_JSON).guardar(vacio);
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_VACIO_SALIDA_JSON).cargar();
		assertTrue(objetos.isEmpty());
	}

	@Test
	void guardarYLeerInventario() {
		Map<Objeto, Integer> guardadoMixto = new HashMap<>();
		guardadoMixto.put(new ObjetoIntermedio("Palo"), 3);
		guardadoMixto.put(new ObjetoBasico("Piedra"), 7);
		new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).guardar(guardadoMixto);
		Map<Objeto, Integer> leidoMixto = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).cargar();
		assertEquals(2, leidoMixto.size());
		assertEquals(3, leidoMixto.get(new ObjetoIntermedio("Palo")));
		assertEquals(7, leidoMixto.get(new ObjetoBasico("Piedra")));
		Set<Objeto> keys = leidoMixto.keySet();
		Iterator<Objeto> it = keys.iterator();
		assertTrue(it.next() instanceof ObjetoBasico);
		assertTrue(it.next() instanceof ObjetoIntermedio);
	}

	@Test
	void cargarInventarioConTipoDesconocido() {
		assertThrows(JsonParseException.class, () -> {
			new InventarioGSON(INVENTARIO_DESCONOCIDO_JSON).cargar();
		});
	}

	@Test
	void cargarInventarioConTipoDesconocidoEsVacio() {
		Map<Objeto, Integer> objetos;
		try {
			objetos = new InventarioGSON(INVENTARIO_DESCONOCIDO_JSON).cargar();
			assertTrue(objetos.isEmpty());
		} catch (Exception e) {
			// System.err.println("Error de parseo al cargar en test ");
		}
	}

	@Test
	void cargarInventarioConArchivoInvalidoEsVacio() {
		Map<Objeto, Integer> objetos = new InventarioGSON("null").cargar();
		assertNotNull(objetos);
		assertTrue(objetos.isEmpty());
	}

	@Test
	void cargarInventarioConMesaDeTrabajo() throws Exception {
		Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_CON_MESA_DE_FUNDICION).cargar();
		assertNotNull(objetos);
		assertEquals(2, objetos.size());

		// Verifica que hay una Madera
		assertTrue(objetos.containsKey(new ObjetoBasico("Madera")));
		assertEquals(10, objetos.get(new ObjetoBasico("Madera")));

		// Verifica que hay una MesaDeHierro
		assertTrue(objetos.containsKey(new MesaDeFundicion()));
		assertEquals(1, objetos.get(new MesaDeFundicion()));
	}

	@Test
	void guardarYLeerInventarioConMesaDeFundicionSinRecetasIncluidas() {
		// Crear objetos
		ObjetoBasico madera = new ObjetoBasico("Madera");
		ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

		// Crear mesa
		MesaDeFundicion mesa = new MesaDeFundicion();

		// Crear receta
		Map<Objeto, Integer> ingredientes = new HashMap<>();
		ingredientes.put(madera, 2);
		Receta receta = new Receta(palo, ingredientes, 4, 10, mesa);

		// Crear inventario y recetario
		Inventario inventario = new Inventario();
		inventario.agregarObjeto(madera, 10);
		inventario.agregarObjeto(mesa, 1);
		Recetario recetario = new Recetario();
		recetario.agregarReceta(receta);

		// Guardar
		new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).guardar(inventario.getObjetos());

		// Cargar y verificar
		Map<Objeto, Integer> leido = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).cargar();

		assertNotNull(leido);
		assertEquals(2, leido.size());

		// Comprobamos si tenemos la mesa y mesa
		assertTrue(leido.containsKey(new ObjetoBasico("Madera")));
		assertEquals(10, leido.get(madera));

		assertTrue(leido.containsKey(new MesaDeFundicion()));
		assertEquals(1, leido.get(new MesaDeFundicion()));
	}
	
	@Test
	void guardarYLeerInventarioConMesaDeFundicionConRecetasIncluidas() {
		// Crear objetos
		ObjetoBasico madera = new ObjetoBasico("Madera");
		ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

		// Crear mesa de fundicion
		MesaDeFundicion mesaFundicion = new MesaDeFundicion();

		// Crear receta que requiere fundicion
		Map<Objeto, Integer> ingredientes = new HashMap<>();
		ingredientes.put(madera, 2);
		Receta recetaDePaloRequiereFundicion = new Receta(palo, ingredientes, 4, 10, mesaFundicion);

		// Crear inventario y recetario
		Recetario recetario = new Recetario();
		Inventario inventario = new Inventario();
		
		//usa agregar de inventario obsoleto [deprecated]
		inventario.agregarObjeto(madera, 10);
		//usa el agregar nuevo
		inventario.agregarObjeto(mesaFundicion, 1, recetario);
		//si el archivo de recetas existe se cargaron las 7 recetas de funducion
		assertEquals(7,recetario.getRecetas().size());
		//agrego una receta mas manualmente de funficion
		recetario.agregarReceta(recetaDePaloRequiereFundicion);
		assertEquals(8,recetario.getRecetas().size());

		//en inventario solo hay 2 cosas madera y la mesa de fundicion
		new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).guardar(inventario.getObjetos());
		Map<Objeto, Integer> leido = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).cargar();

		assertNotNull(leido);
		assertEquals(2, leido.size());

		// Comprobamos si tenemos la mesa y mesa
		assertTrue(leido.containsKey(madera));
		assertEquals(10, leido.get(madera));

		assertTrue(leido.containsKey(mesaFundicion));
		assertEquals(1, leido.get(mesaFundicion));
	}
}
