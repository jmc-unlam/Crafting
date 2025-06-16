package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Recetario {
	private Map<Objeto, List<Receta>> recetasPorObjeto;

    public Recetario() {
        this.recetasPorObjeto = new HashMap<>();
    }

    public Recetario(List<Receta> recetasIniciales) {
        this.recetasPorObjeto = new HashMap<>();
        if (recetasIniciales != null) {
            for (Receta receta : recetasIniciales) {
                this.agregarReceta(receta); // Usa el nuevo método que admite múltiples recetas
            }
        }
    }

    public void agregarReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula");
        }
    	recetasPorObjeto.putIfAbsent(receta.getObjetoProducido(), new ArrayList<>());
        recetasPorObjeto.get(receta.getObjetoProducido()).add(receta);
    }

    public void removerReceta(Receta receta) {
    	if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula");
        }
        List<Receta> recetas = recetasPorObjeto.get(receta.getObjetoProducido());
        if (recetas == null || !recetas.remove(receta)) {
            throw new IllegalArgumentException("La receta no existe en el recetario");
        }
        // Eliminar la clave si la lista queda vacía
        if (recetas.isEmpty()) {
            recetasPorObjeto.remove(receta.getObjetoProducido());
        }
    }

    public List<Receta> getRecetas() {
    	List<Receta> todas = new ArrayList<>();
    	for (List<Receta> lista : recetasPorObjeto.values()) {
            todas.addAll(lista);
        }
    	return todas; // Devuelve toda las recetas.
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== RECETARIO ===\n");
        for (Receta receta : this.getRecetas()) {
            sb.append(receta.toString()) 
                    .append("\n");
        }
        return sb.toString();
    }

    public Receta buscarReceta(Objeto objetoDeseado) {
    	List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
    	if (recetas == null || recetas.isEmpty()) {
            throw new NoSuchElementException(
                "No se encontró ninguna receta para el objeto: " + objetoDeseado.getNombre()
            );
        }
        return recetas.get(0); // Devolver la primera receta disponible
    }
    
    public List<Receta> buscarRecetas(Objeto objetoDeseado) {
        List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
        if (recetas == null || recetas.isEmpty()) {
            throw new NoSuchElementException("No existen recetas asociadas a :" + objetoDeseado);
        }
        return new ArrayList<>(recetas);
    }
    
    public Map<Objeto, Integer> buscarIngredientes(Objeto objeto) {
    	List<Receta> recetas = recetasPorObjeto.get(objeto);
    	if (recetas == null || recetas.isEmpty()) {
            return Collections.emptyMap();
        }
        return new HashMap<>(recetas.get(0).getIngredientes()); // Solo de la primera receta
    }

    //*****Implementacion Mesas de Trabajo*************
	public void agregarRecetas(List<Receta> recetas) {
		for (Receta receta : recetas ) {
			this.agregarReceta(receta);
		}
	}

	public void removerRecetas(List<Receta> recetas) {
		for (Receta receta : recetas ) {
			this.removerReceta(receta);
		}
	}
}
