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
 * ENUNCIADO:
 *   Partiendo de 'alumnos.xml':
 *     - Buscar el alumno con id="2" y poner su nota final a 9.0.
 *     - Buscar el alumno con id="3" y eliminarlo del XML.
 *     - Guardar el resultado en 'alumnos_modificados.xml'.
 *     - El resto de la información debe mantenerse sin cambios.
 *
 * EL FICHERO DE PARTIDA:
 *       <alumnos>
 *           <alumno id="1"><nombre>..</nombre><curso>1DAM</curso><nota>8.5</nota></alumno>
 *           <alumno id="2"><nombre>..</nombre><curso>1DAM</curso><nota>6.5</nota></alumno>
 *           <alumno id="3"><nombre>..</nombre><curso>1DAM</curso><nota>9.25</nota></alumno>
 *       </alumnos>
 *
 * LAS 4 OPERACIONES DEL DOM QUE USAMOS:
 *
 *  1. MODIFICAR EL TEXTO de un nodo:
 *         node.setTextContent("9.0")
 *     Funciona sobre cualquier Node. OJO: esto cambia el objeto Document EN
 *     MEMORIA; no toca el fichero hasta que escribimos con el Transformer.
 *
 *  2. ELIMINAR UN ELEMENTO CON SUS HIJOS:
 *         nodoPadre.removeChild(nodoHijo)
 *     No basta con removeChild(nodo): hay que decirle al PADRE que lo elimine
 *     del árbol. Por eso usamos getParentNode().
 *
 *  3. BUSCAR POR ATRIBUTO:
 *     No hay un getElementById como en JavaScript, así que hay que recorrer
 *     todos los <alumno> y comparar el atributo "id" con getAttribute("id").
 *
 *  4. ESCRIBIR EL RESULTADO (lo que más se olvida):
 *     Se usan cuatro clases: TransformerFactory, Transformer, DOMSource y
 *     StreamResult. DOMSource envuelve el Document (el origen) y StreamResult
 *     indica el fichero de destino. Si nos saltamos este paso, el fichero
 *     original se queda como estaba.
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
            // ----------------------------------------------------------
            // PASO 1: leer el XML y construir el árbol DOM
            // ----------------------------------------------------------
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(ficheroEntrada);

            /*
             getElementsByTagName("alumno") devuelve TODOS los <alumno> del
             documento, en el orden en que aparecen en el fichero.
             */
            NodeList alumnos = documento.getElementsByTagName("alumno");

            for (int i = 0; i < alumnos.getLength(); i++) {

                Node nodo = alumnos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                    // Element amplía a Node: permite usar getAttribute().
                    Element alumno = (Element) nodo;

                    // getAttribute("id") devuelve el valor del atributo id.
                    if ("2".equals(alumno.getAttribute("id"))) {

                        // Localizamos el sub-elemento <nota> de este alumno.
                        Node nota = alumno.getElementsByTagName("nota").item(0);

                        // setTextContent() sustituye TODO el contenido textual
                        // del nodo por el nuevo valor.
                        nota.setTextContent("9.0");

                        System.out.println("Alumno con id=2 actualizado: nota -> 9.0");
                    }
                }
            }

            /*
             aquí no eliminamos mientras recorremos la misma
             NodeList, porque al quitar un nodo la lista cambia de tamaño y
             se podrían saltar elementos. Por eso volvemos a obtener la lista
             solo buscamos el nodo a eliminar.
             */
            NodeList alumnos2 = documento.getElementsByTagName("alumno");

            for (int i = 0; i < alumnos2.getLength(); i++) {

                Node nodo = alumnos2.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                    Element alumno = (Element) nodo;

                    if ("3".equals(alumno.getAttribute("id"))) {

                        /*
                         removeChild() NO se llama sobre el hijo, sino sobre el
                         padre, indicando el hijo que quiere quitar. Por eso
                         pedimos antes el padre con getParentNode().
                         Al eliminar <alumno> se van también sus sub-elementos porque son hijos suyos.
                         */
                        Node padre = alumno.getParentNode();
                        padre.removeChild(alumno);

                        System.out.println("Alumno con id=3 eliminado del XML");

                        /*
                         break porque el NodeList sigue teniendo el mismo
                         número de posiciones: al haber quitado un nodo, el
                         índice i ya no apunta al mismo elemento y el bucle
                         podría volver a mirar un nodo equivocado. Salimos
                         porque solo hay un alumno con id=3.
                         */
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
            // 1) Crear el transformador.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            /*
             Por defecto el XML se escribiría todo en una sola línea. Con
             INDENT y INDENT-AMOUNT se genera un fichero legible y sangrado,
             que es lo que se ve al abrir el resultado en un editor.
            */
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            /*
             * 2) DOMSource: convierte el objeto Document en algo "escribible"
             *    para el Transformer. Es el ORIGEN de la transformación.
             */
            DOMSource source = new DOMSource(documento);

            /*
             * 3) StreamResult: indica el DESTINO. Si el fichero no existe,
             *    el Transformer lo crea automáticamente. Aquí le pasamos un
             *    objeto File, pero también aceptaría un String con la ruta.
             */
            StreamResult result = new StreamResult(fichero);

            // 4) Volcar el contenido del DOM en el fichero.
            transformer.transform(source, result);

            System.out.println("Fichero '" + fichero.getName() + "' generado correctamente.");

        } catch (TransformerException e) {
            /*
             * TransformerException es la excepción propia de la escritura de
             * XML (problemas de configuración del transformador o de E/S).
             */
            System.out.println("Error al guardar el documento XML: " + e.getMessage());
        }
    }
}
