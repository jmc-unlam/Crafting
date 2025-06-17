package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjetoIntermedioTest {

    @Test
    void testEsBasico() {
        ObjetoIntermedio mesa = new ObjetoIntermedio("Mesa");
        assertFalse(mesa.esBasico());
    }
    
    @Test
    void testConstructorYGetNombre() {
        ObjetoIntermedio silla = new ObjetoIntermedio("Silla");
        assertEquals("silla", silla.getNombre());
    }
    
    @Test
    void testEqualsConMismoNombreDiferenteTipo() {
        ObjetoIntermedio mesa = new ObjetoIntermedio("Mesa");
        ObjetoBasico mesaBasica = new ObjetoBasico("Mesa");
        
        assertFalse(mesa.equals(mesaBasica));
        assertFalse(mesaBasica.equals(mesa));
    }
}