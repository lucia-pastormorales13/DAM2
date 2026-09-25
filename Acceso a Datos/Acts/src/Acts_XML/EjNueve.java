package Acts_XML;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class EjNueve {
    public static void main(String[] args) {

        try {
            // Crear los recursos necesarios para crear el parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Crear la estructura de arbol
            Document documento = builder.parse(new File("alumnos.xml"));
            System.out.println("XML cargado correctamente");


        } catch (Exception e) {
            System.out.println("Error al leer el XML");
        }
    }
}