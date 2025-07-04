package modelo;

/**
 * Implementación concreta de una mesa de piedra.
 * Extiende {@link MesaDeTrabajo} y define funcionalidades específicas para fundir materiales.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
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
}
