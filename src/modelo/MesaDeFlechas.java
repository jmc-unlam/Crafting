package modelo;

public class MesaDeFlechas extends MesaDeTrabajo {

	public MesaDeFlechas() {
		super("Mesa de Flechas");
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
		if (!(obj instanceof MesaDeFlechas)) {
			return false;
		}
		return true;
	}
}
