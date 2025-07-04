package datos.json;

import com.google.gson.annotations.SerializedName;

import modelo.Objeto;

/**
 * Clase Data Transfer Object (DTO) que representa un objeto y su cantidad en el inventario.
 * Se utiliza para la serialización/deserialización con GSON, 
 * garantizando que el modelo no tenga dependencia directa con la capa de persistencia.
 * 
 * @author Jorge
 * @version 1.0
 */
public class InventarioSerializable {
	@SerializedName("objeto")
    private Objeto objeto;
	private int cantidad;
	
    public InventarioSerializable() {}
    
	public InventarioSerializable(Objeto objeto, int cantidad) {
		this.objeto = objeto;
		this.cantidad = cantidad;
	}
	public Objeto getObjeto() {
		return objeto;
	}
	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public Objeto toObjeto() {
		return objeto;
	}
}
