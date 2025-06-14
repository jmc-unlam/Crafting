package datos.xml;

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
import org.xml.sax.SAXException;

import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;

class InventarioXMLTest {
	private static final String TEST_DIR = "target/test-temp/";
    private static final String INVENTARIO_VACIO_XML = TEST_DIR + "inventario_vacio.xml";
    private static final String INVENTARIO_BASICOS_XML = TEST_DIR + "inventario_basicos.xml";
    private static final String INVENTARIO_INTERMEDIOS_XML = TEST_DIR + "inventario_intermedios.xml";
    private static final String INVENTARIO_MIXTO_XML = TEST_DIR + "inventario_mixto.xml";
    private static final String INVENTARIO_DESCONOCIDO_XML = TEST_DIR + "inventario_desconocido.xml";
    private static final String INVENTARIO_VACIO_SALIDA_XML = TEST_DIR + "inventario_vacio_salida.xml";
    private static final String INVENTARIO_MIXTO_SALIDA_XML = TEST_DIR + "inventario_mixto_salida.xml";

	@BeforeEach
	void setUp() throws Exception {
		// Ensure the test directory exists
        Files.createDirectories(Path.of(TEST_DIR));

        try (FileWriter writer = new FileWriter(INVENTARIO_VACIO_XML)) {
            writer.write("<objetos/>");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_BASICOS_XML)) {
            writer.write("<objetos>" +
                         "    <objeto _nombre=\"Madera de Roble\" tipo=\"basico\" cantidad=\"10\"/>" +
                         "    <objeto _nombre=\"Piedra\" tipo=\"basico\" cantidad=\"5\"/>" +
                         "</objetos>");
        }

        try (FileWriter writer = new FileWriter(INVENTARIO_INTERMEDIOS_XML)) {
            writer.write("<objetos>" +
                         "    <objeto _nombre=\"Tablones de Roble\" tipo=\"intermedio\" cantidad=\"4\"/>" +
                         "    <objeto _nombre=\"Palo\" tipo=\"intermedio\" cantidad=\"8\"/>" +
                         "</objetos>");
        }
        
        try (FileWriter writer = new FileWriter(INVENTARIO_MIXTO_XML)) {
            writer.write("<objetos>" +
                         "    <objeto _nombre=\"Mineral de Hierro\" tipo=\"basico\" cantidad=\"8\"/>" +
                         "    <objeto _nombre=\"Tablones de Roble\" tipo=\"intermedio\" cantidad=\"3\"/>" +
                         "</objetos>");
        }
        
