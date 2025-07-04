package modelo;

/**
 * Implementación concreta de una mesa de fundición.
 * Extiende {@link MesaDeTrabajo} y define funcionalidades específicas para fundir materiales.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class MesaDeFundicion extends MesaDeTrabajo {

	/**
     * Constructor que inicializa una mesa de fundición con el nombre predeterminado.
     */
	public MesaDeFundicion() {
		super("Mesa de Fundicion");
	}

	/**
     * Devuelve el código hash de esta mesa de fundición.
     * 
     * @return Código hash.
     */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
     * Compara esta mesa de fundición con otro objeto para verificar igualdad.
     * 
     * @param obj Objeto a comparar.
     * @return true si ambos objetos son iguales, false en caso contrario.
     */
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
}
