package modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventarioTest {

    private ObjetoBasico madera;
    private ObjetoBasico hierro;
    private ObjetoIntermedio mesa;
    
    private Inventario inventario;
    
    @Mock
    private Recetario recetario;

    @BeforeEach
    void setUp() {
        madera = new ObjetoBasico("Madera");
        hierro = new ObjetoBasico("Hierro");
        mesa = new ObjetoIntermedio("Mesa");
        
        inventario = new Inventario();
    }

    @Test
    void testAgregarObjetoValido() {
        inventario.agregarObjeto(madera, 5);
        assertEquals(5, inventario.getCantidad(madera));
    }

    @Test
    void testAgregarObjetoConCantidadNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            inventario.agregarObjeto(madera, -1);
        });
    }

    @Test
    void testAgregarObjetoConCantidadCero() {
        assertThrows(IllegalArgumentException.class, () -> {
            inventario.agregarObjeto(madera, 0);
        });
    }

    @Test
    void testRemoverObjetoValido() {
        inventario.agregarObjeto(madera, 5);
        inventario.removerObjeto(madera, 3);
        assertEquals(2, inventario.getCantidad(madera));
    }

    @Test
    void testRemoverObjetoHastaCero() {
        inventario.agregarObjeto(madera, 5);
        inventario.removerObjeto(madera, 5);
        assertEquals(0, inventario.getCantidad(madera));
        assertFalse(inventario.getObjetos().containsKey(madera));
    }

    @Test
    void testRemoverObjetoConCantidadInsuficiente() {
        inventario.agregarObjeto(madera, 3);
        assertThrows(IllegalArgumentException.class, () -> {
            inventario.removerObjeto(madera, 5);
        });
    }

    @Test
    void testGetObjetosDevuelveCopia() {
        inventario.agregarObjeto(madera, 2);
        Map<Objeto, Integer> copia = inventario.getObjetos();
        copia.put(hierro, 1); // Modificación no debe afectar al inventario
        
        assertEquals(1, inventario.getObjetos().size());
        assertFalse(inventario.getObjetos().containsKey(hierro));
    }

    @Test
    void testGetFaltantes() {
        inventario.agregarObjeto(madera, 3);
        inventario.agregarObjeto(hierro, 1);
        
        Map<Objeto, Integer> requeridos = new HashMap<>();
        requeridos.put(madera, 5);
        requeridos.put(hierro, 1);
        requeridos.put(mesa, 2);
        
        Map<Objeto, Integer> faltantes = inventario.getFaltantes(requeridos);
        
        assertEquals(2, faltantes.size());
        assertEquals(2, faltantes.get(madera));
        assertEquals(2, faltantes.get(mesa));
        assertFalse(faltantes.containsKey(hierro));
    }

    @Test
    void testGetCantidadBasicoParaObjetoBasico() {
        inventario.agregarObjeto(madera, 5);
        assertEquals(5, inventario.getCantidadBasico(madera, recetario));
    }

    @Test
    void testGetCantidadBasicoParaObjetoIntermedio() {
        inventario.agregarObjeto(mesa, 2);
        
        // Configurar receta mock para la mesa
        Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
        ingredientesMesa.put(madera, 3);
        Receta recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);
        
        when(recetario.buscarReceta(mesa)).thenReturn(recetaMesa);
        
        // Agregar madera para craftear más mesas
        inventario.agregarObjeto(madera, 7);
        
        // 2 mesas directas + 7 madera / 3 madera por mesa = 2 mesas más
        assertEquals(4, inventario.getCantidadBasico(mesa, recetario));
    }

    @Test
    void testGetFaltantesBasicos() {
        inventario.agregarObjeto(madera, 5);
        
        // Configurar receta mock para la mesa
        Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
        ingredientesMesa.put(madera, 3);
        ingredientesMesa.put(hierro, 2);
        Receta recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);
        
        //when(recetario.buscarReceta(mesa)).thenReturn(recetaMesa);
        
        Map<Objeto, Integer> requeridos = new HashMap<>();
        requeridos.put(hierro, 2);
        
        Map<Objeto, Integer> faltantes = inventario.getFaltantesBasicos(requeridos, recetario);
        
        assertEquals(1, faltantes.size());
        assertEquals(2, faltantes.get(hierro)); // Necesita 2 hierro (2 por mesa), tiene 0
    }
    
    @Test
    void noSePuedenApilarMesasDelMismoTipo() {
    	// Crear objetos
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

        // Crear receta
        Map<Objeto, Integer> ingredientes = new HashMap<>();
        ingredientes.put(madera, 2);
        Receta receta = new Receta(palo, ingredientes, 4, 10);

        // Crear mesa
        MesaDeHierro mesa1 = new MesaDeHierro(Arrays.asList(receta));
        MesaDeHierro mesa2 = new MesaDeHierro(Arrays.asList(receta));

        Recetario recetario = new Recetario();
        inventario.agregarObjeto(mesa1, 1,recetario);
        
        assertThrows(IllegalArgumentException.class, () -> {
            inventario.agregarObjeto(mesa2, 1,recetario); // Debe lanzar excepción
        });
    }
    
    @Test
    void sePermitenMesasDeDistintosTipos() {
    	// Crear objetos
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio palo = new ObjetoIntermedio("Palo");

        // Crear receta
        Map<Objeto, Integer> ingredientes = new HashMap<>();
        ingredientes.put(madera, 2);
        Receta receta = new Receta(palo, ingredientes, 4, 10);

        MesaDeHierro mesa = new MesaDeHierro(Arrays.asList(receta));
        MesaDePiedra otraMesa = new MesaDePiedra(Arrays.asList(receta));

        Inventario inventario = new Inventario();
        Recetario recetario = new Recetario();
        inventario.agregarObjeto(mesa, 1,recetario);
        inventario.agregarObjeto(otraMesa, 1,recetario); // Esto debe funcionar

        assertEquals(2, inventario.getObjetos().size());
    }
}