package modelo;

public class ObjetoIntermedio extends Objeto {

    public ObjetoIntermedio(String nombre) {
        super(nombre);
    }

    public boolean esBasico() {
        return false;
    }

}
