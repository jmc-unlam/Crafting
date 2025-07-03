package modelo;

public class MesaDeGenerica extends MesaDeTrabajo {

	public MesaDeGenerica(String nombre) {
		super("Mesa de Generica");
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
		if (!(obj instanceof MesaDeGenerica)) {
			return false;
		}
		return true;
	}
}
