package datos.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.MesaDeTrabajo;
import modelo.Objeto;
import modelo.Receta;

/**
 * Clase Data Transfer Object (DTO) que representa una receta para la serialización.
 * Se usa para convertir recetas del modelo a una estructura serializable y viceversa.
 * 
 * @author Jorge
 * @version 1.0
 */
public class RecetaSerializable {

	private Objeto objetoProducido;
	private List<InventarioSerializable> ingredientes;
	private int cantidadProducida;
	private int tiempoBase;
	private MesaDeTrabajo mesaRequerida;

	public RecetaSerializable() {}
	
	public RecetaSerializable(Objeto objetoProducido, List<InventarioSerializable> ingredientes, int cantidadProducida,
			int tiempoBase, MesaDeTrabajo mesaRequerida) {
		this.objetoProducido = objetoProducido;
		this.ingredientes = ingredientes;
		this.cantidadProducida = cantidadProducida;
		this.tiempoBase = tiempoBase;
		this.mesaRequerida = mesaRequerida;
	}

	public Objeto getObjetoProducido() {
		return objetoProducido;
	}

	public void setObjetoProducido(Objeto objetoProducido) {
		this.objetoProducido = objetoProducido;
	}

	public List<InventarioSerializable> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<InventarioSerializable> ingredientes) {
		this.ingredientes = ingredientes;
	}

	public int getCantidadProducida() {
		return cantidadProducida;
	}

	public void setCantidadProducida(int cantidadProducida) {
		this.cantidadProducida = cantidadProducida;
	}

	public int getTiempoBase() {
		return tiempoBase;
	}

	public void setTiempoBase(int tiempoBase) {
		this.tiempoBase = tiempoBase;
	}
	
	public MesaDeTrabajo getMesaRequerida() {
		return mesaRequerida;
	}

	public void setMesaRequerida(MesaDeTrabajo mesaRequerida) {
		this.mesaRequerida = mesaRequerida;
	}

	public Receta toReceta() {
        Map<Objeto, Integer> ingredientesMap = new HashMap<>();
        if (this.ingredientes != null) {
            for (InventarioSerializable ingJson : this.ingredientes) {
                ingredientesMap.put(ingJson.getObjeto(), ingJson.getCantidad());
            }
        }
        if (this.objetoProducido.esBasico()) {
            throw new IllegalStateException("El objeto producido no es crafteable como se esperaba en Receta.");
        }
        return new Receta(this.objetoProducido, ingredientesMap, this.cantidadProducida, this.tiempoBase, this.mesaRequerida);
    }

    // Método estático para convertir Receta a RecetaSerializable
    public static RecetaSerializable fromReceta(Receta receta) {
        List<InventarioSerializable> ingredientesJsonList = new ArrayList<>();
        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
            ingredientesJsonList.add(new InventarioSerializable(entry.getKey(), entry.getValue()));
        }
        return new RecetaSerializable(
            receta.getObjetoProducido(),
            ingredientesJsonList,
            receta.getCantidadProducida(),
            receta.getTiempoBase(),
            receta.getMesaRequerida()
        );
    }
}
