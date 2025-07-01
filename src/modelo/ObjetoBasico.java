package modelo;

public class ObjetoBasico extends Objeto {
	public ObjetoBasico(String nombre) {
		super(nombre);
	}

	public boolean esBasico() {
		return true;
	}

	@Override
	public String toString() {
		return "ObjetoBásico: " + getNombre();
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
		if (!(obj instanceof ObjetoBasico)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean esApilable() {
		return true;
	}
}
