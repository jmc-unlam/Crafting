package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Recetario {
    private List<Receta> recetas;

    public Recetario() {
        this.recetas = new ArrayList<>();
    }

    public Recetario(List<Receta> recetasIniciales) {
        this.recetas = new ArrayList<>();
        if (recetasIniciales != null) {
            for (Receta receta : recetasIniciales) {
                this.agregarReceta(receta); // Usamos nuestro método para validar duplicados
            }
        }
    }

    public void agregarReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula");
        }
        
        if (buscarReceta(receta.getObjetoProducido()) != null) {
            throw new IllegalArgumentException("Ya existe una receta para el objeto: " + 
                receta.getObjetoProducido().getNombre());
        }
        
        recetas.add(receta);
    }

    public void removerReceta(Receta receta) {
        if (!recetas.remove(receta)) {
            throw new IllegalArgumentException("La receta no existe en el recetario");
        }
    }

    public List<Receta> getRecetas() {
        return new ArrayList<>(recetas); // Devolver una copia para evitar mutaciones directas
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== RECETARIO ===\n");
        for (Receta receta : recetas) {
            sb.append(receta.toString()) 
                    .append("\n");
        }
        return sb.toString();
    }

    public Receta buscarReceta(Objeto objetoDeseado) {
        return recetas.stream()
            .filter(r -> r.getObjetoProducido().equals(objetoDeseado))
            .findFirst()
            .orElse(null);
    }
    
    public Map<Objeto, Integer> buscarIngredientes(Objeto objeto) {
        return recetas.stream()
            .filter(r -> r.getObjetoProducido().equals(objeto))
            .map(Receta::getIngredientes)
            .findFirst()
            .orElse(Collections.emptyMap());
    }
}
