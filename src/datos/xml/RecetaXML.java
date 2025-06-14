package datos.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;
import modelo.Receta;

public class RecetaXML extends ManejadorXML<List<Receta>> {

    private Receta recetaActual;
    private ObjetoIntermedio objetoProducidoActual;
    private Map<Objeto, Integer> ingredientesActuales;
    //private StringBuilder contenido; //ya no es necesario.

    public RecetaXML(String rutaArchivo) {
        super(rutaArchivo);
        datos = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //contenido = new StringBuilder(); // Limpiar el contenido al inicio de cada elemento

        if (qName.equalsIgnoreCase("receta")) {
            ingredientesActuales = new HashMap<>(); // Inicializar un nuevo mapa para cada receta
        } else if (qName.equalsIgnoreCase("objetoProducido")) {
            String nombre = attributes.getValue("_nombre");
            int cantidadProducida = Integer.parseInt(attributes.getValue("cantidad"));
            int tiempoBase = Integer.parseInt(attributes.getValue("tiempo"));
            // El objetoProducido de una receta SIEMPRE es ObjetoIntermedio
            this.objetoProducidoActual = new ObjetoIntermedio(nombre);
            this.recetaActual = new Receta(objetoProducidoActual, ingredientesActuales, cantidadProducida, tiempoBase);
        } else if (qName.equalsIgnoreCase("ingrediente")) {
            String nombre = attributes.getValue("_nombre");
            String tipo = attributes.getValue("tipo");
            int cantidad = Integer.parseInt(attributes.getValue("cantidad"));

            Objeto ingrediente;
            if ("basico".equalsIgnoreCase(tipo)) {
                ingrediente = new ObjetoBasico(nombre);
            } else if ("intermedio".equalsIgnoreCase(tipo)) {
                ingrediente = new ObjetoIntermedio(nombre);
            } else {
                throw new SAXException("Tipo de objeto desconocido para ingrediente: " + tipo);
            }
            if (ingredientesActuales != null) {
                ingredientesActuales.put(ingrediente, cantidad);
            } else {
                throw new SAXException("Receta incompleta encontrada.");
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //String textoElemento = contenido.toString().trim();

        if (qName.equalsIgnoreCase("receta")) {
            // Cuando termina la etiqueta <receta>, la receta ya debería estar completa
            if (recetaActual != null) {
                datos.add(recetaActual);
            } else {
                throw new SAXException("Receta incompleta encontrada.");
            }
            // Resetear para la siguiente receta
            recetaActual = null;
            objetoProducidoActual = null;
            ingredientesActuales = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Solo acumular contenido para los elementos que tienen texto interno
        // como <cantidadProducida> y <tiempoBase>
        //contenido.append(ch, start, length);
    }

    @Override
    protected void generarDocumento(Document doc, List<Receta> recetas) {
        Element rootElement = doc.createElement("recetas");
        doc.appendChild(rootElement);

        for (Receta receta : recetas) {
            Element recetaElement = doc.createElement("receta");
            rootElement.appendChild(recetaElement);

            // Objeto producido
            Element objProdElement = doc.createElement("objetoProducido");
            objProdElement.setAttribute("_nombre", receta.getObjetoProducido().getNombre());
            objProdElement.setAttribute("cantidad", Integer.toString(receta.getCantidadProducida()));
            objProdElement.setAttribute("tiempo", Integer.toString(receta.getTiempoBase()));
            recetaElement.appendChild(objProdElement);

            // Ingredientes sin tag <ingredientes>
            //Element ingredientesElement = doc.createElement("ingredientes");
            //recetaElement.appendChild(ingredientesElement);

            for (Map.Entry<Objeto, Integer> entry : receta.getIngredientes().entrySet()) {
                Element ingredienteElement = doc.createElement("ingrediente");
                Objeto obj = entry.getKey();

                ingredienteElement.setAttribute("_nombre", obj.getNombre());
                ingredienteElement.setAttribute("tipo", obj instanceof ObjetoBasico ? "basico" : "intermedio");
                ingredienteElement.setAttribute("cantidad", entry.getValue().toString());

                recetaElement.appendChild(ingredienteElement);
            }

            // Cantidad producida
            //Element cantElement = doc.createElement("cantidadProducida");
            //cantElement.appendChild(doc.createTextNode(String.valueOf(receta.getCantidadProducida())));
            //recetaElement.appendChild(cantElement);

            // Tiempo base
            //Element tiempoElement = doc.createElement("tiempoBase");
            //tiempoElement.appendChild(doc.createTextNode(String.valueOf(receta.getTiempoBase())));
            //recetaElement.appendChild(tiempoElement);
        }
    }
}