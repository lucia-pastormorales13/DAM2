package FicherosTexto_y_Binarios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;

/*
  ENUNCIADO:
   1. Pedir nombre y edad de 3 personas.
   2. Escribirlos en el fichero binario 'edades.dat'.
   3. Cerrar el fichero.
   4. Volver a abrirlo.
   5. Leer los tres nombres y las tres edades.
   6. Mostrarlos y calcular su media.
   En ningún momento se debe usar un Writer ni un Reader, porque el fichero
   es binario.
 */
public class Ejcuatro {

    private static final int NUM_PERSONAS = 3;

    private static final String RUTA = "archivos" + File.separator + "edades.dat";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        String[] nombres = new String[NUM_PERSONAS];
        int[] edades = new int[NUM_PERSONAS];

        for (int i = 0; i < NUM_PERSONAS; i++) {
            System.out.print("Nombre de la persona " + (i + 1) + ": ");
            nombres[i] = sc.nextLine().trim();

            System.out.print("Edad de la persona " + (i + 1) + ": ");
            edades[i] = Integer.parseInt(sc.nextLine().trim());
        }

        escribirBinario(new File(RUTA), nombres, edades);

        leerYMostrar(new File(RUTA));
    }

    private static void escribirBinario(File fichero, String[] nombres, int[] edades) {
        try (
                FileOutputStream fos = new FileOutputStream(fichero);
                DataOutputStream salida = new DataOutputStream(fos)) {

            for (int i = 0; i < nombres.length; i++) {

                salida.writeUTF(nombres[i]);
                salida.writeInt(edades[i]);
            }

            // Al salir del try se hace flush y se cierran ambos flujos.

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero binario: " + e.getMessage());
        }
    }

    // Lee el fichero binario, muestra los datos y calcula la media de edades.
    private static void leerYMostrar(File fichero) {

        int sumaEdades = 0;
        int personasLeidas = 0;

        try (
                FileInputStream fis = new FileInputStream(fichero);
                DataInputStream entrada = new DataInputStream(fis)) {

            System.out.println("\n===== Datos recuperados de " + fichero.getName() + " =====");
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
                System.out.printf("Media de las edades:", media);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el fichero binario: " + e.getMessage());
        }

    }
}
