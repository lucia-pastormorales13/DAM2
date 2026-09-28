package FicherosCSV_conOpenCSV;

import java.io.FileReader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/* avisar q el delimitador d alumnos_puntoycoma es ; para leer
 ambos archivos mencionando de donde vienen */
public class Ejcinco {
    public static void main(String[] args) {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (
                FileReader frs = new FileReader("alumnos_comas.csv");
                FileReader frp = new FileReader("alumnos_puntoycoma.csv");
                CSVReader lectors = new CSVReader(frs);
                CSVReader lectorp = new CSVReaderBuilder(frp).withCSVParser(parser).build();
            ) {
            String[] datos;
            System.out.println("Lista de alumnos extraidos de alumnos_comas.csv");
            while ((datos = lectors.readNext()) != null) {
                for (String dato : datos) {
                    System.out.print(dato + " | ");
                }
                System.out.println();
            }

            System.out.println("Lista de alumnos extraidos de alumnos_puntoycoma.csv");
            while ((datos = lectorp.readNext()) != null) {
                for (String dato : datos) {
                    System.out.print(dato + " | ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error al leer el fichero.");
        }

    }

}