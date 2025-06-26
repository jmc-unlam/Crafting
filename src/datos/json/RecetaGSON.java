package datos.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import modelo.Receta;

public class RecetaGSON extends ManejadorGSON<List<RecetaSerializable>> {

	public RecetaGSON(String rutaArchivo) {
		super(rutaArchivo);
		datos =  new ArrayList<>();
		listType =  new TypeToken<List<RecetaSerializable>>() {}.getType();
	}

	public List<Receta> cargar() {
		super.cargarJSON();
		System.out.println("Receta leidas desde: " + super.getRutaArchivo());
		List<Receta> recetas = new ArrayList<>();
        for (RecetaSerializable recetaJson : datos) {
        	Receta recetaConvertida = recetaJson.toReceta();
            recetas.add(recetaConvertida);
        }
		return recetas;
	}
	
	public void guardar(List<Receta> recetas) {
		List<RecetaSerializable> recetasJSON = new ArrayList<>();
	    for (Receta receta : recetas) {
	    	recetasJSON.add(RecetaSerializable.fromReceta(receta));
	    }
	    super.guardarJSON(recetasJSON);
	    System.out.println("Receta guardadas en: " + super.getRutaArchivo());
	}

}
