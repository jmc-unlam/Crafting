package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import main.Config;

public class Recetario {
	private Map<Objeto, List<Receta>> recetasPorObjeto;

    public Recetario() {
        this.recetasPorObjeto = new HashMap<>();
    }

    public Recetario(List<Receta> recetasIniciales) {
        this.recetasPorObjeto = new HashMap<>();
        if (recetasIniciales != null) {
            for (Receta receta : recetasIniciales) {
                this.agregarReceta(receta); // Usa el nuevo método que admite múltiples recetas
            }
        }
    }

    public void agregarReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula");
        }
    	recetasPorObjeto.putIfAbsent(receta.getObjetoProducido(), new ArrayList<>());
        recetasPorObjeto.get(receta.getObjetoProducido()).add(receta);
    }

    public void removerReceta(Receta receta) {
    	if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula");
        }
        List<Receta> recetas = recetasPorObjeto.get(receta.getObjetoProducido());
        if (recetas == null || !recetas.remove(receta)) {
            throw new IllegalArgumentException("La receta no existe en el recetario");
        }
        // Eliminar la clave si la lista queda vacía
        if (recetas.isEmpty()) {
            recetasPorObjeto.remove(receta.getObjetoProducido());
        }
    }

    public List<Receta> getRecetas() {
    	List<Receta> todas = new ArrayList<>();
    	for (List<Receta> lista : recetasPorObjeto.values()) {
            todas.addAll(lista);
        }
    	return todas; // Devuelve toda las recetas.
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== RECETARIO ===\n");
        for (Receta receta : this.getRecetas()) {
            sb.append(receta).append("\n");
        }
        return sb.toString();
    }

    public Receta buscarReceta(Objeto objetoDeseado) {
    	List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
    	if (recetas == null || recetas.isEmpty()) {
            throw new NoSuchElementException(
                "No se encontró ninguna receta para el objeto: " + objetoDeseado.getNombre()
            );
        }
        return recetas.get(0); // Devolver la primera receta disponible
    }
    
    public List<Receta> buscarRecetas(Objeto objetoDeseado) {
        List<Receta> recetas = recetasPorObjeto.get(objetoDeseado);
        if (recetas == null || recetas.isEmpty()) {
            throw new NoSuchElementException("No existen recetas asociadas a :" + objetoDeseado);
        }
        return new ArrayList<>(recetas);
    }
    
    public Map<Objeto, Integer> buscarIngredientes(Objeto objeto) {
    	List<Receta> recetas = recetasPorObjeto.get(objeto);
    	if (recetas == null || recetas.isEmpty()) {
            return Collections.emptyMap();
        }
        return new HashMap<>(recetas.get(0).getIngredientes()); // Solo de la primera receta
    }

    //*****Implementacion Mesas de Trabajo*************
	public void agregarRecetas(List<Receta> recetas) {
		for (Receta receta : recetas ) {
			this.agregarReceta(receta);
		}
	}

	public void removerRecetas(List<Receta> recetas) {
		for (Receta receta : recetas ) {
			this.removerReceta(receta);
		}
	}

	public Map<Objeto, List<Receta>> getRecetasPorObjeto() {
		return new HashMap<>(this.recetasPorObjeto);
	}
	
	public void PrologGenerarRecetas()
	{
		//Borrar Archivo con la lista de Recetas para generarlo nuevamente.
		// Ruta relativa al archivo dentro del proyecto
        File archivo = new File(Config.RUTA_PROLOG_RECETAS);
        
        
        // Verificar si el archivo existe
        if (archivo.exists()) {
            // Intentar eliminarlo
        	archivo.delete();
        }
		
        try {
        	
        	archivo.createNewFile();

            // Escribir texto en el archivo
            FileWriter writer = new FileWriter(archivo);
            writer.write("% Recetas\n");
            writer.write("% Lista de Objetos Crafteables \n");
            
          //Recorrer las recetas y colocar los ingredientes compuestos.
            for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
            	Objeto objetosCrafteable = entry.getKey();
            	writer.write("ingredientecompuesto('" + objetosCrafteable.getNombre() + "').\n");
			}
            
            writer.write("\n");
            writer.write("% Ingredientes por objeto\n");
            
            //Recorrer las recetas y colocar la primera receta para colocar los ingredientes.
            for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
            	Objeto objetosCrafteable = entry.getKey();
            	List<Receta> listaRecetas = entry.getValue();
            	
            	Receta receta = listaRecetas.getFirst(); // Devolver la primera receta.
            	Map<Objeto, Integer> ingredientes = receta.getIngredientes();
            	
            	for (Map.Entry<Objeto, Integer> ingrediente : ingredientes.entrySet()) {
					Objeto objIngrediente = ingrediente.getKey();
					Integer cantidad = ingrediente.getValue();
					writer.write("ingrediente('" + objetosCrafteable.getNombre() +"', '"+ objIngrediente.getNombre() + "', " + cantidad + ").\n");
				}
			}
            
            writer.close(); // Cerrar el flujo

		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public List<Objeto> listaCrafteable(){
		//Devuelve el listado de Objetos Farmeables.
		List<Objeto> listaObjetos = new ArrayList<Objeto>();
		
		for (Map.Entry<Objeto, List<Receta>> entry : recetasPorObjeto.entrySet()) {
			Objeto objetosCrafteable = entry.getKey();
			listaObjetos.add(objetosCrafteable);
		}
		
		if(listaObjetos.size()>0)
		{
			System.out.println("--Lista de Objetos a Craftear.");
			int id = 1;
			for (Objeto objeto : listaObjetos) {
				System.out.println("NrID°:" + id+ "-" + objeto);
				id++;
			}
			System.out.println("\n");
		}
		else
			System.out.println("No hay objetos para craftear.\n");
		
		return listaObjetos;
	}
	
	public List<Objeto> listaObjetosRecolectables(){
		//Devuelve la lista de Objetos Basicos.
		Set<Objeto> listaObjetosOri = new LinkedHashSet<Objeto>();
		
		for (Map.Entry<Objeto, List<Receta>> listaRecetas : recetasPorObjeto.entrySet()) {
			List<Receta> recetasObjeto = listaRecetas.getValue();
			
			for (Receta receta : recetasObjeto) {
				Map<Objeto, Integer> ingredientes = receta.getIngredientes();
				for (Map.Entry<Objeto, Integer> objBuscandoBasicos : ingredientes.entrySet()) {
					Objeto key = objBuscandoBasicos.getKey();
					
					if(key.esBasico())
						listaObjetosOri.add(key);
				}
			}
			
		}
		
		List<Objeto> listaObjetos = new ArrayList<>(listaObjetosOri);
		
		if(listaObjetos.size()>0)
		{
			System.out.println("--Lista de Objetos Farmeables.");
			int id = 1;
			for (Objeto objeto : listaObjetos) {
				System.out.println("NrID°:" + id+ "-" + objeto);
				id++;
			}
			System.out.println("\n");
		}
		else
			System.out.println("No hay objetos para craftear.\n");
		
		return listaObjetos;
	}
}
