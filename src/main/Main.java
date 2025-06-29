package main;

import java.util.HashMap;
import java.util.Map;

import datos.json.InventarioGSON;
import datos.json.RecetaGSON;
import modelo.Inventario;
import modelo.MesaDeFundicion;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.Recetario;
import modelo.SistemaDeCrafteo;

public class Main {


	public static void main(String[] args) {
        //escenarioCraftearHachaDePiedraConUnaReceta();
        escenarioCraftearMesaDeFundicionYSusRecetas();
    }
	 // Método para mostrar el menú
    private static void mostrarMenu() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println("1. ¿Qué necesito para craftear un objeto? - Solo primer nivel.");
        System.out.println("2. ¿Qué necesito para craftear un objeto desde cero?");
        System.out.println("3. ¿Qué me falta para craftear un objeto? - Solo primer nivel.");
        System.out.println("4. ¿Qué me falta para craftear un objeto desde cero?");
        System.out.println("5. ¿Cuántos puedo craftear? ##FALTA EL TIEMPO##");
        System.out.println("6. Realizar el crafteo indicado");
        System.out.println("7. Historial de crafteos");
        System.out.println("96. Recolectar Objetos Básicos. ##FALTA CODIFICAR##");
        System.out.println("97. Mostrar Recetario.");
        System.out.println("98. Mostrar inventario.");
        System.out.println("99. Consulta Prolog -¿Cuáles son todos los productos que podría generar con el inventario actual?");
        System.out.println("0. Salir");
        System.out.println("=========================");
    }
    
	private static void escenarioCraftearHachaDePiedraConUnaReceta() {
		//Crear objetos 
    	ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoBasico piedra = new ObjetoBasico("Piedra");
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        ObjetoIntermedio oxido = new ObjetoIntermedio("Oxido");
        ObjetoIntermedio hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");

        //Crear ingredientes de los objetos
        Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
        ingredientesHachaDePiedra.put(piedra, 3);
        ingredientesHachaDePiedra.put(madera, 2);
        ingredientesHachaDePiedra.put(oxido, 2);
        
        Map<Objeto, Integer> ingredientesOxido = new HashMap<>();
        ingredientesOxido.put(hierro, 7);

        //Crear recetas
                
        Receta recetaHachaDePiedra = new Receta(hachaDePiedra,ingredientesHachaDePiedra, 1, 5); // produce 1 hacha, tiempo 5
        Receta recetaOxido = new Receta(oxido,ingredientesOxido,1,2);

        //Inicializar inventario y agregar algunos recursos
        Inventario inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(piedra, 5);
        inventario.agregarObjeto(hierro, 4);

        //Inicializar recetario y sistema de crafteo
        Recetario recetario = new Recetario();
        recetario.agregarReceta(recetaHachaDePiedra);
        recetario.agregarReceta(recetaOxido);

        SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);
        System.out.println("=== Inventario Inicial ===");
        System.out.println(inventario);
        System.out.println("=== Recetario Inicial ===");
        System.out.println(recetario);
        

        //funcionalidades del sistema
        System.out.println("=== Ingredientes necesarios para Hacha de Piedra ===");
        Map<Objeto, Integer> ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        ingredientes.forEach((obj,cant)->System.out.println("- " + obj + ": " + cant));
        
        System.out.println("=== Ingredientes basicos necesarios para Hacha de Piedra ===");
        ingredientes = sistema.ingredientesBasicosNecesarios(hachaDePiedra);
        ingredientes.forEach((obj,cant)->System.out.println("- " + obj + ": " + cant));
        
        System.out.println("\n=== Pruebas de faltantes ===");
        
        // 1. Prueba ingredientesFaltantesParaCraftear
        System.out.println("\nIngredientes faltantes para Hacha de Piedra:");
        Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(hachaDePiedra);
        if (faltantes.isEmpty())
        	System.out.println("No faltan ingredientes directos!"); 
        else
        	faltantes.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " de " + obj));        
        
        // 2. Prueba ingredientesBasicosFaltantesParaCraftear
        System.out.println("\nIngredientes básicos faltantes para Hacha de Piedra:");
        Map<Objeto, Integer> faltantesBasicos = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
        if (faltantesBasicos.isEmpty()) 
        	System.out.println("No faltan ingredientes básicos!");
        else 
        	faltantesBasicos.forEach((obj, cant) -> System.out.println("- Faltan " + cant + " unidades básicas de " + obj));
        
        // 3. Prueba cantidadCrafteable
        System.out.println("\n=== Prueba de cantidad crafteable ===");
        System.out.println("Hachas de piedra crafteables: " + sistema.cantidadCrafteable(hachaDePiedra));
        System.out.println("Óxido crafteable: " + sistema.cantidadCrafteable(oxido));
        
        // 4. Prueba después de agregar más recursos
        System.out.println("\n[Agregando 10 hierros más al inventario...]");
        inventario.agregarObjeto(hierro, 10);
        System.out.println(inventario);
        
        System.out.println("\nNuevos valores después de agregar recursos:");
        System.out.println("Hachas de piedra crafteables ahora: " + sistema.cantidadCrafteable(hachaDePiedra));
        System.out.println("Óxido crafteable ahora: " + sistema.cantidadCrafteable(oxido));
        
        System.out.println("\nNuevos ingredientes básicos faltantes para Hacha de Piedra:");
        Map<Objeto, Integer> faltantesBasicos2 = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
        if (faltantesBasicos2.isEmpty())
            System.out.println("No faltan ingredientes básicos!");
        else
    		faltantesBasicos2.forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
        
        try {
            System.out.println("\n=== Intentando craftear 1 unidad de " +hachaDePiedra);
            System.out.println("Tiempo Total: "+sistema.craftearObjeto(hachaDePiedra, 1));
            
            System.out.println("\n=== Historial de crfateo:");
            sistema.getHistorial().forEach((registro)-> System.out.println(registro));
            
            System.out.println("\n=== Inventario final:");
            inventario.getObjetos().forEach((obj, cant) -> System.out.println("- " + obj + ": " + cant));
         
            new InventarioGSON("res/inventario_salida.json").guardar(inventario.getObjetos());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	private static void escenarioCraftearMesaDeFundicionYSusRecetas() {
		
		Inventario inventario = new Inventario(new InventarioGSON(Config.INVENTARIO_FUNDIDOR).cargar());
		Recetario recetario = new Recetario(new RecetaGSON(Config.RECETA_FUNDIDOR).cargar());
		SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario,recetario); 
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println("====Agrego mas materiales y recetas de fundicion====");
		inventario.agregarObjetos(new InventarioGSON(Config.INVENTARIO_FUNDICION).cargar());
		recetario.agregarRecetas(new RecetaGSON(Config.RECETAS_FUNDICION).cargar());
		System.out.println(inventario);
		System.out.println(recetario);
		System.out.println("===========================");
		System.out.println("===Intento craftear recetas que usan el fundidor antes===");
		try {
			sistema.craftearObjeto(recetario.getRecetas().getFirst().getObjetoProducido(), 1);
		} catch (UnsupportedOperationException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(inventario);

		System.out.println("=====Crafteando mesa de fundicion======");
		int tiempoFundidor = sistema.craftearObjeto(new MesaDeFundicion(), 1);
		System.out.println("Se crafteo la mesa de fundicion en: "+tiempoFundidor);
		System.out.println(inventario);
		System.out.println("======Crafteando las recetas de fundicion========");

		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting ["+r.getObjetoProducido()+"] en "+sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println("===========================");
		}
		System.out.println("======Crafteando Segunda Vuelta========");
		for (Receta r : recetario.getRecetas()) {
			try {
				System.out.println("Crafting ["+r.getObjetoProducido()+"] en "+sistema.craftearObjeto(r.getObjetoProducido(), 1));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println(inventario);
			System.out.println("===========================");
		}

	}
}
