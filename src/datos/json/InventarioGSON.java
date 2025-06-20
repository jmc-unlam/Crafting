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
		Map<Objeto, Integer> objetos = new HashMap<>();
        for (InventarioSerializable entry : datos) {
            objetos.put(entry.getObjeto(), entry.getCantidad());
        }
        return objetos;
	}
	
	public void guardar (Map<Objeto,Integer> datosAGurdar) {
		
		List<InventarioSerializable> objetosJSON = new ArrayList<>();
		for (Map.Entry<Objeto, Integer> obj : datosAGurdar.entrySet()) {
			objetosJSON.add(new InventarioSerializable(obj.getKey(),obj.getValue()));
		}
		super.guardarJSON(objetosJSON);
	}

}
