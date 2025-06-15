package modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecetarioTest {

    private ObjetoBasico madera;
    private ObjetoBasico hierro;
    private ObjetoIntermedio clavo;
    private ObjetoIntermedio mesa;
    
    private Receta recetaClavo;
    private Receta recetaMesa;
    private Receta recetaClavoAlternativa;
    
    private Recetario recetario;

    @BeforeEach
    void setUp() {
        // Crear objetos básicos e intermedios
        madera = new ObjetoBasico("Madera");
        hierro = new ObjetoBasico("Hierro");
        clavo = new ObjetoIntermedio("Clavo");
        mesa = new ObjetoIntermedio("Mesa");
        
        // Crear recetas
        Map<Objeto, Integer> ingredientesClavo = new HashMap<>();
        ingredientesClavo.put(hierro, 1);
        recetaClavo = new Receta(clavo, ingredientesClavo, 5, 2);
        
        Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
        ingredientesMesa.put(madera, 4);
        ingredientesMesa.put(clavo, 10);
        recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);
        
        // Receta alternativa para el mismo objeto
        Map<Objeto, Integer> ingredientesClavoAlt = new HashMap<>();
        ingredientesClavoAlt.put(hierro, 2); // Diferente cantidad
        recetaClavoAlternativa = new Receta(clavo, ingredientesClavoAlt, 5, 3);
        
        // Inicializar recetario
        recetario = new Recetario();
    }

    @Test
    void testConstructorVacio() {
        assertTrue(recetario.getRecetas().isEmpty());
    }

    @Test
    void testConstructorConRecetasIniciales() {
        List<Receta> recetasIniciales = new ArrayList<>();
        recetasIniciales.add(recetaClavo);
        
        Recetario recetarioConRecetas = new Recetario(recetasIniciales);
        assertEquals(1, recetarioConRecetas.getRecetas().size());
        assertTrue(recetarioConRecetas.getRecetas().contains(recetaClavo));
    }
    
    @Test
    void testConstructorConRecetasInicialesValidaDuplicados() {
        List<Receta> recetasIniciales = new ArrayList<>();
        recetasIniciales.add(recetaClavo);
        recetasIniciales.add(recetaClavoAlternativa); // Duplicado
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Recetario(recetasIniciales);
        });
    }

    @Test
    void testAgregarReceta() {
        recetario.agregarReceta(recetaClavo);
        assertEquals(1, recetario.getRecetas().size());
        assertTrue(recetario.getRecetas().contains(recetaClavo));
    }
    
    @Test
    void testAgregarRecetaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            recetario.agregarReceta(null);
        });
    }
    
    @Test
    void testRemoverReceta() {
        recetario.agregarReceta(recetaClavo);
        recetario.agregarReceta(recetaMesa);
        
        recetario.removerReceta(recetaClavo);
        
        assertEquals(1, recetario.getRecetas().size());
        assertFalse(recetario.getRecetas().contains(recetaClavo));
        assertTrue(recetario.getRecetas().contains(recetaMesa));
    }
    
    @Test
    void testRemoverRecetaNoExistente() {
        recetario.agregarReceta(recetaClavo);
        
        assertThrows(IllegalArgumentException.class, () -> {
            recetario.removerReceta(recetaMesa);
        });
    }

    @Test
    void testGetRecetasDevuelveCopia() {
        recetario.agregarReceta(recetaClavo);
        List<Receta> copia = recetario.getRecetas();
        copia.clear(); // No debería afectar al recetario original
        
        assertEquals(1, recetario.getRecetas().size());
    }

    @Test
    void testBuscarRecetaExistente() {
        recetario.agregarReceta(recetaClavo);
        recetario.agregarReceta(recetaMesa);
        
        assertEquals(recetaClavo, recetario.buscarReceta(clavo));
        assertEquals(recetaMesa, recetario.buscarReceta(mesa));
    }

    @Test
    void testBuscarRecetaNoExistente() {
        recetario.agregarReceta(recetaClavo);
        assertThrows(NoSuchElementException.class, () -> {
        	recetario.buscarReceta(mesa);
        });
    }
    
    @Test
    void testBuscarRecetaDespuesDeRemover() {
        recetario.agregarReceta(recetaClavo);
        recetario.removerReceta(recetaClavo);
        
        assertThrows(NoSuchElementException.class, () -> {
        	recetario.buscarReceta(clavo);
        });
    }

    @Test
    void testBuscarIngredientesExistente() {
        recetario.agregarReceta(recetaMesa);
        Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);
        
        assertEquals(2, ingredientes.size());
        assertEquals(4, ingredientes.get(madera));
        assertEquals(10, ingredientes.get(clavo));
    }

    @Test
    void testBuscarIngredientesNoExistente() {
        recetario.agregarReceta(recetaClavo);
        Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);
        
        assertTrue(ingredientes.isEmpty());
    }
    
    @Test
    void testBuscarIngredientesActualizados() {
        recetario.agregarReceta(recetaClavo);
        recetario.agregarReceta(recetaMesa);
        
        Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);
        assertEquals(2, ingredientes.size());
        
        // Modificar la receta y verificar que se refleje en la búsqueda
        recetario.removerReceta(recetaMesa);
        Map<Objeto, Integer> nuevosIngredientes = new HashMap<>();
        nuevosIngredientes.put(madera, 2);
        nuevosIngredientes.put(clavo, 5);
        Receta nuevaRecetaMesa = new Receta(mesa, nuevosIngredientes, 1, 8);
        recetario.agregarReceta(nuevaRecetaMesa);
        
        Map<Objeto, Integer> ingredientesActualizados = recetario.buscarIngredientes(mesa);
        assertEquals(2, ingredientesActualizados.get(madera));
        assertEquals(5, ingredientesActualizados.get(clavo));
    }

    @Test
    void testToString() {
        recetario.agregarReceta(recetaClavo);
        recetario.agregarReceta(recetaMesa);
        
        String str = recetario.toString();
        
        assertTrue(str.contains("=== RECETARIO ==="));
        assertTrue(str.contains("Objeto producido: ObjetoIntermedio: Clavo"));
        assertTrue(str.contains("Objeto producido: ObjetoIntermedio: Mesa"));
        assertTrue(str.contains("Ingredientes:"));
    }

    @Test
    void testRecetarioVacioToString() {
        String str = recetario.toString();
        assertTrue(str.contains("=== RECETARIO ==="));
        assertFalse(str.contains("Objeto producido:"));
    }

    @Test
    void testNoPermitirRecetasDuplicadas() {
        recetario.agregarReceta(recetaClavo);
        
        // Intentar agregar receta para el mismo objeto
        assertThrows(IllegalArgumentException.class, () -> {
            recetario.agregarReceta(recetaClavoAlternativa);
        });
        
        assertEquals(1, recetario.getRecetas().size());
    }
}