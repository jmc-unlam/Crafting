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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Objeto objeto = (Objeto) o;
		return nombre.equals(objeto.nombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

}