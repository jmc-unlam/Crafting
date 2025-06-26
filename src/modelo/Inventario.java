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
        if (objeto.esBasico()) {
            return getCantidad(objeto);
        } else {
	        Receta receta = recetario.buscarReceta(objeto);
	        
	        int cantidadTotalDisponible = getCantidad(objeto);
	        int numLotesCrafteables = Integer.MAX_VALUE;
	
	        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
	            Objeto ingrediente = entry.getKey();
	            int cantidadNecesariaDelIngrediente = entry.getValue();
	            int cantidadDisponibleDelIngrediente = getCantidadBasico(ingrediente, recetario);
	
	            int lotesPosibles = cantidadDisponibleDelIngrediente / cantidadNecesariaDelIngrediente;
	            
	            // Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más escaso
	            numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
	        }
	
	        cantidadTotalDisponible += numLotesCrafteables * receta.getCantidadProducida();
	
	        return cantidadTotalDisponible;
        }
	}
	
	//*****Implementacion Recetas Alternativas*************
	public Map<Objeto, Integer> getFaltantesBasicos(Map<Objeto, Integer> requeridosBasicos, Recetario recetario, int indiceReceta) {
		Map<Objeto, Integer> faltantesBasicos = new HashMap<>();

	    for (Map.Entry<Objeto, Integer> entry : requeridosBasicos.entrySet()) {
	        Objeto ingrediente = entry.getKey();
	        int cantidadNecesaria = entry.getValue();

	        int disponible = getCantidadBasico(ingrediente,recetario,indiceReceta);

	        if (disponible < cantidadNecesaria) {
	            faltantesBasicos.put(ingrediente, cantidadNecesaria - disponible);
	        }
	    }
	    return faltantesBasicos;
	}
	
	public int getCantidadBasico(Objeto objeto, Recetario recetario, int indiceReceta) {
	    if (objeto.esBasico()) {
	        return getCantidad(objeto);
	    } else { 
	    	List<Receta> recetas = recetario.buscarRecetas(objeto);
	        
	        int cantidadTotalDisponible = getCantidad(objeto);
	        int numLotesCrafteables = Integer.MAX_VALUE;

	        for (Map.Entry<Objeto, Integer> entry : recetas.get(indiceReceta).getIngredientes().entrySet()) {
	            Objeto ingrediente = entry.getKey();
	            int cantidadNecesariaDelIngrediente = entry.getValue();
	            int cantidadDisponibleDelIngrediente = getCantidadBasico(ingrediente, recetario);

	            int lotesPosibles = cantidadDisponibleDelIngrediente / cantidadNecesariaDelIngrediente;
	            
	            // Nos quedamos con el mínimo, ya que estamos limitados por el ingrediente más escaso
	            numLotesCrafteables = Math.min(numLotesCrafteables, lotesPosibles);
	        }

	        cantidadTotalDisponible += numLotesCrafteables * recetas.get(indiceReceta).getCantidadProducida();

	        return cantidadTotalDisponible;
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
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Inventario ===\n");
        for (Map.Entry<Objeto,Integer> entry : objetos.entrySet()) {
            sb.append(entry).append("\n");
        }
        return sb.toString();
    }
}
