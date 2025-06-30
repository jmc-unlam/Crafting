package modelo;

import java.util.HashMap;
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
        this.historial = HistorialDeCrafteo.getInstanciaUnica();
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
			
			System.out.println("Tiempo en Crafteo (min):"+ receta.getTiempoBase());
			
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
			
			System.out.println("Tiempo de Crafteo Total (min):" + receta.calcularTiempoTotal(recetario));
			
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
    	if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
        }
    	
    	//busca la receta del objeto.(usa la primera)
    	Receta receta = recetario.buscarReceta(objeto);
    	
        if (receta == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto);
        }
        
        
        if (!inventario.tieneMesa(receta.getMesaRequerida()) ) {
        	throw new UnsupportedOperationException("No tienes ["+receta.getMesaRequerida() +"] para craftear->" + objeto);
        }

        int cantidadTotalDisponible = 0; //cantidad disponible de 
        int numLotesCrafteables = Integer.MAX_VALUE;
        
        //Recorre los ingredientes de la primera receta. Del Objeto inicial a Craftear.
        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey(); 
            int cantidadNecesariaDelIngrediente = entry.getValue();
            int cantidadDisponibleDelIngrediente = inventario.getCantidadBasico(ingrediente,recetario);

            int lotesPosibles = Math.floorDiv(cantidadDisponibleDelIngrediente, cantidadNecesariaDelIngrediente);
            
            // Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más escaso
            numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
        }

        cantidadTotalDisponible += numLotesCrafteables * receta.getCantidadProducida();

        int vecesReceta = (numLotesCrafteables == 0 ) ? 1:numLotesCrafteables;
        System.out.println("La cantidad crafteable se ejecuto en: "+receta.calcularTiempoTotal(recetario)*vecesReceta);
        return cantidadTotalDisponible;
    }

    public int craftearObjeto(Objeto objeto, int cantACraftear){
    	if (cantACraftear <= 0) {
            throw new IllegalArgumentException("La cantidad a craftear debe ser positiva");
        }
    	// Verificar si es posible craftear la cantidad solicitada
        Resultado res = inventario.cantidadPosibleCraftear(objeto, recetario);
        
        int maxCrafteable = res.getCantidadCrafteable();
        
        if (maxCrafteable < cantACraftear) {
            throw new IllegalStateException("No hay suficientes materiales para craftear " + cantACraftear + " " + objeto);
        }
        if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
        }
                
        Receta receta = recetario.buscarReceta(objeto);
        if (receta == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto);
        }
        //verificar si la receta tiene mesa
        if (!inventario.tieneMesa(receta.getMesaRequerida()) ) {
        	throw new UnsupportedOperationException("No tienes ["+receta.getMesaRequerida() +"] para craftear->" + objeto);
        }
        //verificar si se puede apilar
        if ( inventario.getCantidad(objeto)>=1 && !objeto.esApilable() ) {
        	throw new UnsupportedOperationException("No se puede crafear porque ya lo tienes, no es apilable: " + objeto);
        }

        //cuantos lotes de la receta necesitamos ejecutar.
        int vecesReceta = Math.ceilDiv(cantACraftear, receta.getCantidadProducida());

        int cantidadProducida = vecesReceta * receta.getCantidadProducida();

        int tiempoTotal = receta.getTiempoBase() * vecesReceta;
        Map<Objeto, Integer> ingredientesUsados = new HashMap<Objeto, Integer>();
        
        //Procesar y craftear sub-ingredientes
        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue() * vecesReceta;

            if (!ingrediente.esBasico()) {
                int cantidadExistente = inventario.getCantidad(ingrediente);
                int cantidadFaltante = cantidadNecesaria - cantidadExistente;

                if (cantidadFaltante > 0) {
                    tiempoTotal += craftearObjeto(ingrediente, cantidadFaltante);
                }
            }
            inventario.removerObjeto(ingrediente, cantidadNecesaria);
            ingredientesUsados.put(ingrediente, cantidadNecesaria);
        }

        //Agregar el objeto crafteado al inventario.
        inventario.agregarObjeto(objeto, cantidadProducida);

        //Registrar el crafteo en el historial.
        historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal,ingredientesUsados);

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
    	if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
        }
    	List<Receta> recetas = null;
		try {
			recetas = recetario.buscarRecetas(objeto);
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("El objeto no tiene recetas:"+ objeto);
        }
		if (recetas == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto);
        }
    	
        if (!inventario.tieneMesa(recetas.get(indiceReceta).getMesaRequerida()) ) {
        	throw new UnsupportedOperationException("No tienes ["+recetas.get(indiceReceta).getMesaRequerida() +"] para craftear->" + objeto);
        }

        int cantidadTotalDisponible = 0;
        int numLotesCrafteables = Integer.MAX_VALUE;

        for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesariaDelIngrediente = entry.getValue();
            int cantidadDisponibleDelIngrediente = inventario.getCantidadBasico(ingrediente,recetario);

            int lotesPosibles = Math.floorDiv(cantidadDisponibleDelIngrediente, cantidadNecesariaDelIngrediente);
            
            // Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más escaso
            numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
        }

        cantidadTotalDisponible += numLotesCrafteables * recetas.get(indiceReceta).getCantidadProducida();

        int vecesReceta = (numLotesCrafteables == 0 ) ? 1:numLotesCrafteables;
        System.out.println("La cantidad crafteable se ejecuto en: "+recetas.get(indiceReceta).calcularTiempoTotal(recetario)*vecesReceta);
        return cantidadTotalDisponible;

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
            throw new IllegalStateException("No hay suficientes materiales para craftear " + cantACraftear + " " + objeto);
        }
        if (objeto.esBasico()) {
            throw new UnsupportedOperationException("No se puede craftear un objeto básico: " + objeto);
        }
                
        List<Receta> recetas = recetario.buscarRecetas(objeto);
        if (recetas == null) {
            throw new IllegalStateException("No existe receta para craftear " + objeto);
        }
        //verificar si la receta tiene mesa
        if (!inventario.tieneMesa(recetas.get(indiceReceta).getMesaRequerida()) ) {
        	throw new UnsupportedOperationException("No tienes ["+recetas.get(indiceReceta).getMesaRequerida() +"] para craftear->" + objeto);
        }
        //verificar si se puede apilar
        if ( inventario.getCantidad(objeto)>=1 && !objeto.esApilable() ) {
        	throw new UnsupportedOperationException("No se puede crafear porque ya lo tienes, no es apilable: " + objeto);
        }
        
        //cuantos lotes de la receta necesitamos ejecutar.
        int vecesReceta = Math.ceilDiv(cantACraftear, recetas.get(indiceReceta).getCantidadProducida());

        int cantidadProducida = vecesReceta * recetas.get(indiceReceta).getCantidadProducida();

        int tiempoTotal = recetas.get(indiceReceta).getTiempoBase() * vecesReceta;
        
        //Procesar ingredientes recursivamente
        for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue() * vecesReceta;

            if (!ingrediente.esBasico()) {
                int cantidadExistente = inventario.getCantidad(ingrediente);
                int cantidadFaltante = cantidadNecesaria - cantidadExistente;

                if (cantidadFaltante > 0) {
                    tiempoTotal += craftearObjeto(ingrediente, cantidadFaltante);
                }
            }
            inventario.removerObjeto(ingrediente, cantidadNecesaria);
        }

        // Agregar al inventario y registrar en historial
        inventario.agregarObjeto(objeto, cantidadProducida);
        
        // Registrar en el historial
        historial.agregarRegistro(objeto, cantidadProducida, tiempoTotal);

        return tiempoTotal;
    }
    
    
}
