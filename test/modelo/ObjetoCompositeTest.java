package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ObjetoCompositeTest {

    @Test
    void condicionDeCorte() {
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        ObjetoIntermedio mesa = new ObjetoIntermedio("Mesa");
        
        assertTrue(madera.esBasico());
        assertTrue(hierro.esBasico());
        assertFalse(mesa.esBasico());
        
        List<Objeto> objetos = List.of(madera, hierro, mesa);
        assertEquals(3, objetos.size());
    }
    
    
}