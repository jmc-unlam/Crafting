package main;

public class Config {
	//Rutas de inicio de datos.
	public static final String RUTA_INICIO_INVENTARIO = "ini/inventarioInicial.json";
	public static final String RUTA_INICIO_RECETARIO = "ini/recetarioInicial.json";
	
	//Ruta donde se guarda el inventario.
	public static final String RUTA_FIN_INVENTARIO = "res/inventario_salida.json";
	//Rutas Prolog.
	public static final String PROLOG_DIR = "src/prolog/";
	public static final String RUTA_PROLOG_CONFIG = PROLOG_DIR + "config.pl";
	public static final String RUTA_PROLOG_INVENTARIO = PROLOG_DIR + "inventario.pl";
	public static final String RUTA_PROLOG_RECETAS = PROLOG_DIR + "recetas.pl";
	public static final String RUTA_PROLOG_LOGICA = PROLOG_DIR + "logica.pl";
	
	//mesa d fundicion
	public static final String RECETA_FUNDIDOR = "res/fundicion/recetaFundidor.json";
	public static final String INVENTARIO_FUNDIDOR = "res/fundicion/inventarioFundidor.json";
	public static final String INVENTARIO_FUNDICION = "res/fundicion/inventarioFundicion.json";
	public static final String RECETAS_FUNDICION = "res/fundicion/recetasFundicion.json";
}