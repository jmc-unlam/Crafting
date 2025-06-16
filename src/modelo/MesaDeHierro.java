package modelo;

import java.util.List;

public class MesaDeHierro extends MesaDeTrabajo {

	public MesaDeHierro(List<Receta> recetasDesbloqueadas) {
		super("Mesa de Hierro", recetasDesbloqueadas);
	}

	@Override
	public boolean esBasico() {
		return false;
	}

	@Override
	public boolean esApilable() {
		return false;
	}
}
