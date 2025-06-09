package modelo;

import java.util.HashMap;
import java.util.Map;

public class Receta {
	private ObjetoIntermedio objetoProducido;
    private Map<Objeto, Integer> ingredientes;
    private int cantidadProducida;
    private int tiempoBase;

    // Constructor
    public Receta(Map<Objeto, Integer> ingredientes, int cantidadProducida, int tiempoBase) {
        this.ingredientes = ingredientes;
        this.cantidadProducida = cantidadProducida;
        this.tiempoBase = tiempoBase;
    }

    // Métodos getters
    public Map<Objeto, Integer> getIngredientes() {
        return ingredientes;
    }

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public int getTiempoBase() {
        return tiempoBase;
    }

    // Método para calcular el tiempo total necesario para crear el objeto
    public int calcularTiempoTotal() {
        int tiempoTotal = tiempoBase;
        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            Objeto ingrediente = entry.getKey();
            if (!ingrediente.esBasico()) {
                tiempoTotal += ingrediente.getReceta().calcularTiempoTotal();
            }
        }
        return tiempoTotal;
    }

    
    // Método para obtener todos los ingredientes totales (incluyendo sub-ingredientes)
    public Map<Objeto, Integer> getIngredientesTotales() {
        Map<Objeto, Integer> ingredientesTotales = new HashMap<>(getIngredientes());

        for (Map.Entry<Objeto, Integer> entry : getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey();
            if (!ingrediente.esBasico()) {
                Map<Objeto, Integer> subIngredientes = ingrediente.getReceta().getIngredientesTotales();
                for (Map.Entry<Objeto, Integer> subEntry : subIngredientes.entrySet()) {
                    Objeto subIngrediente = subEntry.getKey();
                    int cantidadSubIngrediente = subEntry.getValue();
                    int cantidadActual = ingredientesTotales.getOrDefault(subIngrediente, 0);
                    ingredientesTotales.put(subIngrediente, cantidadActual + cantidadSubIngrediente);
                }
            }
        }
        return ingredientesTotales;
    }

	public Map<Objeto, Integer> getIngredientesBasicos() {
		return null;
	}

	public Objeto getObjetoProducido() {
		return objetoProducido;
	}
}
