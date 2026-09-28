package FicherosTexto_y_Binarios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

/**
 * ============================================================================
 * EJERCICIO 4 - Escritura y lectura de un fichero BINARIO
 * ============================================================================
 *
 * ENUNCIADO:
 *   1. Pedir nombre y edad de 3 personas.
 *   2. Escribirlos en el fichero binario 'edades.dat'.
 *   3. Cerrar el fichero.
 *   4. Volver a abrirlo.
 *   5. Leer los tres nombres y las tres edades.
 *   6. Mostrarlos y calcular su media.
 *   En ningún momento se debe usar un Writer ni un Reader, porque el fichero
 *   es binario.
 *
 * POR QUÉ CAMBIAN LAS CLASES RESPECTO A LOS FICHEROS DE TEXTO:
 *   Un fichero de TEXTO guarda CARACTERES legibles, por eso usamos
 *   FileReader/FileWriter + BufferedReader/BufferedWriter.
 *   Un fichero BINARIO guarda una secuencia de BYTES que no se corresponde con
 *   texto legible (imágenes, audio, o aquí: int y String en formato binario).
 *   Por eso se usan clases distintas:
 *
 *   - FileOutputStream : abre el fichero binario para ESCRIBIR bytes.
 *                        write(int) escribe un byte. También admite un
 *                        boolean de append, igual que FileWriter.
 *   - FileInputStream  : abre el fichero binario para LEER bytes.
 *                        read() devuelve el byte leído o -1 al final.
 *   - DataOutputStream : envolviendo al anterior, permite escribir tipos de
 *                        datos de Java directamente:
 *                          writeUTF(String), writeInt(int), writeDouble(double),
 *                          writeBoolean(boolean), writeChar(char)
 *   - DataInputStream  : permite leer esos mismos tipos:
 *                          readUTF(), readInt(), readDouble(), ...
 *
 * NOTA SOBRE EL ENUNCIADO:
 *   El enunciado dice "escribir ... utilizando FileOutputStream y
 *   DataInputStream". Es un error tipográfico: para ESCRIBIR los datos
 *   primitivos hay que usar DataOutputStream, porque DataInputStream solo
 *   sirve para leer. Por eso aquí se escriben con FileOutputStream +
 *   DataOutputStream y se leen con FileInputStream + DataInputStream.
 *
 * REGLA FUNDAMENTAL AL LEER:
 *   Los datos deben leerse en el MISMO ORDEN y con el MISMO TIPO en que se
 *   escribieron. Si al escribir alternamos String/int y al leer hacemos
 *   readInt() donde había un readUTF(), el fichero quedaría desincronizado.
 */
public class Ejcuatro {

    /** Número de personas que se piden y se guardan. */
    private static final int NUM_PERSONAS = 3;

    private static final String RUTA = "archivos" + File.separator + "edades.dat";

    public static void main(String[] args) {

        // UTF-8 explícito para que los nombres con acentos y 'ñ' se guarden bien.
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        // Pedimos los datos y los guardamos en arrays para tenerlos en memoria.
        String[] nombres = new String[NUM_PERSONAS];
        int[] edades = new int[NUM_PERSONAS];

        for (int i = 0; i < NUM_PERSONAS; i++) {
            System.out.print("Nombre de la persona " + (i + 1) + ": ");
            nombres[i] = sc.nextLine().trim();

            System.out.print("Edad de la persona " + (i + 1) + ": ");
            edades[i] = Integer.parseInt(sc.nextLine().trim());
        }

        // PASO 2 y 3: escribir en el fichero binario y cerrarlo.
        escribirBinario(new File(RUTA), nombres, edades);

        // PASO 4, 5 y 6: reabrir, leer, mostrar y calcular la media.
        leerYMostrar(new File(RUTA));
    }

    /**
     * Escribe los nombres y las edades en el fichero binario.
     *
     * @param fichero fichero 'edades.dat'
     * @param nombres array con los nombres
     * @param edades  array con las edades
     */
    private static void escribirBinario(File fichero, String[] nombres, int[] edades) {

        /*
         * Flujo "en capas", como en una tubería:
         *   DataOutputStream  ->  FileOutputStream
         * El de arriba (DataOutputStream) es el que entiende writeUTF/writeInt;
         * el de abajo (FileOutputStream) es quien realmente abre y escribe los
         * bytes en el disco. En el try-with-resources se declara primero el
         * de arriba, y Java cierra en orden inverso.
         */
        try (
                FileOutputStream fos = new FileOutputStream(fichero);
                DataOutputStream salida = new DataOutputStream(fos)
        ) {

            for (int i = 0; i < nombres.length; i++) {
                /*
                 * writeUTF(String) escribe la cadena junto con su longitud en
                 * 2 bytes, para que al leer sepas exactamente dónde acaba.
                 * writeInt(int) escribe un entero de 4 bytes.
                 */
                salida.writeUTF(nombres[i]);
                salida.writeInt(edades[i]);
            }

            // Al salir del try se hace flush y se cierran ambos flujos.

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero binario: " + e.getMessage());
        }
    }

    /**
     * Lee el fichero binario, muestra los datos y calcula la media de edades.
     *
     * @param fichero fichero 'edades.dat'
     */
    private static void leerYMostrar(File fichero) {

        // Acumulador para la media. Se declara fuera del try para poder usarlo
        // también al calcular el resultado fuera del bloque.
        int sumaEdades = 0;
        int personasLeidas = 0;

        try (
                FileInputStream fis = new FileInputStream(fichero);
                DataInputStream entrada = new DataInputStream(fis)
        ) {

            System.out.println("\n===== Datos recuperados de " + fichero.getName() + " =====");

            /*
             * Leemos EXACTAMENTE en el mismo orden y con los mismos tipos que
             * usamos al escribir: primero el String, después el int.
             */
            for (int i = 0; i < NUM_PERSONAS; i++) {
                String nombre = entrada.readUTF();
                int edad = entrada.readInt();

                System.out.println("Nombre: " + nombre + " - Edad: " + edad);

                sumaEdades += edad;
                personasLeidas++;
            }

            // Evitamos la división por cero si el fichero estuviera vacío.
            if (personasLeidas > 0) {
                double media = (double) sumaEdades / personasLeidas;
                /*
                 * %.2f muestra el resultado con 2 decimales.
                 * Indicamos Locale.US porque printf usa el separador decimal
                 * del idioma del sistema: en español saldría 33,33, y aquí
                 * queremos el punto (33.33) como en el resto de ejercicios.
                 */
                System.out.printf(Locale.US, "Media de las edades: %.2f%n", media);
            }

        } catch (IOException e) {
            /*
             * Si el fichero no existe o está corrupto salta IOException.
             * EOFException es subclase de IOException: saldría si se intentara
             * leer más datos de los realmente escritos.
             */
            System.out.println("Error al leer el fichero binario: " + e.getMessage());
        }
    }
}
