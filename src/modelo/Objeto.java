package modelo;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Objeto {
    private String nombre;
    
    private String normalizar(String texto) {
    	if (texto == null || texto.isEmpty()) {
    		throw new IllegalArgumentException("El nombre esta vacio");
        }

    	String normalized = texto.toLowerCase();
        //quita todas letras que no son a-z, 0-9, espacios y la ñ
        normalized = normalized.replaceAll("[^a-zñ0-9 ]", "");
    	
    	//quita los espacios repetidos
        normalized = normalized.replaceAll("\\s+", " ");
    	//descomposicion canonica
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        //recompongo solo la ñ
        normalized = normalized.replaceAll("n\\u0303", "ñ");
        //quita los acentos y simbolos raros 
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
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
    
    public List<Receta> listaDeRecetasPropias(){
    	return Collections.emptyList();
    }
    
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