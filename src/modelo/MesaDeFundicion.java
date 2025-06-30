package modelo;

public class MesaDeFundicion extends MesaDeTrabajo {

	public MesaDeFundicion() {
		super("Mesa de Fundicion");
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
		if (!(obj instanceof MesaDeFundicion)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "MesaDeFundicion: " + getNombre();
	}
}
