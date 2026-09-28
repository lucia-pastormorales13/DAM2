package FicherosCSV_conOpenCSV;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
  ENUNCIADO:
    Hay dos CSV con los MISMOS datos de alumnos pero con distinto delimitador:
      a) alumnos_comas.csv        -> usa ',' como separador
      b) alumnos_puntoycoma.csv   -> usa ';' como separador
    El programa debe leer AMBOS con OpenCSV, indicando claramente de qué
    fichero procede cada dato. Está PROHIBIDO usar split().
 */
public class Ejcinco {

    public static void main(String[] args) {

        File ficheroComas = new File("archivos" + File.separator + "alumnos_comas.csv");
        File ficheroPuntoYComa = new File("archivos" + File.separator + "alumnos_puntoycoma.csv");

        
        ICSVParser parserPuntoYComa = new CSVParserBuilder().withSeparator(';').build();
        try (
                FileReader frComas = new FileReader(ficheroComas);
                FileReader frPuntoYComa = new FileReader(ficheroPuntoYComa);
                CSVReader lectorComas = new CSVReader(frComas);
                CSVReader lectorPuntoYComa = new CSVReaderBuilder(frPuntoYComa)
                        .withCSVParser(parserPuntoYComa)
                        .build()
        ) {
            System.out.println("=== Datos de " + ficheroComas.getName()
                    + " (delimitador: ',' ) ===");

           
            String[] datos;

            while ((datos = lectorComas.readNext()) != null) {

                
                for (String campo : datos) {
                    System.out.print(campo + " | ");
                }
                System.out.println();
            }

            
            System.out.println();
            System.out.println("=== Datos de " + ficheroPuntoYComa.getName()
                    + " (delimitador: ';' ) ===");

            while ((datos = lectorPuntoYComa.readNext()) != null) {
                for (String campo : datos) {
                    System.out.print(campo + " | ");
                }
                System.out.println();
            }

        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al leer los ficheros CSV: " + e.getMessage());
        }
    }
}
