package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistorialDeCrafteoTest {

    private HistorialDeCrafteo historial;
    private ObjetoBasico madera;
    private ObjetoIntermedio mesa;
    private ObjetoIntermedio silla;

    @BeforeEach
    void setUp() {
        RegistroCrafteo.reiniciarContador();
        historial = new HistorialDeCrafteo();
        madera = new ObjetoBasico("Madera");
        mesa = new ObjetoIntermedio("Mesa");
        silla = new ObjetoIntermedio("Silla");
    }

    @Test
    void testAgregarRegistro() {
        historial.agregarRegistro(mesa, 1, 15);
        assertEquals(1, historial.getRegistros().size());
        
        RegistroCrafteo registro = historial.getRegistros().get(0);
        assertEquals(mesa, registro.getObjetoCrafteado());
        assertEquals(1, registro.getCantidadCrafteada());
        assertEquals(15, registro.getTiempoTotal());
    }

    @Test
    void testLimpiarRegistros() {
        historial.agregarRegistro(mesa, 1, 15);
        historial.agregarRegistro(silla, 2, 20);
        
        historial.limpiarRegistros();
        
        assertTrue(historial.getRegistros().isEmpty());
        assertEquals(1, new RegistroCrafteo(mesa, 1, 1).getTurno()); // Verifica reinicio contador
    }

    @Test
    void testGetRegistrosDevuelveCopia() {
        historial.agregarRegistro(mesa, 1, 15);
        List<RegistroCrafteo> copia = historial.getRegistros();
        copia.clear();
        
        assertEquals(1, historial.getRegistros().size());
    }

    @Test
    void testBuscarPorNombre() {
        historial.agregarRegistro(mesa, 1, 15);
        historial.agregarRegistro(silla, 2, 20);
        historial.agregarRegistro(mesa, 1, 10);
        
        List<RegistroCrafteo> resultados = historial.buscarCrafteosPorNombre(mesa);
        assertEquals(2, resultados.size());
        assertEquals(mesa, resultados.get(0).getObjetoCrafteado());
        assertEquals(mesa, resultados.get(1).getObjetoCrafteado());
    }

    @Test
    void testGetPrimerCrafteo() {
        historial.agregarRegistro(mesa, 1, 15);
        historial.agregarRegistro(silla, 2, 20);
        historial.agregarRegistro(mesa, 1, 10);
        
        RegistroCrafteo primerMesa = historial.buscarPrimerCrafteo(mesa);
        assertEquals(1, primerMesa.getTurno());
        assertEquals(15, primerMesa.getTiempoTotal());
    }

    @Test
    void testGetUltimoCrafteo() {
        historial.agregarRegistro(mesa, 1, 15);
        historial.agregarRegistro(silla, 2, 20);
        historial.agregarRegistro(mesa, 1, 10);
        
        RegistroCrafteo ultimaMesa = historial.buscarUltimoCrafteo(mesa);
        assertEquals(3, ultimaMesa.getTurno());
        assertEquals(10, ultimaMesa.getTiempoTotal());
    }

    @Test
    void testGetCantidadTotalCrafteada() {
        historial.agregarRegistro(mesa, 1, 15);
        historial.agregarRegistro(silla, 2, 20);
        historial.agregarRegistro(mesa, 3, 10);
        historial.agregarRegistro(mesa, 2, 5);
        
        int totalMesas = historial.getCantidadTotalCrafteada(mesa);
        assertEquals(6, totalMesas);
        
        int totalSillas = historial.getCantidadTotalCrafteada(silla);
        assertEquals(2, totalSillas);
        
        int totalMadera = historial.getCantidadTotalCrafteada(madera);
        assertEquals(0, totalMadera);
    }

    @Test
    void testAgregarRegistroConParametrosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> {
            historial.agregarRegistro(null, 1, 10);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            historial.agregarRegistro(mesa, 0, 10);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            historial.agregarRegistro(mesa, 1, -1);
        });
    }

    @Test
    void testBusquedasConNombreInexistente() {
        historial.agregarRegistro(mesa, 1, 15);
        
        Objeto objetoInexistente = new ObjetoBasico("inexistente");
        assertTrue(historial.buscarCrafteosPorNombre(objetoInexistente).isEmpty());
        assertNull(historial.buscarPrimerCrafteo(objetoInexistente));
        assertNull(historial.buscarUltimoCrafteo(objetoInexistente));
        assertEquals(0, historial.getCantidadTotalCrafteada(objetoInexistente));
    }
}