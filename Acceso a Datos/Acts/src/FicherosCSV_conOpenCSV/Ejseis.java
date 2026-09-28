package FicherosCSV_conOpenCSV;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
    Pedir por teclado nombre, apellido, edad y nota de 3 alumnos y guardarlos
    en 'alumnos.csv' usando OpenCSV, con ',' como delimitador y una primera
    línea con los nombres de las columnas:
        nombre,apellido,edad,nota
    Se debe usar writeNext() y NO concatenar los campos con el operador "+".
 */
public class Ejseis {

    private static final int NUM_ALUMNOS = 3;

    private static final String RUTA = "archivos" + File.separator + "alumnos.csv";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        File fichero = new File(RUTA);
        String[] cabecera = { "nombre", "apellido", "edad", "nota" };

        try (
                FileWriter fw = new FileWriter(fichero);
                CSVWriter escritor = new CSVWriter(fw, ',',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.NO_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)) {

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

                int edad = Integer.parseInt(sc.nextLine().trim());

                System.out.print("Nota: ");
                // El replace(',', '.') permite escribir la nota como 8.5 o 8,5
                double nota = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                escritor.writeNext(new String[] {
                        nombre,
                        apellido,
                        String.valueOf(edad),
                        String.valueOf(nota)
                });
            }

            // Al salir del try se hace flush y se cierran ambos flujos.

        } catch (IOException e) {
            sc.close();
            System.out.println("Error al escribir el fichero CSV: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            sc.close();
            System.out.println("Error: se esperaba un número en la edad o en la nota.");
            return;
        }

        System.out.println("El fichero " + fichero.getName() + " se ha creado correctamente.");
        System.out.println("Contenido:");
        System.out.println("  nombre,apellido,edad,nota");
        System.out.println("  ...una fila por cada alumno introducido...");

        sc.close();
    }
}
