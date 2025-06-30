package modelo;

public class MesaDePiedra extends MesaDeTrabajo {

	public MesaDePiedra() {
		super("Mesa De Piedra");
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
		if (!(obj instanceof MesaDePiedra)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "MesaDePiedra: " + getNombre();
	}
}
