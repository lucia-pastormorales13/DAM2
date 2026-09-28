package FicherosTexto_y_Binarios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
 
   Solicita al usuario nombre y nota de 5 alumnos y los guarda en 'notas.txt'.
   Cada alumno ocupa una línea con el formato:  "Nombre: Ana - Nota: 8.5"
   El fichero debe ABRIRSE BORRANDO su contenido anterior (sobrescribir) y,
   al terminar, hay que informar de que se ha creado/actualizado correctamente.
 */
public class Ejdos {

    private static final int NUM_ALUMNOS = 5;

    private static final String RUTA = "archivos" + File.separator + "notas.txt";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        File fichero = new File(RUTA);

       
        try (
                FileWriter fw = new FileWriter(fichero, false);
                BufferedWriter escritor = new BufferedWriter(fw)) {

            for (int i = 1; i <= NUM_ALUMNOS; i++) {

                System.out.print("Nombre del alumno " + i + ": ");
               
                String nombre = sc.nextLine().trim();

                System.out.print("Nota del alumno " + i + ": ");

            
                String notaTexto = sc.nextLine().trim().replace(',', '.');
                double nota = Double.parseDouble(notaTexto);

                // Aplicamos el formato exacto que pide el enunciado.
                escritor.write("Nombre: " + nombre + " - Nota: " + nota);
                escritor.newLine();
            }

        
        } catch (IOException e) {
            System.out.println("Error al escribir el fichero: " + e.getMessage());
            return; // Salimos para no mostrar un mensaje de éxito falso.
        }

        // Solo llegamos aquí si el try terminó sin excepciones.
        System.out.println("El fichero " + fichero.getName() + " se ha creado/actualizado correctamente.");
    }
}
