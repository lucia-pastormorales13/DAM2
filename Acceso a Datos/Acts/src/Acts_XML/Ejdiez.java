package Acts_XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Locale;

/**
 * ============================================================================
 * EJERCICIO 10 - Crear un catálogo de productos XML desde cero
 * ============================================================================
 *
 * ENUNCIADO:
 *   No tenemos ningún XML que modificar, así que creamos un objeto Document
 *   VACÍO y escribimos el XML desde cero, volcándolo al fichero productos.xml:
 *
 *       <productos>
 *           <producto codigo="P001">
 *               <nombre>Teclado mecánico</nombre>
 *               <precio>59.90</precio>
 *               <stock>25</stock>
 *           </producto>
 *           <producto codigo="P002">
 *               <nombre>Ratón inalámbrico</nombre>
 *               <precio>24.50</precio>
 *               <stock>40</stock>
 *           </producto>
 *       </productos>
 *
 * LA DIFERENCIA CON EL EJERCICIO 9:
 *   Allí se LLENABA el árbol con builder.parse(fichero). Aquí no hay fichero
 *   de entrada, así que se usa builder.newDocument(), que devuelve un Document
 *   vacío y listo para construir el XML.
 *
 * EL CONCEPTO CLAVE: CREAR NO ES ENLAZAR
 *   Cuando creamos un elemento con document.createElement("nombre") el elemento
 *   EXISTE pero todavía NO forma parte del árbol. En este orden el XML
 *   almacenado quedaría VACÍO:
 *       Element producto = document.createElement("producto");
 *   Para que pase a formar parte del árbol hay que ENLAZARLO con appendChild().
 *
 *   Los 4 pasos para crear un elemento completo son:
 *     1. CREAR:   document.createElement("producto")
 *     2. RELLENAR: nombre.setTextContent("Teclado mecánico")
 *     3. ENLAZAR entre sí: producto.appendChild(nombre)   -> los hijos cuelgan
 *                   del padre
 *     4. ENLAZAR al árbol: productos.appendChild(producto) -> cuelga de la raíz
 *
 *   El orden importa: si intentamos appendChild() de un padre que todavía no
 *   está enlazado, los hijos se mueven con él cuando el padre se enlace. Por eso
 *   primero se rellenan y enlazan los hijos, y el último appendChild() es el
 *   que cuelga el subárbol entero de la raíz.
 *
 * LOS ATRIBUTOS se añaden con element.setAttribute("codigo", "P001"), que
 * solo existe en Element (por eso hay que hacer casting de Node a Element).
 * El elemento ATRIBUTO no es un hijo: se escribe dentro de la etiqueta de
 * apertura, por eso va separado del árbol de sub-elementos.
 */
public class Ejdiez {

    private static final String RUTA_SALIDA = "archivos" + File.separator + "productos.xml";

    public static void main(String[] args) {

        try {
            // ----------------------------------------------------------
            // PASO 1: crear un Document VACÍO
            // ----------------------------------------------------------
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // newDocument() no lee ningún fichero: crea el árbol DOM en blanco.
            Document document = builder.newDocument();

            // ----------------------------------------------------------
            // PASO 2: crear el elemento raíz y enlazarlo al Document
            // ----------------------------------------------------------
            /*
             * createElement() crea el nodo, pero aún NO está en el árbol.
             * document.appendChild() lo convierte en la raíz del documento.
             */
            Element productos = document.createElement("productos");
            document.appendChild(productos);

            // ----------------------------------------------------------
            // PASO 3: crear los dos productos y colgarlos de <productos>
            // ----------------------------------------------------------
            anadirProducto(document, productos, "P001", "Teclado mecánico", 59.90, 25);
            anadirProducto(document, productos, "P002", "Ratón inalámbrico", 24.50, 40);

            // ----------------------------------------------------------
            // PASO 4: escribir el árbol en productos.xml
            // ----------------------------------------------------------
            guardarXml(document, new File(RUTA_SALIDA));

        } catch (Exception e) {
            System.out.println("Error al crear el XML: " + e.getMessage());
        }
    }

    /**
     * Crea un elemento <producto> con sus sub-elementos y lo añade a la raíz.
     * Así evitamos repetir el mismo código para cada producto.
     *
     * @param document Document al que pertenece (para crear los elementos)
     * @param productos elemento raíz <productos> donde se enlazará el producto
     * @param codigo   valor del atributo codigo="..."
     * @param nombre   texto del sub-elemento <nombre>
     * @param precio   texto del sub-elemento <precio>
     * @param stock    texto del sub-elemento <stock>
     */
    private static void anadirProducto(Document document, Element productos,
                                       String codigo, String nombre,
                                       double precio, int stock) {

        // PASO 1: CREAR los elementos (aún no están en el árbol).
        Element producto = document.createElement("producto");
        Element elemNombre = document.createElement("nombre");
        Element elemPrecio = document.createElement("precio");
        Element elemStock = document.createElement("stock");

        // PASO 2: RELLENAR los sub-elementos con setTextContent().
        elemNombre.setTextContent(nombre);

        /*
         * Para el precio queremos que salga "59.90" y no "59.9", así que lo
         * formateamos con %.2f (dos decimales).
         *
         * OJO con String.format(): usa el separador decimal del Locale por
         * defecto, que en español es la COMA y rompería el XML (59,90).
         * Por eso forzamos Locale.US, que usa el punto.
         */
        elemPrecio.setTextContent(String.format(Locale.US, "%.2f", precio));

        elemStock.setTextContent(String.valueOf(stock));

        // AÑADIR EL ATRIBUTO al producto (no es un hijo, va en la etiqueta).
        producto.setAttribute("codigo", codigo);

        // PASO 3: ENLAZAR los sub-elementos con su padre <producto>.
        producto.appendChild(elemNombre);
        producto.appendChild(elemPrecio);
        producto.appendChild(elemStock);

        // PASO 4: ENLAZAR el producto completo a la raíz <productos>.
        productos.appendChild(producto);

        System.out.println("Producto " + codigo + " añadido al XML");
    }

    /**
     * Escribe un objeto Document en un fichero XML.
     *
     * @param documento árbol DOM ya construido
     * @param fichero   fichero de destino (se crea si no existe)
     */
    private static void guardarXml(Document documento, File fichero) {

        try {
            // Crear el transformador.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            /*
             * Por defecto el Transformer escribe el XML TODO en una sola línea.
             * Estas dos propiedades lo indentan para que sea legible:
             *   INDENT         -> activa la indentación
             *   INDENT-AMOUNT  -> cuántos espacios por nivel
             */
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // DOMSource = origen (el Document).
            DOMSource source = new DOMSource(documento);

            // StreamResult = destino (el fichero).
            StreamResult result = new StreamResult(fichero);

            // Volcar el árbol DOM al fichero.
            transformer.transform(source, result);

            System.out.println("Fichero '" + fichero.getName() + "' creado correctamente.");

        } catch (TransformerException e) {
            System.out.println("Error al guardar el documento XML: " + e.getMessage());
        }
    }
}
