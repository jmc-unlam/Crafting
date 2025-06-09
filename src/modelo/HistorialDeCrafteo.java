package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HistorialDeCrafteo {
    private List<RegistroCrafteo> registros;

    // Constructor
    public HistorialDeCrafteo() {
        this.registros = new ArrayList<>();
    }

    // Agregar un registro de crafteo
    public void agregarRegistro(Objeto objeto, int cantidad) {
        RegistroCrafteo registro = new RegistroCrafteo(objeto, cantidad);
        registros.add(registro);
    }

    // Limpiar el historial de crafteos
    public void limpiarRegistros() {
        registros.clear();
        RegistroCrafteo.reiniciarContador(); 
    }

    // Obtener todos los registros
    public List<RegistroCrafteo> getRegistros() {
        return new ArrayList<>(registros); // Devolver una copia
    }

    // Mostrar todo el historial de crafteos
    public void mostrarHistorial() {
        System.out.println("=== Historial de Crafteos ===");
        for (RegistroCrafteo registro : registros) {
            System.out.println(registro);
        }
        System.out.println("=============================");
    }

    // Buscar crafteos por nombre de objeto
    public List<RegistroCrafteo> buscarPorNombre(String nombreObjeto) {
        List<RegistroCrafteo> resultados = new ArrayList<>();
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                resultados.add(registro);
            }
        }
        return resultados;
    }

    // Obtener el primer crafteo de un objeto
    public RegistroCrafteo getPrimerCrafteo(String nombreObjeto) {
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                return registro;
            }
        }
        return null;
    }

    // Obtener el último crafteo de un objeto
    public RegistroCrafteo getUltimoCrafteo(String nombreObjeto) {
        RegistroCrafteo ultimo = null;
        for (RegistroCrafteo registro : registros) {
            if (registro.getObjetoCrafteado().getNombre().equalsIgnoreCase(nombreObjeto)) {
                ultimo = registro;
            }
        }
        return ultimo;
    }

    // Obtener cantidad total crafteada de un objeto
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
