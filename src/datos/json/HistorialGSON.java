package datos.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import modelo.RegistroCrafteo;

public class HistorialGSON extends ManejadorGSON<List<RegistroCrafteo>> {

	public HistorialGSON(String rutaArchivo) {
		super(rutaArchivo);
		datos = new ArrayList<>();
		listType =  new TypeToken<List<RegistroCrafteo>>() {}.getType();
	}
	
	public void guardar (List<RegistroCrafteo> datosAGuardar) {
		super.guardarJSON(datosAGuardar);
	}
	

}
