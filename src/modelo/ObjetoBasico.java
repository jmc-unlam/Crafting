package modelo;


public class ObjetoBasico extends Objeto {
    public ObjetoBasico(String nombre) {
        super(nombre);
    }

    public boolean esBasico() {
        return true;
    }

    @Override
    public String toString() {
        return "ObjetoBásico: " + getNombre();
    }
    
}
