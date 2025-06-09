package modelo;


public class ObjetoBasico extends Objeto {
    public ObjetoBasico(String nombre) {
        super(nombre);
    }

    public boolean esBasico() {
        return true;
    }
}
