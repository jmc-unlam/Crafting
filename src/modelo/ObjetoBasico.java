package modelo;

/**
 * Representa un objeto básico en el sistema de crafteo.
 * Es un nodo hoja en el patrón Composite, no tiene componentes internos.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class ObjetoBasico extends Objeto {
	
	/**
     * Constructor que inicializa un objeto básico con un nombre.
     * 
     * @param nombre Nombre del objeto básico.
     */
	public ObjetoBasico(String nombre) {
		super(nombre);
	}

	/**
     * Indica que este objeto es básico (no crafteable).
     * 
     * @return true siempre, ya que los objetos básicos no requieren recetas.
     */
	@Override
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

	/**
     * Indica que los objetos básicos son apilables por defecto.
     * 
     * @return true, ya que pueden tener múltiples unidades en el inventario.
     */
    @Override
	public boolean esApilable() {
		return true;
	}
}
