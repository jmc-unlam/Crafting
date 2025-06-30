package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaDeCrafteoTest {

    private Objeto madera;
    private Objeto piedra;
    private Objeto hierro;
    private Objeto oxido;
    private Objeto hachaDePiedra;
    private Objeto espadaOxidada;

    private Inventario inventario;
    private Recetario recetario;
    private SistemaDeCrafteo sistema;

    @BeforeEach
    void setUp() {
        // Crear objetos
        madera = new ObjetoBasico("Madera");
        piedra = new ObjetoBasico("Piedra");
        hierro = new ObjetoBasico("Hierro");
        oxido = new ObjetoIntermedio("Oxido");
        hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");
        espadaOxidada = new ObjetoIntermedio("Espada Oxidada");

        // Crear ingredientes
        Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
        ingredientesHachaDePiedra.put(piedra, 3);
        ingredientesHachaDePiedra.put(madera, 2);
        ingredientesHachaDePiedra.put(oxido, 2);

        Map<Objeto, Integer> ingredientesOxido = new HashMap<>();
        ingredientesOxido.put(hierro, 7);
        
        Map<Objeto, Integer> ingredientesEspadaOxidada = new HashMap<>();
        ingredientesEspadaOxidada.put(oxido,2);

        // Crear recetas
        //la receta de hacha de piedra produce 2 hachas por receta
        Receta recetaHachaDePiedra = new Receta(hachaDePiedra, ingredientesHachaDePiedra, 2, 5);
        //la receta de oxido produce 9 oxidos por receta usa 7 hierros para eso
        Receta recetaOxido = new Receta(oxido, ingredientesOxido, 9, 2);
        //la receta de espada de oxido da 10 espadas en 5 necesita 2 oxidos.
        Receta recetaEspadaOxidada = new Receta(espadaOxidada,ingredientesEspadaOxidada,10,5);

        // Inicializar inventario
        inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(piedra, 5);
        inventario.agregarObjeto(hierro, 4);

        // Inicializar recetario y sistema
        recetario = new Recetario();
        recetario.agregarReceta(recetaHachaDePiedra);
        recetario.agregarReceta(recetaOxido);
        recetario.agregarReceta(recetaEspadaOxidada);

        sistema = new SistemaDeCrafteo(inventario, recetario);
    }

    @Test
    void ingredientesNecesarios() {
        Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        assertEquals(3, ingredientes.size());
        assertEquals(3, ingredientes.get(piedra));
        assertEquals(2, ingredientes.get(madera));
        assertEquals(2, ingredientes.get(oxido));
    }

    @Test
    void ingredientesBasicosNecesarios() {
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
    void ingredientesFaltantesParaCraftear() {
        Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(hachaDePiedra);
        assertTrue(faltantes.containsKey(oxido));
        assertEquals(2, faltantes.get(oxido));
        assertFalse(faltantes.containsKey(piedra));
    }

    @Test
    void ingredientesBasicosFaltantesParaCraftear() {
        Map<Objeto, Integer> faltantes = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
        assertTrue(faltantes.containsKey(hierro));
        assertEquals(3, faltantes.get(hierro)); // Necesita 7, tiene 4
    }

    @Test
    void cantidadCrafteablePorLote() {
    	//falta oxido para craftear hacha de piedra
        assertEquals(0, sistema.cantidadCrafteable(hachaDePiedra));
        assertEquals(0, sistema.cantidadCrafteable(oxido));

        // Agregar más hierro el cual puede crear oxido
        inventario.agregarObjeto(hierro, 10);
        assertEquals(2, sistema.cantidadCrafteable(hachaDePiedra));
        assertEquals(18, sistema.cantidadCrafteable(oxido)); 
    }
    
    @Test
    void craftearOxidoTieneIngredienteBasico() {
    	//tengo 7 hierros
    	inventario.agregarObjeto(hierro, 3);
    	//quiero nueve oxidos y la receta produce 9 y tarda 2.
    	int tiempoTotal = sistema.craftearObjeto(oxido, 9);
    	
    	//se consume todos los hierros
		assertEquals(0,inventario.getCantidad(hierro)); 
		assertFalse(inventario.getObjetos().containsKey(hierro));
		assertEquals(9,inventario.getCantidad(oxido)); 
		assertEquals(2,tiempoTotal); 
		
		//crafteo 10 oxidos necesito 14 hierros recibo 2 lotes porque el lote es de 9
		inventario.removerObjeto(oxido,9);
		inventario.agregarObjeto(hierro, 14);
		assertEquals(4,sistema.craftearObjeto(oxido, 10));
		assertEquals(0,inventario.getCantidad(hierro)); 
		assertFalse(inventario.getObjetos().containsKey(hierro));
		assertEquals(18,inventario.getCantidad(oxido)); 
		
    }
    
    @Test
    void craftearEspadaOxidoTieneIngredienteIntermedio() {
    	//tengo 4 hierros necesito tener 8 
    	inventario.agregarObjeto(hierro, 4);
    	//quiero 1 espadas y la receta produce 10 y tarda 5.
    	int tiempoTotal = sistema.craftearObjeto(espadaOxidada, 1);
    	
    	//sobra 1 hierro y 7 oxidos 
		assertEquals(1,inventario.getCantidad(hierro)); 
		assertEquals(7,inventario.getCantidad(oxido)); 
		assertTrue(inventario.getObjetos().containsKey(hierro));
		//como la receta produce 10 por lote al querer 1 recibo 10 minimo. 
		assertTrue(inventario.getCantidad(espadaOxidada)==10);
		//el oxido tardo 2 y la espada 5
		assertEquals(7,tiempoTotal); 
		
		//crafteo 20 espadas necesito 7 hierros recibo 2 lote
		inventario.removerObjeto(oxido,7);
		inventario.removerObjeto(espadaOxidada,10);
		inventario.agregarObjeto(hierro, 6);
		//son 1 veces oxido + 2 veces espada es 2+10
		assertEquals(12,sistema.craftearObjeto(espadaOxidada, 20));
		assertEquals(0,inventario.getCantidad(hierro)); 
		assertTrue(inventario.getCantidad(espadaOxidada)==20);
    }
    
    @Test
    void craftearHachaDePiedraTieneIngredienteCompuesto() {
    	//en el inventario hay 10 de madera, 5 de piedra y 4 de hierro
        // aumento la cantidad de hierro = 7 
    	// aumento la cantidad de piedra = 6
        inventario.agregarObjeto(hierro, 3); //produciria 9 oxidos
        inventario.agregarObjeto(piedra, 1);
        
        //crafteo 4 hachas de piedra y la receta produce de a 2 asi
        //que hara 2 veces la receta
        int tiempoTotal = sistema.craftearObjeto(hachaDePiedra, 4);

        //Se consumio 2*3 piedra habia 6 y 2*2 madera habia 10
        assertEquals(0, inventario.getCantidad(piedra));
        assertEquals(6, inventario.getCantidad(madera));
        //Se usó todo el hierro para hacer óxido
        assertEquals(0,inventario.getCantidad(hierro)); 
        //Se crearon 9 oxidos y se usaron 4 para el hacha
        assertEquals(5,inventario.getCantidad(oxido)); 
        //Aparece en el inventario 2*2 hachas exactas 
        assertTrue(inventario.getCantidad(hachaDePiedra)==4);
        //El tiempo de crafteo fue 2*5 del hacha + 2 de oxido
        assertEquals(12,tiempoTotal); 

        //Verificando historial hubieron 2 crafteos
        //el primero para el oxido y el ultimo para la piedra 2 veces 
        List<RegistroCrafteo> historial = sistema.getHistorial();
        assertFalse(historial.isEmpty());
        assertEquals(2,historial.size());
        
        //se produjeron 9 oxidos en 2 de tiempo
        RegistroCrafteo primero = historial.get(0);
        assertEquals(oxido, primero.getObjetoCrafteado());
        assertEquals(9, primero.getCantidadCrafteada());
        assertEquals(2,primero.getTiempoTotal()); 
                
        //se produjo 4 hacha en 12 de tiempo
        RegistroCrafteo ultimo = historial.get(historial.size() - 1);
        assertEquals(hachaDePiedra, ultimo.getObjetoCrafteado());
        assertEquals(4, ultimo.getCantidadCrafteada());
        assertEquals(12,ultimo.getTiempoTotal()); 
    }
    
    @Test
    void craftearConRecetaAlternativa() {
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
	void ingredientesDirectosHachaDePiedra() {
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        
        assertNotNull(ingredientes, "El mapa de ingredientes no debería ser nulo");
        assertEquals(3, ingredientes.size(), "Debería haber 3 tipos de ingredientes directos para Hacha de Piedra");
        
        // Verificar cantidades de ingredientes directos
        assertEquals(3, ingredientes.get(new ObjetoBasico("Piedra")), "Cantidad de Piedra incorrecta");
        assertEquals(2, ingredientes.get(new ObjetoBasico("Madera")), "Cantidad de Madera incorrecta");
        assertEquals(2, ingredientes.get(new ObjetoIntermedio("Oxido")), "Cantidad de Oxido incorrecta");
	}

	@Test
	void ingredientesDirectosOxido() {
		Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(oxido);
        
        assertNotNull(ingredientes, "El mapa de ingredientes no debería ser nulo");
        assertEquals(1, ingredientes.size(), "Debería haber 1 tipo de ingrediente directo para Oxido");
        
        // Verificar cantidad de ingrediente directo
        assertEquals(7, ingredientes.get(new ObjetoBasico("Hierro")), "Cantidad de Hierro incorrecta para Oxido");
	}

    @Test
    void ingredientesBasicosHachaDePiedra() {
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
    void ingredientesObjetoSinRecetaLanzaExcepcion() {
        ObjetoIntermedio objetoSinReceta = new ObjetoIntermedio("Objeto Desconocido");
        //IllegalArgumentException para un objeto sin receta
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesNecesarios(objetoSinReceta);
        });

        //IllegalArgumentException para un objeto sin receta al buscar básico
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesBasicosNecesarios(objetoSinReceta);
        });
    }

    @Test
    void ingredientesBasicosObjetoBasico() {
    	//IllegalArgumentException si se busca ingredientes básicos de un objeto básico sin receta.
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.ingredientesBasicosNecesarios(madera);
        });
    }

}