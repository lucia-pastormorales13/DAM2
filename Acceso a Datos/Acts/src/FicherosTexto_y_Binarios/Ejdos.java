package FicherosTexto_y_Binarios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ============================================================================
 * EJERCICIO 2 - Crear y escribir un fichero de texto
 * ============================================================================
 *
 * ENUNCIADO:
 *   Solicita al usuario nombre y nota de 5 alumnos y los guarda en 'notas.txt'.
 *   Cada alumno ocupa una línea con el formato:  "Nombre: Ana - Nota: 8.5"
 *   El fichero debe ABRIRSE BORRANDO su contenido anterior (sobrescribir) y,
 *   al terminar, hay que informar de que se ha creado/actualizado correctamente.
 *
 * CLASES DE LA TEORÍA (UF01, apartado "Escritura de ficheros de texto plano"):
 *   - FileWriter    : abre el fichero de TEXTO para ESCRIBIR caracteres.
 *                     Su constructor admite un boolean extra que decide el modo:
 *                       false -> sobrescribe (borra lo que hubiera)
 *                       true  -> append (añade al final)
 *                     Si el fichero no existe, lo crea automáticamente.
 *   - BufferedWriter: envuelve al anterior con un BÚFER de memoria (escrituras
 *                     más eficientes) y aporta los dos métodos que usamos:
 *                       write(String) -> escribe texto
 *                       newLine()     -> inserta un salto de línea
 */
public class Ejdos {

    /** Número de alumnos que hay que pedir por teclado. */
    private static final int NUM_ALUMNOS = 5;

    private static final String RUTA = "archivos" + File.separator + "notas.txt";

    public static void main(String[] args) {

        /*
         * Scanner para leer lo que el usuario escribe por teclado.
         * Indicamos UTF-8 explícitamente para que los acentos y la 'ñ' se
         * guarden bien en el fichero.
         */
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        File fichero = new File(RUTA);

        /*
         * new FileWriter(fichero, false)
         * El 'false' es el parámetro "append" de la teoría:
         *   false = sobrescribir el fichero (modo por defecto).
         *   true  = añadir al final.
         * El enunciado pide sobrescribir, por eso usamos false.
         */
        try (
                FileWriter fw = new FileWriter(fichero, false);
                BufferedWriter escritor = new BufferedWriter(fw)
        ) {

            for (int i = 1; i <= NUM_ALUMNOS; i++) {

                System.out.print("Nombre del alumno " + i + ": ");
                // nextLine() lee la línea COMPLETA, incluidos los espacios.
                String nombre = sc.nextLine().trim();

                System.out.print("Nota del alumno " + i + ": ");

                /*
                 * Leemos la nota como texto y la convertimos con Double.parseDouble.
                 *
                 * El replace(',', '.') es una precaución: si el teclado está en
                 * español se escribe 8,5 y Double.parseDouble("8,5") fallaría
                 * porque en Java el separador decimal SIEMPRE es el punto. Con
                 * esta sustitución se aceptan las dos formas: 8.5 y 8,5.
                 */
                String notaTexto = sc.nextLine().trim().replace(',', '.');
                double nota = Double.parseDouble(notaTexto);

                // Aplicamos el formato exacto que pide el enunciado.
                escritor.write("Nombre: " + nombre + " - Nota: " + nota);

                // newLine() escribe el salto de línea de forma portable
                // (no como un "\n" fijo, que no funciona igual en Windows).
                escritor.newLine();
            }

            /*
             * Al salir del try, el try-with-resources hace flush() del búfer y
             * cierra ambos flujos. Por eso los datos quedan realmente escritos
             * en el disco (si faltara el cierre, el búfer se perdería).
             */

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero: " + e.getMessage());
            return; // Salimos para no mostrar un mensaje de éxito falso.
        }

        // Solo llegamos aquí si el try terminó sin excepciones.
        System.out.println("El fichero " + fichero.getName() + " se ha creado/actualizado correctamente.");
    }
}
