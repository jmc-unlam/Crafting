package datos.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonParseException;

import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.MesaDeFundicion;
import modelo.MesaDeTrabajo;

class RecetaGSONTest {

	private static final String TEST_DIR = "target/test-temp-recetas/";
	private static final String RECETAS_VACIO_JSON = TEST_DIR + "recetas_vacio.json";
	private static final String RECETAS_SIMPLES_JSON = TEST_DIR + "recetas_simples.json";
	private static final String RECETAS_COMPLEJAS_JSON = TEST_DIR + "recetas_complejas.json";
	private static final String RECETA_PRODUCE_BASICO_JSON = TEST_DIR + "receta_produce_basico.json";
	private static final String RECETA_INGREDIENTE_DESCONOCIDO_JSON = TEST_DIR + "receta_ingrediente_desconocido.json";
	private static final String RECETAS_SALIDA_JSON = TEST_DIR + "recetas_salida.json";
	private static final String RECETAS_SIN_NADA_JSON = TEST_DIR + "recetas_sin_nada.json";

	private ObjetoBasico madera;
	private ObjetoBasico carbon;
	private ObjetoIntermedio palo;
	private ObjetoIntermedio antorcha;
	private MesaDeTrabajo mesaFundicion;

	private Receta recetaPalo;
	private Receta recetaAntorcha;
	private Receta recetaMesaFundicion;

	@BeforeEach
	void setUp() throws IOException {
		Files.createDirectories(Path.of(TEST_DIR));

		// Objetos
		madera = new ObjetoBasico("Madera");
		carbon = new ObjetoBasico("Carbon");
		palo = new ObjetoIntermedio("Palo");
		antorcha = new ObjetoIntermedio("Antorcha");

		// recetas
		mesaFundicion = new MesaDeFundicion();
		Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
		ingredientesMesa.put(madera, 4);
		recetaMesaFundicion = new Receta(mesaFundicion, ingredientesMesa, 1, 20); // 4 Madera -> 1 Mesa, 20s

		Map<Objeto, Integer> ingredientesPalo = new HashMap<>();
		ingredientesPalo.put(madera, 2);
		recetaPalo = new Receta(palo, ingredientesPalo, 4, 10, mesaFundicion); // 2 Madera -> 4 Palos, 10s

		Map<Objeto, Integer> ingredientesAntorcha = new HashMap<>();
		ingredientesAntorcha.put(palo, 1);
		ingredientesAntorcha.put(carbon, 1);
		recetaAntorcha = new Receta(antorcha, ingredientesAntorcha, 4, 5); // 1 Palo, 1 Carbon -> 4 Antorchas, 5s

		// json estaticos
		try (FileWriter writer = new FileWriter(RECETAS_VACIO_JSON)) {
			writer.write("[]");
		}

		try (FileWriter writer = new FileWriter(RECETA_PRODUCE_BASICO_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objetoProducido\": {\n" +
                    "      \"nombre\": \"Madera\",\n" +
                    "      \"tipo\": \"basico\"\n" + 
                    "    },\n" +
                    "    \"ingredientes\": [],\n" +
                    "    \"cantidadProducida\": 1,\n" +
                    "    \"tiempoBase\": 1\n" +
                    "  }\n" +
                    "]");
        }

        try (FileWriter writer = new FileWriter(RECETA_INGREDIENTE_DESCONOCIDO_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objetoProducido\": {\n" +
                    "      \"nombre\": \"Antorcha\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"ingredientes\": [\n" +
                    "      {\n" +
                    "        \"objeto\": {\n" +
                    "          \"nombre\": \"ObjetoDesconocido\",\n" +
                    "          \"tipo\": \"desconocido\"\n" + // Desconocido
                    "        },\n" +
                    "        \"cantidad\": 1\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"cantidadProducida\": 4,\n" +
                    "    \"tiempoBase\": 5\n" +
                    "  }\n" +
                    "]");
        }
        
        try (FileWriter writer = new FileWriter(RECETAS_SIMPLES_JSON)) {
        	writer.write("[\n" +
                    "  {\n" +
                    "    \"objetoProducido\": {\n" +
                    "      \"nombre\": \"palo\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"ingredientes\": [\n" +
                    "      {\n" +
                    "        \"objeto\": {\n" +
                    "          \"nombre\": \"madera\",\n" +
                    "          \"tipo\": \"basico\"\n" + 
                    "        },\n" +
                    "        \"cantidad\": 2\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"cantidadProducida\": 4,\n" +
                    "    \"tiempoBase\": 10\n" +
                    "  }\n" +
                    "]");
        }
        
        try (FileWriter writer = new FileWriter(RECETAS_COMPLEJAS_JSON)) {
        	writer.write("[\n" +
                    "  {\n" +
                    "    \"objetoProducido\": {\n" +
                    "      \"nombre\": \"antorcha\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"ingredientes\": [\n" +
                    "      {\n" +
                    "        \"objeto\": {\n" +
                    "          \"nombre\": \"palo\",\n" +
                    "          \"tipo\": \"intermedio\"\n" + 
                    "        },\n" +
                    "        \"cantidad\": 1\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"objeto\": {\n" +
                    "          \"nombre\": \"carbon\",\n" +
                    "          \"tipo\": \"basico\"\n" + 
                    "        },\n" +
                    "        \"cantidad\": 1\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"cantidadProducida\": 4,\n" +
                    "    \"tiempoBase\": 5\n" +
                    "  }\n" +
                    "]");
        }
		
		try (FileWriter writer = new FileWriter(RECETAS_SIN_NADA_JSON)) {
			writer.write("");
		}
	}

