package FicherosTexto_y_Binarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * ENUNCIADO:
 *   Registra incidencias de un sistema en 'incidencias.txt'. Cada ejecución
 *   debe AÑADIR la nueva incidencia al final SIN borrar las anteriores.
 *   Cada línea lleva su número, por ejemplo:
 *       "Incidencia 4: Disco duro lleno"
 *   El número se calcula AUTOMÁTICAMENTE a partir de las ya existentes.
 */
public class Ejtres {

    private static final String RUTA = "archivos" + File.separator + "incidencias.txt";

    /** Prefijo que marca el inicio de cada línea de incidencias.txt. */
    private static final String PREFIJO = "Incidencia ";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        File fichero = new File(RUTA);       
        int ultimoNumero = leerUltimoNumero(fichero);

        
        // Comprobamos si el fichero YA termina en salto de línea.
        boolean necesitaSalto = !terminaConSaltoDeLinea(fichero);

        System.out.println("Incidencias registradas hasta ahora: " + ultimoNumero);
        System.out.print("Descripción de la nueva incidencia: ");
        String descripcion = sc.nextLine().trim();

        // El número de la nueva incidencia es el anterior + 1.
        int nuevoNumero = ultimoNumero + 1;

        
        //PASO 2: escribir al FINAL del fichero.
        
        try (
                FileWriter fw = new FileWriter(fichero, true);
                BufferedWriter escritor = new BufferedWriter(fw)
        ) {

            // Cerramos la línea anterior si estaba abierta.
            if (necesitaSalto) {
                escritor.newLine();
            }

            escritor.write(PREFIJO + nuevoNumero + ": " + descripcion);
            escritor.newLine();

            // Al salir del try se hace flush y se cierra: los datos llegan al disco.

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero: " + e.getMessage());
            return;
        }

        System.out.println("Incidencia " + nuevoNumero + " añadida correctamente a " + fichero.getName() + ".");
    }

    
     // Comprueba si el fichero termina con un salto de línea.
     
    private static boolean terminaConSaltoDeLinea(File fichero) {

        if (!fichero.exists() || fichero.length() == 0) {
            return true;
        }

        try {
            byte[] contenido = Files.readAllBytes(fichero.toPath());
            int ultimoByte = contenido[contenido.length - 1];
            return ultimoByte == '\n' || ultimoByte == '\r';
        } catch (IOException e) {
            // Si no se puede comprobar, asumimos que está bien para no
            // intercalar saltos de línea por error.
            return true;
        }
    }

    
    private static int leerUltimoNumero(File fichero) {

        // Si el fichero todavía no existe no hay nada que leer: empezamos en 0.
        if (!fichero.exists()) {
            return 0;
        }

        int maximo = 0;

        try (
                FileReader fr = new FileReader(fichero);
                BufferedReader lector = new BufferedReader(fr)
        ) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                // Solo nos interesan las líneas con el formato esperado.
                if (!linea.startsWith(PREFIJO)) {
                    continue;
                }

                
                 // Buscamos el primer ':' , que es donde termina el número.
                int posColon = linea.indexOf(':');

                if (posColon > PREFIJO.length()) {
                    try {
                        int numero = Integer.parseInt(
                                linea.substring(PREFIJO.length(), posColon).trim());
                        if (numero > maximo) {
                            maximo = numero;
                        }
                    } catch (NumberFormatException e) {
                        // La línea no tenía un número válido: la ignoramos y
                        // seguimos con la siguiente en lugar de abortar el programa.
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Aviso: no se ha podido leer " + fichero.getName()
                    + " (" + e.getMessage() + "). Se empezará a numerar desde 1.");
        }

        return maximo;
    }
}
