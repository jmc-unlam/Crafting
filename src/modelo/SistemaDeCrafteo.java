package modelo;

import java.util.HashMap;
import java.util.Map;

public class SistemaDeCrafteo {
    private Inventario inventario;
    private Recetario recetario;
    private HistorialDeCrafteo historial;

    public SistemaDeCrafteo(Inventario inventario, Recetario recetario) {
        this.inventario = inventario;
        this.recetario = recetario;
        this.historial = new HistorialDeCrafteo();
    }

    // 1. Mostrar ingredientes necesarios para craftear un objeto (primer nivel)
    public Map<Objeto, Integer> getIngredientesDirectos(Objeto objeto) {
        return objeto.getIngredientes(); 
    }

    // 2. Mostrar todos los ingredientes básicos necesarios para craftear un objeto desde cero
    public Map<Objeto, Integer> getIngredientesBasicosTotales(Objeto objeto) {
        return null;
    };

    // 3. Mostrar qué me falta para craftear un objeto (solo primer nivel)
    public Map<Objeto, Integer> getFaltantesDirectos(Objeto objeto) {
        return null;
    };

    // 4. Mostrar qué me falta para craftear un objeto desde cero (todos niveles, elementos básicos)
    public Map<Objeto, Integer> getFaltantesBasicosTotales(Objeto objeto) {
        return null;
    };

    // 5. Cuántos objetos puedo craftear con el inventario actual
    public int getCantidadCrafteable(Objeto objeto) {
        return 0;
    };

    // 6. Realizar un crafteo y actualizar inventario e historial
    public void Craftear(Objeto objeto) {
    	if (sePuedeCrear(objeto)) {
            throw new IllegalStateException("No hay suficientes ingredientes en el inventario para craftear.");
        }

        // Remover los ingredientes del inventario
        for (Map.Entry<Objeto, Integer> entry : objeto.getReceta().getIngredientes().entrySet()) {
            inventario.removerObjeto(entry.getKey(), entry.getValue());
        }

        // Agregar el objeto resultante al inventario
        inventario.agregarObjeto(objeto.getReceta().getObjetoProducido(), objeto.getReceta().getCantidadProducida());

        // Registrar el crafteo
        historial.agregarRegistro(objeto.getReceta().getObjetoProducido(), objeto.getReceta().getCantidadProducida());
    };

    // 7. Obtener el tiempo total necesario para craftear una cantidad del objeto
    public int getTiempoTotal(Objeto objeto, int cantidad) {
        return 0;
    };

    // 8. Obtener historial de crafteos
    public HistorialDeCrafteo getHistorial() {
		return historial;
        
    };
    
    // Método para verificar si se puede crear el objeto dado un inventario
    public boolean sePuedeCrear(Objeto objeto) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = ingredientesDisponibles.getOrDefault(ingrediente, 0);

