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
        assertEquals("Silla", silla.getNombre());
    }
    
    @Test
    void testToString() {
        ObjetoIntermedio hacha = new ObjetoIntermedio("Hacha");
        assertEquals("ObjetoIntermedio: Hacha", hacha.toString());
    }
    
    @Test
    void testEqualsConMismoNombreDiferenteTipo() {
        ObjetoIntermedio mesa = new ObjetoIntermedio("Mesa");
        ObjetoBasico mesaBasica = new ObjetoBasico("Mesa");
        
        assertFalse(mesa.equals(mesaBasica));
        assertFalse(mesaBasica.equals(mesa));
    }
}