package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;

class ObjetoBasicoTest {

    @Test
    void testEsBasico() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        assertTrue(madera.esBasico());
    }
    
    @Test
    void testConstructorYGetNombre() {
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        assertEquals("Hierro", hierro.getNombre());
    }
    
    @Test
    void testToString() {
        ObjetoBasico piedra = new ObjetoBasico("Piedra");
        assertEquals("ObjetoBásico: Piedra", piedra.toString());
    }
    
    @Test
    void testEqualsEntreObjetoBasicoYIntermedio() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio mesa = new ObjetoIntermedio("Madera");
        
        // Aunque tienen el mismo nombre, son de clases diferentes
        assertFalse(madera.equals(mesa));
    }
}