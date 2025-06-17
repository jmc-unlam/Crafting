package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaDeCrafteoTest {

    private ObjetoBasico madera;
    private ObjetoBasico piedra;
    private ObjetoBasico hierro;
    private ObjetoIntermedio oxido;
    private ObjetoIntermedio hachaDePiedra;

    private Inventario inventario;
    private Recetario recetario;
    private SistemaDeCrafteo sistema;

    @BeforeEach
    void setUp() {
        // Paso 1: Crear objetos
        madera = new ObjetoBasico("Madera");
        piedra = new ObjetoBasico("Piedra");
        hierro = new ObjetoBasico("Hierro");
        oxido = new ObjetoIntermedio("Oxido");
        hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");

        // Paso 2: Crear ingredientes
        Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
        ingredientesHachaDePiedra.put(piedra, 3);
        ingredientesHachaDePiedra.put(madera, 2);
        ingredientesHachaDePiedra.put(oxido, 2);

        Map<Objeto, Integer> ingredientesOxido = new HashMap<>();
        ingredientesOxido.put(hierro, 7);

        // Paso 3: Crear recetas
        Receta recetaHachaDePiedra = new Receta(hachaDePiedra, ingredientesHachaDePiedra, 1, 5);
        Receta recetaOxido = new Receta(oxido, ingredientesOxido, 9, 2);

        // Paso 4: Inicializar inventario
        inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(piedra, 5);
        inventario.agregarObjeto(hierro, 4); // Solo 4 hierros inicialmente

        // Paso 5: Inicializar recetario y sistema
        recetario = new Recetario();
        recetario.agregarReceta(recetaHachaDePiedra);
        recetario.agregarReceta(recetaOxido);

        sistema = new SistemaDeCrafteo(inventario, recetario);
    }

    @Test
    void testIngredientesNecesarios() {
        Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        assertEquals(3, ingredientes.size());
        assertEquals(Integer.valueOf(3), ingredientes.get(piedra));
        assertEquals(Integer.valueOf(2), ingredientes.get(madera));
        assertEquals(Integer.valueOf(2), ingredientes.get(oxido));
    }

    @Test
    void testIngredientesBasicosNecesarios() {
        Map<Objeto, Integer> basicos = sistema.ingredientesBasicosNecesarios(hachaDePiedra);
        assertEquals(3, basicos.size());
        
        // Verificar cantidades de ingredientes básicos (piedra, madera, hierro)
        // Para 1 Hacha: 3 Piedra, 2 Madera, 9 Oxido
        // Para 9 Oxido (que son los que necesita el Hacha): 7 Hierro
        // Total: 3 Piedra, 2 Madera, 7 Hierro
        assertEquals(3, basicos.get(new ObjetoBasico("Piedra")));
        assertEquals(2, basicos.get(new ObjetoBasico("Madera")));
        assertEquals(7, basicos.get(new ObjetoBasico("Hierro")));
    }

    @Test
    void testIngredientesFaltantesParaCraftear() {
        Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(hachaDePiedra);
        assertTrue(faltantes.containsKey(oxido));
        assertEquals(2, faltantes.get(oxido));
        assertFalse(faltantes.containsKey(piedra));
    }

    @Test
    void testIngredientesBasicosFaltantesParaCraftear() {
        Map<Objeto, Integer> faltantes = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
        assertTrue(faltantes.containsKey(hierro));
        assertEquals(3, faltantes.get(hierro)); // Necesita 7, tiene 4
    }

    @Test
    void testCantidadCrafteable() {
        assertEquals(0, sistema.cantidadCrafteable(hachaDePiedra));
        assertEquals(0, sistema.cantidadCrafteable(oxido));

        // Agregar más hierro
        inventario.agregarObjeto(hierro, 10);
        assertEquals(1, sistema.cantidadCrafteable(hachaDePiedra));
        assertEquals(2, sistema.cantidadCrafteable(oxido)); 
    }

    @Test
    void testCraftearObjetoYVerificarInventarioYHistorial() {
        // Asegurar que tenemos hierro suficiente
        inventario.agregarObjeto(hierro, 10);

        int tiempoTotal = sistema.craftearObjeto(hachaDePiedra, 1);

        // Verificar inventario
        assertEquals(Integer.valueOf(8), inventario.getCantidad(madera));
        assertEquals(Integer.valueOf(2), inventario.getCantidad(piedra));
        assertTrue(inventario.getCantidad(hierro) < 14); // Se usó hierro para hacer óxido
        assertEquals(Integer.valueOf(1), inventario.getCantidad(hachaDePiedra));

        // Verificar historial
        List<RegistroCrafteo> historial = sistema.getHistorial();
        assertFalse(historial.isEmpty());
        RegistroCrafteo ultimo = historial.get(historial.size() - 1);
        assertEquals(hachaDePiedra, ultimo.getObjetoCrafteado());
        assertEquals(1, ultimo.getCantidadCrafteada());
        assertTrue(tiempoTotal > 0);
    }
    
    @Test
    void testCraftearConRecetaAlternativa() {
    	ObjetoBasico carbonVegetal = new ObjetoBasico("Carbon Vegetal");
        inventario.agregarObjeto(carbonVegetal, 10);
        Map<Objeto, Integer> ingredientesAntorcha1 = new HashMap<>();
        ingredientesAntorcha1.put(carbonVegetal,5);
        
        ObjetoBasico carbonMineral = new ObjetoBasico("Carbon Mineral");
        inventario.agregarObjeto(carbonMineral, 4);
        Map<Objeto, Integer> ingredientesAntorcha2 = new HashMap<>();
        ingredientesAntorcha2.put(carbonMineral,2);
        
        ObjetoIntermedio antorcha = new ObjetoIntermedio("Antorcha");
        Receta recetaAntorcha1 = new Receta(antorcha, ingredientesAntorcha1, 1, 20);
        Receta recetaAntorcha2 = new Receta(antorcha, ingredientesAntorcha2, 1, 5);
        
        recetario.agregarReceta(recetaAntorcha1);
        recetario.agregarReceta(recetaAntorcha2);

        SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

        // Elegimos la segunda receta (índice 1)
        sistema.craftearObjetoConReceta(antorcha,1,1);

        // Verificar que se usó carbón vegetal y palo
        assertEquals(2, inventario.getCantidad(carbonMineral));
        
        // Elegimos la primera receta (índice 0)
        sistema.craftearObjetoConReceta(antorcha,1,0);
        
        // Verificar que se usó carbón vegetal y palo
        assertEquals(5, inventario.getCantidad(carbonVegetal));

        // Verificar que se produjo antorcha
        assertTrue(inventario.getCantidad(antorcha) == 2);
    }
    
    @Test
	void testIngredientesDirectosHachaDePiedra() {
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        
        assertNotNull(ingredientes, "El mapa de ingredientes no debería ser nulo");
        assertEquals(3, ingredientes.size(), "Debería haber 3 tipos de ingredientes directos para Hacha de Piedra");
        
        // Verificar cantidades de ingredientes directos
        assertEquals(3, ingredientes.get(new ObjetoBasico("Piedra")), "Cantidad de Piedra incorrecta");
        assertEquals(2, ingredientes.get(new ObjetoBasico("Madera")), "Cantidad de Madera incorrecta");
        assertEquals(2, ingredientes.get(new ObjetoIntermedio("Oxido")), "Cantidad de Oxido incorrecta");
	}

	@Test
	void testIngredientesDirectosOxido() {
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(oxido);
        
        assertNotNull(ingredientes, "El mapa de ingredientes no debería ser nulo");
        assertEquals(1, ingredientes.size(), "Debería haber 1 tipo de ingrediente directo para Oxido");
        
        // Verificar cantidad de ingrediente directo
        assertEquals(7, ingredientes.get(new ObjetoBasico("Hierro")), "Cantidad de Hierro incorrecta para Oxido");
	}

    @Test
    void testIngredientesBasicosHachaDePiedra() {
        Map<Objeto, Integer> ingredientesBasicos = sistema.ingredientesBasicosNecesarios(hachaDePiedra);

        assertNotNull(ingredientesBasicos, "El mapa de ingredientes básicos no debería ser nulo");
        assertEquals(3, ingredientesBasicos.size(), "Debería haber 3 tipos de ingredientes básicos para Hacha de Piedra");

        // Verificar cantidades de ingredientes básicos (piedra, madera, hierro)
        // Para 1 Hacha: 3 Piedra, 2 Madera, 9 Oxido
        // Para 9 Oxido (que son los que necesita el Hacha): 7 Hierro
        // Total: 3 Piedra, 2 Madera, 7 Hierro
        assertEquals(3, ingredientesBasicos.get(new ObjetoBasico("Piedra")));
        assertEquals(2, ingredientesBasicos.get(new ObjetoBasico("Madera")));
        assertEquals(7, ingredientesBasicos.get(new ObjetoBasico("Hierro")));
    }

    @Test
    void testIngredientesObjetoSinRecetaLanzaExcepcion() {
        ObjetoIntermedio objetoSinReceta = new ObjetoIntermedio("Objeto Desconocido");
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesNecesarios(objetoSinReceta);
        }, "Debería lanzar IllegalArgumentException para un objeto sin receta");

        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesBasicosNecesarios(objetoSinReceta);
        }, "Debería lanzar IllegalArgumentException para un objeto sin receta al buscar básicos");
    }

    @Test
    void testIngredientesBasicosObjetoBasico() {
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesBasicosNecesarios(madera);
        }, "Debería lanzar IllegalArgumentException si se busca ingredientes básicos de un objeto básico sin receta.");
    }

}