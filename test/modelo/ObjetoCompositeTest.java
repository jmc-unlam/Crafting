package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjetoCompositeTest {

    @Test
    void testJerarquiaObjetos() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        ObjetoIntermedio mesa = new ObjetoIntermedio("Mesa");
        
        // Verificar la condición de corte del patrón Composite
        assertTrue(madera.esBasico());
        assertTrue(hierro.esBasico());
        assertFalse(mesa.esBasico());
        
        // Todos son Objetos gracias al polimorfismo
        Objeto[] objetos = {madera, hierro, mesa};
        assertEquals(3, objetos.length);
    }
    
    @Test
    void testUsoEnEstructurasColeccion() {
        ObjetoBasico madera1 = new ObjetoBasico("Madera");
        ObjetoBasico madera2 = new ObjetoBasico("Madera");
        ObjetoIntermedio mesa1 = new ObjetoIntermedio("Mesa");
        ObjetoIntermedio mesa2 = new ObjetoIntermedio("Mesa");
        
        // Igualdad basada en nombre
        assertEquals(madera1, madera2);
        assertEquals(mesa1, mesa2);
        
        // Diferentes instancias
        assertNotSame(madera1, madera2);
        assertNotSame(mesa1, mesa2);
    }
}