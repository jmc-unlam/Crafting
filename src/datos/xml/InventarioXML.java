package datos.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import modelo.Objeto;
import modelo.ObjetoBasico;
import modelo.ObjetoIntermedio;

public class InventarioXML extends ManejadorXML<Map<Objeto, Integer>> {

	private Objeto objetoActual;
	//private StringBuilder contenido;

	public InventarioXML(String rutaArchivo) {
		super(rutaArchivo);
		datos = new HashMap<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//contenido = new StringBuilder();
		if (qName.equalsIgnoreCase("objeto")) {
            String nombre = attributes.getValue("_nombre");
            String tipo = attributes.getValue("tipo");
            int cantidad = Integer.parseInt(attributes.getValue("cantidad"));

            if ("basico".equalsIgnoreCase(tipo)) {
                objetoActual = new ObjetoBasico(nombre);
                datos.put(objetoActual, cantidad);
            } else if ("intermedio".equalsIgnoreCase(tipo)) {
                objetoActual = new ObjetoIntermedio(nombre);
                datos.put(objetoActual, cantidad);
            } else {
            	datos= new HashMap<>();
                throw new SAXException("Tipo de objeto desconocido para ingrediente: " + tipo);
            }
            
        }
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		//contenido.append(ch, start, length);
	}

	@Override
	protected void generarDocumento(Document doc, Map<Objeto, Integer> inventario) {
		Element rootElement = doc.createElement("objetos");
        doc.appendChild(rootElement);

        for (Map.Entry<Objeto, Integer> entry : inventario.entrySet()) {
            Objeto obj = entry.getKey();
            Element objetoElement = doc.createElement("objeto");

            objetoElement.setAttribute("_nombre", obj.getNombre());
            objetoElement.setAttribute("tipo", obj instanceof ObjetoBasico ? "basico" : "intermedio");
            objetoElement.setAttribute("cantidad", entry.getValue().toString());

            rootElement.appendChild(objetoElement);
        }
	}

}
