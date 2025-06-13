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
        // Paso 1: Crear objetos básicos
        ObjetoBasico madera = new ObjetoBasico("Madera");
        ObjetoBasico piedra = new ObjetoBasico("Piedra");
        ObjetoBasico hierro = new ObjetoBasico("Hierro");

        // Paso 2: Crear recetas para objetos intermedios
        Map<Objeto, Integer> ingredientesHachaDePiedra = new HashMap<>();
        ingredientesHachaDePiedra.put(piedra, 3);
        ingredientesHachaDePiedra.put(madera, 2);

                // Paso 3: Crear objeto intermedio
        ObjetoIntermedio hachaDePiedra = new ObjetoIntermedio("Hacha de Piedra");
        
        Receta recetaHachaDePiedra = new Receta(hachaDePiedra,ingredientesHachaDePiedra, 1, 5); // produce 1 hacha, tiempo 5

        // Paso 4: Inicializar inventario y agregar algunos recursos
        Inventario inventario = new Inventario();
        inventario.agregarObjeto(madera, 10);
        inventario.agregarObjeto(piedra, 5);

        // Paso 5: Inicializar recetario y sistema de crafteo
        Recetario recetario = new Recetario();
        recetario.agregarReceta(recetaHachaDePiedra);

        SistemaDeCrafteo sistema = new SistemaDeCrafteo(inventario, recetario);

        // Paso 6: Probar funcionalidades del sistema

        System.out.println("=== Ingredientes necesarios para Hacha de Piedra ===");
        Map<Objeto, Integer> ingredientesDirectos = sistema.getIngredientesDirectos(hachaDePiedra);
        for (Map.Entry<Objeto, Integer> entry : ingredientesDirectos.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }

        System.out.println("\n=== ¿Cuántas hachas puedo fabricar? ===");
        int cantidadFabricable = sistema.getCantidadCrafteable(hachaDePiedra);
        System.out.println("Puedes fabricar: " + cantidadFabricable + " unidades");

        if (cantidadFabricable > 0) {
            System.out.println("\n=== Realizando crafteo de 1 Hacha de Piedra ===");
            //sistema.realizarCrafteo(hachaDePiedra, 1);

            System.out.println("\n=== Inventario actualizado ===");
            for (Map.Entry<Objeto, Integer> entry : inventario.getObjetos().entrySet()) {
                System.out.println(entry.getKey().getNombre() + ": " + entry.getValue());
            }

            System.out.println("\n=== Historial de crafteos ===");
            List<RegistroCrafteo> historial = sistema.getHistorial().getRegistros();
            for (RegistroCrafteo registro : historial) {
                System.out.println(registro);
            }
        }

        System.out.println("\n=== Tiempo total estimado para fabricar 1 Hacha de Piedra ===");
        int tiempo = sistema.getTiempoTotal(hachaDePiedra, 1);
        System.out.println("Tiempo necesario: " + tiempo + " segundos");
    }
}
