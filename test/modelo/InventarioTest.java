package modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventarioTest {

	private ObjetoBasico madera;
	private ObjetoBasico hierro;
	private ObjetoIntermedio mesa;

	private Inventario inventario;

	@Mock
	private Recetario recetario;

	@BeforeEach
	void setUp() {
		madera = new ObjetoBasico("Madera");
		hierro = new ObjetoBasico("Hierro");
		mesa = new ObjetoIntermedio("Mesa");

		inventario = new Inventario();
	}

	@Test
	void agregarObjetoValido() {
		inventario.agregarObjeto(madera, 5);
		assertEquals(5, inventario.getCantidad(madera));
	}

	@Test
	void agregarObjetoConCantidadNegativa() {
		assertThrows(IllegalArgumentException.class, () -> {
			inventario.agregarObjeto(madera, -1);
		});
	}

	@Test
	void agregarObjetoConCantidadCero() {
		assertThrows(IllegalArgumentException.class, () -> {
			inventario.agregarObjeto(madera, 0);
		});
	}

	@Test
	void removerObjetoValido() {
		inventario.agregarObjeto(madera, 5);
		inventario.removerObjeto(madera, 3);
		assertEquals(2, inventario.getCantidad(madera));
	}

	@Test
	void removerObjetoHastaCero() {
		inventario.agregarObjeto(madera, 5);
		inventario.removerObjeto(madera, 5);
		assertEquals(0, inventario.getCantidad(madera));
		assertFalse(inventario.getObjetos().containsKey(madera));
	}

	@Test
	void removerObjetoConCantidadInsuficiente() {
		inventario.agregarObjeto(madera, 3);
		assertThrows(IllegalArgumentException.class, () -> {
			inventario.removerObjeto(madera, 5);
		});
	}

	@Test
	void getObjetosDevuelveCopia() {
		inventario.agregarObjeto(madera, 2);
		Map<Objeto, Integer> copia = inventario.getObjetos();
		copia.put(hierro, 1); // Modificación no debe afectar al inventario

		assertEquals(1, inventario.getObjetos().size());
		assertFalse(inventario.getObjetos().containsKey(hierro));
	}

	@Test
	void getFaltantes() {
		inventario.agregarObjeto(madera, 3);
		inventario.agregarObjeto(hierro, 1);

		Map<Objeto, Integer> requeridos = new HashMap<>();
		requeridos.put(madera, 5);
		requeridos.put(hierro, 1);
		requeridos.put(mesa, 2);

		Map<Objeto, Integer> faltantes = inventario.getFaltantes(requeridos);

		assertEquals(2, faltantes.size());
		assertEquals(2, faltantes.get(madera));
		assertEquals(2, faltantes.get(mesa));
		assertFalse(faltantes.containsKey(hierro));
	}

	@Test
	void getCantidadBasicoParaObjetoBasico() {
		inventario.agregarObjeto(madera, 5);
		assertEquals(5, inventario.getCantidadBasico(madera, recetario));
	}

	@Test
	void getCantidadBasicoParaObjetoIntermedio() {
		inventario.agregarObjeto(mesa, 2);

		// Configurar receta mock para la mesa
		Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
		ingredientesMesa.put(madera, 3);
		Receta recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);

		when(recetario.buscarReceta(mesa)).thenReturn(recetaMesa);

		// Agregar madera para craftear más mesas
		inventario.agregarObjeto(madera, 7);

		// 2 mesas directas + 7 madera / 3 madera por mesa = 2 mesas más
		assertEquals(4, inventario.getCantidadBasico(mesa, recetario));
	}

	@Test
	void getFaltantesBasicos() {
		inventario.agregarObjeto(madera, 5);

		// Configurar receta mock para la mesa
		Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
		ingredientesMesa.put(madera, 3);
		ingredientesMesa.put(hierro, 2);

		Map<Objeto, Integer> requeridos = new HashMap<>();
		requeridos.put(hierro, 2);

		Map<Objeto, Integer> faltantes = inventario.getFaltantesBasicos(requeridos, recetario);

		assertEquals(1, faltantes.size());
		assertEquals(2, faltantes.get(hierro)); // Necesita 2 hierro (2 por mesa), tiene 0
	}

	@Test
	void noSePuedenApilarMesasDelMismoTipo() {

		// Crear mesa
		MesaDeFundicion mesa1 = new MesaDeFundicion();
		MesaDeFundicion mesa2 = new MesaDeFundicion();

		inventario.agregarObjeto(mesa1, 1);

		assertThrows(IllegalArgumentException.class, () -> {
			inventario.agregarObjeto(mesa2, 1);
		});
	}

	@Test
	void sePermitenMesasDeDistintosTipos() {

		MesaDeFundicion mesa = new MesaDeFundicion();
		MesaDePiedra otraMesa = new MesaDePiedra();

		Inventario inventario = new Inventario();
		inventario.agregarObjeto(mesa, 1);
		inventario.agregarObjeto(otraMesa, 1);

		assertEquals(2, inventario.getObjetos().size());
	}
}