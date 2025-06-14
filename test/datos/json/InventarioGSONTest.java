package datos.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
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

class InventarioGSONTest {
    private static final String TEST_DIR = "target/test-temp/";
    private static final String INVENTARIO_VACIO_JSON = TEST_DIR + "inventario_vacio.json";
    private static final String INVENTARIO_BASICOS_JSON = TEST_DIR + "inventario_basicos.json";
    private static final String INVENTARIO_INTERMEDIOS_JSON = TEST_DIR + "inventario_intermedios.json";
    private static final String INVENTARIO_MIXTO_JSON = TEST_DIR + "inventario_mixto.json";
    private static final String INVENTARIO_DESCONOCIDO_JSON = TEST_DIR + "inventario_desconocido.json";
    private static final String INVENTARIO_VACIO_SALIDA_JSON = TEST_DIR + "inventario_vacio_salida.json";
    private static final String INVENTARIO_MIXTO_SALIDA_JSON = TEST_DIR + "inventario_mixto_salida.json";

    @BeforeEach
    void setUp() throws Exception {
        Files.createDirectories(Path.of(TEST_DIR));

        try (FileWriter writer = new FileWriter(INVENTARIO_VACIO_JSON)) {
            writer.write("[]");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_BASICOS_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Madera de Roble\",\n" +
                    "      \"tipo\": \"basico\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 10\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Piedra\",\n" +
                    "      \"tipo\": \"basico\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 5\n" +
                    "  }\n" +
                    "]");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_INTERMEDIOS_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Tablones de Roble\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 4\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Palo\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 8\n" +
                    "  }\n" +
                    "]");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_MIXTO_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Mineral de Hierro\",\n" +
                    "      \"tipo\": \"basico\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 8\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Tablones de Roble\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 3\n" +
                    "  }\n" +
                    "]");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_DESCONOCIDO_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"Ruby\",\n" +
                    "      \"tipo\": \"desconocido\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 1\n" +
                    "  }\n" +
                    "]");
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
        assertTrue(it.next() instanceof ObjetoBasico);
        assertTrue(it.next() instanceof ObjetoIntermedio);
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
        assertTrue(it.next() instanceof ObjetoIntermedio);
        assertTrue(it.next() instanceof ObjetoBasico);
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
			//System.err.println("Error de parseo al cargar en test ");
		}
	}

    @Test
    void cargarInventarioConArchivoInvalidoEsVacio() {
        Map<Objeto, Integer> objetos = new InventarioGSON("null").cargar();
        assertTrue(objetos.isEmpty());
    }
}