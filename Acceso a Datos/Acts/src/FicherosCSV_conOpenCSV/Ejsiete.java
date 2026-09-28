package FicherosCSV_conOpenCSV;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
    'productos.csv' utiliza ',' como delimitador. Crear
    'productos_puntoycoma.csv' con los MISMOS datos pero usando ';'.
    Todo debe hacerse con las funcionalidades de OpenCSV, sin usar el
    operador de concatenación "+" para construir las líneas.*/
public class Ejsiete {

 
    private static final char SEPARADOR_ENTRADA = ';';
    private static final char SEPARADOR_SALIDA = ';';

    private static final String RUTA_ENTRADA = "archivos" + File.separator + "productos.csv";
    private static final String RUTA_SALIDA = "archivos" + File.separator + "productos_puntoycoma.csv";

    public static void main(String[] args) {

        File ficheroEntrada = new File(RUTA_ENTRADA);
        File ficheroSalida = new File(RUTA_SALIDA);

        if (!ficheroEntrada.exists()) {
            System.out.println("No se ha encontrado: " + ficheroEntrada.getAbsolutePath());
            return;
        }

        // Parser configurado con el delimitador que usa el fichero de origen.
        ICSVParser parserEntrada = new CSVParserBuilder()
                .withSeparator(SEPARADOR_ENTRADA)
                .build();

        try (
                FileReader fr = new FileReader(ficheroEntrada);
                FileWriter fw = new FileWriter(ficheroSalida);
                CSVReader lector = new CSVReaderBuilder(fr)
                        .withCSVParser(parserEntrada)
                        .build();
                CSVWriter escritor = new CSVWriter(fw, SEPARADOR_SALIDA,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.NO_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)
        ) {

            int registros = 0;

            /*
             * readNext() devuelve el registro como String[] de campos, o null
             * al llegar al final del fichero.
             */
            String[] campos;

            while ((campos = lector.readNext()) != null) {

                /*
                 * Le pasamos a writeNext() el array TAL cual nos lo ha dado
                 * OpenCSV. El CSVWriter pone el separador de salida entre los
                 * campos, así que no hay que construir la línea a mano.
                 */
                escritor.writeNext(campos);

                registros++;

                // Solo para verlo por consola, no es necesario para el ejercicio.
                System.out.println("Registro " + registros + " convertido: " + String.join(" ", campos));
            }

            System.out.println();
            System.out.println("Se han convertido " + registros + " registros.");
            System.out.println("Fichero creado: " + ficheroSalida.getName()
                    + " (delimitador '" + SEPARADOR_SALIDA + "')");

        } catch (IOException | CsvValidationException e) {
            /*
             * CsvValidationException es la excepción checked que declara
             * readNext() en OpenCSV 5.x, además de IOException.
             */
            System.out.println("Error al convertir el fichero CSV: " + e.getMessage());
        }
    }
}
