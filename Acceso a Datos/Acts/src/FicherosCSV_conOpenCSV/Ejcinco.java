package FicherosCSV_conOpenCSV;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * ============================================================================
 * EJERCICIO 5 - Leer archivos CSV con distintos delimitadores
 * ============================================================================
 *
 * ENUNCIADO:
 *   Hay dos CSV con los MISMOS datos de alumnos pero con distinto delimitador:
 *     a) alumnos_comas.csv        -> usa ',' como separador
 *     b) alumnos_puntoycoma.csv   -> usa ';' como separador
 *   El programa debe leer AMBOS con OpenCSV, indicando claramente de qué
 *   fichero procede cada dato. Está PROHIBIDO usar split().
 *
 * POR QUÉ OpenCSV Y NO split():
 *   Con split(";") sobre la línea
 *       Ana;"Calle Mayor; 25";24
 *   obtendríamos 4 trozos en lugar de 3, porque el ';' está también DENTRO de
 *   un campo entrecomillado y split no entiende de comillas.
 *   OpenCSV interpreta correctamente las comillas, los separadores dentro de
 *   los campos y los registros que ocupan varias líneas. Por eso el enunciado
 *   prohíbe split(): readNext() ya devuelve el registro troceado en campos.
 *
 *   El enunciado menciona un constructor del tipo  new CSVReader(fr, ';')  que
 *   aparece en la teoría, pero en la versión de OpenCSV que usamos (5.12.0) ese
 *   constructor YA NO EXISTE. La forma correcta en esta versión es la de los
 *   "builders": se configura un CSVParser con el separador y se lo pasa al
 *   CSVReaderBuilder. Es exactamente lo que hace este programa.
 *
 * DIFERENCIA ENTRE AMBOS DELIMITADORES:
 *   - OpenCSV usa ',' por defecto, así que para leer alumnos_comas.csv basta
 *     con new CSVReader(fr).
 *   - Para alumnos_puntoycoma.csv hay que indicar que el separador es ';'
 *     mediante CSVParserBuilder().withSeparator(';').
 */
public class Ejcinco {

    public static void main(String[] args) {

        // Rutas de los dos ficheros. File.separator hace la ruta portable.
        File ficheroComas = new File("archivos" + File.separator + "alumnos_comas.csv");
        File ficheroPuntoYComa = new File("archivos" + File.separator + "alumnos_puntoycoma.csv");

        /*
         * Configuramos el parser para el fichero que usa ';'.
         * withSeparator(';') indica cuál es el carácter separador de campos.
         * Este parser se reutilizará luego en el CSVReaderBuilder.
         */
        ICSVParser parserPuntoYComa = new CSVParserBuilder().withSeparator(';').build();

        /*
         * TRY-WITH-RESOURCES con los DOS ficheros abiertos a la vez.
         * OpenCSV envuelve al FileReader, por eso se declara el FileReader
         * primero y el CSVReader después. Java cerrará los 4 recursos en
         * orden inverso al terminar el try.
         */
        try (
                FileReader frComas = new FileReader(ficheroComas);
                FileReader frPuntoYComa = new FileReader(ficheroPuntoYComa);
                CSVReader lectorComas = new CSVReader(frComas);
                CSVReader lectorPuntoYComa = new CSVReaderBuilder(frPuntoYComa)
                        .withCSVParser(parserPuntoYComa)
                        .build()
        ) {

            // ------------------------------------------------------------
            // Fichero 1: alumnos_comas.csv (delimitador ',' por defecto)
            // ------------------------------------------------------------
            System.out.println("=== Datos de " + ficheroComas.getName()
                    + " (delimitador: ',' ) ===");

            // readNext() devuelve un String[] con los campos del registro
            // leído, o null cuando se ha terminado el fichero.
            String[] datos;

            while ((datos = lectorComas.readNext()) != null) {

                // for-each: recorre los campos del registro actual.
                for (String campo : datos) {
                    System.out.print(campo + " | ");
                }
                System.out.println();
            }

            // ------------------------------------------------------------
            // Fichero 2: alumnos_puntoycoma.csv (delimitador ';')
            // ------------------------------------------------------------
            System.out.println();
            System.out.println("=== Datos de " + ficheroPuntoYComa.getName()
                    + " (delimitador: ';' ) ===");

            while ((datos = lectorPuntoYComa.readNext()) != null) {
                for (String campo : datos) {
                    System.out.print(campo + " | ");
                }
                System.out.println();
            }

            /*
             * COMPROBACIÓN: los ficheros tienen líneas como
             *     Ana,García,"Calle Mayor 15; 2ºA",24
             *     Pablo;Navarro;"Calle Valencia 12; 2ºB";35
             * Fíjate en que el campo DIRECCIÓN va entrecomillado y contiene
             * un ';'. Aun así obtenemos 4 campos (nombre, apellido,
             * dirección, edad) porque OpenCSV respeta las comillas.
             * Con split() el primer fichero daría 4 campos y el segundo 5.
             */

        } catch (IOException | CsvValidationException e) {
            /*
             * OpenCSV añade su propia excepción CHECKED: CsvValidationException,
             * que readNext() declara con "throws". Por eso el catch debe
             * incluirla además de IOException. Si solo pusiéramos IOException,
             * el programa no compilaría.
             */
            System.out.println("Error al leer los ficheros CSV: " + e.getMessage());
        }
    }
}
