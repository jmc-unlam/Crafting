package modelo;

import java.util.Map;

public class RegistroCrafteo {
	// Campo estático que cuenta cuántos registros se han creado
	private static int contadorTurnos = 1;

	private Objeto objetoCrafteado;
	private int cantidadCrafteada;
	private int turno; // Número de turno de crafteo
	private final int tiempoTotal;
	private Map<Objeto, Integer> ingredientesUsados;

	public RegistroCrafteo(Objeto objetoCrafteado, int cantidadCrafteada, int tiempoTotal,
			Map<Objeto, Integer> ingredientesUsados) {
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
		this.ingredientesUsados = ingredientesUsados;
	}

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

	public Objeto getObjetoCrafteado() {
		return objetoCrafteado;
	}

	public int getCantidadCrafteada() {
		return cantidadCrafteada;
	}

	public int getTurno() {
		return turno;
	}

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Turno:").append(turno).append("\n");
		sb.append("\t").append("objeto creado->").append(objetoCrafteado).append("\n");
		sb.append("\t").append("cantidad creada: ").append(cantidadCrafteada).append("\n");
		sb.append("\t").append("tiempo total: ").append(tiempoTotal).append("\n");

		if (ingredientesUsados != null) {
			for (Map.Entry<Objeto, Integer> entry : ingredientesUsados.entrySet()) {
				Objeto ingrediente = entry.getKey();
				Integer cantidad = entry.getValue();
				sb.append("\t").append("Ingrediente usado: ").append(ingrediente).append(" - Cantidad: ")
						.append(cantidad).append("\n");
			}
		}
		return sb.toString();
	}

	public static void reiniciarContador() {
		contadorTurnos = 1;
	}
}
