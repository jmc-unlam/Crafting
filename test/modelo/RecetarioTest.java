package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecetarioTest {

	private ObjetoBasico madera;
	private ObjetoBasico hierro;
	private ObjetoIntermedio clavo;
	private ObjetoIntermedio mesa;

	private Receta recetaClavo;
	private Receta recetaMesa;
	private Receta recetaClavoAlternativa;

	private Recetario recetario;

	@BeforeEach
	void setUp() {
		// Crear objetos básicos e intermedios
		madera = new ObjetoBasico("Madera");
		hierro = new ObjetoBasico("Hierro");
		clavo = new ObjetoIntermedio("Clavo");
		mesa = new ObjetoIntermedio("Mesa");

		// Crear recetas
		Map<Objeto, Integer> ingredientesClavo = new HashMap<>();
		ingredientesClavo.put(hierro, 1);
		recetaClavo = new Receta(clavo, ingredientesClavo, 5, 2);

		Map<Objeto, Integer> ingredientesMesa = new HashMap<>();
		ingredientesMesa.put(madera, 4);
		ingredientesMesa.put(clavo, 10);
		recetaMesa = new Receta(mesa, ingredientesMesa, 1, 10);

		// Receta alternativa para el mismo objeto
		Map<Objeto, Integer> ingredientesClavoAlt = new HashMap<>();
		ingredientesClavoAlt.put(hierro, 2); // Diferente cantidad
		recetaClavoAlternativa = new Receta(clavo, ingredientesClavoAlt, 5, 3);

		// Inicializar recetario
		recetario = new Recetario();
	}

	@Test
	void constructorVacio() {
		assertTrue(recetario.getRecetas().isEmpty());
	}

	@Test
	void constructorConRecetasIniciales() {
		List<Receta> recetasIniciales = new ArrayList<>();
		recetasIniciales.add(recetaClavo);
		recetasIniciales.add(recetaClavoAlternativa); // Ahora está permitido

		Recetario recetarioConRecetas = new Recetario(recetasIniciales);
		assertEquals(2, recetarioConRecetas.getRecetas().size());
		assertTrue(recetarioConRecetas.getRecetas().contains(recetaClavo));
		assertTrue(recetarioConRecetas.getRecetas().contains(recetaClavoAlternativa));
	}

	@Test
	void agregarReceta() {
		recetario.agregarReceta(recetaClavo);
		recetario.agregarReceta(recetaClavoAlternativa); // Ahora se permite

		assertEquals(2, recetario.getRecetas().size());
		assertTrue(recetario.getRecetas().contains(recetaClavo));
		assertTrue(recetario.getRecetas().contains(recetaClavoAlternativa));
	}

	@Test
	void agregarRecetaNula() {
		assertThrows(IllegalArgumentException.class, () -> {
			recetario.agregarReceta(null);
		});
	}

	@Test
	void removerReceta() {
		recetario.agregarReceta(recetaClavo);
		recetario.agregarReceta(recetaClavoAlternativa);
		recetario.agregarReceta(recetaMesa);

		recetario.removerReceta(recetaClavoAlternativa);

		assertEquals(2, recetario.getRecetas().size());
		assertTrue(recetario.getRecetas().contains(recetaClavo));
		assertFalse(recetario.getRecetas().contains(recetaClavoAlternativa));
		assertTrue(recetario.getRecetas().contains(recetaMesa));
	}

	@Test
	void removerRecetaNoExistente() {
		recetario.agregarReceta(recetaClavo);

		assertThrows(IllegalArgumentException.class, () -> {
			recetario.removerReceta(recetaMesa);
		});
	}

	@Test
	void getRecetasDevuelveCopia() {
		recetario.agregarReceta(recetaClavo);
		List<Receta> copia = recetario.getRecetas();
		copia.clear(); // No debería afectar al recetario original

		assertEquals(1, recetario.getRecetas().size());
	}

	@Test
	void buscarRecetaExistente() {
		recetario.agregarReceta(recetaClavo);
		recetario.agregarReceta(recetaClavoAlternativa);
		recetario.agregarReceta(recetaMesa);

		// Devuelve la primera receta disponible para "clavo"
		assertNotNull(recetario.buscarReceta(clavo));
		assertEquals(recetaClavo, recetario.buscarReceta(clavo)); // Asume orden de inserción
		assertEquals(recetaMesa, recetario.buscarReceta(mesa));
	}

	@Test
	void buscarRecetaNoExistente() {
		assertThrows(NoSuchElementException.class, () -> {
			recetario.buscarReceta(mesa);
		});
	}

	@Test
	void buscarRecetaDespuesDeRemover() {
		recetario.agregarReceta(recetaClavo);
		recetario.removerReceta(recetaClavo);

		assertThrows(NoSuchElementException.class, () -> {
			recetario.buscarReceta(clavo);
		});
	}

	@Test
	void buscarRecetasAlternativa() {
		recetario.agregarReceta(recetaClavo);
		recetario.agregarReceta(recetaClavoAlternativa);

		List<Receta> recetasClavo = recetario.buscarRecetas(clavo);
		assertEquals(2, recetasClavo.size());
		assertTrue(recetasClavo.contains(recetaClavo));
		assertTrue(recetasClavo.contains(recetaClavoAlternativa));
	}

	@Test
	void buscarRecetasAlternativaQueNoExiste() {
		assertThrows(NoSuchElementException.class, () -> {
			recetario.buscarRecetas(mesa);
		});
	}

	@Test
	void buscarIngredientesExistente() {
		recetario.agregarReceta(recetaMesa);
		Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);

		assertEquals(2, ingredientes.size());
		assertEquals(4, ingredientes.get(madera));
		assertEquals(10, ingredientes.get(clavo));
	}

	@Test
	void buscarIngredientesNoExistente() {
		recetario.agregarReceta(recetaClavo);
		Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);

		assertTrue(ingredientes.isEmpty());
	}

	@Test
	void buscarIngredientesActualizados() {
		recetario.agregarReceta(recetaClavo);
		recetario.agregarReceta(recetaMesa);

		Map<Objeto, Integer> ingredientes = recetario.buscarIngredientes(mesa);
		assertEquals(2, ingredientes.size());

		// Modificar la receta y verificar que se refleje en la búsqueda
		recetario.removerReceta(recetaMesa);
		Map<Objeto, Integer> nuevosIngredientes = new HashMap<>();
		nuevosIngredientes.put(madera, 2);
		nuevosIngredientes.put(clavo, 5);
		Receta nuevaRecetaMesa = new Receta(mesa, nuevosIngredientes, 1, 8);
		recetario.agregarReceta(nuevaRecetaMesa);

		Map<Objeto, Integer> ingredientesActualizados = recetario.buscarIngredientes(mesa);
		assertEquals(2, ingredientesActualizados.get(madera));
		assertEquals(5, ingredientesActualizados.get(clavo));
	}

	@Test
	void recetarioVacioToString() {
		String str = recetario.toString();
		assertTrue(str.contains("=== RECETARIO ==="));
		assertFalse(str.contains("Objeto producido:"));
	}

	@Test
	void agregarDosRecetasParaMismoObjeto() {
		Objeto antorcha = new ObjetoIntermedio("Antorcha");

		Map<Objeto, Integer> ingredientes1 = new HashMap<>();
		ingredientes1.put(new ObjetoBasico("Carbón Mineral"), 1);
		ingredientes1.put(new ObjetoBasico("Palo"), 1);
		Receta receta1 = new Receta((ObjetoIntermedio) antorcha, ingredientes1, 4, 10);

		Map<Objeto, Integer> ingredientes2 = new HashMap<>();
		ingredientes2.put(new ObjetoBasico("Carbón Vegetal"), 1);
		ingredientes2.put(new ObjetoBasico("Palo"), 1);
		Receta receta2 = new Receta((ObjetoIntermedio) antorcha, ingredientes2, 4, 12);

		recetario.agregarReceta(receta1);
		recetario.agregarReceta(receta2);

		// Verificar que ambas recetas están registradas
		assertEquals(2, recetario.buscarRecetas(antorcha).size());
	}
	
	@Test
	void agregarRecetasConCiclos() {
		//recetaMesa -> receta mesa necesita un clavo esta definido
		
		//nueva receta recetaClavoConCiclo requiere mesa
		Map<Objeto, Integer> ingredientesClavoAlt = new HashMap<>();
		ingredientesClavoAlt.put(mesa, 7);
		//esta receta pertenece a clavo.
		Receta recetaClavoConCiclo = new Receta(clavo, ingredientesClavoAlt, 5, 2);
		
		//primero agrego la receta mesa al recetario 
		//pertence a las recetas de mesa
		recetario.agregarReceta(recetaMesa);
		
		//agrego la receta clavo que referencia a mesa
		//pertence a las recetas de clavo
		recetario.agregarReceta(recetaClavoConCiclo);
		
		//no tiene que cargarse la recete mesaCiclo de clavo
		//solo debe haber 1 receta
		assertEquals(1,recetario.getRecetasPorObjeto().size());
		
		//si no esta la recetaConCiclos de clavo preguntar por ella es null
		assertEquals(null,recetario.getRecetasPorObjeto().get(clavo));
		
		//preguntar recetaMesa de mesa si existe
		assertEquals(recetaMesa,recetario.getRecetasPorObjeto().get(mesa).getFirst());
	}
}