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

/**
 * ============================================================================
 * EJERCICIO 7 - Convertir un CSV de ',' a ';'
 * ============================================================================
 *
 * ENUNCIADO:
 *   'productos.csv' utiliza ',' como delimitador. Crear
 *   'productos_puntoycoma.csv' con los MISMOS datos pero usando ';'.
 *   Todo debe hacerse con las funcionalidades de OpenCSV, sin usar el
 *   operador de concatenación "+" para construir las líneas.
 *
 * CÓMO SE RESUELVE:
 *   El truco es NO tratar el fichero como texto. OpenCSV nos da cada registro
 *   ya separado en un String[] de campos, así que:
 *     1. readNext()  -> lee un registro y devuelve sus campos en un array.
 *     2. writeNext() -> escribe ese MISMO array en el fichero de salida.
 *   Como el CSVWriter es el que decide el separador al escribir, el resultado
 *   es idéntico en contenido pero con el delimitador cambiado. Ni una sola
 *   vez se concatenan campos a mano, tal y como pide el enunciado.
 *
 * SOBRE LAS COMILLAS EN EL FICHERO DE SALIDA:
 *   En OpenCSV 5.12.0 el CSVWriter entrecomilla TODOS los campos si le pasamos
 *   DEFAULT_QUOTE_CHARACTER, y el resultado sería "codigo";"nombre";... Para
 *   obtener el formato limpio que se espera usamos NO_QUOTE_CHARACTER. El
 *   contenido de los campos es idéntico en ambos casos: solo cambia el carácter
 *   de comillas, que en un CSV no forma parte del dato.
 *   (Si algún campo contuviera el separador habría que usar comillas para
 *   mantener un CSV válido; con estos datos no es el caso.)
 *
 * ATENCIÓN - DISCREPANCIA ENTRE EL ENUNCIADO Y EL FICHERO REAL:
 *   El enunciado dice que 'productos.csv' usa ',', pero el fichero que hay en
 *   la carpeta 'archivos' usa ';' realmente:
 *       codigo;nombre;precio;stock
 *       P001;Teclado;25.50;15
 *   Por eso los delimitadores están declarados como CONSTANTES abajo. Si el
 *   fichero de tu profesor usa de verdad la coma, solo hay que cambiar
 *   SEPARADOR_ENTRADA a ',' y el programa funciona igual: el resto de la
 *   lógica no cambia. Esto es la ventaja de no dejar el delimitador "escrito"
 *   a lo largo de todo el código.
 */
public class Ejsiete {

    /** Delimitador del fichero de ORIGEN (el que usa productos.csv). */
    private static final char SEPARADOR_ENTRADA = ';';

    /** Delimitador del fichero de DESTINO (el que queremos crear). */
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

        /*
         * TRY-WITH-RESOURCES con los DOS ficheros a la vez: uno en lectura y
         * otro en escritura. Al salir del try ambos quedan cerrados.
         */
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
