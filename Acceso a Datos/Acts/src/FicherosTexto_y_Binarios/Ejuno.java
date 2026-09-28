package FicherosTexto_y_Binarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * ============================================================================
 * EJERCICIO 1 - Lectura de un fichero de texto
 * ============================================================================
 *
 * ENUNCIADO:
 *   Lee el contenido del fichero 'alumnos.txt' utilizando FileReader y
 *   BufferedReader, muestra su contenido por consola y, al final, indica:
 *     - Número total de líneas del fichero.
 *     - Número total de caracteres leídos, sin contar los saltos de línea.
 *   Las excepciones de apertura/lectura se controlan con try-with-resources.
 *
 * CLASES DE LA TEORÍA (UF01, apartado "Lectura de ficheros de texto plano"):
 *   - FileReader    : abre el fichero de TEXTO y lee carácter a carácter.
 *                     Su método read() devuelve el int del carácter leído o -1
 *                     cuando llega al final del fichero.
 *   - BufferedReader: envuelve al anterior con un BÚFER de memoria, lo que hace
 *                     la lectura mucho más eficiente, y aporta readLine(), que
 *                     devuelve la línea leída o null al final del fichero.
 *
 * POR QUÉ try-with-resources:
 *   Cada fichero abierto consume recursos del sistema operativo. Si no se
 *   cierran, se agotan los descriptores, se bloquean ficheros o se pierde
 *   información a medio escribir. El bloque try-with-resources garantiza el
 *   cierre AUTOMÁTICO al salir del try, tanto si el código termina bien como si
 *   salta una excepción. Solo admite objetos Closeable (FileReader, CSVReader,
 *   etc.) y esos objetos solo son visibles DENTRO del try.
 */
public class Ejuno {

    /**
     * File.separator devuelve el separador de rutas del sistema operativo
     * actual ('\' en Windows y '/' en Linux/macOS). Gracias a esto el programa
     * es válido en cualquier sistema, en lugar de escribir la ruta a mano.
     */
    private static final String RUTA = "archivos" + File.separator + "alumnos.txt";

    public static void main(String[] args) {

        // Objeto File: solo REPRESENTA la ruta y permite consultar propiedades
        // (exists(), isFile(), length()...). NO lee ni escribe contenido.
        File fichero = new File(RUTA);

        // Comprobamos que el fichero existe ANTES de intentar abrirlo, para poder
        // dar un mensaje de error útil en lugar de una excepción fea.
        if (!fichero.exists()) {
            System.out.println("No se ha encontrado el fichero: " + fichero.getAbsolutePath());
            System.out.println("Ejecuta el programa desde la raíz del proyecto (Acceso a Datos\\Acts).");
            return;
        }

        // Contadores que pide el enunciado.
        int totalLineas = 0;
        int totalCaracteres = 0;

        /*
         * TRY-WITH-RESOURCES:
         * Declaramos los dos flujos (FileReader "crudo" y el BufferedReader que
         * lo envuelve) dentro de los paréntesis del try.
         *
         * El orden de cierre es INVERSO al de apertura: primero se cierra el
         * BufferedReader y después el FileReader. Java lo hace solo al salir del
         * bloque, tanto en caso normal como si ocurre una excepción.
         *
         * ¿Por qué los dos? FileReader aporta la conexión con el fichero y
         * BufferedReader aporta la eficiencia y el método readLine().
         */
        try (
                FileReader fr = new FileReader(fichero);
                BufferedReader lector = new BufferedReader(fr)
        ) {

            System.out.println("Contenido de " + fichero.getName() + ":");
            System.out.println("------------------------------------");

            String linea;

            /*
             * Patrón clásico para leer ficheros de texto línea a línea:
             * la condición del while ASIGNA el resultado de readLine() a 'linea'
             * y compara con null. readLine() devuelve null SOLO cuando se ha
             * terminado el fichero, así que el bucle termina solo.
             */
            while ((linea = lector.readLine()) != null) {

                // Mostramos la línea tal cual, sin el salto de línea (readLine
                // no lo incluye, por eso println añade el salto al mostrar).
                System.out.println(linea);

                totalLineas++;

                /*
                 * readLine() nos devuelve la línea SIN el carácter de fin de
                 * línea, así que linea.length() ya es el número de caracteres
                 * "reales" de esa línea. Sumándolos obtenemos el total pedido
                 * sin contar los saltos de línea.
                 */
                totalCaracteres += linea.length();
            }

            System.out.println("------------------------------------");
            System.out.println("Número total de líneas: " + totalLineas);
            System.out.println("Número total de caracteres (sin saltos de línea): " + totalCaracteres);

        } catch (IOException e) {
            /*
             * IOException es la excepción que lanzan las clases de java.io.
             * Con try-with-resources el cierre ya está garantizado, así que
             * aquí solo hay que informar del problema.
             */
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }
}
