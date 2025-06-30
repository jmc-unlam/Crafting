package main;

public class Config {
	//Rutas de inicio de datos.
	public static final String RUTA_INICIO_INVENTARIO = "ini/inventarioInicial.json";
	public static final String RUTA_INICIO_RECETARIO = "ini/recetarioInicial.json";
	
	//Ruta donde se guarda el inventario.
	public static final String RUTA_FIN_INVENTARIO = "res/inventario_salida.json";
	
	//Ruta de las Recetas de las mesas.
	public static final String RECETAS_DE_MESAS_DIR = "src/mesasRecetarios/";
	
	//Rutas Prolog.
	public static final String PROLOG_DIR = "src/prolog/";
	public static final String RUTA_PROLOG_CONFIG = PROLOG_DIR + "config.pl";
	public static final String RUTA_PROLOG_INVENTARIO = PROLOG_DIR + "inventario.pl";
	public static final String RUTA_PROLOG_RECETAS = PROLOG_DIR + "recetas.pl";
	public static final String RUTA_PROLOG_LOGICA = PROLOG_DIR + "logica.pl";
	
	//mesa d fundicion -Escenario 02
	public static final String RECETA_FUNDIDOR = "res/fundicion/recetaFundidor.json";
	public static final String INVENTARIO_FUNDIDOR = "res/fundicion/inventarioFundidor.json";
	public static final String INVENTARIO_FUNDICION = "res/fundicion/inventarioFundicion.json";
	public static final String RECETAS_FUNDICION = "res/fundicion/recetasFundicion.json";
	public static final String INVENTARIO_FUNDICION_FINAL = "res/fundicion/inventarioFinal.json"; //Alorda prueba inventario final con mesa
	//RUTA de los archivos de los Escenarios.
	public static final String ESCENARIOSFILES_DIR = "src/EscenariosFiles/";
	//Escenario 03.
	public static final String ESCE03_RUTA_INICIO_INVENTARIO = ESCENARIOSFILES_DIR + "ESCE03inventarioInicial.json";
	public static final String ESCE03_RUTA_INICIO_RECETARIO =  ESCENARIOSFILES_DIR + "ESCE03recetarioInicial.json";
	public static final String ESCE03_RUTA_FINAL_INVENTARIO =  ESCENARIOSFILES_DIR + "ESCE03inventarioFinal.json";
	
	//Escenario 04.
	public static final String ESCE04_RUTA_INICIO_INVENTARIO = ESCENARIOSFILES_DIR + "ESCE04inventarioInicial.json";
	public static final String ESCE04_RUTA_INICIO_RECETARIO =  ESCENARIOSFILES_DIR + "ESCE04recetarioInicial.json";
	public static final String ESCE04_RUTA_FINAL_INVENTARIO = ESCENARIOSFILES_DIR + "ESCE04inventarioFinal.json";
}