        try (FileWriter writer = new FileWriter(INVENTARIO_DESCONOCIDO_XML)) {
            writer.write("<objetos>" +
                         "    <objeto _nombre=\"Ruby\" tipo=\"desconocido\" cantidad=\"1\"/>" +
                         "</objetos>");
        }
	}

	@AfterEach
	void tearDown() throws Exception {
        Files.deleteIfExists(Path.of(INVENTARIO_VACIO_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_BASICOS_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_INTERMEDIOS_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_MIXTO_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_DESCONOCIDO_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_VACIO_SALIDA_XML));
        Files.deleteIfExists(Path.of(INVENTARIO_MIXTO_SALIDA_XML));
        Files.deleteIfExists(Path.of(TEST_DIR)); 
	}

	@Test
	void cargarInventarioVacio() throws SAXException {
		Map<Objeto, Integer> objetos = new InventarioXML(INVENTARIO_VACIO_XML).cargar();
		assertTrue(objetos.isEmpty());
	}

	@Test
	void cargarInventarioConObjetosBasicos() throws SAXException {
		Map<Objeto, Integer> objetos = new InventarioXML(INVENTARIO_BASICOS_XML).cargar();
		assertTrue(objetos.size()==2);
		assertTrue(objetos.get(new ObjetoBasico("Madera de Roble")) == 10);
		assertTrue(objetos.get(new ObjetoBasico("Piedra")) == 5);
		for (Objeto key : objetos.keySet()) {
            assertTrue(key instanceof ObjetoBasico);
        }
	}
	
	@Test
	void cargarInventarioConObjetosIntermedios() throws SAXException {
		Map<Objeto, Integer> objetos = new InventarioXML(INVENTARIO_INTERMEDIOS_XML).cargar();
		assertTrue(objetos.size()==2);
		assertTrue(objetos.get(new ObjetoIntermedio("Tablones de Roble")) == 4);
		assertTrue(objetos.get(new ObjetoIntermedio("Palo")) == 8);
		for (Objeto key : objetos.keySet()) {
            assertTrue(key instanceof ObjetoIntermedio);
        }
	}
	
	@Test
	void cargarInventarioMixto() throws SAXException {
		Map<Objeto, Integer> objetos = new InventarioXML(INVENTARIO_MIXTO_XML).cargar();
		assertTrue(objetos.size()==2);
		assertTrue(objetos.get(new ObjetoBasico("Mineral de Hierro")) == 8);
		assertTrue(objetos.get(new ObjetoIntermedio("Tablones de Roble")) == 3);
		Set<Objeto> keys = objetos.keySet();
		Iterator<Objeto> it = keys.iterator();
		assertTrue(it.next() instanceof ObjetoBasico);
		assertTrue(it.next() instanceof ObjetoIntermedio);
	}
	
	@Test
	void guardarInventarioVacio() throws SAXException {
		Map<Objeto, Integer> vacio = new HashMap<>();
		new InventarioXML(INVENTARIO_VACIO_SALIDA_XML).guardar(vacio);
		Map<Objeto, Integer> objetos = new InventarioXML(INVENTARIO_VACIO_SALIDA_XML).cargar();
		assertTrue(objetos.isEmpty());
	}
	
	@Test
	void guardarYLeerInventario() throws SAXException {
		Map<Objeto, Integer> guardadoMixto = new HashMap<>();
		guardadoMixto.put(new ObjetoIntermedio("Palo"), 3);
		guardadoMixto.put(new ObjetoBasico("Piedra"), 7);
		new InventarioXML(INVENTARIO_MIXTO_SALIDA_XML).guardar(guardadoMixto);
		Map<Objeto, Integer> leidoMixto = new InventarioXML(INVENTARIO_MIXTO_SALIDA_XML).cargar();
		assertTrue(leidoMixto.size()==2);
		assertTrue(leidoMixto.get(new ObjetoIntermedio("Palo")) == 3);
		assertTrue(leidoMixto.get(new ObjetoBasico("Piedra")) == 7);
		Set<Objeto> keys = leidoMixto.keySet();
		Iterator<Objeto> it = keys.iterator();
		assertTrue(it.next() instanceof ObjetoIntermedio);
		assertTrue(it.next() instanceof ObjetoBasico);
	}
	
	@Test
	void cargarInventarioConTipoDesconocido() {
		assertThrows(SAXException.class, () -> {
			new InventarioXML(INVENTARIO_DESCONOCIDO_XML).cargar();
        });
	}
	
	@Test
	void cargarInventarioConTipoDesconocidoEsVacio() {
		Map<Objeto, Integer> objetos;
		try {
			objetos = new InventarioXML(INVENTARIO_DESCONOCIDO_XML).cargar();
			assertTrue(objetos.isEmpty());
		} catch (SAXException e) {
			//System.err.println("Error de parseo SAX al cargar en test ");
		}
	}
	
	@Test
	void cargarInventarioConArchivoInvalidoEsVacio() {
		Map<Objeto, Integer> objetos;
		try {
			objetos = new InventarioXML("null").cargar();
			assertTrue(objetos.isEmpty());
		} catch (Exception e) {
			//System.err.println("Error de parseo SAX al cargar en test ");
		}
	}
}
