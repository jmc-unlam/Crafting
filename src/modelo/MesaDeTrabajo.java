package modelo;

import java.io.File;
import java.util.Collections;
import java.util.List;

import datos.json.RecetaGSON;
import main.Config;

public class MesaDeTrabajo extends Objeto {
	//private List<Receta> recetasDeMesa;

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

		//if (this.recetasDeMesa == null) {
			File archivo = new File(Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json");

			if (archivo.exists()) {
				//recetasDeMesa = new RecetaGSON(archivo.getPath()).cargar();
				return new RecetaGSON(archivo.getPath()).cargar();
			} else
				return Collections.emptyList();
		//} else
			//return recetasDeMesa;
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
