package modelo;

import java.io.File;
import java.util.Collections;
import java.util.List;

import datos.json.RecetaGSON;
import main.Config;

public class MesaDeTrabajo extends Objeto {

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

	@Override
	public List<Receta> listaDeRecetasPropias() {
		File archivo = new File(Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json");

		if (archivo.exists())
			return new RecetaGSON(archivo.getPath()).cargar();
		else
			return Collections.emptyList();
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
