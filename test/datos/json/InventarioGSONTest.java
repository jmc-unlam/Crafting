package datos.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import modelo.Inventario;
import modelo.MesaDeHierro;

class InventarioGSONTest {
    private static final String TEST_DIR = "target/test-temp/";
    private static final String INVENTARIO_VACIO_JSON = TEST_DIR + "inventario_vacio.json";
    private static final String INVENTARIO_BASICOS_JSON = TEST_DIR + "inventario_basicos.json";
    private static final String INVENTARIO_INTERMEDIOS_JSON = TEST_DIR + "inventario_intermedios.json";
    private static final String INVENTARIO_MIXTO_JSON = TEST_DIR + "inventario_mixto.json";
    private static final String INVENTARIO_DESCONOCIDO_JSON = TEST_DIR + "inventario_desconocido.json";
    private static final String INVENTARIO_VACIO_SALIDA_JSON = TEST_DIR + "inventario_vacio_salida.json";
    private static final String INVENTARIO_MIXTO_SALIDA_JSON = TEST_DIR + "inventario_mixto_salida.json";
    private static final String INVENTARIO_CON_MESA_DE_HIERRO = TEST_DIR + "inventario_mesa_hierro_salida.json";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Path.of(TEST_DIR));

        try (FileWriter writer = new FileWriter(INVENTARIO_VACIO_JSON)) {
            writer.write("[]");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_BASICOS_JSON)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"madera de roble\",\n" +
                    "      \"tipo\": \"basico\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 10\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"piedra\",\n" +
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
                    "      \"nombre\": \"tablones de roble\",\n" +
                    "      \"tipo\": \"intermedio\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 4\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"palo\",\n" +
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
                    "      \"nombre\": \"mineral de hierro\",\n" +
                    "      \"tipo\": \"basico\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 8\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"nombre\": \"tablones de roble\",\n" +
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
                    "      \"nombre\": \"ruby\",\n" +
                    "      \"tipo\": \"desconocido\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 1\n" +
                    "  }\n" +
                    "]");
        }
        
        try (FileWriter writer = new FileWriter(INVENTARIO_CON_MESA_DE_HIERRO)) {
            writer.write("[\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"tipo\": \"basico\",\n" +
                    "      \"nombre\": \"madera\"\n" +
                    "    },\n" +
                    "    \"cantidad\": 10\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"objeto\": {\n" +
                    "      \"tipo\": \"mesa\",\n" +
                    "      \"nombre\": \"mesa de hierro\",\n" +
                    "      \"recetasDesbloqueadas\": [\n" +
                    "        {\n" +
                    "          \"objetoProducido\": { \"tipo\": \"intermedio\", \"nombre\": \"palo\" },\n" +
                    "          \"ingredientes\": [\n" +
                    "            { \"objeto\": { \"tipo\": \"basico\", \"nombre\": \"madera\" }, \"cantidad\": 2 }\n" +
                    "          ],\n" +
                    "          \"cantidadProducida\": 4,\n" +
                    "          \"tiempoBase\": 10\n" +
                    "        }\n" +
                    "      ]\n" +
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
        Files.deleteIfExists(Path.of(INVENTARIO_CON_MESA_DE_HIERRO));
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
			//System.err.println("Error de parseo al cargar en test ");
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
        Map<Objeto, Integer> objetos = new InventarioGSON(INVENTARIO_CON_MESA_DE_HIERRO).cargar();
        assertNotNull(objetos);
        assertEquals(2, objetos.size());

        // Verifica que hay una Madera
        ObjetoBasico madera = new ObjetoBasico("Madera");
        assertTrue(objetos.containsKey(madera));
        assertEquals(10, objetos.get(madera));

        // Verifica que hay una MesaDeHierro
        for (Objeto key : objetos.keySet()) {
            if (!key.esBasico() && !key.esApilable()) {
                assertTrue(key instanceof MesaDeHierro);
                assertEquals(1, objetos.get(key));

                // Verifica que tiene recetas desbloqueadas
                MesaDeHierro mesa = (MesaDeHierro) key;
                assertEquals(1, mesa.getRecetasDesbloqueadas().size());
                Receta receta = mesa.getRecetasDesbloqueadas().get(0);

                // Verifica datos de la receta
                assertEquals(new ObjetoIntermedio("Palo"), receta.getObjetoProducido());
                assertEquals(4, receta.getCantidadProducida());
                assertEquals(10, receta.getTiempoBase());

                // Verifica ingredientes
                Map<Objeto, Integer> ing = receta.getIngredientes();
                assertEquals(1, ing.size());
                assertEquals(Integer.valueOf(2), ing.get(new ObjetoBasico("Madera")));
            }
        }
    }
    
    @Test
    void guardarInventarioConMesaDeTrabajo() {
        // Crear objetos
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

        // Crear receta
        Map<Objeto, Integer> ingredientes = new HashMap<>();
        ingredientes.put(madera, 2);
        Receta receta = new Receta(palo, ingredientes, 4, 10);

        // Crear mesa
        MesaDeHierro mesa = new MesaDeHierro(List.of(receta));

        // Crear inventario
        Inventario inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(mesa, 1);

        // Guardar
        InventarioGSON inventarioGson = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON);
        inventarioGson.guardar(inventario.getObjetos());

        // Cargar y verificar
        Map<Objeto, Integer> leido = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON).cargar();

        assertNotNull(leido);
        assertEquals(2, leido.size());

        // Comprobamos si tenemos la mesa
        boolean encontrada = false;
        for (Objeto obj : leido.keySet()) {
            if (!obj.esBasico() && !obj.esApilable()) {
                MesaDeHierro mesaLeida = (MesaDeHierro) obj;
                List<Receta> recetas = mesaLeida.getRecetasDesbloqueadas();
                assertEquals(1, recetas.size());
                assertTrue(receta.equals(recetas.get(0)));
                encontrada = true;
            }
        }
        assertTrue(encontrada);
    }
    
    @Test
    void guardarYLeerMesasNoPierdeDatos() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

        Map<Objeto, Integer> ingredientes = new HashMap<>();
        ingredientes.put(madera, 2);
        Receta receta = new Receta(palo, ingredientes, 4, 10);

        MesaDeHierro mesa = new MesaDeHierro(List.of(receta));

        Map<Objeto, Integer> guardado = new HashMap<>();
        guardado.put(madera, 10);
        guardado.put(mesa, 1);

        InventarioGSON inventarioGson = new InventarioGSON(INVENTARIO_MIXTO_SALIDA_JSON);
        inventarioGson.guardar(guardado);

        Map<Objeto, Integer> cargado = inventarioGson.cargar();
        assertNotNull(cargado);
        assertEquals(2, cargado.size());

        boolean encontrada = false;
        for (Objeto obj : cargado.keySet()) {
            if (!obj.esBasico() && !obj.esApilable()) {
                MesaDeHierro mesaCargada = (MesaDeHierro) obj;
                List<Receta> recetas = mesaCargada.getRecetasDesbloqueadas();
                assertEquals(1, recetas.size());
                assertTrue(receta.equals(recetas.get(0)));
                encontrada = true;
            }
        }
        assertTrue(encontrada);
    }
}