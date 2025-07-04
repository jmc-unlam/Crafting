package datos.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import modelo.Receta;

/**
 * Manejador específico para la serialización de recetas usando GSON.
 * Convierte entre recetas del modelo y el formato serializable.
 * 
 * @author Jorge
 * @version 1.0
 */
public class RecetaGSON extends ManejadorGSON<List<RecetaSerializable>> {

	/**
     * Constructor que inicializa el manejador con la ruta del archivo.
     * 
     * @param rutaArchivo Ruta del archivo JSON.
     */
	public RecetaGSON(String rutaArchivo) {
		super(rutaArchivo);
		datos =  new ArrayList<>();
		listType =  new TypeToken<List<RecetaSerializable>>() {}.getType();
	}

	/**
     * Carga las recetas desde el archivo JSON y las convierte al modelo.
     * Si el archivo falla se devuelve una lista vacia
     * 
     * @return Lista de recetas.
     */
	public List<Receta> cargar() {
		super.cargarJSON();
		System.out.println("Receta leidas desde: " + super.getRutaArchivo());
		List<Receta> recetas = new ArrayList<>();
		try {
	        for (RecetaSerializable recetaJson : datos) {
	        	Receta recetaConvertida = recetaJson.toReceta();
	            recetas.add(recetaConvertida);
	        }
		} catch (NullPointerException e) {
			//si esto pasa devolvera una lista vacia.
        	System.err.println("Recetas leidas no tiene nada: " + super.getRutaArchivo() + ". Se carga una lista vacía.");
		}
		return recetas;
	}
	
	/**
     * Guarda recetas en formato JSON desde una lista de recetas del modelo.
     * 
     * @param recetas Recetas a guardar.
     */
	public void guardar(List<Receta> recetas) {
		List<RecetaSerializable> recetasJSON = new ArrayList<>();
	    for (Receta receta : recetas) {
	    	recetasJSON.add(RecetaSerializable.fromReceta(receta));
	    }
	    super.guardarJSON(recetasJSON);
	    System.out.println("Receta guardadas en: " + super.getRutaArchivo());
	}

}
