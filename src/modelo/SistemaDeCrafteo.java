package modelo;

import java.util.HashMap;
import java.util.Map;

public class SistemaDeCrafteo {
    private Inventario inventario;
    private Recetario recetario;
    private HistorialDeCrafteo historial;

    public SistemaDeCrafteo(Inventario inventario, Recetario recetario) {
        this.inventario = inventario;
        this.recetario = recetario;
        this.historial = new HistorialDeCrafteo();
    }

    // 1. Mostrar ingredientes necesarios para craftear un objeto (primer nivel)
    public Map<Objeto, Integer> getIngredientesDirectos(Objeto objeto) {
        return objeto.getIngredientes(); 
    }

    // 2. Mostrar todos los ingredientes básicos necesarios para craftear un objeto desde cero
    public Map<Objeto, Integer> getIngredientesBasicosTotales(Objeto objeto) {
        return null;
    };

    // 3. Mostrar qué me falta para craftear un objeto (solo primer nivel)
    public Map<Objeto, Integer> getFaltantesDirectos(Objeto objeto) {
        return null;
    };

    // 4. Mostrar qué me falta para craftear un objeto desde cero (todos niveles, elementos básicos)
    public Map<Objeto, Integer> getFaltantesBasicosTotales(Objeto objeto) {
        return null;
    };

    // 5. Cuántos objetos puedo craftear con el inventario actual
    public int getCantidadCrafteable(Objeto objeto) {
        return 0;
    };

    // 6. Realizar un crafteo y actualizar inventario e historial
    public void Craftear(Objeto objeto) {
    	if (sePuedeCrear(objeto)) {
            throw new IllegalStateException("No hay suficientes ingredientes en el inventario para craftear.");
        }

        // Remover los ingredientes del inventario
        for (Map.Entry<Objeto, Integer> entry : objeto.getReceta().getIngredientes().entrySet()) {
            inventario.removerObjeto(entry.getKey(), entry.getValue());
        }

        // Agregar el objeto resultante al inventario
        inventario.agregarObjeto(objeto.getReceta().getObjetoProducido(), objeto.getReceta().getCantidadProducida());

        // Registrar el crafteo
        historial.agregarRegistro(objeto.getReceta().getObjetoProducido(), objeto.getReceta().getCantidadProducida());
    };

    // 7. Obtener el tiempo total necesario para craftear una cantidad del objeto
    public int getTiempoTotal(Objeto objeto, int cantidad) {
        return 0;
    };

    // 8. Obtener historial de crafteos
    public HistorialDeCrafteo getHistorial() {
		return historial;
        
    };
    
    // Método para verificar si se puede crear el objeto dado un inventario
    public boolean sePuedeCrear(Objeto objeto) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = ingredientesDisponibles.getOrDefault(ingrediente, 0);

            if (cantidadDisponible < cantidadNecesaria) {
                return false;
            }
        }
        return true;
    }
    
    // Mostrar los ingredientes necesarios para crear un objeto
    public void mostrarIngredientes(Objeto objeto) {
        System.out.println("Ingredientes necesarios para crear " + objeto.getNombre() + ":");
        Map<Objeto, Integer> ingredientes = objeto.getIngredientes();
        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
    }

    // Mostrar los ingredientes básicos necesarios para crear un objeto
    public void mostrarIngredientesBasicos(Objeto objeto) {
        System.out.println("Ingredientes básicos necesarios para crear " + objeto.getNombre() + ":");
        Map<Objeto, Integer> ingredientesBasicos = objeto.getIngredientesBasicos();
        for (Map.Entry<Objeto, Integer> entry : ingredientesBasicos.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
    }
    
 // Obtener los ingredientes faltantes para crear un objeto dado un inventario
    public Map<Objeto, Integer> getIngredientesFaltantes(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();
        Map<Objeto, Integer> ingredientesFaltantes = new HashMap<>();

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingrediente);

            if (cantidadNecesaria > cantidadDisponible) {
                ingredientesFaltantes.put(ingrediente, cantidadNecesaria - cantidadDisponible);
            }
        }
        return ingredientesFaltantes;
    }

    // Obtener los ingredientes básicos faltantes para crear un objeto dado un inventario
    public Map<Objeto, Integer> getIngredientesFaltantesBasicos(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesBasicosNecesarios = objeto.getIngredientesBasicos();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();
        Map<Objeto, Integer> ingredientesBasicosFaltantes = new HashMap<>();

        for (Map.Entry<Objeto, Integer> entry : ingredientesBasicosNecesarios.entrySet()) {
            Objeto ingredienteBasico = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingredienteBasico);

            if (cantidadNecesaria > cantidadDisponible) {
                ingredientesBasicosFaltantes.put(ingredienteBasico, cantidadNecesaria - cantidadDisponible);
            }
        }
        return ingredientesBasicosFaltantes;
    }

    // Calcular cuántas veces se puede craftear un objeto dado un inventario
    public int cantidadCrafteable(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        int cantidadMinima = Integer.MAX_VALUE;

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingrediente);

            int vecesPosibles = cantidadDisponible / cantidadNecesaria;
            if (vecesPosibles < cantidadMinima) {
                cantidadMinima = vecesPosibles;
            }
        }
        return cantidadMinima;
    }

    public Inventario getInventario() {
		return inventario;};
    public Recetario getRecetario() {
		return recetario;};
}
