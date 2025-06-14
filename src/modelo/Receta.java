package modelo;

import java.util.HashMap;
import java.util.Map;

public class Receta {
	private ObjetoIntermedio objetoProducido;
    private Map<Objeto, Integer> ingredientes;
    private int cantidadProducida;
    private int tiempoBase;

    public Receta(ObjetoIntermedio objetoProducido, Map<Objeto, Integer> ingredientes, int cantidadProducida,
			int tiempoBase) {
		this.objetoProducido = objetoProducido;
		this.ingredientes = ingredientes;
		this.cantidadProducida = cantidadProducida;
		this.tiempoBase = tiempoBase;
	}

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public int getTiempoBase() {
        return tiempoBase;
    }
    
	public Objeto getObjetoProducido() {
		return objetoProducido;
	}

	public int calcularTiempoTotal(Recetario recetario) {
	    int tiempoTotal = tiempoBase;
	    for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
	        Objeto ingrediente = entry.getKey();
	        int cantidadIngrediente = entry.getValue();
	        if (!ingrediente.esBasico()) {
	            Receta recetaIngrediente = recetario.buscarReceta(ingrediente);
	            int subTiempo = cantidadIngrediente * recetaIngrediente.calcularTiempoTotal(recetario);
	            tiempoTotal += subTiempo / recetaIngrediente.getCantidadProducida();
	        }
	    }
	    return tiempoTotal;
	}
    
    public Map<Objeto, Integer> getIngredientes() {
        return new HashMap<>(ingredientes);
    }
    
	public Map<Objeto, Integer> getIngredientesBasicos(Recetario recetario) {
		Map<Objeto, Integer> basicos = new HashMap<>();

	    for (Map.Entry<Objeto, Integer> elemento : this.ingredientes.entrySet()) {
	        Objeto ingrediente = elemento.getKey();
	        int cantidadRequerida = elemento.getValue();

	        if (ingrediente.esBasico()) {
	            // Si es básico, lo agregamos directamente
	            basicos.merge(ingrediente, cantidadRequerida, Integer::sum);
	        } else {
	            // Si es intermedio, buscamos su receta y descomponemos recursivamente
	        	Receta subReceta = recetario.buscarReceta(ingrediente);
	        	Map<Objeto, Integer> subIngredientesBasicos = subReceta.getIngredientesBasicos(recetario);

                // Multiplicamos por la cantidad requerida y fusionamos
                for (Map.Entry<Objeto, Integer> subElemento : subIngredientesBasicos.entrySet()) {
                    basicos.merge(
                        subElemento.getKey(),
                        subElemento.getValue() * cantidadRequerida,
                        Integer::sum
                    );
                }
	        }
	    }
	    return basicos;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Objeto producido: ").append(objetoProducido.toString()).append("\n");
        sb.append("Cantidad producida: ").append(cantidadProducida).append("\n");
        sb.append("Tiempo de crafteo: ").append(tiempoBase).append("\n");
        sb.append("Ingredientes:\n");

        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            Objeto obj = entry.getKey();
            int cantidad = entry.getValue();
            sb.append("    - ").append(obj.toString()).append(" x ").append(cantidad).append("\n");
        }

        return sb.toString();
    }
}
