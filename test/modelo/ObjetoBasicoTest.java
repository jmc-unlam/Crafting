package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;

class ObjetoBasicoTest {

    @Test
    void esBasico() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        assertTrue(madera.esBasico());
    }
    
    @Test
    void constructorYGetNombre() {
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        assertEquals("hierro", hierro.getNombre());
    }
    
    @Test
    void equalsEntreObjetoBasicoYIntermedio() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoIntermedio mesa = new ObjetoIntermedio("Madera");
        
        // Aunque tienen el mismo nombre, son de objetos diferentes
        assertFalse(madera.equals(mesa));
    }
}