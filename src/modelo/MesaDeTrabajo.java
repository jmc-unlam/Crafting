package modelo;

import java.io.File;
import java.util.Collections;
import java.util.List;

import datos.json.RecetaGSON;
import main.Config;

public abstract class MesaDeTrabajo extends Objeto {

	public MesaDeTrabajo(String nombre) {
		super(nombre);
	}

	@Override
	public boolean esBasico() {
		return false;
	}

	@Override
	public boolean esApilable() {
		return false;
	}

	//esto debe ser llamado cada vez que el inventario agrega una mesa
	@Override
	public void listaDeRecetasPropias(Recetario recetario) {
		String rutaRecetasDeMesa = Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json";
		recetario.agregarRecetas(new RecetaGSON(rutaRecetasDeMesa).cargar());
	}
	
	//esto debe ser llamado cada vez que el recetario remueve una mesa
	@Override
	public void removerRecetasPropias(Recetario recetario) {
		String rutaRecetasDeMesa = Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json";
		List<Receta> recetasDeMesa = new RecetaGSON(rutaRecetasDeMesa).cargar();
		recetario.removerRecetas(recetasDeMesa);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof MesaDeTrabajo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MesaDeTrabajo: " + getNombre();
	}
}
