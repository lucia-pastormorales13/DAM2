package Acts_XML;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**

 * ENUNCIADO:
 *   Leer 'libros.xml', transformarlo en un objeto Document y mostrar por
 *   pantalla el título, autor y precio de cada libro. Al final, mostrar el
 *   número total de libros.
 
 */
public class Ejocho {

    private static final String RUTA = "archivos" + File.separator + "libros.xml";

    public static void main(String[] args) {

        File fichero = new File(RUTA);

        if (!fichero.exists()) {
            System.out.println("No se ha encontrado: " + fichero.getAbsolutePath());
            return;
        }

        /*
         * No usamos try-with-resources aquí porque DocumentBuilder y
         * DocumentBuilderFactory no implementan Closeable. El enunciado
         * pedía try-with-resources para los ejercicios de texto (1-4), donde
         * sí se usa FileReader/BufferedReader.
         */
        try {
            // PASO 1: crear el parser y cargar el XML en memoria.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(fichero);

            System.out.println("XML cargado correctamente desde " + fichero.getName());
            System.out.println("------------------------------------");

            /*
             
             * getElementsByTagName("libro") devuelve un NodeList con todos los
             * nodos que tienen esa etiqueta. Se le puede llamar sobre un
             * Document (busca en todo el árbol) o sobre un Element (busca solo
             * en sus descendientes).
             */
            NodeList libros = documento.getElementsByTagName("libro");

            int contador = 0;

            
            for (int i = 0; i < libros.getLength(); i++) {

                Node libro = libros.item(i);

                if (libro.getNodeType() == Node.ELEMENT_NODE) {

                    Element elementoLibro = (Element) libro;

                    String titulo = elementoLibro.getElementsByTagName("titulo")
                            .item(0).getTextContent();
                    String autor = elementoLibro.getElementsByTagName("autor")
                            .item(0).getTextContent();
                    String precio = elementoLibro.getElementsByTagName("precio")
                            .item(0).getTextContent();

                    System.out.println("Libro: " + titulo);
                    System.out.println("Autor: " + autor);
                    System.out.println("Precio: " + precio + " €");
                    System.out.println();

                    //Forma 1: ignorar los nodos que no sean ELEMENT_NODE
                    for (int x=0; x< propiedadeslibro.getLength(); i++){
                        Node n= propiedadeslibro.item(x);
                        if(n.getNodeType() == Node.ELEMENT_NODE){
                            
                            Element e= (Element)n;
                            System.out.println(e.getNodeName()+": "+e.getTextContent());
                        }

                    }

                    //Forma 2: Sacar cada nodo del libro con la f(x)' GETELEMENTSBYTAGNAME
                    NodeList tituloLista= libroElement.getElementsByTagName("titulo");
                    Element titulo= (Element) tituloLista.item(0);
                    titulo.getNodeName(); titulo.getTextContent();
                    //...                 
                }                
            }

            // PASO 3: número total de libros.
            System.out.println("------------------------------------");
            System.out.println("Número de libros: " + contador);

        } catch (Exception e) {
           
            System.out.println("Error al leer el XML: " + e.getMessage());
        }
    }
}
