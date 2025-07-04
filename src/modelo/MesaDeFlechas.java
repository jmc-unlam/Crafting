package modelo;

/**
 * Implementación concreta de una mesa de flechas
 * Extiende {@link MesaDeTrabajo} y define funcionalidades específicas para fundir materiales.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
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
