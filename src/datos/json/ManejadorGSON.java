package datos.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory; // Importar la clase

import modelo.MesaDeFlechas;
import modelo.MesaDeFundicion;
import modelo.MesaDeGenerica;
import modelo.MesaDePiedra;
import modelo.MesaDeTrabajo;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Clase genérica para manejar la serialización y deserialización con GSON.
 * Permite cargar y guardar colecciones de datos sin acoplar el modelo a la capa de persistencia.
 * 
 * @author Jorge
 * @version 1.0
 * @param <T> Tipo de datos a serializar.
 */
public abstract class ManejadorGSON<T> {
	private final String rutaArchivo;
	protected Gson gson;
	protected T datos;
	protected Type listType;

	/**
     * Constructor que inicializa el manejador con la ruta del archivo.
     * Registra las clases que son utilizadas en la des/serializacion
     * 
     * @param rutaArchivo Ruta del archivo JSON a usar.
     */
	public ManejadorGSON(String rutaArchivo) {
    	this.rutaArchivo = rutaArchivo;
        this.gson = new GsonBuilder()
        		//Esto es para des/serializar el campo Objeto [InventarioSerializable/RecetaSerializable] 
                .registerTypeAdapterFactory(
                		RuntimeTypeAdapterFactory.of(Objeto.class, "tipo")
                        .registerSubtype(ObjetoBasico.class, "basico")
                        .registerSubtype(ObjetoIntermedio.class, "intermedio")
                        .registerSubtype(MesaDeFundicion.class, "mesa de fundicion")
    	                .registerSubtype(MesaDePiedra.class, "mesa de piedra")
    	                .registerSubtype(MesaDeFlechas.class, "mesa de flechas")
    	                .registerSubtype(MesaDeGenerica.class, "mesa de generica")
                        )
                //Este es para des/serializar el campo MesaDeTrabajo [RecetaSerializable]
                .registerTypeAdapterFactory(
                    	RuntimeTypeAdapterFactory.of(MesaDeTrabajo.class, "tipo")
    	                .registerSubtype(MesaDeFundicion.class, "mesa de fundicion") 
    	                .registerSubtype(MesaDePiedra.class, "mesa de piedra")
    	                .registerSubtype(MesaDeFlechas.class, "mesa de flechas")
    	                .registerSubtype(MesaDeGenerica.class, "mesa de generica")
    	            )
                .setPrettyPrinting()
                .create();
    }
    
	/**
     * Carga datos desde un archivo JSON.
     * 
     * @return Colección de datos cargada.
     */
    public T cargarJSON() {
        try (FileReader reader = new FileReader(rutaArchivo)) {
        	File file = new File(rutaArchivo);
            if (!file.exists()) {
                System.out.println("Archivo no encontrado: " + rutaArchivo + ". Se carga una lista vacía.");
            }
            //Type listType = new TypeToken<T>() {}.getType();
            datos = gson.fromJson(reader, listType);
        } catch (IOException e) {
        	//si esto pasa devolvera una lista vacia.
            //System.err.println("Archivo de recetas no encontrado o error de lectura: " + rutaArchivo + ". Se carga una lista vacía.");
        }
        catch (NullPointerException e) {
        	//si esto pasa devolvera una lista vacia.
        	//System.err.println("Archivo esta vacio sin nada: " + rutaArchivo + ". Se carga una lista vacía.");
        }
        return this.datos;
    }
    
    /**
     * Guarda datos en un archivo JSON.
     * 
     * @param datosAGuardar datos Datos a guardar.
     */
    public void guardarJSON(T datosAGuardar) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(datosAGuardar, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar recetas en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la ruta del archivo usado para persistencia.
     * 
     * @return Ruta del archivo.
     */
	public String getRutaArchivo() {
		return new String(rutaArchivo);
	}
}