            if (cantidadDisponible < cantidadNecesaria) {
                return false;
            }
        }
        return true;
    }
    
    // Mostrar los ingredientes necesarios para crear un objeto
    public void mostrarIngredientes(Objeto objeto) {
        System.out.println("Ingredientes necesarios para crear " + objeto.getNombre() + ":");
        Map<Objeto, Integer> ingredientes = objeto.getIngredientes();
        for (Map.Entry<Objeto, Integer> entry : ingredientes.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
    }

    // Mostrar los ingredientes básicos necesarios para crear un objeto
    public void mostrarIngredientesBasicos(Objeto objeto) {
        System.out.println("Ingredientes básicos necesarios para crear " + objeto.getNombre() + ":");
        Map<Objeto, Integer> ingredientesBasicos = objeto.getIngredientesBasicos();
        for (Map.Entry<Objeto, Integer> entry : ingredientesBasicos.entrySet()) {
            System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue());
        }
    }
    
 // Obtener los ingredientes faltantes para crear un objeto dado un inventario
    public Map<Objeto, Integer> getIngredientesFaltantes(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();
        Map<Objeto, Integer> ingredientesFaltantes = new HashMap<>();

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingrediente);

            if (cantidadNecesaria > cantidadDisponible) {
                ingredientesFaltantes.put(ingrediente, cantidadNecesaria - cantidadDisponible);
            }
        }
        return ingredientesFaltantes;
    }

    // Obtener los ingredientes básicos faltantes para crear un objeto dado un inventario
    public Map<Objeto, Integer> getIngredientesFaltantesBasicos(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesBasicosNecesarios = objeto.getIngredientesBasicos();
        Map<Objeto, Integer> ingredientesDisponibles = inventario.getObjetos();
        Map<Objeto, Integer> ingredientesBasicosFaltantes = new HashMap<>();

        for (Map.Entry<Objeto, Integer> entry : ingredientesBasicosNecesarios.entrySet()) {
            Objeto ingredienteBasico = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingredienteBasico);

            if (cantidadNecesaria > cantidadDisponible) {
                ingredientesBasicosFaltantes.put(ingredienteBasico, cantidadNecesaria - cantidadDisponible);
            }
        }
        return ingredientesBasicosFaltantes;
    }

    // Calcular cuántas veces se puede craftear un objeto dado un inventario
    public int cantidadCrafteable(Objeto objeto, Inventario inventario) {
        Map<Objeto, Integer> ingredientesNecesarios = objeto.getIngredientes();
        int cantidadMinima = Integer.MAX_VALUE;

        for (Map.Entry<Objeto, Integer> entry : ingredientesNecesarios.entrySet()) {
            Objeto ingrediente = entry.getKey();
            int cantidadNecesaria = entry.getValue();
            int cantidadDisponible = inventario.getCantidad(ingrediente);

            int vecesPosibles = cantidadDisponible / cantidadNecesaria;
            if (vecesPosibles < cantidadMinima) {
                cantidadMinima = vecesPosibles;
            }
        }
        return cantidadMinima;
    }

    public Inventario getInventario() {
		return inventario;
	};
    public Recetario getRecetario() {
		return recetario;
	};
	
	//-----------------------------------------------
	//-----------------------------------------------
	public void ingredientesNecesarios(String objeto){
        int i=1;

        //verificar si tiene recetas o es basico antes de operar
        for(Receta rec : recetario.buscarRecetas(objeto)){
            System.out.println("Ingredientes para Receta " + i + ":");
            for(Map.Entry<Objeto, Integer> elem : rec.getIngredientes().entrySet()){
                Objeto ingrediente = elem.getKey();
                int cantRequerida = elem.getValue();

                System.out.println(ingrediente.getNombre() + " x " + cantRequerida);
            }
            System.out.println("\nTiempo de crafteo: " + rec.getTiempoBase() + " minutos\n");
            i++;
        }
    }

    public void ingBasicosNecesarios(String objeto){
        int i=1;
        //verificar si tiene recetas o es basico antes de operar
        for(Receta rec : recetario.buscarRecetas(objeto)){
            System.out.println("Posibles ingredientes basicos para Receta " + i + ":");
            ArrayList<PosibleReceta> posiblesRecetas = rec.getIngredientesBasicos();
            for(PosibleReceta posRec : posiblesRecetas){
                System.out.println(posRec);
                if(posiblesRecetas.size() > 1){System.out.println("\nO\n");}
            }
            System.out.println('\n');
            i++;
        }
    }

    public void ingFaltantesParaCraftear(String objeto, int cantACraftear){
        if(cantACraftear == 0){
            System.out.println("La cantidad a craftear es 0.");
            return;
        }

        ArrayList<Receta> recetasObj;
        recetasObj = recetario.buscarRecetas(objeto);
        if(recetasObj.isEmpty()){
            System.out.println("El objeto no tiene recetas");
            return;
        }

        int i=1;
        boolean hayFaltantes = false;

        for(Receta rec: recetasObj){
            System.out.println("Ingredientes faltantes para Receta " + i + ":");
            for (Map.Entry<Objeto, Integer> entry : rec.getIngredientes().entrySet()) {
                Objeto ing = entry.getKey();
                int cantReq = entry.getValue() * cantACraftear;

                boolean estaEnInventario = inventario.getObjetos().containsKey(ing);

                if (!estaEnInventario || inventario.getObjetos().get(ing) < cantReq) {
                    hayFaltantes = true;
                    System.out.println('-' + ing.getNombre() + " x " + (cantReq - inventario.getObjetos().getOrDefault(ing, 0)));
                }
            }
            if(!hayFaltantes){
                System.out.println("No hay ingredientes faltantes");
            }

            System.out.println();
            if(rec.getMesaRequerida() != null) {
                if (!inventario.getObjetos().containsKey(rec.getMesaRequerida())) {
                    System.out.println("Mesa faltante: " + rec.getMesaRequerida().getNombre());
                }
            }

            System.out.println("Tiempo de crafteo: " + rec.getTiempoBase() * cantACraftear + " minutos\n");

            i++;
        }
    }

    public void ingBasicosFaltantesParaCraftear(String objeto, int cantACraftear){
        if(cantACraftear == 0){
            System.out.println("La cantidad a craftear es 0.");
            return;
        }

        ArrayList<PosibleFaltante> posibleFaltantes = obtenerPosiblesFaltantes(objeto, cantACraftear, inventario);

        if(!posibleFaltantes.isEmpty()) {
            PosibleFaltante menorFaltanteDeObj = null;

            for (PosibleFaltante posFaltanteObj : posibleFaltantes) {
                if (menorFaltanteDeObj == null) {
                    menorFaltanteDeObj = posFaltanteObj;
                } else if (Util.compararPosRecetas(posFaltanteObj.faltantes, menorFaltanteDeObj.faltantes) < 0) {
                    menorFaltanteDeObj = posFaltanteObj;
                }
            }

            if (menorFaltanteDeObj != null) {
                System.out.println("La menor cantidad de ingredientes basicos faltantes para craftear " + cantACraftear + ' ' + objeto +
                        " desde cero, incluyendo el costo de las mesas faltantes, es la siguiente:");
                System.out.println(menorFaltanteDeObj);
            }
        } else{
            System.out.println("El objeto no tiene recetas");
        }
    }

    public void cantidadCrafteable(String objeto){
        int i=0;
        PosibleFaltante menorFaltanteDeObj = obtenerMinimoFaltante(objeto, i+1, inventario);

        if (menorFaltanteDeObj != null) {
            if(!menorFaltanteDeObj.faltantes.getIngredientes().isEmpty()){
                System.out.println("No hay suficientes ingredientes en el inventario para craftear el objeto.");
                return;
            }

            PosibleFaltante maximoCrafteado;

            do{
                maximoCrafteado = menorFaltanteDeObj;

                i++;
                menorFaltanteDeObj = obtenerMinimoFaltante(objeto, i+1, inventario);
            }while(menorFaltanteDeObj.faltantes.getIngredientes().isEmpty());

            System.out.println("Se pueden craftear hasta " + i +
                    ' ' + objeto + " en " + maximoCrafteado.faltantes.getTiempoCrafteo() + " minutos.");
        } else {
            System.out.println("El objeto no tiene recetas.");
        }
    }

    public void craftearObjeto(String objeto, int cantACraftear){
        if(cantACraftear == 0){
            System.out.println("La cantidad a craftear es 0.");
            return;
        }

        PosibleFaltante invDespuesDeCraftear = obtenerMinimoFaltante(objeto, cantACraftear, inventario);

        if (invDespuesDeCraftear != null) {
            if(!invDespuesDeCraftear.faltantes.getIngredientes().isEmpty()){
                System.out.println("No hay suficientes ingredientes en el inventario para craftear el objeto.");
                return;
            }

            invDespuesDeCraftear.estadoDeInv.agregarObjeto(
                    new Objeto(objeto, recetario.buscarRecetas(objeto)), invDespuesDeCraftear.historialActual.getUltimoRegistro().getCantCrafteada());

            //no hacen falta los prints, solo necesitamos la asignacion, pero sirven para testear
            System.out.println("Inventario antes de craftear: ");
            System.out.println(inventario);

            inventario = invDespuesDeCraftear.estadoDeInv;
            historialDeCrafteo.getRegistros().addAll(invDespuesDeCraftear.historialActual.getRegistros());

            //para la consigna solo necesitas este print

            System.out.println("El crafteo tardo " + invDespuesDeCraftear.faltantes.getTiempoCrafteo() + " minutos, con un sobrante de " +
                    (invDespuesDeCraftear.historialActual.getUltimoRegistro().getCantCrafteada() - cantACraftear) + ' ' + objeto + '.');
            System.out.println("Inventario despues de craftear: ");
            System.out.println(inventario);

            System.out.println("Historial de crafteo: ");
            System.out.println(historialDeCrafteo);

        } else {
            System.out.println("El objeto no tiene recetas.");
        }
    }

    //Lo unico que no puede hacer esta funcion es predecir, al elegir una receta por sobre otra al intentar craftear uno de los ing. faltantes,
    //si quedaria un sobrante de este ultimo que se pudiera utilizar para cubrir el crafteo de uno de los otros faltantes y abaratar el costo. Esto
    //depende del orden en el que esten los ingredientes a la hora de leer la receta y de la receta que se termine eligiendo para el ing. faltante,
    //que el programa consideraria como la mas eficiente.
    private ArrayList<PosibleFaltante> obtenerPosiblesFaltantes(String objeto, int cantACraftear, Inventario invActual){
        ArrayList<PosibleFaltante> listaPosiblesFaltantes = new ArrayList<>();

        for(Receta rec : recetario.buscarRecetas(objeto)){
            PosibleFaltante faltantesDeRec = new PosibleFaltante(new PosibleReceta(), invActual.crearCopia());
            PosibleReceta faltantes = new PosibleReceta();
            int crafteosNecesarios = (cantACraftear + rec.getCantidadProducida() - 1) / rec.getCantidadProducida();

            //si requiere mesa de crafteo veo si la tengo o si la puedo craftear, si no, tambien incluyo su costo
            if(rec.getMesaRequerida() != null) {
                if (!faltantesDeRec.estadoDeInv.estaEnInventario(rec.getMesaRequerida())) {
                    PosibleFaltante menorFaltanteDeMesa = obtenerMinimoFaltante(rec.getMesaRequerida().getNombre(), 1, faltantesDeRec.estadoDeInv);

                    if(menorFaltanteDeMesa != null){
                        faltantesDeRec.estadoDeInv = menorFaltanteDeMesa.estadoDeInv;
                        faltantesDeRec.faltantes = menorFaltanteDeMesa.faltantes;
                        //en este caso agrego la mesa independientemente de si se pudo craftear o no porque si no, el resto de faltantes
                        //no se enteraran que ya se tuvo en cuenta el costo de craftear la mesa
                        faltantesDeRec.estadoDeInv.agregarObjeto(rec.getMesaRequerida());
                        faltantesDeRec.historialActual.getRegistros().addAll(menorFaltanteDeMesa.historialActual.getRegistros());
                    }

                    faltantesDeRec.faltantes.agregarMesaRequerida(rec.getMesaRequerida());
                }
            }

            faltantesDeRec.faltantes.setCantProducida(rec.getCantidadProducida());

            for(Map.Entry<Objeto, Integer> elem : rec.getIngredientes().entrySet()){
                Objeto ing = elem.getKey();
                int cantReq = elem.getValue() * crafteosNecesarios;
                int cantEnInv = faltantesDeRec.estadoDeInv.getObjetos().getOrDefault(ing, 0);

                if(cantEnInv >= cantReq){
                    faltantesDeRec.estadoDeInv.getObjetos().replace(ing, cantEnInv - cantReq);
                } else{
                    faltantesDeRec.estadoDeInv.getObjetos().replace(ing, 0);
                    faltantes.getIngredientes().put(ing, cantReq - cantEnInv);
                }

                faltantesDeRec.estadoDeInv.getObjetos().remove(ing, 0);
            }

            if(!faltantes.getIngredientes().isEmpty()) {
                ArrayList<PosibleFaltante> menoresFaltantesPorObj = new ArrayList<>();

                for (Map.Entry<Objeto, Integer> elem : faltantes.getIngredientes().entrySet()) {
                    Objeto faltante = elem.getKey();
                    int cantFalt = elem.getValue();

                    if (faltante.esBasico()) {
                        faltantesDeRec.faltantes.getIngredientes().put(faltante, cantFalt);
                    } else {
                        PosibleFaltante menorFaltanteDeObj = obtenerMinimoFaltante(faltante.getNombre(), cantFalt, faltantesDeRec.estadoDeInv);

                        menoresFaltantesPorObj.add(menorFaltanteDeObj);
                        faltantesDeRec.estadoDeInv = menorFaltanteDeObj.estadoDeInv;
                        faltantesDeRec.historialActual.getRegistros().addAll(menorFaltanteDeObj.historialActual.getRegistros());
                    }
                }

                for (PosibleFaltante menorFaltanteObj : menoresFaltantesPorObj) {
                    faltantesDeRec.faltantes.combinarConPosReceta(menorFaltanteObj.faltantes);
                }
            }

            faltantesDeRec.faltantes.sumarTiempo(rec.getTiempoBase() * cantACraftear);
            listaPosiblesFaltantes.add(faltantesDeRec);
        }

        return listaPosiblesFaltantes;
    }

    private PosibleFaltante obtenerMinimoFaltante(String objeto, int cantACraftear, Inventario invActual){
        ArrayList<PosibleFaltante> faltantesDeObj = obtenerPosiblesFaltantes(objeto, cantACraftear, invActual);
        PosibleFaltante menorFaltanteDeObj = null;

        if (!faltantesDeObj.isEmpty()) {

            for (PosibleFaltante posFaltanteObj : faltantesDeObj) {
                if (menorFaltanteDeObj == null) {
                    menorFaltanteDeObj = posFaltanteObj;
                } else if (Util.compararPosRecetas(posFaltanteObj.faltantes, menorFaltanteDeObj.faltantes) < 0) {
                    menorFaltanteDeObj = posFaltanteObj;
                }
            }

            if(menorFaltanteDeObj.faltantes.getIngredientes().isEmpty()){
                //primero se redondea la cantidad de crafteos que se necesitaron
                int cantDeCrafteos = (cantACraftear + menorFaltanteDeObj.faltantes.getCantProducida() - 1) / menorFaltanteDeObj.faltantes.getCantProducida();

                menorFaltanteDeObj.historialActual.agregarRegistro(
                        new Objeto(objeto, recetario.buscarRecetas(objeto)),
                        menorFaltanteDeObj.faltantes.getCantProducida() * cantDeCrafteos
                                );
            }
        }
        return menorFaltanteDeObj;
    }
}