	@AfterEach
	void tearDown() throws IOException {
		Files.deleteIfExists(Path.of(RECETAS_VACIO_JSON));
		Files.deleteIfExists(Path.of(RECETA_PRODUCE_BASICO_JSON));
		Files.deleteIfExists(Path.of(RECETA_INGREDIENTE_DESCONOCIDO_JSON));
		Files.deleteIfExists(Path.of(RECETAS_SIMPLES_JSON));
		Files.deleteIfExists(Path.of(RECETAS_COMPLEJAS_JSON));
		Files.deleteIfExists(Path.of(RECETAS_SALIDA_JSON));
		Files.deleteIfExists(Path.of(RECETAS_SIN_NADA_JSON));
		Files.deleteIfExists(Path.of(TEST_DIR));
	}

	@Test
	void cargarRecetasVacias() {
		List<Receta> recetasLeidas = new RecetaGSON(RECETAS_VACIO_JSON).cargar();
		assertNotNull(recetasLeidas);
		assertTrue(recetasLeidas.isEmpty());
	}

	@Test
	void cargarRecetasSimples() {
		List<Receta> recetasLeidas = new RecetaGSON(RECETAS_SIMPLES_JSON).cargar();
		assertEquals(1, recetasLeidas.size());
		assertTrue(recetasLeidas.get(0).equals(recetaPalo));
	}

	@Test
	void cargarRecetasComplejas() {
		RecetaGSON recetaGSON = new RecetaGSON(RECETAS_COMPLEJAS_JSON);
		List<Receta> recetasLeidas = recetaGSON.cargar();
		assertEquals(1, recetasLeidas.size());
		assertTrue(recetasLeidas.get(0).equals(recetaAntorcha));
	}

	@Test
	void guardarYcargarRecetasMixtas() {
		List<Receta> recetasGuardadas = List.of(recetaPalo, recetaAntorcha, recetaMesaFundicion);
		new RecetaGSON(RECETAS_SALIDA_JSON).guardar(recetasGuardadas);

		List<Receta> recetasLeidas = new RecetaGSON(RECETAS_SALIDA_JSON).cargar();
		assertEquals(recetasGuardadas.size(), recetasLeidas.size());

		assertTrue(recetasGuardadas.equals(recetasLeidas));
		assertTrue(recetasLeidas.equals(recetasGuardadas));
	}

	@Test
	void cargarRecetaQueProduceUnObjetoBasico() {
		assertThrows(IllegalStateException.class, () -> {
			new RecetaGSON(RECETA_PRODUCE_BASICO_JSON).cargar();
		});
	}

	@Test
	void cargarRecetaConIngredienteDesconocido() {
		assertThrows(JsonParseException.class, () -> {
			new RecetaGSON(RECETA_INGREDIENTE_DESCONOCIDO_JSON).cargar();
		});
	}

	@Test
	void cargarRecetaConArchivoInvalidoEsVacio() {
		List<Receta> recetasLeidas = new RecetaGSON("null").cargar();
		assertNotNull(recetasLeidas);
		assertTrue(recetasLeidas.isEmpty());
	}
	
	@Test
	void cargarRecetasSinNada() {
		List<Receta> recetasLeidas = new RecetaGSON(RECETAS_SIN_NADA_JSON).cargar();
		assertNotNull(recetasLeidas);
		assertTrue(recetasLeidas.isEmpty());
	}
}