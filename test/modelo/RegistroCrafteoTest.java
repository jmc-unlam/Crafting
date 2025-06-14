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
    void testToString() {
        RegistroCrafteo registro = new RegistroCrafteo(mesa, 2, 25);
        String expected = "Turno 1 - ObjetoIntermedio: Mesa: 2 unidades (Tiempo: 25)";
        assertEquals(expected, registro.toString());
    }

    @Test
    void testMultipleRegistrosToString() {
        RegistroCrafteo registro1 = new RegistroCrafteo(madera, 10, 5);
        RegistroCrafteo registro2 = new RegistroCrafteo(mesa, 1, 30);
        
        String expected1 = "Turno 1 - ObjetoBásico: Madera: 10 unidades (Tiempo: 5)";
        String expected2 = "Turno 2 - ObjetoIntermedio: Mesa: 1 unidades (Tiempo: 30)";
        
        assertEquals(expected1, registro1.toString());
        assertEquals(expected2, registro2.toString());
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