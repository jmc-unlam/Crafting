package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class MesaDeTrabajo extends Objeto {
	
	private final List<Receta> recetasDesbloqueadas;

	public MesaDeTrabajo(String nombre, List<Receta> recetasDesbloqueadas) {
        super(nombre);
        if (recetasDesbloqueadas == null || recetasDesbloqueadas.isEmpty()) {
            throw new IllegalArgumentException("Una mesa debe desbloquear al menos una receta.");
        }
        this.recetasDesbloqueadas = new ArrayList<>(recetasDesbloqueadas);
    }

	public List<Receta> getRecetasDesbloqueadas() {
        return new ArrayList<>(recetasDesbloqueadas);
    }
	
	@Override
	public void activar(Recetario recetario) {
		recetario.agregarRecetas(recetasDesbloqueadas);
	}

	@Override
	public void desactivar(Recetario recetario) {
		recetario.removerRecetas(recetasDesbloqueadas);
	}
}
