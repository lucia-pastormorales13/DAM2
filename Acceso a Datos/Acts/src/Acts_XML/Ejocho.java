package Acts_XML;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;


public class Ejocho {
    public static void main(String [] args) {
        try {
            // Crear los recursos necesarios para crear el parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Crear la estructura de arbol
            Document documento = builder.parse(new File("libros.xml"));
            System.out.println("XML cargado correctamente");
            
            //SACAR TODOS LOS BLOQUES DE ETIQUETA <libro></libro>
            NodeList libros= documento.getElementsByTagName("libro");
            int contador= 0;
            for( int i= 0; i< libros.getLength(); i++){
                Node libro= libros.item(i);

                if(libro.getNodeType()== Node.ELEMENT_NODE){
                    
                    Element libroElement= (Element) libro;
                    NodeList propiedadeslibro= libroElement.getChildNodes();

                    Node titulo= propiedadeslibro.item(0);
                    Node autor= propiedadeslibro.item(1);
                    Node precio= propiedadeslibro.item(2);
                    System.out.println(titulo.getNodeName()+": "+titulo.getTextContent());
                    System.out.println(autor.getNodeName()+": "+autor.getTextContent());
                    System.out.println(precio.getNodeName()+": "+precio.getTextContent());
                    contador++;

                    /*for( int n=0; n<propiedadeslibro.getLength(); n++){

                    }*/
                }
                
            }
            System.out.println("El número total de libros es: "+contador);
        } catch (Exception e) {
            System.out.println("Error al leer el XML");
        }
    }

}