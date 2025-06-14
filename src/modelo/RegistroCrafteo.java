package modelo;

public class RegistroCrafteo {
    // Campo estático que cuenta cuántos registros se han creado
    private static int contadorTurnos = 1;

    private Objeto objetoCrafteado;
    private int cantidadCrafteada;
    private int turno; // Número de turno de crafteo
    private final int tiempoTotal;

    // Constructor
    public RegistroCrafteo(Objeto objetoCrafteado, int cantidadCrafteada, int tiempoTotal) {
        if (objetoCrafteado == null) {
            throw new IllegalArgumentException("El objeto crafteado no puede ser nulo");
        }
        if (cantidadCrafteada <= 0) {
            throw new IllegalArgumentException("La cantidad crafteada debe ser positiva");
        }
        if (tiempoTotal < 0) {
            throw new IllegalArgumentException("El tiempo total no puede ser negativo");
        }
        
        this.turno = contadorTurnos++;
        this.objetoCrafteado = objetoCrafteado;
        this.cantidadCrafteada = cantidadCrafteada;
        this.tiempoTotal = tiempoTotal;
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
    
    public int getTiempoTotal() {
        return tiempoTotal;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Turno ").append(turno)
    	  .append(" - ").append(objetoCrafteado.toString())
    	  .append(": ").append(cantidadCrafteada)
    	  .append(" unidades (Tiempo: ").append(tiempoTotal).append(")");
    	return sb.toString();
    }

	public static void reiniciarContador() {
		contadorTurnos = 1; 
		
	}
}
