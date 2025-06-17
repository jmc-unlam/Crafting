package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistroCrafteoTest {

    private ObjetoBasico madera;
    private ObjetoIntermedio mesa;

    @BeforeEach
    void setUp() {
        // Reiniciar el contador antes de cada prueba
        RegistroCrafteo.reiniciarContador();
        madera = new ObjetoBasico("Madera");
        mesa = new ObjetoIntermedio("Mesa");
    }

    @AfterEach
    void tearDown() {
        // Limpiar el contador después de cada prueba
        RegistroCrafteo.reiniciarContador();
    }

    @Test
    void testConstructorYGetters() {
        RegistroCrafteo registro = new RegistroCrafteo(mesa, 3, 15);
        
        assertEquals(1, registro.getTurno());
        assertEquals(mesa, registro.getObjetoCrafteado());
        assertEquals(3, registro.getCantidadCrafteada());
        assertEquals(15, registro.getTiempoTotal());
    }

    @Test
    void testContadorTurnosAutoincremental() {
        RegistroCrafteo registro1 = new RegistroCrafteo(madera, 5, 2);
        RegistroCrafteo registro2 = new RegistroCrafteo(mesa, 1, 10);
        
        assertEquals(1, registro1.getTurno());
        assertEquals(2, registro2.getTurno());
    }

    @Test
    void testReiniciarContador() {
        RegistroCrafteo registro1 = new RegistroCrafteo(madera, 5, 2);
        RegistroCrafteo.reiniciarContador();
        RegistroCrafteo registro2 = new RegistroCrafteo(mesa, 1, 10);
        
        assertEquals(1, registro1.getTurno());
        assertEquals(1, registro2.getTurno());
    }

    @Test
    void testRegistroConCantidadCero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroCrafteo(mesa, 0, 10);
        });
    }

    @Test
    void testRegistroConCantidadNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroCrafteo(mesa, -1, 10);
        });
    }

    @Test
    void testRegistroConTiempoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroCrafteo(mesa, 1, -5);
        });
    }

    @Test
    void testRegistroConObjetoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroCrafteo(null, 1, 10);
        });
    }
}