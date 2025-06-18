package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Inventario {
    private Map<Objeto, Integer> objetos;

    public Inventario() {
        this.objetos = new HashMap<>();
    }

    public Inventario(Map<Objeto, Integer> objetosIniciales) {
    	this.objetos = new HashMap<>();
    	for (Map.Entry<Objeto, Integer> entry : objetosIniciales.entrySet()) {
            this.agregarObjeto(entry.getKey(), entry.getValue());
        }
    }
    
    public Inventario(Map<Objeto, Integer> objetosIniciales, Recetario recetario) {
    	this.objetos = new HashMap<>();
    	for (Map.Entry<Objeto, Integer> entry : objetosIniciales.entrySet()) {
            this.agregarObjeto(entry.getKey(), entry.getValue(), recetario);
        }
    }

    public void agregarObjeto(Objeto objeto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        objetos.merge(objeto, cantidad, Integer::sum);
    }

    public void removerObjeto(Objeto objeto, int cantidad) {
    	if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
        }
        
        int cantidadActual = objetos.getOrDefault(objeto, 0);
        if (cantidadActual < cantidad) {
            throw new IllegalArgumentException("No hay suficiente cantidad del objeto en el inventario.");
        }
        
        int nuevaCantidad = cantidadActual - cantidad;
        if (nuevaCantidad == 0) {
            objetos.remove(objeto); // Eliminar completamente si llega a cero
        } else {
            objetos.put(objeto, nuevaCantidad);
        }
    }

    public Map<Objeto, Integer> getObjetos() {
        return new HashMap<>(objetos); 
    }

	public Map<Objeto, Integer> getFaltantes(Map<Objeto, Integer> requeridos) {
	    Map<Objeto, Integer> faltantes = new HashMap<>();

	    for (Map.Entry<Objeto, Integer> entry : requeridos.entrySet()) {
	        Objeto objeto = entry.getKey();
	        int cantidadRequerida = entry.getValue();
	        int cantidadEnInventario = this.getCantidad(objeto);

	        if (cantidadRequerida > cantidadEnInventario) {
	            int faltante = cantidadRequerida - cantidadEnInventario;
	            faltantes.put(objeto, faltante);
	        }
	    }
	    return faltantes;
	}
	
	public Map<Objeto, Integer> getFaltantesBasicos(Map<Objeto, Integer> requeridosBasicos, Recetario recetario) {
		Map<Objeto, Integer> faltantesBasicos = new HashMap<>();

	    for (Map.Entry<Objeto, Integer> entry : requeridosBasicos.entrySet()) {
	        Objeto ingrediente = entry.getKey();
	        int cantidadNecesaria = entry.getValue();

	        // Calculamos cuánto tenemos disponible (incluyendo desglose de intermedios)
	        int disponible = getCantidadBasico(ingrediente, recetario);

	        if (disponible < cantidadNecesaria) {
	            faltantesBasicos.put(ingrediente, cantidadNecesaria - disponible);
	        }
	    }
	    return faltantesBasicos;
	}

	public int getCantidad(Objeto objeto) {
		return objetos.getOrDefault(objeto, 0);
	}
	
	public int getCantidadBasico(Objeto objeto, Recetario recetario) {
		// Condición de corte: si es básico, devuelve la cantidad directa
	    if (objeto.esBasico()) {
	        return getCantidad(objeto);
	    } else { 
	        // Llamada recursiva para objetos compuestos
	        Receta receta = recetario.buscarReceta(objeto);
	        if (receta == null) {
	            throw new IllegalArgumentException("No se encontró una receta para el objeto intermedio: " + objeto.getNombre());
	        }
	        
	        // Obtenemos cuántas unidades podemos craftear del objeto compuesto
	        int cantidadDirecta = getCantidad(objeto);
	        Map<Objeto, Integer> ingredientes = receta.getIngredientes();
	        
	        // Calculamos cuántas unidades adicionales podríamos craftear
	        int maxCrafteable = Integer.MAX_VALUE;
	        
	        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
	            Objeto ingrediente = entry.getKey();
	            int cantidadNecesaria = entry.getValue();
	            int cantidadDisponible = getCantidadBasico(ingrediente, recetario); // Llamada recursiva
	            
	            maxCrafteable = Math.min(maxCrafteable, cantidadDisponible / cantidadNecesaria);
	        }
	        
	        // Sumamos lo que tenemos directamente más lo que podemos craftear
	        return cantidadDirecta + maxCrafteable;
	    }
	}
	
	//*****Implementacion Recetas Alternativas*************
	public Map<Objeto, Integer> getFaltantesBasicos(Map<Objeto, Integer> requeridosBasicos, Recetario recetario, int indiceReceta) {
		Map<Objeto, Integer> faltantesBasicos = new HashMap<>();

	    for (Map.Entry<Objeto, Integer> entry : requeridosBasicos.entrySet()) {
	        Objeto ingrediente = entry.getKey();
	        int cantidadNecesaria = entry.getValue();

	        // Calculamos cuánto tenemos disponible (incluyendo desglose de intermedios)
	        int disponible = getCantidadBasico(ingrediente,recetario,indiceReceta);

	        if (disponible < cantidadNecesaria) {
	            faltantesBasicos.put(ingrediente, cantidadNecesaria - disponible);
	        }
	    }
	    return faltantesBasicos;
	}
	
	public int getCantidadBasico(Objeto objeto, Recetario recetario, int indiceReceta) {
		// Condición de corte: si es básico, devuelve la cantidad directa
	    if (objeto.esBasico()) {
	        return getCantidad(objeto);
	    } else { 
	        // Llamada recursiva para objetos compuestos
	    	List<Receta> recetas = recetario.buscarRecetas(objeto);
	        if (recetas == null) {
	            throw new IllegalArgumentException("No se encontró una receta para el objeto intermedio: " + objeto.getNombre());
	        }
	        
	        // Obtenemos cuántas unidades podemos craftear del objeto compuesto
	        int cantidadDirecta = getCantidad(objeto);
	        Map<Objeto, Integer> ingredientes = recetas.get(indiceReceta).getIngredientes();
	        
	        // Calculamos cuántas unidades adicionales podríamos craftear
	        int maxCrafteable = Integer.MAX_VALUE;
	        
	        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
	            Objeto ingrediente = entry.getKey();
	            int cantidadNecesaria = entry.getValue();
	            int cantidadDisponible = getCantidadBasico(ingrediente, recetario); // Llamada recursiva
	            
	            maxCrafteable = Math.min(maxCrafteable, cantidadDisponible / cantidadNecesaria);
	        }
	        
	        // Sumamos lo que tenemos directamente más lo que podemos craftear
	        return cantidadDirecta + maxCrafteable;
	    }
	}
	
	//*****Implementacion Mesas de Trabajo*************
	public void agregarObjeto(Objeto objeto, int cantidad, Recetario recetario) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (!objeto.esApilable()) { // Aquí usamos polimorfismo con esApilable()
            if (objetos.containsKey(objeto)) {
                throw new IllegalArgumentException("El objeto  no es apilable:" + objeto);
            }
        }
        objeto.activar(recetario);
        objetos.merge(objeto, cantidad, Integer::sum);
    }
	
	public void removerObjeto(Objeto objeto, int cantidad, Recetario recetario) {
    	if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
        }
        
        int cantidadActual = objetos.getOrDefault(objeto, 0);
        if (cantidadActual < cantidad) {
            throw new IllegalArgumentException("No hay suficiente cantidad del objeto en el inventario.");
        }
        
        int nuevaCantidad = cantidadActual - cantidad;
        if (nuevaCantidad == 0) {
        	objeto.desactivar(recetario);
            objetos.remove(objeto); // Eliminar completamente si llega a cero
        } else {
            objetos.put(objeto, nuevaCantidad);
        }
    }
}
