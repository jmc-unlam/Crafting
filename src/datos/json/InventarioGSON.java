package datos.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import modelo.Objeto;

/**
 * Manejador específico para la serialización del inventario usando GSON.
 * Carga y guarda datos en formato JSON, convirtiendo entre objetos del modelo y estructuras serializables.
 * 
 * @author Jorge
 * @version 1.0
 */
public class InventarioGSON extends ManejadorGSON<List<InventarioSerializable>> {

	/**
     * Constructor que inicializa el manejador con la ruta del archivo.
     * 
     * @param rutaArchivo Ruta del archivo JSON.
     */
	public InventarioGSON(String rutaArchivo) {
		super(rutaArchivo);
		datos = new ArrayList<>();
		listType =  new TypeToken<List<InventarioSerializable>>() {}.getType();
	}

	/**
     * Carga el inventario desde el archivo JSON y lo convierte a un Map<Objeto, Integer>.
     * Si falla el archivo, devuelve un mapa vacío. 
     * 
     * @return Mapa de objetos y sus cantidades.
     */
	public Map<Objeto,Integer> cargar() {
		super.cargarJSON();
		System.out.println("Inventario leido desde: " + super.getRutaArchivo());
		Map<Objeto, Integer> objetos = new HashMap<>();
		try {
	        for (InventarioSerializable entry : datos) {
	            objetos.put(entry.getObjeto(), entry.getCantidad());
	        }
		} catch (NullPointerException e) {
			//si esto pasa devolvera una lista vacia.
        	System.err.println("Inventario leido esta sin nada: " + super.getRutaArchivo() + ". Se carga una lista vacía.");
		}
		
        return objetos;
	}
	
	/**
     * Guarda un inventario (Map<Objeto, Integer>) en el archivo JSON.
     * 
     * @param datosAGuardar Mapa de objetos y sus cantidades.
     */
	public void guardar (Map<Objeto,Integer> datosAGurdar) {
		
		List<InventarioSerializable> objetosJSON = new ArrayList<>();
		for (Map.Entry<Objeto, Integer> obj : datosAGurdar.entrySet()) {
			objetosJSON.add(new InventarioSerializable(obj.getKey(),obj.getValue()));
		}
		super.guardarJSON(objetosJSON);
		System.out.println("Inventario guardadas en: " + super.getRutaArchivo());
	}

}
