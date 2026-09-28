package Acts_XML;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
  ENUNCIADO:
    Partiendo de 'alumnos.xml':
      - Buscar el alumno con id="2" y poner su nota final a 9.0.
     - Buscar el alumno con id="3" y eliminarlo del XML.
      - Guardar el resultado en 'alumnos_modificados.xml'.
      - El resto de la información debe mantenerse sin cambios.
 */
public class Ejnueve {

    private static final String RUTA_ENTRADA = "archivos" + File.separator + "alumnos.xml";
    private static final String RUTA_SALIDA = "archivos" + File.separator + "alumnos_modificados.xml";

    public static void main(String[] args) {

        File ficheroEntrada = new File(RUTA_ENTRADA);

        if (!ficheroEntrada.exists()) {
            System.out.println("No se ha encontrado: " + ficheroEntrada.getAbsolutePath());
            return;
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(ficheroEntrada);

            NodeList alumnos = documento.getElementsByTagName("alumno");

            for (int i = 0; i < alumnos.getLength(); i++) {

                Node nodo = alumnos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                    Element alumno = (Element) nodo;

                    // getAttribute("id") devuelve el valor del atributo id.
                    if ("2".equals(alumno.getAttribute("id"))) {

                        Node nota = alumno.getElementsByTagName("nota").item(0);

                        // setTextContent() sustituye TODO el contenido textual

                        nota.setTextContent("9.0");

                        System.out.println("Alumno con id=2 actualizado: nota -> 9.0");
                    }
                }
            }

            NodeList alumnos2 = documento.getElementsByTagName("alumno");

            for (int i = 0; i < alumnos2.getLength(); i++) {

                Node nodo = alumnos2.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                    Element alumno = (Element) nodo;

                    if ("3".equals(alumno.getAttribute("id"))) {

                        Node padre = alumno.getParentNode();
                        padre.removeChild(alumno);

                        System.out.println("Alumno con id=3 eliminado del XML");

                        break;
                    }
                }
            }

            guardarXml(documento, new File(RUTA_SALIDA));

        } catch (Exception e) {
            System.out.println("Error al procesar el XML: " + e.getMessage());
        }
    }

    private static void guardarXml(Document documento, File fichero) {

        try {
            // Crear el transformador.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(documento);

            StreamResult result = new StreamResult(fichero);

            // Volcar el contenido del DOM en el fichero.
            transformer.transform(source, result);

            System.out.println("Fichero '" + fichero.getName() + "' generado correctamente.");

        } catch (TransformerException e) {

            System.out.println("Error al guardar el documento XML: " + e.getMessage());
        }
    }
}
