package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class ObjetoTest {
    
    private Objeto objetoBasico;
    private Objeto objetoIntermedio;
    
    @BeforeEach
    void setUp() {
        objetoBasico = new ObjetoBasico("- m A  d e r     a//");
        objetoIntermedio = new ObjetoIntermedio("_M_e__s_a");
    }
    
    @Test
    void nombreEstaNormalizado() {
        assertEquals("madera", objetoBasico.getNombre());
        assertEquals("mesa", objetoIntermedio.getNombre());
    }
    
    @Test
    void equalsSonIguales() {
        Objeto otraMadera = new ObjetoBasico("Madera");
        Objeto otraMesa = new ObjetoIntermedio("Mesa");
        
        assertTrue(objetoBasico.equals(otraMadera));
        assertFalse(objetoBasico.equals(otraMesa));
        assertFalse(objetoBasico.equals(null));
        assertFalse(objetoBasico.equals("Madera"));
        
        assertTrue(objetoIntermedio.equals(otraMesa));
        assertFalse(objetoIntermedio.equals(otraMadera));
        assertFalse(objetoIntermedio.equals(null));
        assertFalse(objetoIntermedio.equals("Mesa"));
    }
    
    @Test
    void hashCodeEsigual() {
        Objeto otraMadera = new ObjetoBasico("Madera");
        assertEquals(objetoBasico.hashCode(), otraMadera.hashCode());
    }
    
    @Test
    void noSonLaMismaInstancia() {
        Objeto madera1 = new ObjetoBasico("Madera");
        Objeto madera2 = new ObjetoBasico("Madera");
        Objeto mesa1 = new ObjetoIntermedio("Mesa");
        Objeto mesa2 = new ObjetoIntermedio("Mesa");
        
        assertNotSame(madera1, madera2);
        assertNotSame(mesa1, mesa2);
    }
    
    @Test
    void equalsSonIgualesMasSimbolos() {
        ObjetoBasico madera1 = new ObjetoBasico("M a d e r  a de ñogal");
        ObjetoBasico madera2 = new ObjetoBasico("madera de nogal");
        ObjetoIntermedio mesa1 = new ObjetoIntermedio("Mesa/ d e  ñogal/");
        ObjetoIntermedio mesa2 = new ObjetoIntermedio("mesa de nogal");
        
        assertEquals(madera1, madera2);
        assertEquals(mesa1, mesa2);
        
    }
}