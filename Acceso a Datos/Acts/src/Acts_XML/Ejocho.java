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
            System.out.println("El número total de libros es: "+contador);
        } catch (Exception e) {
            System.out.println("Error al leer el XML");
        }
    }

}