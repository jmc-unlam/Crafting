package datos.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory; // Importar la clase

import modelo.MesaDeHierro;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class ManejadorGSON<T> {
	private final String rutaArchivo;
	protected Gson gson;
	protected T datos;
	protected Type listType;
	private RecetaTypeAdapter adaptadorReceta;

	public ManejadorGSON(String rutaArchivo) {
    	this.rutaArchivo = rutaArchivo;
    	this.adaptadorReceta = new RecetaTypeAdapter();
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Objeto.class, "tipo")
                        .registerSubtype(ObjetoBasico.class, "basico")
                        .registerSubtype(ObjetoIntermedio.class, "intermedio")
                        .registerSubtype(MesaDeHierro.class, "mesa"))
                .registerTypeAdapter(Receta.class, adaptadorReceta)
                .setPrettyPrinting()
                .create();
        this.adaptadorReceta.setGson(this.gson);
    }
    
    public T cargarJSON() {
        try (FileReader reader = new FileReader(rutaArchivo)) {
        	File file = new File(rutaArchivo);
            if (!file.exists()) {
                System.out.println("Archivo no encontrado: " + rutaArchivo + ". Se carga una lista vacía.");
            }
            //Type listType = new TypeToken<T>() {}.getType();
            datos = gson.fromJson(reader, listType);
            System.out.println("Recetas leidas desde: ./" + rutaArchivo);
        } catch (IOException e) {
        	//si esto pasa devolvera una lista vacia.
            //System.err.println("Archivo de recetas no encontrado o error de lectura: " + rutaArchivo + ". Se carga una lista vacía.");
        }
        return this.datos;
    }
    
    public void guardarJSON(T datosAGuardar) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(datosAGuardar, writer);
            System.out.println("Recetas guardadas en: ./" + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar recetas en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}