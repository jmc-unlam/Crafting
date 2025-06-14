package datos.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Objeto;
import modelo.ObjetoIntermedio;
import modelo.Receta;

public class RecetaSerializable {

	private Objeto objetoProducido;
	private List<InventarioSerializable> ingredientes;
	private int cantidadProducida;
	private int tiempoBase;

	public RecetaSerializable() {}

	public RecetaSerializable(Objeto objetoProducido, List<InventarioSerializable> ingredientes, int cantidadProducida,
			int tiempoBase) {
		this.objetoProducido = objetoProducido;
		this.ingredientes = ingredientes;
		this.cantidadProducida = cantidadProducida;
		this.tiempoBase = tiempoBase;
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
	
	public Receta toReceta() {
        Map<Objeto, Integer> ingredientesMap = new HashMap<>();
        if (this.ingredientes != null) {
            for (InventarioSerializable ingJson : this.ingredientes) {
                ingredientesMap.put(ingJson.getObjeto(), ingJson.getCantidad());
            }
        }
        // Aquí necesitas un casting seguro si tu clase de dominio Receta espera ObjetoIntermedio
        // Asegúrate de que objetoProducido sea de hecho un ObjetoIntermedio
        if (this.objetoProducido.esBasico()) {
            // Manejar error o lanzar excepción si el tipo no es el esperado
            throw new IllegalStateException("El objeto producido no es un ObjetoIntermedio como se esperaba en Receta.");
        }
        return new Receta((ObjetoIntermedio) this.objetoProducido, ingredientesMap, this.cantidadProducida, this.tiempoBase);
    }

    // Método estático para convertir Receta de dominio a RecetaJSON (para serialización)
    public static RecetaSerializable fromReceta(Receta receta) {
        List<InventarioSerializable> ingredientesJsonList = new ArrayList<>();
        for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
            ingredientesJsonList.add(new InventarioSerializable(entry.getKey(), entry.getValue()));
        }
        return new RecetaSerializable(
            receta.getObjetoProducido(),
            ingredientesJsonList,
            receta.getCantidadProducida(),
            receta.getTiempoBase()
        );
    }
}
