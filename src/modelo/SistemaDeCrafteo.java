package modelo;

import java.util.List;
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

    public List<RegistroCrafteo> getHistorial() {
		return historial.getRegistros();
    };
    
    public Map<Objeto, Integer> getInventario() {
		return inventario.getObjetos();
	};
    public List<Receta> getRecetario() {
		return recetario.getRecetas();
	};
	
	public Map<Objeto, Integer> ingredientesNecesarios(Objeto objeto) {
		return recetario.buscarReceta(objeto).getIngredientes();
    }

    public Map<Objeto, Integer> ingredientesBasicosNecesarios(Objeto objeto) {
    	return recetario.buscarReceta(objeto).getIngredientesBasicos(recetario);
    }

    public Map<Objeto, Integer> ingredientesFaltantesParaCraftear(Objeto objeto) {
    	return inventario.getFaltantes(ingredientesNecesarios(objeto));
    }

    public Map<Objeto, Integer> ingredientesBasicosFaltantesParaCraftear(Objeto objeto) {
    	
    	return inventario.getFaltantesBasicos(ingredientesBasicosNecesarios(objeto), recetario);

    }

    public int cantidadCrafteable(Objeto objeto) {
    	return inventario.getCantidadBasico(objeto, recetario);

    }

    public int craftearObjeto(Objeto objeto, int cantACraftear){
    	if (cantACraftear <= 0) {
            throw new IllegalArgumentException("La cantidad a craftear debe ser positiva");
        }

        // Verificar si es posible craftear la cantidad solicitada
        int maxCrafteable = cantidadCrafteable(objeto);
        if (maxCrafteable < cantACraftear) {
            throw new IllegalStateException("No hay suficientes materiales para craftear " + cantACraftear + " " + objeto.getNombre());
        }

        // Si es objeto básico, no se puede craftear
        if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto.getNombre());
        }

        // Obtener la receta
        Receta receta = recetario.buscarReceta(objeto);
        if (receta == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto.getNombre());
        }

        // Calcular tiempo total (empezando con el tiempo de este crafteo)
        int tiempoTotal = receta.getTiempoBase() * cantACraftear;

        // Procesar ingredientes recursivamente
        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue() * cantACraftear;

            if (!ingrediente.esBasico()) {
                int cantidadExistente = inventario.getCantidad(ingrediente);
                int cantidadAFabricar = Math.max(0, cantidadNecesaria - cantidadExistente);

                if (cantidadAFabricar > 0) {
                    tiempoTotal += craftearObjeto(ingrediente, cantidadAFabricar);
                }
            }

            inventario.removerObjeto(ingrediente, cantidadNecesaria);
        }

        // Agregar al inventario y registrar en historial
        int cantidadProducida = cantACraftear * receta.getCantidadProducida();
        inventario.agregarObjeto(objeto, cantidadProducida);
        
        // Registrar en el historial
        historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal);

        return tiempoTotal;
    }
}
