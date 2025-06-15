package modelo;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
		if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
		try {
			Receta receta = recetario.buscarReceta(objeto);
			return receta.getIngredientes();
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:"+ objeto);
        }
    }

    public Map<Objeto, Integer> ingredientesBasicosNecesarios(Objeto objeto) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	try {
			Receta receta = recetario.buscarReceta(objeto);
			return receta.getIngredientesBasicos(recetario);
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:"+ objeto);
        }
    }

    public Map<Objeto, Integer> ingredientesFaltantesParaCraftear(Objeto objeto) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	return inventario.getFaltantes(ingredientesNecesarios(objeto));
    }

    public Map<Objeto, Integer> ingredientesBasicosFaltantesParaCraftear(Objeto objeto) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	return inventario.getFaltantesBasicos(ingredientesBasicosNecesarios(objeto), recetario);

    }

    public int cantidadCrafteable(Objeto objeto) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
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
        if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto.getNombre());
        }
                
        Receta receta = recetario.buscarReceta(objeto);
        if (receta == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto.getNombre());
        }
        

        // Calcular tiempo total (empezando con el tiempo de este crafteo)
        int tiempoTotal = receta.getTiempoBase() * cantACraftear / receta.getCantidadProducida();

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
    
    //*****Implementacion Recetas Alternativas*************
    
    public Map<Objeto, Integer> ingredientesNecesarios(Objeto objeto, int indiceReceta) {
    	if (objeto == null || indiceReceta < 0) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	if (indiceReceta < 0) {
    		throw new IllegalArgumentException("El indice de receta no debe ser negativo");
        }
		try {
			List<Receta> recetas = recetario.buscarRecetas(objeto);
			return recetas.get(indiceReceta).getIngredientes();
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene receta:"+ objeto);
        }
    }
    
    public Map<Objeto, Integer> ingredientesBasicosNecesarios(Objeto objeto, int indiceReceta) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	if (indiceReceta < 0) {
    		throw new IllegalArgumentException("El indice de receta no debe ser negativo");
        }
    	try {
    		List<Receta> recetas = recetario.buscarRecetas(objeto);
			return recetas.get(indiceReceta).getIngredientesBasicos(recetario);
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene recetas:"+ objeto);
        }
    }
    
    public Map<Objeto, Integer> ingredientesBasicosFaltantesParaCraftear(Objeto objeto, int indiceReceta) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	if (indiceReceta < 0) {
    		throw new IllegalArgumentException("El indice de receta no debe ser negativo");
        }
    	return inventario.getFaltantesBasicos(ingredientesBasicosNecesarios(objeto,indiceReceta),recetario,indiceReceta);

    }

    public int cantidadCrafteable(Objeto objeto, int indiceReceta) {
    	if (objeto == null) {
            throw new IllegalArgumentException("No existe objeto:" + objeto);
        }
    	if (indiceReceta < 0) {
    		throw new IllegalArgumentException("El indice de receta no debe ser negativo");
        }
    	return inventario.getCantidadBasico(objeto,recetario,indiceReceta);

    }
    
    public int craftearObjetoConReceta(Objeto objeto, int cantACraftear, int indiceReceta) {
    	if (cantACraftear <= 0) {
            throw new IllegalArgumentException("La cantidad a craftear debe ser positiva");
        }
    	if (indiceReceta < 0) {
    		throw new IllegalArgumentException("El indice de receta no debe ser negativo");
        }
    	// Verificar si es posible craftear la cantidad solicitada
        int maxCrafteable = cantidadCrafteable(objeto);
        if (maxCrafteable < cantACraftear) {
            throw new IllegalStateException("No hay suficientes materiales para craftear " + cantACraftear + " " + objeto.getNombre());
        }
        if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto.getNombre());
        }
                
        List<Receta> recetas = recetario.buscarRecetas(objeto);
        if (recetas == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto.getNombre());
        }
        

        // Calcular tiempo total (empezando con el tiempo de este crafteo)
        int tiempoTotal = recetas.get(indiceReceta).getTiempoBase() * cantACraftear / recetas.get(indiceReceta).getCantidadProducida();

        // Procesar ingredientes recursivamente
        for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
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
        int cantidadProducida = cantACraftear * recetas.get(indiceReceta).getCantidadProducida();
        inventario.agregarObjeto(objeto, cantidadProducida);
        
        // Registrar en el historial
        historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal);

        return tiempoTotal;
    }
    
    
}
