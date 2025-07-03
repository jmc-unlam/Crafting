package datos.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import modelo.Objeto;

public class InventarioGSON extends ManejadorGSON<List<InventarioSerializable>> {

	public InventarioGSON(String rutaArchivo) {
		super(rutaArchivo);
		datos = new ArrayList<>();
		listType =  new TypeToken<List<InventarioSerializable>>() {}.getType();
	}

	public Map<Objeto,Integer> cargar() {
		super.cargarJSON();
		System.out.println("Inventario leidas desde: " + super.getRutaArchivo());
		Map<Objeto, Integer> objetos = new HashMap<>();
		try {
	        for (InventarioSerializable entry : datos) {
	            objetos.put(entry.getObjeto(), entry.getCantidad());
	        }
		} catch (NullPointerException e) {
			//si esto pasa devolvera una lista vacia.
        	//System.err.println("Inventario esta vacio sin nada: " + rutaArchivo + ". Se carga una lista vacía.");
		}
		
        return objetos;
	}
	
	public void guardar (Map<Objeto,Integer> datosAGurdar) {
		
		List<InventarioSerializable> objetosJSON = new ArrayList<>();
		for (Map.Entry<Objeto, Integer> obj : datosAGurdar.entrySet()) {
			objetosJSON.add(new InventarioSerializable(obj.getKey(),obj.getValue()));
		}
		super.guardarJSON(objetosJSON);
		System.out.println("Inventario guardadas en: " + super.getRutaArchivo());
	}

}
