package FicherosCSV_conOpenCSV;

//import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ============================================================================
 * EJERCICIO 6 - Crear un archivo CSV a partir de datos del teclado
 * ============================================================================
 *
 * ENUNCIADO:
 *   Pedir por teclado nombre, apellido, edad y nota de 3 alumnos y guardarlos
 *   en 'alumnos.csv' usando OpenCSV, con ',' como delimitador y una primera
 *   línea con los nombres de las columnas:
 *       nombre,apellido,edad,nota
 *   Se debe usar writeNext() y NO concatenar los campos con el operador "+".
 *
 * CLASE UTILIZADA:
 *   - CSVWriter : clases de OpenCSV para ESCRIBIR CSV.
 *                 new CSVWriter(fw) usa ',' como separador, '"' como carácter
 *                 de comillas y salta de línea al final de cada registro.
 *   - writeNext(String[]) : escribe UN REGISTRO (una fila) del CSV. Recibe
 *                 un array con los campos ya separados, así que OpenCSV se
 *                 encarga de poner el separador entre ellos. Es el equivalente
 *                 a escribir la línea, pero gestionando las comillas.
 *                 Ejemplo: writeNext(new String[]{"Ana","García","20","8.5"})
 *                 produce la línea   Ana,García,20,8.5
 *
 * ERROR A EVITAR (el que tenía mi primera versión):
 *   Pedía los datos por teclado y los guardaba en arrays, pero luego escribía
 *   filas inventadas ("Carlos,21,6.75"). El enunciado pide guardar LOS DATOS
 *   INTRODUCIDOS por el usuario, así que el bucle de escritura debe leer de los
 *   arrays realmente rellenados con lo que escribió el usuario.
 */
public class Ejseis {

    private static final int NUM_ALUMNOS = 3;

    private static final String RUTA = "archivos" + File.separator + "alumnos.csv";

    public static void main(String[] args) {

        /*
         * Scanner para leer lo que el usuario escribe por teclado.
         *
         * Indicamos UTF-8 explícitamente porque los ficheros se guardan en
         * UTF-8, y así los acentos y la 'ñ' se leen y se guardan bien.
         */
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        File fichero = new File(RUTA);

        // Cabecera del CSV: los nombres de las columnas.
        String[] cabecera = { "nombre", "apellido", "edad", "nota" };

        /*
         * TRY-WITH-RESOURCES.
         *
         * Usamos el constructor completo del CSVWriter en lugar de
         * new CSVWriter(fw) para dejar TODO explícito:
         *   fw                  -> fichero de destino
         *   ','                 -> carácter separador (el enunciado pide ',')
         *   NO_QUOTE_CHARACTER  -> sin comillas
         *   NO_ESCAPE_CHARACTER -> sin carácter de escape
         *   DEFAULT_LINE_END    -> salto de línea
         *
         * ¿POR QUÉ NO_QUOTE_CHARACTER Y NO DEFAULT_QUOTE_CHARACTER?
         * En la versión de OpenCSV que usamos (5.12.0) el CSVWriter pone
         * COMILLAS alrededor de TODOS los campos por defecto, y el resultado
         * sería:
         *     "nombre","apellido","edad","nota"
         * que no es el formato que muestra el enunciado. Pasando
         * NO_QUOTE_CHARACTER el resultado es el esperado:
         *     nombre,apellido,edad,nota
         * AVISO: la contrapartida es que si algún dato contuviera el separador
         * (por ejemplo un nombre con coma) habría que entrecomillarlo; en ese
         * caso se usaría CSVWriter.DEFAULT_QUOTE_CHARACTER.
         */
        try (
                FileWriter fw = new FileWriter(fichero);
                CSVWriter escritor = new CSVWriter(fw, ',',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.NO_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)
        ) {

            // Primera línea: nombres de las columnas.
            escritor.writeNext(cabecera);

            // Pedimos los datos del alumno y los escribimos en una fila.
            for (int i = 1; i <= NUM_ALUMNOS; i++) {

                System.out.println("--- Alumno " + i + " ---");

                System.out.print("Nombre: ");
                String nombre = sc.nextLine().trim();

                System.out.print("Apellido: ");
                String apellido = sc.nextLine().trim();

                System.out.print("Edad: ");
                // nextLine() + parseo manual para aceptar "20" y no depender de
                // que Scanner.nextInt() se coma la línea siguiente.
                int edad = Integer.parseInt(sc.nextLine().trim());

                System.out.print("Nota: ");
                // El replace(',', '.') permite escribir la nota como 8.5 o 8,5
                // (en Java el separador decimal siempre es el punto).
                double nota = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));

                /*
                 * writeNext() recibe un String[] con los campos del registro.
                 * No hay concatenación con "+": OpenCSV coloca el separador
                 * entre los campos y añade el salto de línea.
                 */
                escritor.writeNext(new String[] {
                        nombre,
                        apellido,
                        String.valueOf(edad),
                        String.valueOf(nota)
                });
            }

            // Al salir del try se hace flush y se cierran ambos flujos.

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero CSV: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.out.println("Error: se esperaba un número en la edad o en la nota.");
            return;
        }

        System.out.println("El fichero " + fichero.getName() + " se ha creado correctamente.");
        System.out.println("Contenido:");
        System.out.println("  nombre,apellido,edad,nota");
        System.out.println("  ...una fila por cada alumno introducido...");
    }
}
