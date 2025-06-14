package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class ObjetoTest {
    
    private Objeto objetoBasico;
    private Objeto objetoIntermedio;
    
    @BeforeEach
    void setUp() {
        objetoBasico = new Objeto("Madera") {
            @Override
            public boolean esBasico() {
                return true;
            }
        };
        
        objetoIntermedio = new Objeto("Mesa") {
            @Override
            public boolean esBasico() {
                return false;
            }
        };
    }
    
    @Test
    void testGetNombre() {
        assertEquals("Madera", objetoBasico.getNombre());
        assertEquals("Mesa", objetoIntermedio.getNombre());
    }
    
    @Test
    void testEquals() {
        Objeto otraMadera = new ObjetoBasico("Madera");
        Objeto otroHierro = new ObjetoBasico("Hierro");
        
        // Igualdad por nombre
        assertTrue(objetoBasico.equals(otraMadera));
        assertFalse(objetoBasico.equals(otroHierro));
        
        // No igual con null
        assertFalse(objetoBasico.equals(null));
        
        // No igual con clase diferente
        assertFalse(objetoBasico.equals("Madera"));
    }
    
    @Test
    void testHashCode() {
        Objeto otraMadera = new ObjetoBasico("Madera");
        assertEquals(objetoBasico.hashCode(), otraMadera.hashCode());
    }
}