package modelo;

public abstract class Objeto {
    private String nombre;

    public Objeto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public abstract boolean esBasico();

}