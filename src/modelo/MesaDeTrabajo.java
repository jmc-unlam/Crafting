package modelo;

import java.util.List;

import datos.json.RecetaGSON;
import main.Config;

/**
 * Clase abstracta que representa un objeto especializado para habilitar recetas.
 * Extiende {@link Objeto} y actúa como un objeto compuesto lógico en el patrón Composite, 
 * pero con reglas específicas de unicidad y carga de recetas.
 * 
 * @author Grupo Gamma
 * @version 1.0
 */
public abstract class MesaDeTrabajo extends Objeto {

	/**
     * Constructor que inicializa una mesa de trabajo con un nombre.
     * 
     * @param nombre Nombre de la mesa de trabajo.
     */
	public MesaDeTrabajo(String nombre) {
		super(nombre);
	}

	/**
     * Indica que las mesas de trabajo son objetos compuestos (no básicos).
     * 
     * @return false, ya que requieren ser colocadas y no son recursos directos.
     */
	@Override
	public boolean esBasico() {
		return false;
	}

	/**
     * Indica que las mesas de trabajo no son apilables.
     * 
     * @return false, ya que solo puede haber una copia en el inventario.
     */
	@Override
	public boolean esApilable() {
		return false;
	}

	/**
     * Agrega recetas asociadas a esta mesa de trabajo al recetario.
     * Carga las recetas desde un archivo JSON específico.
     * debe ser llamado cuando una mesa es agregada el inventario
     * 
     * @param recetario Recetario donde se agregarán las recetas.
     */
	@Override
	public void listaDeRecetasPropias(Recetario recetario) {
		String rutaRecetasDeMesa = Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json";
		recetario.agregarRecetas(new RecetaGSON(rutaRecetasDeMesa).cargar());
	}
	
	/**
     * Remueve las recetas asociadas a esta mesa de trabajo del recetario.
     * debe ser llamado cunado una mesa es removida del inventario
     * 
     * @param recetario Recetario donde se removerán las recetas.
     */
	@Override
	public void removerRecetasPropias(Recetario recetario) {
		String rutaRecetasDeMesa = Config.RECETAS_DE_MESAS_DIR + this.getNombre() + ".json";
		List<Receta> recetasDeMesa = new RecetaGSON(rutaRecetasDeMesa).cargar();
		recetario.removerRecetas(recetasDeMesa);
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
