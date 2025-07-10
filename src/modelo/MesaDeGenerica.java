package modelo;

/**
 * Implementación concreta de una mesa de generica.
 * Extiende {@link MesaDeTrabajo} y define funcionalidades específicas para fundir materiales.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class MesaDeGenerica extends MesaDeTrabajo {

	public MesaDeGenerica() {
		super("Mesa Generica");
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
