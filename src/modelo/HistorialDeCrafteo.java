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
        if (tiempoTotal < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo");
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

    public List<RegistroCrafteo> buscarPorNombre(String nombreObjeto) {
        List<RegistroCrafteo> resultados = new ArrayList<>();
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                resultados.add(registro);
            }
        }
        return resultados;
    }

    public RegistroCrafteo getPrimerCrafteo(String nombreObjeto) {
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                return registro;
            }
        }
        return null;
    }

    public RegistroCrafteo getUltimoCrafteo(String nombreObjeto) {
        RegistroCrafteo ultimo = null;
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                ultimo = registro;
            }
        }
        return ultimo;
    }

    public int getCantidadTotalCrafteada(String nombreObjeto) {
        int total = 0;
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                total += registro.getCantidadCrafteada();
            }
        }
        return total;
    }
}
