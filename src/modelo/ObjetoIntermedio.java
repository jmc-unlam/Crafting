package modelo;

/**
 * Representa un objeto compuesto lógico en el sistema de crafteo.
 * Aunque físicamente no contiene directamente sus ingredientes,
 * actúa como un proxy para vincularse con recetas que definen su composición.
 * 
 * Es parte del patrón Composite en una variante externalizada,
 * No almacena la estructura interna.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public class ObjetoIntermedio extends Objeto {

	/**
     * Constructor que inicializa un objeto intermedio con un nombre.
     * 
     * @param nombre Nombre del objeto compuesto.
     */
	public ObjetoIntermedio(String nombre) {
		super(nombre);
	}

	/**
     * Indica que este objeto es compuesto (crafteable).
     * 
     * @return false, ya que requiere una receta para ser creado.
     */
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

	/**
     * Indica que los objetos compuestos son apilables por defecto.
     * 
     * @return true, ya que pueden tener múltiples unidades en el inventario.
     */
	@Override
	public boolean esApilable() {
		return true;
	}
}
