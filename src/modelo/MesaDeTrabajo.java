package modelo;

public class MesaDeTrabajo extends Objeto {  //Alorda, quite el abstract para probar algo.

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
