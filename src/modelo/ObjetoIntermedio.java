package modelo;

public class ObjetoIntermedio extends Objeto {

	public ObjetoIntermedio(String nombre) {
		super(nombre);
	}

	public boolean esBasico() {
		return false;
	}

	@Override
	public String toString() {
		return "ObjetoIntermedio: " + getNombre();
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
		if (!(obj instanceof ObjetoIntermedio)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean esApilable() {
		return true;
	}
}
