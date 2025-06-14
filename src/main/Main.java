package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Inventario;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;
import modelo.Recetario;
import modelo.RegistroCrafteo;
import modelo.SistemaDeCrafteo;

public class Main {
    public static void main(String[] args) {
        // Paso 1: Crear objetos 
    	ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoBasico piedra = new ObjetoBasico("Piedra");
        ObjetoBasico hierro = new ObjetoBasico("Hierro");
        ObjetoIntermedio oxido = new ObjetoIntermedio("Oxido");
        ObjetoIntermedio hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");

        // Paso 2: Crear ingredientes de los objetos
        Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
        ingredientesHachaDePiedra.put(piedra, 3);
        ingredientesHachaDePiedra.put(madera, 2);
        ingredientesHachaDePiedra.put(oxido, 2);
        
        Map<Objeto, Integer> ingredientesOxido = new HashMap<>();
        ingredientesOxido.put(hierro, 7);

        // Paso 3: Crear recetas
                
        Receta recetaHachaDePiedra = new Receta(hachaDePiedra,ingredientesHachaDePiedra, 1, 5); // produce 1 hacha, tiempo 5
        Receta recetaOxido = new Receta(oxido,ingredientesOxido,1,2);

        // Paso 4: Inicializar inventario y agregar algunos recursos
        Inventario inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(piedra, 5);
        inventario.agregarObjeto(hierro, 4);

        // Paso 5: Inicializar recetario y sistema de crafteo
        Recetario recetario = new Recetario();
        recetario.agregarReceta(recetaHachaDePiedra);
        recetario.agregarReceta(recetaOxido);

        SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

        // Paso 6: Probar funcionalidades del sistema
        Map<Objeto, Integer> ingredientes = null;
        System.out.println("=== Ingredientes necesarios para Hacha de Piedra ===");
        ingredientes = sistema.ingredientesNecesarios(hachaDePiedra);
        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
        
        System.out.println("=== Ingredientes basicos necesarios para Hacha de Piedra ===");
        ingredientes = sistema.ingredientesBasicosNecesarios(hachaDePiedra);
        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
        
        System.out.println("\n=== Pruebas de faltantes ===");
        
        // 1. Prueba ingredientesFaltantesParaCraftear
        System.out.println("\nIngredientes faltantes para Hacha de Piedra:");
        Map<Objeto, Integer> faltantes = sistema.ingredientesFaltantesParaCraftear(hachaDePiedra);
        if (faltantes.isEmpty()) {
            System.out.println("No faltan ingredientes directos!");
        } else {
            faltantes.forEach((obj, cant) -> 
                System.out.println("- Faltan " + cant + " de " + obj.getNombre()));
        }
        
        // 2. Prueba ingredientesBasicosFaltantesParaCraftear
        System.out.println("\nIngredientes básicos faltantes para Hacha de Piedra:");
        Map<Objeto, Integer> faltantesBasicos = sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra);
        if (faltantesBasicos.isEmpty()) {
            System.out.println("No faltan ingredientes básicos!");
        } else {
            faltantesBasicos.forEach((obj, cant) -> 
                System.out.println("- Faltan " + cant + " unidades básicas de " + obj.getNombre()));
        }
        
        // 3. Prueba cantidadCrafteable
        System.out.println("\n=== Prueba de cantidad crafteable ===");
        System.out.println("Hachas de piedra crafteables: " + sistema.cantidadCrafteable(hachaDePiedra));
        System.out.println("Óxido crafteable: " + sistema.cantidadCrafteable(oxido));
        
        // 4. Prueba después de agregar más recursos
        System.out.println("\n[Agregando 10 hierros más al inventario...]");
        inventario.agregarObjeto(hierro, 10);
        
        System.out.println("\nNuevos valores después de agregar recursos:");
        System.out.println("Hachas de piedra crafteables ahora: " + sistema.cantidadCrafteable(hachaDePiedra));
        System.out.println("Óxido crafteable ahora: " + sistema.cantidadCrafteable(oxido));
        
        System.out.println("\nNuevos ingredientes básicos faltantes para Hacha de Piedra:");
        sistema.ingredientesBasicosFaltantesParaCraftear(hachaDePiedra)
              .forEach((obj, cant) -> System.out.println("- " + obj.getNombre() + ": " + cant));
        
        try {
            System.out.println("\n=== Intentando craftear 1x" +hachaDePiedra);
            int tiempo = sistema.craftearObjeto(hachaDePiedra, 1);
            System.out.println("Tiempo total: " + tiempo);
            
            // Mostrar historial
            System.out.println(sistema.getHistorial());
            
            // Mostrar inventario actualizado
            System.out.println("\nInventario actual:");
            inventario.getObjetos().forEach((obj, cant) -> 
                System.out.println("- " + obj.getNombre() + ": " + cant));
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
