package modelo;

import java.util.Objects;

public abstract class Objeto {
    private String nombre;

    public Objeto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public abstract boolean esBasico();

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