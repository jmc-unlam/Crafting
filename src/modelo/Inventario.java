package modelo;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private Map<Objeto, Integer> objetos;

    public Inventario() {
        this.objetos = new HashMap<>();
    }

    public void agregarObjeto(Objeto objeto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        int cantidadActual = objetos.getOrDefault(objeto, -1);
        if (cantidadActual <= 0) {
        	objetos.remove(objeto);
            throw new IllegalStateException("El objeto tiene una cantidad negativa:" + objeto.getNombre());
        }
        objetos.merge(objeto, cantidad, Integer::sum);
    }

    public void removerObjeto(Objeto objeto, int cantidad) {
        int cantidadActual = objetos.getOrDefault(objeto, 0);
        if (cantidadActual >= cantidad) {
            objetos.put(objeto, cantidadActual - cantidad);
        } else {
            throw new IllegalArgumentException("No hay suficiente cantidad del objeto en el inventario.");
        }
    }

    public Map<Objeto, Integer> getObjetos() {
        return new HashMap<>(objetos); 
    }

}
