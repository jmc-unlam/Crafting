package modelo;

public class RegistroCrafteo {
    // Campo estático que cuenta cuántos registros se han creado
    private static int contadorTurnos = 1;

    private Objeto objetoCrafteado;
    private int cantidadCrafteada;
    private int turno; // Número de turno de crafteo

    // Constructor
    public RegistroCrafteo(Objeto objetoCrafteado, int cantidadCrafteada) {
        this.objetoCrafteado = objetoCrafteado;
        this.cantidadCrafteada = cantidadCrafteada;
        this.turno = contadorTurnos++;
    }

    // Getter para el objeto crafteado
    public Objeto getObjetoCrafteado() {
        return objetoCrafteado;
    }

    // Getter para la cantidad crafteada
    public int getCantidadCrafteada() {
        return cantidadCrafteada;
    }

    // Getter para el turno
    public int getTurno() {
        return turno;
    }

    @Override
    public String toString() {
        return "RegistroCrafteo{" +
                "turno=" + turno +
                ", objetoCrafteado=" + objetoCrafteado.getNombre() +
                ", cantidadCrafteada=" + cantidadCrafteada +
                '}';
    }

	public static void reiniciarContador() {
		contadorTurnos = 1; 
		
	}
}
