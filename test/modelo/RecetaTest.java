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
class RecetaTest {

	private ObjetoBasico madera;
	private ObjetoBasico hierro;
	private ObjetoIntermedio clavo;
	private ObjetoIntermedio mesa;

	private Map<Objeto, Integer> ingredientesClavo;
	private Map<Objeto, Integer> ingredientesMesa;

	@Mock
	private Recetario recetario;

	private Receta recetaClavo;
	private Receta recetaMesa;

	@BeforeEach
	void setUp() {
		// Objetos básicos
		madera = new ObjetoBasico("Madera");
		hierro = new ObjetoBasico("Hierro");

		// Objetos intermedios
		clavo = new ObjetoIntermedio("Clavo");
		mesa = new ObjetoIntermedio("Mesa");

		// Ingredientes para clavo
		ingredientesClavo = new HashMap<>();
		ingredientesClavo.put(hierro, 1);

		// Ingredientes para mesa
		ingredientesMesa = new HashMap<>();
		ingredientesMesa.put(madera, 4);
		ingredientesMesa.put(clavo, 10);

		// Recetas
		recetaClavo = new Receta(clavo, ingredientesClavo, 5, 2);
		recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);

	}

	@Test
	void constructorYGetters() {
		assertEquals(mesa, recetaMesa.getObjetoProducido());
		assertEquals(1, recetaMesa.getCantidadProducida());
		assertEquals(10, recetaMesa.getTiempoBase());

		Map<Objeto, Integer> ingredientes = recetaMesa.getIngredientes();
		assertEquals(4, ingredientes.get(madera));
		assertEquals(10, ingredientes.get(clavo));
	}

	@Test
	void getIngredientesEsUnaCopia() {
		Map<Objeto, Integer> ingredientes = recetaMesa.getIngredientes();
		ingredientes.put(new ObjetoBasico("Oro"), 1); // Modificación no debe afectar a la receta

		assertFalse(recetaMesa.getIngredientes().containsKey(new ObjetoBasico("Oro")));
	}

	@Test
	void calcularTiempoTotalParaRecetaSimple() {
		assertEquals(2, recetaClavo.calcularTiempoTotal(recetario));
	}

	@Test
	void calcularTiempoTotalParaRecetaCompuesta() {
		when(recetario.buscarReceta(clavo)).thenReturn(recetaClavo);
		// Mesa: 10 (tiempo base) + 10 clavos * (2 tiempo cada clavo / 5 clavos por
		// receta)
		assertEquals(14, recetaMesa.calcularTiempoTotal(recetario));
	}

	@Test
	void getIngredientesBasicosParaRecetaSimple() {
		// aqui no usa la maqueta recetario porque son ingredientes basicos
		Map<Objeto, Integer> basicos = recetaClavo.getIngredientesBasicos(recetario,recetaClavo.getCantidadProducida());

		assertEquals(1, basicos.size());
		assertEquals(1, basicos.get(hierro));
	}

	@Test
	void getIngredientesBasicosParaRecetaCompuesta() {
		when(recetario.buscarReceta(clavo)).thenReturn(recetaClavo);
		Map<Objeto, Integer> basicos = recetaMesa.getIngredientesBasicos(recetario,recetaMesa.getCantidadProducida());

		assertEquals(2, basicos.size());
		assertEquals(4, basicos.get(madera));
		assertEquals(2, basicos.get(hierro));
	}

	@Test
	void recetaConIngredienteIntermedioSinReceta() {
		when(recetario.buscarReceta(clavo)).thenReturn(null);

		assertThrows(NullPointerException.class, () -> {
			recetaMesa.getIngredientesBasicos(recetario,0);
		});

		assertThrows(NullPointerException.class, () -> {
			recetaMesa.calcularTiempoTotal(recetario);
		});
	}
}