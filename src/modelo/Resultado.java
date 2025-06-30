package modelo;

public class Resultado {
	private int cantidadCrafteable=0;
	private int tiempo=0;
	
	public Resultado(int cantidadCrafteable, int tiempo) {
		super();
		this.cantidadCrafteable = cantidadCrafteable;
		this.tiempo = tiempo;
	}

	public int getCantidadCrafteable() {
		return cantidadCrafteable;
	}

	public int getTiempo() {
		return tiempo;
	}
	
}
