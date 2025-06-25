package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HistorialDeCrafteo {
    private List<RegistroCrafteo> registros;

    public HistorialDeCrafteo() {
        this.registros = new ArrayList<>();
    }

    public void agregarRegistro(Objeto objeto, int cantidad, int tiempoTotal) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (tiempoTotal <= 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo o nulo");
        }
        
        registros.add(new RegistroCrafteo(objeto, cantidad, tiempoTotal));
    }

    public void limpiarRegistros() {
        registros.clear();
        RegistroCrafteo.reiniciarContador(); 
    }

    public List<RegistroCrafteo> getRegistros() {
        return new ArrayList<>(registros); // Devolver una copia
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("=== Historial de Crafteos ===\n");
        for (RegistroCrafteo registro : registros) {
            sb.append(registro.toString()).append("\n"); 
        }
        sb.append("=============================\n");
        return sb.toString();
    }

    public List<RegistroCrafteo> buscarCrafteosPorNombre(Object objeto) {
        List<RegistroCrafteo> resultados = new ArrayList<>();
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().equals(objeto)) {
                resultados.add(registro);
            }
        }
        return resultados;
    }

    public RegistroCrafteo buscarPrimerCrafteo(Object objeto) {
    	//el primero esta al ultimo de la lista
    	RegistroCrafteo ultimo = null;
        for (int i = registros.size() - 1; i >= 0; i--) {
        	RegistroCrafteo registro = registros.get(i);
            if (registro.getObjetoCrafteado().equals(objeto)) {
                ultimo = registro;
            }
        }
        return ultimo;
    }

    public RegistroCrafteo buscarUltimoCrafteo(Object objeto) {
    	//el ultimo crafteo es el primero de la lista
    	RegistroCrafteo primero = null;
    	for (int i = 0; i < registros.size(); i++) {
    		RegistroCrafteo registro = registros.get(i);
            if (registro.getObjetoCrafteado().equals(objeto)) {
            	primero = registro;
            }
        }
        return primero;
        
    }

    public int getCantidadTotalCrafteada(Object objeto) {
        int total = 0;
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().equals(objeto)) {
                total += registro.getCantidadCrafteada();
            }
        }
        return total;
    }
}
