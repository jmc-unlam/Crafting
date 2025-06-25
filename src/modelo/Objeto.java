package modelo;

import java.text.Normalizer;
import java.util.Objects;

public abstract class Objeto {
    private String nombre;
    
    private String normalizar(String texto) {
    	if (texto == null || texto.isEmpty()) {
    		throw new IllegalArgumentException("El nombre esta vacio");
        }

    	//normalizado en forma canonica
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        //quita los acentos y simbolos raros 
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.toLowerCase();
        //quita todas letras que no son a-z y 0-9
        normalized = normalized.replaceAll("[^a-z0-9 ]", "");
        //quita los espacios repetidos
        normalized = normalized.replaceAll("\\s+", " ");
        //quita los espacios entre palabras de longitud 1
        normalized = normalized.replaceAll("(?<=\\b\\w)\\s(?=\\w\\b)", "");
        //quita los espacios al principio y al final 
        normalized = normalized.trim();

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