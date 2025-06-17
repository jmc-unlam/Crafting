package modelo;

import java.text.Normalizer;
import java.util.Objects;

public abstract class Objeto {
    private String nombre;
    
    private String normalizar(String texto) {
    	if (texto == null || texto.isEmpty()) {
    		throw new IllegalArgumentException("El nombre esta vacio");
        }

        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD)
                                      .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        normalized = normalized.toLowerCase();
        normalized = normalized.trim().replaceAll("\\s+", " ");
        normalized = normalized.replaceAll("[^a-z0-9 ]", "");

        return normalized;
    }

    public Objeto(String nombre) {
        this.nombre = normalizar(nombre);
    }

    public String getNombre() {
        return nombre;
    }
    
    public abstract boolean esBasico();
    
    public abstract boolean esApilable();
    
    public void activar(Recetario recetario) {};
    
    public void desactivar(Recetario recetario) {};
    
	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Objeto)) {
			return false;
		}
		Objeto other = (Objeto) obj;
		return Objects.equals(nombre, other.nombre);
	}
}