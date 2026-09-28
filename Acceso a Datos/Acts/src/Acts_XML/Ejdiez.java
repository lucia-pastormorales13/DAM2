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

public class Ejdiez {

    private static final String RUTA_SALIDA = "archivos" + File.separator + "productos.xml";

    public static void main(String[] args) {

        try {

            // crear un Document VACÍO

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // newDocument() no lee ningún fichero: crea el árbol DOM en blanco.
            Document document = builder.newDocument();

            // crear el elemento raíz y enlazarlo al Document
            Element productos = document.createElement("productos");
            document.appendChild(productos);

            // crear los dos productos y colgarlos de <productos>

            anadirProducto(document, productos, "P001", "Teclado mecánico", 59.90, 25);
            anadirProducto(document, productos, "P002", "Ratón inalámbrico", 24.50, 40);

            // escribir el árbol en productos.xml

            guardarXml(document, new File(RUTA_SALIDA));

        } catch (Exception e) {
            System.out.println("Error al crear el XML: " + e.getMessage());
        }
    }

    private static void anadirProducto(Document document, Element productos,
            String codigo, String nombre,
            double precio, int stock) {

        // CREAR los elementos
        Element producto = document.createElement("producto");
        Element elemNombre = document.createElement("nombre");
        Element elemPrecio = document.createElement("precio");
        Element elemStock = document.createElement("stock");

        // RELLENAR los sub-elementos
        elemNombre.setTextContent(nombre);

        elemPrecio.setTextContent(String.format(Locale.US, "%.2f", precio));

        elemStock.setTextContent(String.valueOf(stock));

        producto.setAttribute("codigo", codigo);

        producto.appendChild(elemNombre);
        producto.appendChild(elemPrecio);
        producto.appendChild(elemStock);

        productos.appendChild(producto);

        System.out.println("Producto " + codigo + " añadido al XML");
    }

    private static void guardarXml(Document documento, File fichero) {

        try {
            // Crear el transformador.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

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
