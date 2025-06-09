package modelo;

import java.util.ArrayList;
import java.util.List;

public class Recetario {
    private List<Receta> recetas;

    // Constructor
    public Recetario() {
        this.recetas = new ArrayList<>();
    }

    // Agregar una receta
    public void agregarReceta(Receta receta) {
        recetas.add(receta);
    }

    // Remover una receta
    public void removerReceta(Receta receta) {
        recetas.remove(receta);
    }

    // Obtener todas las recetas
    public List<Receta> getRecetas() {
        return new ArrayList<>(recetas); // Devolver una copia para evitar mutaciones directas
    }
}
